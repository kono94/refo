package core.policy;

import java.util.Map;

/**
 * Strategy to choose a specific action available in the agent's current
 * state.
 *
 * @param <A> Enum class of available action in specific environment
 */
public interface Policy<A extends Enum> {
    A chooseAction(Map<A, Double> actionValues);
}
