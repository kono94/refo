package evironment.jumpingDino;

import core.RNG;
import core.State;

import java.awt.*;

/**
 * 3580 states
 * if:
 * dx = -(int)((Math.random() + 0.5) * Config.OBSTACLE_SPEED);
 * xSpawn = Config.FRAME_WIDTH + Config.FRAME_WIDTH  + Config.OBSTACLE_SIZE;
 *
 * 350 states
 * if 4 speed variants
 */
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
        double ran = RNG.getRandom().nextDouble();
        if(ran < 0.25){
            dx = -(int)(0.7 * Config.OBSTACLE_SPEED);
        }else if(ran < 0.5){
            dx = -(int)(1.3 * Config.OBSTACLE_SPEED);
        }else if(ran < 0.75){
            dx = -(int)(1.6 * Config.OBSTACLE_SPEED);
        } else{
            dx = -2 * Config.OBSTACLE_SPEED;
        }
        // randomly spawning more right outside of the screen
        xSpawn = Config.FRAME_WIDTH + Config.FRAME_WIDTH  + Config.OBSTACLE_SIZE;
        currentObstacle = new Obstacle(Config.OBSTACLE_SIZE, xSpawn, Config.FRAME_HEIGHT - Config.GROUND_Y - Config.OBSTACLE_SIZE, dx, 0, Color.BLACK);
    }
}
