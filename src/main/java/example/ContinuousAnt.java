package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.antGame.AntAction;
import evironment.antGame.AntWorldContinuous;

import java.io.File;
import java.io.IOException;

public class ContinuousAnt {
    public static final String FILE_NAME = "optDiscTimestampsNew.txt";
    public static void main(String[] args) {
        int k = 4 + 4 + 4 + 6 + 6 + 6 + 8 + 10 + 12 + 14 + 14 + 16 + 16 + 16 + 18 + 18 + 18 + 20 + 20 + 20 + 22 + 22 + 22 + 24 + 24 + 24 + 24 + 26 + 26 + 26 + 26 + 26 + 28 + 28 + 28 + 28 + 28 + 30 + 30 + 30 + 30 + 32 + 32 + 32 + 34 + 34 + 34 + 36 + 36 + 38 + 40 + 42;
        System.out.println(k / 52f);
        File file = new File(FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
            RNG.setSeed(13);
        RLController<AntAction> rl = new RLControllerGUI<>(
                    new AntWorldContinuous(8, 8),
                    Method.Q_LEARNING_OFF_POLICY_CONTROL,
                    AntAction.values());
        rl.setDelay(20);
            rl.setNrOfEpisodes(1);
            //0.99 0.9 0.5
            //0.99 0.95 0.9 0.7 0.5 0.3 0.1
        rl.setDiscountFactor(0.05f);
            // 0.1, 0.3, 0.5, 0.7 0.9
            rl.setLearningRate(0.9f);
            rl.setEpsilon(0.2f);
            rl.start();


    }
}
