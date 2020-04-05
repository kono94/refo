package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorld;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class DinoSampling {
    public static final String FILE_NAME = "converge.txt";

    public static void main(String[] args) {
        File file = new File(FILE_NAME);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(float f = 0.05f; f <= 1.003; f += 0.05f) {
            try {
                Files.writeString(Path.of(file.getPath()), f + ",", StandardOpenOption.APPEND);
            } catch (IOException e) {
                e.printStackTrace();
            }
            for(int i = 1; i <= 100; i++) {
                int seed = i * 13;
                System.out.println("seed: " + seed);
                RNG.setSeed(seed);

                RLController<DinoAction> rl = new RLControllerGUI<>(
                        new DinoWorld(),
                        Method.MC_CONTROL_FIRST_VISIT,
                        DinoAction.values());
                rl.setDelay(300);
                rl.setDiscountFactor(1f);
                rl.setEpsilon(0.5f);
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
