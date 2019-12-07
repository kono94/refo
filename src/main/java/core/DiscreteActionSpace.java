package core;

import java.util.HashSet;
import java.util.Set;

public class DiscreteActionSpace<A extends Enum> implements ActionSpace<A>{
    private Set<Action> actions;

    public DiscreteActionSpace(){
        actions = new HashSet<>();
    }

    @Override
    public void addAction(DiscreteAction<A> action){
        actions.add(action);
    }

    @Override
    public int getNumberOfAction(){
        return actions.size();
    }


    @Override
    public Set<Action> getAllActions(){
        return actions;
    }
}
