package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.antGame.AntAction;
import evironment.antGame.AntWorldContinuous;

public class ContinuousAnt {
    public static void main(String[] args) {
        RNG.setSeed(13, true);
        RLController<AntAction> rl = new RLControllerGUI<>(
                new AntWorldContinuous(8, 8),
                Method.Q_LEARNING_OFF_POLICY_CONTROL,
                AntAction.values());
        rl.setDelay(200);
        rl.setNrOfEpisodes(1);
        rl.setDiscountFactor(0.3f);
        rl.setLearningRate(0.9f);
        rl.setEpsilon(0.15f);
        rl.start();
    }
}
