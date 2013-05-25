
(*  Example cool program testing as many aspects of the code generator
    as possible.
 *)

class Main {
  hw:Helloworld <- (new Helloworld);
  mc:Myclass <- (new Myclass);
  main():Int {
    {
      hw.say();
      mc.hello();
    }
  };
};

class Helloworld inherits Main {
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
