package chess.engine;

import chess.engine.board.BoardUtils;
import chess.engine.player.BlackPlayer;
import chess.engine.player.Player;
import chess.engine.player.WhitePlayer;

//Chess pieces will either be BLACK or WHITE

public enum Color
{
    WHITE
    {
        @Override
        public int getColor()
        {
            return -1;
        }

        @Override
        public int getOppositeColor()
        {
            return 1;
        }

        @Override
        public boolean isWhite()
        {
            return true;
        }

        @Override
        public boolean isBlack()
        {
            return false;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) //If a white pawn is on the eighth row
        {
            return BoardUtils.EIGHTH_ROW[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer white, final BlackPlayer black)
        {
            return white;
        }
    },

    BLACK
    {
        @Override
        public int getColor()
        {
            return 1;
        }

        @Override
        public int getOppositeColor()
        {
            return -1;
        }

        @Override
        public boolean isWhite()
        {
            return false;
        }

        @Override
        public boolean isBlack()
        {
            return true;
        }

        @Override
        public boolean isPawnPromotionSquare(int position) //If a black pawn is on the first row
        {
            return BoardUtils.FIRST_ROW[position];
        }

        @Override
        public Player choosePlayer(final WhitePlayer white, final BlackPlayer black)
        {
            return black;
        }
    };

    public abstract int getColor();
    public abstract int getOppositeColor();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
    public abstract boolean isPawnPromotionSquare(int position);
    public abstract Player choosePlayer(WhitePlayer white, BlackPlayer black);
}
