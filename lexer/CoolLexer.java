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
    private int curr_strLen = 0;
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
	private final int BAD_STRING = 3;
	private final int STRING = 2;
	private final int BLOCK_COMMENT = 1;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0,
		46,
		50,
		53
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
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NOT_ACCEPT,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NOT_ACCEPT,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NOT_ACCEPT,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NOT_ACCEPT,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR,
		/* 94 */ YY_NO_ANCHOR,
		/* 95 */ YY_NO_ANCHOR,
		/* 96 */ YY_NO_ANCHOR,
		/* 97 */ YY_NO_ANCHOR,
		/* 98 */ YY_NO_ANCHOR,
		/* 99 */ YY_NO_ANCHOR,
		/* 100 */ YY_NO_ANCHOR,
		/* 101 */ YY_NO_ANCHOR,
		/* 102 */ YY_NO_ANCHOR,
		/* 103 */ YY_NO_ANCHOR,
		/* 104 */ YY_NO_ANCHOR,
		/* 105 */ YY_NO_ANCHOR,
		/* 106 */ YY_NO_ANCHOR,
		/* 107 */ YY_NO_ANCHOR,
		/* 108 */ YY_NO_ANCHOR,
		/* 109 */ YY_NO_ANCHOR,
		/* 110 */ YY_NO_ANCHOR,
		/* 111 */ YY_NO_ANCHOR,
		/* 112 */ YY_NO_ANCHOR,
		/* 113 */ YY_NO_ANCHOR,
		/* 114 */ YY_NO_ANCHOR,
		/* 115 */ YY_NO_ANCHOR,
		/* 116 */ YY_NO_ANCHOR,
		/* 117 */ YY_NO_ANCHOR,
		/* 118 */ YY_NO_ANCHOR,
		/* 119 */ YY_NO_ANCHOR,
		/* 120 */ YY_NO_ANCHOR,
		/* 121 */ YY_NO_ANCHOR,
		/* 122 */ YY_NO_ANCHOR,
		/* 123 */ YY_NO_ANCHOR,
		/* 124 */ YY_NO_ANCHOR,
		/* 125 */ YY_NO_ANCHOR,
		/* 126 */ YY_NO_ANCHOR,
		/* 127 */ YY_NO_ANCHOR,
		/* 128 */ YY_NO_ANCHOR,
		/* 129 */ YY_NO_ANCHOR,
		/* 130 */ YY_NO_ANCHOR,
		/* 131 */ YY_NO_ANCHOR,
		/* 132 */ YY_NO_ANCHOR,
		/* 133 */ YY_NO_ANCHOR,
		/* 134 */ YY_NO_ANCHOR,
		/* 135 */ YY_NO_ANCHOR,
		/* 136 */ YY_NO_ANCHOR,
		/* 137 */ YY_NO_ANCHOR,
		/* 138 */ YY_NO_ANCHOR,
		/* 139 */ YY_NO_ANCHOR,
		/* 140 */ YY_NO_ANCHOR,
		/* 141 */ YY_NO_ANCHOR,
		/* 142 */ YY_NO_ANCHOR,
		/* 143 */ YY_NO_ANCHOR,
		/* 144 */ YY_NO_ANCHOR,
		/* 145 */ YY_NO_ANCHOR,
		/* 146 */ YY_NO_ANCHOR,
		/* 147 */ YY_NO_ANCHOR,
		/* 148 */ YY_NO_ANCHOR,
		/* 149 */ YY_NO_ANCHOR,
		/* 150 */ YY_NO_ANCHOR,
		/* 151 */ YY_NO_ANCHOR,
		/* 152 */ YY_NO_ANCHOR,
		/* 153 */ YY_NO_ANCHOR,
		/* 154 */ YY_NO_ANCHOR,
		/* 155 */ YY_NO_ANCHOR,
		/* 156 */ YY_NO_ANCHOR,
		/* 157 */ YY_NO_ANCHOR,
		/* 158 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"47,43:7,41:2,40,43:2,0,43:18,41,43,46,43:5,44,50,45,43:2,42,43:2,20:10,43:7" +
",21,22,23,24,25,7,22,26,27,22:2,28,22,29,30,31,22,32,33,12,34,35,36,22:3,37" +
",48,37:2,38,37,3,49,1,15,5,6,39,10,8,39:2,2,39,9,14,16,39,11,4,18,19,13,17," +
"39:3,43:5,51:2")[0];

	private int yy_rmap[] = unpackFromString(1,159,
"0,1,2,3:4,4:2,5,4,3:2,4:3,3,4:4,6,4:7,3:12,7,8,9,10,11,12,13,10,14,15,16,17" +
",18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,6:2,35,6:8,4,6:5,36,37," +
"38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,4,56,57,58,59,60,6,61" +
",62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85,4," +
"86,87,88,89,90,91,92,93,94,95,6,96,97,98,99,100,101,102,103")[0];

	private int yy_nxt[][] = unpackFromString(104,52,
"-1,1,69,139,141,143,42,87,47,103,139:2,145,139,51,139,147,148,149,139,2,150" +
":2,151,150,152,150,88,104,105,89,153,150:4,154,3:2,139,4:2,43,3,48,52,5,3:2" +
",139,3,6,-1,139,155,106,139:16,107,106,139:6,155,139:9,107,139,-1:8,139:2,-" +
"1:22,2,-1:84,139:19,107,139:17,107,139,-1:8,139:2,-1:3,139:9,129,139:9,107," +
"139:5,129,139:11,107,139,-1:8,139:2,-1:3,113:39,-1:8,113:2,-1:3,41:39,16,41" +
":10,-1:2,139:2,112,139:4,7,139:11,107,112,139:5,7,139:10,107,139,-1:8,139:2" +
",-1:44,41,-1:54,11,-1:12,36,-1:2,36,-1:8,36,-1:21,37,-1:8,36,-1:3,29:39,30," +
"29:3,44,49,29:5,6,-1,139:3,114,139,8:2,139,9,139:10,107,139:8,9,139:3,114,1" +
"39:4,107,139,-1:8,139:2,-1:52,31,-1:2,32:39,33,32:5,34,35,45,32:2,6,-1,139:" +
"5,10:2,139:12,107,139:17,107,139,-1:8,139:2,-1:52,12,-1:2,38:39,33,38:5,39," +
"40,38:3,6,-1,139:11,13,139:5,13,139,107,139:17,107,139,-1:8,139:2,-1:3,139:" +
"16,14,139:2,107,139:15,14,139,107,139,-1:8,139:2,-1:3,139:11,15,139:5,15,13" +
"9,107,139:17,107,139,-1:8,139:2,-1:3,139:4,17,139:14,107,139:4,17,139:12,10" +
"7,139,-1:8,139:2,-1:3,139:15,18,139:3,107,139:10,18,139:6,107,139,-1:8,139:" +
"2,-1:3,139:4,19,139:14,107,139:4,19,139:12,107,139,-1:8,139:2,-1:3,20,139:1" +
"8,107,139:2,20,139:14,107,139,-1:8,139:2,-1:3,113:8,21,113:19,21,113:10,-1:" +
"8,113:2,-1:3,139,22,139:17,107,139:7,22,139:9,107,139,-1:8,139:2,-1:3,139:4" +
",23,139:14,107,139:4,23,139:12,107,139,-1:8,139:2,-1:3,139:3,24,139:15,107," +
"139:12,24,139:4,107,139,-1:8,139:2,-1:3,139:4,25,139:14,107,139:4,25,139:12" +
",107,139,-1:8,139:2,-1:3,139:4,26,139:14,107,139:4,26,139:12,107,139,-1:8,1" +
"39:2,-1:3,139:14,27,139:4,107,139:3,27,139:13,107,139,-1:8,139:2,-1:3,139:3" +
",28,139:15,107,139:12,28,139:4,107,139,-1:8,139:2,-1:3,139:4,54,139:8,108,1" +
"39:5,107,139:4,54,139:4,108,139:7,107,139,-1:8,139:2,-1:3,113:9,158,113:15," +
"158,113:13,-1:8,113:2,-1:3,113:7,70,113:18,70,113:12,-1:8,113:2,-1:3,113:3," +
"156,113,71:2,113,72,113:19,72,113:3,156,113:6,-1:8,113:2,-1:3,113:5,73:2,11" +
"3:32,-1:8,113:2,-1:3,113:11,74,113:5,74,113:21,-1:8,113:2,-1:3,113:16,75,11" +
"3:18,75,113:3,-1:8,113:2,-1:3,113:11,76,113:5,76,113:21,-1:8,113:2,-1:3,113" +
":4,77,113:19,77,113:14,-1:8,113:2,-1:3,113:15,78,113:14,78,113:8,-1:8,113:2" +
",-1:3,113:4,79,113:19,79,113:14,-1:8,113:2,-1:3,80,113:21,80,113:16,-1:8,11" +
"3:2,-1:3,139:8,81,139:10,107,139:8,81,139:8,107,139,-1:8,139:2,-1:3,113,82," +
"113:25,82,113:11,-1:8,113:2,-1:3,113:3,83,113:28,83,113:6,-1:8,113:2,-1:3,1" +
"13:4,84,113:19,84,113:14,-1:8,113:2,-1:3,113:14,85,113:8,85,113:15,-1:8,113" +
":2,-1:3,113:3,86,113:28,86,113:6,-1:8,113:2,-1:3,139:4,55,139:8,56,139:5,10" +
"7,139:4,55,139:4,56,139:7,107,139,-1:8,139:2,-1:3,113:4,90,113:8,123,113:10" +
",90,113:4,123,113:9,-1:8,113:2,-1:3,113:4,91,113:8,92,113:10,91,113:4,92,11" +
"3:9,-1:8,113:2,-1:3,139:3,57,139:15,107,139:12,57,139:4,107,139,-1:8,139:2," +
"-1:3,139:13,58,139:5,107,139:9,58,139:7,107,139,-1:8,139:2,-1:3,139,126,139" +
":17,107,139:17,107,139,-1:8,139:2,-1:3,139:3,59,139:15,107,139:12,59,139:4," +
"107,139,-1:8,139:2,-1:3,139:2,60,139:16,107,60,139:16,107,139,-1:8,139:2,-1" +
":3,139,127,139:17,107,139:7,127,139:9,107,139,-1:8,139:2,-1:3,139:12,128,13" +
"9:6,107,139:14,128,139:2,107,139,-1:8,139:2,-1:3,113:4,61,113:19,61,113:14," +
"-1:8,113:2,-1:3,139:13,62,139:5,107,139:9,62,139:7,107,139,-1:8,139:2,-1:3," +
"139:7,130,139:11,107,139:6,130,139:10,107,139,-1:8,139:2,-1:3,139:4,97,139:" +
"14,107,139:4,97,139:12,107,139,-1:8,139:2,-1:3,139:18,63,107,139:13,63,139:" +
"3,107,139,-1:8,139:2,-1:3,113:3,93,113:28,93,113:6,-1:8,113:2,-1:3,113:3,95" +
",113:28,95,113:6,-1:8,113:2,-1:3,113:2,96,113:17,96,113:18,-1:8,113:2,-1:3," +
"113:13,94,113:15,94,113:9,-1:8,113:2,-1:3,113:13,98,113:15,98,113:9,-1:8,11" +
"3:2,-1:3,139:3,64,139:15,107,139:12,64,139:4,107,139,-1:8,139:2,-1:3,139:5," +
"107,139:13,107,139:17,107,139,-1:8,139:2,-1:3,139:3,65,139:15,107,139:12,65" +
",139:4,107,139,-1:8,139:2,-1:3,139:13,133,139:5,107,139:9,133,139:7,107,139" +
",-1:8,139:2,-1:3,139:4,134,139:14,107,139:4,134,139:12,107,139,-1:8,139:2,-" +
"1:3,139,66,139:17,107,139:7,66,139:9,107,139,-1:8,139:2,-1:3,113:3,99,113:2" +
"8,99,113:6,-1:8,113:2,-1:3,113,100,113:25,100,113:11,-1:8,113:2,-1:3,139:7," +
"67,139:11,107,139:6,67,139:10,107,139,-1:8,139:2,-1:3,139:10,136,139:8,107," +
"139:11,136,139:5,107,139,-1:8,139:2,-1:3,113:7,101,113:18,101,113:12,-1:8,1" +
"13:2,-1:3,139:7,137,139:11,107,139:6,137,139:10,107,139,-1:8,139:2,-1:3,139" +
":11,68,139:5,68,139,107,139:17,107,139,-1:8,139:2,-1:3,113:11,102,113:5,102" +
",113:21,-1:8,113:2,-1:3,113:2,131,113:17,131,113:18,-1:8,113:2,-1:3,139:4,1" +
"09,139:14,107,139:17,107,139,-1:8,139:2,-1:3,113:7,132,113:18,132,113:12,-1" +
":8,113:2,-1:3,139,110,139,111,139:15,107,139:7,110,139:4,111,139:4,107,139," +
"-1:8,139:2,-1:3,113:13,135,113:15,135,113:9,-1:8,113:2,-1:3,113:9,115,113:1" +
"5,115,113:13,-1:8,113:2,-1:3,113:7,138,113:18,138,113:12,-1:8,113:2,-1:3,13" +
"9:13,116,139:5,107,139:9,116,139:7,107,139,-1:8,139:2,-1:3,139:9,117,139:9," +
"107,139:5,117,139:11,107,139,-1:8,139:2,-1:3,139:9,118,119,139:8,107,139:5," +
"118,139:5,119,139:5,107,139,-1:8,139:2,-1:3,113,140,120,113:17,120,113:6,14" +
"0,113:11,-1:8,113:2,-1:3,113,121,113,122,113:23,121,113:4,122,113:6,-1:8,11" +
"3:2,-1:3,113:13,124,113:15,124,113:9,-1:8,113:2,-1:3,113:9,142,113:15,142,1" +
"13:13,-1:8,113:2,-1:3,139:2,125,139:16,107,125,139:16,107,139,-1:8,139:2,-1" +
":3,113:12,144,113:21,144,113:4,-1:8,113:2,-1:3,113:10,146,113:20,146,113:7," +
"-1:8,113:2,-1:3,113:4,157,113:19,157,113:14,-1:8,113:2,-1:2");

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
	case STRING:
		yybegin(YYINITIAL);
		System.err.println("EOF in string constant");
		return new Symbol(TokenConstants.ERROR, "EOF in string constant");
	case BAD_STRING:
		yybegin(YYINITIAL);
		System.err.println("EOF in bad_string constant");
		return new Symbol(TokenConstants.ERROR, "EOF in string constant");
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
						{ System.err.println("Identifier found: " + yytext()); }
					case -2:
						break;
					case 2:
						{ System.err.println("Integer found: " + yytext()); }
					case -3:
						break;
					case 3:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -4:
						break;
					case 4:
						{ 	
	if(yytext().equals(" ")){
		System.err.println("space");
	} else if(yytext().equals("\n")){
		System.err.println("newline");
		curr_lineno++;
	}
}
					case -5:
						break;
					case 5:
						{ 
	yybegin(STRING); 
	if (string_buf.length() > 0) string_buf.delete(0, string_buf.length());
	System.err.println("string started"); 
	curr_strLen = 0; 
}
					case -6:
						break;
					case 6:
						
					case -7:
						break;
					case 7:
						{ System.err.println("^^^ keyword: fi"); }
					case -8:
						break;
					case 8:
						{ System.err.println("^^^ keyword: if"); }
					case -9:
						break;
					case 9:
						{ System.err.println("^^^ keyword: in"); }
					case -10:
						break;
					case 10:
						{ System.err.println("^^^ keyword: of"); }
					case -11:
						break;
					case 11:
						{ 
	Counter.num_nested_comments++;
	System.err.println("long comment begin(" + Counter.num_nested_comments + "): " + yytext());
	yybegin(BLOCK_COMMENT);
}
					case -12:
						break;
					case 12:
						{
	System.err.println("Unmatched *)");
}
					case -13:
						break;
					case 13:
						{ System.err.println("^^^ keyword: let"); }
					case -14:
						break;
					case 14:
						{ System.err.println("^^^ keyword: new"); }
					case -15:
						break;
					case 15:
						{ System.err.println("^^^ keyword: not"); }
					case -16:
						break;
					case 16:
						{ System.err.println("comment:" + yytext() + "|"); }
					case -17:
						break;
					case 17:
						{ System.err.println("^^^ keyword: case"); }
					case -18:
						break;
					case 18:
						{ System.err.println("^^^ keyword: loop"); }
					case -19:
						break;
					case 19:
						{ System.err.println("^^^ keyword: else"); }
					case -20:
						break;
					case 20:
						{ System.err.println("^^^ keyword: esac"); }
					case -21:
						break;
					case 21:
						{ System.err.println("^^^ keyword: then"); }
					case -22:
						break;
					case 22:
						{ System.err.println("^^^ keyword: pool"); }
					case -23:
						break;
					case 23:
						{ System.err.println("^^^ keyword: true"); }
					case -24:
						break;
					case 24:
						{ System.err.println("^^^ keyword: class"); }
					case -25:
						break;
					case 25:
						{ System.err.println("^^^ keyword: false"); }
					case -26:
						break;
					case 26:
						{ System.err.println("^^^ keyword: while"); }
					case -27:
						break;
					case 27:
						{ System.err.println("^^^ keyword: isvoid"); }
					case -28:
						break;
					case 28:
						{ System.err.println("^^^ keyword: inherits"); }
					case -29:
						break;
					case 29:
						{
	// ignore
}
					case -30:
						break;
					case 30:
						{
	curr_lineno++;
}
					case -31:
						break;
					case 31:
						{
	Counter.num_nested_comments--;
	System.err.println( "long comment end(" + Counter.num_nested_comments + "): " + yytext() + "|"); 
	if (Counter.num_nested_comments == 0) yybegin(YYINITIAL); 
}
					case -32:
						break;
					case 32:
						{
	curr_strLen++;
	string_buf.append(yytext());
	if (curr_strLen >= MAX_STR_CONST) yybegin(BAD_STRING);	
}
					case -33:
						break;
					case 33:
						{
	curr_lineno++;
	System.err.println("error: unescaped new line in string");
	yybegin(YYINITIAL);
}
					case -34:
						break;
					case 34:
						{
	yybegin(YYINITIAL); 
	System.err.println("string ended: " + string_buf.toString());
}
					case -35:
						break;
					case 35:
						{
	yybegin(BAD_STRING);
	return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -36:
						break;
					case 36:
						{
	System.err.println("found " + yytext());
	if (yytext().equals("\n")) string_buf.append('\n');
	if (yytext().equals("\t")) string_buf.append('\t');
	if (yytext().equals("\f")) string_buf.append('\f');
	if (yytext().equals("\b")) string_buf.append('\b');
	curr_strLen++;
	if (curr_strLen >= MAX_STR_CONST) yybegin(BAD_STRING);
}
					case -37:
						break;
					case 37:
						{
	curr_lineno++;
	System.err.println("legal line break");
}
					case -38:
						break;
					case 38:
						{
	// ignore
}
					case -39:
						break;
					case 39:
						{
	yybegin(YYINITIAL);
	System.err.println("long string ended: " + string_buf.toString());
	return new Symbol(TokenConstants.ERROR, "String constant too long");
}
					case -40:
						break;
					case 40:
						{
	return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -41:
						break;
					case 42:
						{ System.err.println("Identifier found: " + yytext()); }
					case -42:
						break;
					case 43:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -43:
						break;
					case 44:
						{
	// ignore
}
					case -44:
						break;
					case 45:
						{
	curr_strLen++;
	string_buf.append(yytext());
	if (curr_strLen >= MAX_STR_CONST) yybegin(BAD_STRING);	
}
					case -45:
						break;
					case 47:
						{ System.err.println("Identifier found: " + yytext()); }
					case -46:
						break;
					case 48:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -47:
						break;
					case 49:
						{
	// ignore
}
					case -48:
						break;
					case 51:
						{ System.err.println("Identifier found: " + yytext()); }
					case -49:
						break;
					case 52:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -50:
						break;
					case 54:
						{ System.err.println("Identifier found: " + yytext()); }
					case -51:
						break;
					case 55:
						{ System.err.println("Identifier found: " + yytext()); }
					case -52:
						break;
					case 56:
						{ System.err.println("Identifier found: " + yytext()); }
					case -53:
						break;
					case 57:
						{ System.err.println("Identifier found: " + yytext()); }
					case -54:
						break;
					case 58:
						{ System.err.println("Identifier found: " + yytext()); }
					case -55:
						break;
					case 59:
						{ System.err.println("Identifier found: " + yytext()); }
					case -56:
						break;
					case 60:
						{ System.err.println("Identifier found: " + yytext()); }
					case -57:
						break;
					case 61:
						{ System.err.println("Identifier found: " + yytext()); }
					case -58:
						break;
					case 62:
						{ System.err.println("Identifier found: " + yytext()); }
					case -59:
						break;
					case 63:
						{ System.err.println("Identifier found: " + yytext()); }
					case -60:
						break;
					case 64:
						{ System.err.println("Identifier found: " + yytext()); }
					case -61:
						break;
					case 65:
						{ System.err.println("Identifier found: " + yytext()); }
					case -62:
						break;
					case 66:
						{ System.err.println("Identifier found: " + yytext()); }
					case -63:
						break;
					case 67:
						{ System.err.println("Identifier found: " + yytext()); }
					case -64:
						break;
					case 68:
						{ System.err.println("Identifier found: " + yytext()); }
					case -65:
						break;
					case 69:
						{ System.err.println("Identifier found: " + yytext()); }
					case -66:
						break;
					case 70:
						{ System.err.println("^^^ keyword: fi"); }
					case -67:
						break;
					case 71:
						{ System.err.println("^^^ keyword: if"); }
					case -68:
						break;
					case 72:
						{ System.err.println("^^^ keyword: in"); }
					case -69:
						break;
					case 73:
						{ System.err.println("^^^ keyword: of"); }
					case -70:
						break;
					case 74:
						{ System.err.println("^^^ keyword: let"); }
					case -71:
						break;
					case 75:
						{ System.err.println("^^^ keyword: new"); }
					case -72:
						break;
					case 76:
						{ System.err.println("^^^ keyword: not"); }
					case -73:
						break;
					case 77:
						{ System.err.println("^^^ keyword: case"); }
					case -74:
						break;
					case 78:
						{ System.err.println("^^^ keyword: loop"); }
					case -75:
						break;
					case 79:
						{ System.err.println("^^^ keyword: else"); }
					case -76:
						break;
					case 80:
						{ System.err.println("^^^ keyword: esac"); }
					case -77:
						break;
					case 81:
						{ System.err.println("^^^ keyword: then"); }
					case -78:
						break;
					case 82:
						{ System.err.println("^^^ keyword: pool"); }
					case -79:
						break;
					case 83:
						{ System.err.println("^^^ keyword: class"); }
					case -80:
						break;
					case 84:
						{ System.err.println("^^^ keyword: while"); }
					case -81:
						break;
					case 85:
						{ System.err.println("^^^ keyword: isvoid"); }
					case -82:
						break;
					case 86:
						{ System.err.println("^^^ keyword: inherits"); }
					case -83:
						break;
					case 87:
						{ System.err.println("Identifier found: " + yytext()); }
					case -84:
						break;
					case 88:
						{ System.err.println("Identifier found: " + yytext()); }
					case -85:
						break;
					case 89:
						{ System.err.println("Identifier found: " + yytext()); }
					case -86:
						break;
					case 90:
						{ System.err.println("Identifier found: " + yytext()); }
					case -87:
						break;
					case 91:
						{ System.err.println("Identifier found: " + yytext()); }
					case -88:
						break;
					case 92:
						{ System.err.println("Identifier found: " + yytext()); }
					case -89:
						break;
					case 93:
						{ System.err.println("Identifier found: " + yytext()); }
					case -90:
						break;
					case 94:
						{ System.err.println("Identifier found: " + yytext()); }
					case -91:
						break;
					case 95:
						{ System.err.println("Identifier found: " + yytext()); }
					case -92:
						break;
					case 96:
						{ System.err.println("Identifier found: " + yytext()); }
					case -93:
						break;
					case 97:
						{ System.err.println("Identifier found: " + yytext()); }
					case -94:
						break;
					case 98:
						{ System.err.println("Identifier found: " + yytext()); }
					case -95:
						break;
					case 99:
						{ System.err.println("Identifier found: " + yytext()); }
					case -96:
						break;
					case 100:
						{ System.err.println("Identifier found: " + yytext()); }
					case -97:
						break;
					case 101:
						{ System.err.println("Identifier found: " + yytext()); }
					case -98:
						break;
					case 102:
						{ System.err.println("Identifier found: " + yytext()); }
					case -99:
						break;
					case 103:
						{ System.err.println("Identifier found: " + yytext()); }
					case -100:
						break;
					case 104:
						{ System.err.println("Identifier found: " + yytext()); }
					case -101:
						break;
					case 105:
						{ System.err.println("Identifier found: " + yytext()); }
					case -102:
						break;
					case 106:
						{ System.err.println("Identifier found: " + yytext()); }
					case -103:
						break;
					case 107:
						{ System.err.println("Identifier found: " + yytext()); }
					case -104:
						break;
					case 108:
						{ System.err.println("Identifier found: " + yytext()); }
					case -105:
						break;
					case 109:
						{ System.err.println("Identifier found: " + yytext()); }
					case -106:
						break;
					case 110:
						{ System.err.println("Identifier found: " + yytext()); }
					case -107:
						break;
					case 111:
						{ System.err.println("Identifier found: " + yytext()); }
					case -108:
						break;
					case 112:
						{ System.err.println("Identifier found: " + yytext()); }
					case -109:
						break;
					case 113:
						{ System.err.println("Identifier found: " + yytext()); }
					case -110:
						break;
					case 114:
						{ System.err.println("Identifier found: " + yytext()); }
					case -111:
						break;
					case 115:
						{ System.err.println("Identifier found: " + yytext()); }
					case -112:
						break;
					case 116:
						{ System.err.println("Identifier found: " + yytext()); }
					case -113:
						break;
					case 117:
						{ System.err.println("Identifier found: " + yytext()); }
					case -114:
						break;
					case 118:
						{ System.err.println("Identifier found: " + yytext()); }
					case -115:
						break;
					case 119:
						{ System.err.println("Identifier found: " + yytext()); }
					case -116:
						break;
					case 120:
						{ System.err.println("Identifier found: " + yytext()); }
					case -117:
						break;
					case 121:
						{ System.err.println("Identifier found: " + yytext()); }
					case -118:
						break;
					case 122:
						{ System.err.println("Identifier found: " + yytext()); }
					case -119:
						break;
					case 123:
						{ System.err.println("Identifier found: " + yytext()); }
					case -120:
						break;
					case 124:
						{ System.err.println("Identifier found: " + yytext()); }
					case -121:
						break;
					case 125:
						{ System.err.println("Identifier found: " + yytext()); }
					case -122:
						break;
					case 126:
						{ System.err.println("Identifier found: " + yytext()); }
					case -123:
						break;
					case 127:
						{ System.err.println("Identifier found: " + yytext()); }
					case -124:
						break;
					case 128:
						{ System.err.println("Identifier found: " + yytext()); }
					case -125:
						break;
					case 129:
						{ System.err.println("Identifier found: " + yytext()); }
					case -126:
						break;
					case 130:
						{ System.err.println("Identifier found: " + yytext()); }
					case -127:
						break;
					case 131:
						{ System.err.println("Identifier found: " + yytext()); }
					case -128:
						break;
					case 132:
						{ System.err.println("Identifier found: " + yytext()); }
					case -129:
						break;
					case 133:
						{ System.err.println("Identifier found: " + yytext()); }
					case -130:
						break;
					case 134:
						{ System.err.println("Identifier found: " + yytext()); }
					case -131:
						break;
					case 135:
						{ System.err.println("Identifier found: " + yytext()); }
					case -132:
						break;
					case 136:
						{ System.err.println("Identifier found: " + yytext()); }
					case -133:
						break;
					case 137:
						{ System.err.println("Identifier found: " + yytext()); }
					case -134:
						break;
					case 138:
						{ System.err.println("Identifier found: " + yytext()); }
					case -135:
						break;
					case 139:
						{ System.err.println("Identifier found: " + yytext()); }
					case -136:
						break;
					case 140:
						{ System.err.println("Identifier found: " + yytext()); }
					case -137:
						break;
					case 141:
						{ System.err.println("Identifier found: " + yytext()); }
					case -138:
						break;
					case 142:
						{ System.err.println("Identifier found: " + yytext()); }
					case -139:
						break;
					case 143:
						{ System.err.println("Identifier found: " + yytext()); }
					case -140:
						break;
					case 144:
						{ System.err.println("Identifier found: " + yytext()); }
					case -141:
						break;
					case 145:
						{ System.err.println("Identifier found: " + yytext()); }
					case -142:
						break;
					case 146:
						{ System.err.println("Identifier found: " + yytext()); }
					case -143:
						break;
					case 147:
						{ System.err.println("Identifier found: " + yytext()); }
					case -144:
						break;
					case 148:
						{ System.err.println("Identifier found: " + yytext()); }
					case -145:
						break;
					case 149:
						{ System.err.println("Identifier found: " + yytext()); }
					case -146:
						break;
					case 150:
						{ System.err.println("Identifier found: " + yytext()); }
					case -147:
						break;
					case 151:
						{ System.err.println("Identifier found: " + yytext()); }
					case -148:
						break;
					case 152:
						{ System.err.println("Identifier found: " + yytext()); }
					case -149:
						break;
					case 153:
						{ System.err.println("Identifier found: " + yytext()); }
					case -150:
						break;
					case 154:
						{ System.err.println("Identifier found: " + yytext()); }
					case -151:
						break;
					case 155:
						{ System.err.println("Identifier found: " + yytext()); }
					case -152:
						break;
					case 156:
						{ System.err.println("Identifier found: " + yytext()); }
					case -153:
						break;
					case 157:
						{ System.err.println("Identifier found: " + yytext()); }
					case -154:
						break;
					case 158:
						{ System.err.println("Identifier found: " + yytext()); }
					case -155:
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
