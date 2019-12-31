package core.controller;

import core.Environment;
import core.algo.Method;
import core.gui.LearningView;
import core.gui.View;

import javax.swing.*;
import java.util.List;

public class RLControllerGUI<A extends Enum> extends RLController<A> {
    private LearningView learningView;

    public RLControllerGUI(Environment<A> env, Method method, A... actions) {
        super(env, method, actions);
    }

    @Override
    protected void initListeners() {
        SwingUtilities.invokeLater(() -> {
            learningView = new View<>(learning, environment, this);
            learning.addListener(this);
        });
    }

    @Override
    public void onLearnMoreEpisodes(int nrOfEpisodes) {
        super.onLearnMoreEpisodes(nrOfEpisodes);
        learningView.updateLearningInfoPanel();
    }

    @Override
    public void onLoadState(String fileName) {
        super.onLoadState(fileName);
        SwingUtilities.invokeLater(() -> learningView.updateLearningInfoPanel());
    }

    @Override
    public void onEpsilonChange(float epsilon) {
        super.onEpsilonChange(epsilon);
        SwingUtilities.invokeLater(() -> learningView.updateLearningInfoPanel());
    }

    @Override
    protected void changeLearningDelay(int delay) {
        super.changeLearningDelay(delay);
        SwingUtilities.invokeLater(() -> learningView.updateLearningInfoPanel());
    }

    @Override
    public void onLearningEnd() {
        super.onLearningEnd();
        SwingUtilities.invokeLater(() -> learningView.updateRewardGraph(latestRewardsHistory));
    }

    @Override
    public void onEpisodeEnd(List<Double> rewardHistory) {
        super.onEpisodeEnd(rewardHistory);
        SwingUtilities.invokeLater(() -> {
            if (!fastLearning) {
                learningView.updateRewardGraph(latestRewardsHistory);
            }
            learningView.updateLearningInfoPanel();
        });
    }

    @Override
    public void onStepEnd() {
        if (!fastLearning) {
            SwingUtilities.invokeLater(() -> learningView.repaintEnvironment());
        }
    }
}
