package chess.gui;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Moves;
import chess.engine.board.Tile;
import chess.engine.pieces.Piece;
import chess.engine.player.MoveTransition;
import chess.engine.player.ai.MinMax;
import chess.engine.player.ai.MoveStrategy;
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
import java.util.*;
import java.util.concurrent.ExecutionException;

import static javax.swing.SwingUtilities.isLeftMouseButton;
import static javax.swing.SwingUtilities.isRightMouseButton;

//This class deals with the game ui

public class Table extends Observable
{
    private final JFrame gameFrame;
    private final GameHistory gameHistory;
    private final TakenPieces takenPieces;
    private final BoardPanel boardPanel;
    private final MoveLog moveLog;
    private final GameSetup gameSetup;

    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;
    private BoardDirection boardDirection;

    private Moves computerMove;

    private boolean highlightLegalMoves;

    private static final int SEARCH_DEPTH = 3;
    private static final Dimension OUTER_FRAME_DIMENSION = new Dimension(600, 600);
    private static final Dimension BOARD_PANEL_DIMENSION = new Dimension(450, 450);
    private static final Dimension TILE_PANEL_DIMENSION = new Dimension(10, 15);
    private static String defaultPieceImagesPath = "icons/";

    private final Color lightTileColor = Color.decode("#FFFACD");
    private final Color darkTileColor = Color.decode("#593E1A");

    private static final Table INSTANCE = new Table();

    private Table()
    {
        this.gameFrame = new JFrame("JChess");
        this.gameFrame.setLayout(new BorderLayout());

        final JMenuBar tableMenuBar = createTableMenuBar();

        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.boardDirection = BoardDirection.NORMAL;
        this.moveLog = new MoveLog();
        this.addObserver(new TableGameAIWatcher());
        this.gameSetup = new GameSetup(this.gameFrame, true);
        this.gameHistory = new GameHistory();
        this.takenPieces = new TakenPieces();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.add(this.takenPieces, BorderLayout.WEST);
        this.gameFrame.add(this.gameHistory, BorderLayout.EAST);
        this.gameFrame.setVisible(true);
        this.highlightLegalMoves = false;
    }

    public static Table get()
    {
        return INSTANCE;
    }

    private JMenuBar createTableMenuBar()
    {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        tableMenuBar.add(createPreferencesMenu());
        tableMenuBar.add(createOptionsMenu());
        return tableMenuBar;
    }


