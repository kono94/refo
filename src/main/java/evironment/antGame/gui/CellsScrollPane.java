package evironment.antGame.gui;

import evironment.antGame.Cell;

import javax.swing.*;
import java.awt.*;

public class CellsScrollPane extends JScrollPane {
    private int cellSize;
    private final int paneWidth = 500;
    private final int paneHeight = 500;

    public CellsScrollPane(Cell[][] cells, int size){
        super();
        cellSize = size;
        setPreferredSize(new Dimension(paneWidth, paneHeight));
        cellSize = (paneWidth- cells.length) /cells.length;
        JPanel worldPanel = new JPanel(){
            {
                setPreferredSize(new Dimension(cells.length * cellSize, cells[0].length * cellSize));
                setVisible(true);

                addMouseWheelListener(e -> {
                    if(e.getWheelRotation() > 0){
                        cellSize -= 1;
                    }else {
                        cellSize += 1;
                    }
                    setPreferredSize(new Dimension(cells.length * cellSize, cells[0].length * cellSize));
                    revalidate();
                    repaint();
                });
            }

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < cells.length; i++) {
                    for (int j = 0; j < cells[0].length; j++) {
                        g.setColor(Color.BLACK);
                        g.drawRect(i*cellSize, j*cellSize, cellSize, cellSize);
                        g.setColor(CellColor.map.get(cells[i][j].getType()));
                        g.fillRect(i*cellSize+1, j*cellSize+1, cellSize -1, cellSize-1);
                    }
                }
            }
        };
        getViewport().add(worldPanel);
        setVisible(true);
    }
}
