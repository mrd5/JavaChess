package chess.engine.player.ai;

import chess.engine.board.Board;

//Used by MinMax

public interface BoardEvaluator
{
    int evaluate(Board board, int depth);
}
