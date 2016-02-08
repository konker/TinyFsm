package com.luxvelocitas.tinyfsm;

/**
 */
public class StateTransitionException extends RuntimeException {
    public StateTransitionException(String message, Throwable cause) {
        super(message, cause);
    }
    public StateTransitionException(String message) {
        super(message);
    }
    public StateTransitionException(Throwable cause) {
        super(cause);
    }
}
