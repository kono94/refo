package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.antGame.AntAction;
import evironment.antGame.AntWorldContinuous;
import evironment.antGame.AntWorldContinuousOriginalState;

import java.io.File;
import java.io.IOException;

public class ContinuousAnt {
    public static final String FILE_NAME = "converge05.txt";
    public static void main(String[] args) {
        File file = new File(FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        RNG.setSeed(56);
        RLController<AntAction> rl = new RLController<>(
                new AntWorldContinuous(8, 8),
                Method.Q_LEARNING_OFF_POLICY_CONTROL,
                AntAction.values());
        rl.setDelay(0);
        rl.setNrOfEpisodes(1);
        rl.setDiscountFactor(0.7f);
        rl.setLearningRate(0.2f);
        rl.setEpsilon(0.5f);
        rl.start();
    }
}
