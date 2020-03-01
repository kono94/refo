package evironment.blackjack.gui;

import evironment.blackjack.BlackJackTable;
import evironment.blackjack.Config;

import javax.swing.*;
import java.awt.*;

public class BlackJackTableComponent extends JComponent {
    private BlackJackTable blackJackTable;

    public BlackJackTableComponent(BlackJackTable blackJackTable) {
        this.blackJackTable = blackJackTable;
        setPreferredSize(new Dimension(Config.COMPONENT_WIDTH, Config.COMPONENT_HEIGHT));
        setVisible(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);
        g.drawString(blackJackTable.getPlayerSum() + " " + blackJackTable.getPlayer().isUsableAce(), 150, 300);
        g.drawString(blackJackTable.getDealerSum() + "", 150, 150);
    }
}
