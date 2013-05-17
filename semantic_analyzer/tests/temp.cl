class Main {
	b:Int;
	c:String;
	main():Int {
		{ --don't start new scope for blocks
			c <- "tata";
			b <- 5;
		}
	};
	a:Int <- 8 + 5;
};
