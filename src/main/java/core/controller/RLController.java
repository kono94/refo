package core.controller;

import core.*;
import core.algo.EpisodicLearning;
import core.algo.Learning;
import core.algo.Method;
import core.algo.mc.MonteCarloOnPolicyEGreedy;
import core.gui.LearningView;
import core.gui.View;
import core.listener.LearningListener;
import core.listener.ViewListener;
import core.policy.EpsilonPolicy;
import lombok.Setter;

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RLController<A extends Enum> implements ViewListener, LearningListener {
    protected final String folderPrefix = "learningStates" + File.separator;
    protected Environment<A> environment;
    protected DiscreteActionSpace<A> discreteActionSpace;
    protected Method method;
    @Setter
    protected int delay = LearningConfig.DEFAULT_DELAY;
    @Setter
    protected float discountFactor = LearningConfig.DEFAULT_DISCOUNT_FACTOR;
    @Setter
    protected float epsilon = LearningConfig.DEFAULT_EPSILON;
    protected Learning<A> learning;
    protected boolean fastLearning;
    protected List<Double> latestRewardsHistory;
    @Setter
    protected int nrOfEpisodes;
    protected int prevDelay;
    protected volatile boolean printNextEpisode;

    public RLController(Environment<A> env, Method method, A... actions){
        setEnvironment(env);
        setMethod(method);
        setAllowedActions(actions);
        printNextEpisode = true;
    }

    public void start(){
        switch (method){
            case MC_ONPOLICY_EGREEDY:
                learning = new MonteCarloOnPolicyEGreedy<>(environment, discreteActionSpace, discountFactor, epsilon, delay);
                break;
            case TD_ONPOLICY:
                break;
            default:
                throw new IllegalArgumentException("Undefined method");
        }
        System.out.println("Initialized learning: " + learning.getClass());
        initListeners();
        System.out.println("Set listeners");
        initLearning();
    }

    protected void initListeners(){
        learning.addListener(this);
        new Thread(() -> {
            while (true){
                printNextEpisode = true;
                try {
                    Thread.sleep(30*1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initLearning(){
        if(learning instanceof EpisodicLearning){
            System.out.println("Starting learning of <" + nrOfEpisodes + "> episodes");
           ((EpisodicLearning) learning).learn(nrOfEpisodes);
        }else{
            learning.learn();
        }
    }

    /*************************************************
     **                VIEW LISTENERS               **
     *************************************************/
    @Override
    public void onLearnMoreEpisodes(int nrOfEpisodes){
        if(learning instanceof EpisodicLearning){
            ((EpisodicLearning) learning).learn(nrOfEpisodes);
        }else{
            throw new RuntimeException("Triggering onLearnMoreEpisodes on non-episodic learning!");
        }
    }

    @Override
    public void onLoadState(String fileName) {
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

    @Override
    public void onSaveState(String fileName) {
        FileOutputStream fos;
        ObjectOutputStream out;
        try{
            fos = new FileOutputStream(folderPrefix +  fileName);
            out = new ObjectOutputStream(fos);
            learning.interruptLearning();
            learning.save(out);
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onEpsilonChange(float epsilon) {
        if(learning.getPolicy() instanceof EpsilonPolicy){
            ((EpsilonPolicy<A>) learning.getPolicy()).setEpsilon(epsilon);
        }else{
            System.out.println("Trying to call inEpsilonChange on non-epsilon policy");
        }
    }

    @Override
    public void onDelayChange(int delay) {
        changeLearningDelay(delay);
    }

    protected void changeLearningDelay(int delay){
        learning.setDelay(delay);
    }

    @Override
    public void onFastLearnChange(boolean fastLearn) {
        this.fastLearning = fastLearn;
        if(fastLearn){
            prevDelay = learning.getDelay();
            changeLearningDelay(0);
        }else{
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
        onSaveState( method.toString() + System.currentTimeMillis()/1000 + (learning instanceof EpisodicLearning ? "e" + ((EpisodicLearning) learning).getCurrentEpisode() : ""));
    }

    @Override
    public void onEpisodeStart() {

    }

    @Override
    public void onEpisodeEnd(List<Double> rewardHistory) {
        latestRewardsHistory = rewardHistory;
        if(printNextEpisode){
            System.out.println("Episode " + ((EpisodicLearning) learning).getCurrentEpisode() + " Latest Reward: " + rewardHistory.get(rewardHistory.size()-1));
            System.out.println("Eps/sec: " +  ((EpisodicLearning) learning).getEpisodePerSecond());
            printNextEpisode = false;
        }
    }

    @Override
    public void onStepEnd() {
    }


    /*************************************************
     **                   SETTERS                   **
     *************************************************/

    private void setEnvironment(Environment<A> environment){
        if(environment == null){
            throw new IllegalArgumentException("Environment cannot be null");
        }
        this.environment = environment;
    }

    private void setMethod(Method method){
        if(method == null){
            throw new IllegalArgumentException("Method cannot be null");
        }
        this.method = method;
    }

    private void setAllowedActions(A[] actions){
        if(actions == null || actions.length == 0){
            throw new IllegalArgumentException("There has to be at least one action");
        }
        this.discreteActionSpace = new ListDiscreteActionSpace<>(actions);
    }
}
