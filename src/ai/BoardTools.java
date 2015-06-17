package ai;

import java.util.Arrays;

public class BoardTools {
	
	static int totalCounter, subCounter, maxDepth;
	
	public static long[] initiateStandard(){
		return initiateFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -");
	}
	public static long[] initiateFEN(String FEN){
		long P,p,R,r,N,n,B,b,Q,q,K,k,epSquare,wkCastle,wqCastle,bkCastle,bqCastle,whiteToMove;
		P = p = R = r = N = n = B = b = Q = q = K = k = epSquare = wkCastle = wqCastle = bkCastle = bqCastle = whiteToMove = 0;
		int indexA = 0, indexB = 0;
		while (FEN.charAt(indexA) != ' '){
			switch(FEN.charAt(indexA ++)){
				case 'P': P |= (1L << indexB ++); break;
				case 'p': p |= (1L << indexB ++); break;
				case 'N': N |= (1L << indexB ++); break;
				case 'n': n |= (1L << indexB ++); break;
				case 'B': B |= (1L << indexB ++); break;
				case 'b': b |= (1L << indexB ++); break;
				case 'R': R |= (1L << indexB ++); break;
				case 'r': r |= (1L << indexB ++); break;
				case 'Q': Q |= (1L << indexB ++); break;
				case 'q': q |= (1L << indexB ++); break;
				case 'K': K |= (1L << indexB ++); break;
				case 'k': k |= (1L << indexB ++); break;
				case '/': break;
				case '1': indexB ++; break;
				case '2': indexB += 2; break;
				case '3': indexB += 3; break;
				case '4': indexB += 4; break;
				case '5': indexB += 5; break;
				case '6': indexB += 6; break;
				case '7': indexB += 7; break;
				case '8': indexB += 8; break;
				default: break;
			}
		}
		if (FEN.charAt(++ indexA) == 'w') whiteToMove = 1;
		indexA += 2;
		while (FEN.charAt(indexA) != ' '){
			char temp = FEN.charAt(indexA ++);
			if (temp == 'K') wkCastle = 1;
			else if (temp == 'Q') wqCastle = 1;
			else if (temp == 'k') bkCastle = 1;
			else if (temp == 'q') bqCastle = 1;
		}
		if (FEN.charAt(++ indexA) != '-'){
			int column = FEN.charAt(indexA ++) - 'a';
			char rank = FEN.charAt(indexA ++);
			int row;
			if (rank == '3') row = 4;
			else row = 3;
			epSquare = 1L << (row * 8 + column);
		}
		return new long[]{P,p,R,r,N,n,B,b,Q,q,K,k,epSquare,wkCastle, wqCastle, bkCastle, bqCastle, whiteToMove};
	}
	
	public static void drawBitboard(long bitBoard) {
        String chessBoard[][]=new String[8][8];
        for (int i=0;i<64;i++) {
            if (((bitBoard>>>i)&1)==1) chessBoard[i/8][i%8]="X";
            else chessBoard[i/8][i%8]=" ";
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }
	public static void drawBoard(long[] data){
		long P = data[0], p = data[1], R = data[2], r = data[3], N = data[4], n = data[5], B = data[6], b = data[7], Q = data[8], q = data[9], K = data[10], k = data[11], epSquare = data[12];
		String chessBoard[][] = new String[8][8];
        for (int i=0;i<64;i++) chessBoard[i/8][i%8]=" ";
        for (int i=0;i<64;i++)
            if (((P>>i)&1)==1) chessBoard[i/8][i%8]="P";
            else if (((N>>i)&1)==1) chessBoard[i/8][i%8]="N";
            else if (((B>>i)&1)==1) chessBoard[i/8][i%8]="B";
            else if (((R>>i)&1)==1) chessBoard[i/8][i%8]="R";
            else if (((Q>>i)&1)==1) chessBoard[i/8][i%8]="Q";
            else if (((K>>i)&1)==1) chessBoard[i/8][i%8]="K";
            else if (((p>>i)&1)==1) chessBoard[i/8][i%8]="p";
            else if (((n>>i)&1)==1) chessBoard[i/8][i%8]="n";
            else if (((b>>i)&1)==1) chessBoard[i/8][i%8]="b";
            else if (((r>>i)&1)==1) chessBoard[i/8][i%8]="r";
            else if (((q>>i)&1)==1) chessBoard[i/8][i%8]="q";
            else if (((k>>i)&1)==1) chessBoard[i/8][i%8]="k";
            else if (((epSquare>>i)&1)==1) chessBoard[i/8][i%8]="|";
        for (int i=0;i<8;i++) System.out.println(Arrays.toString(chessBoard[i]));
	}
	
	public static String moveToAlgebra(String move){
		if (!Character.isDigit(move.charAt(3))) return move;
		String moveString = "" + (char)(move.charAt(1) + 49) + ('8' - move.charAt(0)) + (char)(move.charAt(3) + 49) + ('8' - move.charAt(2));
		return moveString;
	}
	public static void perft(String FEN, int depth){
		maxDepth = depth;
		long[] data = initiateFEN(FEN);
		drawBoard(data);
		System.out.println();
		long startTime = System.currentTimeMillis();
		perft(0, data);
		long endTime = System.currentTimeMillis();
		System.out.println("\nTotal: " + totalCounter);
		System.out.println("Time: " + (endTime-startTime) + " milliseconds");
        System.out.println("Moves/sec : "+(int)(totalCounter / ((endTime-startTime)/1000.0)));
	}
	public static void perft(int depth, long[] data){
		String moves = MoveGenerator.getMoves(data);
		for (int i = 0; i < moves.length(); i += 4){
			long[] newData = MoveGenerator.makeMove(moves.substring(i, i + 4), data);
			if (newData != null){
				if (depth + 1 == maxDepth) subCounter ++;
				else perft(depth + 1, newData);
				if (depth == 0){
					System.out.println(moveToAlgebra(moves.substring(i, i + 4)) + " " + subCounter);
					totalCounter += subCounter;
					subCounter = 0;
				}
			}
		}
	}
	public static void perftConcurrency(String FEN, int depth) throws InterruptedException{
		maxDepth = depth;
		long[] data = initiateFEN(FEN);
		drawBoard(data);
		System.out.println();
		long startTime = System.currentTimeMillis();
		String moves = MoveGenerator.getMoves(data);
		for (int i = 0; i < moves.length(); i += 4){
			long[] newData = MoveGenerator.makeMove(moves.substring(i, i + 4), data);
			if (newData != null){
				PerftThread thread = new PerftThread(newData);
				thread.start();
			}
		}
		Thread.sleep(4000);
		long endTime = System.currentTimeMillis();
		System.out.println("\nTotal: " + totalCounter);
		System.out.println("Time: " + (endTime-startTime) + " milliseconds");
        System.out.println("Moves/sec : "+(int)(totalCounter / ((endTime-startTime)/1000.0)));
	}
	static class PerftThread extends Thread{
		long[] data;
		public PerftThread(long[] data){
			this.data = data;
		}
		public void run(){
			
			perft(1, data);
			System.out.println("HI");
		}
		public void perft(int depth, long[] data){
			String moves = MoveGenerator.getMoves(data);
			for (int i = 0; i < moves.length(); i += 4){
				long[] newData = MoveGenerator.makeMove(moves.substring(i, i + 4), data);
				if (newData != null)
					if (depth + 1 == maxDepth) totalCounter ++;
					else perft(depth + 1, newData);
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		//perft("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 6);
		perftConcurrency("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 3);
	}
}