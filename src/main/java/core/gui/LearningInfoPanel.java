package core.gui;

import core.algo.Learning;
import core.controller.ViewListener;
import core.policy.EpsilonPolicy;

import javax.swing.*;

public class LearningInfoPanel extends JPanel {
    private Learning learning;
    private JLabel policyLabel;
    private JLabel discountLabel;
    private JLabel epsilonLabel;
    private JSlider epsilonSlider;
    private JLabel delayLabel;
    private JSlider delaySlider;

    public LearningInfoPanel(Learning learning, ViewListener viewListener){
        this.learning = learning;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        policyLabel = new JLabel();
        discountLabel = new JLabel();
        epsilonLabel = new JLabel();
        delayLabel = new JLabel();
        delaySlider = new JSlider(0,1000, learning.getDelay());
        delaySlider.addChangeListener(e -> viewListener.onDelayChange(delaySlider.getValue()));
        add(policyLabel);
        add(discountLabel);
        if(learning.getPolicy() instanceof EpsilonPolicy){
            epsilonSlider = new JSlider(0, 100, (int)((EpsilonPolicy)learning.getPolicy()).getEpsilon() * 100);
            epsilonSlider.addChangeListener(e -> viewListener.onEpsilonChange(epsilonSlider.getValue() / 100f));
            add(epsilonLabel);
            add(epsilonSlider);
        }
        add(delayLabel);
        add(delaySlider);
        refreshLabels();
        setVisible(true);
    }

    public void refreshLabels(){
        policyLabel.setText("Policy: " + learning.getPolicy().getClass());
        discountLabel.setText("Discount factor: " + learning.getDiscountFactor());
        if(learning.getPolicy() instanceof EpsilonPolicy){
            epsilonLabel.setText("Exploration (Epsilon): " + ((EpsilonPolicy)learning.getPolicy()).getEpsilon());
        }
        delayLabel.setText("Delay (ms): " + learning.getDelay());
    }
}
