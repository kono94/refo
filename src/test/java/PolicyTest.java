import core.RNG;
import core.policy.RandomPolicy;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PolicyTest {

    private Map<DummyAction, Double> createActionValueMap(){
        Map<DummyAction, Double> actionValues = new HashMap<>();
        actionValues.put(DummyAction.UP, 1d);
        actionValues.put(DummyAction.DOWN, 2d);
        actionValues.put(DummyAction.LEFT, 3d);
        actionValues.put(DummyAction.RIGHT, 4d);

        return actionValues;
    }

    /**
     * Test if the random policy generates true random results
     * based upon the static RNG class.
     */
    @Test
    public void RandomPolicyTest(){
        RandomPolicy<DummyAction> randomPolicy =  new RandomPolicy<>();
        Map<DummyAction, Double> dummyActionValues = createActionValueMap();

        // dummy action space to choose an action from
        List<DummyAction> actionList = new ArrayList<>(dummyActionValues.keySet());
        // list of 100 random selected actions in a row
        List<DummyAction> chosenActions = new ArrayList<>();

        // randomly selecting an action based upon an index generated from RNG class
        RNG.setSeed(123);
        for(int i = 0; i < 100; ++i){
            int randomPos = RNG.getRandom().nextInt(actionList.size());
            chosenActions.add(actionList.get(randomPos));
        }

        // check if all randomly chosen action in the implementation rely
        // on RNG static class
        RNG.setSeed(123);
        for(DummyAction action : chosenActions){
            assert (action == randomPolicy.chooseAction(dummyActionValues));
        }
    }
}
