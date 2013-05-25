#include <assert.h>
#include <stdio.h>
#include "emit.h"
#include "cool-tree.h"
#include "symtab.h"

#include <map>


enum Basicness     {Basic, NotBasic};
#define TRUE 1
#define FALSE 0

class CgenClassTable;
typedef CgenClassTable *CgenClassTableP;

class CgenNode;
typedef CgenNode *CgenNodeP;

class CgenClassTable : public SymbolTable<Symbol,CgenNode> {
private:
   List<CgenNode> *nds;
   ostream& str;
   int objectclasstag;
   int ioclasstag;
   int stringclasstag;
   int intclasstag;
   int boolclasstag;


// The following methods emit code for
// constants and global declarations.

   void code_global_data();
   void code_global_text();
   void code_bools(int);
   void code_select_gc();
   void code_constants();
   void code_prototypes();
   void code_basic_prototypes();
   void code_user_prototypes();

// The following creates an inheritance graph from
// a list of classes.  The graph is implemented as
// a tree of `CgenNode', and class names are placed
// in the base class symbol table.

   void install_basic_classes();
   void install_class(CgenNodeP nd);
   void install_classes(Classes cs);
   void build_inheritance_tree();
   void set_relations(CgenNodeP nd);

// Following is used for handling and marking things with class tags.
   std::map<Symbol, CgenNodeP> *class_tags; // Maps from the class name (Symbol) to the node of the class

public:
   CgenClassTable(Classes, ostream& str);
   void code();
   CgenNodeP root();

   CgenNodeP map_get_tag(Symbol symbol) { return (*class_tags)[symbol]; }
   void map_add_tag(Symbol symbol, CgenNodeP nd) { (*class_tags)[symbol] = nd; }
   void add_class_tags(List<CgenNode> *list, int curr_tag);
};


class CgenNode : public class__class {
private: 
   CgenNodeP parentnd;                        // Parent of class
   List<CgenNode> *children;                  // Children of class
   Basicness basic_status;                    // `Basic' if class is basic
                                              // `NotBasic' otherwise
   int tag;


public:
   CgenNode(Class_ c,
            Basicness bstatus,
            CgenClassTableP class_table);

   void add_child(CgenNodeP child);
   List<CgenNode> *get_children() { return children; }
   void set_parentnd(CgenNodeP p);
   CgenNodeP get_parentnd() { return parentnd; }

   int basic() { return (basic_status == Basic); }

   void nd_set_tag(int intag) { tag = intag; }
   int nd_get_tag() { return tag; }

};

class BoolConst 
{
 private: 
  int val;
 public:
  BoolConst(int);
  void code_def(ostream&, int boolclasstag);
  void code_ref(ostream&) const;
};

