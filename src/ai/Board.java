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
	
	public boolean whiteUnderCheck(){
		
		int[][] pawnChecks = new int[][]{{-1,1},{1,1}};
		int[][] knightChecks = new int[][]{{1,2},{2,1},{2,-1},{1,-2},{-1,2},{-2,1},{-2,-1},{-1,-2}};
		
		for (int x = 0; x <= 7; x ++){
			for (int y = 0; y <= 7; y ++){
				Piece temp = board[x][y];
				if (temp != null && temp.ID == 'k' && temp.white){
					for (int[] pawnMoves: pawnChecks){
						int targetX = pawnMoves[0] + x;
						int targetY = pawnMoves[1] + y;
						if (targetX >= 0 && targetX <= 7 && targetY >= 0 && targetY <= 7 && board[targetX][targetY] != null && board[targetX][targetY].ID == 'p')
							return true;
					}
					for (int[] knightMoves: knightChecks){
						int targetX = knightMoves[0] + x;
						int targetY = knightMoves[1] + y;
						if (targetX >= 0 && targetX <= 7 && targetY >= 0 && targetY <= 7 && board[targetX][targetY] != null && board[targetX][targetY].ID == 'n')
							return true;
					}
					for (int i = 1; i <= 7; i ++){
						int posX = x + i;
						int posY = y + i;
						int negX = x - i;
						int negY = y - i;
						if (posX >= 0 && posX <= 7 && posY >= 0 && posY <= 7 && board[posX][posY] != null && (board[posX][posY].ID == 'b' || board[posX][posY].ID == 'q'))
							return true;
					}
					return false;
				}
			}
		}
		System.out.println("White king not found");
		return false;
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
	
	public static void main(String[] args){
		Board standard = new Board();
		standard.printBoard();
	}

}
