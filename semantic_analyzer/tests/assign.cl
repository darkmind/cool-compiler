Class Main {
	a:Int <- 5;   --good
	b:A <- new B; --good
	c:A <- new A; --good
	d:A <- new C; --bad
	e:String <- "hello"; --good
	f:Bool <- true;
	g:SELF_TYPE <- new B;
	i:A <- d;
	main():C {
	    {
	    	f <- false;
	    	f <- 6;
	    }
	};
};

Class A {
	h:SELF_TYPE <- new A;
};

Class B inherits A {
	j:SELF_TYPE <- new B;
	k:SELF_TYPE <- new A;
	l:SELF_TYPE <- new C;
};

Class C {
	m:IO <- 4;
	n:Object <- m;
	o:B <- new B;
	p:Object <- o;
	q:SELF_TYPE <- self;
	r:SELF_TYPE <- object;
};
