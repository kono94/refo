package core.algo.mc;

import core.*;
import core.algo.EpisodicLearning;
import core.policy.EpsilonGreedyPolicy;
import core.policy.GreedyPolicy;
import core.policy.Policy;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * Includes both variants of Monte-Carlo methods
 * Default method is First-Visit.
 * Change to Every-Visit by setting flag "useEveryVisit" in the constructor to true.
 * @param <A>
 */
public class MonteCarloControlEGreedy<A extends Enum> extends EpisodicLearning<A> {

    private Map<Pair<State, A>, Double> returnSum;
    private Map<Pair<State, A>, Integer> returnCount;
    private boolean isEveryVisit;

    // t
    private float epsilon;
    // t
    private Policy<A> greedyPolicy = new GreedyPolicy<>();


    public MonteCarloControlEGreedy(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, float epsilon, int delay, boolean useEveryVisit) {
        super(environment, actionSpace, discountFactor, delay);
        isEveryVisit = useEveryVisit;
        // t
        this.epsilon = epsilon;
        this.policy = new EpsilonGreedyPolicy<>(epsilon);
        this.stateActionTable = new DeterministicStateActionTable<>(this.actionSpace);
        returnSum = new HashMap<>();
        returnCount = new HashMap<>();
    }

    public MonteCarloControlEGreedy(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, float epsilon, int delay) {
        this(environment, actionSpace, discountFactor, epsilon, delay, false);
    }

    public MonteCarloControlEGreedy(Environment<A> environment, DiscreteActionSpace<A> actionSpace, int delay) {
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

        while(envResult == null || !envResult.isDone()) {
            Map<A, Double> actionValues = stateActionTable.getActionValues(state);
            A chosenAction;
            if(currentEpisode % 2 == 1){
                chosenAction = greedyPolicy.chooseAction(actionValues);
            }else{
                chosenAction = policy.chooseAction(actionValues);
            }

            envResult = environment.step(chosenAction);
            State nextState = envResult.getState();
            sumOfRewards += envResult.getReward();
            rewardCheckSum += envResult.getReward();
            episode.add(new StepResult<>(state, chosenAction, envResult.getReward()));

            state = nextState;

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            timestamp++;
            dispatchStepEnd();
            if(converged) return;
        }

        if(currentEpisode % 2 == 1){
            return;
        }

        //  System.out.printf("Episode %d \t Reward: %f \n", currentEpisode, sumOfRewards);
        HashMap<Pair<State, A>, List<Integer>> stateActionPairs = new LinkedHashMap<>();

        int firstOccurrenceIndex = 0;
        for(StepResult<A> sr : episode) {
            Pair<State, A> pair = new ImmutablePair<>(sr.getState(), sr.getAction());
            if(!stateActionPairs.containsKey(pair)) {
                List<Integer> l = new ArrayList<>();
                l.add(firstOccurrenceIndex);
                stateActionPairs.put(pair, l);
            }

            /*
            This is the only difference between First-Visit and Every-Visit.
            When First-Visit is selected, only the first index of the occurrence is put into the list.
            When Every-Visit is selected, every following occurrence is saved
            into the list as well.
             */
            else if(isEveryVisit) {
                stateActionPairs.get(pair).add(firstOccurrenceIndex);
            }
            ++firstOccurrenceIndex;
        }
        //System.out.println("stateActionPairs " + stateActionPairs.size());
        for(Map.Entry<Pair<State, A>, List<Integer>> entry : stateActionPairs.entrySet()) {
            Pair<State, A> stateActionPair = entry.getKey();
            List<Integer> firstOccurrences = entry.getValue();
            for(Integer firstOccurrencesIdx : firstOccurrences) {
                double G = 0;
                for(int l = firstOccurrencesIdx; l < episode.size(); ++l) {
                    G += episode.get(l).getReward() * (Math.pow(discountFactor, l - firstOccurrencesIdx));
                }
                // slick trick to add G to the entry.
                // if the key does not exists, it will create a new entry with G as default value
                returnSum.merge(stateActionPair, G, Double::sum);
                returnCount.merge(stateActionPair, 1, Integer::sum);
                stateActionTable.setValue(stateActionPair.getKey(), stateActionPair.getValue(), returnSum.get(stateActionPair) / returnCount.get(stateActionPair));
            }
        }
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
