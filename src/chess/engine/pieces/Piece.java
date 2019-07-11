package chess.engine.pieces;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;

import java.util.Collection;

public abstract class Piece
{
    protected final PieceType pieceType;
    protected final int position;
    protected final Color color;
    protected final boolean isFirstMove;

    Piece(final PieceType pieceType, int position, final Color color)
    {
        this.pieceType = pieceType;
        this.position = position;
        this.color = color;
        this.isFirstMove = false; //TODO
    }

    public PieceType getPieceType()
    {
        return this.pieceType;
    }

    public int getPiecePosition()
    {
        return this.position;
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

    public enum PieceType
    {
        PAWN("P")
        {
            @Override
            public boolean isKing()
            {
                return false;
            }
        },
        KNIGHT("N")
        {
            @Override
            public boolean isKing()
            {
                return false;
            }
        },
        BISHOP("B")
        {
            @Override
            public boolean isKing()
            {
                return false;
            }
        },
        QUEEN("Q")
        {
            @Override
            public boolean isKing()
            {
                return false;
            }
       },
        ROOK("R")
        {
            @Override
            public boolean isKing()
            {
                return false;
            }
        },
        KING("K")
        {
            @Override
            public boolean isKing()
            {
                return false;
            }
        };


        private String pieceName;

        PieceType(final String pieceName)
        {
            this.pieceName = pieceName;
        }

        @Override
        public String toString()
        {
            return this.pieceName;
        }

        public abstract boolean isKing();
    }
}
