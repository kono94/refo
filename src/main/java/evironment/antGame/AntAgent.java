package evironment.antGame;


import java.awt.*;

public class AntAgent {
    // the brain
    private Cell[][] knownWorld;
    private Point pos;

    public AntAgent(int width, int height){
        knownWorld = new Cell[width][height];
        initUnknownWorld();
    }

    public AntState feedObservation(AntObservation observation){

    }

    private void initUnknownWorld(){
        for(int x = 0; x < knownWorld.length; ++x){
            for(int y = 0; y < knownWorld[x].length; ++y){
                knownWorld[x][y] = new Cell(new Point(x,y), CellType.UNKNOWN);
            }
        }
    }

    public Point getPos(){
        return pos;
    }
}
