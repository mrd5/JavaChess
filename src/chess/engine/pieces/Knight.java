package chess.engine.pieces;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;
import chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece
{
    private final static int[] KNIGHT_MOVES = {-17, -15, -10, -6, 6, 10, 15, 17}; //Represents all of the possible moves a knight can make
    Knight(final int position, final Color color)
    {
        super(position, color);
    }

    @Override
    public List<Moves> getLegalMoves(Board board) //Returns a list of all possible tiles the knight can move to
    {
        final List<Moves> legalMoves = new ArrayList<>();
        for (final int current: KNIGHT_MOVES)
        {
                int possibleCoordinate = this.position + current; //Possible position the knight can move to

                if (possibleCoordinate >= 0 && possibleCoordinate < 64) //Coordinate must not be out of bounds
                {
                    final Tile currentTile = board.getTile(possibleCoordinate); //Tile corresponding to the new posssible position of the knight

                    if (!currentTile.isFull()) //If tile is empty, knight can move to it
                    {
                        legalMoves.add(new Moves());
                    }
                    else //If tile is already occupied
                    {
                        final Piece currentTilePiece = currentTile.getPiece();
                        final Color pieceColor = currentTilePiece.getPieceColor();

                        if (this.color != pieceColor) //If occupied tile has a piece of the opposite color, knight can move to it and claim the piece on it
                        {
                            legalMoves.add(new Moves());
                        }

                    }
                }
        }

        return ImmutableList.copyOf(legalMoves);
    }
}
