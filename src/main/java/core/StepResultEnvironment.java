package core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * After each timestamp the environment returns a reward
 * for the previous action (still the same timestamp t), the resulting
 * observation/state (environment is in charge to process the observation
 * and build a markov state) and the information whether or not the episode
 * has ended.
 */
@Getter
@Setter
@AllArgsConstructor
public class StepResultEnvironment {
    private State state;
    private double reward;
    private boolean done;
    private String info;
}
