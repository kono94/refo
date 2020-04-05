package core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of a discrete action space.
 * "Discrete" because actions are exclusively defined as Enum,
 * realized as generic "A".
 *
 * @param <A> Enum class of actions in the specific environment
 */
public class ListDiscreteActionSpace<A extends Enum> implements DiscreteActionSpace<A>, Serializable{
    private static final long serialVersionUID = 1L;
    private List<A> actions;

    public ListDiscreteActionSpace(){
        actions = new ArrayList<>();
    }

    @SafeVarargs
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
