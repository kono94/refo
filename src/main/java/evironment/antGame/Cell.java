package evironment.antGame;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;

public class Cell {
    @Getter
    @Setter
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

    public Cell(Cell c){
        this.pos = new Point(c.pos.x, c.pos.y);
        this.food = c.getFood();
        this.type = c.getType();
    }

    public Cell( Point pos, CellType cellType){
       this(pos, cellType, 0);
    }

    @Override
    public boolean equals(Object obj){
        if(obj instanceof Cell){
            Cell cell = (Cell) obj;
            return this.type == cell.getType() && this.food == cell.getFood() && this.pos.x == cell.getPos().x && this.pos.y ==cell.getPos().y;
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "type=" + type +
                ", food=" + food +
                ", pos=" + pos +
                '}';
    }
}
