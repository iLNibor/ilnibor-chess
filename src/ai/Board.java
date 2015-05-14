package ai;

public class Board {
	Piece[][] board;
	// whiteToMove; canWhiteKingSideCastle, canWhiteQueenSideCastle, canBlackKingSideCastle, canBlackQueenSideCastle; enPassantSquare
	String[] gameInfo;
	
	public Board(){
		setupBoard("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -");
	}
	
	public void setupBoard(String fenPosition){
		board = new Piece[8][8];
		String[] fields = fenPosition.split(" ");
		char[] position = fields[0].toCharArray();
		int x = 0;
		int y = 7;
		for (char a: position)
			if (Character.isDigit(a))
				x += a - 48;
			else if (a == '/'){
				x = 0;
				y --;
			}
			else{
				Piece newPiece = new Piece(a);
				board[x][y] = newPiece;
				x ++;
			}
		gameInfo = new String[3];
		gameInfo[0] = fields[1];
		gameInfo[1] = fields[2];
		if (!fields[3].equals("-"))
			gameInfo[2] = "" + (fields[3].charAt(0) - 'a') + (fields[3].charAt(1) - 49);
		else
			gameInfo[2] = fields[3];
	}
	
	public void printBoard(){
		if (gameInfo[0].equals("w"))
			System.out.println("White to move");
		else
			System.out.println("Black to move");
		System.out.println(gameInfo[1]);
		System.out.println(gameInfo[2] + "\n");
		for (int y = 7; y >= 0; y --){
			for (int x = 0; x <= 7; x ++){
				if (board[x][y] != null) System.out.print(" " + board[x][y] + " ");
				else System.out.print("   ");
			}
			System.out.println();
		}
	}
	
	public static void main(String[] args) {
		Board standard = new Board();
		standard.printBoard();
	}

}
