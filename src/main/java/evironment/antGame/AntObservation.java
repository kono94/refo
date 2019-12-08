package evironment.antGame;

import core.Observation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;


@AllArgsConstructor
@Getter
@Setter
public class AntObservation implements Observation {
      private Cell cell;
      private Point pos;


      @Getter(AccessLevel.NONE)
      private boolean hasFood;

      public boolean hasFood(){
         return hasFood;
      }
}
