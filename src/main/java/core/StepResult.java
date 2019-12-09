package core;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StepResult<A extends Enum> {
    private State state;
    private A action;
    private double reward;
}
