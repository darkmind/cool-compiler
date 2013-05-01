Class B inherits A {
    let x:Int <- 1 in 2
};

class B {
    let x:Int, y:Int <- x, abc:String <- ERRORHERE, ab:String <- "not an error" in { 21 };
};

class Lets_Do_Lets {

    (* multiple bindings, errors in the middle *)
    
    bar():Int { 
	let x:Int, y:string (* misspelled! *) , z:Int in x + y + z 
    };

    (* multiple bindings, no errors *)
    bar():String { 
	let x:Int <- 5, y:String <- "asdfasdfads", z:Int in x;

	let x:Int <- 5, y:Int <- let y:Int in let z:Int in 3 in 6;
    };
};
