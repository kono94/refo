package core.policy;

import core.RNG;

import java.util.Map;

/**
 * Chooses an action arbitrarily.
 *
 * @param <A> Enum class of available action in specific environment
 */
public class RandomPolicy<A extends Enum> implements Policy<A>{
    @Override
    public A chooseAction(Map<A, Double> actionValues) {
        int idx = RNG.getRandom().nextInt(actionValues.size());
        int i = 0;
        for(A action : actionValues.keySet()){
            if(i++ == idx)  return action;
        }
        return null;
    }
}
