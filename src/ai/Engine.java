package ai;

import java.util.Scanner;

public class Engine {
	static String ENGINE_NAME = "Ultron 1.0";
	static long[] data;
	
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
			} else if (inputString.equals("go")){
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
		input=input.substring(9).concat(" ");
        if (input.contains("startpos")){
            input=input.substring(9);
            BoardTools.initiateStandard();
        }
        else if (input.contains("fen")){
            input=input.substring(4);
            BoardTools.initiateFEN(input.substring(4));
        }
        if (input.contains("moves")){
        	input=input.substring(6);
        	while (input.length() > 0){
        		int start = 0, end = 0;
                int from = (input.charAt(0) - 'a') + (8 * ('8' - input.charAt(1)));
                int to = (input.charAt(2) - 'a') + (8 * ('8' - input.charAt(3)));
                String moves = MoveGenerator.getMoves(data);
                
                for (int i = 0; i < moves.length(); i += 4){
                	String move = moves.substring(i, i + 4);
                	char a = move.charAt(0), b = move.charAt(1), c = move.charAt(2), d = move.charAt(3);
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
                	
                }
                
                input=input.substring(input.indexOf(' ') + 1);
            }
        }
        System.out.println(input);
	}
	public static void inputGo(){
		
	}
	public static void inputPrint(){
		
	}
	
	public static void main(String[] args) {
		Engine.run();

	}
}
