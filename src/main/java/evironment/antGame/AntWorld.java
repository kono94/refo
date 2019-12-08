package evironment.antGame;

import core.*;

import java.awt.*;

public class AntWorld {
    /**
     * 
     */
    private Grid grid;
    /**
     * Intern (backend) representation of the ant.
     * The AntWorld essentially acts like the game host of the original AntGame.
     */
    private MyAnt myAnt;
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
    private AntAgent antAgent;

    public AntWorld(int width, int height, double foodDensity){
        grid = new Grid(width, height, foodDensity);
        antAgent = new AntAgent(width, height);
    }

    public AntWorld(){
        this(Constants.DEFAULT_GRID_WIDTH, Constants.DEFAULT_GRID_HEIGHT, Constants.DEFAULT_FOOD_DENSITY);
    }

    private static class MyAnt{
        int x,y;
        boolean hasFood;
        boolean spawned;
    }

    public StepResult step(DiscreteAction<AntAction> action){
        AntObservation observation;
        State newState;
        if(!myAnt.spawned){
            observation = new AntObservation(grid.getCell(grid.getStartPoint()));
            newState = antAgent.feedObservation(observation);
            return new StepResult(newState, 0.0, false, "Just spawned on the map");
        }
        switch (action.getValue()) {
            case MOVE_UP:
                break;
            case MOVE_RIGHT:
                break;
            case MOVE_DOWN:
                break;
            case MOVE_LEFT:
                break;
            case PICK_UP:
                break;
            case DROP_DOWN:
                break;
            default:
                throw new RuntimeException(String.format("Action <%s> is not a valid action!", action.toString()));
                break;
        }
        newState = antAgent.feedObservation(observation);
        return new StepResult(newState, 0.0, false, "");
    }

    public void reset() {
        RNG.reseed();
        grid.initRandomWorld();
        myAnt = new MyAnt();
    }

    public Point getSpawningPoint(){
        return grid.getStartPoint();
    }
}
