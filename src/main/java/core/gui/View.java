package core.gui;

import core.Environment;
import core.algo.Learning;
import core.listener.ViewListener;
import core.listener.LearningListener;
import lombok.Getter;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class View<A extends Enum> implements LearningListener {
    private Learning<A> learning;
    private Environment<A> environment;
    @Getter
    private XYChart rewardChart;
    @Getter
    private LearningInfoPanel learningInfoPanel;
    @Getter
    private JFrame mainFrame;
    private JFrame environmentFrame;
    private XChartPanel<XYChart> rewardChartPanel;
    private ViewListener viewListener;
    private boolean drawEveryStep;

    public View(Learning<A> learning, Environment<A> environment, ViewListener viewListener) {
        this.learning = learning;
        this.environment = environment;
        this.viewListener = viewListener;
        drawEveryStep = true;
        SwingUtilities.invokeLater(this::initMainFrame);
    }

    private void initMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setPreferredSize(new Dimension(1280, 720));
        mainFrame.setLayout(new BorderLayout());

        initLearningInfoPanel();
        initRewardChart();

        mainFrame.add(BorderLayout.WEST, learningInfoPanel);
        mainFrame.add(BorderLayout.CENTER, rewardChartPanel);

        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.pack();
        mainFrame.setVisible(true);

        if (environment instanceof Visualizable) {
            environmentFrame = new JFrame() {
                {
                    add(((Visualizable) environment).visualize());
                    pack();
                    setVisible(true);
                }
            };

        }
    }

    private void initLearningInfoPanel() {
        learningInfoPanel = new LearningInfoPanel(learning, viewListener);
    }

    private void initRewardChart() {
        rewardChart =
                QuickChart.getChart(
                        "Sum of Rewards per Episode",
                        "Episode",
                        "Reward",
                        "rewardHistory",
                        new double[]{0},
                        new double[]{0});
        rewardChart.getStyler().setLegendVisible(true);
        rewardChart.getStyler().setXAxisTicksVisible(true);
        rewardChartPanel = new XChartPanel<>(rewardChart);
        rewardChartPanel.setPreferredSize(new Dimension(300, 300));
    }

    public void showState(Visualizable state) {
        new JFrame() {
            {
                JComponent stateComponent = state.visualize();
                setPreferredSize(new Dimension(stateComponent.getWidth(), stateComponent.getHeight()));
                add(stateComponent);
                setVisible(true);
            }
        };
    }

    public void setDrawEveryStep(boolean drawEveryStep){
        this.drawEveryStep = drawEveryStep;
    }

    public void updateRewardGraph(List<Double> rewardHistory) {
        rewardChart.updateXYSeries("rewardHistory", null, rewardHistory, null);
        rewardChartPanel.revalidate();
        rewardChartPanel.repaint();
    }

    public void updateLearningInfoPanel() {
        this.learningInfoPanel.refreshLabels();
    }

    @Override
    public void onEpisodeEnd(List<Double> rewardHistory) {
            SwingUtilities.invokeLater(() ->{
                if(drawEveryStep){
                    updateRewardGraph(rewardHistory);
                }
                updateLearningInfoPanel();
            });
    }

    @Override
    public void onEpisodeStart() {
        if(drawEveryStep) {
            SwingUtilities.invokeLater(this::repaintEnvironment);
        }
    }

    @Override
    public void onStepEnd() {
        if(drawEveryStep){
            SwingUtilities.invokeLater(this::repaintEnvironment);
        }
    }

    private void repaintEnvironment(){
        if (environmentFrame != null) {
            environmentFrame.repaint();
        }
    }
}
