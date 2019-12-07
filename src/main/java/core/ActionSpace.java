package core;

import java.util.Set;

public interface ActionSpace<A extends Enum> {
    Set<Action> getAllActions();
    int getNumberOfAction();
    void addAction(DiscreteAction<A> a);
}
