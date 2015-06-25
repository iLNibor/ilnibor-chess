package ai;

import java.util.Scanner;

public class Engine {
	static String ENGINE_NAME = "Ultron 1.0";
	static long[] data;
	static int MAX_DEPTH = 6;
	static boolean ultronIsWhite;
	
	public static void run(){
		Scanner input = new Scanner(System.in);
		while(true){
			String inputString = input.nextLine();
			if (inputString.equals("uci")){
				inputUCI();
			} else if (inputString.startsWith("setoption")){
				inputSetOption(inputString);
			} else if (inputString.equals("isready")){
				inputIsReady();
			} else if (inputString.equals("ucinewgame")){
				inputNewGame();
			} else if (inputString.startsWith("position")){
				inputPosition(inputString);
			} else if (inputString.contains("go")){
				inputGo();
			} else if (inputString.equals("quit")){
				input.close();
				System.exit(1);
			} else if (inputString.equals("print")){
				inputPrint();
			}
		}
	}
	
	public static void inputUCI(){
		System.out.println("id name " + ENGINE_NAME);
		System.out.println("id author Akhilles");
		System.out.println("uciok");
	}
	public static void inputSetOption(String inputString){
		
	}
	public static void inputIsReady(){
		System.out.println("readyok");
	}
	public static void inputNewGame(){
	
	}
	public static void inputPosition(String input){
		input = input.substring(9);
        if (input.contains("startpos")){
            input=input.substring(8);
            data = BoardTools.initiateStandard();
        }
        else if (input.contains("fen")){
            input=input.substring(4);
            data = BoardTools.initiateFEN(input);
        }
        if (input.contains("moves")){
        	for (String a: input.substring(input.indexOf("moves") + 6).split(" ")){
                String moves = MoveGenerator.getMoves(data);
                for (int i = 0; i < moves.length(); i += 4){
                	String move = moves.substring(i, i + 4);
                	String algebraicMove = BoardTools.moveToAlgebra(move);
                	if (a.equals(algebraicMove)){
                		data = MoveGenerator.makeMove(move, data);
                		break;
                	}
                }
            }
        }
        inputPrint();
	}
	public static void inputGo(){
		System.out.println("bestmove " + bestMove());
	}
	public static void inputPrint(){
		BoardTools.drawBoard(data);
	}
	
	public static String bestMove(){
		String bestMove = "";
		int bestScore = Integer.MIN_VALUE;
		long[] tempData = data;
		String moves = MoveGenerator.getMoves(data);
		
		for (int i = 0; i < moves.length(); i += 4){
			String move = moves.substring(i, i + 4);
			data = MoveGenerator.makeMove(move, tempData);
			if (data != null){
				int tempScore = min(1, bestScore);
				if (tempScore > bestScore){
					bestScore = tempScore;
					bestMove =  BoardTools.moveToAlgebra(move);
				}
			}
		}
		
		data = tempData;
		if (bestScore > 195000) System.out.println("(mate in " + (200001 - bestScore) / 2 + ")");
		return bestMove;
	}
	public static int min(int depth, int hardMin){
		if (depth == MAX_DEPTH) return evaluate();
		int bestScore = Integer.MAX_VALUE;
		long[] tempData = data;
		String moves = MoveGenerator.getMoves(data);
		
		for (int i = 0; i < moves.length(); i += 4){
			data = MoveGenerator.makeMove(moves.substring(i, i + 4), tempData);
			if (data != null){
				int tempScore = max(depth + 1, bestScore);
				if (tempScore <= hardMin) return tempScore;
				else if (tempScore < bestScore) bestScore = tempScore;
			}
		}
		
		if (bestScore == Integer.MAX_VALUE)
			if (MoveGenerator.underCheck(tempData)) return 200000 - depth;
			else return 0;
		
		return bestScore;
	}
	public static int max(int depth, int hardMax){
		if (depth == MAX_DEPTH) return evaluate();
		int bestScore = Integer.MIN_VALUE;
		long[] tempData = data;
		String moves = MoveGenerator.getMoves(data);
		
		for (int i = 0; i < moves.length(); i += 4){
			data = MoveGenerator.makeMove(moves.substring(i, i + 4), tempData);
			if (data != null){
				int tempScore = min(depth + 1, bestScore);
				if (tempScore >= hardMax) return tempScore;
				else if (tempScore > bestScore) bestScore = tempScore;
			}
		}
		
		if (bestScore == Integer.MIN_VALUE)
			if (MoveGenerator.underCheck(tempData)) return -200000 + depth;
			else return 0;
		
		return bestScore;
	}
	public static int evaluate(){
		int whiteScore = 0;
		long P = data[0], p = data[1], R = data[2], r = data[3], N = data[4], n = data[5], B = data[6], b = data[7], Q = data[8], q = data[9];
		
		long location = P & ~(P - 1);
		while (location != 0){
			whiteScore += 100;
			P &= ~location;
			location = P & ~(P - 1);
		}
		
		location = p & ~(p - 1);
		while (location != 0){
			whiteScore -= 100;
			p &= ~location;
			location = p & ~(p - 1);
		}
		
		location = R & ~(R - 1);
		while (location != 0){
			whiteScore += 500;
			R &= ~location;
			location = R & ~(R - 1);
		}
		
		location = r & ~(r - 1);
		while (location != 0){
			whiteScore -= 500;
			r &= ~location;
			location = r & ~(r - 1);
		}
		
		location = N & ~(N - 1);
		while (location != 0){
			whiteScore += 320;
			N &= ~location;
			location = N & ~(N - 1);
		}
		
		location = n & ~(n - 1);
		while (location != 0){
			whiteScore -= 320;
			n &= ~location;
			location = n & ~(n - 1);
		}
		
		location = B & ~(B - 1);
		while (location != 0){
			whiteScore += 330;
			B &= ~location;
			location = B & ~(B - 1);
		}
		
		location = b & ~(b - 1);
		while (location != 0){
			whiteScore -= 330;
			b &= ~location;
			location = b & ~(b - 1);
		}
		
		location = Q & ~(Q - 1);
		while (location != 0){
			whiteScore += 900;
			Q &= ~location;
			location = Q & ~(Q - 1);
		}
		
		location = q & ~(q - 1);
		while (location != 0){
			whiteScore -= 900;
			q &= ~location;
			location = q & ~(q - 1);
		}
		
		if (ultronIsWhite) return whiteScore;
		else return whiteScore * -1;
	}
	
	public static void main(String[] args) {
		Engine.run();
	}
}
