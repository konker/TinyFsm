package fi.hiit.edfsm4j;


public interface IStateEventHandler<E, S> {
    public void onEnter(E triggerEvent, S state);
    public void onExit(E triggerEvent, S state);
}
