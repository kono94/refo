package core.algo.td;

import core.*;
import core.algo.EpisodicLearning;
import core.policy.EpsilonGreedyPolicy;
import core.policy.GreedyPolicy;
import core.policy.Policy;

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
        Map<A, Double> actionValues;

        sumOfRewards = 0;
        while(envResult == null || !envResult.isDone()) {
            actionValues = stateActionTable.getActionValues(state);
            A action = policy.chooseAction(actionValues);

            // Take a step
            envResult = environment.step(action);
            double reward = envResult.getReward();
            State nextState = envResult.getState();
            sumOfRewards += reward;

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
