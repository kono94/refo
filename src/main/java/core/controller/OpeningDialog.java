package core.controller;

import core.algo.Method;
import evironment.antGame.AntAction;
import evironment.antGame.AntWorldContinuous;
import evironment.antGame.Constants;
import evironment.blackjack.BlackJackTable;
import evironment.blackjack.PlayerAction;
import evironment.jumpingDino.DinoAction;
import evironment.jumpingDino.DinoWorld;
import evironment.jumpingDino.DinoWorldAdvanced;

import javax.swing.*;
import java.text.NumberFormat;

public class OpeningDialog {

    // JSlider is integer value only. Instead of creating a subclass
    // the JSlider value is simply divided by this scale factor.
    // 100 does mean 0 to 1 in 0.01 steps.
    private int scaleFactor = 100;

    public OpeningDialog() {
        createStartingDialog();
    }

    private void setLabelText(JSlider slider, JLabel label, boolean scaledValue, String parameterName) {
        if(scaledValue) {
            label.setText(parameterName + ": " + (float) slider.getValue() / scaleFactor);
        } else {
            label.setText(parameterName + ": " + slider.getValue());
        }

    }

    private void linkSliderWithLabel(JSlider slider, JLabel label, boolean scaledValue, String parameterName) {
        setLabelText(slider, label, scaledValue, parameterName);

        slider.addChangeListener(changeEvent -> setLabelText(slider, label, scaledValue, parameterName));
    }

    private void createStartingDialog() {
        JComboBox<Scenario> scenarioSelection = new JComboBox<>(Scenario.values());
        JComboBox<Method> algorithmSelection = new JComboBox<>(Method.values());

        JSlider delaySlider = new JSlider(1, 1000, 200);
        JLabel delayLabel = new JLabel();
        linkSliderWithLabel(delaySlider, delayLabel, false, "Delay");

        JSlider discountSlider = new JSlider(0, 100, 100);
        JLabel discountLabel = new JLabel();
        linkSliderWithLabel(discountSlider, discountLabel, true, "Discount Factor (gamma)");

        JSlider epsilonSlider = new JSlider(0, 100, 15);
        JLabel epsilonLabel = new JLabel();
        linkSliderWithLabel(epsilonSlider, epsilonLabel, true, "Exploration Factor (epsilon)");

        JSlider learningRateSlider = new JSlider(0, 100, 90);
        JLabel learningRateLabel = new JLabel();
        linkSliderWithLabel(learningRateSlider, learningRateLabel, true, "Learning rate (alpha)");

        JTextField episodesToLearn = new JFormattedTextField(NumberFormat.getIntegerInstance());
        episodesToLearn.setText("10000");

        JTextField seedTextField = new JFormattedTextField(NumberFormat.getIntegerInstance());
        seedTextField.setText("29");


        Object[] parameters = {
                "Environment:", scenarioSelection,
                "Algorithm:", algorithmSelection,
                discountLabel, discountSlider,
                epsilonLabel, epsilonSlider,
                learningRateLabel, learningRateSlider,
                delayLabel, delaySlider,
                "Episodes to learn:", episodesToLearn,
                "RNG Seed: ", seedTextField,
        };

        int option = JOptionPane.showConfirmDialog(null, parameters, "Learning parameters", JOptionPane.OK_CANCEL_OPTION);
        if(option == JOptionPane.OK_OPTION) {
            Scenario selectedScenario = (Scenario) scenarioSelection.getSelectedItem();

            RLControllerGUI rl;
            if(selectedScenario == Scenario.JUMPING_DINO_SIMPLE) {
                rl = new RLControllerGUI<DinoAction>(new DinoWorld(), (Method) algorithmSelection.getSelectedItem(), DinoAction.values());
            } else if(selectedScenario == Scenario.JUMPING_DINO_ADVANCED) {
                rl = new RLControllerGUI<DinoAction>(new DinoWorldAdvanced(), (Method) algorithmSelection.getSelectedItem(), DinoAction.values());
            } else if(selectedScenario == Scenario.ANTGAME) {
                rl = new RLControllerGUI<AntAction>(new AntWorldContinuous(Constants.DEFAULT_GRID_WIDTH, Constants.DEFAULT_GRID_HEIGHT), (Method) algorithmSelection.getSelectedItem(), AntAction.values());
            } else if(selectedScenario == Scenario.BLACKJACK) {
                rl = new RLControllerGUI<PlayerAction>(new BlackJackTable(), (Method) algorithmSelection.getSelectedItem(), PlayerAction.values());
            } else {
                throw new IllegalArgumentException("Invalid learning scenario selected");
            }
            rl.setDelay(delaySlider.getValue());
            rl.setDiscountFactor((float) discountSlider.getValue() / scaleFactor);
            rl.setEpsilon((float) epsilonSlider.getValue() / scaleFactor);
            rl.setLearningRate((float) learningRateSlider.getValue() / scaleFactor);
            rl.setNrOfEpisodes(Integer.parseInt(episodesToLearn.getText()));
            rl.start();
        } else {
            System.out.println("Parameter dialog canceled");
        }
    }

    private enum Scenario {
        JUMPING_DINO_SIMPLE,
        JUMPING_DINO_ADVANCED,
        ANTGAME,
        BLACKJACK
    }

}
