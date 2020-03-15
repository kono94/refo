package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorld;

public class JumpingDino {
    public static void main(String[] args) {
        RNG.setSeed(29);

        RLController<DinoAction> rl = new RLController<>(
                new DinoWorld(),
                Method.MC_CONTROL_FIRST_VISIT,
                DinoAction.values());

        rl.setDelay(0);
        rl.setDiscountFactor(1f);
        rl.setEpsilon(0.15f);
        rl.setLearningRate(1f);
        rl.setNrOfEpisodes(30000000);
        rl.start();
    }
}
