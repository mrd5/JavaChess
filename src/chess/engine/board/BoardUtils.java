package chess.engine.board;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public class BoardUtils
{
    //Board Columns
    public static final boolean[] FIRST_COLUMN = initColumn(0);
    public static final boolean[] SECOND_COLUMN = initColumn(1);
    public static final boolean[] SEVENTH_COLUMN = initColumn(6);
    public static final boolean[] EIGHTH_COLUMN = initColumn(7);

    //Board Rows
    public static final boolean[] EIGHTH_ROW = initRow(0);
    public static final boolean[] SEVENTH_ROW = initRow(8);
    public static final boolean[] SIXTH_ROW = initRow(16);
    public static final boolean[] FIFTH_ROW = initRow(24);
    public static final boolean[] FOURTH_ROW = initRow(32);
    public static final boolean[] THIRD_ROW = initRow(40);
    public static final boolean[] SECOND_ROW = initRow(48);
    public static final boolean[] FIRST_ROW = initRow(56);

    public static final String[] ALGEBREIC_NOTATION = initializeAlgebreicNotation();
    public static final Map<String, Integer> POSITION_TO_COORDINATE = initializePositionToCoordinateMap();


    public static final int NUM_TILES = 64;
    public static final int NUM_TILES_PER_ROW = 8;

    private BoardUtils()
    {
        throw new RuntimeException("How????");
    }

    public static boolean isValidCoordinate(int position)
    {
        return (position >= 0 && position < NUM_TILES); //Valid tiles range from 0 to 63
    }

    private static boolean[] initColumn(int colNum) //Initializes the column so that it is an array containing the indices of all of its tiles
    {
        final boolean[] column = new boolean[NUM_TILES];

        do
        {
            column[colNum]= true;
            colNum += NUM_TILES_PER_ROW;
        } while(colNum < NUM_TILES);

        return column;
    }

    private static boolean[] initRow(int rowNum) //Initializes the row to contain the indices of all of its tiles
    {
        final boolean[] row = new boolean[NUM_TILES];
        do
        {
            row[rowNum] = true;
            rowNum++;
        } while(rowNum % NUM_TILES_PER_ROW != 0);
        return row;
    }

    private static String[] initializeAlgebreicNotation() //Standard chess board algebraic notation
    {
        return new String[]
        {
                "a8", "b8", "c8", "d8", "e8", "f8", "g8", "h8",
                "a7", "b7", "c7", "d7", "e7", "f7", "g7", "h7",
                "a6", "b6", "c6", "d6", "e6", "f6", "g6", "h6",
                "a5", "b5", "c5", "d5", "e5", "f5", "g5", "h5",
                "a4", "b4", "c4", "d4", "e4", "f4", "g4", "h4",
                "a3", "b3", "c3", "d3", "e3", "f3", "g3", "h3",
                "a2", "b2", "c2", "d2", "e2", "f2", "g2", "h2",
                "a1", "b1", "c1", "d1", "e1", "f1", "g1", "h1"
        };
    }

    private static Map<String,Integer> initializePositionToCoordinateMap() //Associates a tile with its corresponding algebraic notation entry
    {
        //For example: Tile 0 will become associated with a8
        final Map<String, Integer> positionToCoordinate = new HashMap<>();

        for (int i = 0; i < NUM_TILES; i++)
        {
            positionToCoordinate.put(ALGEBREIC_NOTATION[i], i);
        }

        return ImmutableMap.copyOf(positionToCoordinate);
    }

    public static int getCoordinateAtPosition(String position)
    {
        return POSITION_TO_COORDINATE.get(position);
    }

    public static String getPositionAtCoordinate(int destination)
    {
        return ALGEBREIC_NOTATION[destination];
    }
}
