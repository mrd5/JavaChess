package chess.gui;

import chess.engine.board.Moves;
import chess.engine.pieces.Piece;
import com.google.common.primitives.Ints;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

//This displays the icons of all of the taken pieces, on the left-hand side of the screen

public class TakenPieces extends JPanel
{
    private static final EtchedBorder PANEL_BORDER = new EtchedBorder(EtchedBorder.RAISED);
    private static final Color PANEL_COLOR = Color.decode("0xFDF5F6");
    private static final Dimension TAKEN_PIECES_DIMENSION = new Dimension(40, 80);
    private final JPanel northPanel;
    private final JPanel southPanel;

    public TakenPieces()
    {
        super(new BorderLayout());
        setBackground(PANEL_COLOR);
        setBorder(PANEL_BORDER);
        this.northPanel = new JPanel(new GridLayout(8, 2));
        this.southPanel = new JPanel(new GridLayout(8, 2));
        this.northPanel.setBackground(PANEL_COLOR);
        this.southPanel.setBackground(PANEL_COLOR);
        this.add(this.northPanel, BorderLayout.NORTH);
        this.add(this.southPanel, BorderLayout.SOUTH);
        setPreferredSize(TAKEN_PIECES_DIMENSION);
    }

    public void redo(final Table.MoveLog moveLog)
    {
        this.northPanel.removeAll();
        this.southPanel.removeAll();

        final java.util.List<Piece> whiteTakenPieces = new ArrayList<>();
        final java.util.List<Piece> blackTakenPieces = new ArrayList<>();

        for (final Moves move : moveLog.getMoves())
        {
            if (move.isAttack())
            {
                final Piece takenPiece = move.getAttackedPiece();
                if (takenPiece.getPieceColor().isWhite())
                {
                    whiteTakenPieces.add(takenPiece);
                }
                else if (takenPiece.getPieceColor().isBlack())
                {
                    blackTakenPieces.add(takenPiece);
                }
                else
                {
                    throw new RuntimeException("HOW!?");
                }
            }
        }

        Collections.sort(whiteTakenPieces, new Comparator<Piece>()
        {
            @Override
            public int compare(Piece o1, Piece o2)
            {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        Collections.sort(blackTakenPieces, new Comparator<Piece>()
        {
            @Override
            public int compare(Piece o1, Piece o2)
            {
                return Ints.compare(o1.getPieceValue(), o2.getPieceValue());
            }
        });

        for (final Piece takenPiece : whiteTakenPieces)
        {
            try
            {
                System.out.println("icons/" + takenPiece.getPieceColor().toString().substring(0, 1) + takenPiece.toString());
                final BufferedImage image = ImageIO.read(new File("icons/" + takenPiece.getPieceColor().toString().substring(0, 1) + takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.southPanel.add(imageLabel);
            } catch(final IOException e)
            {
                e.printStackTrace();
            }
        }

        for (final Piece takenPiece : blackTakenPieces)
        {
            try
            {
                final BufferedImage image = ImageIO.read(new File("icons/" + takenPiece.getPieceColor().toString().substring(0, 1) + takenPiece.toString() + ".png"));
                final ImageIcon icon = new ImageIcon(image);
                final JLabel imageLabel = new JLabel();
                this.southPanel.add(imageLabel);
            } catch(final IOException e)
            {
                e.printStackTrace();
            }
        }

        validate();
    }
}
