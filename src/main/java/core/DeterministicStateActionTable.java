package core;

import java.io.Serializable;
import java.util.*;

import org.apache.commons.collections4.queue.CircularFifoQueue;
/**
 * Premise: All states have the complete action space
 */
public class DeterministicStateActionTable<A extends Enum> implements StateActionTable<A>, Serializable {

    private final Map<State, Map<A, Double>> table;
    private DiscreteActionSpace<A> discreteActionSpace;
    private Queue<Map.Entry<State, Map<A, Double>>> latestChanges;

    public DeterministicStateActionTable(DiscreteActionSpace<A> discreteActionSpace){
        table =  new LinkedHashMap<>();
        this.discreteActionSpace = discreteActionSpace;
        latestChanges = new CircularFifoQueue<>(10);
    }

    /**
     * If the state is not present in the table at the time of
     * calling this method the DEFAULT_VALUE gets returned BUT
     * no the missing state is not inserted into the table!
     *
     * Inserting of missing states is ONLY done in "setValue()"
     * method.
     *
     * @param state given state
     * @param action given action
     * @return estimate value of state-action pair
     */
    @Override
    public double getValue(State state, A action) {
        final Map<A, Double> actionValues = table.get(state);
        if (actionValues != null) {
            return actionValues.get(action);
        }
        return DEFAULT_VALUE;
    }

    /**
     * Update the value of an action for a specific state.
     * If the state is not present in the table yet,
     * it will get stored in combination with every action
     * from the action space initialized with the default value.
     *
     * @param state given state
     * @param action given action
     * @param value new estimate of the state-action pair
     */
    @Override
    public void setValue(State state, A action, double value) {
        final Map<A, Double> actionValues;
        if (table.containsKey(state)) {
            actionValues = table.get(state);
        } else {
            actionValues = createDefaultActionValues();
            table.put(state, actionValues);
        }
        latestChanges.add(new AbstractMap.SimpleEntry<>(state, actionValues));
        actionValues.put(action, value);
    }

    /**
     * @param state given state
     * @return all available action in given state and their corresponding estimated values
     */
    @Override
    public Map<A, Double> getActionValues(State state) {
        if(table.get(state) == null){
            table.put(state, createDefaultActionValues());
        }
        return table.get(state);
    }

    @Override
    public Queue<Map.Entry<State, Map<A, Double>>> getFirstStateEntriesForView() {
        return latestChanges;
    }

    /**
     * @return Map with initial values for every available action
     */
    private Map<A, Double> createDefaultActionValues(){
        final Map<A, Double> defaultActionValues = new LinkedHashMap<>();
        for(A action: discreteActionSpace){
            defaultActionValues.put(action, DEFAULT_VALUE);
        }
        return defaultActionValues;
    }
    @Override
    public int getStateCount(){
        return table.size();
    }
}
