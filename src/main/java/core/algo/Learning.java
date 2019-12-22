package core.algo;

import core.DiscreteActionSpace;
import core.Environment;
import core.LearningConfig;
import core.StateActionTable;
import core.listener.LearningListener;
import core.policy.Policy;
import lombok.Getter;
import lombok.Setter;

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
    protected List<Double> rewardHistory;

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

    public abstract void learn();

    public void addListener(LearningListener learningListener){
        learningListeners.add(learningListener);
    }

    protected void dispatchStepEnd(){
        for(LearningListener l: learningListeners){
            l.onStepEnd();
        }
    }

    protected void dispatchLearningStart(){
        for(LearningListener l: learningListeners){
            l.onLearningStart();
        }
    }

    protected void dispatchLearningEnd(){
        for(LearningListener l: learningListeners){
            l.onLearningEnd();
        }
    }
}
