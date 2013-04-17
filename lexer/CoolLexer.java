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
		87,
		91,
		94
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
		/* 94 */ YY_NOT_ACCEPT,
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
		/* 174 */ YY_NO_ANCHOR,
		/* 175 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"61,59:7,40:2,38,59:2,39,59:18,40,59,60,59:5,3,4,16,14,8,11,7,15,37:10,13,12" +
",10,1,2,59,17,41,42,43,44,45,24,42,46,47,42:2,48,42,49,50,51,42,52,53,29,54" +
",55,56,42:3,59,62,59:2,58,59,20,57,18,32,22,23,57,27,25,57:2,19,57,26,31,33" +
",57,28,21,35,36,30,34,57:3,5,59,6,9,59,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,176,
"0,1,2,1,3,1:6,4,5,1:4,6,1,7,8,9,1:7,10:2,11,10,1,10:7,12,10:7,1:12,13,14,15" +
",16,12:2,17,12:8,10,12:5,3,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33," +
"34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58," +
"59,60,61,62,63,10,64,65,66,67,68,69,70,71,72,73,74,75,76,77,78,79,80,81,82," +
"83,84,85,86,87,88,89,90,91,92,93,94,95,96,97,98,10,12,99,100,101,102,103,10" +
"4,105,106,65")[0];

	private int yy_nxt[][] = unpackFromString(107,63,
"1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,123,165:2,167,62,20,88,125," +
"165:2,63,165,92,165,169,171,173,165,21,22,64,22,166:2,168,166,170,166,89,12" +
"4,126,93,172,166:4,174,165,3:2,23,3:2,-1:65,24,-1:76,25,-1:47,26,-1:9,27,-1" +
":62,61,-1:55,28,-1:76,165,175,127,165:3,129,165:4,129,165:7,129,-1:3,127,12" +
"9:6,131,129:8,165,129,-1:22,166:7,65,166:12,-1:3,166:6,65,166:11,-1:41,21,-" +
"1:43,165:6,129,165:4,129,165:7,129,-1:3,129:16,165,129,-1:22,165:6,129,165:" +
"2,157,165,129,165:7,129,-1:3,129:5,157,129:10,165,129,-1:22,166:20,-1:3,166" +
":18,-1:5,61:37,33:2,61:23,-1:18,165:2,139,165:3,129,29,165:3,129,165:7,129," +
"-1:3,139,129:5,29,129:9,165,129,-1:22,166:9,128,166:10,-1:3,166:5,128,166:1" +
"2,-1:42,22,-1:42,166:9,150,166:10,-1:3,166:5,150,166:12,-1:42,50,-1:25,57:3" +
"7,56:2,57:23,-1:38,56,-1:62,59,-1:24,1,49:2,82,49:12,90,49:21,50,83,49:23,-" +
"1:18,165:3,141,165,30:2,165,31,165:2,129,165:7,129,-1:3,129:8,31,129:3,141," +
"129:3,165,129,-1:22,166:3,138,166,66:2,166,67,166:11,-1:3,166:8,67,166:3,13" +
"8,166:5,-1:8,51,-1:58,1,52:37,53,85,52:20,54,55,84,-1:18,165:5,32:2,165:4,1" +
"29,165:7,129,-1:3,129:16,165,129,-1:22,166:5,68:2,166:13,-1:3,166:18,-1:4,1" +
",58:37,59,86,58:20,60,58:2,-1:18,165:6,129,165:4,34,165:5,34,165,129,-1:3,1" +
"29:16,165,129,-1:22,166:11,69,166:5,69,166:2,-1:3,166:18,-1:22,165:6,129,16" +
"5:4,129,165:4,35,165:2,129,-1:3,129:15,35,165,129,-1:22,166:16,70,166:3,-1:" +
"3,166:15,70,166:2,-1:22,165:6,129,165:4,36,165:5,36,165,129,-1:3,129:16,165" +
",129,-1:22,166:11,71,166:5,71,166:2,-1:3,166:18,-1:22,165:4,37,165,129,165:" +
"4,129,165:7,129,-1:3,129:4,37,129:11,165,129,-1:22,166:8,41,166:11,-1:3,166" +
":8,41,166:9,-1:22,165:6,129,165:4,129,165:3,38,165:3,129,-1:3,129:10,38,129" +
":5,165,129,-1:22,166:4,72,166:15,-1:3,166:4,72,166:13,-1:22,165:4,39,165,12" +
"9,165:4,129,165:7,129,-1:3,129:4,39,129:11,165,129,-1:22,166:4,74,166:15,-1" +
":3,166:4,74,166:13,-1:22,40,165:5,129,165:4,129,165:7,129,-1:3,129:2,40,129" +
":13,165,129,-1:22,75,166:19,-1:3,166:2,75,166:15,-1:22,165,42,165:4,129,165" +
":4,129,165:7,129,-1:3,129:7,42,129:8,165,129,-1:22,166:15,73,166:4,-1:3,166" +
":10,73,166:7,-1:22,165:6,129,165,76,165:2,129,165:7,129,-1:3,129:8,76,129:7" +
",165,129,-1:22,166,77,166:18,-1:3,166:7,77,166:10,-1:22,165:4,43,165,129,16" +
"5:4,129,165:7,129,-1:3,129:4,43,129:11,165,129,-1:22,166:3,78,166:16,-1:3,1" +
"66:12,78,166:5,-1:22,165:3,44,165:2,129,165:4,129,165:7,129,-1:3,129:12,44," +
"129:3,165,129,-1:22,166:4,79,166:15,-1:3,166:4,79,166:13,-1:22,165:4,45,165" +
",129,165:4,129,165:7,129,-1:3,129:4,45,129:11,165,129,-1:22,166:14,80,166:5" +
",-1:3,166:3,80,166:14,-1:22,165:4,46,165,129,165:4,129,165:7,129,-1:3,129:4" +
",46,129:11,165,129,-1:22,166:3,81,166:16,-1:3,166:12,81,166:5,-1:22,165:6,1" +
"29,165:4,129,165:2,47,165:4,129,-1:3,129:3,47,129:12,165,129,-1:22,165:3,48" +
",165:2,129,165:4,129,165:7,129,-1:3,129:12,48,129:3,165,129,-1:22,165:4,95," +
"165,129,165:4,129,165,133,165:5,129,-1:3,129:4,95,129:4,133,129:6,165,129,-" +
"1:22,166:4,96,166:8,140,166:6,-1:3,166:4,96,166:4,140,166:8,-1:22,165:4,97," +
"165,129,165:4,129,165,99,165:5,129,-1:3,129:4,97,129:4,99,129:6,165,129,-1:" +
"22,166:4,98,166:8,100,166:6,-1:3,166:4,98,166:4,100,166:8,-1:22,165:3,101,1" +
"65:2,129,165:4,129,165:7,129,-1:3,129:12,101,129:3,165,129,-1:22,166:4,102," +
"166:15,-1:3,166:4,102,166:13,-1:22,166:2,146,166:17,-1:3,146,166:17,-1:22,1" +
"65:2,151,165:3,129,165:4,129,165:7,129,-1:3,151,129:15,165,129,-1:22,166:3," +
"104,166:16,-1:3,166:12,104,166:5,-1:22,165:6,129,165:4,129,165,103,165:5,12" +
"9,-1:3,129:9,103,129:6,165,129,-1:22,166:3,106,166:16,-1:3,166:12,106,166:5" +
",-1:22,165:3,105,165:2,129,165:4,129,165:7,129,-1:3,129:12,105,129:3,165,12" +
"9,-1:22,166:2,108,166:17,-1:3,108,166:17,-1:22,165:2,107,165:3,129,165:4,12" +
"9,165:7,129,-1:3,107,129:15,165,129,-1:22,166:12,148,166:7,-1:3,166:14,148," +
"166:3,-1:22,165,153,165:4,129,165:4,129,165:7,129,-1:3,129:7,153,129:8,165," +
"129,-1:22,166:13,110,166:6,-1:3,166:9,110,166:8,-1:22,165:6,129,165:4,129,1" +
"55,165:6,129,-1:3,129:14,155,129,165,129,-1:22,166:13,112,166:6,-1:3,166:9," +
"112,166:8,-1:22,165:6,129,165:4,129,165,109,165:5,129,-1:3,129:9,109,129:6," +
"165,129,-1:22,166:7,152,166:12,-1:3,166:6,152,166:11,-1:22,165:6,129,159,16" +
"5:3,129,165:7,129,-1:3,129:6,159,129:9,165,129,-1:22,166:3,114,166:16,-1:3," +
"166:12,114,166:5,-1:22,165:4,111,165,129,165:4,129,165:7,129,-1:3,129:4,111" +
",129:11,165,129,-1:22,166:13,154,166:6,-1:3,166:9,154,166:8,-1:22,165:6,129" +
",165:4,129,165:6,113,129,-1:3,129:13,113,129:2,165,129,-1:22,166:4,156,166:" +
"15,-1:3,166:4,156,166:13,-1:22,165:3,115,165:2,129,165:4,129,165:7,129,-1:3" +
",129:12,115,129:3,165,129,-1:22,166,116,166:18,-1:3,166:7,116,166:10,-1:22," +
"165:3,117,165:2,129,165:4,129,165:7,129,-1:3,129:12,117,129:3,165,129,-1:22" +
",166:7,118,166:12,-1:3,166:6,118,166:11,-1:22,165:6,129,165:4,129,165,161,1" +
"65:5,129,-1:3,129:9,161,129:6,165,129,-1:22,166:10,158,166:9,-1:3,166:11,15" +
"8,166:6,-1:22,165:4,162,165,129,165:4,129,165:7,129,-1:3,129:4,162,129:11,1" +
"65,129,-1:22,166:7,160,166:12,-1:3,166:6,160,166:11,-1:22,165,119,165:4,129" +
",165:4,129,165:7,129,-1:3,129:7,119,129:8,165,129,-1:22,166:11,120,166:5,12" +
"0,166:2,-1:3,166:18,-1:22,165:6,129,121,165:3,129,165:7,129,-1:3,129:6,121," +
"129:9,165,129,-1:22,165:6,129,165:3,163,129,165:7,129,-1:3,129:11,163,129:4" +
",165,129,-1:22,165:6,129,164,165:3,129,165:7,129,-1:3,129:6,164,129:9,165,1" +
"29,-1:22,165:6,129,165:4,122,165:5,122,165,129,-1:3,129:16,165,129,-1:22,16" +
"5,135,165,137,165:2,129,165:4,129,165:7,129,-1:3,129:7,135,129:4,137,129:3," +
"165,129,-1:22,166,130,132,166:17,-1:3,132,166:6,130,166:10,-1:22,165:6,129," +
"165:4,129,165,143,165:5,129,-1:3,129:9,143,129:6,165,129,-1:22,166,134,166," +
"136,166:16,-1:3,166:7,134,166:4,136,166:5,-1:22,165:6,129,165:2,145,165,129" +
",165:7,129,-1:3,129:5,145,129:10,165,129,-1:22,166:13,142,166:6,-1:3,166:9," +
"142,166:8,-1:22,165:6,129,165:2,147,149,129,165:7,129,-1:3,129:5,147,129:5," +
"149,129:4,165,129,-1:22,166:9,144,166:10,-1:3,166:5,144,166:12,-1:4");

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
		System.err.println("lines: " + curr_lineno);
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
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -20:
						break;
					case 20:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -21:
						break;
					case 21:
						{
	System.err.println("Integer found: " + yytext());
	AbstractSymbol intSymbol = AbstractTable.inttable.addInt(Integer.parseInt(yytext()));
	return new Symbol(TokenConstants.INT_CONST, intSymbol);
}
					case -22:
						break;
					case 22:
						{ 	
	if(yytext().equals("\n")){
		System.err.println("newline");
		curr_lineno++;
	}
}
					case -23:
						break;
					case 23:
						{ 
	yybegin(STRING); 
	if (string_buf.length() > 0) string_buf.delete(0, string_buf.length());
	System.err.println("string started"); 
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
	Counter.num_nested_comments++;
	System.err.println("long comment begin(" + Counter.num_nested_comments + "): " + yytext());
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
	System.err.println("Unmatched *)");
	return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}
					case -29:
						break;
					case 29:
						{ System.err.println("^^^ keyword: fi"); return new Symbol(TokenConstants.FI); }
					case -30:
						break;
					case 30:
						{ System.err.println("^^^ keyword: if"); return new Symbol(TokenConstants.IF); }
					case -31:
						break;
					case 31:
						{ System.err.println("^^^ keyword: in"); return new Symbol(TokenConstants.IN); }
					case -32:
						break;
					case 32:
						{ System.err.println("^^^ keyword: of"); return new Symbol(TokenConstants.OF); }
					case -33:
						break;
					case 33:
						{ 
	System.err.println("comment:" + yytext() + "|");
	curr_lineno++;
}
					case -34:
						break;
					case 34:
						{ System.err.println("^^^ keyword: let"); return new Symbol(TokenConstants.LET); }
					case -35:
						break;
					case 35:
						{ System.err.println("^^^ keyword: new"); return new Symbol(TokenConstants.NEW); }
					case -36:
						break;
					case 36:
						{ System.err.println("^^^ keyword: not"); return new Symbol(TokenConstants.NOT); }
					case -37:
						break;
					case 37:
						{ System.err.println("^^^ keyword: case"); return new Symbol(TokenConstants.CASE); }
					case -38:
						break;
					case 38:
						{ System.err.println("^^^ keyword: loop"); return new Symbol(TokenConstants.LOOP); }
					case -39:
						break;
					case 39:
						{ System.err.println("^^^ keyword: else"); return new Symbol(TokenConstants.ELSE); }
					case -40:
						break;
					case 40:
						{ System.err.println("^^^ keyword: esac"); return new Symbol(TokenConstants.ESAC); }
					case -41:
						break;
					case 41:
						{ System.err.println("^^^ keyword: then"); return new Symbol(TokenConstants.THEN); }
					case -42:
						break;
					case 42:
						{ System.err.println("^^^ keyword: pool"); return new Symbol(TokenConstants.POOL); }
					case -43:
						break;
					case 43:
						{ System.err.println("^^^ keyword: true"); return new Symbol(TokenConstants.BOOL_CONST, true); }
					case -44:
						break;
					case 44:
						{ System.err.println("^^^ keyword: class"); return new Symbol(TokenConstants.CLASS); }
					case -45:
						break;
					case 45:
						{ System.err.println("^^^ keyword: false"); return new Symbol(TokenConstants.BOOL_CONST, false); }
					case -46:
						break;
					case 46:
						{ System.err.println("^^^ keyword: while"); return new Symbol(TokenConstants.WHILE); }
					case -47:
						break;
					case 47:
						{ System.err.println("^^^ keyword: isvoid"); return new Symbol(TokenConstants.ISVOID); }
					case -48:
						break;
					case 48:
						{ System.err.println("^^^ keyword: inherits"); return new Symbol(TokenConstants.INHERITS); }
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
	Counter.num_nested_comments--;
	System.err.println( "long comment end(" + Counter.num_nested_comments + "): " + yytext() + "|"); 
	if (Counter.num_nested_comments == 0) yybegin(YYINITIAL); 
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
	System.err.println("error: unescaped new line in string");
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}
					case -54:
						break;
					case 54:
						{
	yybegin(YYINITIAL); 
	System.err.println("string ended: " + string_buf.toString());
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
	System.err.println("legal line break");
}
					case -57:
						break;
					case 57:
						{
	System.err.println("found " + yytext());
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
	System.err.println("long string ended: " + string_buf.toString());
}
					case -61:
						break;
					case 62:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -62:
						break;
					case 63:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -63:
						break;
					case 64:
						{ 	
	if(yytext().equals("\n")){
		System.err.println("newline");
		curr_lineno++;
	}
}
					case -64:
						break;
					case 65:
						{ System.err.println("^^^ keyword: fi"); return new Symbol(TokenConstants.FI); }
					case -65:
						break;
					case 66:
						{ System.err.println("^^^ keyword: if"); return new Symbol(TokenConstants.IF); }
					case -66:
						break;
					case 67:
						{ System.err.println("^^^ keyword: in"); return new Symbol(TokenConstants.IN); }
					case -67:
						break;
					case 68:
						{ System.err.println("^^^ keyword: of"); return new Symbol(TokenConstants.OF); }
					case -68:
						break;
					case 69:
						{ System.err.println("^^^ keyword: let"); return new Symbol(TokenConstants.LET); }
					case -69:
						break;
					case 70:
						{ System.err.println("^^^ keyword: new"); return new Symbol(TokenConstants.NEW); }
					case -70:
						break;
					case 71:
						{ System.err.println("^^^ keyword: not"); return new Symbol(TokenConstants.NOT); }
					case -71:
						break;
					case 72:
						{ System.err.println("^^^ keyword: case"); return new Symbol(TokenConstants.CASE); }
					case -72:
						break;
					case 73:
						{ System.err.println("^^^ keyword: loop"); return new Symbol(TokenConstants.LOOP); }
					case -73:
						break;
					case 74:
						{ System.err.println("^^^ keyword: else"); return new Symbol(TokenConstants.ELSE); }
					case -74:
						break;
					case 75:
						{ System.err.println("^^^ keyword: esac"); return new Symbol(TokenConstants.ESAC); }
					case -75:
						break;
					case 76:
						{ System.err.println("^^^ keyword: then"); return new Symbol(TokenConstants.THEN); }
					case -76:
						break;
					case 77:
						{ System.err.println("^^^ keyword: pool"); return new Symbol(TokenConstants.POOL); }
					case -77:
						break;
					case 78:
						{ System.err.println("^^^ keyword: class"); return new Symbol(TokenConstants.CLASS); }
					case -78:
						break;
					case 79:
						{ System.err.println("^^^ keyword: while"); return new Symbol(TokenConstants.WHILE); }
					case -79:
						break;
					case 80:
						{ System.err.println("^^^ keyword: isvoid"); return new Symbol(TokenConstants.ISVOID); }
					case -80:
						break;
					case 81:
						{ System.err.println("^^^ keyword: inherits"); return new Symbol(TokenConstants.INHERITS); }
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
	System.err.println("error: unescaped new line in string");
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
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -87:
						break;
					case 89:
						{
	System.err.println("Identifier found: " + yytext());
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
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -90:
						break;
					case 93:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -91:
						break;
					case 95:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -92:
						break;
					case 96:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -93:
						break;
					case 97:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -94:
						break;
					case 98:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -95:
						break;
					case 99:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -96:
						break;
					case 100:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -97:
						break;
					case 101:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -98:
						break;
					case 102:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -99:
						break;
					case 103:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -100:
						break;
					case 104:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -101:
						break;
					case 105:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -102:
						break;
					case 106:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -103:
						break;
					case 107:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -104:
						break;
					case 108:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -105:
						break;
					case 109:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -106:
						break;
					case 110:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -107:
						break;
					case 111:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -108:
						break;
					case 112:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -109:
						break;
					case 113:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -110:
						break;
					case 114:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -111:
						break;
					case 115:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -112:
						break;
					case 116:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -113:
						break;
					case 117:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -114:
						break;
					case 118:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -115:
						break;
					case 119:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -116:
						break;
					case 120:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -117:
						break;
					case 121:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -118:
						break;
					case 122:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -119:
						break;
					case 123:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -120:
						break;
					case 124:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -121:
						break;
					case 125:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -122:
						break;
					case 126:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -123:
						break;
					case 127:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -124:
						break;
					case 128:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -125:
						break;
					case 129:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -126:
						break;
					case 130:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -127:
						break;
					case 131:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -128:
						break;
					case 132:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -129:
						break;
					case 133:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -130:
						break;
					case 134:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -131:
						break;
					case 135:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -132:
						break;
					case 136:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -133:
						break;
					case 137:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -134:
						break;
					case 138:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -135:
						break;
					case 139:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -136:
						break;
					case 140:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -137:
						break;
					case 141:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -138:
						break;
					case 142:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -139:
						break;
					case 143:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -140:
						break;
					case 144:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -141:
						break;
					case 145:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -142:
						break;
					case 146:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -143:
						break;
					case 147:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -144:
						break;
					case 148:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -145:
						break;
					case 149:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -146:
						break;
					case 150:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -147:
						break;
					case 151:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -148:
						break;
					case 152:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -149:
						break;
					case 153:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -150:
						break;
					case 154:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -151:
						break;
					case 155:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -152:
						break;
					case 156:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -153:
						break;
					case 157:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -154:
						break;
					case 158:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -155:
						break;
					case 159:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -156:
						break;
					case 160:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -157:
						break;
					case 161:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -158:
						break;
					case 162:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -159:
						break;
					case 163:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -160:
						break;
					case 164:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -161:
						break;
					case 165:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -162:
						break;
					case 166:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -163:
						break;
					case 167:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -164:
						break;
					case 168:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -165:
						break;
					case 169:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -166:
						break;
					case 170:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -167:
						break;
					case 171:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -168:
						break;
					case 172:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -169:
						break;
					case 173:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.OBJECTID, stringSymbol);
}
					case -170:
						break;
					case 174:
						{
	System.err.println("Identifier found: " + yytext());
	AbstractSymbol stringSymbol = AbstractTable.idtable.addString(yytext());
	return new Symbol(TokenConstants.TYPEID, stringSymbol);
}
					case -171:
						break;
					case 175:
						{
	System.err.println("Identifier found: " + yytext());
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
