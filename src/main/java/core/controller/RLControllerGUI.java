package core.controller;

import core.Environment;
import core.algo.EpisodicLearning;
import core.algo.Method;
import core.gui.LearningView;
import core.gui.View;
import core.listener.ViewListener;

import javax.swing.*;
import java.util.List;

public class RLControllerGUI<A extends Enum> extends RLController<A> implements ViewListener {
    private LearningView learningView;

    @SafeVarargs
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

    /*************************************************
     **                View LISTENERS               **
     *************************************************/

    @Override
    public void onLoadState(String fileName) {
        super.loadState(fileName);
        SwingUtilities.invokeLater(() -> learningView.updateLearningInfoPanel());
    }

    @Override
    public void onSaveState(String fileName) {
        super.saveState(fileName);
    }

    @Override
    public void onShowQTable() {
        learningView.showQTableFrame();
    }

    @Override
    public void onEpsilonChange(float epsilon) {
        super.changeEpsilon(epsilon);
        SwingUtilities.invokeLater(() -> learningView.updateLearningInfoPanel());
    }

    @Override
    public void onDelayChange(int delay) {
        super.changeLearningDelay(delay);
        SwingUtilities.invokeLater(() -> learningView.updateLearningInfoPanel());
    }

    @Override
    public void onFastLearnChange(boolean isFastLearn) {
        super.changeFastLearning(isFastLearn);
        SwingUtilities.invokeLater(() -> learningView.updateLearningInfoPanel());
    }

    @Override
    protected void changeLearningDelay(int delay) {
        super.changeLearningDelay(delay);
        SwingUtilities.invokeLater(() -> learningView.updateLearningInfoPanel());
    }

    @Override
    public void onLearnMoreEpisodes(int nrOfEpisodes) {
        super.learnMoreEpisodes(nrOfEpisodes);
        learningView.updateLearningInfoPanel();
    }


    /*************************************************
     **              LEARNING LISTENERS             **
     *************************************************/

    @Override
    public void onEpisodeEnd(List<Double> rewardHistory) {
        super.onEpisodeEnd(rewardHistory);
        SwingUtilities.invokeLater(() -> {
            if(!fastLearning) {
                learningView.updateRewardGraph(latestRewardsHistory);
                learningView.updateQTable();
            }
            learningView.updateLearningInfoPanel();
        });
    }

    @Override
    public void onStepEnd() {
        if(!fastLearning) {
            SwingUtilities.invokeLater(() -> learningView.repaintEnvironment());
        }
    }

    @Override
    public void onLearningEnd() {
        super.onLearningEnd();
        onSaveState(method.toString() + System.currentTimeMillis() / 1000 + (learning instanceof EpisodicLearning ? "e" + learning.getCurrentEpisode() : ""));
        SwingUtilities.invokeLater(() -> learningView.updateRewardGraph(latestRewardsHistory));
    }
}
