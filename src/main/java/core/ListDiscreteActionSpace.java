package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListDiscreteActionSpace<A extends Enum> implements DiscreteActionSpace<A>, Serializable {
    private List<A> actions;

    public ListDiscreteActionSpace(){
        actions = new ArrayList<>();
    }

    public ListDiscreteActionSpace(A... actions){
        this.actions = new ArrayList<>(Arrays.asList(actions));
    }

    @Override
    public void addAction(A action){
        actions.add(action);
    }

    @SafeVarargs
    @Override
    public final void addActions(A... as) {
        actions.addAll(Arrays.asList(as));
    }

    @Override
    public List<A> getAllActions() {
        return actions;
    }

    @Override
    public int getNumberOfActions(){
        return actions.size();
    }
}
