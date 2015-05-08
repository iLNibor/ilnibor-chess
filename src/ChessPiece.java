import java.util.LinkedList;

/**
 * An abstract class that represents a chess piece.
 * All chess pieces have certain shared behaviors.
 * @author Akhil Velagapudi, Siddarth Senthilkumar
 * @version 2.0
 */
public abstract class ChessPiece {

    protected boolean white;
    protected String name;
    protected int[] rowColPosition;

    public ChessPiece(boolean white, String name, int row, int column) {
        this.white = white;
        this.name = name;
        this.rowColPosition = new int[2];
        rowColPosition[0] = row;
        rowColPosition[1] = column;
    }

    public abstract LinkedList<int[]> moveDirections();

    public boolean isWhite() {
        return white;
    }

    public String name() {
        return name;
    }

    public String position() {
        return "(" + rowColPosition[0] + ", " + rowColPosition[1] + ")";
    }

    public int[] rowColPosition() {
        return rowColPosition;
    }

    public String toString() {
        String returnString = "";
        if (white) {
            returnString += "White ";
        } else {
            returnString += "Black ";
        }

        returnString += name + " at " + position();
        return returnString;
    }
}