class Main {
    mya : A <- (new A);
    myio : IO <- (new IO);
    main() : Int {
	{
	    myio.out_string("Outputs for 'less than'\n");
	    mya.a();
	    mya.b();
	    mya.c();
	    mya.d();
	    mya.e();

	    myio.out_string("Outputs for 'less than or equal to'\n");
	    mya.f();
	    mya.g();
	    mya.h();
	    mya.i();
	    mya.j();

	    myio.out_string("Nested conditionals\n");
	    mya.k();
	}
    };
};

class A inherits IO {
    myint1 : Int <- 88;
    myint2 : Int <- 88;
    a() : Int {
        {
        (if (5 < 7) then
	     { 
	         out_string("5 < 7");
		 1;
	     }
	 else 
             { 
	         out_string("5 not < 7");
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };

    b() : Int {
        {
        (if (myint1 < myint2) then
	     { 
		 out_int(myint1);
	         out_string(" < ");
		 out_int(myint2);
		 1;
	     }
	 else 
             { 
	         out_int(myint1);
	         out_string(" not < ");
		 out_int(myint2);
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };

    c() : Int {
        {
        (if (myint2 < myint1) then
	     { 
		 out_int(myint2);
	         out_string(" < ");
		 out_int(myint1);
		 1;
	     }
	 else 
             { 
	         out_int(myint2);
	         out_string(" not < ");
		 out_int(myint1);
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };

    d() : Int {
        {
        (if (5 < myint2) then
	     { 
		 out_int(5);
	         out_string(" < ");
		 out_int(myint2);
		 1;
	     }
	 else 
             { 
	         out_int(5);
	         out_string(" not < ");
		 out_int(myint2);
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };

    e() : Int {
        {
        (if (myint1 < 3) then
	     { 
		 out_int(myint1);
	         out_string(" < ");
		 out_int(3);
		 1;
	     }
	 else 
             { 
	         out_int(myint1);
	         out_string(" not < ");
		 out_int(3);
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };

    f() : Int {
        {
        (if (5 <= 7) then
	     { 
	         out_string("5 <= 7");
		 1;
	     }
	 else 
             { 
	         out_string("5 not <= 7");
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };

    g() : Int {
        {
        (if (myint1 <= myint2) then
	     { 
		 out_int(myint1);
	         out_string(" <= ");
		 out_int(myint2);
		 1;
	     }
	 else 
             { 
	         out_int(myint1);
	         out_string(" not <= ");
		 out_int(myint2);
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };

    h() : Int {
        {
        (if (myint2 <= myint1) then
	     { 
		 out_int(myint2);
	         out_string(" <= ");
		 out_int(myint1);
		 1;
	     }
	 else 
             { 
	         out_int(myint2);
	         out_string(" not <= ");
		 out_int(myint1);
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };

    i() : Int {
        {
        (if (5 <= myint2) then
	     { 
		 out_int(5);
	         out_string(" <= ");
		 out_int(myint2);
		 1;
	     }
	 else 
             { 
	         out_int(5);
	         out_string(" not <= ");
		 out_int(myint2);
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };

    j() : Int {
        {
        (if (myint1 <= 3) then
	     { 
		 out_int(myint1);
	         out_string(" <= ");
		 out_int(3);
		 1;
	     }
	 else 
             { 
	         out_int(myint1);
	         out_string(" not <= ");
		 out_int(3);
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };

    myint3 : Int <- 2;

    k() : Int {
	{
	    (if if myint3 < 3 then 8 < 9 else 100 < 99 fi then
		 if (2+5) <= 6 then 
		     (new IO).out_string("A\n") 
		 else
		     {
		         (new IO).out_string("B\n");
	                 (new IO).out_string("correct branch\n");
		     }
		 fi
	     else
		 (new IO).out_string("wrong branch\n")
	     fi
	    );
	    9999;
	}
    };
};
