package ai;

import java.util.ArrayList;

public class Piece {
	char ID;
	double value;
	boolean white;
	int[][] moves;
	
	public Piece(char id){
		white = false;
		if (id >= 'A' && id <= 'Z'){
			id += 32;
			white = true;
		}
		ID = id;
		switch(ID){
			case 'p':
				value = 1;
				if (white) moves = new int[][]{{0,1}};
				else moves = new int[][]{{0,-1}};
				break;
			case 'n':
				value = 3;
				moves = new int[][]{{1,2},{2,1},{2,-1},{1,-2},{-1,2},{-2,1},{-2,-1},{-1,-2}};
				break;
			case 'b':
				value = 3;
				moves = new int[][]{{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},
									{1,-1},{2,-2},{3,-3},{4,-4},{5,-5},{6,-6},{7,-7},
									{-1,1},{-2,2},{-3,3},{-4,4},{-5,5},{-6,6},{-7,7},
									{-1,-1},{-2,-2},{-3,-3},{-4,-4},{-5,-5},{-6,-6},{-7,-7}};
				break;
			case 'r':
				value = 5;
				moves = new int[][]{{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},
									{-1,0},{-2,0},{-3,0},{-4,0},{-5,0},{-6,0},{-7,0},
									{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},
									{0,-1},{0,-2},{0,-3},{0,-4},{0,-5},{0,-6},{0,-7}};
				break;
			case 'q':
				value = 9;
				moves = new int[][]{{1,1},{2,2},{3,3},{4,4},{5,5},{6,6},{7,7},
									{1,-1},{2,-2},{3,-3},{4,-4},{5,-5},{6,-6},{7,-7},
									{-1,1},{-2,2},{-3,3},{-4,4},{-5,5},{-6,6},{-7,7},
									{-1,-1},{-2,-2},{-3,-3},{-4,-4},{-5,-5},{-6,-6},{-7,-7},
									{1,0},{2,0},{3,0},{4,0},{5,0},{6,0},{7,0},
									{-1,0},{-2,0},{-3,0},{-4,0},{-5,0},{-6,0},{-7,0},
									{0,1},{0,2},{0,3},{0,4},{0,5},{0,6},{0,7},
									{0,-1},{0,-2},{0,-3},{0,-4},{0,-5},{0,-6},{0,-7}};
				break;
			case 'k':
				value = -1;
				moves = new int[][]{{1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1}};
				break;
		}
	}
	
//	public ArrayList<String> getMoves(){
//		ArrayList<String> possibleMoves = new ArrayList<String>();
//		for (int[] a: moves){
//			int endX = xCoord + a[0];
//			int endY = yCoord + a[1];
//			if (endX >= 0 && endX <= 7 && endY >= 0 && endY <= 7)
//				possibleMoves.add("" + xCoord + yCoord + endX + endY);
//		}
//		return possibleMoves;
//	}
	
	public String toString(){
		String output = "" + ID;
		if (white)
			output = output.toUpperCase();
		return output;
	}
	
	public static void main(String[] args){
		String[][] temp = new String[5][5];
		System.out.println(temp[2][3]);
	}
}
