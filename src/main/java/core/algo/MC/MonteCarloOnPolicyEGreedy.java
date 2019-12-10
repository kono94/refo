package core.algo.mc;

import core.*;
import core.algo.Learning;
import core.policy.EpsilonGreedyPolicy;
import javafx.util.Pair;
import java.util.*;

/**
 * TODO: Major problem:
 * StateActionPairs are only unique accounting for their position in the episode.
 * For example:
 *
 * startingState -> MOVE_LEFT : very first state action in the episode i = 1
 * image the agent does not collect the food and drops it to the start, the agent will receive
 * -1 for every timestamp hence (startingState -> MOVE_LEFT) will get a value of -10;
 *
 * BUT image moving left from the starting position will have no impact on the state because
 * the agent ran into a wall. The known world stays the same.
 * Taking an action after that will have the exact same state but a different action
 * making the value of this stateActionPair -9 because the stateAction pair took place on the second
 * timestamp, summing up all remaining rewards will be -9...
 *
 * How to encounter this problem?
 * @param <A>
 */
public class MonteCarloOnPolicyEGreedy<A extends Enum> extends Learning<A> {

    public MonteCarloOnPolicyEGreedy(Environment<A> environment, DiscreteActionSpace<A> actionSpace) {
        super(environment, actionSpace);
        discountFactor = 1f;
        this.policy = new EpsilonGreedyPolicy<>(0.1f);
        this.stateActionTable = new StateActionHashTable<>(actionSpace);
    }

    @Override
    public void learn(int nrOfEpisodes, int delay) {

        Map<Pair<State, A>, Double> returnSum = new HashMap<>();
        Map<Pair<State, A>, Integer> returnCount = new HashMap<>();

        State startingState = environment.reset();
        for(int i = 0; i < nrOfEpisodes; ++i) {
            List<StepResult<A>> episode = new ArrayList<>();
            State state = environment.reset();
            double rewardSum = 0;
            for(int j=0; j < 10; ++j){
                Map<A, Double> actionValues = stateActionTable.getActionValues(state);
                A chosenAction = policy.chooseAction(actionValues);
                StepResultEnvironment envResult = environment.step(chosenAction);
                State nextState = envResult.getState();
                rewardSum +=  envResult.getReward();
                episode.add(new StepResult<>(state, chosenAction, envResult.getReward()));

                if(envResult.isDone()) break;

                state = nextState;

                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.printf("Episode %d \t Reward: %f \n", i, rewardSum);
            Set<Pair<State, A>> stateActionPairs = new HashSet<>();

            for(StepResult<A> sr: episode){
                stateActionPairs.add(new Pair<>(sr.getState(), sr.getAction()));
            }
            System.out.println("stateActionPairs " + stateActionPairs.size());
            for(Pair<State, A> stateActionPair: stateActionPairs){
                int firstOccurenceIndex = 0;
                // find first occurance of state action pair
                for(StepResult<A> sr: episode){
                    if(stateActionPair.getKey().equals(sr.getState()) && stateActionPair.getValue().equals(sr.getAction())){
;
                        break;
                    }
                    firstOccurenceIndex++;
                }

                double G = 0;
                for(int l = firstOccurenceIndex; l < episode.size(); ++l){
                    G += episode.get(l).getReward() * (Math.pow(discountFactor, l - firstOccurenceIndex));
                }
                // slick trick to add G to the entry.
                // if the key does not exists, it will create a new entry with G as default value
                returnSum.merge(stateActionPair, G, Double::sum);
                returnCount.merge(stateActionPair, 1, Integer::sum);
                stateActionTable.setValue(stateActionPair.getKey(), stateActionPair.getValue(), returnSum.get(stateActionPair) / returnCount.get(stateActionPair));
            }
        }
    }
}
