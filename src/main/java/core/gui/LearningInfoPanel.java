package core.gui;

import core.algo.Episodic;
import core.algo.Learning;
import core.listener.ViewListener;
import core.policy.EpsilonPolicy;

import javax.swing.*;

public class LearningInfoPanel extends JPanel {
    private Learning learning;
    private JLabel policyLabel;
    private JLabel discountLabel;
    private JLabel epsilonLabel;
    private JLabel episodeLabel;
    private JSlider epsilonSlider;
    private JLabel delayLabel;
    private JSlider delaySlider;
    private JButton toggleFastLearningButton;
    private boolean fastLearning;

    public LearningInfoPanel(Learning learning, ViewListener viewListener){
        this.learning = learning;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        policyLabel = new JLabel();
        discountLabel = new JLabel();
        delayLabel = new JLabel();
        if(learning instanceof Episodic){
            episodeLabel = new JLabel();
            add(episodeLabel);
        }
        delaySlider = new JSlider(0,1000, learning.getDelay());
        delaySlider.addChangeListener(e -> viewListener.onDelayChange(delaySlider.getValue()));
        add(policyLabel);
        add(discountLabel);
        if(learning.getPolicy() instanceof EpsilonPolicy){
            epsilonLabel = new JLabel();
            epsilonSlider = new JSlider(0, 100, (int)((EpsilonPolicy)learning.getPolicy()).getEpsilon() * 100);
            epsilonSlider.addChangeListener(e -> viewListener.onEpsilonChange(epsilonSlider.getValue() / 100f));
            add(epsilonLabel);
            add(epsilonSlider);
        }

        toggleFastLearningButton = new JButton("Enable fast-learn");
        fastLearning = false;
        toggleFastLearningButton.addActionListener(e->{
            fastLearning = !fastLearning;
            delaySlider.setEnabled(!fastLearning);
            epsilonSlider.setEnabled(!fastLearning);
            viewListener.onFastLearnChange(fastLearning);
        });
        add(delayLabel);
        add(delaySlider);
        add(toggleFastLearningButton);
        refreshLabels();
        setVisible(true);
    }

    public void refreshLabels() {
        policyLabel.setText("Policy: " + learning.getPolicy().getClass());
        discountLabel.setText("Discount factor: " + learning.getDiscountFactor());
        if(learning instanceof Episodic){
            episodeLabel.setText("Episode: " + ((Episodic)(learning)).getCurrentEpisode());
        }
        if (learning.getPolicy() instanceof EpsilonPolicy) {
            epsilonLabel.setText("Exploration (Epsilon): " + ((EpsilonPolicy) learning.getPolicy()).getEpsilon());
            epsilonSlider.setValue((int)(((EpsilonPolicy) learning.getPolicy()).getEpsilon() * 100));
        }
        delayLabel.setText("Delay (ms): " + learning.getDelay());
        delaySlider.setValue(learning.getDelay());
        toggleFastLearningButton.setText(fastLearning ? "Disable fast-learning" : "Enable fast-learning");
    }
}
