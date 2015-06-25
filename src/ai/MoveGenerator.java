package ai;

public class MoveGenerator {

	// Bitboards consisting of all files from left to right
	static final long[] FILES = new long[]{72340172838076673L, 144680345676153346L, 289360691352306692L, 578721382704613384L, 1157442765409226768L, 2314885530818453536L, 4629771061636907072L, -9187201950435737472L};
	// Bitboards consisting of all ranks from bottom to top
	static final long[] RANKS = new long[]{-72057594037927936L, 71776119061217280L, 280375465082880L, 1095216660480L, 4278190080L, 16711680L, 65280L, 255L};
	// Bitboards consisting of all diagonals from top-left to bottom-right
	static final long[] DIAGS = new long[]{0x1L, 0x102L, 0x10204L, 0x1020408L, 0x102040810L, 0x10204081020L, 0x1020408102040L, 0x102040810204080L, 0x204081020408000L, 0x408102040800000L, 0x810204080000000L, 0x1020408000000000L, 0x2040800000000000L, 0x4080000000000000L, 0x8000000000000000L};
	// Bitboards consisting of all anti-diagonals from top-right to bottom-left
	static final long[] ANTIDIAGS = new long[]{0x80L, 0x8040L, 0x804020L, 0x80402010L, 0x8040201008L, 0x804020100804L, 0x80402010080402L, 0x8040201008040201L, 0x4020100804020100L, 0x2010080402010000L, 0x1008040201000000L, 0x804020100000000L, 0x402010000000000L, 0x201000000000000L, 0x100000000000000L};
	static final long KNIGHT_SPAN = 43234889994L, KING_SPAN = 460039L;

