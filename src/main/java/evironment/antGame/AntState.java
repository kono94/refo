package evironment.antGame;

import core.State;

import java.awt.*;
import java.util.Arrays;

/**
 * Markov state of the experienced environment.
 * Essentially a snapshot of the current Ant Agent
 * and therefor has to be deep copied
 */
public class AntState implements State {
    private final Cell[][] knownWorld;
    private final Point pos;
    private final boolean hasFood;
    private final int computedHash;

    public AntState(Cell[][] knownWorld, Point antPosition, boolean hasFood){
        this.knownWorld = deepCopyCellGrid(knownWorld);
        this.pos = deepCopyAntPosition(antPosition);
        this.hasFood = hasFood;
        computedHash = computeHash();
    }

    private int computeHash(){
        int hash = 7;
        int prime = 31;

        int unknown = 0;
        int diff = 0;
        for (int i = 0; i < knownWorld.length; i++) {
            for (int j = 0; j < knownWorld[i].length; j++) {
                if(knownWorld[i][j].getType() == CellType.UNKNOWN){
                    unknown += 1;
                }else{
                    diff +=1;
                }
            }
        }
        hash = prime * hash + unknown;
        hash = prime * hash * diff;
        hash = prime * hash + (hasFood ? 1:0);
        hash = prime * hash + pos.hashCode();
        return hash;
    }
    private Cell[][] deepCopyCellGrid(Cell[][] toCopy){
        Cell[][] cells = new Cell[toCopy.length][toCopy[0].length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                // calling copy constructor of Cell class
                cells[i][j] = new Cell(toCopy[i][j]);
            }
        }
        return cells;
    }

    private Point deepCopyAntPosition(Point toCopy){
        return new Point(toCopy.x,toCopy.y);
    }

    @Override
    public String toString(){
        return String.format("Pos: %s, hasFood: %b, knownWorld: %s", pos.toString(), hasFood, Arrays.toString(knownWorld));
}

    //TODO: make this a utility function to generate hash Code based upon 2 prime numbers
    @Override
    public int hashCode(){
       return computedHash;
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof AntState){
            AntState toCompare = (AntState) obj;
            if(!this.pos.equals(toCompare.pos) || !this.hasFood == toCompare.hasFood){
                return false;
            }
            for (int i = 0; i < toCompare.knownWorld.length; i++) {
                for (int j = 0; j < toCompare.knownWorld[i].length; j++) {
                    if(!this.knownWorld[i][j].equals(toCompare.knownWorld[i][j])){
                        return false;
                    }

                }
            }
            return true;
        }
        return  super.equals(obj);
    }
}
