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

enum feature_type {
    ATTRIBUTE,
    METHOD
};

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

  bool class_exists(Symbol class_name);
  bool check_for_cycles();
  bool is_child(Symbol child_name, Symbol class_name);

  Symbol lca(Symbol class1, Symbol class2);
};

class FeatureTable {
private:
  struct features_struct {
    std::map<Symbol, method_class *> methods;
    std::map<Symbol, Symbol> attributes;
    Class_ c;
  };
  std::map<Symbol, features_struct> features;

public:
  FeatureTable();
  std::map<Symbol, method_class *> *get_methods(Symbol class_name);
  std::map<Symbol, Symbol> *get_attributes(Symbol class_name);
  std::map<Symbol, features_struct> *get_features_map();
  bool method_exists_in_class(Symbol method_name, Symbol class_name);
  bool add_method(Symbol class_name, method_class *method_ptr, Class_ c);
  bool add_attribute(Symbol class_name, attr_class *attr_ptr, Class_ c);
  void populate(Classes);
};

class SemanticAnalyzer {
private:
  SymbolTable<Symbol, Symbol> *symbol_table;
  ClassTable *class_table;
  FeatureTable *feature_table;
public:
  SemanticAnalyzer();
  void set_symbol_table(SymbolTable<Symbol, Symbol> *symbol_table);
  void set_class_table(ClassTable *class_table);
  void set_feature_table(FeatureTable *feature_table);
  void check_attributes(Features features, Symbol class_name);
  void check_attribute(attr_class *attribute, Symbol class_name);
  bool is_attr_in_parent_classes(attr_class *attribute, Symbol class_name);
  feature_type get_type_of_feature(Feature feature);
  void check_methods(Features features, Symbol class_name);
  void check_method(method_class *method, Symbol class_name);
  bool valid_type(Symbol type);
  bool method_redefined_with_different_signature(method_class *method, Symbol class_name);
  bool have_identical_signatures(method_class *method_one, method_class *method_two);
  void traverse(Classes);
};

#endif
