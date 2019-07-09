package chess.engine;

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
        public boolean isWhite()
        {
            return true;
        }

        @Override
        public boolean isBlack()
        {
            return false;
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
        public boolean isWhite()
        {
            return false;
        }

        @Override
        public boolean isBlack()
        {
            return true;
        }
    };

    public abstract int getColor();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
}
