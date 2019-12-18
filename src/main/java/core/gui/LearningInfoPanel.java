package core.gui;

import core.algo.Learning;
import core.controller.ViewListener;

import javax.swing.*;

public class LearningInfoPanel extends JPanel {
    private Learning learning;
    private JLabel policyLabel;
    private JLabel discountLabel;
    private JLabel epsilonLabel;
    private JSlider epsilonSlider;
    private JSlider delaySlider;

    public LearningInfoPanel(Learning learning, ViewListener viewListener){
        this.learning = learning;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        policyLabel = new JLabel();
        discountLabel = new JLabel();
        epsilonLabel = new JLabel();
        epsilonSlider = new JSlider(0, 100, (int)(learning.getEpsilon() * 100));
        epsilonSlider.addChangeListener(e -> viewListener.onEpsilonChange(epsilonSlider.getValue() / 100f));
        add(policyLabel);
        add(discountLabel);
        add(epsilonLabel);
        add(epsilonSlider);
        refreshLabels();
        setVisible(true);
    }

    public void refreshLabels(){
        policyLabel.setText("Policy: " + learning.getPolicy().getClass());
        discountLabel.setText("Discount factor: " + learning.getDiscountFactor());
        epsilonLabel.setText("Exploration (Epsilon): " + learning.getEpsilon());
    }

    protected JSlider getEpsilonSlider(){
        return epsilonSlider;
    }
}
