
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
(let x:Int <- 1 in 2)+3
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

blah:Int <- 34;
yo:Boolean <- True;
