package com.luxvelocitas.tinyfsm;

import java.util.HashMap;
import java.util.Map;
import com.luxvelocitas.tinydatautils.Pair;
import com.luxvelocitas.tinyevent.ITinyEventDispatcher;
import com.luxvelocitas.tinyevent.ITinyEventListener;
import com.luxvelocitas.tinyevent.SimpleTinyEventDispatcher;
import org.slf4j.Logger;

/**
 */
public class TinyStateMachine<S extends Enum, E extends Enum, D> implements ITinyStateMachine<S, E, D> {
    protected Logger mLogger;
    protected boolean mDebugMode;
    protected S mStartingState;
    protected S mCurrentState;
    protected Map<Pair<S,E>, S> mStateMap;
    protected ITinyEventDispatcher<E, D> mOnEventDispatcher;
    protected ITinyEventDispatcher<S, D> mOnEnterStateEventDispatcher;
    protected ITinyEventDispatcher<S, D> mOnExitStateEventDispatcher;

    public TinyStateMachine(Logger logger, S startingState) {
        mLogger = logger;
        mDebugMode = false;
        mStartingState = startingState;
        mCurrentState = startingState;
        mStateMap = new HashMap<Pair<S, E>, S>();
        mOnEventDispatcher = new SimpleTinyEventDispatcher<E, D>();
        mOnEnterStateEventDispatcher = new SimpleTinyEventDispatcher<S, D>();
        mOnExitStateEventDispatcher = new SimpleTinyEventDispatcher<S, D>();
    }

    @Override
    public void setDebugMode(boolean debugMode) {
        mDebugMode = debugMode;
    }

    @Override
    public boolean isDebugMode() {
        return mDebugMode;
    }

    @Override
    public S getCurrentState() {
        return mCurrentState;
    }

    @Override
    public ITinyStateMachine<S, E, D> transition(S fromState, E eventType, S toState) {
        Pair<S,E> key = new Pair(fromState, eventType);
        mStateMap.put(key, toState);
        return this;
    }

    @Override
    public ITinyStateMachine<S, E, D> trigger(E eventType) {
        return trigger(eventType, null);
    }

    @Override
    public ITinyStateMachine<S, E, D> trigger(E eventType, D eventData) {
        if (mDebugMode) {
            mLogger.debug("trigger: {} x {} ({})", mCurrentState, eventType, eventData);
        }

        // Re-fire the event to any event listeners
        if (eventData == null) {
            mOnEventDispatcher.notify(eventType);
        }
        else {
            mOnEventDispatcher.notify(eventType, eventData);
        }

        Pair<S,E> key = new Pair<S,E>(mCurrentState, eventType);
        S toState = mStateMap.get(key);
        if (toState == null)  {
            if (mDebugMode) {
                mLogger.debug("Not state transition found: {} x {}", mCurrentState, eventType);
            }
            return this;
        }
        return execStateTransition(toState, eventData);
    }

    @Override
    public ITinyStateMachine<S, E, D> restart() {
        if (mDebugMode) {
            mLogger.debug("restart: {}", mStartingState);
        }
        return execStateTransition(mStartingState, null);
    }

    @Override
    public ITinyStateMachine<S, E, D> forceState(S toState) {
        if (mDebugMode) {
            mLogger.debug("force state: {}", toState);
        }
        return execStateTransition(toState, null);
    }

    @Override
    public ITinyStateMachine<S, E, D> onEvent(E event, ITinyEventListener<E, D> listener) {
        mOnEventDispatcher.addListener(event, listener);
        return this;
    }

    @Override
    public ITinyStateMachine<S, E, D> onEnterState(S state, ITinyEventListener<S, D> listener) {
        mOnEnterStateEventDispatcher.addListener(state, listener);
        return this;
    }

    @Override
    public ITinyStateMachine<S, E, D> onExitState(S state, ITinyEventListener<S, D> listener) {
        mOnExitStateEventDispatcher.addListener(state, listener);
        return this;
    }

    protected ITinyStateMachine<S, E, D> execStateTransition(S toState, D eventData) {
        // Exiting current state
        try {
            if (eventData == null) {
                mOnExitStateEventDispatcher.notify(mCurrentState);
            }
            else {
                mOnExitStateEventDispatcher.notify(mCurrentState, eventData);
            }
        }
        catch(StateTransitionException e) {
            // One or more listeners to the exit event returned false,
            // therefore do not complete the state transition
            mLogger.info("StateTransitionException caught, canceling transition to {} -> {}", mCurrentState, toState);
            return this;
        }

        // Set new state
        S oldCurrentState = mCurrentState;
        mCurrentState = toState;

        // Entered new state
        try {
            if (eventData == null) {
                mOnEnterStateEventDispatcher.notify(mCurrentState);
            }
            else {
                mOnEnterStateEventDispatcher.notify(mCurrentState, eventData);
            }
        }
        catch(StateTransitionException e) {
            // One or more listeners to the enter event returned false,
            // therefore do not complete the state transition
            mLogger.info("StateTransitionException caught, canceling transition to {} -> {}", mCurrentState, toState);
            mCurrentState = oldCurrentState;
        }
        finally {
            return this;
        }
    }
}
