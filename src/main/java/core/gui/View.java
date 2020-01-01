package core.gui;

import core.Environment;
import core.algo.Learning;
import core.listener.ViewListener;
import lombok.Getter;
import org.javatuples.Pair;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class View<A extends Enum> implements LearningView{
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
    private JMenuBar menuBar;
    private JMenu fileMenu;

    public View(Learning<A> learning, Environment<A> environment, ViewListener viewListener) {
        this.learning = learning;
        this.environment = environment;
        this.viewListener = viewListener;
        initMainFrame();
    }

    private void initMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setPreferredSize(new Dimension(1280, 720));
        mainFrame.setLayout(new BorderLayout());
        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        fileMenu.add(new JMenuItem(new AbstractAction("Load") {
            @Override
            public void actionPerformed(ActionEvent e) {
                final JFileChooser fc = new JFileChooser();
                fc.setCurrentDirectory(new File(System.getProperty("user.dir")));
                int returnVal = fc.showOpenDialog(mainFrame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    viewListener.onLoadState(fc.getSelectedFile().toString());
                }
            }
        }));

        fileMenu.add(new JMenuItem(new AbstractAction("Save") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String fileName = JOptionPane.showInputDialog("Enter file name", "path/to/file");
                if(fileName != null){
                    viewListener.onSaveState(fileName);
                }
            }
        }));
        mainFrame.setJMenuBar(menuBar);
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

    public void updateRewardGraph(final List<Double> rewardHistory) {
        List<Integer> xValues;
        List<Double> yValues;
        if(learningInfoPanel.isLast100Selected()){
            yValues = new CopyOnWriteArrayList<>(rewardHistory.subList(rewardHistory.size() - Math.min(rewardHistory.size(), 100), rewardHistory.size()));
            xValues = new CopyOnWriteArrayList<>();
            for(int i = rewardHistory.size() - Math.min(rewardHistory.size(), 100); i <rewardHistory.size(); ++i){
                xValues.add(i);
            }
        }else{
            if(learningInfoPanel.isSmoothenGraphSelected()){
                Pair<List<Integer>, List<Double>> XYvalues = smoothenGraph(rewardHistory);
                xValues = XYvalues.getValue0();
                yValues = XYvalues.getValue1();
            }else{
                xValues = null;
                yValues = rewardHistory;
            }
        }

        rewardChart.updateXYSeries("rewardHistory", xValues, yValues, null);
        rewardChartPanel.revalidate();
        rewardChartPanel.repaint();
    }

    private Pair<List<Integer>, List<Double>> smoothenGraph(List<Double> original){
        int totalXPoints = 100;

        List<Integer> xValues = new CopyOnWriteArrayList<>();
        List<Double> tmp = new CopyOnWriteArrayList<>();
        int meanBatch = original.size() / totalXPoints;
        if(meanBatch < 1){
            meanBatch = 1;
        }

        int idx = 0;
        int batchIdx = 0;
        double batchSum = 0;
        for(Double x: original) {
            ++idx;
            batchSum += x;
            if (idx == 1 || ++batchIdx % meanBatch == 0) {
                tmp.add(batchSum / meanBatch);
                xValues.add(idx);
                batchSum = 0;
            }
        }
        return new Pair<>(xValues, tmp);
    }

    public void updateLearningInfoPanel() {
        this.learningInfoPanel.refreshLabels();
    }

    public void repaintEnvironment(){
        if (environmentFrame != null && learningInfoPanel.isDrawEnvironmentSelected()) {
            environmentFrame.repaint();
        }
    }
}
