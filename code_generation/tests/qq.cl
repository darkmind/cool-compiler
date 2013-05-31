class Main {
    boo1 : Bool;
    boo2 : Bool;
    io : IO <- new IO;
    a : Int <- 3;
    dummy : Int;
    main1() : Object {
	{
	    if boo1 then io.out_string("boo1 is true") else io.out_string("boo1 is false") fi;
	    if boo2 then io.out_string("boo2 is true") else io.out_string("boo2 is false") fi;
	    if equaltest() then (new IO).out_string("yay\n") else (new IO).out_string("uh oh\n") fi;
	    -- equaltestInt();
	}
    };

    equaltest() : Bool {
	{
	    boo1 = boo2;
	}
    };

    main() : IO {
	{
	    dummy <- 3 + a;
	    io.out_int(dummy); io.out_string("\n");
	    io.out_int(6);
	    io.out_int(3+a); io.out_string("\n");
	    if (dummy = (6)) then io.out_string("ok 3 + a is 6, good\n") else io.out_string("why is 3 + a not 6?\n") fi;
	    if (dummy = (3)) then io.out_string("ok 3 + a is 3, not good\n") else io.out_string("good, 3+a is not 3?\n") fi;
	    if (dummy = 3 + a) then io.out_string("WTF") else io.out_string("ok at least we're consistent") fi;
	    if (3 = 3) then io.out_string("WTF") else io.out_string("ok at least we're consistent") fi;
	    (if 3 = (3 + a) then io.out_string("uh oh ints fail\n") else io.out_string("YAYA\n") fi);
	}
    };
};


