class A {
  myself:A <- new SELF_TYPE;
  me : A <- new A;
};

class Main {
    main() : SELF_TYPE {
	new SELF_TYPE
    };
};
