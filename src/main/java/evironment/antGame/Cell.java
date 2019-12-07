package evironment.antGame;

public class Cell {
    private CellType type;
    private int food;

    public Cell(CellType cellType, int foodAmount){
        type = cellType;
        food = foodAmount;
    }

    public Cell(CellType cellType){
       this(cellType, 0);
    }

    public void setFoodCount(int amount){
        food = amount;
    }

    public int getFoodCount(){
        return food;
    }
}
