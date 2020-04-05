package evironment.antGame;

import core.State;
import core.gui.Visualizable;
import evironment.antGame.gui.CellColor;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/**
 * Markov state of the experienced environment.
 * Essentially a snapshot of the current Ant Agent
 * and therefor has to be deep copied
 */
public class AntState implements State, Visualizable {
    private final Cell[][] knownWorld;
    private final Point pos;
    private final boolean hasFood;
    private final int computedHash;

    public AntState(Cell[][] knownWorld, Point antPosition, boolean hasFood){
        this.knownWorld = Util.deepCopyCellGrid(knownWorld);
        this.pos = deepCopyAntPosition(antPosition);
        this.hasFood = hasFood;
        computedHash = computeHash();
    }

    private int computeHash() {
        int hash = 7;
        int prime = 31;
        int unknown = 0;
        int diff = 0;
        for (Cell[] cells : knownWorld) {
            for (Cell cell : cells) {
                if (cell.getType() == CellType.UNKNOWN) {
                    unknown += 1;
                } else {
                    diff += 1;
                }
            }
        }
        hash = prime * hash + unknown;
        hash = prime * hash * diff;
        hash = prime * hash + (hasFood ? 1 : 0);
        hash = prime * hash + pos.hashCode();
        return hash;
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
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

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

    @Override
    public JComponent visualize() {
        return new JScrollPane() {
            private int cellSize;
            private Font font;
            {
                int paneWidth = 500;
                int paneHeight = 500;
                setPreferredSize(new Dimension(paneWidth, paneHeight));
                cellSize = (paneWidth - knownWorld.length) / knownWorld.length;
                font = new Font("plain", Font.BOLD, cellSize);
                JPanel worldPanel = new JPanel(){
                    {
                        setPreferredSize(new Dimension(knownWorld.length * cellSize, knownWorld[0].length * cellSize));
                        setVisible(true);

                        addMouseWheelListener(e -> {
                            if(e.getWheelRotation() > 0){
                                cellSize -= 1;
                            }else {
                                cellSize += 1;
                            }
                            font = new Font("plain", Font.BOLD, cellSize);
                            setPreferredSize(new Dimension(knownWorld.length * cellSize, knownWorld[0].length * cellSize));
                            revalidate();
                            repaint();
                        });
                    }

                    @Override
                    public void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        for (int i = 0; i < knownWorld.length; i++) {
                            for (int j = 0; j < knownWorld[0].length; j++) {
                                g.setColor(Color.BLACK);
                                g.drawRect(i*cellSize, j*cellSize, cellSize, cellSize);
                                g.setColor(CellColor.map.get(knownWorld[i][j].getType()));
                                if(knownWorld[i][j].getFood() > 0){
                                    g.setColor(Color.YELLOW);
                                }
                                g.fillRect(i*cellSize+1, j*cellSize+1, cellSize -1, cellSize-1);
                            }
                        }
                        if(hasFocus()){
                            g.setColor(Color.RED);
                        }else {
                            g.setColor(Color.BLACK);
                        }
                        g.setFont(font);
                        g.drawString("A", pos.x * cellSize, (pos.y + 1) * cellSize);
                    }
                };
                getViewport().add(worldPanel);
                setVisible(true);
            }
        };
    }
}
