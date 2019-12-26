package core;

import java.util.Map;

/**
 * Q-Table which saves all seen states, all available actions for each state
 * and their value (state-action values/ action values).
 *
 * @param <A>
 */
public interface StateActionTable<A extends Enum> {
    double DEFAULT_VALUE = 0.0;

    double getValue(State state, A action);
    void setValue(State state, A action, double value);
    int getStateCount();
    Map<A, Double> getActionValues(State state);
}
