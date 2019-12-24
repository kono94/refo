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

import javax.swing.*;
import java.io.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RLController<A extends Enum> implements ViewListener, LearningListener {
    private Environment<A> environment;
    private DiscreteActionSpace<A> discreteActionSpace;
    private Method method;
    private int delay = LearningConfig.DEFAULT_DELAY;
    private float discountFactor = LearningConfig.DEFAULT_DISCOUNT_FACTOR;
    private float epsilon = LearningConfig.DEFAULT_EPSILON;
    private Learning<A> learning;
    private LearningView learningView;
    private ExecutorService learningExecutor;
    private boolean currentlyLearning;
    private boolean fastLearning;
    private List<Double> latestRewardsHistory;
    private int nrOfEpisodes;
    private int prevDelay;

    public RLController(Environment<A> env, Method method, A... actions){
        learningExecutor = Executors.newSingleThreadExecutor();
        setEnvironment(env);
        setMethod(method);
        setAllowedActions(actions);
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

        initGUI();
        initLearning();
    }

    private void initGUI(){
        SwingUtilities.invokeLater(()->{
            learningView = new View<>(learning, environment, this);
            learning.addListener(this);
        });
    }

    private void initLearning(){
        if(learning instanceof EpisodicLearning){
            learningExecutor.submit(()->((EpisodicLearning) learning).learn(nrOfEpisodes));
        }else{
            learningExecutor.submit(()->learning.learn());
        }
    }

    /*************************************************
     **                VIEW LISTENERS               **
     *************************************************/
    @Override
    public void onLearnMoreEpisodes(int nrOfEpisodes){
        if(!currentlyLearning){
            if(learning instanceof EpisodicLearning){
                learningExecutor.submit(()->((EpisodicLearning) learning).learn(nrOfEpisodes));
            }else{
                throw new RuntimeException("Triggering onLearnMoreEpisodes on non-episodic learning!");
            }
        }
    }

    @Override
    public void onLoadState(String fileName) {
        FileInputStream fis;
        ObjectInput in;
        try {
            fis = new FileInputStream(fileName);
            in = new ObjectInputStream(fis);
            SaveState<A>  saveState = (SaveState<A>) in.readObject();
            learning.setStateActionTable(saveState.getStateActionTable());
            if(learning instanceof EpisodicLearning){
                ((EpisodicLearning) learning).setCurrentEpisode(saveState.getCurrentEpisode());
            }
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
            fos = new FileOutputStream(fileName);
            out = new ObjectOutputStream(fos);
            int currentEpisode;
            if(learning instanceof EpisodicLearning){
                currentEpisode = ((EpisodicLearning) learning).getCurrentEpisode();
            }else{
                currentEpisode = 0;
            }
            out.writeObject(new SaveState<>(learning.getStateActionTable(), currentEpisode));
            out.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onEpsilonChange(float epsilon) {
        if(learning.getPolicy() instanceof EpsilonPolicy){
            ((EpsilonPolicy<A>) learning.getPolicy()).setEpsilon(epsilon);
            SwingUtilities.invokeLater(() -> learningView.updateLearningInfoPanel());
        }else{
            System.out.println("Trying to call inEpsilonChange on non-epsilon policy");
        }
    }

    @Override
    public void onDelayChange(int delay) {
        changeLearningDelay(delay);
    }

    private void changeLearningDelay(int delay){
        learning.setDelay(delay);
        SwingUtilities.invokeLater(() -> learningView.updateLearningInfoPanel());
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
        currentlyLearning = true;
    }

    @Override
    public void onLearningEnd() {
        currentlyLearning = false;
        SwingUtilities.invokeLater(()-> learningView.updateRewardGraph(latestRewardsHistory));
    }

    @Override
    public void onEpisodeEnd(List<Double> rewardHistory) {
        latestRewardsHistory = rewardHistory;
        SwingUtilities.invokeLater(() ->{
            if(!fastLearning){
                learningView.updateRewardGraph(latestRewardsHistory);
            }
            learningView.updateLearningInfoPanel();
        });
    }

    @Override
    public void onEpisodeStart() {

    }

    @Override
    public void onStepEnd() {
        if(!fastLearning){
            SwingUtilities.invokeLater(() -> learningView.repaintEnvironment());
        }
    }


    /*************************************************
     **                   SETTER                    **
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

    public void setDelay(int delay){
        this.delay = delay;
    }

    public void setEpisodes(int nrOfEpisodes){
        this.nrOfEpisodes = nrOfEpisodes;
    }

    public void setDiscountFactor(float discountFactor){
        this.discountFactor = discountFactor;
    }
    public void setEpsilon(float epsilon){
        this.epsilon = epsilon;
    }
}
