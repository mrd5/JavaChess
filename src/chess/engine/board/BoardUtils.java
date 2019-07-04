package chess.engine.board;

public class BoatUtils
{
    public static boolean isValidCoordinate(int position)
    {
        return (position >= 0 && position < 64);
    }
}
