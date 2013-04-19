/*
 *  The scanner definition for COOL.
 */
import java_cup.runtime.Symbol;


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
    // For detecting unmatched "(*" symbols
    private int num_nested_comments = 0;
    // For keeping track of line number
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
		61,
		87,
		91
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
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NOT_ACCEPT,
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
		/* 87 */ YY_NOT_ACCEPT,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NOT_ACCEPT,
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
		/* 162 */ YY_NO_ANCHOR,
		/* 163 */ YY_NO_ANCHOR,
		/* 164 */ YY_NO_ANCHOR,
		/* 165 */ YY_NO_ANCHOR,
		/* 166 */ YY_NO_ANCHOR,
		/* 167 */ YY_NO_ANCHOR,
		/* 168 */ YY_NO_ANCHOR,
		/* 169 */ YY_NO_ANCHOR,
		/* 170 */ YY_NO_ANCHOR,
		/* 171 */ YY_NO_ANCHOR,
		/* 172 */ YY_NO_ANCHOR,
		/* 173 */ YY_NO_ANCHOR,
		/* 174 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"61,59:8,58,56,59,58,57,59:18,58,59,60,59:5,3,4,16,14,8,11,7,15,37:10,13,12," +
"10,1,2,59,17,38,39,40,41,42,24,39,43,44,39:2,45,39,46,47,48,39,49,50,29,51," +
"52,53,39:3,59,62,59:2,55,59,20,54,18,32,22,23,54,27,25,54:2,19,54,26,31,33," +
"54,28,21,35,36,30,34,54:3,5,59,6,9,59,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,175,
"0,1,2,1,3,1:6,4,5,1:4,6,1,7,8,9,1:6,10,1,11:2,12,11:8,13,11:7,1:12,14,15,16" +
",17,13:2,18,13:8,11,13:5,3,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34," +
"35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59," +
"60,61,62,63,11,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82,83," +
"84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,11,13,99,100,101,102,103,104,1" +
"05,106,65")[0];

	private int yy_nxt[][] = unpackFromString(107,63,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,122,164:2,166,62,20,88,124," +
"164:2,63,164,92,164,168,170,172,164,21,165:2,167,165,169,165,89,123,125,93," +
"171,165:4,173,164,3,22,64,22,3,23,3:2,-1:65,24,-1:76,25,-1:47,26,-1:9,27,-1" +
":62,28,-1:55,29,-1:76,164,174,126,164:3,128,164:4,128,164:7,128,126,128:6,1" +
"30,128:8,164,128,-1:25,165:7,65,165:18,65,165:11,-1:44,21,-1:26,28:55,-1:2," +
"28:5,-1:18,164:6,128,164:4,128,164:7,128:17,164,128,-1:25,164:6,128,164:2,1" +
"56,164,128,164:7,128:6,156,128:10,164,128,-1:25,165:38,-1:7,1,49:2,82,49:12" +
",90,49:39,50,83,49:5,-1:18,164:2,138,164:3,128,30,164:3,128,164:7,128,138,1" +
"28:5,30,128:9,164,128,-1:25,165:9,127,165:15,127,165:12,-1:63,22,-1:24,165:" +
"9,149,165:15,149,165:12,-1:63,50,-1:7,57:55,56:2,57:5,-1:56,56,-1:62,59,-1:" +
"6,1,52:55,53,85,52:2,54,55,84,-1:18,164:3,140,164,31:2,164,32,164:2,128,164" +
":7,128:9,32,128:3,140,128:3,164,128,-1:25,165:3,137,165,66:2,165,67,165:19," +
"67,165:3,137,165:5,-1:11,51,-1:58,1,58:55,59,86,58:2,60,58:2,-1:18,164:5,33" +
":2,164:4,128,164:7,128:17,164,128,-1:25,165:5,68:2,165:31,-1:25,164:6,128,1" +
"64:4,34,164:5,34,164,128:17,164,128,-1:25,165:11,69,165:5,69,165:20,-1:25,1" +
"64:6,128,164:4,128,164:4,35,164:2,128:16,35,164,128,-1:25,165:16,70,165:18," +
"70,165:2,-1:25,164:6,128,164:4,36,164:5,36,164,128:17,164,128,-1:25,165:11," +
"71,165:5,71,165:20,-1:25,164:4,37,164,128,164:4,128,164:7,128:5,37,128:11,1" +
"64,128,-1:25,165:8,41,165:19,41,165:9,-1:25,164:6,128,164:4,128,164:3,38,16" +
"4:3,128:11,38,128:5,164,128,-1:25,165:4,72,165:19,72,165:13,-1:25,164:4,39," +
"164,128,164:4,128,164:7,128:5,39,128:11,164,128,-1:25,165:4,74,165:19,74,16" +
"5:13,-1:25,40,164:5,128,164:4,128,164:7,128:3,40,128:13,164,128,-1:25,75,16" +
"5:21,75,165:15,-1:25,164,42,164:4,128,164:4,128,164:7,128:8,42,128:8,164,12" +
"8,-1:25,165:15,73,165:14,73,165:7,-1:25,164:6,128,164,76,164:2,128,164:7,12" +
"8:9,76,128:7,164,128,-1:25,165,77,165:25,77,165:10,-1:25,164:4,43,164,128,1" +
"64:4,128,164:7,128:5,43,128:11,164,128,-1:25,165:3,78,165:28,78,165:5,-1:25" +
",164:3,44,164:2,128,164:4,128,164:7,128:13,44,128:3,164,128,-1:25,165:4,79," +
"165:19,79,165:13,-1:25,164:4,45,164,128,164:4,128,164:7,128:5,45,128:11,164" +
",128,-1:25,165:14,80,165:8,80,165:14,-1:25,164:4,46,164,128,164:4,128,164:7" +
",128:5,46,128:11,164,128,-1:25,165:3,81,165:28,81,165:5,-1:25,164:6,128,164" +
":4,128,164:2,47,164:4,128:4,47,128:12,164,128,-1:25,164:3,48,164:2,128,164:" +
"4,128,164:7,128:13,48,128:3,164,128,-1:25,164:4,94,164,128,164:4,128,164,13" +
"2,164:5,128:5,94,128:4,132,128:6,164,128,-1:25,165:4,95,165:8,139,165:10,95" +
",165:4,139,165:8,-1:25,164:4,96,164,128,164:4,128,164,98,164:5,128:5,96,128" +
":4,98,128:6,164,128,-1:25,165:4,97,165:8,99,165:10,97,165:4,99,165:8,-1:25," +
"164:3,100,164:2,128,164:4,128,164:7,128:13,100,128:3,164,128,-1:25,165:4,10" +
"1,165:19,101,165:13,-1:25,165:2,145,165:17,145,165:17,-1:25,164:2,150,164:3" +
",128,164:4,128,164:7,128,150,128:15,164,128,-1:25,165:3,103,165:28,103,165:" +
"5,-1:25,164:6,128,164:4,128,164,102,164:5,128:10,102,128:6,164,128,-1:25,16" +
"5:3,105,165:28,105,165:5,-1:25,164:3,104,164:2,128,164:4,128,164:7,128:13,1" +
"04,128:3,164,128,-1:25,165:2,107,165:17,107,165:17,-1:25,164:2,106,164:3,12" +
"8,164:4,128,164:7,128,106,128:15,164,128,-1:25,165:12,147,165:21,147,165:3," +
"-1:25,164,152,164:4,128,164:4,128,164:7,128:8,152,128:8,164,128,-1:25,165:1" +
"3,109,165:15,109,165:8,-1:25,164:6,128,164:4,128,154,164:6,128:15,154,128,1" +
"64,128,-1:25,165:13,111,165:15,111,165:8,-1:25,164:6,128,164:4,128,164,108," +
"164:5,128:10,108,128:6,164,128,-1:25,165:7,151,165:18,151,165:11,-1:25,164:" +
"6,128,158,164:3,128,164:7,128:7,158,128:9,164,128,-1:25,165:3,113,165:28,11" +
"3,165:5,-1:25,164:4,110,164,128,164:4,128,164:7,128:5,110,128:11,164,128,-1" +
":25,165:13,153,165:15,153,165:8,-1:25,164:6,128,164:4,128,164:6,112,128:14," +
"112,128:2,164,128,-1:25,165:4,155,165:19,155,165:13,-1:25,164:3,114,164:2,1" +
"28,164:4,128,164:7,128:13,114,128:3,164,128,-1:25,165,115,165:25,115,165:10" +
",-1:25,164:3,116,164:2,128,164:4,128,164:7,128:13,116,128:3,164,128,-1:25,1" +
"65:7,117,165:18,117,165:11,-1:25,164:6,128,164:4,128,164,160,164:5,128:10,1" +
"60,128:6,164,128,-1:25,165:10,157,165:20,157,165:6,-1:25,164:4,161,164,128," +
"164:4,128,164:7,128:5,161,128:11,164,128,-1:25,165:7,159,165:18,159,165:11," +
"-1:25,164,118,164:4,128,164:4,128,164:7,128:8,118,128:8,164,128,-1:25,165:1" +
"1,119,165:5,119,165:20,-1:25,164:6,128,120,164:3,128,164:7,128:7,120,128:9," +
"164,128,-1:25,164:6,128,164:3,162,128,164:7,128:12,162,128:4,164,128,-1:25," +
"164:6,128,163,164:3,128,164:7,128:7,163,128:9,164,128,-1:25,164:6,128,164:4" +
",121,164:5,121,164,128:17,164,128,-1:25,164,134,164,136,164:2,128,164:4,128" +
",164:7,128:8,134,128:4,136,128:3,164,128,-1:25,165,129,131,165:17,131,165:6" +
",129,165:10,-1:25,164:6,128,164:4,128,164,142,164:5,128:10,142,128:6,164,12" +
"8,-1:25,165,133,165,135,165:23,133,165:4,135,165:5,-1:25,164:6,128,164:2,14" +
"4,164,128,164:7,128:6,144,128:10,164,128,-1:25,165:13,141,165:15,141,165:8," +
"-1:25,164:6,128,164:2,146,148,128,164:7,128:6,146,128:5,148,128:4,164,128,-" +
"1:25,165:9,143,165:15,143,165:12,-1:7");

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
		return new Symbol(TokenConstants.ERROR, "EOF in comment");
	case STRING:
		yybegin(YYINITIAL);
		return new Symbol(TokenConstants.ERROR, "EOF in string constant");
	case BAD_STRING:
		yybegin(YYINITIAL);
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
						{ return new Symbol(TokenConstants.EQ); }
					case -3:
						break;
					case 3:
						{ return new Symbol(TokenConstants.ERROR, yytext()); }
					case -4:
						break;
					case 4:
						{ return new Symbol(TokenConstants.LPAREN); }
					case -5:
						break;
					case 5:
						{ return new Symbol(TokenConstants.RPAREN); }
					case -6:
						break;
					case 6:
						{ return new Symbol(TokenConstants.LBRACE); }
					case -7:
						break;
					case 7:
						{ return new Symbol(TokenConstants.RBRACE); }
					case -8:
						break;
					case 8:
						{ return new Symbol(TokenConstants.DOT); }
					case -9:
						break;
					case 9:
						{ return new Symbol(TokenConstants.COMMA); }
					case -10:
						break;
					case 10:
						{ return new Symbol(TokenConstants.NEG); }
					case -11:
						break;
					case 11:
						{ return new Symbol(TokenConstants.LT); }
					case -12:
						break;
					case 12:
						{ return new Symbol(TokenConstants.MINUS); }
					case -13:
						break;
					case 13:
						{ return new Symbol(TokenConstants.SEMI); }
					case -14:
						break;
					case 14:
						{ return new Symbol(TokenConstants.COLON); }
					case -15:
						break;
					case 15:
						{ return new Symbol(TokenConstants.PLUS); }
					case -16:
						break;
					case 16:
						{ return new Symbol(TokenConstants.DIV); }
					case -17:
						break;
					case 17:
						{ return new Symbol(TokenConstants.MULT); }
					case -18:
						break;
					case 18:
						{ return new Symbol(TokenConstants.AT); }
					case -19:
						break;
					case 19:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -20:
						break;
					case 20:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -21:
						break;
					case 21:
						{
	AbstractSymbol intSymbol = AbstractTable.inttable.addInt(Integer.parseInt(yytext()));
	return new Symbol(TokenConstants.INT_CONST, intSymbol);
}
					case -22:
						break;
					case 22:
						{ 	
	if(yytext().equals("\n")){
		curr_lineno++;
	}
}
					case -23:
						break;
					case 23:
						{ 
	yybegin(STRING); 
	if (string_buf.length() > 0) string_buf.delete(0, string_buf.length());
	curr_strLen = 0; 
}
					case -24:
						break;
					case 24:
						{ return new Symbol(TokenConstants.DARROW); }
					case -25:
						break;
					case 25:
						{ 
	num_nested_comments++;
	yybegin(BLOCK_COMMENT);
}
					case -26:
						break;
					case 26:
						{ return new Symbol(TokenConstants.LE); }
					case -27:
						break;
					case 27:
						{ return new Symbol(TokenConstants.ASSIGN); }
					case -28:
						break;
					case 28:
						{ 
//	ignore
}
					case -29:
						break;
					case 29:
						{
	return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -30:
						break;
					case 30:
						{ return new Symbol(TokenConstants.FI); }
					case -31:
						break;
					case 31:
						{ return new Symbol(TokenConstants.IF); }
					case -32:
						break;
					case 32:
						{ return new Symbol(TokenConstants.IN); }
					case -33:
						break;
					case 33:
						{ return new Symbol(TokenConstants.OF); }
					case -34:
						break;
					case 34:
						{ return new Symbol(TokenConstants.LET); }
					case -35:
						break;
					case 35:
						{ return new Symbol(TokenConstants.NEW); }
					case -36:
						break;
					case 36:
						{ return new Symbol(TokenConstants.NOT); }
					case -37:
						break;
					case 37:
						{ return new Symbol(TokenConstants.CASE); }
					case -38:
						break;
					case 38:
						{ return new Symbol(TokenConstants.LOOP); }
					case -39:
						break;
					case 39:
						{ return new Symbol(TokenConstants.ELSE); }
					case -40:
						break;
					case 40:
						{ return new Symbol(TokenConstants.ESAC); }
					case -41:
						break;
					case 41:
						{ return new Symbol(TokenConstants.THEN); }
					case -42:
						break;
					case 42:
						{ return new Symbol(TokenConstants.POOL); }
					case -43:
						break;
					case 43:
						{ return new Symbol(TokenConstants.BOOL_CONST, true); }
					case -44:
						break;
					case 44:
						{ return new Symbol(TokenConstants.CLASS); }
					case -45:
						break;
					case 45:
						{ return new Symbol(TokenConstants.BOOL_CONST, false); }
					case -46:
						break;
					case 46:
						{ return new Symbol(TokenConstants.WHILE); }
					case -47:
						break;
					case 47:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -48:
						break;
					case 48:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -49:
						break;
					case 49:
						{
	// ignore
}
					case -50:
						break;
					case 50:
						{
	curr_lineno++;
}
					case -51:
						break;
					case 51:
						{
	num_nested_comments--;
	if (num_nested_comments == 0) yybegin(YYINITIAL); 
}
					case -52:
						break;
					case 52:
						{
	curr_strLen++;
	string_buf.append(yytext());
	if (curr_strLen >= MAX_STR_CONST) {
		yybegin(BAD_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
					case -53:
						break;
					case 53:
						{
	curr_lineno++;
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -54:
						break;
					case 54:
						{
	yybegin(YYINITIAL); 
	AbstractSymbol stringSymbol = AbstractTable.stringtable.addString(string_buf.toString());
	return new Symbol(TokenConstants.STR_CONST, stringSymbol);
}
					case -55:
						break;
					case 55:
						{
	yybegin(BAD_STRING);
	return new Symbol(TokenConstants.ERROR, "String contains null character");
}
					case -56:
						break;
					case 56:
						{
	curr_lineno++;
}
					case -57:
						break;
					case 57:
						{
	if (curr_strLen >= MAX_STR_CONST) {
		yybegin(BAD_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
	if (yytext().equals("\\n")) {
		string_buf.append('\n');
	} else if (yytext().equals("\\t")) {
		string_buf.append('\t');
	} else if (yytext().equals("\\f")) {
		string_buf.append('\f');
	} else if (yytext().equals("\\b")) {
		string_buf.append('\b');
	} else {
		string_buf.append(yytext().substring(1));
	}
	curr_strLen++;
}
					case -58:
						break;
					case 58:
						{
	// ignore
}
					case -59:
						break;
					case 59:
						{
	yybegin(YYINITIAL);
	curr_lineno++;
}
					case -60:
						break;
					case 60:
						{
	yybegin(YYINITIAL);
}
					case -61:
						break;
					case 62:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -62:
						break;
					case 63:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -63:
						break;
					case 64:
						{ 	
	if(yytext().equals("\n")){
		curr_lineno++;
	}
}
					case -64:
						break;
					case 65:
						{ return new Symbol(TokenConstants.FI); }
					case -65:
						break;
					case 66:
						{ return new Symbol(TokenConstants.IF); }
					case -66:
						break;
					case 67:
						{ return new Symbol(TokenConstants.IN); }
					case -67:
						break;
					case 68:
						{ return new Symbol(TokenConstants.OF); }
					case -68:
						break;
					case 69:
						{ return new Symbol(TokenConstants.LET); }
					case -69:
						break;
					case 70:
						{ return new Symbol(TokenConstants.NEW); }
					case -70:
						break;
					case 71:
						{ return new Symbol(TokenConstants.NOT); }
					case -71:
						break;
					case 72:
						{ return new Symbol(TokenConstants.CASE); }
					case -72:
						break;
					case 73:
						{ return new Symbol(TokenConstants.LOOP); }
					case -73:
						break;
					case 74:
						{ return new Symbol(TokenConstants.ELSE); }
					case -74:
						break;
					case 75:
						{ return new Symbol(TokenConstants.ESAC); }
					case -75:
						break;
					case 76:
						{ return new Symbol(TokenConstants.THEN); }
					case -76:
						break;
					case 77:
						{ return new Symbol(TokenConstants.POOL); }
					case -77:
						break;
					case 78:
						{ return new Symbol(TokenConstants.CLASS); }
					case -78:
						break;
					case 79:
						{ return new Symbol(TokenConstants.WHILE); }
					case -79:
						break;
					case 80:
						{ return new Symbol(TokenConstants.ISVOID); }
					case -80:
						break;
					case 81:
						{ return new Symbol(TokenConstants.INHERITS); }
					case -81:
						break;
					case 82:
						{
	// ignore
}
					case -82:
						break;
					case 83:
						{
	curr_lineno++;
}
					case -83:
						break;
					case 84:
						{
	curr_strLen++;
	string_buf.append(yytext());
	if (curr_strLen >= MAX_STR_CONST) {
		yybegin(BAD_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}
					case -84:
						break;
					case 85:
						{
	curr_lineno++;
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -85:
						break;
					case 86:
						{
	yybegin(YYINITIAL);
	curr_lineno++;
}
					case -86:
						break;
					case 88:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -87:
						break;
					case 89:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -88:
						break;
					case 90:
						{
	// ignore
}
					case -89:
						break;
					case 92:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -90:
						break;
					case 93:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -91:
						break;
					case 94:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -92:
						break;
					case 95:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -93:
						break;
					case 96:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -94:
						break;
					case 97:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -95:
						break;
					case 98:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -96:
						break;
					case 99:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -97:
						break;
					case 100:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -98:
						break;
					case 101:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -99:
						break;
					case 102:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -100:
						break;
					case 103:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -101:
						break;
					case 104:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -102:
						break;
					case 105:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -103:
						break;
					case 106:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -104:
						break;
					case 107:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -105:
						break;
					case 108:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -106:
						break;
					case 109:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -107:
						break;
					case 110:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -108:
						break;
					case 111:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -109:
						break;
					case 112:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -110:
						break;
					case 113:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -111:
						break;
					case 114:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -112:
						break;
					case 115:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -113:
						break;
					case 116:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -114:
						break;
					case 117:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -115:
						break;
					case 118:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -116:
						break;
					case 119:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -117:
						break;
					case 120:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -118:
						break;
					case 121:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -119:
						break;
					case 122:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -120:
						break;
					case 123:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -121:
						break;
					case 124:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -122:
						break;
					case 125:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -123:
						break;
					case 126:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -124:
						break;
					case 127:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -125:
						break;
					case 128:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -126:
						break;
					case 129:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -127:
						break;
					case 130:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -128:
						break;
					case 131:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -129:
						break;
					case 132:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -130:
						break;
					case 133:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -131:
						break;
					case 134:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -132:
						break;
					case 135:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -133:
						break;
					case 136:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -134:
						break;
					case 137:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -135:
						break;
					case 138:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -136:
						break;
					case 139:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -137:
						break;
					case 140:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -138:
						break;
					case 141:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -139:
						break;
					case 142:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -140:
						break;
					case 143:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -141:
						break;
					case 144:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -142:
						break;
					case 145:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -143:
						break;
					case 146:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -144:
						break;
					case 147:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -145:
						break;
					case 148:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -146:
						break;
					case 149:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -147:
						break;
					case 150:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -148:
						break;
					case 151:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -149:
						break;
					case 152:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -150:
						break;
					case 153:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -151:
						break;
					case 154:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -152:
						break;
					case 155:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -153:
						break;
					case 156:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -154:
						break;
					case 157:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -155:
						break;
					case 158:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -156:
						break;
					case 159:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -157:
						break;
					case 160:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -158:
						break;
					case 161:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -159:
						break;
					case 162:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -160:
						break;
					case 163:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -161:
						break;
					case 164:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -162:
						break;
					case 165:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -163:
						break;
					case 166:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -164:
						break;
					case 167:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -165:
						break;
					case 168:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -166:
						break;
					case 169:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -167:
						break;
					case 170:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -168:
						break;
					case 171:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -169:
						break;
					case 172:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -170:
						break;
					case 173:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -171:
						break;
					case 174:
						{
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -172:
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
