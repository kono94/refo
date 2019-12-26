package core;

/**
 * Collection of all available actions for a specific
 * environment. It defines the actions the agent is able
 * to choose from at every timestamp T.
 * Extending from <interface>Iterable</interface> for easy
 * "unmutable" iteration of the action space.
 *
 * @param <A> Actions as defined in an <Enum> based class
 */
public interface DiscreteActionSpace<A extends Enum> extends Iterable<A> {
    int getNumberOfActions();
    void addAction(A a);
    void addActions(A... as);
}
