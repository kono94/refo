package evironment.jumpingDino;

import core.State;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@AllArgsConstructor
@Getter
public class DinoState implements State, Serializable {
    private int xDistanceToObstacle;

    @Override
    public String toString() {
        return Integer.toString(xDistanceToObstacle);
    }

    @Override
    public int hashCode() {
        return this.xDistanceToObstacle;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof DinoState){
            DinoState toCompare = (DinoState) obj;
            return toCompare.getXDistanceToObstacle() == this.xDistanceToObstacle;
        }
        return  super.equals(obj);
    }
}
