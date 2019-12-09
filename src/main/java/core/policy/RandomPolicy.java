package core.policy;

import core.RNG;
import java.util.Map;

public class RandomPolicy<A extends Enum> implements Policy<A>{
    @Override
    public A chooseAction(Map<A, Double> actionValues) {
        int idx = RNG.getRandom().nextInt(actionValues.size());
        int i = 0;
        for(A action : actionValues.keySet()){
            if(i++ == idx) return action;
        }

        return null;
    }
}
