package core;

/**
 * Interface of the environment as defined in the RL framework.
 * An agent is able to interact with its environment, submitting
 * an action (.step()) every timestamp T and receiving a reward and a new
 * observation.
 * <class>StepResultEnvironment</class> is the datatype to combine all the
 * receiving information.
 * After each episode the environment is reset to its original state,
 * returning the starting state.
 *
 * @param <A> related <Enum> which defines the available action for this environment
 */
public interface Environment<A extends Enum> {
    StepResultEnvironment step(A action);
    State reset();
}
