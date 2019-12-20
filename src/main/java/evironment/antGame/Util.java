package evironment.antGame;

public class Util {
    public static Cell[][] deepCopyCellGrid(Cell[][] toCopy){
        Cell[][] cells = new Cell[toCopy.length][toCopy[0].length];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                // calling copy constructor of Cell class
                cells[i][j] = new Cell(toCopy[i][j]);
            }
        }
        return cells;
    }
}
