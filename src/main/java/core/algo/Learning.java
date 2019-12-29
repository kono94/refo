package core.algo;

import core.DiscreteActionSpace;
import core.Environment;
import core.LearningConfig;
import core.StateActionTable;
import core.listener.LearningListener;
import core.policy.Policy;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @param <A> discrete action type for a specific environment
 */
@Getter
public abstract class Learning<A extends Enum>{
    protected Policy<A> policy;
    protected DiscreteActionSpace<A> actionSpace;
    @Setter
    protected StateActionTable<A> stateActionTable;
    protected Environment<A> environment;
    protected float discountFactor;
    protected Set<LearningListener> learningListeners;
    @Setter
    protected int delay;
    protected List<Double> rewardHistory;
    protected ExecutorService learningExecutor;
    protected boolean currentlyLearning;

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, int delay) {
        this.environment = environment;
        this.actionSpace = actionSpace;
        this.discountFactor = discountFactor;
        this.delay = delay;
        currentlyLearning = false;
        learningListeners = new HashSet<>();
        rewardHistory = new CopyOnWriteArrayList<>();
        learningExecutor = Executors.newSingleThreadExecutor();
    }

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor) {
        this(environment, actionSpace, discountFactor, LearningConfig.DEFAULT_DELAY);
    }

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, int delay) {
        this(environment, actionSpace, LearningConfig.DEFAULT_DISCOUNT_FACTOR, delay);
    }

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace) {
        this(environment, actionSpace, LearningConfig.DEFAULT_DISCOUNT_FACTOR, LearningConfig.DEFAULT_DELAY);
    }

    public abstract void learn();

    public void addListener(LearningListener learningListener) {
        learningListeners.add(learningListener);
    }

    protected void dispatchStepEnd() {
        for (LearningListener l : learningListeners) {
            l.onStepEnd();
        }
    }

    protected void dispatchLearningStart() {
        currentlyLearning = true;
        for (LearningListener l : learningListeners) {
            l.onLearningStart();
        }
    }

    protected void dispatchLearningEnd() {
        currentlyLearning = false;
        for (LearningListener l : learningListeners) {
            l.onLearningEnd();
        }
    }

    public synchronized void interruptLearning(){
        //TODO: for non episodic learning
    }


    public void save(ObjectOutputStream oos) throws IOException {
        oos.writeObject(rewardHistory);
        oos.writeObject(stateActionTable);
    }

    public void load(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        rewardHistory = (List<Double>) ois.readObject();
        stateActionTable = (StateActionTable<A>) ois.readObject();
    }
}
