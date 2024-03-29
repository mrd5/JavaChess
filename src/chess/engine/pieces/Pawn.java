package chess.engine.pieces;

import chess.engine.Color;
import chess.engine.board.Board;
import chess.engine.board.BoardUtils;
import chess.engine.board.Moves;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static chess.engine.board.BoardUtils.*;
import static chess.engine.board.Moves.*;


public class Pawn extends Piece
{
    private final static int[] PAWN_MOVES = {7, 8, 9, 16}; //16 = Jump two tiles, 9 = 1 tile, 7/8 = attacking moves

    public Pawn(final int position, final Color color)
    {
        super(PieceType.PAWN, position, color, true);
    }

    public Pawn(final int position, final Color color, boolean isFirstMove)
    {
        super(PieceType.PAWN, position, color, isFirstMove);
    }

    @Override
    public Collection<Moves> getLegalMoves(final Board board) //Returns all of the legal moves the pawn can make
    {
        final List<Moves> legalMoves = new ArrayList<>();

        for (final int currentOffset : PAWN_MOVES)
        {
            int possibleCoordinate = this.position + (this.getPieceColor().getColor() * currentOffset);

            if (!isValidCoordinate(possibleCoordinate))
            {
                continue;
            }

            if (currentOffset == 8 && !board.getTile(possibleCoordinate).isFull()) //Single move
            {
                if (this.color.isPawnPromotionSquare(possibleCoordinate))
                {
                    legalMoves.add(new PawnPromotion(new PawnMove(board, this, possibleCoordinate), this));
                }
                else
                {
                    legalMoves.add(new PawnMove(board, this, possibleCoordinate));
                }
            }
            else if (currentOffset == 16 && this.isFirstMove() && //Moves two tiles (at first turn)
                    ((BoardUtils.SECOND_ROW[this.position] &&
                      this.getPieceColor().isWhite()) ||
                      BoardUtils.SEVENTH_ROW[this.position] && this.getPieceColor().isBlack()))
            {
                final int behindPossiblePosition = this.position + (this.getPieceColor().getColor() * 8);
                if (!board.getTile(behindPossiblePosition).isFull() &&
                    !board.getTile(possibleCoordinate).isFull())
                {
                    legalMoves.add(new PawnJump(board, this, possibleCoordinate));
                }
            }
            else if (currentOffset == 7 && //If attacking (non-en passant)
                    !(BoardUtils.EIGHTH_COLUMN[this.position] && this.getPieceColor().isWhite() ||
                     BoardUtils.FIRST_COLUMN[this.position] && this.getPieceColor().isBlack()))
            {
                if (board.getTile(possibleCoordinate).isFull())
                {
                    final Piece targetPiece = board.getTile(possibleCoordinate).getPiece();
                    if (this.getPieceColor() != targetPiece.getPieceColor()) //ATTACKING different color
                    {
                        if (this.color.isPawnPromotionSquare(possibleCoordinate))
                        {
                           legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, possibleCoordinate, targetPiece), this));
                        }
                        else
                        {
                            legalMoves.add(new PawnAttackMove(board, this, possibleCoordinate, targetPiece));
                        }
                    }
                }
                else if (board.getEnPassantPawn() != null) //En passant attack
                {
                    if (board.getEnPassantPawn().getPiecePosition() == (this.position + (this.color.getOppositeColor())))
                    {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.color != pieceOnCandidate.getPieceColor())
                        {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this , possibleCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
            else if (currentOffset == 9 && //Attacking in other direction
                    !(BoardUtils.FIRST_COLUMN[this.position] && this.getPieceColor().isWhite() ||
                      BoardUtils.EIGHTH_COLUMN[this.position] && this.getPieceColor().isBlack()))
            {
                if (board.getTile(possibleCoordinate).isFull())
                {
                    final Piece targetPiece = board.getTile(possibleCoordinate).getPiece();
                    if (this.getPieceColor() != targetPiece.getPieceColor()) //ATTACKING different color
                    {
                        if (this.color.isPawnPromotionSquare(possibleCoordinate))
                        {
                            legalMoves.add(new PawnPromotion(new PawnAttackMove(board, this, possibleCoordinate, targetPiece), this));
                        }
                        else
                        {
                            legalMoves.add(new PawnAttackMove(board, this, possibleCoordinate, targetPiece));
                        }
                    }
                }
                else if (board.getEnPassantPawn() != null) //En passant attack
                {
                    if (board.getEnPassantPawn().getPiecePosition() == (this.position - (this.color.getOppositeColor())))
                    {
                        final Piece pieceOnCandidate = board.getEnPassantPawn();
                        if (this.color != pieceOnCandidate.getPieceColor())
                        {
                            legalMoves.add(new PawnEnPassantAttackMove(board, this , possibleCoordinate, pieceOnCandidate));
                        }
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public Pawn movePiece(Moves move)
    {
        return new Pawn(move.getDestination(), move.getMovedPiece().getPieceColor());
    }

    @Override
    public String toString()
    {
        return PieceType.PAWN.toString();
    }

    public Piece getPromotionPiece() //Pawns will always be promoted to a queen
    {
        return new Queen(this.position, this.color, false);
    }
}
