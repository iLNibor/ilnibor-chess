import java.util.LinkedList;

public class Queen extends ChessPiece {
	
	public Queen(boolean white, int row, int column) {
		super(white, "queen", row, column);
	}

	public LinkedList<int[]> emptyBoardMoves() {

		LinkedList<int[]> positions = new LinkedList<int[]>();

		// All positions in the current file and rank
		for (int i = 0; i < 8; i++) {
			int[] positionOne = {row, i};
			int[] positionTwo = {i, col};
			positions.add(positionOne);
			positions.add(positionTwo);
		}

		// Add in the southwest-northeast diagonal
		int positionRow = row;
		int positionCol = col;
		while (Engine.onBoard(positionRow + 1, positionCol - 1)) {
			positionRow += 1;
			positionCol -= 1;
		}

		while (Engine.onBoard(positionRow, positionCol)) {
			int[] position = new int[] {positionRow, positionCol};
			if (row != positionRow && col != positionCol) {
				positions.add(position);
			}
			positionRow -= 1;
			positionCol += 1;
		}

		// Add in the northwest-southeast diagonal
		positionRow = row;
		positionCol = col;

		while (Engine.onBoard(positionRow - 1, positionCol - 1)) {
			positionRow -= 1;
			positionCol -= 1;
		}

		while (Engine.onBoard(positionRow, positionCol)) {
			int[] position = new int[] {positionRow, positionCol};
			if (row != positionRow && col != positionCol) {
				positions.add(position);
			}
			positionRow += 1;
			positionCol += 1;
		}

		return positions;
	}
}