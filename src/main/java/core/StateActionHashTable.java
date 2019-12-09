package core;

import evironment.antGame.AntAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Premise: All states have the complete action space
 */
public class StateActionHashTable<A extends Enum> implements StateActionTable<A> {

    private final Map<State, Map<A, Double>> table;
    private DiscreteActionSpace<A> discreteActionSpace;

    public StateActionHashTable(DiscreteActionSpace<A> discreteActionSpace){
        table =  new HashMap<>();
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
        Map<A, Double> actionValues = table.get(state);
        if(actionValues == null){
            actionValues = createDefaultActionValues();
        }
        return actionValues;
    }

    public static void main(String[] args) {
        DiscreteActionSpace<AntAction> da = new ListDiscreteActionSpace<>(AntAction.MOVE_RIGHT, AntAction.PICK_UP);
        StateActionTable sat = new StateActionHashTable<>(da);
        State t = new State() {
        };

        System.out.println(sat.getActionValues(t));
    }
    private Map<A, Double> createDefaultActionValues(){
        final Map<A, Double> defaultActionValues = new HashMap<>();
        for(A action: discreteActionSpace.getAllActions()){
            defaultActionValues.put(action, DEFAULT_VALUE);
        }
        return defaultActionValues;
    }
}
