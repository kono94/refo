package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorld;
import evironment.jumpingDino.DinoWorldAdvanced;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DinoSampling {
    public static final float f =0.05f;
    public static final String FILE_NAME = "convergenceAdvancedMCnegRew.txt";
    public static void main(String[] args) {
        File file = new File(FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(float f = 0.05f; f <=1.003 ; f+=0.05f) {
            try {
                Files.writeString(Path.of(file.getPath()), f + ",", StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (int i = 1; i <= 100; i++) {
                System.out.println("seed: " + i * 13);
                RNG.setSeed(i * 13);

                RLController<DinoAction> rl = new RLController<>(
                        new DinoWorldAdvanced(),
                        Method.Q_LEARNING_OFF_POLICY_CONTROL,
                        DinoAction.values());
                rl.setDelay(0);
                rl.setDiscountFactor(0.99f);
                rl.setEpsilon(f);
                rl.setLearningRate(0.9f);
                rl.setNrOfEpisodes(400000);
                rl.start();
            }
            try {
                Files.writeString(Path.of(file.getPath()), "\n", StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
