class C {
    a : Int;
    b : Bool;
    init(x : Int, y : Bool) : C {
        {
	    a <- x;
	    b <- y;
	    self;
        }
    };
};


Class Main {
    a:Int;
    main():C {
	{
	    a <- 5;
	    (new C).init(1,true);
	}
    };
};
