package evironment.antGame.gui;

import javax.swing.*;
import java.awt.*;

public class HistoryPanel extends JPanel {
    private final int panelWidth =  1000;
    private final int panelHeight = 300;
    private JTextArea textArea;

    public HistoryPanel(){
        setPreferredSize(new Dimension(panelWidth, panelHeight));
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        JScrollPane scrollBar = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollBar.setPreferredSize(new Dimension(panelWidth, panelHeight));
        add(scrollBar);
        setVisible(true);
    }

    public void addText(String toAppend){
        textArea.append(toAppend);
        textArea.append("\n\n");
        revalidate();
    }
}
