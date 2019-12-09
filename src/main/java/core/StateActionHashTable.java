package core;

import java.util.HashMap;
import java.util.Map;

/**
 * Premise: All states have the complete action space
 */
public class StateActionHashTable<A extends Enum> implements StateActionTable {

    private final Map<State, Map<Action, Double>> table;
    private ActionSpace<A> actionSpace;

    public StateActionHashTable(ActionSpace<A> actionSpace){
        table =  new HashMap<>();
        this.actionSpace = actionSpace;
    }

    /*
       If the state is not present in the table at the time of
       calling this method the DEFAULT_VALUE gets returned BUT
       no the missing state is not inserted into the table!

       Inserting of missing states is ONLY done in "setValue()"
       method.
     */
    @Override
    public double getValue(State state, Action action) {
        final Map<Action, Double> actionValues = table.get(state);
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
    public void setValue(State state, Action action, double value) {
        final Map<Action, Double> actionValues;
        if (table.containsKey(state)) {
            actionValues = table.get(state);
        } else {
            actionValues = createDefaultActionValues();
            table.put(state, actionValues);
        }
        actionValues.put(action, value);
    }

    @Override
    public Map<Action, Double> getActionValues(State state) {
        return null;
    }

    private Map<Action, Double> createDefaultActionValues(){
        final Map<Action, Double> defaultActionValues = new HashMap<>();
       // for(Action action: actionSpace.getAllActions()){
       //     defaultActionValues.put(action, DEFAULT_VALUE);
        //}
        return defaultActionValues;
    }
}
