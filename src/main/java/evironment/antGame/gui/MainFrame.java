package evironment.antGame.gui;

import core.StepResultEnvironment;
import evironment.antGame.AntAction;
import evironment.antGame.AntAgent;
import evironment.antGame.AntWorld;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private AntWorld antWorld;
    private HistoryPanel historyPanel;

    public MainFrame(AntWorld antWorld, AntAgent antAgent){
        this.antWorld = antWorld;
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        CellsScrollPane worldPane = new CellsScrollPane(antWorld.getCellArray(), antWorld.getAnt(), 10);
        CellsScrollPane antBrainPane = new CellsScrollPane(antAgent.getKnownWorld(), antWorld.getAnt(), 10);
        historyPanel = new HistoryPanel();

        JComponent mapComponent = new JPanel();
        FlowLayout flowLayout = new FlowLayout();
        flowLayout.setHgap(20);
        mapComponent.setLayout(flowLayout);
        mapComponent.add(worldPane);
        mapComponent.add(antBrainPane);

        add(BorderLayout.CENTER, mapComponent);
        add(BorderLayout.SOUTH, historyPanel);
        pack();
        setVisible(true);
    }

    public void update(AntAction lastAction, StepResultEnvironment stepResultEnvironment){
        historyPanel.addText(String.format("Tick %d: \t Selected action: %s \t Reward: %f \t Info: %s \n totalPoints: %d \t hasFood: %b \t ",
                antWorld.getTick(), lastAction.toString(), stepResultEnvironment.getReward(), stepResultEnvironment.getInfo(), antWorld.getAnt().getPoints(), antWorld.getAnt().hasFood()));

        repaint();
    }
}
