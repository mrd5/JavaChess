package tests.chess.engine;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Moves;
import chess.engine.pieces.King;
import chess.engine.pieces.Knight;
import org.junit.Test;

import java.util.Collection;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class TestPiece
{
    @Test
    public void testLegalMoveAllAvailable() {

        final Board.Builder boardBuilder = new Board.Builder();
        // Black Layout
        boardBuilder.setPiece(new King(4, Color.BLACK, false));
        boardBuilder.setPiece(new Knight(28, Color.BLACK));
        // White Layout
        boardBuilder.setPiece(new Knight(36, Color.WHITE));
        boardBuilder.setPiece(new King(60, Color.WHITE, false));
        // Set the current player
        boardBuilder.setMoveMaker(Color.WHITE);
        final Board board = boardBuilder.build();
        final Collection<Moves> whiteLegals = board.whitePlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 13);
        final Moves wm1 = Moves.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d6"));
        final Moves wm2 = Moves.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("f6"));
        final Moves wm3 = Moves.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("c5"));
        final Moves wm4 = Moves.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("g5"));
        final Moves wm5 = Moves.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("c3"));
        final Moves wm6 = Moves.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("g3"));
        final Moves wm7 = Moves.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("d2"));
        final Moves wm8 = Moves.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e4"), BoardUtils.getCoordinateAtPosition("f2"));

        assertTrue(whiteLegals.contains(wm1));
        assertTrue(whiteLegals.contains(wm2));
        assertTrue(whiteLegals.contains(wm3));
        assertTrue(whiteLegals.contains(wm4));
        assertTrue(whiteLegals.contains(wm5));
        assertTrue(whiteLegals.contains(wm6));
        assertTrue(whiteLegals.contains(wm7));
        assertTrue(whiteLegals.contains(wm8));

        final Board.Builder boardBuilder2 = new Board.Builder();
        // Black Layout
        boardBuilder2.setPiece(new King(4, Color.BLACK, false));
        boardBuilder2.setPiece(new Knight(28, Color.BLACK));
        // White Layout
        boardBuilder2.setPiece(new Knight(36, Color.WHITE));
        boardBuilder2.setPiece(new King(60, Color.WHITE, false));
        // Set the current player
        boardBuilder2.setMoveMaker(Color.BLACK);
        final Board board2 = boardBuilder2.build();
        final Collection<Moves> blackLegals = board.blackPlayer().getLegalMoves();

        final Moves bm1 = Moves.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("d7"));
        final Moves bm2 = Moves.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("f7"));
        final Moves bm3 = Moves.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("c6"));
        final Moves bm4 = Moves.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("g6"));
        final Moves bm5 = Moves.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("c4"));
        final Moves bm6 = Moves.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("g4"));
        final Moves bm7 = Moves.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("d3"));
        final Moves bm8 = Moves.MoveFactory
                .createMove(board2, BoardUtils.getCoordinateAtPosition("e5"), BoardUtils.getCoordinateAtPosition("f3"));

        assertEquals(blackLegals.size(), 13);

        assertTrue(blackLegals.contains(bm1));
        assertTrue(blackLegals.contains(bm2));
        assertTrue(blackLegals.contains(bm3));
        assertTrue(blackLegals.contains(bm4));
        assertTrue(blackLegals.contains(bm5));
        assertTrue(blackLegals.contains(bm6));
        assertTrue(blackLegals.contains(bm7));
        assertTrue(blackLegals.contains(bm8));
    }
}
