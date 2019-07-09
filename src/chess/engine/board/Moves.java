package chess.engine.board;

import chess.engine.pieces.Piece;

public abstract class Moves
{
    final Board board;
    final Piece movedPiece;
    final int destination;

    private Moves(final Board board, final Piece movedPiece, final int destination)
    {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destination = destination;
    }

    public static final class MajorMove extends Moves //A Major move is a move where no opposing piece is claimed by the user
    {
        public MajorMove(final Board board, final Piece movedPiece, final int destination)
        {
            super(board, movedPiece, destination);
        }
    }

    public static final class AttackMove extends Moves //An Attack move is one which claims the other players' piece
    {
        final Piece attackedPiece;

        public AttackMove(final Board board, final Piece movedPiece, final int destination, final Piece attackedPiece)
        {
            super(board, movedPiece, destination);
            this.attackedPiece = attackedPiece;
        }
    }
}