	// Makes a specified 'move' onto a board represented by 'data' and returns the new board
	// A move is represented by 4 characters. The first 2 characters correspond to initial location and the final 2 characters correspond to the end location
	// A location is represented by 2 digits. The first being the row and the second being the column (top-left is '00', bottom-right is '77', a1 is '70', h8 is '07')
	// There are some special moves that are represented differently from regular moves (en passants, promotions, castling)
	public static long[] makeMove(String move, long[] data){
		long P = data[0], p = data[1], R = data[2], r = data[3], N = data[4], n = data[5], B = data[6], b = data[7], Q = data[8], q = data[9], K = data[10], k = data[11], epSquare = 0, wkCastle = data[13], wqCastle = data[14], bkCastle = data[15], bqCastle = data[16], whiteToMove = data[17];
		char a = move.charAt(0), bb = move.charAt(1), c = move.charAt(2), d = move.charAt(3);
		int start, end;
		if (Character.isDigit(d)){
			start = (a - 48) * 8 + (bb - 48);
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
				end = Long.numberOfTrailingZeros(FILES[bb - 48] & RANKS[7]);
			} else {
				start = Long.numberOfTrailingZeros(FILES[a - 48] & RANKS[1]);
				end = Long.numberOfTrailingZeros(FILES[bb - 48] & RANKS[0]);
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
				P |= (FILES[bb - 48] & RANKS[5]);
				p &= ~(FILES[bb - 48] & RANKS[4]);
			} else {
				p &= ~(FILES[a - 48] & RANKS[3]);
				p |= (FILES[bb - 48] & RANKS[2]);
				P &= ~(FILES[bb - 48] & RANKS[3]);
			}
		}
		else if (d == 'C'){
			if (a == 'W'){
				if (bb == 'K'){
					K = K << 2;
					R &= ~(1L << 63);
					R |= 1L << 61;
				} else {
					K = K >> 2;
					R &= ~(1L << 56);
					R |= 1L << 59;
				}
				wkCastle = wqCastle = 0;
			} else {
				if (bb == 'K'){
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
		if (underCheck(new long[]{P,p,R,r,N,n,B,b,Q,q,K,k,epSquare,wkCastle, wqCastle, bkCastle, bqCastle, whiteToMove})) return null;
		whiteToMove = 1 - whiteToMove;
		return new long[]{P,p,R,r,N,n,B,b,Q,q,K,k,epSquare,wkCastle, wqCastle, bkCastle, bqCastle, whiteToMove};
	}
	// Gets a list of moves that are possible by the side to move
	// All moves are concatenated together, each move is 4 characters long
	// Does not account for king safety, some of these moves may be invalid
	public static String getMoves(long[] data){
		long P = data[0], p = data[1], R = data[2], r = data[3], N = data[4], n = data[5], B = data[6], b = data[7], Q = data[8], q = data[9], K = data[10], k = data[11], epSquare = data[12], wkCastle = data[13], wqCastle = data[14], bkCastle = data[15], bqCastle = data[16], whiteToMove = data[17];
		if (whiteToMove == 1) return whiteMoves(P,N,B,R,Q,K,p,n,b,r,q,k,epSquare,wkCastle,wqCastle);
		else return blackMoves(P,N,B,R,Q,K,p,n,b,r,q,k,epSquare,bkCastle,bqCastle);
	}
	
	//Everything below here consists of helper methods that are not that important
	public static long linearMoves(int origin, long occupied){
		long binaryOrigin = 1L << origin;
		long horizontal = (occupied - 2 * binaryOrigin) ^ Long.reverse(Long.reverse(occupied) - 2 * Long.reverse(binaryOrigin));
        long vertical = ((occupied & FILES[origin%8]) - (2 * binaryOrigin)) ^ Long.reverse(Long.reverse(occupied & FILES[origin%8]) - (2 * Long.reverse(binaryOrigin)));
        return (horizontal & RANKS[7 - origin/8]) | (vertical & FILES[origin%8]);
	}
	public static long diagMoves(int origin, long occupied){
		long binaryOrigin = 1L << origin;
        long diag = ((occupied & DIAGS[(origin/8) + (origin%8)]) - (2 * binaryOrigin)) ^ Long.reverse(Long.reverse(occupied & DIAGS[(origin/8) + (origin%8)]) - (2 * Long.reverse(binaryOrigin)));
        long antidiag = ((occupied & ANTIDIAGS[(origin/8) + 7 - (origin%8)]) - (2 * binaryOrigin)) ^ Long.reverse(Long.reverse(occupied & ANTIDIAGS[(origin/8) + 7 - (origin%8)]) - (2 * Long.reverse(binaryOrigin)));
        return (diag & DIAGS[(origin/8) + (origin%8)]) | (antidiag & ANTIDIAGS[(origin/8) + 7 - (origin%8)]);
	}
	public static String whiteMoves(long P, long N, long B, long R, long Q, long K, long p, long n, long b, long r, long q, long k, long epSquare, long wkCastle, long wqCastle){
		long whiteLegal = ~(P|R|N|B|Q|K|k), blackPieces = p|r|n|b|q, occupied = P|N|B|R|Q|K|p|n|b|r|q|k, empty = ~occupied;
        
        String list = "";
        long location, moves;
        
        if ((R & (1L<<63)) == 0) wkCastle = 0;
		if ((R & (1L<<56)) == 0) wqCastle = 0;
		if (wkCastle == 1 && ((occupied & ((1L<<61)|(1L<<62))) == 0) && ((unsafeWhite(p,n,b,r,q,k, occupied) & ((1L<<60)|(1L<<61))) == 0)) list += "WK C";
		if (wqCastle == 1 && ((occupied & ((1L<<57)|(1L<<58)|(1L<<59))) == 0) && ((unsafeWhite(p,n,b,r,q,k, occupied) & ((1L<<60)|(1L<<59))) == 0)) list += "WQ C";
        
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
        
		location = B & ~(B - 1);
		while (location != 0){
			int indexA = Long.numberOfTrailingZeros(location);
			moves = diagMoves(indexA, occupied) & whiteLegal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (indexA/8) + (indexA%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			B &= ~location;
			location = B & ~(B - 1);
		}
        
		location = R & ~(R - 1);
		while (location != 0){
			int indexA = Long.numberOfTrailingZeros(location);
			moves = linearMoves(indexA, occupied) & whiteLegal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (indexA/8) + (indexA%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			R &= ~location;
			location = R & ~(R - 1);
		}
		
		location = Q & ~(Q - 1);
		while (location != 0){
			int indexA = Long.numberOfTrailingZeros(location);
			moves = (linearMoves(indexA, occupied) | diagMoves(indexA, occupied)) & whiteLegal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (indexA/8) + (indexA%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			Q &= ~location;
			location = Q & ~(Q - 1);
		}
		
		location = N & ~(N - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			if (index > 18) moves = KNIGHT_SPAN << (index - 18);
			else moves = KNIGHT_SPAN >> (18 - index);
			if (index % 8 < 4) moves &= ~(FILES[6] | FILES[7]) & whiteLegal;
			else moves &= ~(FILES[0] | FILES[1]) & whiteLegal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (index/8) + (index%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			N &= ~location;
			location = N & ~(N - 1);
		}
		
		location = K;
		int index = Long.numberOfTrailingZeros(location);
		if (index > 9) moves = KING_SPAN << (index - 9);
		else moves = KING_SPAN >> (9 - index);
		if (index % 8 < 4) moves &= ~(FILES[6] | FILES[7]) & whiteLegal;
		else moves &= ~(FILES[0] | FILES[1]) & whiteLegal;
		long move = moves & ~(moves - 1);
		while (move != 0){
			int indexB = Long.numberOfTrailingZeros(move);
			list += "" + (index/8) + (index%8) + (indexB/8) + (indexB%8);
			moves &= ~move;
			move = moves & ~(moves - 1);
		}
		
		return list;
	}
	public static String blackMoves(long P, long N, long B, long R, long Q, long K, long p, long n, long b, long r, long q, long k, long epSquare, long bkCastle, long bqCastle){
		long blackLegal = ~(p|r|n|b|q|k|K), whitePieces = P|R|N|B|Q, occupied = P|N|B|R|Q|K|p|n|b|r|q|k, empty = ~occupied;
        
		String list = "";
        long location, moves;
        
        if ((r & (1L<<7)) == 0) bkCastle = 0;
		if ((r & (1L<<0)) == 0) bqCastle = 0;
		if (bkCastle == 1 && ((occupied & ((1L<<5)|(1L<<6))) == 0) && ((unsafeBlack(P,N,B,R,Q,K, occupied) & ((1L<<4)|(1L<<5))) == 0)) list += "BK C";
		if (bqCastle == 1 && ((occupied & ((1L<<1)|(1L<<2)|(1L<<3))) == 0) && ((unsafeBlack(P,N,B,R,Q,K, occupied) & ((1L<<4)|(1L<<3))) == 0)) list += "BQ C";
        
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
      	
      	location = b & ~(b - 1);
		while (location != 0){
			int indexA = Long.numberOfTrailingZeros(location);
			moves = diagMoves(indexA, occupied) & blackLegal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (indexA/8) + (indexA%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			b &= ~location;
			location = b & ~(b - 1);
		}
        
		location = r & ~(r - 1);
		while (location != 0){
			int indexA = Long.numberOfTrailingZeros(location);
			moves = linearMoves(indexA, occupied) & blackLegal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (indexA/8) + (indexA%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			r &= ~location;
			location = r & ~(r - 1);
		}
		
		location = q & ~(q - 1);
		while (location != 0){
			int indexA = Long.numberOfTrailingZeros(location);
			moves = (linearMoves(indexA, occupied) | diagMoves(indexA, occupied)) & blackLegal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (indexA/8) + (indexA%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			q &= ~location;
			location = q & ~(q - 1);
		}
		
		location = n & ~(n - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			if (index > 18) moves = KNIGHT_SPAN << (index - 18);
			else moves = KNIGHT_SPAN >> (18 - index);
			if (index % 8 < 4) moves &= ~(FILES[6] | FILES[7]) & blackLegal;
			else moves &= ~(FILES[0] | FILES[1]) & blackLegal;
			long move = moves & ~(moves - 1);
			while (move != 0){
				int indexB = Long.numberOfTrailingZeros(move);
				list += "" + (index/8) + (index%8) + (indexB/8) + (indexB%8);
				moves &= ~move;
				move = moves & ~(moves - 1);
			}
			n &= ~location;
			location = n & ~(n - 1);
		}
		
		location = k;
		int index = Long.numberOfTrailingZeros(location);
		if (index > 9) moves = KING_SPAN << (index - 9);
		else moves = KING_SPAN >> (9 - index);
		if (index % 8 < 4) moves &= ~(FILES[6] | FILES[7]) & blackLegal;
		else moves &= ~(FILES[0] | FILES[1]) & blackLegal;
		long move = moves & ~(moves - 1);
		while (move != 0){
			int indexB = Long.numberOfTrailingZeros(move);
			list += "" + (index/8) + (index%8) + (indexB/8) + (indexB%8);
			moves &= ~move;
			move = moves & ~(moves - 1);
		}
		
      	return list;
	}
	public static long unsafeBlack(long P, long N, long B, long R, long Q, long K, long occupied){
		long unsafe;
		
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
			moves = diagMoves(index, occupied);
			unsafe |= moves;
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		
		locations = Q | R;
		location = locations & ~(locations - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			moves = linearMoves(index, occupied);
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
		return unsafe;
	}
	public static long unsafeWhite(long p, long n, long b, long r, long q, long k, long occupied){
		long unsafe;
		
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
			moves = diagMoves(index, occupied);
			unsafe |= moves;
			locations &= ~location;
			location = locations & ~(locations - 1);
		}
		
		locations = q | r;
		location = locations & ~(locations - 1);
		while (location != 0){
			int index = Long.numberOfTrailingZeros(location);
			moves = linearMoves(index, occupied);
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
		return unsafe;
	}
	public static boolean underCheck(long[] data){
		long P = data[0], p = data[1], R = data[2], r = data[3], N = data[4], n = data[5], B = data[6], b = data[7], Q = data[8], q = data[9], K = data[10], k = data[11], whiteToMove = data[17];
		long occupied = P|N|B|R|Q|K|p|n|b|r|q|k;
		return (whiteToMove == 1 && (K & unsafeWhite(p,n,b,r,q,k,occupied)) != 0) || (whiteToMove == 0 && (k & unsafeBlack(P,N,B,R,Q,K,occupied)) != 0);
	}
}
