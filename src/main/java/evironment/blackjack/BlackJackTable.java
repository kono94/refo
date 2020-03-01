package evironment.blackjack;

import core.Environment;
import core.State;
import core.StepResultEnvironment;
import core.gui.Visualizable;
import evironment.blackjack.cards.CardDeck;
import evironment.blackjack.cards.Rank;
import evironment.blackjack.gui.BlackJackTableComponent;
import lombok.Getter;

import javax.swing.*;

public class BlackJackTable implements Environment<PlayerAction>, Visualizable {
    private CardDeck cardDeck;
    @Getter
    private Player player;
    private Player dealer;
    private int dealerSumShowing;
    @Getter
    private int playerSum;
    @Getter
    private int dealerSum;
    private BlackJackTableComponent comp;

    public BlackJackTable() {
        cardDeck = new CardDeck();
        player = new Player(0, false);
        dealer = new Player(0, false);
        comp = new BlackJackTableComponent(this);
    }

    @Override
    public StepResultEnvironment step(PlayerAction action) {
        boolean done = false;
        int reward = 0;

        if(action == PlayerAction.HIT) {
            obtainCard(player);
            playerSum = calculateSum(player);
            // bust
            if(playerSum > 21) {
                done = true;
                reward = -1;
            }
        } else if(action == PlayerAction.STICK) {
            done = true;
            // play out the game
            obtainCard(dealer);
            // do not change the initial dealerSum that is important for the state
            dealerSum = calculateSum(dealer);
            // fixed strategy of hitting until sum of 17 or greater
            while(dealerSum < Config.DEALER_HOLD_BOUND) {
                obtainCard(dealer);
                dealerSum = calculateSum(dealer);
                comp.repaint();
            }
            // dealer went bust, player wins
            if(dealerSum > 21) {
                reward = +1;
            } else if(dealerSum == 21 && playerSum == 21) {
                // draw; player and dealer got 21
                reward = 0;
            } else {
                int playerDiff = 21 - playerSum;
                int dealerDiff = 21 - dealerSum;

                // reward based on who is closer to 21
                reward = Integer.compare(dealerDiff, playerDiff);
            }
        }
        return new StepResultEnvironment(new TableState(playerSum, dealerSumShowing, player.isUsableAce()), reward, done, "");
    }

    @Override
    public State reset() {
        player.setHandValue(0);
        player.setUsableAce(false);
        dealer.setHandValue(0);
        dealer.setUsableAce(false);

        // player gets two cards
        obtainCard(player);
        obtainCard(player);
        playerSum = calculateSum(player);

        // dealer is only showing one card
        obtainCard(dealer);
        dealerSumShowing = dealerSum = calculateSum(dealer);

        return new TableState(playerSum, dealerSumShowing, player.isUsableAce());
    }

    private int calculateSum(Player p) {
        if(p.isUsableAce()) {
            return p.getHandValue() + 10;
        } else {
            return p.getHandValue();
        }
    }

    private void obtainCard(Player p) {
        Rank rank = cardDeck.nextCard().getRank();
        if(rank == Rank.JACK || rank == Rank.QUEEN || rank == Rank.KING) {
            p.addValue(10);
        } else if(rank == Rank.ACE) {
            if(p.getHandValue() + 11 <= 21) {
                p.setUsableAce(true);
            }
            p.addValue(1);
        } else {
            p.addValue(rank.ordinal() + 2);
        }

        if(p.isUsableAce() && p.getHandValue() + 10 > 21) {
            p.setUsableAce(false);
        }
    }

    @Override
    public JComponent visualize() {
        return comp;
    }
}
