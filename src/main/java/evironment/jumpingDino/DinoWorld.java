package evironment.jumpingDino;

import core.Environment;
import core.State;
import core.StepResultEnvironment;
import core.gui.Visualizable;
import evironment.jumpingDino.gui.DinoWorldComponent;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * 57 states
 */
@Getter
public class DinoWorld implements Environment<DinoAction>, Visualizable {
    protected Dino dino;
    protected Obstacle currentObstacle;
    private DinoWorldComponent comp;

    public DinoWorld(){
        dino = new Dino(Config.DINO_SIZE, Config.DINO_STARTING_X, Config.FRAME_HEIGHT - Config.GROUND_Y - Config.DINO_SIZE, 0, 0, Color.GREEN);
        spawnNewObstacle();
        comp = new DinoWorldComponent(this);
    }
    private boolean ranIntoObstacle(){
        Obstacle o = currentObstacle;
        Dino p = dino;

        boolean xAxis = (o.getX() <= p.getX() && p.getX() < o.getX() + Config.OBSTACLE_SIZE)
                || (o.getX() <= p.getX() + Config.DINO_SIZE && p.getX() + Config.DINO_SIZE < o.getX() + Config.OBSTACLE_SIZE);

        boolean yAxis = (o.getY() <= p.getY() && p.getY() < o.getY() + Config.OBSTACLE_SIZE)
                || (o.getY() <= p.getY() + Config.DINO_SIZE && p.getY() + Config.DINO_SIZE < o.getY() + Config.OBSTACLE_SIZE);

        return xAxis && yAxis;
    }

    protected int getDistanceToObstacle(){
        return currentObstacle.getX() - dino.getX() + Config.DINO_SIZE;
    }

    @Override
    public StepResultEnvironment step(DinoAction action) {
        boolean done = false;
        int reward = 1;

        if(action == DinoAction.JUMP){
            dino.jump();
        }
        dino.tick();
        currentObstacle.tick();
        if(currentObstacle.getX() < -Config.OBSTACLE_SIZE) {
            spawnNewObstacle();
        }
        if(ranIntoObstacle()) {
            reward = 0;
            done = true;
        }

        return new StepResultEnvironment(generateReturnState(), reward, done, "");
    }

    protected State generateReturnState(){
        return new DinoStateSimple(getDistanceToObstacle());
    }
    protected State generateReturnState(){
        return new DinoState(getDistanceToObstacle(), dino.isInJump());
    }

    protected void spawnNewObstacle(){
        int dx;
        int xSpawn;

        dx = -Config.OBSTACLE_SPEED;
        // instantly respawning on the left screen border
        xSpawn = Config.FRAME_WIDTH + Config.OBSTACLE_SIZE;

        currentObstacle = new Obstacle(Config.OBSTACLE_SIZE, xSpawn, Config.FRAME_HEIGHT - Config.GROUND_Y - Config.OBSTACLE_SIZE, dx, 0, Color.BLACK);
    }

    private void spawnDino(){
        dino = new Dino(Config.DINO_SIZE, Config.DINO_STARTING_X, Config.FRAME_HEIGHT - Config.GROUND_Y - Config.DINO_SIZE, 0, 0, Color.GREEN);
    }

    @Override
    public State reset() {
        spawnDino();
        spawnNewObstacle();
        return generateReturnState();
    }

    @Override
    public JComponent visualize() {
        return comp;
    }
}
