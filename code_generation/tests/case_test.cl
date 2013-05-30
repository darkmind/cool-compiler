class Main {
    a : Object;
    b : FF;
    c : GG;
    int1 : Int;
    main() : Object {
	case 5 of
	  a : Int => 5 + a + int1;
	  b : B => 4;
	  c : FF => 3;
	esac
    } ;
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
