package core.gui;

import core.Util;
import core.algo.Episodic;
import core.algo.EpisodicLearning;
import core.algo.Learning;
import core.listener.ViewListener;
import core.policy.EpsilonPolicy;

import javax.swing.*;
import java.awt.*;

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
    private JCheckBox smoothGraphCheckbox;
    private JCheckBox last100Checkbox;
    private JCheckBox drawEnvironmentCheckbox;
    private JTextField learnMoreEpisodesInput;
    private JButton learnMoreEpisodesButton;
    private JButton showQTableButton;

    public LearningInfoPanel(Learning learning, ViewListener viewListener) {
        this.learning = learning;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        policyLabel = new JLabel();
        discountLabel = new JLabel();
        delayLabel = new JLabel();
        if(learning instanceof Episodic) {
            episodeLabel = new JLabel();
            add(episodeLabel);
        }
        delaySlider = new JSlider(0, 1000, learning.getDelay());
        delaySlider.addChangeListener(e -> viewListener.onDelayChange(delaySlider.getValue()));
        add(policyLabel);
        add(discountLabel);
        if(learning.getPolicy() instanceof EpsilonPolicy) {
            epsilonLabel = new JLabel();
            epsilonSlider = new JSlider(0, 100, (int) ((EpsilonPolicy) learning.getPolicy()).getEpsilon() * 100);
            epsilonSlider.addChangeListener(e -> viewListener.onEpsilonChange(epsilonSlider.getValue() / 100f));
            add(epsilonLabel);
            add(epsilonSlider);
        }

        toggleFastLearningButton = new JButton("Enable fast-learn");
        fastLearning = false;
        toggleFastLearningButton.addActionListener(e -> {
            fastLearning = !fastLearning;
            delaySlider.setEnabled(!fastLearning);
            epsilonSlider.setEnabled(!fastLearning);
            drawEnvironmentCheckbox.setSelected(!fastLearning);
            viewListener.onFastLearnChange(fastLearning);
        });
        smoothGraphCheckbox = new JCheckBox("Smoothen Graph");
        smoothGraphCheckbox.setSelected(false);
        last100Checkbox = new JCheckBox("Only show last 100 Rewards");
        last100Checkbox.setSelected(true);
        drawEnvironmentCheckbox = new JCheckBox("Update Environment");
        drawEnvironmentCheckbox.setSelected(true);

        add(delayLabel);
        add(delaySlider);
        add(toggleFastLearningButton);

        if(learning instanceof EpisodicLearning) {
            learnMoreEpisodesInput = new JTextField();
            learnMoreEpisodesInput.setMaximumSize(new Dimension(200, 20));
            learnMoreEpisodesButton = new JButton("Learn More Episodes");
            learnMoreEpisodesButton.addActionListener(e -> {
                if(Util.isNumeric(learnMoreEpisodesInput.getText())) {
                    viewListener.onLearnMoreEpisodes(Integer.parseInt(learnMoreEpisodesInput.getText()));
                } else {
                    learnMoreEpisodesInput.setText("");
                }
            });
            add(learnMoreEpisodesInput);
            add(learnMoreEpisodesButton);
        }
        showQTableButton = new JButton("Show Q-Table");
        showQTableButton.addActionListener(e -> {
            viewListener.onShowQTable();
        });
        add(drawEnvironmentCheckbox);
        add(smoothGraphCheckbox);
        add(last100Checkbox);
        add(showQTableButton);
        refreshLabels();
        setVisible(true);
    }

    public void refreshLabels() {
        policyLabel.setText("Policy: " + learning.getPolicy().getClass());
        discountLabel.setText("Discount factor: " + learning.getDiscountFactor());
        if(learning instanceof Episodic) {
            episodeLabel.setText("Episode: " + ((Episodic) (learning)).getCurrentEpisode() +
                    "\t Episodes to go: " + ((Episodic) (learning)).getEpisodesToGo() +
                    "\t Eps/Sec: " + ((Episodic) (learning)).getEpisodesPerSecond());
        }
        if(learning.getPolicy() instanceof EpsilonPolicy) {
            epsilonLabel.setText("Exploration (Epsilon): " + ((EpsilonPolicy) learning.getPolicy()).getEpsilon());
            epsilonSlider.setValue((int) (((EpsilonPolicy) learning.getPolicy()).getEpsilon() * 100));
        }
        delayLabel.setText("Delay (ms): " + learning.getDelay());
        if(delaySlider.isEnabled()) {
            delaySlider.setValue(learning.getDelay());
        }
        toggleFastLearningButton.setText(fastLearning ? "Disable fast-learning" : "Enable fast-learning");
    }

    protected boolean isSmoothenGraphSelected() {
        return smoothGraphCheckbox.isSelected();
    }

    protected boolean isLast100Selected() {
        return last100Checkbox.isSelected();
    }

    protected boolean isDrawEnvironmentSelected() {
        return drawEnvironmentCheckbox.isSelected();
    }
}
