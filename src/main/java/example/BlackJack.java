package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.blackjack.BlackJackTable;
import evironment.blackjack.PlayerAction;

public class BlackJack {
    public static void main(String[] args) {
        RNG.setSeed(55);

        RLController<PlayerAction> rl = new RLControllerGUI<>(
                new BlackJackTable(),
                Method.MC_CONTROL_FIRST_VISIT,
                PlayerAction.values());

        rl.setDelay(1000);
        rl.setDiscountFactor(1f);
        rl.setEpsilon(0.1f);
        rl.setLearningRate(0.5f);
        rl.setNrOfEpisodes(1000);
        rl.start();
    }
}
