package core;

import java.util.List;

public interface DiscreteActionSpace<A extends Enum> {
    int getNumberOfActions();
    void addAction(A a);
    void addActions(A... as);
    List<A> getAllActions();
}
