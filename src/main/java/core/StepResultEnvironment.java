package core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StepResultEnvironment {
    private State state;
    private double reward;
    private boolean done;
    private String info;
}
