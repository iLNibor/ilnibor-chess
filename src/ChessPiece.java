import java.util.ArrayList;
import java.util.Arrays;

public class ChessPiece{
	private boolean white;
	private int value, identity;
	private ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
	public ChessPiece(int pieceIdentity, boolean isWhite){
		white = isWhite;
		identity = pieceIdentity;
		determineValue();
		determineMoves();
	}
	public void determineValue(){
		switch (identity){
			case 0:	value = 0;
					break;
			case 1: value = 0;
					break;
			case 2: value = 0;
					break;
			case 3: value = 0;
					break;
			case 4: value = 1;
					break;
			case 5: value = 1;
					break;
			case 6: value = 1;
					break;
			case 7: value = 1;
					break;
			case 8: value = 3;
					break;
			case 9: value = 3;
					break;
			case 10: value = 5;
					break;
			case 11: value = 8;
					break;
			default: value = -1;
					break;
		}
	}
	
	public void determineMoves(){
		switch (identity){
			case 0: moves.add(new ArrayList<Integer>(Arrays.asList(1, 1)));
					moves.add(new ArrayList<Integer>(Arrays.asList(1, 0)));
					moves.add(new ArrayList<Integer>(Arrays.asList(1, -1)));
					moves.add(new ArrayList<Integer>(Arrays.asList(0, 1)));
					moves.add(new ArrayList<Integer>(Arrays.asList(0, -1)));
					moves.add(new ArrayList<Integer>(Arrays.asList(-1, 1)));
					moves.add(new ArrayList<Integer>(Arrays.asList(-1, 0)));
					moves.add(new ArrayList<Integer>(Arrays.asList(-1, -1)));
					break;
			case 1: 
		}
	}
	
	public static void main(String[] args){
	}
}
