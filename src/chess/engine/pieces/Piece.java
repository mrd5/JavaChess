package chess.engine.pieces;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;

import java.util.List;

public abstract class Piece
{
    protected final int position;
    protected final Color color;

    Piece(final int position, final Color color)
    {
        this.position = position;
        this.color = color;
    }

    public Color getPieceColor() //Pieces are either white or black
    {
        return this.color;
    }

    public abstract List<Moves> getLegalMoves(final Board board); //All of the possible moves a piece can make
}
