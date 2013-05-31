class Main {
    a : Int <- 0;
    main() : Int {
	{
        (while (a < 5) loop
	    {
	        (a <- a + 1);
		(new IO).out_string("incremented a to ");
		(new IO).out_int(a);
		(new IO).out_string("\n");
	    }
	pool);
	(new IO).out_string("value of a: ");
	(new IO).out_int(a);
	(new IO).out_string("\n");
	nestedloop();
	9999;
	}
    };

    b : Int <- 0;
    c : Int <- ~5;
    nestedloop() : Int {
	{
	(while (c < 0) loop
	    {
		(while (b < 10) loop { 
					b <- b+1;
					(new IO).out_string("curr b: ");
					(new IO).out_int(b);
					(new IO).out_string("\n");
					} pool); 
		b <- 0;
		c <- c + 1;
		(new IO).out_string("curr c: ");
		(new IO).out_int(c);
		(new IO).out_string("\n");
	    }
	pool);
	9999;
	}
    };
};
