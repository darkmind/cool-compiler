class Main {
    a : Object;
    b : FF;
    c : GG;
    int1 : Int;
    main() : Object {
	{
	    (new IO).out_string("starting the case");
	    case 5 of
	      a : Int => { 
		  (new IO).out_string("case a");
		  1;
	      };
	      b : B => {
		  (new IO).out_string("case b");
		  2;
	      };
	      c : FF => {
		  (new IO).out_string("case ff");
		  3;
	      };
	    esac;
	    (new IO).out_string("finished the case");
	}
    };
};

class FF {
    what : Int;
};

class GG inherits FF {
    isthis : Int;
};


class A {
    a : Int;
};

class B inherits A {
    b : Int;
};

class C inherits B {
    c : Int;
};

class D inherits C {
    d : Int;
};

class E inherits B {
    e : Int;
};

class F inherits E {
    f : Int;
};

class J inherits C {
    j : Int;
};

class K inherits B {
    k : Int;
};

class L inherits A {
    l : Int;
};

class N inherits L {
    n : Int;
};

class M inherits A {
    m : Int;
};

class P inherits Main {
    p : Int;
};
