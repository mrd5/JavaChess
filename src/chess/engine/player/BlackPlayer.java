package chess.engine.player;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.Moves;
import chess.engine.board.Tile;
import chess.engine.pieces.Piece;
import chess.engine.pieces.Rook;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

    @Override
    protected Collection<Moves> calculateKingCastles(Collection<Moves> currentsMoves, Collection<Moves> opponentsMoves)
    {
        final List<Moves> kingCastles = new ArrayList<>();

        if (this.king.isFirstMove() && !this.isInCheck())
        {
            if (!this.board.getTile(5).isFull() && !this.board.getTile(6).isFull()) //Spaces between king-side rook and king are empty
            {
                final Tile rookTile = this.board.getTile(7);

                if (rookTile.isFull() && rookTile.getPiece().isFirstMove())
                {
                    if (Player.calculateAttacksOnTile(5, opponentsMoves).isEmpty() &&
                            Player.calculateAttacksOnTile(6, opponentsMoves).isEmpty() &&
                            rookTile.getPiece().getPieceType().isRook())
                    {
                        kingCastles.add(new Moves.KingSideCastleMove(this.board, this.king, 6, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 5));
                    }
                }
            }

            if (!this.board.getTile(1).isFull() && !this.board.getTile(2).isFull() && !this.board.getTile(3).isFull()) //queen-side rook and inbetween tiles are empty
            {
                final Tile rookTile = this.board.getTile(0);

                if (rookTile.isFull() && rookTile.getPiece().isFirstMove())
                {
                    kingCastles.add(new Moves.QueenSideCastleMove(this.board, this.king, 2, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 3));
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
