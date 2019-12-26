package core;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

/**
 * Small datatype to combine needed information to save and recover
 * the learning progress. Essentially, only the Q-Table needs to be saved
 * for all tabular methods because they all try to estimate
 * the action values until convergence.
 * For episodic method the number of episodes so far is also saved.
 *
 * @param <A> enum class of action for a specific environment
 */
@AllArgsConstructor
@Getter
public class SaveState<A extends Enum> implements Serializable {
    private static final long serialVersionUID = 1L;
    private StateActionTable<A> stateActionTable;
    private int currentEpisode;
}
