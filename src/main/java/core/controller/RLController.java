package core.controller;

import core.DiscreteActionSpace;
import core.Environment;
import core.LearningConfig;
import core.ListDiscreteActionSpace;
import core.algo.EpisodicLearning;
import core.algo.Learning;
import core.algo.Method;
import core.algo.mc.MonteCarloControlFirstVisitEGreedy;
import core.algo.td.QLearningOffPolicyTDControl;
import core.algo.td.SARSA;
import core.listener.LearningListener;
import core.policy.EpsilonPolicy;
import lombok.Setter;

import java.io.*;
import java.util.List;


public class RLController<A extends Enum> implements LearningListener {
    protected final String folderPrefix = "learningStates" + File.separator;
    protected Environment<A> environment;
    protected DiscreteActionSpace<A> discreteActionSpace;
    protected Method method;
    @Setter
    protected int delay = LearningConfig.DEFAULT_DELAY;
    @Setter
    protected float discountFactor = LearningConfig.DEFAULT_DISCOUNT_FACTOR;
    @Setter
    protected float learningRate = LearningConfig.DEFAULT_DISCOUNT_FACTOR;
    @Setter
    protected float epsilon = LearningConfig.DEFAULT_EPSILON;
    protected Learning<A> learning;
    protected boolean fastLearning;
    protected List<Double> latestRewardsHistory;
    @Setter
    protected int nrOfEpisodes;
    protected int prevDelay;
    protected volatile boolean printNextEpisode;

    public RLController(Environment<A> env, Method method, A... actions) {
        setEnvironment(env);
        setMethod(method);
        setAllowedActions(actions);
        printNextEpisode = true;
    }

    public void start() {
        switch(method) {
            case MC_CONTROL_FIRST_VISIT:
                learning = new MonteCarloControlFirstVisitEGreedy<>(environment, discreteActionSpace, discountFactor, epsilon, delay);
                break;
            case MC_CONTROL_EVERY_VISIT:
                learning = new MonteCarloControlFirstVisitEGreedy<>(environment, discreteActionSpace, discountFactor, epsilon, delay, true);
                break;

            case SARSA_ON_POLICY_CONTROL:
                learning = new SARSA<>(environment, discreteActionSpace, discountFactor, epsilon, learningRate, delay);
                break;
            case Q_LEARNING_OFF_POLICY_CONTROL:
                learning = new QLearningOffPolicyTDControl<>(environment, discreteActionSpace, discountFactor, epsilon, learningRate, delay);
                break;
            default:
                throw new IllegalArgumentException("Undefined method");
        }
        System.out.println("Initialized learning: " + learning.getClass());
        initListeners();
        System.out.println("Set listeners");
        initLearning();
    }

    protected void initListeners() {
            learning.addListener(this);
            new Thread(() -> {
                while(learning.isCurrentlyLearning()) {
                    printNextEpisode = true;
                    try {
                        Thread.sleep(30 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
    }

    private void initLearning() {
        if(learning instanceof EpisodicLearning) {
            System.out.println("Starting learning of <" + nrOfEpisodes + "> episodes");
            ((EpisodicLearning) learning).learn(nrOfEpisodes);
        } else {
            learning.learn();
        }
    }

    protected void changeLearningDelay(int delay) {
        learning.setDelay(delay);
    }

    protected void learnMoreEpisodes(int nrOfEpisodes) {
        if(learning instanceof EpisodicLearning) {
            if(learning.isCurrentlyLearning()){
                ((EpisodicLearning) learning).learnMoreEpisodes(nrOfEpisodes);
            }else{
                new Thread(() -> {
                    ((EpisodicLearning) learning).learn(nrOfEpisodes);
                }).start();
            }
        } else {
            throw new RuntimeException("Triggering onLearnMoreEpisodes on non-episodic learning!");
        }
    }

    protected void changeEpsilon(float epsilon) {
        if(learning.getPolicy() instanceof EpsilonPolicy) {
            ((EpsilonPolicy<A>) learning.getPolicy()).setEpsilon(epsilon);
        } else {
            System.out.println("Trying to call inEpsilonChange on non-epsilon policy");
        }
    }

    protected void loadState(String fileName) {
        FileInputStream fis;
        ObjectInputStream in;
        try {
            fis = new FileInputStream(fileName);
            in = new ObjectInputStream(fis);
            System.out.println("interrupt" + Thread.currentThread().getId());
            learning.interruptLearning();
            learning.load(in);
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected void saveState(String fileName) {
        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = new FileOutputStream(folderPrefix + fileName);
            out = new ObjectOutputStream(fos);
            learning.interruptLearning();
            learning.save(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void changeFastLearning(boolean fastLearn) {
        this.fastLearning = fastLearn;
        if(fastLearn) {
            prevDelay = learning.getDelay();
            changeLearningDelay(0);
        } else {
            changeLearningDelay(prevDelay);
        }
    }

    /*************************************************
     **              LEARNING LISTENERS             **
     *************************************************/
    @Override
    public void onLearningStart() {
    }

    @Override
    public void onLearningEnd() {
        System.out.println("Learning finished");
    }

    @Override
    public void onEpisodeStart() {

    }

    @Override
    public void onEpisodeEnd(List<Double> rewardHistory) {
        latestRewardsHistory = rewardHistory;
        if(printNextEpisode) {
            System.out.println("Episode " + ((EpisodicLearning) learning).getCurrentEpisode() + " Latest Reward: " + rewardHistory.get(rewardHistory.size() - 1));
            System.out.println("Eps/sec: " + ((EpisodicLearning) learning).getEpisodePerSecond());
            printNextEpisode = false;
        }
    }

    @Override
    public void onStepEnd() {
    }


    /*************************************************
     **                   SETTERS                   **
     *************************************************/

    private void setEnvironment(Environment<A> environment) {
        if(environment == null) {
            throw new IllegalArgumentException("Environment cannot be null");
        }
        this.environment = environment;
    }

    private void setMethod(Method method) {
        if(method == null) {
            throw new IllegalArgumentException("Method cannot be null");
        }
        this.method = method;
    }

    private void setAllowedActions(A[] actions) {
        if(actions == null || actions.length == 0) {
            throw new IllegalArgumentException("There has to be at least one action");
        }
        this.discreteActionSpace = new ListDiscreteActionSpace<>(actions);
    }
}
