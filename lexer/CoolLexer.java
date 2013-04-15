/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;
class Counter {
	public static int num_nested_comments = 0;
}


class CoolLexer implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */
    // Max size of string constants
    static int MAX_STR_CONST = 1025;
    // For assembling string constants
    StringBuffer string_buf = new StringBuffer();
    private int curr_lineno = 1;
    int get_curr_lineno() {
	return curr_lineno;
    }
    private AbstractSymbol filename;
    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }
    AbstractSymbol curr_filename() {
	return filename;
    }
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private boolean yy_at_bol;
	private int yy_lexical_state;

	CoolLexer (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	CoolLexer (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private CoolLexer () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */
    // empty for now
	}

	private boolean yy_eof_done = false;
	private final int STRING = 2;
	private final int BLOCK_COMMENT = 1;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		51,
		52
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NOT_ACCEPT,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NOT_ACCEPT,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NOT_ACCEPT,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NOT_ACCEPT,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NOT_ACCEPT,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NOT_ACCEPT,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NOT_ACCEPT,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NOT_ACCEPT,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NOT_ACCEPT,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NOT_ACCEPT,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NOT_ACCEPT,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NOT_ACCEPT,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NOT_ACCEPT,
		/* 39 */ YY_NOT_ACCEPT,
		/* 40 */ YY_NOT_ACCEPT,
		/* 41 */ YY_NOT_ACCEPT,
		/* 42 */ YY_NOT_ACCEPT,
		/* 43 */ YY_NOT_ACCEPT,
		/* 44 */ YY_NOT_ACCEPT,
		/* 45 */ YY_NOT_ACCEPT,
		/* 46 */ YY_NOT_ACCEPT,
		/* 47 */ YY_NOT_ACCEPT,
		/* 48 */ YY_NOT_ACCEPT,
		/* 49 */ YY_NOT_ACCEPT,
		/* 50 */ YY_NOT_ACCEPT,
		/* 51 */ YY_NOT_ACCEPT,
		/* 52 */ YY_NOT_ACCEPT,
		/* 53 */ YY_NOT_ACCEPT,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NOT_ACCEPT,
		/* 56 */ YY_NOT_ACCEPT,
		/* 57 */ YY_NOT_ACCEPT
	};
	private int yy_cmap[] = unpackFromString(1,130,
"21:8,19:2,18,21:2,0,21:18,19,21:7,22,24,23,21:2,20,21:19,3,21,1,14,6,5,21,9" +
",7,21:2,2,21,8,13,15,21,10,4,11,17,12,16,21:9,3,21,1,14,6,5,21,9,7,21:2,2,2" +
"1,8,13,15,21,10,4,11,17,12,16,21:8,25:2")[0];

	private int yy_rmap[] = unpackFromString(1,58,
"0,1,2:9,3,4,5,6,7,2,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,6,2" +
"6,27,28,7,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46")[0];

	private int yy_nxt[][] = unpackFromString(47,26,
"-1,1,12,16:2,19,21,23,54,16:2,25,16,27,16,29,31,16,2:2,33,16,35,37,16,3,-1:" +
"2,11,15,-1:51,55,-1:28,18,-1:6,20,-1:21,57,-1:39,5,-1:6,36,-1:45,10,-1:12,4" +
",-1:17,22,-1:3,4,-1:31,38,-1:16,53,-1:23,39,-1:27,24,4,-1:2,13,-1:29,41,-1:" +
"22,28,30,-1:31,4,-1:14,4,-1:26,42,-1:32,56,-1:29,36,-1:17,32,-1:23,44,-1:38" +
",34,-1:6,34:17,7,34:6,-1:7,4,-1:43,6,-1:16,4,-1:11,4,-1:37,46,-1:20,4,-1:19" +
",4,-1:25,36,-1:27,4,-1:28,48,-1:28,49,-1:29,4,-1:18,50,-1:29,45,-1:15,8:17," +
"9,8:3,14,17,8,3,-1:25,3,-1:3,40,-1:28,26,-1:6,18,-1:16,45,-1:34,43,-1:18,47" +
",-1:19");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */
    switch(yy_lexical_state) {
    	case YYINITIAL:
		/* nothing special to do in the initial state */
		break;
    	case BLOCK_COMMENT:
		yybegin(YYINITIAL);
		System.err.println("EOF in comment");
		return new Symbol(TokenConstants.ERROR, "EOF in comment");
    }
    return new Symbol(TokenConstants.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -2:
						break;
					case 2:
						{ 	
	if(yytext().equals(" ")){
		System.err.println("space");
	} else if(yytext().equals("\n")){
		System.err.println("newline");
	}
}
					case -3:
						break;
					case 3:
						
					case -4:
						break;
					case 4:
						{ System.err.println("############## keyword:|" + yytext() + "|"); }
					case -5:
						break;
					case 5:
						{ 
Counter.num_nested_comments++;
System.err.println("long comment begin(" + Counter.num_nested_comments + "): " + yytext());
yybegin(BLOCK_COMMENT);
}
					case -6:
						break;
					case 6:
						{
System.err.println("Unmatched *)");
}
					case -7:
						break;
					case 7:
						{ System.err.println("comment:" + yytext() + "|"); }
					case -8:
						break;
					case 8:
						{
// ignore
}
					case -9:
						break;
					case 9:
						{
//ignore
}
					case -10:
						break;
					case 10:
						{
Counter.num_nested_comments--;
System.err.println("long comment end(" + Counter.num_nested_comments + "): " + yytext() + "|"); 
if (Counter.num_nested_comments == 0) yybegin(YYINITIAL); 
}
					case -11:
						break;
					case 12:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -12:
						break;
					case 13:
						{ System.err.println("############## keyword:|" + yytext() + "|"); }
					case -13:
						break;
					case 14:
						{
// ignore
}
					case -14:
						break;
					case 16:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -15:
						break;
					case 17:
						{
// ignore
}
					case -16:
						break;
					case 19:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -17:
						break;
					case 21:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -18:
						break;
					case 23:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -19:
						break;
					case 25:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -20:
						break;
					case 27:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -21:
						break;
					case 29:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -22:
						break;
					case 31:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -23:
						break;
					case 33:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -24:
						break;
					case 35:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -25:
						break;
					case 37:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -26:
						break;
					case 54:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -27:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
