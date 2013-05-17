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

-- Can't redefine IO
class IO {
    a : Int;
};

-- Can't inherit from Bool
class Fool inherits Bool {
    a : Int;
};

-- Can't redefine SELF_TYPE
class SELF_TYPE {
    a : Int;
};

-- Can't inherit from SELF_TYPE
class XKCD inherits SELF_TYPE {
    a : Int;
};

-- CAN inherit from IO
class OL inherits IO {
    a : Int;
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
