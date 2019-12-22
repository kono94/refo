package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorld;

public class JumpingDino {
    public static void main(String[] args) {
        RNG.setSeed(55);

        RLController<DinoAction> rl = new RLController<DinoAction>()
                .setEnvironment(new DinoWorld())
                .setAllowedActions(DinoAction.values())
                .setMethod(Method.MC_ONPOLICY_EGREEDY)
                .setDiscountFactor(1f)
                .setEpsilon(0.15f)
                .setDelay(200)
                .setEpisodes(100000);
        rl.start();
    }
}
