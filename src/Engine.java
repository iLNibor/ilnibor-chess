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

			// Pawns can move one diagonal if opposite color piece is there
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

		} else if (piece instanceof Queen) {

		} else {
			System.out.println("What is this magical piece?!");
			System.exit(0);
		}


		return moveLocations;
	}

	public static boolean onBoard(int row, int column) {
		return row < 8 && column < 8 && row >= 0 && column >= 0;
	}
}
