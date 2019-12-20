package core.algo;

import core.DiscreteActionSpace;
import core.Environment;

public abstract class EpisodicLearning<A extends Enum> extends Learning<A> implements Episodic{
    protected int currentEpisode;

    public EpisodicLearning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, int delay) {
        super(environment, actionSpace, discountFactor, delay);
    }

    public EpisodicLearning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor) {
        super(environment, actionSpace, discountFactor);
    }

    public EpisodicLearning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, int delay) {
        super(environment, actionSpace, delay);
    }

    public EpisodicLearning(Environment<A> environment, DiscreteActionSpace<A> actionSpace) {
        super(environment, actionSpace);
    }

    @Override
    public int getCurrentEpisode(){
        return currentEpisode;
    }
}
