package evironment.antGame;

import core.DiscreteAction;
import core.Observation;
import core.RNG;
import core.StepResult;

public class AntWorld {
    private Grid grid;

    public AntWorld(int width, int height, double foodDensity){
        grid = new Grid(width, height, foodDensity);
    }

    public AntWorld(){
        this(30, 30, 0.1);
    }

    public StepResult step(DiscreteAction<AntAction> action){
        Observation observation = new AntObservation();
        return new StepResult(observation, 0.0, false, "");
    }

    public void reset(){
        RNG.reseed();
        grid.initCells();
    }
}
