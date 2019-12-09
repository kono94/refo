package core;

public interface Environment<A extends Enum> {
    StepResultEnvironment step(A action);
    State reset();
}
