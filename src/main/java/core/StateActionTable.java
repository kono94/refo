package core;

import java.util.Map;

public interface StateActionTable<A extends Enum> {
    double DEFAULT_VALUE = 0.0;

    double getValue(State state, A action);
    void setValue(State state, A action, double value);

    Map<A, Double> getActionValues(State state);
}
