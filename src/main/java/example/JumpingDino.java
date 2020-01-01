package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorld;

public class JumpingDino {
    public static void main(String[] args) {
        RNG.setSeed(55);

        RLController<DinoAction> rl = new RLControllerGUI<>(
                new DinoWorld(true, true),
                Method.MC_ONPOLICY_EGREEDY,
                DinoAction.values());

        rl.setDelay(0);
        rl.setDiscountFactor(1f);
        rl.setEpsilon(0.15f);
        rl.setNrOfEpisodes(100000);

        rl.start();
    }
}