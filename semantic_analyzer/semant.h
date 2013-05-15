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

#include "classtable.h"
#include "featuretable.h"

class SemanticAnalyzer {
private:
  SymbolTable<Symbol, Symbol> *symbol_table;
  ClassTable *class_table;
  FeatureTable *feature_table;
public:
  SemanticAnalyzer();
  void traverse(Classes);
}

#endif
