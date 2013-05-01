
(*
 *  execute "coolc bad.cl" to see the error messages that the coolc parser
 *  generates
 *
 *  execute "myparser bad.cl" to see the error messages that your parser
 *  generates
 *)

(* no error *)
class A {
};

(* redefine the same class *)
class A {
x:Int <- 5
};

(* error: class ended prematurely  *)
Class B {
a:Int <- 5;
};
a:String <- "class ended already";
};

(* error: a is not a type identifier *)
Class C inherits a {
};

(* error: keyword inherits is misspelled *)
Class D inherts A {
};

(* error: closing brace is missing *)
Class E inherits A {
;

(* error: f is not a type identifier *)
Class f inherits A {
};

(* error: malformed attributes *)
Class G inherits A {
myint1:Int <- 1
myint2:int <- 1;
myint3:Int;
myint4:int;
myint5:integer;
myint6:Int <- 01;
myint7:Int <- 00000000000000000001;
myint8:Int <- 00000000000000000000000;
myint9:Int <- not True;

mybool1:Bool <- True;
mybool2:bool <- TrUE
mybool3:bool <- trUE;
mybool4:ool <- fALse;
mybool5:Boolean <- falSe;
mybool6:boolean <- faLsE;
mybool7:Bool <- FALSE;
mybool8:boolean;
mybool8:Bool;
mybool9:Void;
mybool10:Boolean <- void;
mybool11
:             Boolean
<-
"FaLsE"             ;
mybool12:Boolean <- not True;

mystr1:String <- "hello";
mystr2:String <- "no ending semi-colon"
mystr3:String <- "no ending quotes;
mystr4:String <- no quotes at all;
mystr5:String -> "reverse assign operator";
myst			r6:str;
mystr7:string;
mystr8
:
String
<-
"multiple line attribute"
;
mystr9
	:		String 		<- 			"all 				over the place"		;
mystr10:String <- "YxP7VmCd3CyuO2ykOHCSKGa9KHrmvhmmVq6elUaSt8RFO32kiZ7O7cKamWBA8aRfNsGomAWLv5l9V3h0bvZYqKiqouQmzfFwvz8JAoat1qr37WQKBTTWu7ZzIqnzfePVbPqwBb5LW0dBPKbsOjJgGjQpSvdcVr3DiTglRzSg3lO7Y29M0wgU4lJkaBsldYQ7i3Q7gfj74EWJg6wJzeiWgdGn9lGOhy0nPsLF1Y8M5KjdfCi2G2EqGIzHkmwARqLxSTXubKXWtGbLlowQD1b5H2d266h5jNpHXXbJSkOfaL4yv463JORKvwgCjmLPduPlOG68BPnd8rmTRCZ29mvL4mvEDxiXs6lck40qPBaAhGLzbluYaL4kNu1cQD11DLw7d6GImQMWSXiiAifYDVmC3tNl33uYkpYMrHedMqtoDHR0xp3WPwDKBnJrShMI1loEbCYsnMhL32UwCnMGwVkDRVshGSS9Ozc5DJn8j8kCbxDYbk6dSNJ6Kl4Ae8ztk95qSZz2rivtATaJWObBcmzrZBrIvFtfHeuHK8fKU9YoPEJbiygJfWqD43pBRardvqYEHJWoOIMVyqJTp7cThQDePJD4Uthsgzi11wTHApJp5QBUjIe2x2MY8muNphOidgaqEoeQnZZo0Qx8afwIRz3sQv9z16g2yHe0kb61X4uUCjZT2ILM2DYnNf5u1JHIo42yCXiEvWJ73AtPoB9zInIq5o92MbIzIJAbWnHEHbwibBld1GUm69uvhQOnmVed19dc3WOHvjjrpZ6TPS4fkTMlZAlpA24qjbJlILNmQAB7xRKACNr4jqQLp3N4eH4WB54IHZbayiwrJw9GPK8G6hEafv6xxIbpLlm3AllX8qaCERkQynVttZmbaXvevTEKLAJaWiWGdcJLcLaE8jwvGTYJd47VPAb3Z2NUQbBw8G22bwA6bY7xOmwkLN2qD3Yioe3DXkZPH1zQEBcFbgVvxHReytf2jQcLwanaV0zUiNqEYIKUz305n1JmBBjEXLAbmKdR";
mystr11:String <- "AtPgWRziWSfhZ0V5FBtMTz8ENV2jzfQrbWJVvxMBE8f3tzT0mrIzEaypZixhF2MOyPfFEII2IxqzaGw86di06gFmX9CJHXc7BC7KvJ16zpURrx64Za9FycJBJyqIxc3ScTIO1nnxs4epWfjRN6VexLBCXq9wlCdQggJGp6GlBhjvb8OrHB2sCWoeFD6nEH8NhcmFh24MzQL5Vg98JdXqvduElfrEfaoKnRXniI1V9sfQ4ERoSH5y99z5B3GDJPqFhRYp5YHDF6E7XlXHLCqB21fR8B73iwyh7tNrcNOq8sExflzZLLAsfo5w6BCBDiXHwT2l0djhOGYJcnS0yHfEk2lL2slhQSTTwocJAnknKy3XqYk1ua8YYjjsa9Rs5Wf09xT5Wt7suaNcbDMEtHJmrUnky4ud5wPQcgbf6VLAJFF9z3CCeKqKN3FuvcbCrq9eL1rMDEEgTUIKSDMptABDfOvy9ghhFVlhvF5dkHTDBhpQfMcxpJG12S2RYiWvgatz5gVQieYXWZb7gvyLKqOFe05qMgA2W9OytIQ5DYp87wFPgKFVRqgwu0N5HyYxQhYggIwVWpstgeZadjFQ2BTwjrgjS7e3mDNBgQJjIhRJwzckjefP86nUUhAdKvxVEWtapQOnNd0QjO6fdeMW4nzOpOJ0UDdQ31RxMZaVca1qAiQl527MZxq8Dg8T3pEd2uW1R8tTrWYfaMJSNKVvvzARB6b0BAC6qk3rOsypB6bsiNmgj1LOSfwtAAA71j26s50bWGuPF8sRCJut55u1gY6Hg1RPpT356jUr6tUNf8adkTraE6NoIPSg8MAT71dB3QZSbdacuiCrSuuGJuwyyMMLdEqhkLhONszaLfXob907fUfc38sTxeOs0vRgPY9OWnvOxSBPf1ovMOO2tGS7v5qaKv0uxajCTs8h7gDr3zC0cW3nuItqcPNUR83290iUYWWds65A8oky1BaodtsBecpflUL6WS1EoIMyUMqY7qUNsij9v4zsMDb75LplsH5E";
mystr12:String <- "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";

misc1: <- "no type defined";
};

Class H inherits A {
"no ending semi-colon for this class"
}

(* error: malformed method definitions *)
Class I inherits A {
func1:String{"no parans"};
func2():{"no return type defined"};
func3():String{"no ending semi-colon"}
func4():String{apparently a string};
func5()
:
String
{
"multiple line function"
};
func5():integer{5};
func6():Int{};
func7():Int();
func8():func9():func10():{"nested functions"};
func11   	(  )  		: 	{	"w	h	i	t	e	space"	};
func12(arg1:Int, arg2:Int):Int {arg1+arg2};
func13():Int {0};
func14:Int {0};
--func15(:Int {0};
--func16):Int {0};
self.func17():Int {0};
a@B.func18();
c@De@F.func19();
};

Class J inherits A {
Feature:String <- "feature beginning with uppercase letter";
};

(* More errors with features *)
Class K inherits A {
method1():Int{};
method1():Int{};
method1():Int{};
attribute1:String <- "same attribute name";
attribute1:String <- "same attribute name again";
attribute1:String <- "same attribute name again again";
method1:String <- "attribute having the same name as method";
};

Class L {
init(hd:Int, tl:String):Int {
5
};
};

Class M {
(new L).init(1, "str");
c:L <- new L;
c.init(2, "str again");
};

(* Errors with inheritance *)
Class N_Parent {
method1():Int{4};
attribute1:String <- "this attribute is going to be overridden";
};

Class O_Child inherits N_Parent{
method1():Int{5};
attribute1:String <- "overriding this attribute";
};

Class P inherits L inherits M {
5
};

Class Q inherits L,M {
6;
};

Class R inherits L M {
7;
};

Class S inherits LM {
8;
};

Class T inherits U {
9;
};

Class U inherits T {
10;
};

Class V inherits IO {
a:String <- "testing inheriting from IO";
};

Class W inherits Int {
a:String <- "inherits from Int";
};

Class X inherits String {
a:String <- "inherits from String";
};

Class Y inherits Bool {
a:String <- "inherits from Bool";
};

(* self/SELF_TYPE errors *)
Class Z {
-- valid uses of self/SELF_TYPE
new SELF_TYPE;
method1():SELF_TYPE{self;};
attribute1:SELF_TYPE <- self;
let x:SELF_TYPE <- self in 2;

-- invalid uses of self/SELF_TYPE
method2():Int{SELF_TYPE};
SELF_TYPE():Int{"SELF_TYPE is the object id"};
self:String <- "assigning to self";
self <- "assigning to self again";
let self:Int <- 5 in 3;
case self of a:Int => 5 esac;
a:Int <- self;
};

(* Void errors *)
Class AA {
void:Int <- 5;
a:Int <- void;
isvoid 5;
let a:Bool <- isvoid 7 in 5;
void = 5
a = void
void.method(3);
case void of a:Int => "hello" esac
};

(* Arithmetic and comparison operators *)
Class AB {
method1():Int{
2*5+3/6-4 3*5
2*5+3/6-4 = 3*5
2*5+3/6-4 < 3*5
2*5+3/6-4 <= 3*5
"hello"+4
True+False
5 <= 7
-4 < 3
-4 < 3 <= 67
4 = 4
void = void = 4 = 3
void = 5
5 = void
a:Int <- ~5
};
};

(* Basic classes' methods *)
Class AC inherits IO{
-- Object
abort();
type_name();
copy();

-- IO
out_string("my string");
out_int(5);
mystr:String <- in_string();
myint:Int <- in_int();

-- String
mystr2:String <- "hello";
mystr2.length();
mystr2.concat(" tata");
mystr2.substr(2, 4);
};

Class IO {};
Class String {};
Class Bool {};
Class Int {};
