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
		System.err.println("EOF in comment");
        	break;
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
identifier = {typeIdentifier}|{objectIdentifier}|_

inputChar = [^\r\n]
lineTerminator = \n
whiteSpace = {lineTerminator}|[\ \t\b\012]
inlineComment = "--"{inputChar}*{lineTerminator}

commentBegin = "(*"
commentEnd = "*)"
blockComment = ([^"*"]|"*"[^")"])*{commentEnd}

quotes = "\""
strEscapes = "\n"|"\b"|"\t"|"\f"

everything = .|{lineTerminator}
garbage = .
nestedBlockComment = {blockComment}

keywords = [Cc][Ll][Aa][Ss][Ss]|[Ff][Aa][Ll][Ss][Ee]|[Ff][Ii]|[Ii][Ff]|[Ii][Nn]|[Ii][Nn][Hh][Ee][Rr][Ii][Tt][Ss]|[Ii][Ss][Vv][Oo][Ii][Dd]|[Ll][Ee][Tt]|[Ll][Oo][Oo][Pp]|[Pp][Oo][Oo][Ll]|[Tt][Hh][Ee][Nn]|[Ww][Hh][Ii][Ll][Ee]|[Cc][Aa][Ss][Ee]|[Ee][Ss][Aa][Cc]|[Nn][Ee][Ww]|[Oo][Ff]|[Nn][Oo][Tt]|[Tt][Rr][Uu][Ee]

keywords2 = {whiteSpace}+{keywords}{whiteSpace}+

%state BLOCK_COMMENT
%state STRING
%state BAD_STRING

%%
<YYINITIAL>{keywords}				{ System.err.println("keyword:|" + yytext() + "|"); }

<YYINITIAL>{whiteSpace}
{ 	
	if(yytext().equals(" ")){
		System.err.println("space");
	} else if(yytext().equals("\n")){
		System.err.println("newline");
		curr_lineno++;
	}
}
<YYINITIAL>{keywords2}				{ System.err.println("keyword:|" + yytext() + "|"); }

<YYINITIAL>{inlineComment}			{ System.err.println("comment:" + yytext()); }

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

<STRING, BAD_STRING>{quotes}			
{
	yybegin(YYINITIAL); 
	System.err.println("string ended: " + string_buf.toString());
}

<STRING>{strEscapes}
{
	if (yytext().equals("\n")) string_buf.append('\n');
	if (yytext().equals("\t")) string_buf.append('\t');
	if (yytext().equals("\f")) string_buf.append('\f');
	if (yytext().equals("\b")) string_buf.append('\b');
	curr_strLen++;
	if (curr_strLen >= MAX_STR_CONST) yybegin(BAD_STRING);
}

<STRING>{lineTerminator}
{
	curr_lineno++;
	System.err.println( "error: unescaped new line in string");
	yybegin(YYINITIAL);
}			

<STRING>{garbage}
{
	curr_strLen++;
	string_buf.append(yytext());
	if (curr_strLen >= MAX_STR_CONST) yybegin(BAD_STRING);	
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
