package evironment.blackjack;

import core.State;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class TableState implements State, Serializable {
    // between 12 to 21
    private int playerSum;
    // dealer showing one card on player's turn;
    // A = 1
    private int dealerCardValue;
    // if player holds an ace without going bust
    private boolean usableAce;

    @Override
    public String toString() {
        return "PlayerState{" +
                "handValue=" + playerSum +
                ", dealerCardValue=" + dealerCardValue +
                ", usableAce=" + usableAce +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof TableState)) return false;
        TableState that = (TableState) o;
        return playerSum == that.playerSum &&
                usableAce == that.usableAce &&
                dealerCardValue == that.dealerCardValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerSum, dealerCardValue, usableAce);
    }
}
