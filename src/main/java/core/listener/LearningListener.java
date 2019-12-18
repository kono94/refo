package core.listener;

public interface LearningListener{
    void onEpisodeEnd(double sumOfRewards);
    void onEpisodeStart();
}
