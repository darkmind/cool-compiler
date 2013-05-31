class Main {
    a : Int <- 8;
    b : Int <- 11;
    boo1 : Bool;
    boo2 : Bool;
    io : IO <- new IO;
    checkIsVoid : Main2 <- new Main2;
    main() : Object {
	{
	    (5 < 3 + 2);
	    (a < b);
	    b < a;
	    5 < b;
	    a < 3;
	    -- io.out_int(equaltest());
	    if equaltest() then (new IO).out_string("yay\n") else (new IO).out_string("uh oh\n") fi;
	    checkIsVoid.checkisvoid();
	}
    };

    equaltest() : Bool {
	{
	    io.out_int(a);
	    io.out_int(a + 3);
	    (if 3 = (3 + a) then io.out_string("uh oh\n") else io.out_string("YAYA\n") fi);
	    a = b;
	    "str1" = "str2";
	    true = false;
	    boo1 = boo2;
	}
    };

    compvsneg() : Object {
	{
	    ~3;
	    ~4;
	    not false;
	    not boo1;
	    ~a;
	}
    };
};

class Main2 {
    a : Main;
    io : IO <- new IO;
    checklt() : Bool {
        5 < 3
    };

    checkisvoid() : IO {
	{
	    if isvoid 3 then io.out_string("bad\n") else io.out_string("good\n") fi;
	    if isvoid a then io.out_string("yes i'm void right now\n") else io.out_string("what are you doing i'm void\n") fi;
	    if isvoid (a <- new Main) then io.out_string("waht the fuck i'm declared now\n") else io.out_string("correct, i exist now\n") fi;
	}
    };
};
