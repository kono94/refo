package core.listener;

/**
 * Interface the controller is implementing and gets passed to
 * the View. (Preventing the controller from adding all
 * ActionListeners to view elements)
 */
public interface ViewListener {
    void onEpsilonChange(float epsilon);
    void onDelayChange(int delay);
    void onFastLearnChange(boolean isFastLearn);
    void onLearnMoreEpisodes(int nrOfEpisodes);
    void onLoadState(String fileName);
    void onSaveState(String fileName);
    void onShowQTable();
}
