package ai;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BoardTools {
	
	static int totalCounter, subCounter, maxDepth;
	static AtomicInteger iter = new AtomicInteger(0);
	
	// Returns a standard board representation
	public static long[] initiateStandard(){
		return initiateFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
	}
	// Returns a board representing the starting position specified in FEN notation
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
	
	// Prints a bitboard
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
	// Prints the chess board represented by 'data'
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
	
	// Converts a 4 character move from row + column notation to algebraic notation (e.g. a1b2, e3d4)
	public static String moveToAlgebra(String move){
		int start = 0, end = 0;
		char a = move.charAt(0), b = move.charAt(1), c = move.charAt(2), d = move.charAt(3);
		String algebraicMove = "";
		
    	if (Character.isDigit(d)){
			start = (a - 48) * 8 + (b - 48);
			end = (c - 48) * 8 + (d - 48);
    	} else if (d == 'P'){
			if (Character.isUpperCase(c)){
				start = Long.numberOfTrailingZeros(MoveGenerator.FILES[a - 48] & MoveGenerator.RANKS[6]);
				end = Long.numberOfTrailingZeros(MoveGenerator.FILES[b - 48] & MoveGenerator.RANKS[7]);
			} else {
				start = Long.numberOfTrailingZeros(MoveGenerator.FILES[a - 48] & MoveGenerator.RANKS[1]);
				end = Long.numberOfTrailingZeros(MoveGenerator.FILES[b - 48] & MoveGenerator.RANKS[0]);
			}
			algebraicMove = "" + Character.toLowerCase(c);
    	} else if (d == 'E'){
    		if (c == 'W'){
    			start = Long.numberOfTrailingZeros(MoveGenerator.FILES[a - 48] & MoveGenerator.RANKS[4]);
    			end = Long.numberOfTrailingZeros(MoveGenerator.FILES[b - 48] & MoveGenerator.RANKS[5]);
    		} else {
    			start = Long.numberOfTrailingZeros(MoveGenerator.FILES[a - 48] & MoveGenerator.RANKS[3]);
    			end = Long.numberOfTrailingZeros(MoveGenerator.FILES[b - 48] & MoveGenerator.RANKS[2]);
    		}
    	} else if (d == 'C'){
    		if (a == 'W'){
    			start = 60;
    			if (b == 'K') end = 62;
    			else end = 58;
    		} else {
    			start = 4;
    			if (b == 'K') end = 6;
    			else end = 2;
    		}
    	}
    	else System.out.println("INVALID MOVE");
    	
    	return "" + (char)('a' + (start % 8)) + (char)('8' - (start / 8)) + (char)('a' + (end % 8)) + (char)('8' - (end / 8)) + algebraicMove;
	}
	
	// Performs a single-threaded perft routine to test move generation from the 'FEN' starting position to a specified 'depth'
	public static void perft(String FEN, int depth){
		maxDepth = depth;
		totalCounter = 0;
		long[] data = initiateFEN(FEN);
		drawBoard(data);
		System.out.println();
		long startTime = System.currentTimeMillis();
		perft(0, data);
		long endTime = System.currentTimeMillis();
		System.out.println("Total: " + totalCounter);
		System.out.println("Time: " + (endTime-startTime) + " milliseconds");
        System.out.println("Moves/sec : "+(int)(totalCounter / ((endTime-startTime)/1000.0)));
	}
	// Recursive helper method to perft()
	public static void perft(int depth, long[] data){
		String moves = MoveGenerator.getMoves(data);
		for (int i = 0; i < moves.length(); i += 4){
			long[] newData = MoveGenerator.makeMove(moves.substring(i, i + 4), data);
			if (newData != null){
				if (depth + 1 == maxDepth) subCounter ++;
				else perft(depth + 1, newData);
				if (depth == 0){
					//System.out.println(moveToAlgebra(moves.substring(i, i + 4)) + " " + subCounter);
					totalCounter += subCounter;
					subCounter = 0;
				}
			}
		}
	}
	// Performs a multi-threaded perft routine to test move generation from the 'FEN' starting position to a specified 'depth' using the specified 'numThreads'
	public static void perftConcurrency(String FEN, int depth, int numThreads) throws InterruptedException{
		maxDepth = depth;
		totalCounter = 0;
		long[] data = initiateFEN(FEN);
		drawBoard(data);
		System.out.println();
		long startTime = System.currentTimeMillis();
		
		BlockingQueue<Runnable> jobs = new ArrayBlockingQueue<Runnable>(60);
		ThreadPoolExecutor engine = new ThreadPoolExecutor(numThreads, numThreads, 1, TimeUnit.SECONDS, jobs);
		
		String moves = MoveGenerator.getMoves(data);
		for (int i = 0; i < moves.length(); i += 4){
			long[] newData = MoveGenerator.makeMove(moves.substring(i, i + 4), data);
			if (newData != null)
				engine.execute(new PerftJob(newData));
		}
		
		engine.shutdown();
		engine.awaitTermination(10, TimeUnit.MINUTES);
		
		long endTime = System.currentTimeMillis();
		
		System.out.println("Total: " + iter.get());
		System.out.println("Time: " + (endTime-startTime) + " milliseconds");
        System.out.println("Moves/sec : "+(int)(iter.get() / ((endTime-startTime)/1000.0)));
	}
	// Thread object used in the multi-threaded perft routine
	static class PerftJob implements Runnable{
		long[] data;
		public PerftJob(long[] data){
			this.data = data;
		}
		public void run(){
			iter.addAndGet(perft(1, data));
		}
		public int perft(int depth, long[] data){
			String moves = MoveGenerator.getMoves(data);
			int counter = 0;
			for (int i = 0; i < moves.length(); i += 4){
				long[] newData = MoveGenerator.makeMove(moves.substring(i, i + 4), data);
				if (newData != null)
					if (depth + 1 == maxDepth) counter ++;
					else counter += perft(depth + 1, newData);
			}
			return counter;
		}
	}
	// Benchmark routine to test move generation speed using 1 thread to 25 threads
	public static void benchmark(int depth) throws InterruptedException{
		maxDepth = depth;
		long[] data = initiateFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq -");
		String moves = MoveGenerator.getMoves(data);
		
		for (int i = 1; i <= 25; i ++){
			iter.set(0);
			long startTime = System.currentTimeMillis();
			
			BlockingQueue<Runnable> jobs = new ArrayBlockingQueue<Runnable>(60);
			ThreadPoolExecutor engine = new ThreadPoolExecutor(i, i, 1, TimeUnit.SECONDS, jobs);
			for (int j = 0; j < moves.length(); j += 4){
				long[] newData = MoveGenerator.makeMove(moves.substring(j, j + 4), data);
				if (newData != null)
					engine.execute(new PerftJob(newData));
			}
			
			engine.shutdown();
			engine.awaitTermination(1, TimeUnit.MINUTES);
			
			long endTime = System.currentTimeMillis();
			
			System.out.println(i + " threads:\t" + (endTime-startTime)/1000.0 + " seconds");
		}
	}

}
