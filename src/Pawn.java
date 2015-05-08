import java.util.LinkedList;
import java.util.Arrays;

public class Pawn extends ChessPiece {
	
	public Pawn(boolean white, int row, int column) {
		super(white, "pawn", row, column);
	}

	public LinkedList<int[]> moveDirections() {
		// TODO: Does not include enpassant

		int row = rowColPosition[0];
		int col = rowColPosition[1];

		int[] above = {row - 1, col};
		int[] uLeft = {row - 1, col - 1};
		int[] uRight = {row - 1, col + 1};

		return new LinkedList<int[]>(Arrays.asList(above, uLeft, uRight));
	}
}