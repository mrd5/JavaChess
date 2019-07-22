package chess.engine.board;

import chess.engine.pieces.Pawn;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;


public abstract class Moves
{
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destination;
    protected final boolean isFirstMove;

    public static final Moves NULL_MOVE = new NullMove();

    private Moves(final Board board, final Piece movedPiece, final int destination)
    {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destination = destination;
        this.isFirstMove = movedPiece.isFirstMove();
    }

    private Moves(final Board board, final int destination)
    {
        this.board = board;
        this.destination = destination;
        this.movedPiece = null;
        this.isFirstMove = false;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = prime * result + this.destination;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (this == other)
        {
            return true;
        }

        if (!(other instanceof Moves))
        {
            return false;
        }

        final Moves otherMove = (Moves) other;
        return getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
               getDestination() == otherMove.getDestination() &&
               getMovedPiece().equals(otherMove.getMovedPiece());
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

    public boolean isAttack()
    {
        return false;
    }

    public boolean isCastlingMove()
    {
        return false;
    }

    public Piece getAttackedPiece()
    {
        return null;
    }

    public Board getBoard() { return this.board; }

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

    public static class MajorAttackMove extends AttackMove
    {
        public MajorAttackMove(final Board board, final Piece pieceMoved, final int destination, final Piece attackedPiece)
        {
            super(board, pieceMoved, destination, attackedPiece);
        }

        @Override
        public boolean equals(final Object other)
        {
            return this == other || other instanceof MajorAttackMove && super.equals(other);
        }

        @Override
        public String toString()
        {
            return movedPiece.getPieceType() + BoardUtils.getPositionAtCoordinate(this.destination);
        }
    }

    public static final class MajorMove extends Moves //A Major move is simply a move
    {
        public MajorMove(final Board board, final Piece movedPiece, final int destination)
        {
            super(board, movedPiece, destination);
        }

        @Override
        public boolean equals(final Object other)
        {
            return this == other | other instanceof MajorMove && super.equals(other);
        }

        @Override
        public String toString()
        {
            return movedPiece.getPieceType().toString() + BoardUtils.getPositionAtCoordinate(this.destination);
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
        public boolean isAttack()
        {
            return true;
        }

        @Override
        public Piece getAttackedPiece()
        {
            return this.attackedPiece;
        }
    }

    public static class PawnMove extends Moves //When a pawn is moved
    {
        public PawnMove(final Board board, final Piece movedPiece, final int destination)
        {
            super(board, movedPiece, destination);
        }

        @Override
        public boolean equals(final Object other)
        {
            return this == other || other instanceof PawnMove && super.equals(other);
        }

        @Override
        public String toString()
        {
            return BoardUtils.getPositionAtCoordinate(this.destination);
        }
    }

    public static class PawnAttackMove extends AttackMove //When a pawn makes an attack
    {
        public PawnAttackMove(final Board board, final Piece movedPiece, final int destination, final Piece attackedPiece)
        {
            super(board, movedPiece, destination, attackedPiece);
        }

        @Override
        public boolean equals(final Object other)
        {
            return this == other || other instanceof PawnAttackMove && super.equals(other);
        }

        @Override
        public String toString()
        {
            return BoardUtils.getPositionAtCoordinate(this.movedPiece.getPiecePosition()).substring(0, 1) + "x" + BoardUtils.getPositionAtCoordinate(this.destination);
        }
    }

    public static final class PawnEnPassantAttackMove extends PawnAttackMove //When a pawn makes a special type of attack move, called en passant
    {
        public PawnEnPassantAttackMove(final Board board, final Piece movedPiece, final int destination, final Piece attackedPiece)
        {
            super(board, movedPiece, destination, attackedPiece);
        }

        @Override
        public boolean equals(final Object other)
        {
            return this == other || other instanceof PawnEnPassantAttackMove && super.equals(other);
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
                if (!piece.equals(this.getAttackedPiece()))
                {
                    builder.setPiece(piece);
                }
            }

            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getColor());

            return builder.build();
        }
    }

