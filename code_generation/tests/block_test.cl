class Main {
    a : Int <- 1;
    b : IO <- new IO;
    c : String <- "asdf";
    d : Bool <- true;
    e : Int;
    main() : Int {
	{
	    d <- false;
	    1 + 2;
	    e <- 1 + 2 + 3;
	}
    };
};

class A inherits IO {
    b : Int;
    formals(c : Int) : Int {
	b <- c
    };
};
