package ai;

import java.util.Arrays;

public class ChessBoard {

	long P,p,R,r,N,n,B,b,Q,q,K,k;
	
	public ChessBoard(){
		P = p = R = r = N = n = B = b = Q = q = K = k = 0;
		initiateBoard();
	}
	
	public void initiateBoard(){
		String chessBoard[][]={{"r","n","b","q","k","b","n","r"},
							   {"p","p","p","p","p","p","p","p"},
							   {" "," "," "," "," "," "," "," "},
							   {" "," "," "," "," "," "," "," "},
							   {" "," "," "," "," "," "," "," "},
							   {" "," "," "," "," "," "," "," "},
							   {"P","P","P","P","P","P","P","P"},
							   {"R","N","B","Q","K","B","N","R"}};
		arrayToBitboards(chessBoard);
	}
	
	public void arrayToBitboards(String[][] chessBoard){
		for (int i=0;i<64;i++) {
            String binary="0000000000000000000000000000000000000000000000000000000000000000";
            binary = binary.substring(i+1)+"1"+binary.substring(0, i);
            long temp = binaryToBitboard(binary);
            switch (chessBoard[i/8][i%8]){
                case "P": P += temp; break;
                case "N": N += temp; break;
                case "B": B += temp; break;
                case "R": R += temp; break;
                case "Q": Q += temp; break;
                case "K": K += temp; break;
                case "p": p += temp; break;
                case "n": n += temp; break;
                case "b": b += temp; break;
                case "r": r += temp; break;
                case "q": q += temp; break;
                case "k": k += temp; break;
            }
        }
        drawBoard();
	}
	
	public long binaryToBitboard(String binary){
		if (binary.charAt(0) == '0') return Long.parseLong(binary, 2);
		else return Long.parseLong("1" + binary.substring(2), 2) * 2;
	}
	
	public void drawBoard(){
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
        for (int i=0;i<8;i++) System.out.println(Arrays.toString(chessBoard[i]));
	}
	
	public static void main(String[] args) {
		ChessBoard board = new ChessBoard();
	}

}
