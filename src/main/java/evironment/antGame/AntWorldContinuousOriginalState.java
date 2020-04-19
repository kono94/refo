package evironment.antGame;

import core.State;

public class AntWorldContinuousOriginalState extends AntWorldContinuous {
    public AntWorldContinuousOriginalState(int width, int height, int numberOfConcurrentFood) {
        super(width, height, numberOfConcurrentFood);
    }

    public AntWorldContinuousOriginalState() {
        super();
    }

    @Override
    protected State generateReturnState(){
       return new AntStateOriginal(myAnt.hasFood()? 1:0, myAnt.getPos().x, myAnt.getPos().y, grid.getCell(myAnt.getPos()).getType(), calculateSmell(), grid.getCell(myAnt.getPos()).getFood());
    }

    /**
     * @return total smell of neighbour food cells
     */
    private int calculateSmell(){
        int smell = 0;
        int maxX = grid.getGrid().length -1;
        int maxY = grid.getGrid()[0].length -1;
        int antX = myAnt.getPos().x;
        int antY = myAnt.getPos().y;

        smell += antY > 0 ? grid.getCell(antX, antY - 1).getFood() : 0;
        smell += antY < maxY ? grid.getCell(antX, antY + 1).getFood() : 0;
        smell += antX > 0 ? grid.getCell(antX - 1, antY).getFood() : 0;
        smell += antX < maxX ? grid.getCell(antX + 1, antY).getFood() : 0;

        return smell;
    }
}
