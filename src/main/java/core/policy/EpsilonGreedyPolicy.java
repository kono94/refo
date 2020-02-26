package core.policy;

import core.RNG;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * To prevent the agent from getting stuck only using the "best" action
 * according to the current learning history, this policy
 * will take random action with the probability of epsilon.
 * (random action space includes the best action as well)
 *
 * @param <A> Discrete Action Enum
 */
public class EpsilonGreedyPolicy<A extends Enum> implements EpsilonPolicy<A>{
    @Setter
    @Getter
    private float epsilon;
    private RandomPolicy<A> randomPolicy;
    private GreedyPolicy<A> greedyPolicy;

    public EpsilonGreedyPolicy(float epsilon){
        this.epsilon = epsilon;
        randomPolicy = new RandomPolicy<>();
        greedyPolicy = new GreedyPolicy<>();
    }

    @Override
    public A chooseAction(Map<A, Double> actionValues) {
        float f = RNG.getRandom().nextFloat();
        if(f < epsilon){
            // Take random action
            return randomPolicy.chooseAction(actionValues);
        }else{
            // Take the action with the highest value
            return greedyPolicy.chooseAction(actionValues);
        }

    }
}
