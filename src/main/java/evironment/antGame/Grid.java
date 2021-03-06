package evironment.antGame;

import core.RNG;

import java.awt.*;

public class Grid {
    private int width;
    private int height;
    private Point start;
    private Cell[][] grid;
    private Cell[][] initialGrid;
    private int numberOfConcurrentFood;

    public Grid(int width, int height, int numberOfConcurrentFood) {
        this.width = width;
        this.height = height;
        this.numberOfConcurrentFood = numberOfConcurrentFood;
        grid = new Cell[width][height];
        initialGrid = new Cell[width][height];
        start = new Point(Constants.START_X, Constants.START_Y);
        initRandomWorld();
    }

    public void resetWorld(){
        grid = Util.deepCopyCellGrid(initialGrid);
    }

    public void initRandomWorld(){
        for(int x = 0; x < width; ++x){
            for(int y = 0; y < height; ++y){
                initialGrid[x][y] = new Cell(new Point(x, y), CellType.FREE);
            }
        }
        spawnObstacles();
        initialGrid[start.x][start.y] = new Cell(new Point(start.x, start.y), CellType.START);
        for(int i = 0; i < numberOfConcurrentFood; ++i) {
            spawnNewFood(initialGrid);
        }
    }

    //TODO
    private void spawnObstacles() {
        initialGrid[3][1].setType(CellType.OBSTACLE);
        initialGrid[4][1].setType(CellType.OBSTACLE);
        initialGrid[5][1].setType(CellType.OBSTACLE);
        initialGrid[6][1].setType(CellType.OBSTACLE);
        initialGrid[7][1].setType(CellType.OBSTACLE);
        initialGrid[3][2].setType(CellType.OBSTACLE);
        initialGrid[3][3].setType(CellType.OBSTACLE);
        initialGrid[3][4].setType(CellType.OBSTACLE);
        initialGrid[4][4].setType(CellType.OBSTACLE);
        initialGrid[5][4].setType(CellType.OBSTACLE);
        initialGrid[6][4].setType(CellType.OBSTACLE);
    }

    /**
     * Spawns one additional food on a random field EXCEPT for the starting position
     */
    private void spawnNewFood(Cell[][] grid) {
        boolean foodSpawned = false;
        Point potFood = new Point(0, 0);
        CellType potFieldType;
        while(!foodSpawned) {
            potFood.x = RNG.getRandomEnv().nextInt(width);
            potFood.y = RNG.getRandomEnv().nextInt(height);
            potFieldType = grid[potFood.x][potFood.y].getType();
            if(potFieldType != CellType.START && grid[potFood.x][potFood.y].getFood() == 0 && potFieldType != CellType.OBSTACLE) {
                grid[potFood.x][potFood.y].setFood(1);
                foodSpawned = true;
               // System.out.println("spawned new food at " + potFood);
               // System.out.println(initialGrid[potFood.x][potFood.y]);
            }
        }
    }

    public void removeAllFood() {
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                grid[x][y].setFood(0);
            }
        }
    }

    public void spawnNewFood() {
        spawnNewFood(grid);
    }

    public Point getStartPoint(){
        return start;
    }

    public boolean isAllFoodCollected(){
        for(int x = 0; x < width; ++x){
            for(int y = 0; y < height; ++y){
              if(grid[x][y].getFood() > 0){
                  return false;
              }
            }
        }

        return true;
    }

    public Cell[][] getGrid(){
        return grid;
    }

    public Cell getCell(Point pos){
        return grid[pos.x][pos.y];
    }
    public Cell getCell(int x, int y){
        return grid[x][y];
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

}
