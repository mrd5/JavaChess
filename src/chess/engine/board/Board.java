package chess.engine.board;

import chess.engine.Color;
import chess.engine.pieces.*;
import chess.engine.player.BlackPlayer;
import chess.engine.player.Player;
import chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Board
{
    private final List<Tile> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;

    private final WhitePlayer white;
    private final BlackPlayer black;

    private final Player current;

    private final Pawn enPassantPawn;

    private Board(final Builder builder)
    {
        this.gameBoard = createGameBoard(builder);
        this.whitePieces = calculateActivePieces(this.gameBoard, Color.WHITE);
        this.blackPieces = calculateActivePieces(this.gameBoard, Color.BLACK);

        final Collection<Moves> whiteStandardLegalMoves = getLegalMoves(this.whitePieces);
        final Collection<Moves> blackStandardLegalMoves = getLegalMoves(this.blackPieces);

        this.white = new WhitePlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.black = new BlackPlayer(this, whiteStandardLegalMoves, blackStandardLegalMoves);
        this.current = builder.nextMoveMaker.choosePlayer(this.white, this.black);
        this.enPassantPawn = builder.enPassantPawn;
    }

    @Override
    public String toString()
    {
        //Used for early testing, displays the board in the format below
        //  r  n  b  q  k  b  n  r
        //  p  p  p  p  p  p  p  p
        //  -  -  -  -  -  -  -  -
        //  -  -  -  -  -  -  -  -
        //  -  -  -  -  -  -  -  -
        //  -  -  -  -  -  -  -  -
        //  P  P  P  P  P  P  P  P
        //  R  N  B  Q  K  B  N  R
        final StringBuilder builder = new StringBuilder();

        for (int i = 0; i < BoardUtils.NUM_TILES; i++)
        {
            final String tileText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s", tileText));
            if ((i + 1) % BoardUtils.NUM_TILES_PER_ROW == 0)
            {
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    public Player whitePlayer()
    {
        return this.white;
    }

    public Player blackPlayer()
    {
        return this.black;
    }

    public Player currentPlayer()
    {
        return this.current;
    }

    public Iterable<Moves> getAllLegalMoves() //Gets all of the legal moves for all pieces
    {
        return Iterables.unmodifiableIterable(Iterables.concat(this.white.getLegalMoves(), this.black.getLegalMoves()));
    }

    private Collection<Moves> getLegalMoves(final Collection<Piece> pieces) //Gets the legal moves for some set of pieces
    {
        final List<Moves> legalMoves = new ArrayList<>();

        for (final Piece piece : pieces)
        {
            legalMoves.addAll(piece.getLegalMoves(this));
        }

        return ImmutableList.copyOf(legalMoves);
    }

    public Collection<Piece> getBlackPieces()
    {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces()
    {
        return this.whitePieces;
    }

    public Collection<Piece> getAllPieces()
    {
        return Stream.concat(this.whitePieces.stream(), this.blackPieces.stream()).collect(Collectors.toList());
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Color color) //Returns which pieces are currently on the board
    {
        final List<Piece> activePieces = new ArrayList<>();

        for (final Tile tile : gameBoard)
        {
            if (tile.isFull())
            {
                final Piece piece = tile.getPiece();
                if (piece.getPieceColor() == color)
                {
                    activePieces.add(piece);
                }
            }
        }

        return ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(final int position)
    {
        return gameBoard.get(position);
    }

    private static List<Tile> createGameBoard(final Builder builder) //Creates the initial game board with 64 tiles
    {
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];

        for (int i = 0; i < BoardUtils.NUM_TILES; i++)
        {
            tiles[i] = Tile.create(i, builder.boardConfig.get(i));
        }

        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard() //Builds the board by placing the pieces into their initial positions
    {
        final Builder builder = new Builder();

        //BLACK SIDE
        builder.setPiece(new Rook(0, Color.BLACK));
        builder.setPiece(new Knight(1, Color.BLACK));
        builder.setPiece(new Bishop(2, Color.BLACK));
        builder.setPiece(new Queen(3,Color.BLACK ));
        builder.setPiece(new King(4,Color.BLACK ));
        builder.setPiece(new Bishop(5, Color.BLACK));
        builder.setPiece(new Knight(6, Color.BLACK));
        builder.setPiece(new Rook(7, Color.BLACK));
        builder.setPiece(new Pawn(8, Color.BLACK));
        builder.setPiece(new Pawn(9, Color.BLACK));
        builder.setPiece(new Pawn(10, Color.BLACK));
        builder.setPiece(new Pawn(11, Color.BLACK));
        builder.setPiece(new Pawn(12, Color.BLACK));
        builder.setPiece(new Pawn(13, Color.BLACK));
        builder.setPiece(new Pawn(14, Color.BLACK));
        builder.setPiece(new Pawn(15, Color.BLACK));

        //WHITE SIDE
        builder.setPiece(new Pawn(48, Color.WHITE));
        builder.setPiece(new Pawn(49, Color.WHITE));
        builder.setPiece(new Pawn(50, Color.WHITE));
        builder.setPiece(new Pawn(51, Color.WHITE));
        builder.setPiece(new Pawn(52, Color.WHITE));
        builder.setPiece(new Pawn(53, Color.WHITE));
        builder.setPiece(new Pawn(54, Color.WHITE));
        builder.setPiece(new Pawn(55, Color.WHITE));
        builder.setPiece(new Rook(56, Color.WHITE));
        builder.setPiece(new Knight(57, Color.WHITE));
        builder.setPiece(new Bishop(58, Color.WHITE));
        builder.setPiece(new Queen(59, Color.WHITE));
        builder.setPiece(new King(60, Color.WHITE));
        builder.setPiece(new Bishop(61, Color.WHITE));
        builder.setPiece(new Knight(62, Color.WHITE));
        builder.setPiece(new Rook(63, Color.WHITE));

        //Set white to move first
        builder.setMoveMaker(Color.WHITE);

        return builder.build();
    }

    public Pawn getEnPassantPawn()
    {
        return this.enPassantPawn;
    }

    public static class Builder
    {
        Map<Integer, Piece> boardConfig;
        Color nextMoveMaker;
        Pawn enPassantPawn;

        public Builder()
        {
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece) //Places a piece on a tile
        {
            this.boardConfig.put(piece.getPiecePosition(), piece);

            return this;
        }

        public Builder setMoveMaker(final Color nextMoveMaker) //Sets which color player moves next
        {
            this.nextMoveMaker = nextMoveMaker;

            return this;
        }

        public Board build()
        {
            return new Board(this);
        }

        public void setEnPassantPawn(Pawn enPassantPawn)
        {
            this.enPassantPawn = enPassantPawn;
        }
    }
}
