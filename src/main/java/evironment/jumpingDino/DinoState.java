package evironment.jumpingDino;

import core.State;
import core.gui.Visualizable;
import lombok.Getter;

import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

@Getter
public class DinoState extends DinoStateSimple implements State, Serializable, Visualizable {
    private boolean isJumping;

    public DinoState(int xDistanceToObstacle, boolean isJumping) {
        super(xDistanceToObstacle);
        this.isJumping = isJumping;
    }

    @Override
    public String toString() {
        return "DinoState{" +
                "xDistanceToObstacle=" + xDistanceToObstacle +
                "isJumping=" + isJumping +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DinoState dinoState = (DinoState) o;
        return xDistanceToObstacle == dinoState.xDistanceToObstacle && isJumping == dinoState.isJumping;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xDistanceToObstacle, isJumping);
    }

    @Override
    protected void drawDinoInfo(Graphics g) {
        int dinoY;
        if(!isJumping) {
            dinoY = Config.FRAME_HEIGHT - Config.GROUND_Y - Config.DINO_SIZE;
            g.fillRect((int) (scale * Config.DINO_STARTING_X), (int) (scale * (dinoY)), (int) (scale * Config.DINO_SIZE), (int) (scale * Config.DINO_SIZE));
        } else {
            dinoY = Config.FRAME_HEIGHT - Config.GROUND_Y - Config.DINO_SIZE - (int) (scale * Config.MAX_JUMP_HEIGHT);
            g.fillRect((int) (scale * Config.DINO_STARTING_X), (int) (scale * (dinoY)), (int) (scale * Config.DINO_SIZE), (int) (scale * Config.DINO_SIZE));
        }
        g.drawString("Distance: " + xDistanceToObstacle + " inJump: " + isJumping, (int) (scale * Config.DINO_STARTING_X), (int) (scale * (dinoY - 20)));
    }
}
