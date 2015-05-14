package gui;
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
    protected int row;
    protected int col;

    public ChessPiece(boolean white, String name, int row, int column) {
        this.white = white;
        this.name = name;
        this.row = row;
        this.col = column;
    }

    public abstract LinkedList<int[]> emptyBoardMoves();

    public boolean isWhite() {
        return white;
    }

    public String name() {
        return name;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return col;
    }

    public String position() {
        return "(" + row + ", " + col + ")";
    }

    public int[] rowColPosition() {
        return new int[] {row, col};
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