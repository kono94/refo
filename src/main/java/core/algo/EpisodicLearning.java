package core.algo;

import core.DiscreteActionSpace;
import core.Environment;
import core.listener.LearningListener;

public abstract class EpisodicLearning<A extends Enum> extends Learning<A> implements Episodic{
    protected int currentEpisode;
    protected int episodesToLearn;
    protected volatile int episodePerSecond;
    protected int episodeSumCurrentSecond;
    private volatile boolean meseaureEpisodeBenchMark;

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

    protected void dispatchEpisodeEnd(double recentSumOfRewards){
        ++episodeSumCurrentSecond;
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

    @Override
    public void learn(){
        learn(0);
    }

    public void learn(int nrOfEpisodes){
        meseaureEpisodeBenchMark = true;
        new Thread(()->{
            while(meseaureEpisodeBenchMark){
                episodePerSecond = episodeSumCurrentSecond;
                episodeSumCurrentSecond = 0;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        episodesToLearn += nrOfEpisodes;
        dispatchLearningStart();
        for(int i=0; i < nrOfEpisodes; ++i){
            nextEpisode();
        }
        dispatchLearningEnd();
        meseaureEpisodeBenchMark = false;
    }

    protected abstract void nextEpisode();

    @Override
    public int getCurrentEpisode(){
        return currentEpisode;
    }

    @Override
    public int getEpisodesToGo(){
        return episodesToLearn - currentEpisode;
    }
}
