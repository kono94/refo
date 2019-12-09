package core;

import java.util.List;

public interface DiscreteActionSpace<A extends Enum> {
    int getNumberOfAction();
    void addAction(A a);
    void addActions(A... as);
    List<A> getAllActions();
}
