package chess.engine;

import chess.engine.board.Board;


public class JavaChess
{
    public static void main(String[] args)
    {
        Board board = Board.createStandardBoard();

        System.out.println(board);
    }
}
