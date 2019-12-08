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
    private Cell[][] knownWorld;
    private Point pos;
    private boolean hasFood;


    public AntState(Cell[][] knownWorld, Point antPosition, boolean hasFood){
        this.knownWorld = deepCopyCellGrid(knownWorld);
        this.pos = deepCopyAntPosition(antPosition);
        this.hasFood = hasFood;
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
        int hash = 7;
        int prime = 31;
        hash = prime * hash + Arrays.hashCode(knownWorld);
        hash = prime * hash + (hasFood ? 1:0);
        hash = prime * hash + pos.hashCode();
        return hash;
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
