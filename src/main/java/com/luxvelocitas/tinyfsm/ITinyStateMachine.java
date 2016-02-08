package com.luxvelocitas.tinyfsm;

/*[TODO]
- Event Driven FSM implementation
- Must be Android compatible

- Basically:
    - Possible States is an enum
        - accepting state(s)?
    - Inputs are Events
        - Possible Events is an enum
            - Enum declares event types
    - Event triggers state transition
    - State has an optional onEnter callback
    - State has an optional onExit callback

- Events
    - Split out to separate "TinyEvent" library

- Configuration
    - How are the mappings configured?
        - S x E -> S
    - Some kind of tabular representation?
        - It must be type-checked at compile time
            - e.g. load JSON maybe later, not as basic way
    - An 2-dim array
*/

import com.luxvelocitas.tinyevent.ITinyEventListener;

/**
 *
 * @param <S>
 * @param <E>
 * @param <D>
 */
public interface ITinyStateMachine<S extends Enum, E extends Enum, D> {
    S getCurrentState();
    ITinyStateMachine<S,E,D> transition(S fromState, E eventType, S toState);
    ITinyStateMachine<S,E,D> trigger(E event);
    ITinyStateMachine<S,E,D> trigger(E event, D eventData);
    ITinyStateMachine<S,E,D> restart();
    ITinyStateMachine<S,E,D> forceState(S state);
    ITinyStateMachine<S,E,D> onEvent(E event, ITinyEventListener<E, D> listener);
    ITinyStateMachine<S,E,D> onEnterState(S state, ITinyEventListener<S, D> listener);
    ITinyStateMachine<S,E,D> onExitState(S state, ITinyEventListener<S, D> listener);
    void setDebugMode(boolean debugMode);
    boolean isDebugMode();
}
