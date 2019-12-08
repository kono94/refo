package evironment.antGame;

import core.Observation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;

@AllArgsConstructor
@Getter
@Setter
public class AntObservation implements Observation {
   private Cell cell;
}
