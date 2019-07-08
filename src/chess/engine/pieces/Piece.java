package chess.engine.pieces;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;

import java.util.Collection;

public abstract class Piece
{
    protected final int position;
    protected final Color color;
    protected final boolean isFirstMove;

    Piece(final int position, final Color color)
    {
        this.position = position;
        this.color = color;
        this.isFirstMove = false; //TODO
    }

    public Color getPieceColor() //Pieces are either white or black
    {
        return this.color;
    }

    public boolean isFirstMove()
    {
        return this.isFirstMove;
    }

    public abstract Collection<Moves> getLegalMoves(final Board board); //All of the possible moves a piece can make
}
