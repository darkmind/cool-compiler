
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
myint1:Int <- 1;
myint2:int <- 1;

mybool1:Boolean <- True;
mybool2:boolean <- True;
mybool3:Boolean <- true;
mybool4:boolean <- true;

mystr1:String <- "hello";
mystr2:String <- "no ending semi-colon"
mystr3:String <- "no ending quotes;
mystr4:String <- no quotes at all;
mystr5:String -> "reverse assign operator";
}
