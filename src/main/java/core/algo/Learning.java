package core.algo;

import core.DiscreteActionSpace;
import core.Environment;
import core.LearningConfig;
import core.StateActionTable;
import core.listener.LearningListener;
import core.policy.Policy;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class Learning<A extends Enum> {
    protected Policy<A> policy;
    protected DiscreteActionSpace<A> actionSpace;
    protected StateActionTable<A> stateActionTable;
    protected Environment<A> environment;
    protected float discountFactor;
    @Setter
    protected float epsilon;
    protected Set<LearningListener> learningListeners;
    @Setter
    protected int delay;

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, float epsilon, int delay){
        this.environment = environment;
        this.actionSpace = actionSpace;
        this.discountFactor = discountFactor;
        this.epsilon = epsilon;
        this.delay = delay;
        learningListeners = new HashSet<>();
    }

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, float epsilon){
        this(environment, actionSpace, discountFactor, epsilon, LearningConfig.DEFAULT_DELAY);
    }

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace){
        this(environment, actionSpace, LearningConfig.DEFAULT_DISCOUNT_FACTOR, LearningConfig.DEFAULT_EPSILON, LearningConfig.DEFAULT_DELAY);
    }

    public abstract void learn(int nrOfEpisodes);

    public void addListener(LearningListener learningListener){
        learningListeners.add(learningListener);
    }

    protected void dispatchEpisodeEnd(double sum){
        for(LearningListener l: learningListeners) {
            l.onEpisodeEnd(sum);
        }
    }

    protected void dispatchEpisodeStart(){
        for(LearningListener l: learningListeners){
            l.onEpisodeStart();
        }
    }
}
