package core.algo;

import core.DiscreteActionSpace;
import core.Environment;
import core.LearningConfig;
import core.StepResult;
import core.listener.LearningListener;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class EpisodicLearning<A extends Enum> extends Learning<A> implements Episodic {
    @Setter
    protected int currentEpisode = 0;
    protected volatile AtomicInteger episodesToLearn = new AtomicInteger(0);
    @Getter
    protected volatile int episodePerSecond;
    protected int episodeSumCurrentSecond;
    protected double sumOfRewards;
    protected List<StepResult<A>> episode = new ArrayList<>();

    public EpisodicLearning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, int delay) {
        super(environment, actionSpace, discountFactor, delay);
        initBenchMarking();
    }

    public EpisodicLearning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor) {
        super(environment, actionSpace, discountFactor);
        initBenchMarking();
    }

    public EpisodicLearning(Environment<A> environment, DiscreteActionSpace<A> actionSpace, int delay) {
        super(environment, actionSpace, delay);
        initBenchMarking();
    }

    public EpisodicLearning(Environment<A> environment, DiscreteActionSpace<A> actionSpace) {
        super(environment, actionSpace);
        initBenchMarking();
    }

    protected abstract void nextEpisode();

    private void initBenchMarking(){
        new Thread(()->{
            while (true){
                episodePerSecond = episodeSumCurrentSecond;
                episodeSumCurrentSecond = 0;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    protected void dispatchEpisodeEnd(){
        ++episodeSumCurrentSecond;
        if(rewardHistory.size() > 10000){
            rewardHistory.clear();
        }
        rewardHistory.add(sumOfRewards);
        for(LearningListener l: learningListeners) {
            l.onEpisodeEnd(rewardHistory);
        }
    }

    protected void dispatchEpisodeStart(){
        ++currentEpisode;
        episodesToLearn.decrementAndGet();
        for(LearningListener l: learningListeners){
            l.onEpisodeStart();
        }
    }

    @Override
    public void learn(){
        learn(LearningConfig.DEFAULT_NR_OF_EPISODES);
    }

    private void startLearning(){
        learningExecutor.submit(()->{
            dispatchLearningStart();
            while(episodesToLearn.get() > 0){
                dispatchEpisodeStart();
                nextEpisode();
                dispatchEpisodeEnd();
            }
            synchronized (this){
                dispatchLearningEnd();
                notifyAll();
            }
        });
    }

    /**
     * Stopping the while loop by setting episodesToLearn to 0.
     * The current episode can not be interrupted, so the sleep delay
     * is removed and the calling thread has to wait until the
     * current episode is done.
     * Resetting the delay afterwards.
     */
    @Override
    public synchronized void interruptLearning(){
        episodesToLearn.set(0);
        int prevDelay = delay;
        delay = 0;
        while(currentlyLearning) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        delay = prevDelay;
    }

    public synchronized void learn(int nrOfEpisodes){
        boolean isLearning = episodesToLearn.getAndAdd(nrOfEpisodes) != 0;
        if(!isLearning)
            startLearning();
    }

    @Override
    public int getEpisodesToGo(){
        return episodesToLearn.get();
    }


    public int getCurrentEpisode() {
        return currentEpisode;
    }

    public int getEpisodesPerSecond() {
        return episodePerSecond;
    }

    @Override
    public synchronized void save(ObjectOutputStream oos) throws IOException {
      super.save(oos);
      oos.writeInt(currentEpisode);
    }

    @Override
    public synchronized void load(ObjectInputStream ois) throws IOException, ClassNotFoundException {
       super.load(ois);
       currentEpisode = ois.readInt();
    }
}
