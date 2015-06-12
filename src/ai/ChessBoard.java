package ai;

import java.util.Arrays;

public class ChessBoard {

	long P,p,R,r,N,n,B,b,Q,q,K,k,epSquare;
	final long[] FILES = new long[]{72340172838076673L, 144680345676153346L, 289360691352306692L, 578721382704613384L, 1157442765409226768L, 2314885530818453536L, 4629771061636907072L, -9187201950435737472L};
	final long[] RANKS = new long[]{-72057594037927936L, 71776119061217280L, 280375465082880L, 1095216660480L, 4278190080L, 16711680L, 65280L, 255L};
	final long[] DIAGS = new long[]{0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L, 0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L, 0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L};
	final long[] ANTIDIAGS = new long[]{0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L, 0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L, 0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L};
    final long KNIGHT_SPAN = 43234889994L, KING_SPAN = 460039L;
    long whiteLegal, whitePieces, blackLegal, blackPieces, occupied, empty;
    long wkCastle, wqCastle, bkCastle, bqCastle;
	
	public ChessBoard(){
		P = p = R = r = N = n = B = b = Q = q = K = k = 0;
		initiateBoard();
	}
	public void initiateBoard(){
		epSquare = 0;
		wkCastle = wqCastle = bkCastle = bqCastle = 1;
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
	}
	
	public void makeMove(String move){
		epSquare = 0;
		char a = move.charAt(0), b = move.charAt(1), c = move.charAt(2), d = move.charAt(3);
		int start, end;
		if (Character.isDigit(d)){
			start = (a - 48) * 8 + (b - 48);
			end = (c - 48) * 8 + (d - 48);
			if (((P >>> start) & 1) == 1) {
				P &= ~(1L << start);
				P |= (1L << end);
				if (a - c == 2) epSquare = 1L << end;
			} else P &= ~(1L << end);
			if (((p >>> start) & 1) == 1) {
				p &= ~(1L << start);
				p |= (1L << end);
				if (c - a == 2) epSquare = 1L << end;
			} else p &= ~(1L << end);
			if (((R >>> start) & 1) == 1) {
				R &= ~(1L << start);
				R |= (1L << end);
				if (start == 63) wkCastle = 0;
				else if (start == 56) wqCastle = 0;
			} else R &= ~(1L << end);
			if (((r >>> start) & 1) == 1) {
				r &= ~(1L << start);
				r |= (1L << end);
				if (start == 7) bkCastle = 0;
				else if (start == 0) bqCastle = 0;
			} else r &= ~(1L << end);
			if (((N >>> start) & 1) == 1) {N &= ~(1L << start); N |= (1L << end);} else N &= ~(1L << end);
			if (((n >>> start) & 1) == 1) {n &= ~(1L << start); n |= (1L << end);} else n &= ~(1L << end);
			if (((B >>> start) & 1) == 1) {B &= ~(1L << start); B |= (1L << end);} else B &= ~(1L << end);
			if (((b >>> start) & 1) == 1) {b &= ~(1L << start); b |= (1L << end);} else b &= ~(1L << end);
			if (((Q >>> start) & 1) == 1) {Q &= ~(1L << start); Q |= (1L << end);} else Q &= ~(1L << end);
			if (((q >>> start) & 1) == 1) {q &= ~(1L << start); q |= (1L << end);} else q &= ~(1L << end);
			if (((K >>> start) & 1) == 1) {
				K &= ~(1L << start);
				K |= (1L << end);
				wkCastle = wqCastle = 0;
			}
			if (((k >>> start) & 1) == 1) {
				k &= ~(1L << start);
				k |= (1L << end);
				bkCastle = bqCastle = 0;
			}
		}
		else if (d == 'P'){
			if (Character.isUpperCase(c)){
				start = Long.numberOfTrailingZeros(FILES[a - 48] & RANKS[6]);
				end = Long.numberOfTrailingZeros(FILES[b - 48] & RANKS[7]);
			} else {
				start = Long.numberOfTrailingZeros(FILES[a - 48] & RANKS[1]);
				end = Long.numberOfTrailingZeros(FILES[b - 48] & RANKS[0]);
			}
			P &= ~(1L << start);
			p &= ~(1L << start);
			if (c == 'R') R |= (1L << end); else R &= ~(1L << end);
			if (c == 'r') r |= (1L << end); else r &= ~(1L << end);
			if (c == 'B') B |= (1L << end); else B &= ~(1L << end);
			if (c == 'b') b |= (1L << end); else b &= ~(1L << end);
			if (c == 'N') N |= (1L << end); else N &= ~(1L << end);
			if (c == 'n') n |= (1L << end); else n &= ~(1L << end);
			if (c == 'Q') Q |= (1L << end); else Q &= ~(1L << end);
			if (c == 'q') q |= (1L << end); else q &= ~(1L << end);
		}
		else if (d == 'E'){
			if (c == 'W'){
				P &= ~(FILES[a - 48] & RANKS[4]);
				P |= (FILES[b - 48] & RANKS[5]);
				p &= ~(FILES[b - 48] & RANKS[4]);
			} else {
				p &= ~(FILES[a - 48] & RANKS[3]);
				p |= (FILES[b - 48] & RANKS[2]);
				P &= ~(FILES[b - 48] & RANKS[3]);
			}
		}
		else if (d == 'C'){
			if (a == 'W'){
				if (b == 'K'){
					K = K << 2;
					R &= ~(1L << 63);
					R |= 1L << 61;
				} else {
					K = K >> 2;
					R &= ~(1L << 56);
					R |= 1L << 59;
				}
				wkCastle = wqCastle = 0;
				if (b == 'K'){
					k = k << 2;
					r &= ~(1L << 7);
					r |= 1L << 5;
				} else {
					k = k >> 2;
					r &= ~(1L << 0);
					r |= 1L << 3;
				}
				bkCastle = bqCastle = 0;
			}
		}
		else
			System.out.println("INVALID MOVE");
	}
	
