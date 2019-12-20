package core.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GreedyPolicy<A extends Enum> implements Policy<A> {

    @Override
    public A chooseAction(Map<A, Double> actionValues) {
        if(actionValues.size() == 0) throw new RuntimeException("Empty actionActionValues set");

        Double highestValueAction = null;

        List<A> equalHigh = new ArrayList<>();

        for(Map.Entry<A, Double> actionValue : actionValues.entrySet()){
           // System.out.println(actionValue.getKey() + " " + actionValue.getValue());
            if(highestValueAction == null || highestValueAction < actionValue.getValue()){
                highestValueAction = actionValue.getValue();
                equalHigh.clear();
                equalHigh.add(actionValue.getKey());
            }else if(highestValueAction.equals(actionValue.getValue())){
                equalHigh.add(actionValue.getKey());
            }
        }

        return equalHigh.get(new Random().nextInt(equalHigh.size()));
    }
}
