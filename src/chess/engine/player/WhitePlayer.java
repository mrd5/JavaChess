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

//All methods and values associated with the white player

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

    @Override
    public String toString()
    {
        return "White";
    }

    @Override
    protected Collection<Moves> calculateKingCastles(Collection<Moves> currentsMoves, Collection<Moves> opponentsMoves) //Returns all of the possible castle moves that can be made
    {
        final List<Moves> kingCastles = new ArrayList<>();

        if (this.king.isFirstMove() && !this.isInCheck()) //Preconditions that must be met to castle
        {
            if (!this.board.getTile(61).isFull() &&
                !this.board.getTile(62).isFull()) //Spaces between king-side rook and king are empty
            {
                final Tile rookTile = this.board.getTile(63); //A rook is on the last tile

                if (rookTile.isFull() && rookTile.getPiece().isFirstMove()) //If the rook hasn't yet been moved
                {
                    if (Player.calculateAttacksOnTile(61, opponentsMoves).isEmpty() &&
                        Player.calculateAttacksOnTile(62, opponentsMoves).isEmpty() &&
                        rookTile.getPiece().getPieceType().isRook()) //If no black pieces can attack the tiles inbetween the king and rook (on the king side)
                    {
                        kingCastles.add(new Moves.KingSideCastleMove(this.board, this.king, 62, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                    }
                }
            }

            if (!this.board.getTile(59).isFull() &&
                !this.board.getTile(58).isFull() &&
                !this.board.getTile(57).isFull()) //queen-side rook and inbetween tiles are empty
            {
                final Tile rookTile = this.board.getTile(56);

                if (rookTile.isFull() &&
                    rookTile.getPiece().isFirstMove() &&
                    Player.calculateAttacksOnTile(58, opponentsMoves).isEmpty() &&
                    Player.calculateAttacksOnTile(59, opponentsMoves).isEmpty() &&
                    rookTile.getPiece().getPieceType().isRook()) //If no pieces can attack the tiles inbetween the king and rook (on the queen side)
                {
                    kingCastles.add(new Moves.QueenSideCastleMove(this.board, this.king, 58, (Rook) rookTile.getPiece(), rookTile.getTileCoordinate(), 59));
                }
            }
        }

        return ImmutableList.copyOf(kingCastles);
    }
}
