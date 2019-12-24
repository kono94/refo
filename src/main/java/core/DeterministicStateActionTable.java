package core;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Premise: All states have the complete action space
 */
public class DeterministicStateActionTable<A extends Enum> implements StateActionTable<A>, Serializable {

    private final Map<State, Map<A, Double>> table;
    private DiscreteActionSpace<A> discreteActionSpace;

    public DeterministicStateActionTable(DiscreteActionSpace<A> discreteActionSpace){
        table =  new LinkedHashMap<>();
        this.discreteActionSpace = discreteActionSpace;
    }

    /*
       If the state is not present in the table at the time of
       calling this method the DEFAULT_VALUE gets returned BUT
       no the missing state is not inserted into the table!

       Inserting of missing states is ONLY done in "setValue()"
       method.
     */
    @Override
    public double getValue(State state, A action) {
        final Map<A, Double> actionValues = table.get(state);
        if (actionValues != null) {
            return actionValues.get(action);
        }
        return DEFAULT_VALUE;
    }

    /*
       Update the value of an action for a specific state.
       If the state is not present in the table yet,
       it will get stored in combination with every action
       from the action space initialized with the default value.
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
        actionValues.put(action, value);
    }

    @Override
    public Map<A, Double> getActionValues(State state) {
        if(table.get(state) == null){
            table.put(state, createDefaultActionValues());
        }
        return table.get(state);
    }

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
