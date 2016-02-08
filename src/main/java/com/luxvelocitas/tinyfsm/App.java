package com.luxvelocitas.tinyfsm;

import com.luxvelocitas.tinydatautils.DataBundle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 */
public class App {
    private static enum AppStates { ST_IDLE, ST_5C, ST_10C, ST_15C, ST_20C, ST_VEND, ST_CHANGE, ST_ERROR };
    private static enum AppEvents { EV_ENTER_5C, EV_ENTER_10C, EV_ENTER_15C, EV_ENTER_20C, EV_CHOOSE, EV_ANY };

    private Logger mLogger;

    public static void main(String[] args) {
        new App().exec(args);
    }

    private void exec(String[] args) {
        mLogger = LoggerFactory.getLogger(App.class);

        //S extends Enum, E extends Enum, D, T extends TinyEvent<E, D>
        // Vending machine where prodcuts all cost 15c
        TinyStateMachine<AppStates, AppEvents, DataBundle> tinyStateMachine =
                new TinyStateMachine(mLogger, AppStates.ST_IDLE);
        tinyStateMachine.transition(AppStates.ST_IDLE, AppEvents.EV_ENTER_5C, AppStates.ST_5C);
        tinyStateMachine.transition(AppStates.ST_IDLE, AppEvents.EV_ENTER_10C, AppStates.ST_10C);
        tinyStateMachine.transition(AppStates.ST_IDLE, AppEvents.EV_ENTER_15C, AppStates.ST_15C);
        tinyStateMachine.transition(AppStates.ST_IDLE, AppEvents.EV_ENTER_20C, AppStates.ST_20C);
        tinyStateMachine.transition(AppStates.ST_5C, AppEvents.EV_CHOOSE, AppStates.ST_ERROR);
        tinyStateMachine.transition(AppStates.ST_10C, AppEvents.EV_CHOOSE, AppStates.ST_ERROR);
        tinyStateMachine.transition(AppStates.ST_15C, AppEvents.EV_CHOOSE, AppStates.ST_VEND);
        tinyStateMachine.transition(AppStates.ST_20C, AppEvents.EV_CHOOSE, AppStates.ST_VEND);
        tinyStateMachine.transition(AppStates.ST_VEND, AppEvents.EV_ANY, AppStates.ST_CHANGE);
    }
}
