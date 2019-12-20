package core.controller;

import core.DiscreteActionSpace;
import core.Environment;
import core.ListDiscreteActionSpace;
import core.algo.Learning;
import core.algo.Method;
import core.algo.mc.MonteCarloOnPolicyEGreedy;
import core.gui.View;
import core.listener.ViewListener;
import core.policy.EpsilonPolicy;

import javax.swing.*;

public class RLController<A extends Enum> implements ViewListener {
    protected Environment<A> environment;
    protected Learning<A> learning;
    protected DiscreteActionSpace<A> discreteActionSpace;
    protected View<A> view;
    private int delay;
    private int nrOfEpisodes;
    private Method method;
    private int prevDelay;

    public RLController(){
    }

    public void start(){
        if(environment == null || discreteActionSpace == null || method == null){
            throw new RuntimeException("Set environment, discreteActionSpace and method before calling .start()");
        }

        switch (method){
            case MC_ONPOLICY_EGREEDY:
                learning = new MonteCarloOnPolicyEGreedy<>(environment, discreteActionSpace, delay);
                break;
            case TD_ONPOLICY:
                break;
            default:
                throw new RuntimeException("Undefined method");
        }
        /*
         not using SwingUtilities here on purpose to ensure the view is fully
         initialized and can be passed as LearningListener.
         */
        view = new View<>(learning, environment, this);
        learning.addListener(view);
        learning.learn(nrOfEpisodes);
    }

    @Override
    public void onEpsilonChange(float epsilon) {
        if(learning.getPolicy() instanceof EpsilonPolicy){
            ((EpsilonPolicy<A>) learning.getPolicy()).setEpsilon(epsilon);
            SwingUtilities.invokeLater(() -> view.updateLearningInfoPanel());
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
        SwingUtilities.invokeLater(() -> view.updateLearningInfoPanel());
    }

    @Override
    public void onFastLearnChange(boolean fastLearn) {
        view.setDrawEveryStep(!fastLearn);
        if(fastLearn){
            prevDelay = learning.getDelay();
            changeLearningDelay(0);
        }else{
            changeLearningDelay(prevDelay);
        }
    }

    public RLController<A> setMethod(Method method){
        this.method = method;
        return this;
    }
    public RLController<A> setEnvironment(Environment<A> environment){
        this.environment = environment;
        return this;
    }
    @SafeVarargs
    public final RLController<A> setAllowedActions(A... actions){
        this.discreteActionSpace = new ListDiscreteActionSpace<>(actions);
        return this;
    }

    public RLController<A> setDelay(int delay){
        this.delay = delay;
        return this;
    }

    public RLController<A> setEpisodes(int nrOfEpisodes){
        this.nrOfEpisodes = nrOfEpisodes;
        return this;
    }

}
