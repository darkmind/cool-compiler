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
		50,
		54,
		57
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
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NOT_ACCEPT,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NOT_ACCEPT,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NOT_ACCEPT,
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
		/* 158 */ YY_NO_ANCHOR,
		/* 159 */ YY_NO_ANCHOR,
		/* 160 */ YY_NO_ANCHOR,
		/* 161 */ YY_NO_ANCHOR,
		/* 162 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"48,44:7,42:2,40,44:2,41,44:18,42,44,47,44:5,45,50,46,44:2,43,44:2,20:10,44:" +
"7,21,22,23,24,25,7,22,26,27,22:2,28,22,29,30,31,22,32,33,12,34,35,36,22:3,3" +
"7,49,37:2,38,37,3,39,1,15,5,6,39,10,8,39:2,2,39,9,14,16,39,11,4,18,19,13,17" +
",39:3,44:5,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,163,
"0,1,2,3,1:3,4:2,5,4,1:2,4:3,1,4:4,6,4:7,1:12,7,8,9,10,11,12,13,14,15,16,17," +
"11,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,6:2,39,6:" +
"8,4,6:5,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,4,60,61" +
",62,63,64,6,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83,84,85," +
"86,87,88,89,4,90,91,92,93,94,95,96,97,98,99,6,100,101,102,103,104,105,106,1" +
"07")[0];

	private int yy_nxt[][] = unpackFromString(108,51,
"1,2,73,143,145,147,42,91,51,107,143:2,149,143,55,143,151,152,153,143,3,154:" +
"2,155,154,156,154,92,108,109,93,157,154:4,158,4:2,143,5,44,5,43,4,52,56,6,4" +
":3,-1:52,143,159,110,143:16,111,110,143:6,159,143:9,111,143,-1:9,143,-1:21," +
"3,-1:31,143:19,111,143:17,111,143,-1:9,143,-1:2,143:9,133,143:9,111,143:5,1" +
"33,143:11,111,143,-1:9,143,-1:2,117:39,-1:9,117,-1:2,41:39,16:2,41:9,-1,143" +
":2,116,143:4,7,143:11,111,116,143:5,7,143:10,111,143,-1:9,143,-1:44,41,-1:4" +
"7,5,-1:56,11,-1:44,30,-1:11,37:39,36:2,37:9,-1:40,36,-1:50,39,-1:10,1,29:39" +
",30,46,29:3,45,53,29:4,-1,143:3,118,143,8:2,143,9,143:10,111,143:8,9,143:3," +
"118,143:4,111,143,-1:9,143,-1:51,31,1,32:39,33,48,32:5,34,35,47,32,-1,143:5" +
",10:2,143:12,111,143:17,111,143,-1:9,143,-1:51,12,1,38:39,39,49,38:5,40,38:" +
"3,-1,143:11,13,143:5,13,143,111,143:17,111,143,-1:9,143,-1:2,143:16,14,143:" +
"2,111,143:15,14,143,111,143,-1:9,143,-1:2,143:11,15,143:5,15,143,111,143:17" +
",111,143,-1:9,143,-1:2,143:4,17,143:14,111,143:4,17,143:12,111,143,-1:9,143" +
",-1:2,143:15,18,143:3,111,143:10,18,143:6,111,143,-1:9,143,-1:2,143:4,19,14" +
"3:14,111,143:4,19,143:12,111,143,-1:9,143,-1:2,20,143:18,111,143:2,20,143:1" +
"4,111,143,-1:9,143,-1:2,117:8,21,117:19,21,117:10,-1:9,117,-1:2,143,22,143:" +
"17,111,143:7,22,143:9,111,143,-1:9,143,-1:2,143:4,23,143:14,111,143:4,23,14" +
"3:12,111,143,-1:9,143,-1:2,143:3,24,143:15,111,143:12,24,143:4,111,143,-1:9" +
",143,-1:2,143:4,25,143:14,111,143:4,25,143:12,111,143,-1:9,143,-1:2,143:4,2" +
"6,143:14,111,143:4,26,143:12,111,143,-1:9,143,-1:2,143:14,27,143:4,111,143:" +
"3,27,143:13,111,143,-1:9,143,-1:2,143:3,28,143:15,111,143:12,28,143:4,111,1" +
"43,-1:9,143,-1:2,143:4,58,143:8,112,143:5,111,143:4,58,143:4,112,143:7,111," +
"143,-1:9,143,-1:2,117:9,162,117:15,162,117:13,-1:9,117,-1:2,117:7,74,117:18" +
",74,117:12,-1:9,117,-1:2,117:3,160,117,75:2,117,76,117:19,76,117:3,160,117:" +
"6,-1:9,117,-1:2,117:5,77:2,117:32,-1:9,117,-1:2,117:11,78,117:5,78,117:21,-" +
"1:9,117,-1:2,117:16,79,117:18,79,117:3,-1:9,117,-1:2,117:11,80,117:5,80,117" +
":21,-1:9,117,-1:2,117:4,81,117:19,81,117:14,-1:9,117,-1:2,117:15,82,117:14," +
"82,117:8,-1:9,117,-1:2,117:4,83,117:19,83,117:14,-1:9,117,-1:2,84,117:21,84" +
",117:16,-1:9,117,-1:2,143:8,85,143:10,111,143:8,85,143:8,111,143,-1:9,143,-" +
"1:2,117,86,117:25,86,117:11,-1:9,117,-1:2,117:3,87,117:28,87,117:6,-1:9,117" +
",-1:2,117:4,88,117:19,88,117:14,-1:9,117,-1:2,117:14,89,117:8,89,117:15,-1:" +
"9,117,-1:2,117:3,90,117:28,90,117:6,-1:9,117,-1:2,143:4,59,143:8,60,143:5,1" +
"11,143:4,59,143:4,60,143:7,111,143,-1:9,143,-1:2,117:4,94,117:8,127,117:10," +
"94,117:4,127,117:9,-1:9,117,-1:2,117:4,95,117:8,96,117:10,95,117:4,96,117:9" +
",-1:9,117,-1:2,143:3,61,143:15,111,143:12,61,143:4,111,143,-1:9,143,-1:2,14" +
"3:13,62,143:5,111,143:9,62,143:7,111,143,-1:9,143,-1:2,143,130,143:17,111,1" +
"43:17,111,143,-1:9,143,-1:2,143:3,63,143:15,111,143:12,63,143:4,111,143,-1:" +
"9,143,-1:2,143:2,64,143:16,111,64,143:16,111,143,-1:9,143,-1:2,143,131,143:" +
"17,111,143:7,131,143:9,111,143,-1:9,143,-1:2,143:12,132,143:6,111,143:14,13" +
"2,143:2,111,143,-1:9,143,-1:2,117:4,65,117:19,65,117:14,-1:9,117,-1:2,143:1" +
"3,66,143:5,111,143:9,66,143:7,111,143,-1:9,143,-1:2,143:7,134,143:11,111,14" +
"3:6,134,143:10,111,143,-1:9,143,-1:2,143:4,101,143:14,111,143:4,101,143:12," +
"111,143,-1:9,143,-1:2,143:18,67,111,143:13,67,143:3,111,143,-1:9,143,-1:2,1" +
"17:3,97,117:28,97,117:6,-1:9,117,-1:2,117:3,99,117:28,99,117:6,-1:9,117,-1:" +
"2,117:2,100,117:17,100,117:18,-1:9,117,-1:2,117:13,98,117:15,98,117:9,-1:9," +
"117,-1:2,117:13,102,117:15,102,117:9,-1:9,117,-1:2,143:3,68,143:15,111,143:" +
"12,68,143:4,111,143,-1:9,143,-1:2,143:5,111,143:13,111,143:17,111,143,-1:9," +
"143,-1:2,143:3,69,143:15,111,143:12,69,143:4,111,143,-1:9,143,-1:2,143:13,1" +
"37,143:5,111,143:9,137,143:7,111,143,-1:9,143,-1:2,143:4,138,143:14,111,143" +
":4,138,143:12,111,143,-1:9,143,-1:2,143,70,143:17,111,143:7,70,143:9,111,14" +
"3,-1:9,143,-1:2,117:3,103,117:28,103,117:6,-1:9,117,-1:2,117,104,117:25,104" +
",117:11,-1:9,117,-1:2,143:7,71,143:11,111,143:6,71,143:10,111,143,-1:9,143," +
"-1:2,143:10,140,143:8,111,143:11,140,143:5,111,143,-1:9,143,-1:2,117:7,105," +
"117:18,105,117:12,-1:9,117,-1:2,143:7,141,143:11,111,143:6,141,143:10,111,1" +
"43,-1:9,143,-1:2,143:11,72,143:5,72,143,111,143:17,111,143,-1:9,143,-1:2,11" +
"7:11,106,117:5,106,117:21,-1:9,117,-1:2,117:2,135,117:17,135,117:18,-1:9,11" +
"7,-1:2,143:4,113,143:14,111,143:17,111,143,-1:9,143,-1:2,117:7,136,117:18,1" +
"36,117:12,-1:9,117,-1:2,143,114,143,115,143:15,111,143:7,114,143:4,115,143:" +
"4,111,143,-1:9,143,-1:2,117:13,139,117:15,139,117:9,-1:9,117,-1:2,117:9,119" +
",117:15,119,117:13,-1:9,117,-1:2,117:7,142,117:18,142,117:12,-1:9,117,-1:2," +
"143:13,120,143:5,111,143:9,120,143:7,111,143,-1:9,143,-1:2,143:9,121,143:9," +
"111,143:5,121,143:11,111,143,-1:9,143,-1:2,143:9,122,123,143:8,111,143:5,12" +
"2,143:5,123,143:5,111,143,-1:9,143,-1:2,117,144,124,117:17,124,117:6,144,11" +
"7:11,-1:9,117,-1:2,117,125,117,126,117:23,125,117:4,126,117:6,-1:9,117,-1:2" +
",117:13,128,117:15,128,117:9,-1:9,117,-1:2,117:9,146,117:15,146,117:13,-1:9" +
",117,-1:2,143:2,129,143:16,111,129,143:16,111,143,-1:9,143,-1:2,117:12,148," +
"117:21,148,117:4,-1:9,117,-1:2,117:10,150,117:20,150,117:7,-1:9,117,-1:2,11" +
"7:4,161,117:19,161,117:14,-1:9,117,-1");

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
						
					case -2:
						break;
					case 2:
						{ System.err.println("Identifier found: " + yytext()); }
					case -3:
						break;
					case 3:
						{ System.err.println("Integer found: " + yytext()); }
					case -4:
						break;
					case 4:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -5:
						break;
					case 5:
						{ 	
	if(yytext().equals(" ")){
		System.err.println("space");
	} else if(yytext().equals("\n")){
		System.err.println("newline");
		curr_lineno++;
	}
}
					case -6:
						break;
					case 6:
						{ 
	yybegin(STRING); 
	if (string_buf.length() > 0) string_buf.delete(0, string_buf.length());
	System.err.println("string started"); 
	curr_strLen = 0; 
}
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
	return new Symbol(TokenConstants.ERROR, "Unmatched *)");
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
	if (curr_strLen >= MAX_STR_CONST) {
		yybegin(BAD_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
					case -33:
						break;
					case 33:
						{
	curr_lineno++;
	System.err.println("error: unescaped new line in string");
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -34:
						break;
					case 34:
						{
	yybegin(YYINITIAL); 
	System.err.println("string ended: " + string_buf.toString());
	AbstractSymbol stringSymbol = AbstractTable.stringtable.addString(string_buf.toString());
	return new Symbol(TokenConstants.STR_CONST, stringSymbol);
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
	curr_lineno++;
	System.err.println("legal line break");
}
					case -37:
						break;
					case 37:
						{
	System.err.println("found " + yytext());
	if (curr_strLen >= MAX_STR_CONST) {
		yybegin(BAD_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
	if (yytext().equals("\n")) {
		System.err.println("in newline case");
		string_buf.append('\n');
	} else if (yytext().equals("\t")) {
		string_buf.append('\t');
	} else if (yytext().equals("\f")) {
		string_buf.append('\f');
	} else if (yytext().equals("\b")) {
		string_buf.append('\b');
	} else {
		System.err.println("here");
		string_buf.append(yytext().substring(1));
	}
	curr_strLen++;
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
	curr_lineno++;
}
					case -40:
						break;
					case 40:
						{
	yybegin(YYINITIAL);
	System.err.println("long string ended: " + string_buf.toString());
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
	if(yytext().equals(" ")){
		System.err.println("space");
	} else if(yytext().equals("\n")){
		System.err.println("newline");
		curr_lineno++;
	}
}
					case -44:
						break;
					case 45:
						{
	// ignore
}
					case -45:
						break;
					case 46:
						{
	curr_lineno++;
}
					case -46:
						break;
					case 47:
						{
	curr_strLen++;
	string_buf.append(yytext());
	if (curr_strLen >= MAX_STR_CONST) {
		yybegin(BAD_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
					case -47:
						break;
					case 48:
						{
	curr_lineno++;
	System.err.println("error: unescaped new line in string");
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -48:
						break;
					case 49:
						{
	yybegin(YYINITIAL);
	curr_lineno++;
}
					case -49:
						break;
					case 51:
						{ System.err.println("Identifier found: " + yytext()); }
					case -50:
						break;
					case 52:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
					case -51:
						break;
					case 53:
						{
	// ignore
}
					case -52:
						break;
					case 55:
						{ System.err.println("Identifier found: " + yytext()); }
					case -53:
						break;
					case 56:
						{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
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
						{ System.err.println("Identifier found: " + yytext()); }
					case -67:
						break;
					case 71:
						{ System.err.println("Identifier found: " + yytext()); }
					case -68:
						break;
					case 72:
						{ System.err.println("Identifier found: " + yytext()); }
					case -69:
						break;
					case 73:
						{ System.err.println("Identifier found: " + yytext()); }
					case -70:
						break;
					case 74:
						{ System.err.println("^^^ keyword: fi"); }
					case -71:
						break;
					case 75:
						{ System.err.println("^^^ keyword: if"); }
					case -72:
						break;
					case 76:
						{ System.err.println("^^^ keyword: in"); }
					case -73:
						break;
					case 77:
						{ System.err.println("^^^ keyword: of"); }
					case -74:
						break;
					case 78:
						{ System.err.println("^^^ keyword: let"); }
					case -75:
						break;
					case 79:
						{ System.err.println("^^^ keyword: new"); }
					case -76:
						break;
					case 80:
						{ System.err.println("^^^ keyword: not"); }
					case -77:
						break;
					case 81:
						{ System.err.println("^^^ keyword: case"); }
					case -78:
						break;
					case 82:
						{ System.err.println("^^^ keyword: loop"); }
					case -79:
						break;
					case 83:
						{ System.err.println("^^^ keyword: else"); }
					case -80:
						break;
					case 84:
						{ System.err.println("^^^ keyword: esac"); }
					case -81:
						break;
					case 85:
						{ System.err.println("^^^ keyword: then"); }
					case -82:
						break;
					case 86:
						{ System.err.println("^^^ keyword: pool"); }
					case -83:
						break;
					case 87:
						{ System.err.println("^^^ keyword: class"); }
					case -84:
						break;
					case 88:
						{ System.err.println("^^^ keyword: while"); }
					case -85:
						break;
					case 89:
						{ System.err.println("^^^ keyword: isvoid"); }
					case -86:
						break;
					case 90:
						{ System.err.println("^^^ keyword: inherits"); }
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
					case 159:
						{ System.err.println("Identifier found: " + yytext()); }
					case -156:
						break;
					case 160:
						{ System.err.println("Identifier found: " + yytext()); }
					case -157:
						break;
					case 161:
						{ System.err.println("Identifier found: " + yytext()); }
					case -158:
						break;
					case 162:
						{ System.err.println("Identifier found: " + yytext()); }
					case -159:
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
