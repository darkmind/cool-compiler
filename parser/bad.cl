
(*
 *  execute "coolc bad.cl" to see the error messages that the coolc parser
 *  generates
 *
 *  execute "myparser bad.cl" to see the error messages that your parser
 *  generates
 *)

(* no error *)
class A {
};

(* error: class ended prematurely  *)
Class B inherits A {
let x:Int <- 1 in 2
};
a:String <- "class ended already";
};

(* error: a is not a type identifier *)
Class C inherits a {
};

(* error: keyword inherits is misspelled *)
Class D inherts A {
};

(* error: closing brace is missing *)
Class E inherits A {
;

(* error: f is not a type identifier *)
Class f inherits A {
};

(* error: malformed attributes *)
Class G inherits A {
myint1:Int <- 1
myint2:int <- 1;
myint3:Int;
myint4:int;
myint5:integer;

mybool1:Bool <- True;
mybool2:bool <- TrUE
mybool3:bool <- trUE;
mybool4:ool <- fALse;
mybool5:Boolean <- falSe;
mybool6:boolean <- faLsE;
mybool7:Bool <- FALSE;
mybool8:boolean;
mybool8:Bool;
mybool9:Void;
mybool10:Boolean <- void;
mybool11
:             Boolean
<-
"FaLsE"             ;

mystr1:String <- "hello";
mystr2:String <- "no ending semi-colon"
mystr3:String <- "no ending quotes;
mystr4:String <- no quotes at all;
mystr5:String -> "reverse assign operator";
myst			r6:str;
mystr7:string;
mystr8
:
String
<-
"multiple line attribute"
;
mystr9
	:		String 		<- 			"all 				over the place"		;

misc1: <- "no type defined";
}

Class H inherits A {
"no ending semi-colon for this class"
}

(* error: malformed method definitions *)
Class I inherits A {
func1:String{"no parans"};
func2():{"no return type defined"};
func3():String{"no ending semi-colon"}
func4():String{apparently a string};
func5()
:
String
{
"multiple line function"
};
func5():integer{5};
func6():Int{};
func7():Int();
func8():func9():func10():{"nested functions"};
func11   	(  )  		: 	{	"w	h	i	t	e	space"	};
};


