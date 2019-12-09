package evironment.antGame;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@AllArgsConstructor
@Getter
@Setter
public class Ant {
    private Point pos;
    private boolean spawned;
    @Getter(AccessLevel.NONE)
    private boolean hasFood;

    public boolean hasFood(){
        return hasFood;
    }
}