    private JMenu createFileMenu() //Creates the File button which can be used to load a PGN file (not yet implemented) or Exit to close the game
    {
        final JMenu fileMenu = new JMenu("File");

        final JMenuItem openPGN = new JMenuItem("Load PGN File");
        openPGN.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("WORK IN PROGRESS");
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

    private JMenu createPreferencesMenu() //With the Preferences button, users can flip the board and highlight all valid moves once a piece has been selected to move
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

    private JMenu createOptionsMenu() //The Options menu allows plays to Setup the game
    {
        final JMenu optionsMenu = new JMenu("Options");
        final JMenuItem setupGameMenuItem = new JMenuItem("Setup Game");

        setupGameMenuItem.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                Table.get().getGameSetup().promptUser();
                Table.get().setupUpdate(Table.get().getGameSetup());
            }
        });

        optionsMenu.add(setupGameMenuItem);

        return optionsMenu;
    }

    private void setupUpdate(final GameSetup gameSetup) //Notifies the AI it can now make its move
    {
        setChanged();
        notifyObservers(gameSetup);
    }

    public void show()
    {
        Table.get().getMoveLog().clear();
        Table.get().getGameHistoryPanel().redo(chessBoard, Table.get().getMoveLog());
        Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
        Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
    }

    private static class TableGameAIWatcher implements Observer
    {

        @Override
        public void update(Observable o, Object arg)
        {
            if (Table.get().getGameSetup().isAIPlayer(Table.get().getGameBoard().currentPlayer()) &&
               !Table.get().getGameBoard().currentPlayer().isInCheckMate() &&
               !Table.get().getGameBoard().currentPlayer().isInStalemate()) //If more moves can be made
            {
                final AIThinkTank thinkTank = new AIThinkTank();
                thinkTank.execute();
            }

            if (Table.get().getGameBoard().currentPlayer().isInCheckMate()) //Player is in checkmate, game is done
            {
                System.out.println("Game over!!");
            }

            if (Table.get().getGameBoard().currentPlayer().isInStalemate()) //Player can't make a move, game is done
            {
                System.out.println("In stalemate!!!");
            }
        }
    }

    private static class AIThinkTank extends SwingWorker<Moves, String>
    {
        private AIThinkTank()
        {

        }

        @Override
        protected Moves doInBackground() throws Exception
        {
            final MoveStrategy minMax = new MinMax();
            final Moves bestMove = minMax.execute(Table.get().getGameBoard(), SEARCH_DEPTH); //Searches for the best move to make

            return bestMove;
        }

        @Override
        public void done() //Perform the move
        {
            try
            {
                final Moves bestMove = get();
                
                Table.get().updateComputerMove(bestMove);
                Table.get().updateGameBoard(Table.get().getGameBoard().currentPlayer().makeMove(bestMove).getTransitionBoard());
                Table.get().getMoveLog().addMove(bestMove);
                Table.get().getGameHistoryPanel().redo(Table.get().getGameBoard(), Table.get().getMoveLog());
                Table.get().getTakenPiecesPanel().redo(Table.get().getMoveLog());
                Table.get().getBoardPanel().drawBoard(Table.get().getGameBoard());
                Table.get().moveMadeUpdate(PlayerType.COMPUTER);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            catch (ExecutionException e)
            {
                e.printStackTrace();
            }
        }
    }

    private void moveMadeUpdate(final PlayerType computer)
    {
        setChanged();
        notifyObservers(computer);
    }

    private BoardPanel getBoardPanel()
    {
        return this.boardPanel;
    }

    private TakenPieces getTakenPiecesPanel()
    {
        return this.takenPieces;
    }

    private GameHistory getGameHistoryPanel()
    {
        return this.gameHistory;
    }

    private MoveLog getMoveLog()
    {
        return this.moveLog;
    }

    private void updateComputerMove(final Moves bestMove)
    {
        this.computerMove = bestMove;
    }

    private void updateGameBoard(final Board board)
    {
        this.chessBoard = board;
    }

    private GameSetup getGameSetup()
    {
        return this.gameSetup;
    }

    private Board getGameBoard()
    {
        return this.chessBoard;
    }

    public enum BoardDirection //Initially white is on the bottom of the board, and black is on top of the board
                               //By clicking the Flip Board button, the board will flip so that black is on botton and white on top
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

        BoardPanel() //Initializes and 8x8 board of tiles
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

        public void drawBoard(final Board board) //Draws the 8x8 board and all of the pieces on the board
        {
            removeAll();
            for (final TilePanel boardTile : boardDirection.traverse(boardTiles))
            {
                boardTile.drawTile(board);
                add(boardTile);
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

    enum PlayerType
    {
        HUMAN,
        COMPUTER
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
                    if (isRightMouseButton(e)) //Right clicking the mouse will unchoose a piece to move if one has been clicked on
                    {
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    else if (isLeftMouseButton(e)) //Clicking on a tile/piece
                    {
                        if (sourceTile == null) //Clicking on a blank tile
                        {
                            sourceTile = chessBoard.getTile(tileId);
                            humanMovedPiece = sourceTile.getPiece();

                            if (humanMovedPiece == null)
                            {
                                sourceTile = null;
                            }
                        }
                        else //Clicking on a tile that contains a piece
                        {
                            destinationTile = chessBoard.getTile(tileId);
                            final Moves move = Moves.MoveFactory.createMove(chessBoard, sourceTile.getTileCoordinate(), destinationTile.getTileCoordinate());
                            final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);

                            if (transition.getMoveStatus().isDone()) //If move has been made
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

                                if (gameSetup.isAIPlayer(chessBoard.currentPlayer()))
                                {
                                    Table.get().moveMadeUpdate(PlayerType.HUMAN);
                                }

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

        public void drawTile(final Board board) //Draws an individual tile
        {
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }

        private void highlightLegals(final Board board) //When the user clicks on a piece to move, all of the tiles it can move to will be highlighted
        {
            if (Table.get().getHighlightLegalMoves())
            {
                for (final Moves move : pieceLegalMoves(board))
                {
                    if (move.getDestination() == this.tileId)
                    {
                        try
                        {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File(defaultPieceImagesPath + "dot.png")))));
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }


        private Collection<Moves> pieceLegalMoves(final Board board) //Returns a collection of all of the legal moves of the last moved piece
        {
            if (humanMovedPiece != null && humanMovedPiece.getPieceColor() == board.currentPlayer().getColor())
            {
                return humanMovedPiece.getLegalMoves(board);
            }

            return Collections.emptyList();
        }
        private void assignTilePieceIcon(final Board board) //Associates a specific piece image to a tile
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

        private void assignTileColor() //The checkboard will be drawn in two different colors, as usual
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

    private boolean getHighlightLegalMoves()
    {
        return this.highlightLegalMoves;
    }
}
