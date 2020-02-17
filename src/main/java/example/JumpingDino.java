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
                new DinoWorld(false, false),
                Method.Q_LEARNING_OFF_POLICY_CONTROL,
                DinoAction.values());

        rl.setDelay(10);
        rl.setDiscountFactor(0.8f);
        rl.setEpsilon(0.1f);
        rl.setLearningRate(0.5f);
        rl.setNrOfEpisodes(10000);
        rl.start();


    }
}
