package chess.engine.board;


//The tiles make up the entire 8x8 chess board
public abstract class Tile
{
    int position; //Tile position

    Tile(int position)
    {
        this.position = position;
    }

    public abstract boolean isFull(); //Checks if a specific tile has a piece on it

    public abstract Piece getPiece();

    public static final class Empty extends Tile //Empty tiles (tiles without a piece on them)
    {
        Empty(int position)
        {
            super(position);
        }

        @Override
        public boolean isFull() //Empty tile is never full
        {
            return false;
        }

        @Override
        public Piece getPiece() //Empty tile has no piece on it
        {
            return null;
        }
    }

    public static final class Full extends Tile //Full tiles (tiles with a piece on them)
    {
        Piece tilePiece;

        Full(int position, Piece tilePiece)
        {
            super(position);
            this.tilePiece = tilePiece;
        }

        @Override
        public boolean isFull() //Full tile is full
        {
            return true;
        }

        @Override
        public Piece getPiece() //Returns piece on the current tile
        {
            return tilePiece;
        }
    }
}