package evironment.jumpingDino;

import lombok.Getter;

import java.util.Objects;

@Getter
public class DinoStateWithSpeed extends DinoState{
    private int obstacleSpeed;

    public DinoStateWithSpeed(int xDistanceToObstacle, int obstacleSpeed) {
        super(xDistanceToObstacle);
        this.obstacleSpeed = obstacleSpeed;
    }

    @Override
    public String toString() {
        return "DinoStateWithSpeed{" +
                "obstacleSpeed=" + obstacleSpeed +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DinoStateWithSpeed)) return false;
        if (!super.equals(o)) return false;
        DinoStateWithSpeed that = (DinoStateWithSpeed) o;
        return getObstacleSpeed() == that.getObstacleSpeed();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getObstacleSpeed());
    }
}
