package evironment.blackjack;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Player {
    private int handValue;
    private boolean usableAce;

    public void addValue(int value) {
        handValue += value;
    }
}
