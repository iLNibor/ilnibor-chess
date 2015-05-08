import java.util.LinkedList;

public class Rook extends ChessPiece {
	
	public Rook(boolean white, int row, int column) {
		super(white, "rook", row, column);
	}

	public LinkedList<int[]> moveDirections() {
		return new LinkedList<int[]>();
	}
}