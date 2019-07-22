package chess.engine.player.ai;

import chess.engine.board.Board;
import chess.engine.board.Moves;
import chess.engine.player.MoveTransition;

//This is the MinMax algorithm used by the AI to choose the next move

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

        for (final Moves move : board.currentPlayer().getLegalMoves()) //Search through the current player's moves
        {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if (moveTransition.getMoveStatus().isDone())
            {
                currentValue = board.currentPlayer().getColor().isWhite() ? min(moveTransition.getTransitionBoard(), depth - 1)
                                                                          : max(moveTransition.getTransitionBoard(), depth - 1);

                if (board.currentPlayer().getColor().isWhite() && currentValue >= highestSeenValue) //Found a new highest seen value move
                {
                    highestSeenValue = currentValue;
                    bestMove = move;
                }
                else if (board.currentPlayer().getColor().isBlack() && currentValue <= lowestSeenValue) //Found a new lowest seen value move
                {
                    lowestSeenValue = currentValue;
                    bestMove = move;
                }
            }
        }

        final long executionTime = System.currentTimeMillis() - startTime;

        return bestMove;
    }

    public int min(final Board board, final int depth) //min finds the 'worst' move
    {
        if (depth == 0 || isEndGameScenario(board)) //Done searching
        {
            return this.boardEvaluator.evaluate(board, depth);
        }

        int lowestSeenValue = Integer.MAX_VALUE;

        for (final Moves move : board.currentPlayer().getLegalMoves())
        {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if (moveTransition.getMoveStatus().isDone())
            {
                final int currentValue = max(moveTransition.getTransitionBoard(), depth - 1); //Call to max

                if (currentValue <= lowestSeenValue)
                {
                    lowestSeenValue = currentValue;
                }
            }
        }
        return lowestSeenValue;
    }

    public int max(final Board board, final int depth) //max finds the 'best' move
    {
        if (depth == 0 || isEndGameScenario(board)) //Done searching
        {
            return this.boardEvaluator.evaluate(board, depth);
        }

        int highestSeenValue = Integer.MAX_VALUE;

        for (final Moves move : board.currentPlayer().getLegalMoves())
        {
            final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

            if (moveTransition.getMoveStatus().isDone())
            {
                final int currentValue = min(moveTransition.getTransitionBoard(), depth - 1); //Call to min

                if (currentValue >= highestSeenValue)
                {
                    highestSeenValue = currentValue;
                }
            }
        }

        return highestSeenValue;
    }

    private boolean isEndGameScenario(Board board) //If game over by stalemate or checkmate
    {
        return board.currentPlayer().isInCheckMate() || board.currentPlayer().isInStalemate();
    }


}
