package evironment.antGame;

import core.State;
import core.StepResultEnvironment;

public class AntWorldContinuous extends AntWorld {
    public AntWorldContinuous(int width, int height, int numberOfConcurrentFood) {
        super(width, height, numberOfConcurrentFood);
    }

    public AntWorldContinuous() {
        super();
    }

    @Override
    public StepResultEnvironment step(AntAction action) {
        StepCalculation sc = processStep(action);

        // flag is set to true if food gets dropped onto starts
        if(sc.foodCollected) {
            grid.removeAllFood();
            System.out.println(numberOfConcurrentFood);
            for(int i = 0; i < numberOfConcurrentFood; ++i) {
                grid.spawnNewFood();
            }
        }
        // valid movement
        if(!sc.stayOnCell) {
            myAnt.getPos().setLocation(sc.potentialNextPos);
        }

        return new StepResultEnvironment(generateReturnState(), sc.reward, false, sc.info);
    }

    @Override
    protected State generateReturnState(){
        AntObservation observation = new AntObservation(grid.getCell(myAnt.getPos()), myAnt.getPos(), myAnt.hasFood());
        return new AntState(grid.getGrid(), observation.getPos(), observation.hasFood());
    }

}
