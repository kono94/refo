package core.algo.MC;

import core.*;
import core.algo.Learning;
import core.policy.EpsilonGreedyPolicy;
import javafx.util.Pair;

import java.util.*;

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

        for(int i = 0; i < nrOfEpisodes; ++i) {

            List<StepResult<A>> episode = new ArrayList<>();
            State state = environment.reset();
            for(int j=0; j < 100; ++j){
                Map<A, Double> actionValues = stateActionTable.getActionValues(state);
                A chosenAction = policy.chooseAction(actionValues);
                StepResultEnvironment envResult = environment.step(chosenAction);
                State nextState = envResult.getState();
                episode.add(new StepResult<>(state, chosenAction, envResult.getReward()));

                if(envResult.isDone()) break;

                state = nextState;

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Set<Pair<State, A>> stateActionPairs = new HashSet<>();

            for(StepResult<A> sr: episode){
                stateActionPairs.add(new Pair<>(sr.getState(), sr.getAction()));
            }

            for(Pair<State, A> stateActionPair: stateActionPairs){
                int firstOccurenceIndex = 0;
                // find first occurance of state action pair
                for(StepResult<A> sr: episode){
                    if(stateActionPair.getKey().equals(sr.getState()) && stateActionPair.getValue().equals(sr.getAction())){
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
