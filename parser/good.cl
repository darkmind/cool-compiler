class A {
    ana(): Int {
	(
	let x:Int <- 1, y:Int <- x + 2, z:Int <- x * 3 in 
	{
	    result <- (x + y + z); 
	} 	
	) + 3
    };
    };


class Foo { 
    x : Int; 
    y : Int;
    foo : String;
    bar : String;
    likesCats : Bool;
};

Class BB__ inherits A {
};

class Main {
main(): Int {

}
};
