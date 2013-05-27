
(*  Example cool program testing as many aspects of the code generator
    as possible.
 *)

class Main {
  hw:Helloworld <- (new Helloworld);
  mc:Myclass <- (new Myclass);
  hi:Myclass;
  main():Int {
    {
      hw.say();
      mc.hello();
    }
  };
};

class Helloworld inherits Main {
  myint:Int;
  myint2:Int <- 5;
  myint3:Int <- myint2;
  myint4:Int <- self.say();
  mystr:String;
  mystr2:String <- "hello";
  mybool:Bool;
  mybool2:Bool <- true;
  mybool3:Bool <- false;
  blah:Myclass;
  myobj:Object;
  myobj2:Object <- 6;
  say():Int {
    {
      --out_string("hello world");
      1;
    }
  };
};

class Myclass {
  hello():Int {
    1
  };
};

class IO2 inherits IO {
    asdf : Int;
};
