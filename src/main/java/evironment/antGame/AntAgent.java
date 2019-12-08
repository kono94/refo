package evironment.antGame;


import java.awt.*;

public class AntAgent {
    // the learned representation of the environment (gridWorld)
    private Cell[][] knownWorld;

    public AntAgent(int width, int height){
        knownWorld = new Cell[width][height];
        initUnknownWorld();
    }

    /**
     * Learn from observation received after last action
     * and generate a markov state.
     *
     * @param observation received input from the game host (environment)
     * @return the current state of the agent
     */
    public AntState feedObservation(AntObservation observation){
        knownWorld[observation.getPos().x][observation.getPos().y] = observation.getCell();
        return new AntState(knownWorld, observation.getPos(), observation.hasFood());
    }

    private void initUnknownWorld(){
        for(int x = 0; x < knownWorld.length; ++x){
            for(int y = 0; y < knownWorld[x].length; ++y){
                knownWorld[x][y] = new Cell(new Point(x,y), CellType.UNKNOWN);
            }
        }
    }

    public Cell getCell(Point pos){
        return knownWorld[pos.x][pos.y];
    }
}
