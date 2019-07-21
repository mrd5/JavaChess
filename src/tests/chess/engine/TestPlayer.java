package tests.chess.engine;

import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Moves;
import chess.engine.player.MoveTransition;
import chess.engine.player.ai.StandardBoardEvaluator;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

public class TestPlayer
{
    @Test
    public void testSimpleEvaluation() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer().makeMove(Moves.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                BoardUtils.getCoordinateAtPosition("e4")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getToBoard()
                .currentPlayer()
                .makeMove(Moves.MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("e7"),
                        BoardUtils.getCoordinateAtPosition("e5")));
        assertTrue(t2.getMoveStatus().isDone());
    }

    @Test
    public void testBug() {
        final Board board = Board.createStandardBoard();
        final MoveTransition t1 = board.currentPlayer().makeMove(Moves.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("c2"),
                        BoardUtils.getCoordinateAtPosition("c3")));
        assertTrue(t1.getMoveStatus().isDone());
        final MoveTransition t2 = t1.getToBoard().currentPlayer().makeMove(Moves.MoveFactory.createMove(t1.getToBoard(), BoardUtils.getCoordinateAtPosition("b8"),
                        BoardUtils.getCoordinateAtPosition("a6")));
        assertTrue(t2.getMoveStatus().isDone());
        final MoveTransition t3 = t2.getToBoard().currentPlayer().makeMove(Moves.MoveFactory.createMove(t2.getToBoard(), BoardUtils.getCoordinateAtPosition("d1"),
                        BoardUtils.getCoordinateAtPosition("a4")));
        assertTrue(t3.getMoveStatus().isDone());
        final MoveTransition t4 = t3.getToBoard().currentPlayer().makeMove(Moves.MoveFactory.createMove(t3.getToBoard(), BoardUtils.getCoordinateAtPosition("d7"),
                        BoardUtils.getCoordinateAtPosition("d6")));
        assertFalse(t4.getMoveStatus().isDone());
    }

    @Test
    public void testIllegalMove() {
        final Board board = Board.createStandardBoard();
        final Moves m1 = Moves.MoveFactory.createMove(board, BoardUtils.getCoordinateAtPosition("e2"),
                BoardUtils.getCoordinateAtPosition("e6"));
        final MoveTransition t1 = board.currentPlayer()
                .makeMove(m1);
        assertFalse(t1.getMoveStatus().isDone());
    }
}