class Main {
    a : Int <- 1;
    b : IO <- new IO;
    c : String <- "asdf";
    d : Bool <- true;
    main() : Bool {
	d <- false
    };
};

class A inherits IO {
    b : Int;
    formals(c : Int) : Int {
	b <- c
    };
};
