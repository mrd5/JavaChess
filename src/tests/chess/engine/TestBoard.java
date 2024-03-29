package tests.chess.engine;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Moves;
import chess.engine.pieces.*;
import chess.engine.player.MoveTransition;
import chess.engine.player.ai.StandardBoardEvaluator;
import com.google.common.collect.Iterables;
import org.junit.Test;

import static org.junit.Assert.*;

//Tests the Board class

public class TestBoard
{
    @Test
    public void initialBoard()
    {
        final Board board = Board.createStandardBoard();

        assertEquals(board.currentPlayer().getLegalMoves().size(), 20);
        assertEquals(board.currentPlayer().getOpponent().getLegalMoves().size(), 20);
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().isCastled());
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isCastled());
        assertTrue(board.whitePlayer().toString().equals("White"));
        assertTrue(board.blackPlayer().toString().equals("Black"));

        final Iterable<Piece> allPieces = board.getAllPieces();
        final Iterable<Moves> allMoves = Iterables.concat(board.whitePlayer().getLegalMoves(), board.blackPlayer().getLegalMoves());
        for(final Moves move : allMoves) {
            assertFalse(move.isAttack());
            assertFalse(move.isCastlingMove());
        }

        assertEquals(Iterables.size(allMoves), 40);
        assertEquals(Iterables.size(allPieces), 32);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidBoard()
    {
        final Board.Builder builder = new Board.Builder();
        builder.setPiece(new Rook(0, Color.BLACK));
        builder.setPiece(new Knight(1, Color.BLACK));
        builder.setPiece(new Bishop(2, Color.BLACK));
        builder.setPiece(new Queen(3, Color.BLACK));
        builder.setPiece(new Bishop(5, Color.BLACK));
        builder.setPiece(new Knight(6, Color.BLACK));
        builder.setPiece(new Rook(7, Color.BLACK));
        builder.setPiece(new Pawn(8, Color.BLACK));
        builder.setPiece(new Pawn(9, Color.BLACK));
        builder.setPiece(new Pawn(10, Color.BLACK));
        builder.setPiece(new Pawn(11, Color.BLACK));
        builder.setPiece(new Pawn(12, Color.BLACK));
        builder.setPiece(new Pawn(13, Color.BLACK));
        builder.setPiece(new Pawn(14, Color.BLACK));
        builder.setPiece(new Pawn(15, Color.BLACK));


        builder.setPiece(new Pawn(48, Color.WHITE));
        builder.setPiece(new Pawn(49, Color.WHITE));
        builder.setPiece(new Pawn(50, Color.WHITE));
        builder.setPiece(new Pawn(51, Color.WHITE));
        builder.setPiece(new Pawn(52, Color.WHITE));
        builder.setPiece(new Pawn(53, Color.WHITE));
        builder.setPiece(new Pawn(54, Color.WHITE));
        builder.setPiece(new Pawn(55, Color.WHITE));
        builder.setPiece(new Rook(56, Color.WHITE));
        builder.setPiece(new Knight(57, Color.WHITE));
        builder.setPiece(new Bishop(58, Color.WHITE));
        builder.setPiece(new Queen(59, Color.WHITE));
        builder.setPiece(new Bishop(61, Color.WHITE));
        builder.setPiece(new Knight(62, Color.WHITE));
        builder.setPiece(new Rook(63, Color.WHITE));

        builder.setMoveMaker(Color.WHITE);

        builder.build();
    }


    @Test
    public void testAlgebreicNotation()
    {
        assertEquals(BoardUtils.getPositionAtCoordinate(0), "a8");
        assertEquals(BoardUtils.getPositionAtCoordinate(1), "b8");
        assertEquals(BoardUtils.getPositionAtCoordinate(2), "c8");
        assertEquals(BoardUtils.getPositionAtCoordinate(3), "d8");
        assertEquals(BoardUtils.getPositionAtCoordinate(4), "e8");
        assertEquals(BoardUtils.getPositionAtCoordinate(5), "f8");
        assertEquals(BoardUtils.getPositionAtCoordinate(6), "g8");
        assertEquals(BoardUtils.getPositionAtCoordinate(63), "h1");
    }

    @Test
    public void mem()
    {
        final Runtime runtime = Runtime.getRuntime();
        long start, end;
        runtime.gc();
        start = runtime.freeMemory();
        Board board = Board.createStandardBoard();
        end = runtime.freeMemory();
        System.out.println("That took " + ( start - end ) + " bytes of memory!");
    }
}