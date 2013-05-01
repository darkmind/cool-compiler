-- inside the feature, (one thing) something incorrect. (semicolon)
CLASS A {
        foo(i : INT) : F {
                1+5;
        };
};
 
-- multiple things incorrect inside the feature.
CLASS B {
        foo(i : INT) : F {
                1+5;
        };
        roo(i : INT) : F {
                1+5;
        };
        eoo(i : INT) : F {
                1+5;
        };
        doo(i : INT) : F {
                1+5;
        };
        boo(i : INT) : F {
                1+5;
        };
};
