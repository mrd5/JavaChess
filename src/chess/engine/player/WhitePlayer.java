package chess.engine.player;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;
import chess.engine.pieces.Piece;

import java.util.Collection;

public class WhitePlayer extends Player
{
    public WhitePlayer(Board board, Collection<Moves> whiteStandardLegalMoves, Collection<Moves> blackStandardLegalMoves)
    {
        super(board, whiteStandardLegalMoves, blackStandardLegalMoves);
    }

    @Override
    public Collection<Piece> getActivePieces()
    {
        return this.board.getWhitePieces();
    }

    @Override
    public Color getColor()
    {
        return Color.WHITE;
    }

    @Override
    public Player getOpponent()
    {
        return this.board.blackPlayer();
    }
}
