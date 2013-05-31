class Main {
    a : Int <- 1;
    main() : Bool {
	{
	    1/0;
	    ((((false))));
	}
    };
};

class A inherits IO {
    b : Int;
    a : Int;
    parens(c : Int, d : Int) : Int {
	{
	    1/2;
	    1/a;
	    6/0;
	    (*
		((a));
		b <- (c);
		c <- (((a)));
	    *)
	}
    };
};

class Plusses {
    a : Int;
    b : Int;
    test(form1 : Int, form2 : Int, wrongType : Bool) : Int {
	{
	    -- 1 + 2;
	    -- 1 + a;
	    -- a + b;
	    1 + form1;
	    -- form1 + form2;
	}
    };
};

class ArithOrder {
    a : Int;
    test() : Int {
	{
	    a/2;
	    -- a - 2;
	    -- a * 2;
	}
    };
    negtest() : Int {
	~2
    };
};
