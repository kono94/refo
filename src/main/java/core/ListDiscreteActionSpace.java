package core;

import java.io.Serializable;
import java.util.*;

public class ListDiscreteActionSpace<A extends Enum> implements DiscreteActionSpace<A>, Serializable{
    private static final long serialVersionUID = 1L;
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
    public int getNumberOfActions(){
        return actions.size();
    }

    @Override
    public Iterator<A> iterator() {
        return actions.iterator();
    }
}
