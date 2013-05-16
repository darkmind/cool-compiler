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

Class Ain {
	a:Int;
	main():C {
	  a <- 5
	};
};
