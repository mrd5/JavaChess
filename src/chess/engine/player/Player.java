package chess.engine.player;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;
import chess.engine.pieces.King;
import chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//Base player class

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
        this.legalMoves = ImmutableList.copyOf(Iterables.concat(legalMoves, calculateKingCastles(legalMoves, opponentMoves))); //All of the possible moves the player can make
        this.isInCheck = !Player.calculateAttacksOnTile(this.king.getPiecePosition(), opponentMoves).isEmpty();
    }

    protected static Collection<Moves> calculateAttacksOnTile(int piecePosition, Collection<Moves> moves) //Returns all of the attacks that can be made on a specific tile by all pieces
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

    private King establishKing() //Returns the king piece
    {
        for (final Piece piece: getActivePieces())
        {
            if (piece.getPieceType().isKing())
            {
                return (King) piece;
            }
        }

        throw new RuntimeException("How did you get here!?"); //Both players must initially have a king!
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

    protected boolean hasEscapeMoves() //If a king is able to escape check
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
        if (!isMoveLegal(move)) //If an illegal move is attempted
        {
            return new MoveTransition(this.board,  move, MoveStatus.ILLEGAL_MOVE, this.board);
        }

        final Board transitionBoard = move.execute();

        final Collection<Moves> kingAttacks = Player.calculateAttacksOnTile(transitionBoard.currentPlayer().getOpponent().getPlayerKing().getPiecePosition(), transitionBoard.currentPlayer().getLegalMoves());

        if (!kingAttacks.isEmpty()) //If a player is put into check
        {
            return new MoveTransition(this.board, move, MoveStatus.LEAVES_PLAYER_IN_CHECK, this.board);
        }

        return new MoveTransition(transitionBoard, move, MoveStatus.DONE, transitionBoard);

    }


    public abstract Collection<Piece> getActivePieces();
    public abstract Color getColor();
    public abstract Player getOpponent();
    protected abstract Collection<Moves> calculateKingCastles(Collection<Moves> currentsMoves, Collection<Moves> opponentsMoves);
}
