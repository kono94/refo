package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorld;
import evironment.jumpingDino.DinoWorldAdvanced;

public class JumpingDino {
    public static void main(String[] args) {
        RNG.setSeed(55);

        RLController<DinoAction> rl = new RLControllerGUI<>(
                new DinoWorldAdvanced(),
                Method.MC_CONTROL_FIRST_VISIT,
                DinoAction.values());

        rl.setDelay(100);
        rl.setDiscountFactor(1f);
        rl.setEpsilon(0.15f);
        rl.setLearningRate(1f);
        rl.setNrOfEpisodes(50000);
        rl.start();
    }
}
