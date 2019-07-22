package chess.engine.pieces;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;

import java.util.Collection;

//Abstract piece class that each type of piece will extend

public abstract class Piece
{
    protected final PieceType pieceType;
    protected final int position;
    protected final Color color;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType, int position, final Color color, final boolean isFirstMove)
    {
        this.pieceType = pieceType;
        this.position = position;
        this.color = color;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode()
    {
        int result = pieceType.hashCode();
        result = 31 * result + color.hashCode();
        result = 31 * result * position;
        result = 31 * result + (isFirstMove ? 1 : 0);
        return result;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (this == other)
        {
            return true;
        }
        if (!(other instanceof Piece))
        {
            return false;
        }

        final Piece otherPiece = (Piece) other;

        return position == otherPiece.getPiecePosition() && pieceType == otherPiece.getPieceType() && color == otherPiece.getPieceColor() && isFirstMove == otherPiece.isFirstMove();
    }

    @Override
    public int hashCode()
    {
        return this.cachedHashCode;
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

    public int getPieceValue()
    {
        return this.pieceType.getPieceValue();
    }

    public boolean isFirstMove()
    {
        return this.isFirstMove;
    }

    public abstract Collection<Moves> getLegalMoves(final Board board); //All of the possible moves a piece can make

    public abstract Piece movePiece(Moves move);



    public enum PieceType //Each type of piece will have a value associated with it, used by the AI when analyzing the moves
    {
        PAWN("P", 100)
        {
            @Override
            public boolean isKing()
            {
                return false;
            }

            @Override
            public boolean isRook()
            {
                return false;
            }
        },
        KNIGHT("N", 300)
        {
            @Override
            public boolean isKing()
            {
                return false;
            }

            @Override
            public boolean isRook()
            {
                return false;
            }
        },
        BISHOP("B", 300)
        {
            @Override
            public boolean isKing()
            {
                return false;
            }

            @Override
            public boolean isRook()
            {
                return false;
            }
        },
        QUEEN("Q", 900)
        {
            @Override
            public boolean isKing()
            {
                return false;
            }

            @Override
            public boolean isRook()
            {
                return false;
            }
       },
        ROOK("R", 500)
        {
            @Override
            public boolean isKing()
            {
                return false;
            }

            @Override
            public boolean isRook()
            {
                return true;
            }
        },
        KING("K", 10000)
        {
            @Override
            public boolean isKing()
            {
                return true;
            }

            @Override
            public boolean isRook()
            {
                return false;
            }
        };


        private String pieceName;
        private int pieceValue;

        PieceType(final String pieceName, final int pieceValue)
        {
            this.pieceName = pieceName;
            this.pieceValue = pieceValue;
        }

        @Override
        public String toString()
        {
            return this.pieceName;
        }

        public int getPieceValue()
        {
            return this.pieceValue;
        } //Returns the value of a piece

        public abstract boolean isKing();
        public abstract boolean isRook();
    }
}
