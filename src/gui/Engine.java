package gui;
import java.util.LinkedList;

/**
 * This is the engine of the chess game.
 * It controls game flow and changing of turns,
 * keeps track of captured pieces, and will eventually
 * house the AI component of the chess game.
 * @author Siddarth Senthilkumar
 * @version 1.1
 */
public class Engine {

	ChessPiece[][] board;

	public Engine() {
		setupBoard();
	}

	private void setupBoard() {
		// Board assumes we are white player and opposite side is black player
		this.board = new ChessPiece[8][8];

		// Place pawns
		for (int i = 0; i < board.length; i++) {
			board[1][i] = new Pawn(true, 1, i);
			board[6][i] = new Pawn(false, 6, i);
		}

		// Place rooks
		board[0][0] = new Rook(true, 0, 0);
		board[0][7] = new Rook(true, 0, 7);
		board[7][0] = new Rook(false, 7, 0);
		board[7][7] = new Rook(false, 7, 7);

		// Place knights
		board[0][1] = new Knight(true, 0, 1);
		board[0][6] = new Knight(true, 0, 6);
		board[7][1] = new Knight(false, 7, 1);
		board[7][6] = new Knight(false, 7, 6);

		// Place bishops
		board[0][2] = new Bishop(true, 0, 2);
		board[0][5] = new Bishop(true, 0, 5);
		board[7][2] = new Bishop(false, 7, 2);
		board[7][5] = new Bishop(false, 7, 5);

		// Place queens
		board[0][3] = new Queen(true, 0, 3);
		board[7][3] = new Queen(false, 7, 3);

		// Place kings
		board[0][4] = new King(true, 0, 4);
		board[7][4] = new King(false, 7, 4);
	}

	// TODO: can't move something that is pinned
	public LinkedList<int[]> possibleMoves(ChessPiece piece) {
		LinkedList<int[]> moveLocations = new LinkedList<int[]>();

		int row = piece.rowColPosition()[0];
		int col = piece.rowColPosition()[1];

		// TODO: enpassant
		if (piece instanceof Pawn) {
			// Pawns can move forward when no one is there
			if (onBoard(row - 1, col)) {
				int[] validPosition = {row - 1, col};
				moveLocations.add(validPosition);
			}

			// Pawns can move one diagonally if opposite color piece is there
			if (onBoard(row - 1, col - 1)) {
				ChessPiece existing = board[row - 1][col - 1];
				if (existing != null) {
					if (existing.isWhite() != piece.isWhite()) {
						int[] validPosition = {row - 1, col - 1};
						moveLocations.add(validPosition);
					}
				}
			}

			if (onBoard(row - 1, col + 1)) {
				ChessPiece existing = board[row - 1][col + 1];
				if (existing != null) {
					if (existing.isWhite() != piece.isWhite()) {
						int[] validPosition = {row - 1, col + 1};
						moveLocations.add(validPosition);
					}
				}
			}
		} else if (piece instanceof Rook) {
			moveLocations = rookMoves(piece);
		} else if (piece instanceof Knight || piece instanceof King) {
			// If a piece blocks its path, it must be opposite color
			// for the knight or king to move there.
			// Otherwise, if there is no piece, we can also move there.

			LinkedList<int[]> directions = piece.emptyBoardMoves();

			for (int[] coords : directions) {
				if (onBoard(coords[0], coords[1])) {
					ChessPiece existing = board[coords[0]][coords[1]];
					if (existing == null || existing.isWhite() != piece.isWhite()) {
						moveLocations.add(coords);
					}
				}
			}
		} else if (piece instanceof Bishop) {
			moveLocations = bishopMoves(piece);
		} else if (piece instanceof Queen) {
			LinkedList<int[]> rMoves = rookMoves(piece);
			LinkedList<int[]> bMoves = bishopMoves(piece);
			moveLocations.addAll(rMoves);
			moveLocations.addAll(bMoves);
		} else {
			System.out.println("What is this magical piece?!");
			System.exit(0);
		}


		return moveLocations;
	}

