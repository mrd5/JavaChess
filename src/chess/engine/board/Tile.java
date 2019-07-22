package chess.engine.board;

import chess.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

import static chess.engine.board.BoardUtils.NUM_TILES;

//The tiles make up the entire 8x8 chess board

public abstract class Tile
{
    protected final int position; //Tile position

    private static final Map<Integer, Empty> EMPTY_TILES = createEmptyTiles();

    private static Map<Integer, Empty> createEmptyTiles()
    {
        final Map<Integer, Empty> emptyMap = new HashMap<>();

        for (int i = 0; i < NUM_TILES; i++) //Chess board initially consists of 64 empty tiles
        {
            emptyMap.put(i, new Empty(i));
        }

        return ImmutableMap.copyOf(emptyMap);
    }

    public static Tile create(final int position, final Piece piece) //Creates a new Full tile when a piece moves to its position
    {
        return piece != null ? new Full(position, piece) : EMPTY_TILES.get(position);
    }

    private Tile(final int position)
    {
        this.position = position;
    }

    public abstract boolean isFull(); //Checks if a specific tile has a piece on it

    public abstract Piece getPiece(); //Returns the piece currently on the tile, if any

    public int getTileCoordinate()
    {
        return this.position;
    }

    public static final class Empty extends Tile //Empty tiles (tiles without a piece on them)
    {
        private Empty(int position)
        {
            super(position);
        }

        @Override
        public String toString()
        {
            return "-";
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
        private final Piece tilePiece;

        private Full(int position, Piece tilePiece)
        {
            super(position);
            this.tilePiece = tilePiece;
        }

        @Override
        public String toString()
        {
            return getPiece().getPieceColor().isBlack() ? getPiece().toString().toLowerCase() : getPiece().toString(); //white piece = upper case, black piece = lower case
        }

        @Override
        public boolean isFull() //Full tile is full
        {
            return true;
        }

        @Override
        public Piece getPiece() //Returns piece on the current tile
        {
            return this.tilePiece;
        }
    }
}