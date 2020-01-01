package core.gui;

import core.State;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;

@Setter
public class StateActionRow<A extends Enum> extends JTextArea {
    private State state;
    private Map<A, Double> actionValues;

    public StateActionRow(){
        this.state = null;
        this.actionValues = null;
        setMaximumSize(new Dimension(600, 100));
        setEditable(false);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                showState();
            }
        });
    }

    protected void refreshLabels(){
        if(state == null || actionValues == null) return;
        System.out.println("refreshing");
        StringBuilder sb = new StringBuilder(state.toString()).append("\n");
        for(Map.Entry<A, Double> actionValue: actionValues.entrySet()){
            sb.append("\t").append(actionValue.getKey()).append("\t").append(actionValue.getValue()).append("\n");
        }
        setText(sb.toString());
    }

    private void showState() {
        if(state != null && state instanceof Visualizable){
            new JFrame() {
                {
                    JComponent stateComponent = ((Visualizable)state).visualize();
                    setPreferredSize(stateComponent.getPreferredSize());
                    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    add(stateComponent);
                    pack();
                    setVisible(true);
                }
            };
        }
    }
}
