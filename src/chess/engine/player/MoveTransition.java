package chess.engine.player;

import chess.engine.board.Board;
import chess.engine.board.Moves;

//During the move

public class MoveTransition
{
    private final Board transitionBoard;
    private final Board toBoard;
    private final Moves move;
    private final MoveStatus moveStatus;

    public MoveTransition(final Board transitionBoard, final Moves move, final MoveStatus moveStatus, final Board toBoard)
    {
        this.transitionBoard = transitionBoard;
        this.move = move;
        this.moveStatus = moveStatus;
        this.toBoard = toBoard;
    }

    public MoveStatus getMoveStatus()
    {
        return this.moveStatus;
    }

    public Board getToBoard()
    {
        return this.toBoard;
    } //Post-move board

    public Board getTransitionBoard()
    {
        return this.transitionBoard;
    } //Pre-move board

}
