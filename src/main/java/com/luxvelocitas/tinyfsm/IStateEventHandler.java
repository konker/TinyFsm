package com.luxvelocitas.tinyfsm;


import com.luxvelocitas.tinyevent.TinyEvent;

public interface IStateEventHandler<S extends Enum, E extends Enum, D> {
    public void onEnter(TinyEvent<E, D> triggerEvent, S state);
    public void onExit(TinyEvent<E, D> triggerEvent, S state);
}
