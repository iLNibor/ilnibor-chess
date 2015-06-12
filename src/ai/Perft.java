package ai;

public class Perft {
	
	static int totalCounter, subCounter, maxDepth;

	public static String moveToAlgebra(String move){
		if (!Character.isDigit(move.charAt(3))) return move;
		String moveString = "" + (char)(move.charAt(1) + 49) + ('8' - move.charAt(0)) + (char)(move.charAt(3) + 49) + ('8' - move.charAt(2));
		return moveString;
	}
	
	public static void perft(int depth){
		maxDepth = depth;
		ChessBoard board = new ChessBoard();
		//board.makeMove("6252", true);
		//board.makeMove("1323", false);
		//board.makeMove("7340", true);
		board.drawBoard();
		System.out.println();
		perft(0, board, true);
		System.out.println("Total: " + totalCounter);
	}
	
	public static void perft(int depth, ChessBoard board, boolean whiteToMove){
		String moves;
		if (whiteToMove) moves = board.whiteMoves();
		else moves = board.blackMoves();
		long[] gameData = board.getGameData();
		for (int i = 0; i < moves.length(); i += 4){
			if (board.makeMove(moves.substring(i, i + 4), whiteToMove) != null){
				if (depth + 1 == maxDepth) subCounter ++;
				else perft(depth + 1, board, !whiteToMove);
				if (depth == 0){
					System.out.println(moveToAlgebra(moves.substring(i, i + 4)) + " " + subCounter);
					totalCounter += subCounter;
					subCounter = 0;
				}
			}
			board.setGameData(gameData);
		}
	}
	
	public static void main(String[] args) {
		perft(3);
	}
}