	public long linearMoves(int origin){
		long binaryOrigin = 1L << origin;
		long horizontal = (occupied - 2 * binaryOrigin) ^ Long.reverse(Long.reverse(occupied) - 2 * Long.reverse(binaryOrigin));
        long vertical = ((occupied & FILES[origin%8]) - (2 * binaryOrigin)) ^ Long.reverse(Long.reverse(occupied & FILES[origin%8]) - (2 * Long.reverse(binaryOrigin)));
        return (horizontal & RANKS[7 - origin/8]) | (vertical & FILES[origin%8]);
	}
	public long diagMoves(int origin){
		long binaryOrigin = 1L << origin;
        long diag = ((occupied & DIAGS[(origin/8) + (origin%8)]) - (2 * binaryOrigin)) ^ Long.reverse(Long.reverse(occupied & DIAGS[(origin/8) + (origin%8)]) - (2 * Long.reverse(binaryOrigin)));
        long antidiag = ((occupied & ANTIDIAGS[(origin/8) + 7 - (origin%8)]) - (2 * binaryOrigin)) ^ Long.reverse(Long.reverse(occupied & ANTIDIAGS[(origin/8) + 7 - (origin%8)]) - (2 * Long.reverse(binaryOrigin)));
        return (diag & DIAGS[(origin/8) + (origin%8)]) | (antidiag & ANTIDIAGS[(origin/8) + 7 - (origin%8)]);
	}
	public String whiteMoves(){
		whiteLegal = ~(P|R|N|B|Q|K|k);
		blackPieces = p|r|n|b|q;
		occupied = P|N|B|R|Q|K|p|n|b|r|q|k;
        empty = ~occupied;
        
        String moves = whitePawnMoves() + bishopMoves(true, whiteLegal) + rookMoves(true, whiteLegal) + queenMoves(true, whiteLegal) + knightMoves(true, whiteLegal) + kingMoves(true, whiteLegal) + whiteCastleMoves();
        
        System.out.println("Moves: " + moves);
        System.out.println(moves.length() / 4 + " moves");
		return moves;
	}
	public String blackMoves(){
		blackLegal = ~(p|r|n|b|q|k|K);
		whitePieces = P|R|N|B|Q;
		occupied = P|N|B|R|Q|K|p|n|b|r|q|k;
        empty = ~occupied;
        
        String moves = blackPawnMoves() + bishopMoves(false, blackLegal) + rookMoves(false, blackLegal) + queenMoves(false, blackLegal) + knightMoves(false, blackLegal) + kingMoves(false, blackLegal) + blackCastleMoves();
        
        System.out.println("Moves: " + moves);
        System.out.println(moves.length() / 4 + " moves");
        return moves;
	}
	public String whitePawnMoves(){
        String list="";
        //x1,y1,x2,y2
        long PAWN_MOVES=(P>>7)&blackPieces&~RANKS[7]&~FILES[0];//capture right
        long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8-1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(P>>9)&blackPieces&~RANKS[7]&~FILES[7];//capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8+1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(P>>8)&empty&~RANKS[7];//move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+1)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(P>>16)&empty&(empty>>8)&RANKS[3];//move 2 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8+2)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Promotion Type,"P"
        PAWN_MOVES=(P>>7)&blackPieces&RANKS[7]&~FILES[0];//pawn promotion by capture right
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"QP"+(index%8-1)+(index%8)+"RP"+(index%8-1)+(index%8)+"BP"+(index%8-1)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(P>>9)&blackPieces&RANKS[7]&~FILES[7];//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"QP"+(index%8+1)+(index%8)+"RP"+(index%8+1)+(index%8)+"BP"+(index%8+1)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(P>>8)&empty&RANKS[7];//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8)+(index%8)+"QP"+(index%8)+(index%8)+"RP"+(index%8)+(index%8)+"BP"+(index%8)+(index%8)+"NP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Space,"E"
        //en passant right
	    possibility = (P << 1)&~FILES[0]&epSquare;//shows piece to remove, not the destination
        if (possibility != 0)
        {
        	int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"WE";
        }
        //en passant left
        possibility = (P >> 1)&~FILES[7]&epSquare;//shows piece to remove, not the destination
        if (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"WE";
        }
        return list;
	}
	public String blackPawnMoves(){
        String list="";
        //x1,y1,x2,y2
        long PAWN_MOVES=(p<<7)&whitePieces&~RANKS[0]&~FILES[7];//capture right
        long possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-1)+(index%8+1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(p<<9)&whitePieces&~RANKS[0]&~FILES[0];//capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-1)+(index%8-1)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(p<<8)&empty&~RANKS[0];//move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-1)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(p<<16)&empty&(empty<<8)&RANKS[4];//move 2 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index/8-2)+(index%8)+(index/8)+(index%8);
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Promotion Type,"P"
        PAWN_MOVES=(p<<7)&whitePieces&RANKS[0]&~FILES[7];//pawn promotion by capture right
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8+1)+(index%8)+"qP"+(index%8+1)+(index%8)+"rP"+(index%8+1)+(index%8)+"bP"+(index%8+1)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(p<<9)&whitePieces&RANKS[0]&~FILES[0];//pawn promotion by capture left
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8-1)+(index%8)+"qP"+(index%8-1)+(index%8)+"rP"+(index%8-1)+(index%8)+"bP"+(index%8-1)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        PAWN_MOVES=(p<<8)&empty&RANKS[0];//pawn promotion by move 1 forward
        possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        while (possibility != 0)
        {
            int index=Long.numberOfTrailingZeros(possibility);
            list+=""+(index%8)+(index%8)+"qP"+(index%8)+(index%8)+"rP"+(index%8)+(index%8)+"bP"+(index%8)+(index%8)+"nP";
            PAWN_MOVES&=~possibility;
            possibility=PAWN_MOVES&~(PAWN_MOVES-1);
        }
        //y1,y2,Space,"E"
        //en passant right
        possibility = (p >> 1)&~FILES[7]&epSquare;//shows piece to remove, not the destination
        if (possibility != 0)
        {
        	int index=Long.numberOfTrailingZeros(possibility);
        	list+=""+(index%8+1)+(index%8)+"BE";
      	}
       	//en passant left
      	possibility = (p << 1)&~FILES[0]&epSquare;//shows piece to remove, not the destination
      	if (possibility != 0)
       	{
          	int index=Long.numberOfTrailingZeros(possibility);
         	list+=""+(index%8-1)+(index%8)+"BE";
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
	public String whiteCastleMoves(){
		String list = "";
		if (wkCastle == 1 && ((occupied & ((1L<<61)|(1L<<62))) == 0)) list += "WK C";
		if (wqCastle == 1 && ((occupied & ((1L<<57)|(1L<<58)|(1L<<59))) == 0)) list += "WQ C";
		return list;
	}
	public String blackCastleMoves(){
		String list = "";
		if (bkCastle == 1 && ((occupied & ((1L<<5)|(1L<<6))) == 0)) list += "BK C";
		if (bqCastle == 1 && ((occupied & ((1L<<1)|(1L<<2)|(1L<<3))) == 0)) list += "BQ C";
		return list;
	}
	
	public long unsafeBlack(){
		long unsafe;
		occupied = P|N|B|R|Q|K|p|n|b|r|q|k;
		
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
		occupied = P|N|B|R|Q|K|p|n|b|r|q|k;
		
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
        System.out.println();
        drawBitboard(epSquare);
        System.out.println();
        System.out.println("WK Castle: " + wkCastle);
        System.out.println("WQ Castle: " + wqCastle);
        System.out.println("BK Castle: " + bkCastle);
        System.out.println("BQ Castle: " + bqCastle);
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
	public long[] getGameData(){
		return new long[]{P,p,R,r,N,n,B,b,Q,q,K,k,epSquare,wkCastle,wqCastle,bkCastle,bqCastle};
	}
	public void setGameData(long[] data){
		P = data[0];
		p = data[1];
		R = data[2];
		r = data[3];
		N = data[4];
		n = data[5];
		B = data[6];
		b = data[7];
		Q = data[8];
		q = data[9];
		K = data[10];
		k = data[11];
		epSquare = data[12];
		wkCastle = data[13];
		wqCastle = data[14];
		bkCastle = data[15];
		bqCastle = data[16];
	}
	
	public static void main(String[] args) {
		ChessBoard board = new ChessBoard();
		board.whiteMoves();
		System.out.println();
		//board.makeMove("WK C");
		//board.blackMoves();
		//System.out.println();
		board.drawBoard();
	}
}
