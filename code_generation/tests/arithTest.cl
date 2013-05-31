class Main {
    a : Int <- 8;
    b : Int <- 11;
    main() : Bool {
	{
	    (5 < 3);
	    (a < b);
	    b < a;
	    5 < b;
	    a < 3;
	}
    };
};

class LT {
    checklt() : Bool {
        5 < 3
    };
};
