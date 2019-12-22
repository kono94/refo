package evironment.jumpingDino;

import core.Environment;
import core.State;
import core.StepResultEnvironment;
import core.gui.Visualizable;
import evironment.jumpingDino.gui.DinoWorldComponent;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class DinoWorld implements Environment<DinoAction>, Visualizable {
    private Dino dino;
    private Obstacle currentObstacle;

    public DinoWorld(){
        dino = new Dino(Config.DINO_SIZE, Config.DINO_STARTING_X, Config.FRAME_HEIGHT - Config.GROUND_Y - Config.DINO_SIZE, 0, 0, Color.GREEN);
        spawnNewObstacle();
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
    private int getDistanceToObstacle(){
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
        if(currentObstacle.getX() < -Config.OBSTACLE_SIZE){
            spawnNewObstacle();
        }

        if(ranIntoObstacle()){
            done = true;
        }

        return new StepResultEnvironment(new DinoState(getDistanceToObstacle()), reward, done, "");
    }
    private void spawnNewObstacle(){
        currentObstacle = new Obstacle(Config.OBSTACLE_SIZE, Config.FRAME_WIDTH + Config.OBSTACLE_SIZE, Config.FRAME_HEIGHT - Config.GROUND_Y - Config.OBSTACLE_SIZE, -Config.OBSTACLE_SPEED, 0, Color.BLACK);
    }

    private void spawnDino(){
        dino = new Dino(Config.DINO_SIZE, Config.DINO_STARTING_X, Config.FRAME_HEIGHT - Config.GROUND_Y - Config.DINO_SIZE, 0, 0, Color.GREEN);
    }
    @Override
    public State reset() {
        spawnDino();
        spawnNewObstacle();
        return new DinoState(getDistanceToObstacle());
    }

    @Override
    public JComponent visualize() {
        return new DinoWorldComponent(this);
    }
}
