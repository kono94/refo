package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.antGame.AntAction;
import evironment.antGame.AntWorld;

public class RunningAnt {
    public static void main(String[] args) {
        RNG.setSeed(56);

        RLController<AntAction> rl = new RLControllerGUI<>(
                new AntWorld(8, 8, 1),
                Method.Q_LEARNING_OFF_POLICY_CONTROL,
                AntAction.values());

        rl.setDelay(200);
        rl.setNrOfEpisodes(10000);
        rl.setDiscountFactor(0.9f);
        rl.setLearningRate(0.9f);
        rl.setEpsilon(0.15f);
        rl.start();
    }
}
