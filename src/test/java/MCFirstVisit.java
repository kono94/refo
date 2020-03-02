import core.RNG;
import core.algo.Method;
import core.controller.RLController;
import core.controller.RLControllerGUI;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorld;
import org.junit.Test;

public class MCFirstVisit {

    /**
     * Test if the action sequence is deterministic
     */
    @Test
    public void deterministicActionSequence(){
        RNG.setSeed(55);

        RLController<DinoAction> rl = new RLControllerGUI<>(
                new DinoWorld(false, false),
                Method.MC_CONTROL_FIRST_VISIT,
                DinoAction.values());

        rl.setDelay(10);
        rl.setDiscountFactor(1f);
        rl.setEpsilon(0.1f);
        rl.setLearningRate(0.8f);
        rl.setNrOfEpisodes(4000000);
        rl.start();
    }
}
