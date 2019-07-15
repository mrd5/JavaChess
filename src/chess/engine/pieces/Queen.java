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

public class Queen extends Piece
{
    private final static int[] QUEEN_MOVES = {-9, -8, -7, -1, 1, 7, 8, 9}; //Same as Rook OR Bishop

    public Queen(int position, Color color)
    {
        super(PieceType.QUEEN, position, color, true);
    }

    public Queen(int position, Color color, boolean isFirstMove)
    {
        super(PieceType.QUEEN, position, color, isFirstMove);
    }

    @Override
    public Collection<Moves> getLegalMoves(final Board board)
    {
        final List<Moves> legalMoves = new ArrayList<>();

        for (final int currentOffset : QUEEN_MOVES)
        {
            int possibleCoordinate = this.position;

            while (isValidCoordinate(possibleCoordinate))
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
    public Queen movePiece(Moves move) //Returns a new piece at the updated position (after a move)
    {
        return new Queen(move.getDestination(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString()
    {
        return PieceType.QUEEN.toString();
    }

    private static boolean isFirstColumn(final int currentPosition, final int possibleMove)
    {
        return FIRST_COLUMN[currentPosition] && ((possibleMove == -9) || (possibleMove == -1) || (possibleMove == 7));
    }

    private static boolean isEighthColumn(final int currentPosition, final int possibleMove)
    {
        return EIGHTH_COLUMN[currentPosition] && ((possibleMove == -7) || (possibleMove == 1) || (possibleMove == 9));
    }
}
