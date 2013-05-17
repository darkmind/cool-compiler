Class Main {
    a:Int;
    main():C {
	{
	    a <- 5;
	    (new C).init(1,true);
	}
    };
};

Class A {
    a():Int {
	{
	    a <- 5 -- no semi-colon here
	    (new C).init(1,true);
	}
    };
};

Class B {
    b():Int {
	{
	    a <- 5;
	    (new C).init(1,true) -- no semi-colon here
	}
    };
};

Class C {
    c():Int {
	{
	    a <- 5
	}
    };
};

Class D {
    d():Int {
	{
	}
    };
};
