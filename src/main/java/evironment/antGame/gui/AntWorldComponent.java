package evironment.antGame.gui;

import evironment.antGame.AntAgent;
import evironment.antGame.AntWorld;

import javax.swing.*;
import java.awt.*;

public class AntWorldComponent extends JComponent {

    public AntWorldComponent(AntWorld antWorld, AntAgent antAgent){
        setLayout(new BorderLayout());
        CellsScrollPane worldPane = new CellsScrollPane(antWorld.getCellArray(), antWorld.getAnt(), 10);
        CellsScrollPane antBrainPane = new CellsScrollPane(antAgent.getKnownWorld(), antWorld.getAnt(), 10);

        JComponent mapComponent = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(20);
        mapComponent.setLayout(flowLayout);
        mapComponent.add(worldPane);
        mapComponent.add(antBrainPane);
        add(BorderLayout.CENTER, mapComponent);
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }
}
