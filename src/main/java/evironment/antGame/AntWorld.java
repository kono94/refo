package evironment.antGame;

import core.*;
import core.algo.Learning;
import core.algo.MC.MonteCarloOnPolicyEGreedy;
import evironment.antGame.gui.MainFrame;


import java.awt.*;

public class AntWorld implements Environment<AntAction>{
    /**
     *
     */
    private Grid grid;
    /**
     * Intern (backend) representation of the ant.
     * The AntWorld essentially acts like the game host of the original AntGame.
     */
    private Ant myAnt;
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

    private int tick;
    private int maxEpisodeTicks;
    MainFrame gui;

    public AntWorld(int width, int height, double foodDensity){
        grid = new Grid(width, height, foodDensity);
        antAgent = new AntAgent(width, height);
        myAnt = new Ant();
        gui = new MainFrame(this, antAgent);
        maxEpisodeTicks = 1000;
        reset();
    }

    public MainFrame getGui(){
        return gui;
    }

    public AntWorld(){
        this(Constants.DEFAULT_GRID_WIDTH, Constants.DEFAULT_GRID_HEIGHT, Constants.DEFAULT_FOOD_DENSITY);
    }

    @Override
    public StepResultEnvironment step(AntAction action){
        AntObservation observation;
        State newState;
        double reward = 0;
        String info = "";
        boolean done = false;

        Cell currentCell = grid.getCell(myAnt.getPos());
        Point potentialNextPos = new Point(myAnt.getPos().x, myAnt.getPos().y);
        boolean stayOnCell = true;
        // flag to enable a check if all food has been collected only fired if food was dropped
        // on the starting position
        boolean checkCompletion = false;

        switch (action) {
            case MOVE_UP:
                potentialNextPos.y -= 1;
                stayOnCell = false;
                break;
            case MOVE_RIGHT:
                potentialNextPos.x += 1;
                stayOnCell = false;
                break;
            case MOVE_DOWN:
                potentialNextPos.y += 1;
                stayOnCell = false;
                break;
            case MOVE_LEFT:
                potentialNextPos.x -= 1;
                stayOnCell = false;
                break;
            case PICK_UP:
                if(myAnt.hasFood()){
                    // Ant tries to pick up food but can only hold one piece
                    reward = Reward.FOOD_PICK_UP_FAIL_HAS_FOOD_ALREADY;
                }else if(currentCell.getFood() == 0){
                    // Ant tries to pick up food on cell that has no food on it
                    reward = Reward.FOOD_PICK_UP_FAIL_NO_FOOD;
                }else if(currentCell.getFood() > 0){
                    // Ant successfully picks up food
                    currentCell.setFood(currentCell.getFood() - 1);
                    myAnt.setHasFood(true);
                    reward = Reward.FOOD_PICK_UP_SUCCESS;
                }
                break;
            case DROP_DOWN:
                if(!myAnt.hasFood()){
                    // Ant had no food to drop
                    reward = Reward.FOOD_DROP_DOWN_FAIL_NO_FOOD;
                }else{
                    // Drop food onto the ground
                    currentCell.setFood(currentCell.getFood() + 1);
                    myAnt.setHasFood(false);

                    // negative reward if the agent drops food on any other field
                    // than the starting point
                    if(currentCell.getType() != CellType.START){
                        reward = Reward.FOOD_DROP_DOWN_FAIL_NOT_START;
                    }else{
                        reward = Reward.FOOD_DROP_DOWN_SUCCESS;
                        myAnt.setPoints(myAnt.getPoints() + 1);
                        checkCompletion = true;
                    }
                }
                break;
            default:
                throw new RuntimeException(String.format("Action <%s> is not a valid action!", action.toString()));
        }

        // movement action was selected
        if(!stayOnCell){
            if(!isInGrid(potentialNextPos)){
                stayOnCell = true;
                reward = Reward.RAN_INTO_WALL;
            }else if(hitObstacle(potentialNextPos)){
                stayOnCell = true;
                reward = Reward.RAN_INTO_OBSTACLE;
            }
        }

        // valid movement
        if(!stayOnCell){
            myAnt.getPos().setLocation(potentialNextPos);
            if(antAgent.getCell(myAnt.getPos()).getType() == CellType.UNKNOWN){
                // the ant will move to a cell that was previously unknown
                reward = Reward.UNKNOWN_FIELD_EXPLORED;
            }else{
                reward = 0;
            }
        }

        // get observation after action was computed
        observation = new AntObservation(grid.getCell(myAnt.getPos()), myAnt.getPos(), myAnt.hasFood());

        // let the ant agent process the observation to create a valid markov state
        newState = antAgent.feedObservation(observation);

        if(checkCompletion){
            done = grid.isAllFoodCollected();
        }

        if(++tick == maxEpisodeTicks){
            done = true;
        }

        StepResultEnvironment result = new StepResultEnvironment(newState, reward, done, info);
        getGui().update(action, result);
        return result;
    }

    private boolean isInGrid(Point pos){
        return pos.x >= 0 && pos.x < grid.getWidth() && pos.y >= 0 && pos.y < grid.getHeight();
    }

    private boolean hitObstacle(Point pos){
        return grid.getCell(pos).getType() == CellType.OBSTACLE;
    }

    public State reset() {
        RNG.reseed();
        grid.initRandomWorld();
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

    public static void main(String[] args) {
        RNG.setSeed(1993);

        Learning<AntAction> monteCarlo = new MonteCarloOnPolicyEGreedy<>(
                new AntWorld(3, 3, 0.1),
                new ListDiscreteActionSpace<>(AntAction.values())
        );
        monteCarlo.learn(100,5);
    }
}
