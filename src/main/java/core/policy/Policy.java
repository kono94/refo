package core.policy;

import java.util.Map;

public interface Policy<A extends Enum> {
    A chooseAction(Map<A, Double> actionValues);
}
