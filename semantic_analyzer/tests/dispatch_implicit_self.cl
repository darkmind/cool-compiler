(* Testing implicit self dispatch call *)
Class Main {
	attr_one:A <- new B;
	attr_two:C <- new D;
	main():String {
	    {
		a(5, "hello"); --good
		a(5, "hello", "extra argument"); --bad
		a("hello", 5); --bad
		b(attr_one, attr_two); --good
		b(attr_two, attr_one); --bad
		b((new C), (new A)); --bad
		b((new A), (new C)); --good
	    }
	};

	a(arg_one:Int, arg_two:String):Int {
		5
	};

	b(arg_one:A, arg_two:C):String {
		"return"
	};
};

Class A {

};

Class B inherits A {

};

Class C {

};

Class D inherits C {

};
