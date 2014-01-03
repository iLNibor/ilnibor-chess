import java.util.ArrayList;

public class ChessPiece{
	private int value, identity;
	private ArrayList<ArrayList<Integer>> moves = new ArrayList<ArrayList<Integer>>();
	public ChessPiece(int pieceValue, int pieceIdentity, ArrayList<ArrayList<Integer>> possibleMoves){
		value = pieceValue;
		identity = pieceIdentity;
		moves = possibleMoves;
	}
	
	public static void main(String[] args){
	}
}
