package core.policy;

/**
 * Chooses the action with the highest values with possibility: 1-Epsilon + Epsilon/|A|
 * With possibility of Epsilon, a random action is taken (highest values option included).
 *
 * @param <A> Enum class of available action in specific environment
 */
public interface EpsilonPolicy<A extends Enum> extends Policy<A> {
    float getEpsilon();
    void setEpsilon(float epsilon);
}
