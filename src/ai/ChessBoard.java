package ai;

import java.util.Arrays;

public class ChessBoard {

	long P,p,R,r,N,n,B,b,Q,q,K,k;
	final long[] FILES = new long[]{72340172838076673L, 144680345676153346L, 289360691352306692L, 578721382704613384L, 1157442765409226768L, 2314885530818453536L, 4629771061636907072L, -9187201950435737472L};
	final long[] RANKS = new long[]{-72057594037927936L, 71776119061217280L, 280375465082880L, 1095216660480L, 4278190080L, 16711680L, 65280L, 255L};
	final long[] DIAGS = new long[]{0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L, 0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L, 0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L};
	final long[] ANTIDIAGS = new long[]{0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L, 0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L, 0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L};
    final long CENTER = 103481868288L, EXTENDED_CENTER = 66229406269440L, KING_SIDE = -1085102592571150096L, QUEEN_SIDE = 1085102592571150095L, KNIGHT_SPAN = 43234889994L, KING_SPAN = 460039L;
    long WHITE_LEGAL, WHITE_PIECES, BLACK_LEGAL, BLACK_PIECES, OCCUPIED, EMPTY;
    String history;
	
	public ChessBoard(){
		P = p = R = r = N = n = B = b = Q = q = K = k = 0;
		history = "6242";
		initiateBoard();
	}
	
	public void initiateBoard(){
		String chessBoard[][]={{"r","n","b","q","k","b","n","r"},
							   {"p","p","p","p","p","p","p","p"},
							   {" "," "," "," "," "," "," "," "},
							   {" "," "," "," "," "," "," "," "},
							   {" "," ","P","p"," "," "," "," "},
							   {" "," "," "," "," "," "," "," "},
							   {"P","P","P","P","P","P","P","P"},
							   {"R","N","B","Q","K","B","N","R"}};
		arrayToBitboards(chessBoard);
	}
	
