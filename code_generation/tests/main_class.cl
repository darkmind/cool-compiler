class Main {
    main() : Object { 
	{
	    (new Main2).asdf();
	    side();
	    (new IO).out_string("main here\n");
	}
    };

    side() : Object {
	(new IO).out_string("i'm on the side\n")
    };
};

class Main2 {
    asdf() : Object {
	(new IO).out_string("here I am, Main2!")
    };
};
