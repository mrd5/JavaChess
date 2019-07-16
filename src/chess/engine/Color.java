package chess.engine;

import chess.engine.player.BlackPlayer;
import chess.engine.player.Player;
import chess.engine.player.WhitePlayer;

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
        public Player choosePlayer(final WhitePlayer white, final BlackPlayer black)
        {
            return black;
        }
    };

    public abstract int getColor();
    public abstract int getOppositeColor();
    public abstract boolean isWhite();
    public abstract boolean isBlack();

    public abstract Player choosePlayer(WhitePlayer white, BlackPlayer black);
}
