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

public class Rook extends Piece
{
    private final static int[] ROOK_MOVES = {-8, -1, 1, 8};

    public Rook(int position, Color color)
    {
        super(PieceType.ROOK, position, color, true);
    }

    public Rook(int position, Color color, boolean isFirstMove)
    {
        super(PieceType.ROOK, position, color, isFirstMove);
    }

    @Override
    public Collection<Moves> getLegalMoves(Board board)
    {
        final List<Moves> legalMoves = new ArrayList<>();

        for (final int currentOffset : ROOK_MOVES)
        {
            int possibleCoordinate = this.position;

            while (isValidCoordinate(possibleCoordinate)) //Checks each horizontal and vertical move until an invalid move
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
    public Rook movePiece(Moves move)
    {
        return new Rook(move.getDestination(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString()
    {
        return PieceType.ROOK.toString();
    }

    //Rook move exceptions
    private static boolean isFirstColumn(final int currentPosition, final int possibleMove)
    {
        return FIRST_COLUMN[currentPosition] && (possibleMove == -1);
    }

    private static boolean isEighthColumn(final int currentPosition, final int possibleMove)
    {
        return EIGHTH_COLUMN[currentPosition] && (possibleMove == 1);
    }
}
