package evironment.antGame;

import core.StepResultEnvironment;

public class AntWorldContinuous extends AntWorld {
    public AntWorldContinuous(int width, int height) {
        super(width, height);
    }

    public AntWorldContinuous() {
        super();
    }

    @Override
    public StepResultEnvironment step(AntAction action) {
        AntObservation observation;
        Cell currentCell = grid.getCell(myAnt.getPos());

        StepCalculation sc = processStep(action);

        // flag is set to true if food gets dropped onto starts
        if(sc.checkCompletion) {
            grid.spawnNewFood();
        }
        // valid movement
        if(!sc.stayOnCell) {
            myAnt.getPos().setLocation(sc.potentialNextPos);
        }

        // get observation after action was computed
        observation = new AntObservation(grid.getCell(myAnt.getPos()), myAnt.getPos(), myAnt.hasFood());

        return new StepResultEnvironment(new AntState(grid.getGrid(), observation.getPos(), observation.hasFood()), sc.reward, false, sc.info);
    }

}
