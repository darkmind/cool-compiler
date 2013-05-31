class Main {
    dummy : Int;
    a : Int <- b + 2;
    b : Int <- a + 2;

    f : Foo;
    g : Bar;

    hello : String;
    
    main() : Object {
	{
	    --f <- new Foo;
	    (new Foo).fu(1+2);
	    --(new IO).out_string("IT WORKED\n");
	    hello <- (new Jay).sayhello();
	    (new IO).out_string(hello);
	    --(new IO).out_int(func(1, "asdf"));
	    (new IO).out_int(dummy);
	    mainstatic();
	}
    };

    mainstatic() : Object {
	{
	    (new IO2)@IO.out_int(b);
	}
    };

    func(arg1 : Int, arg2 : String) : Int {
	5
    };
};

class IO2 inherits IO {
    a : Int <- b;
    b : Int <- a;
};

class Foo {
    a : Int;
    fu(arg1 : Int) : Int {
	arg1
    };
};

class Bar inherits Foo {
    b: Int;
    fu (b : Int) : Int {
	a + b
    };
};

class Jay {
    sayhello() : String {
	"hello everyone"
    };
};
