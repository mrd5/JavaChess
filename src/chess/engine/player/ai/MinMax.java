package chess.engine.player.ai;

import chess.engine.board.Board;
import chess.engine.board.Moves;
import chess.engine.player.MoveTransition;

public class MinMax implements  MoveStrategy
{
    private final BoardEvaluator boardEvaluator;

    public MinMax()
    {
        this.boardEvaluator = new StandardBoardEvaluator();
    }

    @Override
    public String toString()
    {
        return "MinMax";
    }

    @Override
    public Moves execute(Board board, int depth)
    {
        final long startTime = System.currentTimeMillis();

        Moves bestMove = null;

        int highestSeenValue = Integer.MIN_VALUE;
        int lowestSeenValue = Integer.MAX_VALUE;
        int currentValue;

        System.out.println(board.currentPlayer() + " Thinking with depth = " + depth);

        int numMoves = board.currentPlayer().getLegalMoves().size();

        for (final Moves move : board.currentPlayer().getLegalMoves())
        {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if (moveTransition.getMoveStatus().isDone())
            {
                currentValue = board.currentPlayer().getColor().isWhite() ? min(moveTransition.getTransitionBoard(), depth - 1) : max(moveTransition.getTransitionBoard(), depth - 1);

                if (board.currentPlayer().getColor().isWhite() && currentValue >= highestSeenValue)
                {
                    highestSeenValue = currentValue;
                    bestMove = move;
                }
                else if (board.currentPlayer().getColor().isBlack() && currentValue <= lowestSeenValue)
                {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;

        return bestMove;
    }

    public int min(final Board board, final int depth)
    {
        if (depth == 0 || isEndGameScenario(board))
        {
            return this.boardEvaluator.evaluate(board, depth);
        }

        int lowestSeenValue = Integer.MAX_VALUE;

        for (final Moves move : board.currentPlayer().getLegalMoves())
        {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if (moveTransition.getMoveStatus().isDone())
            {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1);

                if (currentValue <= lowestSeenValue)
                {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(final Board board, final int depth)
    {
        if (depth == 0 || isEndGameScenario(board))
        {
            return this.boardEvaluator.evaluate(board, depth);
        }

        int highestSeenValue = Integer.MAX_VALUE;

        for (final Moves move : board.currentPlayer().getLegalMoves())
        {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if (moveTransition.getMoveStatus().isDone())
            {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1);

                if (currentValue >= highestSeenValue)
                {
                    highestSeenValue = currentValue;
                }
            }
        }

        return highestSeenValue;
    }

    private boolean isEndGameScenario(Board board)
    {
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStalemate();
    }


}
