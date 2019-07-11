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


public class Bishop extends Piece
{
    private final static int[] BISHOP_MOVES = {-9, -7, 7, 9};

    public Bishop(int position, Color color)
    {
        super(PieceType.BISHOP, position, color);
    }

    @Override
    public Collection<Moves> getLegalMoves(final Board board)
    {
        final List<Moves> legalMoves = new ArrayList<>();

        for (final int currentOffset : BISHOP_MOVES)
        {
            int possibleCoordinate = this.position;

            while (isValidCoordinate(possibleCoordinate)) //Checks each diagonal move until an invalid move
            {
                if (isFirstColumn(possibleCoordinate, currentOffset) || isEighthColumn(possibleCoordinate, currentOffset))
                {
                    break;
                }

                possibleCoordinate += currentOffset;
                if (isValidCoordinate(possibleCoordinate))
                {
                    final Tile currentTile = board.getTile(possibleCoordinate); //Tile corresponding to the new posssible position of the knight

                    if (!currentTile.isFull()) //If tile is empty, knight can move to it
                    {
                        legalMoves.add(new Moves.MajorMove(board, this, possibleCoordinate));
                    } else //If tile is already occupied
                    {
                        final Piece currentTilePiece = currentTile.getPiece();
                        final Color pieceColor = currentTilePiece.getPieceColor();

                        if (this.color != pieceColor) //If occupied tile has a piece of the opposite color, knight can move to it and claim the piece on it
                        {
                            legalMoves.add(new AttackMove(board, this, possibleCoordinate, currentTilePiece));
                        }
                        break;
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString()
    {
        return PieceType.BISHOP.toString();
    }

    //Move exceptions for the bishop
    private static boolean isFirstColumn(final int position, final int possible)
    {
        return FIRST_COLUMN[position] && ((possible == -9) || (possible == 7));
    }

    private static boolean isEighthColumn(final int position, final int possible)
    {
        return EIGHTH_COLUMN[position] && ((possible == -7) || (possible == 9));
    }
}
