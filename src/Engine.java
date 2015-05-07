public class Engine{

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
}