    public static class PawnPromotion extends PawnMove //When a pawn reaches the other end of the board, and is promoted (always to a queen in this program)
    {
        final Moves decoratedMove;
        final Pawn promotedPawn;
        final Piece promotionPiece;

        public PawnPromotion(final Moves decoratedMove, final Piece promotionPiece)
        {
            super(decoratedMove.getBoard(), decoratedMove.getMovedPiece(), decoratedMove.getDestination());
            this.decoratedMove = decoratedMove;
            this.promotedPawn = (Pawn) decoratedMove.getMovedPiece();
            this.promotionPiece = promotionPiece;
        }

        @Override
        public int hashCode()
        {
            return decoratedMove.hashCode() + (31 * promotedPawn.hashCode());
        }

        @Override
        public boolean equals(final Object other)
        {
            return this == other || other instanceof PawnPromotion && super.equals(other);
        }

        @Override
        public Board execute()
        {
            final Board pawnMovedBoard = this.decoratedMove.execute();
            final Board.Builder builder = new Board.Builder();

            for (final Piece piece : pawnMovedBoard.currentPlayer().getActivePieces())
            {
                if (!this.promotedPawn.equals(piece))
                {
                    builder.setPiece(piece);
                }
            }

            for (final Piece piece : pawnMovedBoard.currentPlayer().getOpponent().getActivePieces())
            {
                builder.setPiece(piece);
            }
            builder.setPiece(this.promotedPawn.getPromotionPiece().movePiece(this));
            builder.setPiece(this.promotionPiece.movePiece(this));
            builder.setMoveMaker(pawnMovedBoard.currentPlayer().getColor());

            return builder.build();
        }

        @Override
        public boolean isAttack()
        {
            return this.decoratedMove.isAttack();
        }

        @Override
        public Piece getAttackedPiece()
        {
            return this.decoratedMove.getAttackedPiece();
        }

        @Override
        public String toString()
        {
            return "";
        }
    }

    public static final class PawnJump extends Moves //When a pawn jumps forward twice in its forst move
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

        @Override
        public String toString()
        {
            return BoardUtils.getPositionAtCoordinate(this.destination);
        }
    }

    abstract static class CastleMove extends Moves //When a king and rook castle
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

        @Override
        public int hashCode()
        {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + this.castleRook.hashCode();
            result = prime * result + this.castleRookDestination;
            return result;
        }

        @Override
        public boolean equals(final Object other)
        {
            if (this == other)
            {
                return true;
            }

            if (!(other instanceof CastleMove))
            {
                return false;
            }

            final CastleMove otherCastleMove = (CastleMove) other;

            return super.equals(otherCastleMove) && this.castleRook.equals(otherCastleMove.getCastleRook());
        }
    }

    public static final class KingSideCastleMove extends CastleMove //When the rook on the right side of the board castles with the king
    {
        public KingSideCastleMove(final Board board, final Piece movedPiece, final int destination, Rook castleRook, int castleRookStart, int castleRookDestination)
        {
            super(board, movedPiece, destination, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object other)
        {
            return this == other || other instanceof KingSideCastleMove && super.equals(other);
        }

        @Override
        public String toString()
        {
            return "0-0";
        }
    }

    public static final class QueenSideCastleMove extends CastleMove //When the rook on the left side of the board castles with the king
    {
        public QueenSideCastleMove(final Board board, final Piece movedPiece, final int destination, Rook castleRook, int castleRookStart, int castleRookDestination)
        {
            super(board, movedPiece, destination, castleRook, castleRookStart, castleRookDestination);
        }

        @Override
        public boolean equals(final Object other)
        {
            return this == other || other instanceof QueenSideCastleMove && super.equals(other);
        }

        @Override
        public String toString()
        {
            return "0-0-0";
        }
    }

    public static final class NullMove extends Moves //A nothing move
    {
        public NullMove()
        {
            super(null, -1);
        }

        @Override
        public Board execute()
        {
            throw new RuntimeException("How did you do this??");
        }

        @Override
        public int getCurrentCoordinate()
        {
            return -1;
        }
    }

    public static class MoveFactory //Factory design pattern - Returns a move based on the situation
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
