package core;

public interface Environment<A extends Enum> {
    StepResult step(A action);
}
