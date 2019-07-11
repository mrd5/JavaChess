package chess.engine.player;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;
import chess.engine.pieces.King;
import chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Player
{
    protected final Board board;
    protected final King king;
    protected final Collection<Moves> legalMoves;
    private final boolean isInCheck;

    Player(final Board board, final Collection<Moves> legalMoves, final Collection<Moves> opponentMoves)
    {
        this.board = board;
        this.king = establishKing();
        this.legalMoves = legalMoves;
        this.isInCheck = !Player.calculateAttacksOnTile(this.king.getPiecePosition(), opponentMoves).isEmpty();
    }

    private static Collection<Moves> calculateAttacksOnTile(int piecePosition, Collection<Moves> moves)
    {
        final List<Moves> attackMoves = new ArrayList<>();
        for (final Moves move : moves)
        {
            if (piecePosition == move.getDestination())
            {
                attackMoves.add(move);
            }
        }

        return ImmutableList.copyOf(attackMoves);
    }

    private King establishKing()
    {
        for (final Piece piece: getActivePieces())
        {
            if (piece.getPieceType().isKing())
            {
                return (King) piece;
            }
        }

        throw new RuntimeException("How did you get here!?");
    }

    public boolean isMoveLegal(final Moves move)
    {
        return this.legalMoves.contains(move);
    }

    public boolean isInCheck()
    {
        return this.isInCheck;
    }

    public King getPlayerKing()
    {
        return this.king;
    }

    public Collection<Moves> getLegalMoves()
    {
        return this.legalMoves;
    }

    public boolean isInCheckMate()
    {
        return this.isInCheck && !hasEscapeMoves();
    }

    protected boolean hasEscapeMoves()
    {
        for (final Moves move : this.legalMoves)
        {
            final MoveTransition transition = makeMove(move);
            if (transition.getMoveStatus().isDone())
            {
                return true;
            }
        }

        return false;
    }

    public boolean isInStalemate()
    {
        return !this.isInCheck && !hasEscapeMoves();
    }

    public boolean isCastled()
    {
        return false;
    }

    public MoveTransition makeMove(final Moves move)
    {
        if (!isMoveLegal(move))
        {
            return new MoveTransition(this.board,  move, MoveStatus.ILLEGAL_MOVE);
        }

        final Board transitionBoard = move.execute();

        final Collection<Moves> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.currentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty())
        {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK);
        }

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE);

    }


    public abstract Collection<Piece> getActivePieces();
    public abstract Color getColor();
    public abstract Player getOpponent();
}
