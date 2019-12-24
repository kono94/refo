package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorld;

public class JumpingDino {
    public static void main(String[] args) {
        RNG.setSeed(55);

        RLController<DinoAction> rl = new RLController<>(
                new DinoWorld(),
                Method.MC_ONPOLICY_EGREEDY,
                DinoAction.values());

        rl.setDelay(200);
        rl.setDiscountFactor(1f);
        rl.setEpsilon(0.15f);
        rl.setEpisodes(5000);

        rl.start();
    }
}
