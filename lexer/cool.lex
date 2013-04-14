/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

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
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

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
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup

digit = [0-9]
lower = [a-z]
upper = [A-Z]
inputChar = [^\r\n]
lineTerminator = \n
whiteSpace = {lineTerminator}|[\ \t\b\012]
inlineComment = "--"{inputChar}*{lineTerminator}
commentBegin = "(*"
commentEnd = "*)"
everything = .|{lineTerminator}
keywords = "class"|"else"|"false"|"fi"|"if"|"in"|"inherits"|"isvoid"|"let"|"loop"|"pool"|"then"|"while"|"case"|"esac"|"new"|"of"|"not"|"true"
keywords2 = {whiteSpace}+{keywords}{whiteSpace}+

%state BLOCK_COMMENT
%state STRING

%%
{whiteSpace}
{ 	
	if(yytext().equals(" ")){
		System.err.println("space");
	} else if(yytext().equals("\n")){
		System.err.println("newline");
	}
}
<YYINITIAL>{keywords2}				{ System.err.println("keyword:|" + yytext() + "|"); }
<YYINITIAL>{inlineComment}			{ System.err.println("comment:" + yytext()); }
<YYINITIAL>{commentBegin}			{ System.err.println("long comment begin"); yybegin(BLOCK_COMMENT); }
<BLOCK_COMMENT>{everything}*{commentEnd}	{ System.err.println("long comment end" + yytext()); yybegin(YYINITIAL); }
.               				{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }
