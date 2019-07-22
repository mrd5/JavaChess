package chess.engine.player.ai;

import chess.engine.board.Board;
import chess.engine.pieces.Piece;
import chess.engine.player.Player;

//Returns points that are used by the ai to determine the best move to make

public class StandardBoardEvaluator implements BoardEvaluator
{
    private static final int CHECK_BONUS = 50;
    private static final int CHECK_MATE_BONUS = 10000;
    private static final int DEPTH_BONUS = 100;
    private static final int CASTLE_BONUS = 60;

    @Override
    public int evaluate(final Board board, final int depth) //Calculates the difference in points
    {
        return scorePlayer(board, board.whitePlayer(), depth) - scorePlayer(board, board.blackPlayer(), depth);
    }

    private int scorePlayer(final Board board, final Player player, final int depth) //Returns the amount of points of a single player
    {
        return pieceValue(player) +
               mobility(player) +
               check(player) +
               checkmate(player, depth) +
               castled(player);
    }

    private int castled(Player player) //60 points if castled
    {
        return player.isCastled() ? CASTLE_BONUS : 0;
    }

    private static int check(final Player player) //50 points if in check
    {
        return player.getOpponent().isInCheck() ? CHECK_BONUS : 0;
    }

    private static int mobility(final Player player) //Points equivalent to how many moves can be made
    {
        return player.getLegalMoves().size();
    }

    private static int checkmate(final Player player, int depth) //If checkmate you win!
    {
        return player.getOpponent().isInCheckMate() ? CHECK_MATE_BONUS * depthBonus(depth) : 0;
    }

    private static int depthBonus(int depth) //How far down the AI will search
    {
        return depth == 0 ? 1 : DEPTH_BONUS * depth;
    }

    private static int pieceValue(final Player player) //Each type of piece has a different number of points associated with it
    {
        int pieceValueScore = 0;

        for (final Piece piece : player.getActivePieces())
        {
            pieceValueScore += piece.getPieceValue();
        }

        return pieceValueScore;
    }
}
