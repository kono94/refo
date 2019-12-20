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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public abstract class Learning<A extends Enum> {
    protected Policy<A> policy;
    protected DiscreteActionSpace<A> actionSpace;
    protected StateActionTable<A> stateActionTable;
    protected Environment<A> environment;
    protected float discountFactor;
    protected Set<LearningListener> learningListeners;
    @Setter
    protected int delay;
    private List<Double> rewardHistory;

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, int delay){
        this.environment = environment;
        this.actionSpace = actionSpace;
        this.discountFactor = discountFactor;
        this.delay = delay;
        learningListeners = new HashSet<>();
        rewardHistory = new CopyOnWriteArrayList<>();
    }

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor){
        this(environment, actionSpace, discountFactor, LearningConfig.DEFAULT_DELAY);
    }

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, int delay){
        this(environment, actionSpace, LearningConfig.DEFAULT_DISCOUNT_FACTOR, delay);
    }

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace){
        this(environment, actionSpace, LearningConfig.DEFAULT_DISCOUNT_FACTOR, LearningConfig.DEFAULT_DELAY);
    }


    public abstract void learn(int nrOfEpisodes);

    public void addListener(LearningListener learningListener){
        learningListeners.add(learningListener);
    }

    protected void dispatchEpisodeEnd(double recentSumOfRewards){
        rewardHistory.add(recentSumOfRewards);
        for(LearningListener l: learningListeners) {
            l.onEpisodeEnd(rewardHistory);
        }
    }

    protected void dispatchEpisodeStart(){
        for(LearningListener l: learningListeners){
            l.onEpisodeStart();
        }
    }
}
