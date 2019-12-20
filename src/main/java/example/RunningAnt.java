package example;

import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import evironment.antGame.AntAction;
import evironment.antGame.AntWorld;

public class RunningAnt {
    public static void main(String[] args) {
        RNG.setSeed(1234);

        RLController<AntAction> rl = new RLController<AntAction>()
                            .setEnvironment(new AntWorld(3,3,0.1))
                            .setAllowedActions(AntAction.values())
                            .setMethod(Method.MC_ONPOLICY_EGREEDY)
                            .setDelay(10)
                            .setEpisodes(10000);
        rl.start();
    }
}
