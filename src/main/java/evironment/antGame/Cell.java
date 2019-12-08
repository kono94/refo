package evironment.antGame;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class Cell {
    @Getter
    private CellType type;
    @Getter
    @Setter
    private int food;
    @Getter
    private Point pos;

    public Cell(Point pos, CellType cellType, int foodAmount){
        this.pos = pos;
        type = cellType;
        food = foodAmount;
    }

    public Cell( Point pos, CellType cellType){
       this(pos, cellType, 0);
    }

}
