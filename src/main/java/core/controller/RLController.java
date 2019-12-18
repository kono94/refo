package core.controller;

import core.DiscreteActionSpace;
import core.Environment;
import core.ListDiscreteActionSpace;
import core.algo.Learning;
import core.algo.Method;
import core.algo.mc.MonteCarloOnPolicyEGreedy;
import core.gui.View;

import javax.swing.*;
import java.util.Optional;

public class RLController<A extends Enum> implements ViewListener{
    protected Environment<A> environment;
    protected Learning<A> learning;
    protected DiscreteActionSpace<A> discreteActionSpace;
    protected View<A> view;
    private int delay;
    private int nrOfEpisodes;
    private Method method;

    public RLController(){
    }

    public void start(){
        if(environment == null || discreteActionSpace == null || method == null){
            throw new RuntimeException("Set environment, discreteActionSpace and method before calling .start()");
        }

        switch (method){
            case MC_ONPOLICY_EGREEDY:
                learning = new MonteCarloOnPolicyEGreedy<>(environment, discreteActionSpace);
                break;
            case TD_ONPOLICY:
                break;
            default:
                throw new RuntimeException("Undefined method");
        }
        SwingUtilities.invokeLater(() ->{
            view = new View<>(learning, this);
            learning.addListener(view);
        });
        learning.learn(nrOfEpisodes);
    }
    
    @Override
    public void onEpsilonChange(float epsilon) {
        learning.setEpsilon(epsilon);
        SwingUtilities.invokeLater(() -> view.updateLearningInfoPanel());
    }

    @Override
    public void onDelayChange(int delay) {
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
