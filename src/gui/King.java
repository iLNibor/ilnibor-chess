package gui;
import java.util.LinkedList;
import java.util.Arrays;

public class King extends ChessPiece {
	
	public King(boolean white, int row, int column) {
		super(white, "king", row, column);
	}

	public LinkedList<int[]> emptyBoardMoves() {

		// TODO: castling

		int[] above = {row - 1, col};
		int[] below = {row + 1, col};
		int[] left = {row, col - 1};
		int[] right = {row, col + 1};
		int[] uLeft = {row - 1, col - 1};
		int[] uRight = {row - 1, col + 1};
		int[] dLeft = {row + 1, col - 1};
		int[] dRight = {row + 1, col + 1};

		return new LinkedList<int[]>(Arrays.asList(above, below, left, right, uLeft, uRight, dLeft, dRight));
	}
}