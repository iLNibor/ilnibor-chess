package ai;

import java.util.Arrays;

public class ChessBoard {

	long P,p,R,r,N,n,B,b,Q,q,K,k;
	final long[] FILES = new long[]{72340172838076673L, 144680345676153346L, 289360691352306692L, 578721382704613384L, 1157442765409226768L, 2314885530818453536L, 4629771061636907072L, -9187201950435737472L};
	final long FILE_A=72340172838076673L, FILE_H=-9187201950435737472L, FILE_AB=217020518514230019L, FILE_GH=-4557430888798830400L;
	final long[] RANKS = new long[]{255L, 65280L, 16711680L, 4278190080L, 1095216660480L, 280375465082880L, 71776119061217280L, -72057594037927936L};
    final long RANK_1=-72057594037927936L, RANK_4=1095216660480L, RANK_5=4278190080L, RANK_8=255L;
    final long CENTRE=103481868288L, EXTENDED_CENTRE=66229406269440L, KING_SIDE=-1085102592571150096L, QUEEN_SIDE=1085102592571150095L;
    final long KING_B7=460039L;
    final long KNIGHT_C6=43234889994L;
    long WHITE_LEGAL, WHITE_PIECES, BLACK_LEGAL, BLACK_PIECES, EMPTY;
    String history;
	
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
            binary = binary.substring(0, i) + "1" + binary.substring(i + 1);
            long temp = binaryToBitboard(binary);
            switch (chessBoard[7 - i/8][7 - i%8]){
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
	
	public String whiteMoves(){
		WHITE_LEGAL = ~(P|R|N|B|Q|K|k);
		BLACK_PIECES = p|r|n|b|q;
		
		return "";
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
