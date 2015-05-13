import java.util.LinkedList;

public class Rook extends ChessPiece {
	
	public Rook(boolean white, int row, int column) {
		super(white, "rook", row, column);
	}

	public LinkedList<int[]> emptyBoardMoves() {

		// Does not include castling.
		// Castling should be handled in King class
		LinkedList<int[]> positions = new LinkedList<int[]>();

		// All positions in the current file and rank
		for (int i = 0; i < 8; i++) {
			int[] positionOne = {row, i};
			int[] positionTwo = {i, col};
			positions.add(positionOne);
			positions.add(positionTwo);
		}

		return positions;
	}
}