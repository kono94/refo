package core.gui;

import java.util.List;

/**
 * Switched out Views have to implement this interface.
 */
public interface LearningView {
    void repaintEnvironment();
    void updateLearningInfoPanel();
    void updateQTable();
    void updateRewardGraph(final List<Double> rewardHistory);
    void showQTableFrame();
}
