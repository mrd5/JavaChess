package chess.engine.pieces;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Moves;
import chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.engine.board.BoardUtils.*;
import static chess.engine.board.Moves.*;

public class King extends Piece
{
    private final static int[] KING_MOVES = {-9, -8, -7, -1, 1, 7, 8, 9}; //Moves a king can make (one tile in each direction)

    public King(final int position, final Color color)
    {
        super(PieceType.KING, position, color, true);
    }

    public King(final int position, final Color color, boolean isFirstMove)
    {
        super(PieceType.KING, position, color, isFirstMove);
    }

    @Override
    public Collection<Moves> getLegalMoves(final Board board) //Returns all of the possible moves the king can make
    {
        final List<Moves> legalMoves = new ArrayList<>();

        for (final int currentOffset : KING_MOVES)
        {
            final int possibleCoordinate = this.position + currentOffset;

            if (BoardUtils.isValidCoordinate(possibleCoordinate))
            {
                final Tile possibleCoordinateTile = board.getTile(possibleCoordinate);

                if (!possibleCoordinateTile.isFull())
                {
                    legalMoves.add(new MajorMove(board, this, possibleCoordinate));
                }
                else
                {
                    final Piece destinationPiece = possibleCoordinateTile.getPiece();
                    final Color pieceColor = destinationPiece.getPieceColor();
                    if (this.getPieceColor() != pieceColor)
                    {
                        legalMoves.add(new MajorAttackMove(board, this,possibleCoordinate, destinationPiece));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public King movePiece(Moves move)
    {
        return new King(move.getDestination(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString()
    {
        return PieceType.KING.toString();
    }


    //Exceptions: Moves the king can't make
    private static boolean isFirstColumn(final int currentPosition, final int possibleMove) //If a knight is on the first column, there are some exceptions to the moves it can make
    {
        return FIRST_COLUMN[currentPosition] && ((possibleMove == -9) || (possibleMove == -1) || (possibleMove == 7));
    }

    private static boolean isEighthColumn(final int currentPosition, final int possibleMove) //Eighth column exceptions
    {
        return EIGHTH_COLUMN[currentPosition] && ((possibleMove == -7) || (possibleMove == 1) || (possibleMove == 9));
    }
}
