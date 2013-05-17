class Main {
  main(): Int {
	doh()
  };

  doh() : Int {
	{
		(let i : Int <- 5 in { i; } );
		let i : Int <- 5, i : Int <- 6 in { i; };
		(let i : Int <- 5, j:Int <- 7 in { i; } );
		let i : String <- "hello", i : Int <- 5 in { i; };
		(let i : String <- 7, i : Int <- "hello" in { i; } );
		(let i : IO <- 5 in { i; } );
		(let i : Int, j : Blah, k : Bool in { i; } );
	}
  };

};
