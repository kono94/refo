package core.algo;

/**
 * Instead of reflections this enum is used to determine
 * which RL-algorithm should be used.
 */
public enum Method {
    MC_CONTROL_EGREEDY, SARSA_EPISODIC, Q_LEARNING_OFF_POLICY_CONTROL
}
