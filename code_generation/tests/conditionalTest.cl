class Main {
    mya : A <- (new A);
    main() : Int {
	{
	    mya.a();
	    mya.b();
	    mya.c();
	    mya.d();
	    mya.e();
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
	         out_string("5 >= 7");
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
	         out_string(" >= ");
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
	         out_string(" >= ");
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
	         out_string(" >= ");
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
	         out_string(" >= ");
		 out_int(3);
		 0;
	     }
	 fi);
	out_string("\n");
	9999;
	}
    };
};
