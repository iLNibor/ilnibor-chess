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
	
	public boolean underCheck(){
		
		boolean whiteToMove = gameInfo[0].equals("w");
		
		int[][] pawnChecks = new int[][]{{-1,1},{1,1}};
		int[][] knightChecks = new int[][]{{1,2},{2,1},{2,-1},{1,-2},{-1,2},{-2,1},{-2,-1},{-1,-2}};
		int[][] kingChecks = new int[][]{{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
		
		for (int x = 0; x <= 7; x ++){
			for (int y = 0; y <= 7; y ++){
				Piece temp = board[x][y];
				if (temp != null && temp.ID == 'k' && temp.white == whiteToMove){
					for (int[] pawnMoves: pawnChecks){
						int targetX = pawnMoves[0] + x;
						int targetY = pawnMoves[1] + y;
						if (!temp.white) targetY -= 2;
						if (targetX >= 0 && targetX <= 7 && targetY >= 0 && targetY <= 7 && board[targetX][targetY] != null && board[targetX][targetY].ID == 'p' && board[targetX][targetY].white != whiteToMove)
							return true;
					}
					for (int[] knightMoves: knightChecks){
						int targetX = knightMoves[0] + x;
						int targetY = knightMoves[1] + y;
						if (targetX >= 0 && targetX <= 7 && targetY >= 0 && targetY <= 7 && board[targetX][targetY] != null && board[targetX][targetY].ID == 'n' && board[targetX][targetY].white != whiteToMove)
							return true;
					}
					for (int[] kingMoves: kingChecks){
						int targetX = kingMoves[0] + x;
						int targetY = kingMoves[1] + y;
						if (targetX >= 0 && targetX <= 7 && targetY >= 0 && targetY <= 7 && board[targetX][targetY] != null && board[targetX][targetY].ID == 'k' && board[targetX][targetY].white != whiteToMove)
							return true;
					}
					boolean t = false, tr = false, r = false, br = false, b = false, bl = false, l = false, tl = false;
					for (int i = 1; i <= 7; i ++){
						int posX = x + i;
						int posY = y + i;
						int negX = x - i;
						int negY = y - i;
						if (posX > 7) br = r = tr = true;
						if (negX < 0) bl = l = tl = true;
						if (posY > 7) tl = t = tr = true;
						if (negY < 0) bl = b = br = true;
						if (!tr && board[posX][posY] != null)
							if (board[posX][posY].white != whiteToMove && (board[posX][posY].ID == 'b' || board[posX][posY].ID == 'q')) return true;
							else tr = true;
						if (!bl && board[negX][negY] != null)
							if (board[negX][negY].white != whiteToMove && (board[negX][negY].ID == 'b' || board[negX][negY].ID == 'q')) return true;
							else bl = true;
						if (!br && board[posX][negY] != null)
							if (board[posX][negY].white != whiteToMove && (board[posX][negY].ID == 'b' || board[posX][negY].ID == 'q')) return true;
							else br = true;
						if (!tl && board[negX][posY] != null)
							if (board[negX][posY].white != whiteToMove && (board[negX][posY].ID == 'b' || board[negX][posY].ID == 'q')) return true;
							else tl = true;
						if (!r && board[posX][y] != null)
							if (board[posX][y].white != whiteToMove && (board[posX][y].ID == 'r' || board[posX][y].ID == 'q')) return true;
							else r = true;
						if (!l && board[negX][y] != null)
							if (board[negX][y].white != whiteToMove && (board[negX][y].ID == 'r' || board[negX][y].ID == 'q')) return true;
							else l = true;
						if (!t && board[x][posY] != null)
							if (board[x][posY].white != whiteToMove && (board[x][posY].ID == 'r' || board[x][posY].ID == 'q')) return true;
							else t = true;
						if (!b && board[x][negY] != null)
							if (board[x][negY].white != whiteToMove && (board[x][negY].ID == 'r' || board[x][negY].ID == 'q')) return true;
							else b = true;
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
		System.out.println(standard.underCheck());
	}

}
