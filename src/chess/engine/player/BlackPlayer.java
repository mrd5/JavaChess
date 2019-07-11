package chess.engine.player;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;
import chess.engine.pieces.Piece;

import java.util.Collection;

public class BlackPlayer extends Player
{
    public BlackPlayer(Board board, Collection<Moves> whiteStandardLegalMoves, Collection<Moves> blackStandardLegalMoves)
    {
        super(board, blackStandardLegalMoves, whiteStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces()
    {
        return this.board.getBlackPieces();
    }

    @Override
    public Color getColor()
    {
        return Color.BLACK;
    }

    @Override
    public Player getOpponent()
    {
        return this.board.whitePlayer();
    }
}
