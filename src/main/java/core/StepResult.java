package core;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Almost the same as the datatype <class>StepResultEnvironment</class>
 * but includes the last action taken as well.
 * The environment does not return the last action taken in its result
 * <class>StepResultEnvironment</class> but is needed for the prediction problem.
 *
 * @param <A> Enum class of last action taken
 */
@AllArgsConstructor
@Getter
public class StepResult<A extends Enum> {
    private State state;
    private A action;
    private double reward;
}
