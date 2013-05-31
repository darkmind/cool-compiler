class Main {
    a : Int <- 1;
    b : Int <- 2;
    c : Int <- 3;

    main() : Object {
	{
	    let d : Int <- 1000, a : Int <- 5,  out : IO <- new IO in {
		out.out_int(a + b + c + d);
		out.out_string("\n");
		out.out_int(d);
		out.out_string("\n");
	    };
	    (new IO).out_int(a + b + c);
	}
    };

    simple() : Object {
	let a : Int <- 4 in 5
    };

};
