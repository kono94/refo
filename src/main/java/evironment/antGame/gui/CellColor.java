package evironment.antGame.gui;

import evironment.antGame.CellType;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CellColor {
    public static final Map<CellType, Color> map;
    static {
        map = new HashMap<>();
        map.put(CellType.FREE, Color.GREEN);
        map.put(CellType.START, Color.BLUE);
        map.put(CellType.UNKNOWN, Color.GRAY);
        map.put(CellType.OBSTACLE, Color.RED);
    }
}
