class Main {
    main() : Int {
	{
	    5;
	    (new A).formals2(5, 6);
	    -- new A;
	}
    };
};

class A {
    a : Int <- 5;
    z : Int <- 6;
    formals (b : Int, c : Int, d:Int) : Int {
	{ 
	    b <- a;
	    c <- a;
	    d <- z;
	    a + b + c + d;
	}
    };

   formals2 (b : Int, a : Int) : Int {
	{ 
	    b <- a;
	}
    }; 

    method1 () : Int {
	filler
    };

    filler : Int <- 5;

    manyargs (a1 : Int, a2 : Int, a3: Int, a4: Int, a5:Int): Int {
	a
    };
};
