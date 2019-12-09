package core;

import java.util.List;

public interface ActionSpace<A extends Enum> {
    int getNumberOfAction();
    void addAction(DiscreteAction<A> a);
    void addActions(A[] as);
}
