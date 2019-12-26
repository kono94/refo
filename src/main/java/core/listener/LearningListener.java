package core.listener;

import java.util.List;

/**
 * Methods that gets triggered to inform about the current learning process.
 */
public interface LearningListener{
    void onLearningStart();
    void onLearningEnd();
    void onEpisodeEnd(List<Double> rewardHistory);
    void onEpisodeStart();
    void onStepEnd();
}
