package core.algo.td;

import core.*;
import core.algo.EpisodicLearning;
import core.policy.EpsilonGreedyPolicy;

import java.util.Map;


public class SARSA<A extends Enum> extends EpisodicLearning<A> {
    private float alpha;

    public SARSA(Environment<A> environment, DiscreteActionSpace<A> actionSpace, float discountFactor, float epsilon, float learningRate, int delay) {
        super(environment, actionSpace, discountFactor, delay);
        alpha = learningRate;
        this.policy = new EpsilonGreedyPolicy<>(epsilon);
        this.stateActionTable = new DeterministicStateActionTable<>(this.actionSpace);
    }

    public SARSA(Environment<A> environment, DiscreteActionSpace<A> actionSpace, int delay) {
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
        Map<A, Double> actionValues = stateActionTable.getActionValues(state);
        A action = policy.chooseAction(actionValues);

        sumOfRewards = 0;
        while(envResult == null || !envResult.isDone()) {
            // Take a step
            envResult = environment.step(action);
            sumOfRewards += envResult.getReward();

            State nextState = envResult.getState();

            // Pick next action
            actionValues = stateActionTable.getActionValues(nextState);
            A nextAction = policy.chooseAction(actionValues);

            // TD update
            // target = reward + gamma * Q(nextState, nextAction)
            double currentQValue = stateActionTable.getActionValues(state).get(action);
            double nextQValue = stateActionTable.getActionValues(nextState).get(nextAction);
            double reward = envResult.getReward();
            double updatedQValue = currentQValue + alpha * (reward + discountFactor * nextQValue - currentQValue);
            stateActionTable.setValue(state, action, updatedQValue);

            state = nextState;
            action = nextAction;

            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            dispatchStepEnd();
        }
    }
}
