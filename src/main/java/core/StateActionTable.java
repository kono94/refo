package core;

import java.util.Map;

public interface StateActionTable {
    double DEFAULT_VALUE = 0.0;

    double getValue(State state, Action action);
    void setValue(State state, Action action, double value);

    Map<Action, Double> getActionValues(State state);
}
