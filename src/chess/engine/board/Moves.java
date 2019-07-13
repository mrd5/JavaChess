package chess.engine.board;

import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;

public abstract class Moves
{
    final Board board;
    final Piece movedPiece;
    final int destination;

    public static final Moves NULL_MOVE = new NullMove();

    private Moves(final Board board, final Piece movedPiece, final int destination)
    {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destination = destination;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.destination;
        result = prime * result + this.movedPiece.hashCode();
        return result;
    }

    @Override public boolean equals(final Object other)
    {
        if (this == other)
        {
            if (!(other instanceof Moves))
            {
                return false;
            }

            final Moves otherMove = (Moves) other;
            return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
                   getDestination() == otherMove.getDestination() &&
                   getMovedPiece() == otherMove.getMovedPiece();
        }
    }

    public int getCurrentCoordinate()
    {
        return this.movedPiece.getPiecePosition();
    }

    public int getDestination()
    {
        return this.destination;
    }

    public Piece getMovedPiece()
    {
        return this.movedPiece;
    }

    public boolean isCastlingMove()
    {
        return false;
    }

    public Piece getAttackedPiece()
    {
        return null;
    }

    public Board execute() //Returns a new board updated with the new move
    {
        final Board.Builder builder = new Board.Builder();

        for (final Piece piece : this.board.currentPlayer().getActivePieces()) //Set all of the pieces of the current player
        {
            if (!this.movedPiece.equals(piece)) //Don't set the current move piece to its original position
            {
                builder.setPiece(piece);
            }
        }

        for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()) //Set all of the pieces of the opponent
        {
            builder.setPiece(piece);
        }

        builder.setPiece(this.movedPiece.movePiece(this)); //Move the moved piece

        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor()); //Set MoveMaker to the opponent for their turn

        return builder.build();
    }

    public static final class MajorMove extends Moves //A Major move is a move where no opposing piece is claimed by the user
    {
        public MajorMove(final Board board, final Piece movedPiece, final int destination)
        {
            super(board, movedPiece, destination);
        }
    }

    public static class AttackMove extends Moves //An Attack move is one which claims the other players' piece
    {
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destination, final Piece attackedPiece)
        {
            super(board, movedPiece, destination);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode()
        {
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other)
        {
            if (this == other)
            {
                return true;
            }

            if (!(other instanceof AttackMove))
            {
                return false;
            }

            final AttackMove otherAttackMove = (AttackMove) other;

            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }

        @Override
        public Board execute()
        {
            return null;
        }

        @Override
        public boolean isAttack()
        {

        }

        @Override
        public Piece getAttackedPiece()
        {
            return this.attackedPiece;
        }
    }

    public static final class PawnMove extends Moves //A Major move is a move where no opposing piece is claimed by the user
    {
        public PawnMove(final Board board, final Piece movedPiece, final int destination)
        {
            super(board, movedPiece, destination);
        }
    }

    public static class PawnAttackMove extends AttackMove //A Major move is a move where no opposing piece is claimed by the user
    {
        public PawnAttackMove(final Board board, final Piece movedPiece, final int destination, final Piece attackedPiece)
        {
            super(board, movedPiece, destination, attackedPiece);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove //A Major move is a move where no opposing piece is claimed by the user
    {
        public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destination, final Piece attackedPiece)
        {
            super(board, movedPiece, destination, attackedPiece);
        }
    }

    public static final class PawnJump extends Moves //A Major move is a move where no opposing piece is claimed by the user
    {
        public PawnJump(final Board board, final Piece movedPiece, final int destination)
        {
            super(board, movedPiece, destination);
        }

        @Override
        public Board execute()
        {
            final Board.Builder builder = new Board.Builder();

            for (final Piece piece : this.board.currentPlayer().getActivePieces())
            {
                if (!this.movedPiece.equals(piece))
                {
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces())
            {
                builder.setPiece(piece);
            }

            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setEnPassantPawn(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());

            return builder.build();
        }
    }

    abstract static class CastleMove extends Moves //A Major move is a move where no opposing piece is claimed by the user
    {
        protected final Rook castleRook;
        protected final int castleRookStart;
        protected final int castleRookDestination;

        public CastleMove(final Board board, final Piece movedPiece, final int destination, Rook castleRook, int castleRookStart, int castleRookDestination)
        {
            super(board, movedPiece, destination);
            this.castleRook = castleRook;
            this.castleRookStart = castleRookStart;
            this.castleRookDestination = castleRookDestination;
        }

        public Rook getCastleRook()
        {
            return this.castleRook;
        }

        @Override
        public boolean isCastlingMove()
        {
            return true;
        }

        @Override
        public Board execute()
        {
            final Board.Builder builder = new Board.Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces())
            {
                if (!this.movedPiece.equals(piece) && !this.castleRook.equals(piece))
                {
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces())
            {
                builder.setPiece(piece);
            }

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setPiece(new Rook(this.castleRookDestination, this.castleRook.getPieceColor()));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());

            return builder.build();
        }
    }

    public static final class KingSideCastleMove extends CastleMove //A Major move is a move where no opposing piece is claimed by the user
    {
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destination, Rook castleRook, int castleRookStart, int castleRookDestination)
        {
            super(board, movedPiece, destination, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString()
        {
            return "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove //A Major move is a move where no opposing piece is claimed by the user
    {
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destination, Rook castleRook, int castleRookStart, int castleRookDestination)
        {
            super(board, movedPiece, destination, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public String toString()
        {
            return "0-0-0";
        }
    }

    public static final class NullMove extends Moves //A Major move is a move where no opposing piece is claimed by the user
    {
        public NullMove()
        {
            super(null, null, -1);
        }

        @Override
        public Board execute()
        {
            throw new RuntimeException("How did you do this??");
        }
    }

    public static class MoveFactory
    {
        private MoveFactory()
        {
            throw new RuntimeException("What the!?");
        }

        public static Moves createMove(final Board board, final int currentPosition, final int destinationPosition)
        {
            for (final Moves move : board.getAllLegalMoves())
            {
                if (move.getCurrentCoordinate() == currentPosition && move.getDestination() == destinationPosition)
                {
                    return move;
                }
            }

            return NULL_MOVE;
        }
    }
}
