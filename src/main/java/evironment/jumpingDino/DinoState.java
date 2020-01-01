package evironment.jumpingDino;

import core.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class DinoState implements State, Serializable {
    private int xDistanceToObstacle;

    @Override
    public String toString() {
        return "DinoState{" +
                "xDistanceToObstacle=" + xDistanceToObstacle +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DinoState dinoState = (DinoState) o;
        return xDistanceToObstacle == dinoState.xDistanceToObstacle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xDistanceToObstacle);
    }
}
