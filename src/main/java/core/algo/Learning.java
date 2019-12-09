package core.algo;

import core.DiscreteActionSpace;
import core.Environment;
import core.StateActionTable;
import core.policy.Policy;

public abstract class Learning<A extends Enum> {
    protected Policy<A> policy;
    protected DiscreteActionSpace<A> actionSpace;
    protected StateActionTable<A> stateActionTable;
    protected Environment<A> environment;
    protected float discountFactor;
    protected float epsilon;

    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, float epsilon){
        this.environment = environment;
        this.actionSpace = actionSpace;
        this.discountFactor = discountFactor;
        this.epsilon = epsilon;
    }
    public Learning(Environment<A> environment, DiscreteActionSpace<A> actionSpace){
        this(environment, actionSpace, 1.0f, 0.1f);
    }

    public abstract void learn(int nrOfEpisodes, int delay);
}
