/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

class Counter {
	public static int num_nested_comments = 0;
}

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
%eofval}

%class CoolLexer
%cup

digit = [0-9]
integer = {digit}+
lower = [a-z]
upper = [A-Z]
anyChar = [a-zA-z]

typeIdentifier = {upper}({anyChar}|{digit}|_)*
objectIdentifier = {lower}({anyChar}|{digit}|_)*
identifier = {typeIdentifier}|{objectIdentifier}|self|SELF_TYPE

inputChar = [^\r\n]
lineTerminator = [\n\r]|(\r\n)
whiteSpace = {lineTerminator}|[\ \t\b\012]
inlineComment = "--"{inputChar}*{lineTerminator}

commentBegin = "(*"
commentEnd = "*)"
blockComment = ([^"*"]|"*"[^")"])*{commentEnd}

quotes = "\""
strEscapes = [\\]{garbage}
legalLineBreak = [\\]{lineTerminator}

everything = .|{lineTerminator}
garbage = .
nestedBlockComment = {blockComment}

classKeyword = [Cc][Ll][Aa][Ss][Ss]
elseKeyword = [Ee][Ll][Ss][Ee]
falseKeyword = [f][Aa][Ll][Ss][Ee]
fiKeyword = [Ff][Ii]
ifKeyword = [Ii][Ff]
inKeyword = [Ii][Nn]
inheritsKeyword = [Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss]
isvoidKeyword = [Ii][Ss][Vv][Oo][Ii][Dd]
letKeyword = [Ll][Ee][Tt]
loopKeyword = [Ll][Oo][Oo][Pp]
poolKeyword = [Pp][Oo][Oo][Ll]
thenKeyword = [Tt][Hh][Ee][Nn]
whileKeyword = [Ww][Hh][Ii][Ll][Ee]
caseKeyword = [Cc][Aa][Ss][Ee]
esacKeyword = [Ee][Ss][Aa][Cc]
newKeyword = [Nn][Ee][Ww]
ofKeyword = [Oo][Ff]
notKeyword = [Nn][Oo][Tt]
trueKeyword = [t][Rr][Uu][Ee]

syntacticSymbols = "("|")"|"{"|"}"|"."|"<-"|";"|":"|"+"|"-"|"/"|"*"|"="|"<"|"<="

%state BLOCK_COMMENT
%state STRING
%state BAD_STRING

%%
<YYINITIAL>{classKeyword}			{ System.err.println("^^^ keyword: class"); }
<YYINITIAL>{elseKeyword}			{ System.err.println("^^^ keyword: else"); }
<YYINITIAL>{falseKeyword}			{ System.err.println("^^^ keyword: false"); }
<YYINITIAL>{fiKeyword}				{ System.err.println("^^^ keyword: fi"); }
<YYINITIAL>{ifKeyword}				{ System.err.println("^^^ keyword: if"); }
<YYINITIAL>{inKeyword}				{ System.err.println("^^^ keyword: in"); }
<YYINITIAL>{inheritsKeyword}			{ System.err.println("^^^ keyword: inherits"); }
<YYINITIAL>{isvoidKeyword}			{ System.err.println("^^^ keyword: isvoid"); }
<YYINITIAL>{letKeyword}				{ System.err.println("^^^ keyword: let"); }
<YYINITIAL>{loopKeyword}			{ System.err.println("^^^ keyword: loop"); }
<YYINITIAL>{poolKeyword}			{ System.err.println("^^^ keyword: pool"); }
<YYINITIAL>{thenKeyword}			{ System.err.println("^^^ keyword: then"); }
<YYINITIAL>{whileKeyword}			{ System.err.println("^^^ keyword: while"); }
<YYINITIAL>{caseKeyword}			{ System.err.println("^^^ keyword: case"); }
<YYINITIAL>{esacKeyword}			{ System.err.println("^^^ keyword: esac"); }
<YYINITIAL>{newKeyword}				{ System.err.println("^^^ keyword: new"); }
<YYINITIAL>{ofKeyword}				{ System.err.println("^^^ keyword: of"); }
<YYINITIAL>{notKeyword}				{ System.err.println("^^^ keyword: not"); }
<YYINITIAL>{trueKeyword}			{ System.err.println("^^^ keyword: true"); }

<YYINITIAL>{integer}				{ System.err.println("Integer found: " + yytext()); }

<YYINITIAL>{identifier}				{ System.err.println("Identifier found: " + yytext()); }

<YYINITIAL>{whiteSpace}
{ 	
	if(yytext().equals(" ")){
		System.err.println("space");
	} else if(yytext().equals("\n")){
		System.err.println("newline");
		curr_lineno++;
	}
}
<YYINITIAL>{inlineComment}			{ System.err.println("comment:" + yytext() + "|"); }

<YYINITIAL, BLOCK_COMMENT>{commentBegin}			
{ 
	Counter.num_nested_comments++;
	System.err.println("long comment begin(" + Counter.num_nested_comments + "): " + yytext());
	yybegin(BLOCK_COMMENT);
}

<YYINITIAL>{quotes} 				
{ 
	yybegin(STRING); 
	if (string_buf.length() > 0) string_buf.delete(0, string_buf.length());
	System.err.println("string started"); 
	curr_strLen = 0; 
}

<STRING>{quotes}			
{
	yybegin(YYINITIAL); 
	System.err.println("string ended: " + string_buf.toString());
	AbstractSymbol stringSymbol = AbstractTable.stringtable.addString(string_buf.toString());
	return new Symbol(TokenConstants.STR_CONST, stringSymbol);
}

<BAD_STRING>{quotes}
{
	yybegin(YYINITIAL);
	System.err.println("long string ended: " + string_buf.toString());
}

<BAD_STRING>{lineTerminator}
{
	yybegin(YYINITIAL);
	curr_lineno++;
}

<BAD_STRING>{garbage}
{
	// ignore
}

<STRING>[\0]
{
	yybegin(BAD_STRING);
	return new Symbol(TokenConstants.ERROR, "String contains null character");
}

<STRING>{strEscapes}
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

<STRING>{legalLineBreak}
{
	curr_lineno++;
	System.err.println("legal line break");
}

<STRING>{lineTerminator}
{
	curr_lineno++;
	System.err.println("error: unescaped new line in string");
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "Unterminated string constant");
}

<STRING>{garbage}
{
	curr_strLen++;
	string_buf.append(yytext());
	if (curr_strLen >= MAX_STR_CONST) {
		yybegin(BAD_STRING);
		return new Symbol(TokenConstants.ERROR, "String constant too long");
	}
}

<BLOCK_COMMENT>{commentEnd}
{
	Counter.num_nested_comments--;
	System.err.println( "long comment end(" + Counter.num_nested_comments + "): " + yytext() + "|"); 
	if (Counter.num_nested_comments == 0) yybegin(YYINITIAL); 
}

<YYINITIAL>{commentEnd}
{
	System.err.println("Unmatched *)");
	return new Symbol(TokenConstants.ERROR, "Unmatched *)");
}

<BLOCK_COMMENT>{lineTerminator}
{
	curr_lineno++;
}

<YYINITIAL>{garbage}               		{ System.err.println("LEXER BUG - UNMATCHED: " + yytext()); }

<BLOCK_COMMENT>{garbage}
{
	// ignore
}
