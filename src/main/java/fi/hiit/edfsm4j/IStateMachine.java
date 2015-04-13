package fi.hiit.edfsm4j;

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

//import fi.hiit.tinyevent.TinyEvent;

/**
 *
 * @param <S> State enum
 * @param <E> Event type enum
 */
public interface IStateMachine<S extends Enum, E extends Enum> {
    public void trigger(E event);
    public IStateMachine<S, E> onEnter(S state, IStateEventHandler<S, E> handler);
    public IStateMachine<S, E> onExit(S state, IStateEventHandler<S, E> handler);
}
