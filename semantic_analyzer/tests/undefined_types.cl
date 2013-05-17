class C {
    a : Int;
    b : Bool;
    c : Calvinball; --undefined attr
    init(x : Int, y : Bool, z : Hobbes ) : C { --undefined formal
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
	    let b : WTF in (c <- 5);
	    (new C).init(1,true);
	}
    };
};
