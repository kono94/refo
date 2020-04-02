package core.algo.td;

import core.*;
import core.algo.EpisodicLearning;
import core.policy.EpsilonGreedyPolicy;
import core.policy.GreedyPolicy;
import core.policy.Policy;
import evironment.antGame.Reward;
import example.ContinuousAnt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;

public class QLearningOffPolicyTDControl<A extends Enum> extends EpisodicLearning<A> {
    private float alpha;
    private Policy<A> greedyPolicy = new GreedyPolicy<>();

    public QLearningOffPolicyTDControl(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, float epsilon, float learningRate, int delay) {
        super(environment, actionSpace, discountFactor, delay);
        alpha = learningRate;
        this.policy = new EpsilonGreedyPolicy<>(epsilon);
        this.stateActionTable = new DeterministicStateActionTable<>(this.actionSpace);
    }

    public QLearningOffPolicyTDControl(Environment<A> environment, DiscreteActionSpace<A> actionSpace, int delay) {
        this(environment, actionSpace, LearningConfig.DEFAULT_DISCOUNT_FACTOR, LearningConfig.DEFAULT_EPSILON, LearningConfig.DEFAULT_ALPHA, delay);
    }

    @Override
    protected void nextEpisode() {
        State state = environment.reset();
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        StepResultEnvironment envResult = null;
        Map<A, Double> actionValues = null;


        sumOfRewards = 0;
        int timestampTilFood = 0;
        int rewardsPer1000 = 0;
        int foodCollected = 0;
        int iterations = 0;
        int foodTimestampsTotal= 0;
        while(envResult == null || !envResult.isDone()) {
            actionValues = stateActionTable.getActionValues(state);
            A action = policy.chooseAction(actionValues);

            // Take a step
            envResult = environment.step(action);
            double reward = envResult.getReward();
            State nextState = envResult.getState();
            sumOfRewards += reward;
            rewardsPer1000+=reward;
            timestampTilFood++;

          /*  if(iterations == 100){
                File file = new File(ContinuousAnt.FILE_NAME);
                try {
                    Files.writeString(Path.of(file.getPath()),  "\n", StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return;
            }*/


            if(reward == Reward.FOOD_DROP_DOWN_SUCCESS){
                foodCollected++;
                foodTimestampsTotal += timestampTilFood;
                if(foodCollected % 1000 == 0){
                    System.out.println(foodTimestampsTotal / 1000f + " " + timestampCurrentEpisode);
                    File file = new File(ContinuousAnt.FILE_NAME);
                    try {
                        Files.writeString(Path.of(file.getPath()),  foodTimestampsTotal/1000f +",", StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    foodTimestampsTotal = 0;
                }
                if(foodCollected == 1000){
                    ((EpsilonGreedyPolicy<A>) this.policy).setEpsilon(0.15f);
                }
                if(foodCollected == 2000){
                    ((EpsilonGreedyPolicy<A>) this.policy).setEpsilon(0.10f);
                }
                if(foodCollected == 3000){
                    ((EpsilonGreedyPolicy<A>) this.policy).setEpsilon(0.05f);
                }
                if(foodCollected == 4000){
                    System.out.println("final 0 expl");
                    ((EpsilonGreedyPolicy<A>) this.policy).setEpsilon(0.00f);
                }
                if(foodCollected == 15000){
                    File file = new File(ContinuousAnt.FILE_NAME);
                    try {
                        Files.writeString(Path.of(file.getPath()),  "\n", StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return;
                }
                iterations++;
                timestampTilFood = 0;
            }

            // Q Update
            double currentQValue = stateActionTable.getActionValues(state).get(action);
            // maxQ(S', a);
            // Using intern "greedy policy" as a helper to determine the highest action-value
            double highestValueNextState = stateActionTable.getActionValues(nextState).get(greedyPolicy.chooseAction(stateActionTable.getActionValues(nextState)));

            double updatedQValue = currentQValue + alpha * (reward + discountFactor * highestValueNextState - currentQValue);
            stateActionTable.setValue(state, action, updatedQValue);

            state = nextState;
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dispatchStepEnd();
        }
    }
}
