package evironment.antGame;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@Getter
@Setter
public class Ant {
    // only set this via .setLocation of Point-class!
    @Setter(AccessLevel.NONE)
    private Point pos;
    private int points;
    @Getter(AccessLevel.NONE)
    private boolean hasFood;

    public Ant(){
        pos = new Point();
        points = 0;
        hasFood = false;
    }
    public boolean hasFood(){
        return hasFood;
    }
}
