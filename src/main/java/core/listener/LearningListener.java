package core.listener;

import java.util.List;

public interface LearningListener{
    void onLearningStart();
    void onLearningEnd();
    void onEpisodeEnd(List<Double> rewardHistory);
    void onEpisodeStart();
    void onStepEnd();
}
