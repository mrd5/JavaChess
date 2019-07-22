package chess.gui;

import chess.engine.Color;
import chess.engine.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//This class deals with the Game Setup button ui

public class GameSetup extends JDialog
{
    private Table.PlayerType whitePlayer;
    private Table.PlayerType blackPlayer;
    private JSpinner searchDepth;

    private static final String HUMAN_TEXT = "Human";
    private static final String COMPUTER_TEXT = "Computer";

    GameSetup(final JFrame frame, final boolean modal)
    {
        super(frame, modal);
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));
        final JRadioButton whiteHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton whiteComputerButton = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton blackHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton blackComputerButton = new JRadioButton(COMPUTER_TEXT);
        whiteHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup whiteGroup = new ButtonGroup();
        whiteGroup.add(whiteHumanButton);
        whiteGroup.add(whiteComputerButton);
        whiteHumanButton.setSelected(true);

        final ButtonGroup blackGroup = new ButtonGroup();
        blackGroup.add(blackHumanButton);
        blackGroup.add(blackComputerButton);
        blackHumanButton.setSelected(true);

        getContentPane().add(myPanel);
        myPanel.add(new JLabel("White"));
        myPanel.add(whiteHumanButton);
        myPanel.add(whiteComputerButton);
        myPanel.add(new JLabel("Black"));
        myPanel.add(blackHumanButton);
        myPanel.add(blackComputerButton);
        myPanel.add(new JLabel("Search"));

        this.searchDepth = addLabeledSpinner(myPanel, "SearchDepth", new SpinnerNumberModel(3, 0, Integer.MAX_VALUE, 1));

        final JButton cancelButton = new JButton("Cancel");
        final JButton okButton = new JButton("OK");

        okButton.addActionListener(new ActionListener() //When a user clicks ok
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                //Black and white players can both either be a computer or human
                whitePlayer = whiteComputerButton.isSelected() ? Table.PlayerType.COMPUTER : Table.PlayerType.HUMAN;
                blackPlayer = blackComputerButton.isSelected() ? Table.PlayerType.COMPUTER : Table.PlayerType.HUMAN;
                GameSetup.this.setVisible(false);
            }
        });

        cancelButton.addActionListener(new ActionListener() //User clicks cancel, closing menu
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Cancel");
                GameSetup.this.setVisible(false);
            }
        });

        myPanel.add(cancelButton);
        myPanel.add(okButton);

        setLocationRelativeTo(frame);
        pack();
        setVisible(false);
    }

    void promptUser() //Displays the menu
    {
        setVisible(true);
        repaint();
    }

    boolean isAIPlayer(final Player player) //True is returned if the player is a computer
    {
        if (player.getColor() == Color.WHITE)
        {
            return getWhitePlayerType() == Table.PlayerType.COMPUTER;
        }
        return getBlackPlayerType() == Table.PlayerType.COMPUTER;
    }

    Table.PlayerType getWhitePlayerType()
    {
        return this.whitePlayer;
    }

    Table.PlayerType getBlackPlayerType()
    {
        return this.blackPlayer;
    }

    private static JSpinner addLabeledSpinner(Container c, String label, SpinnerModel model)
    {
        final JLabel l = new JLabel(label);
        c.add(l);
        final JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    int getSearchDepth() //Search depth used by the ai when deciding on a move
    {
        return (Integer) this.searchDepth.getValue();
    }
}
