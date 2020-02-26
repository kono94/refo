package core.gui;

import core.State;
import core.StateActionTable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QTableFrame<A extends Enum> extends JFrame {
    private JLabel stateCountLabel;
    private StateActionTable<A> stateActionTable;
    private List<StateActionRow<A>> rows;
    private JPanel areaWrapper;

    public QTableFrame(StateActionTable<A> stateActionTable) {
        super("Q-Table");
        this.stateActionTable = stateActionTable;
        rows = new ArrayList<>(10);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(500, 500));
        stateCountLabel = new JLabel();
        add(BorderLayout.NORTH, stateCountLabel);
        areaWrapper = new JPanel();
        areaWrapper.setLayout(new BoxLayout(areaWrapper, BoxLayout.Y_AXIS));
        for(int i = 0; i < 10; ++i) {
            StateActionRow<A> a = new StateActionRow<>();
            rows.add(a);
            areaWrapper.add(a);
        }
        add(BorderLayout.CENTER, areaWrapper);
        setVisible(false);
        pack();
    }

    private void refreshAllTextAreas(){
        for(StateActionRow<A> row : rows){
            row.refreshLabels();
        }
    }
    protected void refreshQTable() {
        int stateCount = stateActionTable.getStateCount();
        stateCountLabel.setText("Total states: " + stateCount);
        int idx = -1;
        for(Map.Entry<State, Map<A, Double>> entry : stateActionTable.getFirstStateEntriesForView()) {
            if(++idx > rows.size() -1) break;
            StateActionRow<A> row = rows.get(idx);
            row.setState(entry.getKey());
            row.setActionValues(entry.getValue());
        }
        refreshAllTextAreas();
    }
}
