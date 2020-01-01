package core.algo.mc;

import core.*;
import core.algo.EpisodicLearning;
import core.policy.EpsilonGreedyPolicy;
import org.javatuples.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * TODO: Major problem:
 * StateActionPairs are only unique accounting for their position in the episode.
 * For example:
 * <p>
 * startingState -> MOVE_LEFT : very first state action in the episode i = 1
 * image the agent does not collect the food and drops it to the start, the agent will receive
 * -1 for every timestamp hence (startingState -> MOVE_LEFT) will get a value of -10;
 * <p>
 * BUT image moving left from the starting position will have no impact on the state because
 * the agent ran into a wall. The known world stays the same.
 * Taking an action after that will have the exact same state but a different action
 * making the value of this stateActionPair -9 because the stateAction pair took place on the second
 * timestamp, summing up all remaining rewards will be -9...
 * <p>
 * How to encounter this problem?
 *
 * @param <A>
 */
public class MonteCarloOnPolicyEGreedy<A extends Enum> extends EpisodicLearning<A> {

    private Map<Pair<State, A>, Double> returnSum;
    private Map<Pair<State, A>, Integer> returnCount;

    public MonteCarloOnPolicyEGreedy(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, float epsilon, int delay) {
        super(environment, actionSpace, discountFactor, delay);
        currentEpisode = 0;
        this.policy = new EpsilonGreedyPolicy<>(epsilon);
        this.stateActionTable = new DeterministicStateActionTable<>(this.actionSpace);
        returnSum = new HashMap<>();
        returnCount = new HashMap<>();
    }

    public MonteCarloOnPolicyEGreedy(Environment<A> environment, DiscreteActionSpace<A> actionSpace, int delay) {
        this(environment, actionSpace, LearningConfig.DEFAULT_DISCOUNT_FACTOR, LearningConfig.DEFAULT_EPSILON, delay);
    }

    @Override
    public void nextEpisode() {
        episode = new ArrayList<>();
        State state = environment.reset();
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sumOfRewards = 0;
        StepResultEnvironment envResult = null;
        while(envResult == null || !envResult.isDone()){
            Map<A, Double> actionValues = stateActionTable.getActionValues(state);
            A chosenAction = policy.chooseAction(actionValues);
            envResult = environment.step(chosenAction);
            State nextState = envResult.getState();
            sumOfRewards += envResult.getReward();
            episode.add(new StepResult<>(state, chosenAction, envResult.getReward()));

            state = nextState;

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dispatchStepEnd();
        }

      //  System.out.printf("Episode %d \t Reward: %f \n", currentEpisode, sumOfRewards);
        Set<Pair<State, A>> stateActionPairs = new LinkedHashSet<>();

        for (StepResult<A> sr : episode) {
            stateActionPairs.add(new Pair<>(sr.getState(), sr.getAction()));
        }

        //System.out.println("stateActionPairs " + stateActionPairs.size());
        for (Pair<State, A> stateActionPair : stateActionPairs) {
            int firstOccurenceIndex = 0;
            // find first occurance of state action pair
            for (StepResult<A> sr : episode) {
                if (stateActionPair.getValue0().equals(sr.getState()) && stateActionPair.getValue1().equals(sr.getAction())) {
                    break;
                }
                firstOccurenceIndex++;
            }

            double G = 0;
            for (int l = firstOccurenceIndex; l < episode.size(); ++l) {
                G += episode.get(l).getReward() * (Math.pow(discountFactor, l - firstOccurenceIndex));
            }
            // slick trick to add G to the entry.
            // if the key does not exists, it will create a new entry with G as default value
            returnSum.merge(stateActionPair, G, Double::sum);
            returnCount.merge(stateActionPair, 1, Integer::sum);
            stateActionTable.setValue(stateActionPair.getValue0(), stateActionPair.getValue1(), returnSum.get(stateActionPair) / returnCount.get(stateActionPair));
        }
    }

    @Override
    public int getCurrentEpisode() {
        return currentEpisode;
    }

    @Override
    public int getEpisodesPerSecond(){
        return episodePerSecond;
    }

    @Override
    public void save(ObjectOutputStream oos) throws IOException {
        super.save(oos);
        oos.writeObject(returnSum);
        oos.writeObject(returnCount);
    }

    @Override
    public void load(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        super.load(ois);
        returnSum = (Map<Pair<State, A>, Double>) ois.readObject();
        returnCount = (Map<Pair<State, A>, Integer>) ois.readObject();
    }
}
