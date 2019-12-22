package core.gui;

import java.util.List;

public interface LearningView {
    void repaintEnvironment();
    void updateLearningInfoPanel();
    void updateRewardGraph(final List<Double> rewardHistory);
}
