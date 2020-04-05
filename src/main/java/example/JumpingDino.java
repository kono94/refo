package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorldAdvanced;

public class JumpingDino {
    public static void main(String[] args) {
        RNG.setSeed(29);

        RLController<DinoAction> rl = new RLControllerGUI<>(
                new DinoWorldAdvanced(),
                Method.MC_CONTROL_FIRST_VISIT,
                DinoAction.values());

        rl.setDelay(200);
        rl.setDiscountFactor(9f);
        rl.setEpsilon(0.05f);
        rl.setLearningRate(0.8f);
        rl.setNrOfEpisodes(100000);
        rl.start();
    }
}
