package core;

import java.util.ArrayList;
import java.util.List;

public class DiscreteActionSpace<A extends Enum> implements ActionSpace<A>{
    private List<DiscreteAction<A>> actions;

    public DiscreteActionSpace(){
        actions = new ArrayList<>();
    }

    @Override
    public void addAction(DiscreteAction<A> action){
        actions.add(action);
    }

    @Override
    public void addActions(A[] as) {
        for(A a : as){
            actions.add(new DiscreteAction<>(a));
        }
    }

    @Override
    public int getNumberOfAction(){
        return actions.size();
    }

    public List<DiscreteAction<A>> getAllDiscreteActions(){
        return actions;
    }
}
