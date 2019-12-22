package core.listener;

public interface ViewListener {
    void onEpsilonChange(float epsilon);
    void onDelayChange(int delay);
    void onFastLearnChange(boolean isFastLearn);
    void onLearnMoreEpisodes(int nrOfEpisodes);
    void onLoadState(String fileName);
    void onSaveState(String fileName);
}
