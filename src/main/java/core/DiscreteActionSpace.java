package core;

public interface DiscreteActionSpace<A extends Enum> extends Iterable<A> {
    int getNumberOfActions();
    void addAction(A a);
    void addActions(A... as);
}
