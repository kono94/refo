package evironment.jumpingDino;

import core.State;
import core.gui.Visualizable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@Getter
public class DinoStateSimple implements State, Serializable, Visualizable {
    protected final double scale = 0.5;
    protected int xDistanceToObstacle;

    @Override
    public String toString() {
        return "DinoState{" +
                "xDistanceToObstacle=" + xDistanceToObstacle +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        DinoStateSimple dinoState = (DinoStateSimple) o;
        return xDistanceToObstacle == dinoState.xDistanceToObstacle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xDistanceToObstacle);
    }

    @Override
    public JComponent visualize() {
        return new JComponent() {
            {
                setPreferredSize(new Dimension((int) (scale * Config.FRAME_WIDTH), (int) (scale * Config.FRAME_HEIGHT)));
                setVisible(true);
            }

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponents(g);
                drawObjects(g);
            }
        };
    }

    protected void drawDinoInfo(Graphics g) {
        g.fillRect((int) (scale * Config.DINO_STARTING_X), (int) (scale * (Config.FRAME_HEIGHT - Config.GROUND_Y - Config.DINO_SIZE)), (int) (scale * Config.DINO_SIZE), (int) (scale * Config.DINO_SIZE));
        g.drawString("Distance: " + xDistanceToObstacle, (int) (scale * Config.DINO_STARTING_X), (int) (scale * (Config.FRAME_HEIGHT - Config.GROUND_Y - Config.OBSTACLE_SIZE - 40)));
    }

    public void drawObjects(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(0, (int) (scale * (Config.FRAME_HEIGHT - Config.GROUND_Y)), Config.FRAME_WIDTH, 2);
        g.fillRect((int) (scale * (Config.DINO_STARTING_X + getXDistanceToObstacle())), (int) (scale * (Config.FRAME_HEIGHT - Config.GROUND_Y - Config.OBSTACLE_SIZE)), (int) (scale * Config.OBSTACLE_SIZE), (int) (scale * Config.OBSTACLE_SIZE));
        drawDinoInfo(g);
    }
}
