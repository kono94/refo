package core.gui;

import core.algo.Learning;
import core.controller.ViewListener;
import core.listener.LearningListener;
import lombok.Getter;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class View<A extends Enum> implements LearningListener {
    private Learning<A> learning;
    @Getter
    private XYChart chart;
    @Getter
    private LearningInfoPanel learningInfoPanel;
    @Getter
    private JFrame mainFrame;
    private XChartPanel<XYChart> rewardChartPanel;
    private ViewListener viewListener;

    public View(Learning<A> learning, ViewListener viewListener){
        this.learning = learning;
        this.viewListener = viewListener;
        this.initMainFrame();
    }

    private void initMainFrame(){
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
    }

    private void initLearningInfoPanel(){
        learningInfoPanel = new LearningInfoPanel(learning, viewListener);
    }

    private void initRewardChart(){
        chart =
                QuickChart.getChart(
                        "Rewards per Episode",
                        "Episode",
                        "Reward",
                        "randomWalk",
                        new double[] {0},
                        new double[] {0});
        chart.getStyler().setLegendVisible(true);
        chart.getStyler().setXAxisTicksVisible(true);
        rewardChartPanel = new XChartPanel<>(chart);
        rewardChartPanel.setPreferredSize(new Dimension(300,300));
    }

    public void showState(Visualizable state){
        new JFrame(){
            {
                JComponent stateComponent = state.visualize();
                setPreferredSize(new Dimension(stateComponent.getWidth(), stateComponent.getHeight()));
                add(stateComponent);
                setVisible(true);
            }
        };
    }

    public void updateRewardGraph(List<Double> rewardHistory){
        chart.updateXYSeries("randomWalk", null, rewardHistory, null);
        rewardChartPanel.revalidate();
        rewardChartPanel.repaint();
    }

    public void updateLearningInfoPanel(){
        this.learningInfoPanel.refreshLabels();
    }

    @Override
    public void onEpisodeEnd(List<Double> rewardHistory) {
        SwingUtilities.invokeLater(()->{
            updateRewardGraph(rewardHistory);
        });
    }

    @Override
    public void onEpisodeStart() {

    }
}
