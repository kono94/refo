package evironment.jumpingDino;

import core.gui.Visualizable;
import lombok.Getter;

import java.awt.*;
import java.util.Objects;

@Getter
public class DinoStateWithSpeed extends DinoState implements Visualizable {
    private int obstacleSpeed;

    public DinoStateWithSpeed(int xDistanceToObstacle, boolean isJumping, int obstacleSpeed) {
        super(xDistanceToObstacle, isJumping);
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

    @Override
    public void drawObjects(Graphics g) {
        super.drawObjects(g);
        g.drawString("Speed: " + obstacleSpeed, (int)(scale * (Config.DINO_STARTING_X + getXDistanceToObstacle())),(int)(scale * (Config.FRAME_HEIGHT - Config.GROUND_Y - Config.OBSTACLE_SIZE - 40)) );
    }
}
