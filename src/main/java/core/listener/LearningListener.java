package core.listener;

import java.util.List;

public interface LearningListener{
    void onEpisodeEnd(List<Double> rewardHistory);
    void onEpisodeStart();
}