	public void arrayToBitboards(String[][] chessBoard){
		for (int i=0;i<64;i++) {
            String binary="0000000000000000000000000000000000000000000000000000000000000000";
            binary = binary.substring(i + 1) + "1" + binary.substring(0, i);
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
        System.out.println();
	}
	
	public long linearMoves(int origin){
		long binaryOrigin = 1L << origin;
		long horizontal = (OCCUPIED - 2 * binaryOrigin) ^ Long.reverse(Long.reverse(OCCUPIED) - 2 * Long.reverse(binaryOrigin));
        long vertical = ((OCCUPIED & FILES[origin%8]) - (2 * binaryOrigin)) ^ Long.reverse(Long.reverse(OCCUPIED & FILES[origin%8]) - (2 * Long.reverse(binaryOrigin)));
        return (horizontal & RANKS[7 - origin/8]) | (vertical & FILES[origin%8]);
	}
	
	public long diagMoves(int origin){
		long binaryOrigin = 1L << origin;
        long diag = ((OCCUPIED & DIAGS[(origin/8) + (origin%8)]) - (2 * binaryOrigin)) ^ Long.reverse(Long.reverse(OCCUPIED & DIAGS[(origin/8) + (origin%8)]) - (2 * Long.reverse(binaryOrigin)));
        long antidiag = ((OCCUPIED & ANTIDIAGS[(origin/8) + 7 - (origin%8)]) - (2 * binaryOrigin)) ^ Long.reverse(Long.reverse(OCCUPIED & ANTIDIAGS[(origin/8) + 7 - (origin%8)]) - (2 * Long.reverse(binaryOrigin)));
        return (diag & DIAGS[(origin/8) + (origin%8)]) | (antidiag & ANTIDIAGS[(origin/8) + 7 - (origin%8)]);
	}
	
	public String whiteMoves(){
		String moves = "";
		
		WHITE_LEGAL = ~(P|R|N|B|Q|K|k);
		BLACK_PIECES = p|r|n|b|q;
		OCCUPIED = P|N|B|R|Q|K|p|n|b|r|q|k;
        EMPTY = ~OCCUPIED;
        
        moves += whitePawnMoves();
        moves += bishopMoves(true, WHITE_LEGAL);
        moves += rookMoves(true, WHITE_LEGAL);
        moves += queenMoves(true, WHITE_LEGAL);
        moves += knightMoves(true, WHITE_LEGAL);
        moves += kingMoves(true, WHITE_LEGAL);
        
        unsafeBlack();
        System.out.println();
        
        System.out.println("Moves: " + moves);
        System.out.println(moves.length() / 4 + " moves");
		return moves;
	}
	
	public String blackMoves(){
		String moves = "";
		
		BLACK_LEGAL = ~(p|r|n|b|q|k|K);
		WHITE_PIECES = P|R|N|B|Q;
		OCCUPIED = P|N|B|R|Q|K|p|n|b|r|q|k;
        EMPTY = ~OCCUPIED;
        
        moves += blackPawnMoves();
        moves += bishopMoves(false, BLACK_LEGAL);
        moves += rookMoves(false, BLACK_LEGAL);
        moves += queenMoves(false, BLACK_LEGAL);
        moves += knightMoves(false, BLACK_LEGAL);
        moves += kingMoves(false, BLACK_LEGAL);
        
        unsafeWhite();
        System.out.println();
        
        System.out.println("Moves: " + moves);
        System.out.println(moves.length() / 4 + " moves");
        return moves;
	}
	
	public String whitePawnMoves(){
        String list="";
        //x1,y1,x2,y2
        long PAWN_MOVES=(P>>7)&BLACK_PIECES&~RANKS[7]&~FILES[0];//capture right
        long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8-1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(P>>9)&BLACK_PIECES&~RANKS[7]&~FILES[7];//capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8+1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(P>>8)&EMPTY&~RANKS[7];//move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(P>>16)&EMPTY&(EMPTY>>8)&RANKS[3];//move 2 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+2)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Promotion Type,"P"
        PAWN_MOVES=(P>>7)&BLACK_PIECES&RANKS[7]&~FILES[0];//pawn promotion by capture right
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"QP"+(index%8-1)+(index%8)+"RP"+(index%8-1)+(index%8)+"BP"+(index%8-1)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(P>>9)&BLACK_PIECES&RANKS[7]&~FILES[7];//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"QP"+(index%8+1)+(index%8)+"RP"+(index%8+1)+(index%8)+"BP"+(index%8+1)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(P>>8)&EMPTY&RANKS[7];//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8)+(index%8)+"QP"+(index%8)+(index%8)+"RP"+(index%8)+(index%8)+"BP"+(index%8)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Space,"E"
        if (history.length()>=4 && history.charAt(history.length()-1)==history.charAt(history.length()-3) && Math.abs(history.charAt(history.length()-2)-history.charAt(history.length()-4))==2){
        	int eFile=history.charAt(history.length()-1)-'0';
            //en passant right
            possibility = (P << 1)&p&RANKS[4]&~FILES[0]&FILES[eFile];//shows piece to remove, not the destination
            if (possibility != 0)
            {
                int index=Long.numberOfTrailingZeros(possibility);
                list+=""+(index%8-1)+(index%8)+" E";
            }
            //en passant left
            possibility = (P >> 1)&p&RANKS[4]&~FILES[7]&FILES[eFile];//shows piece to remove, not the destination
            if (possibility != 0)
            {
                int index=Long.numberOfTrailingZeros(possibility);
                list+=""+(index%8+1)+(index%8)+" E";
            }
        }
        return list;
	}

	public String blackPawnMoves(){
        String list="";
        //x1,y1,x2,y2
        long PAWN_MOVES=(p<<7)&WHITE_PIECES&~RANKS[0]&~FILES[7];//capture right
        long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-1)+(index%8+1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(p<<9)&WHITE_PIECES&~RANKS[0]&~FILES[0];//capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-1)+(index%8-1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(p<<8)&EMPTY&~RANKS[0];//move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-1)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(p<<16)&EMPTY&(EMPTY<<8)&RANKS[4];//move 2 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-2)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Promotion Type,"P"
        PAWN_MOVES=(p<<7)&WHITE_PIECES&RANKS[0]&~FILES[7];//pawn promotion by capture right
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"qP"+(index%8+1)+(index%8)+"rP"+(index%8+1)+(index%8)+"bP"+(index%8+1)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(p<<9)&WHITE_PIECES&RANKS[0]&~FILES[0];//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"qP"+(index%8-1)+(index%8)+"rP"+(index%8-1)+(index%8)+"bP"+(index%8-1)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(p<<8)&EMPTY&RANKS[0];//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8)+(index%8)+"qP"+(index%8)+(index%8)+"rP"+(index%8)+(index%8)+"bP"+(index%8)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Space,"E"
        if (history.length()>=4 && history.charAt(history.length()-1)==history.charAt(history.length()-3) && Math.abs(history.charAt(history.length()-2)-history.charAt(history.length()-4))==2){
        	int eFile=history.charAt(history.length()-1)-'0';
            //en passant right
            possibility = (p >> 1)&P&RANKS[3]&~FILES[7]&FILES[eFile];//shows piece to remove, not the destination
            if (possibility != 0)
            {
                int index=Long.numberOfTrailingZeros(possibility);
                list+=""+(index%8+1)+(index%8)+" E";
            }
            //en passant left
            possibility = (p << 1)&P&RANKS[3]&~FILES[0]&FILES[eFile];//shows piece to remove, not the destination
            if (possibility != 0)
            {
                int index=Long.numberOfTrailingZeros(possibility);
                list+=""+(index%8-1)+(index%8)+" E";
            }
        }
        return list;
	}
	
	public String bishopMoves(boolean white, long legal){
		String list = "";
		long locations;
		if (white) locations = B;
		else locations = b;
		long location = locations & ~(locations - 1);
		while (location != 0){
			int indexA = Long.numberOfTrailingZeros(location);
			long moves = diagMoves(indexA) & legal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (indexA/8) + (indexA%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		return list;
	}
	
	public String rookMoves(boolean white, long legal){
		String list = "";
		long locations;
		if (white) locations = R;
		else locations = r;
		long location = locations & ~(locations - 1);
		while (location != 0){
			int indexA = Long.numberOfTrailingZeros(location);
			long moves = linearMoves(indexA) & legal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (indexA/8) + (indexA%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		return list;
	}
	
	public String queenMoves(boolean white, long legal){
		String list = "";
		long locations;
		if (white) locations = Q;
		else locations = q;
		long location = locations & ~(locations - 1);
		while (location != 0){
			int indexA = Long.numberOfTrailingZeros(location);
			long moves = (linearMoves(indexA) | diagMoves(indexA)) & legal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (indexA/8) + (indexA%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		return list;
	}
	
	public String knightMoves(boolean white, long legal){
		String list = "";
		long locations;
		if (white) locations = N;
		else locations = n;
		long location = locations & ~(locations - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			long moves;
			if (index > 18) moves = KNIGHT_SPAN << (index - 18);
			else moves = KNIGHT_SPAN >> (18 - index);
			if (index % 8 < 4) moves &= ~(FILES[6] | FILES[7]) & legal;
			else moves &= ~(FILES[0] | FILES[1]) & legal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (index/8) + (index%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		return list;
	}
	
	public String kingMoves(boolean white, long legal){
		String list = "";
		long location;
		if (white) location = K;
		else location = k;
		int index = Long.numberOfTrailingZeros(location);
		long moves;
		if (index > 9) moves = KING_SPAN << (index - 9);
		else moves = KING_SPAN >> (9 - index);
		if (index % 8 < 4) moves &= ~(FILES[6] | FILES[7]) & legal;
		else moves &= ~(FILES[0] | FILES[1]) & legal;
		long move = moves & ~(moves - 1);
		while (move != 0){
			int indexB = Long.numberOfTrailingZeros(move);
			list += "" + (index/8) + (index%8) + (indexB/8) + (indexB%8);
			moves &= ~move;
			move = moves & ~(moves - 1);
		}
		return list;
	}
	
	public long unsafeBlack(){
		long unsafe;
		OCCUPIED = P|N|B|R|Q|K|p|n|b|r|q|k;
		
		unsafe = ((P >>> 7) & ~FILES[0]);
		unsafe |= ((P >>> 9) & ~FILES[7]);
		
		long moves;
		
		long locations = N;
		long location = locations & ~(locations - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			if (index > 18) moves = KNIGHT_SPAN << (index - 18);
			else moves = KNIGHT_SPAN >> (18 - index);
			if (index % 8 < 4) moves &= ~(FILES[6] | FILES[7]);
			else moves &= ~(FILES[0] | FILES[1]);
			unsafe |= moves;
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		
		locations = Q | B;
		location = locations & ~(locations - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			moves = diagMoves(index);
			unsafe |= moves;
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		
		locations = Q | R;
		location = locations & ~(locations - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			moves = linearMoves(index);
			unsafe |= moves;
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		
		int index = Long.numberOfTrailingZeros(K);
		if (index > 9) moves = KING_SPAN << (index - 9);
		else moves = KING_SPAN >> (9 - index);
		if (index % 8 < 4) moves &= ~(FILES[6] | FILES[7]);
		else moves &= ~(FILES[0] | FILES[1]);
		unsafe |= moves;
		drawBitboard(unsafe);
		return unsafe;
	}
	
	public long unsafeWhite(){
		long unsafe;
		OCCUPIED = P|N|B|R|Q|K|p|n|b|r|q|k;
		
		unsafe = ((p << 7) & ~FILES[7]);
		unsafe |= ((p << 9) & ~FILES[0]);
	
		long moves;
		
		long locations = n;
		long location = locations & ~(locations - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			if (index > 18) moves = KNIGHT_SPAN << (index - 18);
			else moves = KNIGHT_SPAN >> (18 - index);
			if (index % 8 < 4) moves &= ~(FILES[6] | FILES[7]);
			else moves &= ~(FILES[0] | FILES[1]);
			unsafe |= moves;
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		
		locations = q | b;
		location = locations & ~(locations - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			moves = diagMoves(index);
			unsafe |= moves;
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		
		locations = q | r;
		location = locations & ~(locations - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			moves = linearMoves(index);
			unsafe |= moves;
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		
		int index = Long.numberOfTrailingZeros(k);
		if (index > 9) moves = KING_SPAN << (index - 9);
		else moves = KING_SPAN >> (9 - index);
		if (index % 8 < 4) moves &= ~(FILES[6] | FILES[7]);
		else moves &= ~(FILES[0] | FILES[1]);
		unsafe |= moves;
		drawBitboard(unsafe);
		return unsafe;
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
	
	public void drawBitboard(long bitBoard) {
        String chessBoard[][]=new String[8][8];
        for (int i=0;i<64;i++) {
            if (((bitBoard>>>i)&1)==1) chessBoard[i/8][i%8]="X";
            else chessBoard[i/8][i%8]=" ";
        }
        for (int i=0;i<8;i++) {
            System.out.println(Arrays.toString(chessBoard[i]));
        }
    }
	
	public static void main(String[] args) {
		ChessBoard board = new ChessBoard();
		board.whiteMoves();
		System.out.println();
		board.blackMoves();
	}

}
