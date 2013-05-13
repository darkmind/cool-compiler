#ifndef SEMANT_H_
#define SEMANT_H_

#include <assert.h>
#include <iostream>  
#include "cool-tree.h"
#include "stringtab.h"
#include "symtab.h"
#include "list.h"

#include <map>
#include <set>

#define TRUE 1
#define FALSE 0

class ClassTable;
typedef ClassTable *ClassTableP;

// This is a structure that may be used to contain the semantic
// information such as the inheritance graph.  You may use it or not as
// you like: it is only here to provide a container for the supplied
// methods.

class ClassTable {
private:
  int semant_errors;
  void install_basic_classes();
  ostream& error_stream;
  
  struct map_val {
	Symbol parent;
	Class_ c;
  };
  std::map<Symbol, map_val> class_map;

public:
  ClassTable(Classes);
  int errors() { return semant_errors; }
  ostream& semant_error();
  ostream& semant_error(Class_ c);
  ostream& semant_error(Symbol filename, tree_node *t);

  bool check_for_cycles();
};

class MethodTable {
private:
  struct method_val {
    std::set<Symbol> methods;
    Class_ c;
  };
  std::map<Symbol, method_val> class_methods;

public:
  MethodTable();
  std::set<Symbol> getMethods(Symbol class_name);
  bool addMethod(Symbol method_name, Symbol class_name, Class_ c);
  void populate_method_table(Classes);
};

class MySymbolTable {
private:
  struct sym_data {
    // here, put in data about the symbol that we want to store for later retrieval
    Symbol type;
    Expression init;
  };
  SymbolTable<Symbol, sym_data *> *sym_tab;

public:
  void populate(Classes);
}

#endif
