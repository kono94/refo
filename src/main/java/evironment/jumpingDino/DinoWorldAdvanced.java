package evironment.jumpingDino;

import core.State;

import java.awt.*;

public class DinoWorldAdvanced extends DinoWorld{
    public DinoWorldAdvanced(){
        super();
    }

    @Override
    protected State generateReturnState() {
        return new DinoStateWithSpeed(getDistanceToObstacle(), dino.isInJump(), getCurrentObstacle().getDx());
    }

    @Override
    protected void spawnNewObstacle() {
        int dx;
        int xSpawn;
        dx = -(int)((Math.random() + 0.5) * Config.OBSTACLE_SPEED);
        // randomly spawning more right outside of the screen
        xSpawn = (int)(Math.random() + 0.5 * Config.FRAME_WIDTH + Config.FRAME_WIDTH  + Config.OBSTACLE_SIZE);
        currentObstacle = new Obstacle(Config.OBSTACLE_SIZE, xSpawn, Config.FRAME_HEIGHT - Config.GROUND_Y - Config.OBSTACLE_SIZE, dx, 0, Color.BLACK);
    }
}
