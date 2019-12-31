package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import evironment.antGame.AntAction;
import evironment.antGame.AntWorld;

public class RunningAnt {
    public static void main(String[] args) {
        RNG.setSeed(123);

        RLController<AntAction> rl = new RLController<>(
                new AntWorld(3, 3, 0.1),
                Method.MC_ONPOLICY_EGREEDY,
                AntAction.values());

        rl.setDelay(200);
        rl.setNrOfEpisodes(10000);
        rl.setDiscountFactor(1f);
        rl.setEpsilon(0.15f);

        rl.start();
    }
}
