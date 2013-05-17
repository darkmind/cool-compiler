(* Missing a main class *)

Class Mainasdf {
    a:Int;
    main():Int {
	{
	    a <- 5;
	}
    };
};

Class Mainasd {
    a:Int;
    main(b:Int):C { -- should not be marked as main, so formals should be fine.
	{
	    a <- 5;
	    (new C).init(1,true); -- missing class definition for C
	}
    };
};
