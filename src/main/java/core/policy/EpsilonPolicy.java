package core.policy;

public interface EpsilonPolicy<A extends Enum> extends Policy<A> {
    float getEpsilon();
    void setEpsilon(float epsilon);
}
