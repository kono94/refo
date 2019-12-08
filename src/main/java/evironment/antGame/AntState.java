package evironment.antGame;

import core.State;

// somewhat the "brain" of the agent, current known setting of the environment
public class AntState implements State {
    private Grid knownGrid;

    public AntState(int width, int height){
        knownGrid = new Grid(width, height);

    }

    public AntState(){
        this(Constants.DEFAULT_GRID_WIDTH, Constants.DEFAULT_GRID_HEIGHT);
    }
}
