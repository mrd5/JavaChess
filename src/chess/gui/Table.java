package chess.gui;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Moves;
import chess.engine.board.Tile;
import chess.engine.pieces.Piece;
import chess.engine.player.MoveTransition;
import com.google.common.collect.Lists;
import com.sun.xml.internal.bind.v2.TODO;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

public class Table
{
    private final JFrame gameFrame;
    private final GameHistory gameHistory;
    private final TakenPieces takenPieces;
    private final BoardPanel boardPanel;
    private final MoveLog moveLog;

    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;

    private boolean highlightLegalMoves;

    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(450, 450);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 15);
    private static String defaultPieceImagesPath = "icons/";

    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    public Table()
    {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());

        final JMenuBar tableMenuBar = createTableMenuBar();

        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.moveLog = new MoveLog();
        this.gameHistory = new GameHistory();
        this.takenPieces = new TakenPieces();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.takenPieces, BorderLayout.WEST);
        this.gameFrame.add(this.gameHistory, BorderLayout.EAST);
        this.gameFrame.setVisible(true);
        this.highlightLegalMoves = true;
    }

    private JMenuBar createTableMenuBar()
    {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        return tableMenuBar;
    }


    private JMenu createFileMenu()
    {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Finesse boys");
            }
        });
        fileMenu.add(openPGN);

        final JMenuItem exitMenuItem = new JMenuItem("Exit");
        exitMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.exit(0);
            }
        });
        fileMenu.add(exitMenuItem);

        return fileMenu;
    }

    private JMenu createPreferencesMenu()
    {
        final JMenu preferencesMenu = new JMenu("Preferences");
        final JMenuItem flipBoardMenuItem = new JMenuItem("Flip Board");
        flipBoardMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                boardDirection = boardDirection.opposite();
                boardPanel.drawBoard(chessBoard);
            }
        });
        preferencesMenu.add(flipBoardMenuItem);
        preferencesMenu.addSeparator();

        final JCheckBoxMenuItem legalMoveHighlighterCheckbox = new JCheckBoxMenuItem("Highlight Legal Moves", false);

        legalMoveHighlighterCheckbox.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                highlightLegalMoves = legalMoveHighlighterCheckbox.isSelected();
            }
        });
        preferencesMenu.add(legalMoveHighlighterCheckbox);

        return preferencesMenu;
    }

    public enum BoardDirection
    {
        NORMAL
        {
            @Override
            java.util.List<TilePanel> traverse(final java.util.List<TilePanel> boardTiles)
            {
                return boardTiles;
            }

            @Override
            BoardDirection opposite()
            {
                return FLIPPED;
            }
        },
        FLIPPED
        {
            @Override
            java.util.List<TilePanel> traverse(final java.util.List<TilePanel> boardTiles)
            {
                return Lists.reverse(boardTiles);
            }

            @Override
            BoardDirection opposite()
            {
                return NORMAL;
            }
        };

        abstract java.util.List<TilePanel> traverse(final java.util.List<TilePanel> boardTiles);
        abstract BoardDirection opposite();
    }

    private class BoardPanel extends JPanel
    {
        final java.util.List<TilePanel> boardTiles;

        BoardPanel()
        {
            super(new GridLayout(8, 8));
            this.boardTiles = new ArrayList<>();
            for (int i = 0; i < BoardUtils.NUM_TILES; i++)
            {
                final TilePanel tilePanel = new TilePanel(this, i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }

            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board)
        {
            removeAll();
            for (final TilePanel tilePanel : boardTiles)
            {
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public static class MoveLog
    {
        private final java.util.List<Moves> moves;

        MoveLog()
        {
            this.moves = new ArrayList<>();
        }

        public java.util.List<Moves> getMoves()
        {
            return this.moves;
        }

        public void addMove(final Moves move)
        {
            this.moves.add(move);
        }

        public int size()
        {
            return this.moves.size();
        }

        public void clear()
        {
            this.moves.clear();
        }

        public Moves removeMove(int index)
        {
            return this.moves.remove(index);
        }

        public boolean removeMove(final Moves move)
        {
            return this.moves.remove(move);
        }
    }

    private class TilePanel extends JPanel
    {
        private final int tileId;

        TilePanel(final BoardPanel boardPanel, final int tileId)
        {
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    if (isRightMouseButton(e))
                    {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    else if (isLeftMouseButton(e))
                    {
                        if (sourceTile == null)
                        {
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();

                            System.out.println("TEST");

                            if (humanMovedPiece == null)
                            {
                                sourceTile = null;
                            }
                        }
                        else
                        {
                            destinationTile = chessBoard.getTile(tileId);
                            final Moves move = Moves.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);

                            if (transition.getMoveStatus().isDone())
                            {
                                chessBoard = transition.getTransitionBoard();
                                moveLog.addMove(move);
                            }
                            sourceTile = null;
                            destinationTile = null;
                            humanMovedPiece = null;
                        }
                        SwingUtilities.invokeLater(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                gameHistory.redo(chessBoard, moveLog);
                                takenPieces.redo(moveLog);
                                boardPanel.drawBoard(chessBoard);
                            }
                        });
                    }
                }

                @Override
                public void mousePressed(MouseEvent e)
                {

                }

                @Override
                public void mouseReleased(MouseEvent e)
                {

                }

                @Override
                public void mouseEntered(MouseEvent e)
                {

                }

                @Override
                public void mouseExited(MouseEvent e)
                {

                }
            });
            validate();
        }

        public void drawTile(final Board board)
        {
            assignTileColor();
            assignTilePieceIcon(board);
            validate();
            repaint();
        }

        private void highlightLegals(final Board board)
        {
            if (highlightLegalMoves)
            {
                for (final Moves move : pieceLegalMoves(board))
                {
                    if (move.getDestination() == this.tileId)
                    {
                        try
                        {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPieceImagesPath + "dot.png")))));
                        } catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


        private Collection<Moves> pieceLegalMoves(final Board board)
        {
            if (humanMovedPiece != null && humanMovedPiece.getPieceColor() == board.currentPlayer().getColor())
            {
                return humanMovedPiece.getLegalMoves(board);
            }

            return Collections.emptyList();
        }

        private void assignTilePieceIcon(final Board board)
        {
            this.removeAll();
            if (board.getTile(this.tileId).isFull())
            {
                try
                {
                    final BufferedImage image = ImageIO.read(new File(defaultPieceImagesPath +  board.getTile(this.tileId).getPiece().getPieceColor().toString().substring(0, 1) + board.getTile(this.tileId).getPiece().toString() + ".png"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        private void assignTileColor()
        {
            if (BoardUtils.FIRST_ROW[this.tileId] ||
                BoardUtils.THIRD_ROW[this.tileId] ||
                BoardUtils.FIFTH_ROW[this.tileId] ||
                BoardUtils.SEVENTH_ROW[this.tileId])
            {
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            }
            else
            {
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }
    }
}
