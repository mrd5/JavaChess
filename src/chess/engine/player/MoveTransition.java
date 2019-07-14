package chess.engine.player;

import chess.engine.board.Board;
import chess.engine.board.Moves;

public class MoveTransition
{
    private final Board transitionBoard;
    private final Moves move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board transitionBoard, final Moves move, final MoveStatus moveStatus)
    {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
    }

    public MoveStatus getMoveStatus()
    {
        return this.moveStatus;
    }

    public Board getTransitionBoard()
    {
        return this.transitionBoard;
    }

}
