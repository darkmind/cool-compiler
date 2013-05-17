(* Testing explicitly defined static object dispatch *)

Class Main {
	attr_one:A <- new B;
	attr_two:A <- new C;
	attr_three:A <- new A;
	attr_four:C <- new C;
	main():String {
	    {
		attr_one@A.a(5, "hello");
		attr_one@B.a(5, "hello", "extra argument");
		attr_two@C.a("hello", 5);
		attr_two@A.b(attr_three, attr_four);
		attr_one@A.a(5, "hello");

		attr_one@C.b(attr_four, attr_three);
		attr_four@C.c((new C), (new A));
		attr_four@A.c((new A), (new C));
	    }
	};
};

Class A {
	a(arg_one:Int, arg_two:String):Int {
		5
	};
};

Class B inherits A {
	a(arg_one:Int, arg_two:String):Int {
		8
	};

	b(arg_one:A, arg_two:C):String {
		"return"
	};
};

Class C inherits A {
	c(arg_one:B, arg_two:A):String {
		"return"
	};
};
