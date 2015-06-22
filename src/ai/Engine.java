package ai;

import java.util.Scanner;

public class Engine {
	static String ENGINE_NAME = "Ultron 1.0";
	
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
				inputPosition();
			} else if (inputString.equals("go")){
				inputGo();
			} else if (inputString.equals("quit")){
				inputQuit();
				input.close();
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
	
	public static void inputPosition(){
		
	}
	
	public static void inputGo(){
		
	}
	
	public static void inputQuit(){
		
	}
	
	public static void inputPrint(){
		
	}
	
	public static void main(String[] args) {
		

	}

}
