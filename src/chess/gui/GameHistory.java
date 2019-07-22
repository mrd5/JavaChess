package chess.gui;

import chess.engine.board.Board;
import chess.engine.board.Moves;
import com.sun.rowset.internal.Row;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

//Game history panel on the right side of the ui, which displays the moves made by both players

public class GameHistory extends JPanel
{
    private final DataModel model;
    private final JScrollPane scrollPane;
    private static final Dimension HISTORY_PANEL_DIMENSION = new Dimension(100, 400);

    GameHistory()
    {
        this.setLayout(new BorderLayout());
        this.model = new DataModel();
        final JTable table = new JTable(model);
        table.setRowHeight(15);
        this.scrollPane = new JScrollPane(table);
        scrollPane.setColumnHeaderView(table.getTableHeader());
        scrollPane.setPreferredSize(HISTORY_PANEL_DIMENSION);
        this.add(scrollPane, BorderLayout.CENTER);
        this.setVisible(true);
    }

    void redo(final Board board, final Table.MoveLog moveHistory) //Redo a move, not yet fully implemented
    {
        int currentRow = 0;
        this.model.clear();
        for (final Moves move : moveHistory.getMoves())
        {
            final String moveText = move.toString();
            if (move.getMovedPiece().getPieceColor().isWhite())
            {
                this.model.setValueAt(moveText, currentRow, 0);
            }
            else if (move.getMovedPiece().getPieceColor().isBlack())
            {
                this.model.setValueAt(moveText, currentRow, 1);
                currentRow++;
            }
        }

        if (moveHistory.getMoves().size() > 0)
        {
            final Moves lastMove = moveHistory.getMoves().get(moveHistory.size() - 1);
            final String moveText = lastMove.toString();

            if (lastMove.getMovedPiece().getPieceColor().isWhite())
            {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow, 1);
            }
            else if (lastMove.getMovedPiece().getPieceColor().isBlack())
            {
                this.model.setValueAt(moveText + calculateCheckAndCheckMateHash(board), currentRow - 1, 1);
            }
        }

        final JScrollBar vertical = scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private String calculateCheckAndCheckMateHash(Board board)
    {
        if (board.currentPlayer().isInCheckMate()) //Displays a # when the current player is in checkmate
        {
            return "#";
        }
        else if (board.currentPlayer().isInCheck()) // + is displayed for the user when in check and not checkmate
        {
            return "+";
        }
        return "";
    }

    private static class DataModel extends DefaultTableModel
        //Used to store and set the data into the game history panel
    {
        private final java.util.List<GameHistory.Row> values;
        private static final String[] NAMES = {"White", "Black"};

        DataModel()
        {
            this.values = new ArrayList<>();
        }

        public void clear() //Reset the columns
        {
            this.values.clear();
            setRowCount(0);
        }

        @Override
        public int getRowCount() //Returns the size of the row of data
        {
            if (this.values == null)
            {
                return 0;
            }

            return this.values.size();
        }

        @Override
        public int getColumnCount() //Returns 2
        {
            return NAMES.length;
        }

        @Override
        public Object getValueAt(final int row, final int column) //Returns the value at a specific entry in the table
        {
            final GameHistory.Row currentRow = this.values.get(row);
            if (column == 0)
            {
                return currentRow.getWhiteMove();
            }
            else if (column == 1)
            {
                return currentRow.getBlackMove();
            }
            return null;
        }

        @Override
        public void setValueAt(final Object aValue, final int row, final int column) //Sets a value at a specific entry in the table
        {
            final GameHistory.Row currentRow;
            if (this.values.size() <= row)
            {
                currentRow = new GameHistory.Row();
                this.values.add(currentRow);
            }
            else
            {
                currentRow = this.values.get(row);
            }
            if (column == 0)
            {
                currentRow.setWhiteMove((String)aValue);
                fireTableRowsInserted(row, row);
            }
            else if (column == 1)
            {
                currentRow.setBlackMove((String)aValue);
                fireTableCellUpdated(row, column);
            }
        }

        @Override
        public Class<?> getColumnClass(final int column)
        {
            return Moves.class;
        }

        @Override
        public String getColumnName(final int column)
        {
            return NAMES[column];
        }
    }

    private static class Row
    {
        private String whiteMove;
        private String blackMove;

        Row()
        {

        }

        public String getWhiteMove()
        {
            return this.whiteMove;
        }

        public String getBlackMove()
        {
            return this.blackMove;
        }

        public void setWhiteMove(final String move)
        {
            this.whiteMove = move;
        }

        public void setBlackMove(final String move)
        {
            this.blackMove = move;
        }
    }
}
