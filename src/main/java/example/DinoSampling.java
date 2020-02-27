package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorld;

public class DinoSampling {
    public static void main(String[] args) {
        for (int i = 0; i < 10 ; i++) {
            RNG.setSeed(55);

            RLController<DinoAction> rl = new RLController<>(
                    new DinoWorld(false, false),
                    Method.MC_CONTROL_FIRST_VISIT,
                    DinoAction.values());

            rl.setDelay(0);
            rl.setDiscountFactor(1f);
            rl.setEpsilon(0.15f);
            rl.setLearningRate(1f);
            rl.setNrOfEpisodes(400);
            rl.start();
        }
    }
}
