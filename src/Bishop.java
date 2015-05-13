import java.util.LinkedList;

public class Bishop extends ChessPiece {
	
	public Bishop(boolean white, int row, int column) {
		super(white, "bishop", row, column);
	}

	public LinkedList<int[]> emptyBoardMoves() {

		LinkedList<int[]> positions = new LinkedList<int[]>();

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