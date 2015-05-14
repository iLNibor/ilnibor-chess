package gui;
import java.util.LinkedList;
import java.util.Arrays;

public class Knight extends ChessPiece {
	
	public Knight(boolean white, int row, int column) {
		super(white, "knight", row, column);
	}

	public LinkedList<int[]> emptyBoardMoves() {

		// For visual, see:
		// http://www.mark-weeks.com/aboutcom/images/ble1321n.gif
		int[] uLeft = {row - 2, col - 1};
		int[] uRight = {row - 2, col + 1};
		int[] lUp = {row - 1, col - 2};
		int[] lDown = {row + 1, col - 2};
		int[] rUp = {row - 1, col + 2};
		int[] rDown = {row + 1, col + 2};
		int[] dLeft = {row + 2, col - 1};
		int[] dRight = {row + 2, col + 1};

		return new LinkedList<int[]>(Arrays.asList(uLeft, uRight, lUp, lDown, rUp, rDown, dLeft, dRight));
	}
}