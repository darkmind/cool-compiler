(* while loop pool tests *)

class Loops inherits IO {
    
    (* Control *)

    main() : SELF_TYPE {
	while asdf loop asdf pool
    };

    main() : SELF_TYPE {
	while isvoid a + 2 loop ((not isvoid ~~~~~~~~~~(a <- 3)))  pool
    };

    (* Parens added *)
    main() : SELF_TYPE {
	while isvoid (a + 2) loop ((not isvoid ~~~~~~~~~~(a <- 3)))  pool
    };
    
    (* Missing first expr *)
    main() : SELF_TYPE {
	while loop asdf pool
    };

    (* Missing second expr *)
    main() : Foo {
	while asdf loop pool
    };

    (* Missing both *)
    main() : FOO {
	while loop pool
    };

    (* Not an expr *)
    main() : Foo {
	while object : TYPE loop object pool
    };
};

(* Bad class to test classlist *)
class lowercase inherits ThisShouldNotWork {

    feat1() : Foo {
	-- Empty feature is bad!
    };

    Feat2() : wrongCases {
	PlaceholderTypeID
    };


};

(* testing the feature list *)
class Foo inherits BadFeatures {

    feature3: a ;

    feat1() : Foo {
	-- Empty feature is bad!
    };

    Feat2() : wrongCases {
	PlaceholderTypeID
    };

    feat4() : Foo {
	-- Another empty feature!

    };


};


CLaSs Blocks inherits Loops {

    (* Control *)
    main() : Foo {
	asdfasdfasd
    };

    (* Added some blocks *)
    main() : SELF_TYPE {
	{
	{
	{
	  {a + 2; };
	  while isvoid ({a + 2; }) loop ((not isvoid ~~~~~~~~~~(a <- 3)))  pool;
	};
	};
	}
    };

    (* Extra semicolon *)
    main() : SELF_TYPE {
	{
	{
	{
	  {a + 2; };
	  while isvoid ({a + 2; }) loop ((not isvoid ~~~~~~~~~~(a <- 3)))  pool;
	};
	};
	} -- HERE
    };

    (* Error recovery *)

    bar(x:Int;y:Int):String { 
	{
	    if true then 4 fi; 
	    INVALIDasdfasdfasd
	} 
    };

   (* Empty block *)
    main() : S {
	{
	    {};
	    {a + 2;};
	    {b;};
	}
    };

};

class UhOhEmptyCommaList inherits IO {
    mystr:String <- in_string();
    myint:Int <- in_int();
};

class CaSes inherits Main {
    
    (* Good syntax *)
    main() : Foo {
	case foo.bar() of y:Int => 3;
	z:String => 4;
	x:Test => 5; esac 
    };

};

class Lets_Do_Lets {

    (* multiple bindings, errors in the middle *)
    
    bar():Int { 
	let x:Int, y:string (* misspelled! *) , z:Int in x + y + z 
    };

    (* multiple bindings, no errors *)
    bar():String { 
	let x:Int <- 5, y:String <- "asdfasdfads", z:Int in x 

	let x:Int <- 5, let y:Int in let z:Int in a
    };


};