	public LinkedList<int[]> rookMoves(ChessPiece piece) {
		LinkedList<int[]> moveLocations = new LinkedList<int[]>();

		// Down
		for (int i = 1; i + piece.getRow() < 8; i++) {
			ChessPiece existing = board[i + piece.getRow()][piece.getColumn()];
			if (existing != null) {
				// We hit the first item below that is a piece on the board
				if (existing.isWhite() != piece.isWhite()) {
					// Colors don't match, rook can go there
					int[] position = new int[] {i + piece.getRow(), piece.getColumn()};
					moveLocations.add(position);
				}

				// Done looking down
				i = 8;
			} else {
				// Just a blank space
				int[] position = new int[] {i + piece.getRow(), piece.getColumn()};
				moveLocations.add(position);
			}
		}

		// Up
		for (int i = 1; piece.getRow() - i >= 0; i++) {
			ChessPiece existing = board[piece.getRow() - i][piece.getColumn()];
			if (existing != null) {
				// We hit the first item above that is a piece on the board
				if (existing.isWhite() != piece.isWhite()) {
					// Colors don't match, rook can go there
					int[] position = new int[] {piece.getRow() - i, piece.getColumn()};
					moveLocations.add(position);
				}

				// Done looking up
				i = 8;
			} else {
				// Just a blank space
				int[] position = new int[] {piece.getRow() - i, piece.getColumn()};
				moveLocations.add(position);
			}
		}

		// Left
		for (int i = 1; piece.getColumn() - i >= 0; i++) {
			ChessPiece existing = board[piece.getRow()][piece.getColumn() - i];
			if (existing != null) {
				// We hit the first item left that is a piece on the board
				if (existing.isWhite() != piece.isWhite()) {
					// Colors don't match, rook can go there
					int[] position = new int[] {piece.getRow(), piece.getColumn() - i};
					moveLocations.add(position);
				}

				// Done looking left
				i = 8;
			} else {
				// Just a blank space
				int[] position = new int[] {piece.getRow(), piece.getColumn() - i};
				moveLocations.add(position);
			}
		}

		// Right
		for (int i = 1; piece.getColumn() + i < 8; i++) {
			ChessPiece existing = board[piece.getRow()][piece.getColumn() + i];
			if (existing != null) {
				// We hit the first item right that is a piece on the board
				if (existing.isWhite() != piece.isWhite()) {
					// Colors don't match, rook can go there
					int[] position = new int[] {piece.getRow(), piece.getColumn() + i};
					moveLocations.add(position);
				}

				// Done looking right
				i = 8;
			} else {
				// Just a blank space
				int[] position = new int[] {piece.getRow(), piece.getColumn() + i};
				moveLocations.add(position);
			}
		}

		return moveLocations;
	}

	public LinkedList<int[]> bishopMoves(ChessPiece piece) {
		LinkedList<int[]> moveLocations = new LinkedList<int[]>();

		int row = piece.getRow();
		int col = piece.getColumn();
		// Add in the southwest-northeast diagonal
		int positionRow = row;
		int positionCol = col;
		while (onBoard(positionRow + 1, positionCol - 1) && isEmpty(positionRow + 1, positionCol - 1)) {
			positionRow += 1;
			positionCol -= 1;
		}

		if (onBoard(positionRow + 1, positionCol - 1) && board[positionRow + 1][positionCol - 1].isWhite() != piece.isWhite()) {
			positionRow += 1;
			positionCol -= 1;
		}

		// While space is on the board and
		// while either we're talking about the current position
		// or the other position is empty
		while (onBoard(positionRow, positionCol) && (row == positionRow && col == positionCol || isEmpty(positionRow, positionCol))) {
			int[] position = new int[] {positionRow, positionCol};
			if (row != positionRow && col != positionCol) {
				moveLocations.add(position);
			}
			positionRow -= 1;
			positionCol += 1;
		}

		// Add in the northwest-southeast diagonal
		positionRow = row;
		positionCol = col;

		while (onBoard(positionRow - 1, positionCol - 1) && isEmpty(positionRow - 1, positionCol - 1)) {
			positionRow -= 1;
			positionCol -= 1;
		}

		if (onBoard(positionRow - 1, positionCol - 1) && board[positionRow - 1][positionCol - 1].isWhite() != piece.isWhite()) {
			positionRow -= 1;
			positionCol -= 1;
		}

		while (onBoard(positionRow, positionCol) && (row == positionRow && col == positionCol || isEmpty(positionRow, positionCol))) {
			int[] position = new int[] {positionRow, positionCol};
			if (row != positionRow && col != positionCol) {
				moveLocations.add(position);
			}
			positionRow += 1;
			positionCol += 1;
		}


		return moveLocations;
	}

	public static boolean onBoard(int row, int column) {
		return row < 8 && column < 8 && row >= 0 && column >= 0;
	}

	public boolean isEmpty(int row, int column) {
		return board[row][column] == null;
	}
}
