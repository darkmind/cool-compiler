-- good inheritance
Class Parent {
};

Class Child inherits Parent{
};

-- inherit from undefined class
Class S inherits Z {
};

-- cyclic
Class T inherits U {
};

Class U inherits T {
};

-- inherit from base classes
Class V inherits IO {
};

Class W inherits Int {
};

Class X inherits String {
};

Class Y inherits Bool {
};

-- redefine class
Class Child {
};
