package evironment.antGame;

import core.RNG;

import java.awt.*;

public class Grid {
    private int width;
    private int height;
    private double foodDensity;
    private Point start;
    private Cell[][] grid;

    public Grid(int width, int height, double foodDensity){
        this.width = width;
        this.height = height;
        this.foodDensity = foodDensity;

        grid = new Cell[width][height];
    }

    public Grid(int width, int height){
        this(width, height, 0);
    }

    public void initRandomWorld(){
        for(int x = 0; x < width; ++x){
            for(int y = 0; y < height; ++y){
                if( RNG.getRandom().nextDouble() < foodDensity){
                    grid[x][y] = new Cell(new Point(x,y), CellType.FOOD, 1);
                }else{
                    grid[x][y] = new Cell(new Point(x,y), CellType.FREE);
                }
            }
        }
        start = new Point(RNG.getRandom().nextInt(width), RNG.getRandom().nextInt(height));
        grid[start.x][start.y] = new Cell(new Point(start.x, start.y), CellType.START);
    }

    public Point getStartPoint(){
        return start;
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
