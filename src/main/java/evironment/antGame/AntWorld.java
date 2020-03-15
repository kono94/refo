package evironment.antGame;

import core.Environment;
import core.State;
import core.StepResultEnvironment;
import core.gui.Visualizable;
import evironment.antGame.gui.AntWorldComponent;

import javax.swing.*;
import java.awt.*;

/**
 * Episodic AntWorld
 */
public class AntWorld implements Environment<AntAction>, Visualizable {
    /**
     *
     */
    protected Grid grid;
    /**
     * Intern (backend) representation of the ant.
     * The AntWorld essentially acts like the game host of the original AntGame.
     */
    protected Ant myAnt;
    /**
     * The client agent. In the original AntGame the host would send jade messages
     * of the current observation to each client on every tick.
     * In this reinforcement learning environment, the agent is part of
     * "backend" to make this environment an MDP. The environment should (convention of
     * openGym) return all vital information from the .step() method (nextState, reward, done).
     * But the antGame itself only returns observation for each ant on each tick. These
     * observation are not markov, hence a "middleware" has to compute the unique markov states
     * based upon these receiving observation -> the (client) ant!
     * The AntAgent has an intern strategy to generate markov states from observations,
     * through an intern grid clone (brain), for example. A history as mentioned in
     * various lectures could be possible as well.
     */
    protected AntAgent antAgent;

    protected int tick;
    private int maxEpisodeTicks;

    public AntWorld(int width, int height) {
        grid = new Grid(width, height);
        antAgent = new AntAgent(width, height);
        myAnt = new Ant();
        maxEpisodeTicks = 1000;
        reset();
    }

    public AntWorld(){
        this(Constants.DEFAULT_GRID_WIDTH, Constants.DEFAULT_GRID_HEIGHT);
    }

    protected StepCalculation processStep(AntAction action) {
        StepCalculation sc = new StepCalculation();
        sc.reward = -1;
        sc.info = "";
        sc.done = false;
        Cell currentCell = grid.getCell(myAnt.getPos());
        sc.potentialNextPos = new Point(myAnt.getPos().x, myAnt.getPos().y);
        sc.stayOnCell = true;
        // flag to enable a check if all food has been collected only fired if food was dropped
        // on the starting position
        sc.checkCompletion = false;

        switch(action) {
            case MOVE_UP:
                sc.potentialNextPos.y -= 1;
                sc.stayOnCell = false;
                break;
            case MOVE_RIGHT:
                sc.potentialNextPos.x += 1;
                sc.stayOnCell = false;
                break;
            case MOVE_DOWN:
                sc.potentialNextPos.y += 1;
                sc.stayOnCell = false;
                break;
            case MOVE_LEFT:
                sc.potentialNextPos.x -= 1;
                sc.stayOnCell = false;
                break;
            case PICK_UP:
                if(myAnt.hasFood()) {
                    // Ant tries to pick up food but can only hold one piece
                    sc.reward += Reward.FOOD_PICK_UP_FAIL_HAS_FOOD_ALREADY;
                } else if(currentCell.getFood() == 0) {
                    // Ant tries to pick up food on cell that has no food on it
                    sc.reward += Reward.FOOD_PICK_UP_FAIL_NO_FOOD;
                } else if(currentCell.getFood() > 0) {
                    // Ant successfully picks up food
                    currentCell.setFood(currentCell.getFood() - 1);
                    myAnt.setHasFood(true);
                    sc.reward = Reward.FOOD_PICK_UP_SUCCESS;
                }
                break;
            case DROP_DOWN:
                if(!myAnt.hasFood()) {
                    // Ant had no food to drop
                    sc.reward += Reward.FOOD_DROP_DOWN_FAIL_NO_FOOD;
                } else {
                    myAnt.setHasFood(false);
                    // negative reward if the agent drops food on any other field
                    // than the starting point
                    if(currentCell.getType() != CellType.START) {
                        sc.reward += Reward.FOOD_DROP_DOWN_FAIL_NOT_START;
                        // Drop food onto the ground
                        currentCell.setFood(currentCell.getFood() + 1);
                    } else {
                        sc.reward = Reward.FOOD_DROP_DOWN_SUCCESS;
                        myAnt.setPoints(myAnt.getPoints() + 1);
                        sc.checkCompletion = true;
                    }
                }
                break;
            default:
                throw new RuntimeException(String.format("Action <%s> is not a valid action!", action.toString()));
        }

        // movement action was selected
        if(!sc.stayOnCell) {
            if(!isInGrid(sc.potentialNextPos)) {
                sc.stayOnCell = true;
                sc.reward += Reward.RAN_INTO_WALL;
            } else if(hitObstacle(sc.potentialNextPos)) {
                sc.stayOnCell = true;
                sc.reward += Reward.RAN_INTO_OBSTACLE;
            }
        }

        return sc;
    }

    @Override
    public StepResultEnvironment step(AntAction action){
        AntObservation observation;
        State newState;

        StepCalculation sc = processStep(action);

        // valid movement
        if(!sc.stayOnCell) {
            myAnt.getPos().setLocation(sc.potentialNextPos);
            if(antAgent.getCell(myAnt.getPos()).getType() == CellType.UNKNOWN){
                // the ant will move to a cell that was previously unknown
                // TODO: not optimal for going straight for food
                // sc.reward = Reward.UNKNOWN_FIELD_EXPLORED;
            }
        }

        // get observation after action was computed
        observation = new AntObservation(grid.getCell(myAnt.getPos()), myAnt.getPos(), myAnt.hasFood());

        // let the ant agent process the observation to create a valid markov state
        newState = antAgent.feedObservation(observation);

        if(sc.checkCompletion) {
            sc.done = grid.isAllFoodCollected();
        }

        if(++tick == maxEpisodeTicks){
            sc.done = true;
        }

        return new StepResultEnvironment(newState, sc.reward, sc.done, sc.info);
    }

    protected boolean isInGrid(Point pos) {
        return pos.x >= 0 && pos.x < grid.getWidth() && pos.y >= 0 && pos.y < grid.getHeight();
    }

    protected boolean hitObstacle(Point pos) {
        return grid.getCell(pos).getType() == CellType.OBSTACLE;
    }

    protected class StepCalculation {
        double reward;
        String info;
        boolean done;
        Point potentialNextPos = new Point(myAnt.getPos().x, myAnt.getPos().y);
        boolean stayOnCell = true;
        // flag to enable a check if all food has been collected only fired if food was dropped
        // on the starting position
        boolean checkCompletion = false;
    }

    public State reset() {
        grid.resetWorld();
        antAgent.initUnknownWorld();
        tick = 0;
        myAnt.getPos().setLocation(grid.getStartPoint());
        myAnt.setPoints(0);
        myAnt.setHasFood(false);
        AntObservation observation = new AntObservation(grid.getCell(myAnt.getPos()), myAnt.getPos(), myAnt.hasFood());
        return antAgent.feedObservation(observation);
    }

    public void setMaxEpisodeLength(int maxTicks){
        this.maxEpisodeTicks = maxTicks;
    }

    public Point getSpawningPoint(){
        return grid.getStartPoint();
    }

    public Cell[][] getCellArray(){
        return grid.getGrid();
    }

    public int getTick(){
        return tick;
    }

    public Ant getAnt(){
        return myAnt;
    }

    @Override
    public JComponent visualize() {
        return new AntWorldComponent(this, this.antAgent);
    }
}
