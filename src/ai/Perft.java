package ai;

public class Perft {
	
	static int totalCounter, subCounter, maxDepth;
	static MoveGenerator board;

	public static String moveToAlgebra(String move){
		if (!Character.isDigit(move.charAt(3))) return move;
		String moveString = "" + (char)(move.charAt(1) + 49) + ('8' - move.charAt(0)) + (char)(move.charAt(3) + 49) + ('8' - move.charAt(2));
		return moveString;
	}
	
	public static void perft(int depth){
		maxDepth = depth;
		board = new MoveGenerator("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq -");
		board.drawBoard();
		System.out.println();
		perft(0, board.getGameData());
		System.out.println("\nTotal: " + totalCounter);
	}
	
	public static void perft(int depth, long[] gameData){
		String moves;
		board.setGameData(gameData);
		if (board.whiteToMove == 1) moves = board.whiteMoves();
		else moves = board.blackMoves();
		for (int i = 0; i < moves.length(); i += 4){
			if (board.makeMove(moves.substring(i, i + 4)) != null){
				if (depth + 1 == maxDepth) subCounter ++;
				else perft(depth + 1, board.getGameData());
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
		long startTime = System.currentTimeMillis();
		perft(4);
		long endTime = System.currentTimeMillis();
        System.out.println("Time: " + (endTime-startTime) + " milliseconds");
        System.out.println("Moves/sec : "+(int)(totalCounter / ((endTime-startTime)/1000.0)));
	}
}
