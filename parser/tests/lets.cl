Class B inherits A {
    let x:Int <- 1 in 2
};

class B {
    let x:Int, y:Int <- x, abc:String <- ERRORHERE, ab:String <- "not an error" in { 21 };
};

