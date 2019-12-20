package core.listener;

public interface ViewListener {
    void onEpsilonChange(float epsilon);
    void onDelayChange(int delay);
    void onFastLearnChange(boolean isFastLearn);
}
