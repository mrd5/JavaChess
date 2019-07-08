package chess.engine.pieces;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;
import chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.engine.board.BoardUtils.*;
import static chess.engine.board.Moves.*;


public class Knight extends Piece
{
    private final static int[] KNIGHT_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17}; //Represents all of the possible moves a knight can make
    Knight(final int position, final Color color)
    {
        super(position, color);
    }

    @Override
    public Collection<Moves> getLegalMoves(final Board board) //Returns a list of all possible tiles the knight can move to
    {
        final List<Moves> legalMoves = new ArrayList<>();
        for (final int current: KNIGHT_MOVES)
        {
                int possibleCoordinate = this.position + current; //Possible position the knight can move to

                if (isValidCoordinate(possibleCoordinate)) //Coordinate must not be out of bounds
                {
                    if (isFirstColumn(position, possibleCoordinate) ||
                            isSecondColumn(position, possibleCoordinate) ||
                            isSeventhColumn(position, possibleCoordinate) ||
                            isRightColumn(position, possibleCoordinate))
                    {
                        continue;
                    }

                    final Tile currentTile = board.getTile(possibleCoordinate); //Tile corresponding to the new posssible position of the knight

                    if (!currentTile.isFull()) //If tile is empty, knight can move to it
                    {
                        legalMoves.add(new MajorMove(board, this, possibleCoordinate));
                    }
                    else //If tile is already occupied
                    {
                        final Piece currentTilePiece = currentTile.getPiece();
                        final Color pieceColor = currentTilePiece.getPieceColor();

                        if (this.color != pieceColor) //If occupied tile has a piece of the opposite color, knight can move to it and claim the piece on it
                        {
                            legalMoves.add(new AttackMove(board, this, possibleCoordinate, currentTilePiece));
                        }

                    }
                }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isFirstColumn(final int current, final int possible)
    {
        return LEFT_COLUMN[current] && ((possible == -17) || (possible == -10) || (possible == 6) || (possible == 15));
    }

    private static boolean isSecondColumn(final int current, final int possible)
    {
        return SECOND_COLUMN[current] && ((possible == -10) || (possible == 6));
    }

    private static boolean isSeventhColumn(final int current, final int possible)
    {
        return SEVENTH_COLUMN[current] && ((possible == -6) || (possible == 10));
    }

    private static boolean isRightColumn(final int current, final int possible)
    {
        return RIGHT_COLUMN[current] && ((possible == -15) || (possible == -6) || (possible == 10) || (possible == 17));
    }
}
