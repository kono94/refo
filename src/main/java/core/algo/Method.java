package core.algo;

/**
 * Instead of reflections this enum is used to determine
 * which RL-algorithm should be used.
 */
public enum Method {
    MC_CONTROL_FIRST_VISIT, MC_CONTROL_EVERY_VISIT, SARSA_ON_POLICY_CONTROL, Q_LEARNING_OFF_POLICY_CONTROL
}
