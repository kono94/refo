package evironment.jumpingDino.gui;

import evironment.jumpingDino.Config;
import evironment.jumpingDino.DinoWorld;

import javax.swing.*;
import java.awt.*;

public class DinoWorldComponent extends JComponent {
    private DinoWorld dinoWorld;

    public DinoWorldComponent(DinoWorld dinoWorld){
        this.dinoWorld = dinoWorld;
        setPreferredSize(new Dimension(Config.FRAME_WIDTH, Config.FRAME_HEIGHT));
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.fillRect(0, Config.FRAME_HEIGHT - Config.GROUND_Y, Config.FRAME_WIDTH, 2);

        dinoWorld.getDino().render(g);
        dinoWorld.getCurrentObstacle().render(g);
    }
}
