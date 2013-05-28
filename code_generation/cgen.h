#include <assert.h>
#include <stdio.h>
#include "emit.h"
#include "cool-tree.h"
#include "symtab.h"

#include <map>
#include <vector>


enum Basicness     {Basic, NotBasic};
#define TRUE 1
#define FALSE 0

class CgenClassTable;
typedef CgenClassTable *CgenClassTableP;

class CgenNode;
typedef CgenNode *CgenNodeP;

typedef attr_class *AttrP;
typedef method_class *MethP;

// Encodes a method/class pair
typedef struct method_dispatch {
    Symbol method_name;
    MethP method_p;
    Symbol class_name;
} method_dispatch;

///////////////////////////////////////////

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

// Following is used for handling the printing of class table
   void code_class_names();
   void recurse_class_names(List<CgenNode> *list);

// Following is used for handling the printing of class_objTab
   void code_object_table();
   void recurse_object_table(List<CgenNode> *list);

   void code_dispatch_tables();
   void code_prototypes();


// The object initialization assembly code is generated
   void code_object_inits();
   void recurse_object_inits(List<CgenNode> *l, int counter);

// Methods are generated
   void code_methods();
   void recurse_methods(List<CgenNode> *l, int counter);

//////////////////////////////////////////////////////////////////
//
// Following is used for actually generating assembly code!
//
//////////////////////////////////////////////////////////////////



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
   void assign_class_tags();
   void recurse_class_tags(List<CgenNode> *list, int *curr_tag);
   std::map<Symbol, CgenNodeP> *class_tags; // Maps from the class name (Symbol) to the node of the class





// Following is used for handling attributes/inherited attributes
   void populate_attr_map(List<CgenNode> *list);
   std::map<Symbol, std::vector<AttrP> *> *attr_map;
   std::map<Symbol, std::vector<method_dispatch> *> *meth_map;

public:
   CgenClassTable(Classes, ostream& str);
   void code();
   CgenNodeP root();

// Following is used for public functions to get/set class tags
   CgenNodeP map_get_class(Symbol symbol) { return (*class_tags)[symbol]; }
   void map_add_tag(Symbol symbol, CgenNodeP nd) { (*class_tags)[symbol] = nd; }
   
   // Basically, a short function that "probes" our map for the symbol, since symbol comparison is tricky
   bool classes_contains_name(Symbol symbol); 

// Used to get attr_list
   std::vector<AttrP> *map_get_attr_list(Symbol symbol) { return (*attr_map)[symbol]; } 
   void add_attr_list(Symbol symbol, std::vector<AttrP> *attr_list) { (*attr_map)[symbol] = attr_list; }
   void populate_attr_map(List<CgenNode> *list, std::vector<AttrP> *parent_list);

// Used to get meth_list
   std::vector<method_dispatch> *map_get_meth_list(Symbol symbol) { return (*meth_map)[symbol]; }
   void add_meth_list(Symbol symbol, std::vector<method_dispatch> *meth_list) { (*meth_map)[symbol] = meth_list; }
   void populate_meth_map(List<CgenNode> *list, std::vector<method_dispatch> *parent_list);

// Used to find strings in the string or int or id tables JK WE DON'T NEED THIS
// TODO: DELETE
/*
   int find_in_stringtable(char * str);
   int find_in_inttable(char * str);
   int find_in_idtable(char * str);
 */
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

   // Used for populating the method and attribute lists for each node
   void nd_populate_attr_list(std::vector<AttrP> *attr_list); 
   void nd_populate_meth_list(std::vector<method_dispatch> *meth_list);

   // Used for generating the initialization assembly code
   int code_init(ostream& str, int counter, CgenClassTableP class_table);
   int code_dispatch(ostream& str, int counter, CgenClassTableP class_table);

   // Common code used in the head and end of every dispatch
   void generate_disp_head(ostream& str, int addn_registers);
   void generate_disp_end(ostream& str, int addn_registers);

   // Code used to figure out how many registers are used in each expression/part of method
   int registers_needed();
  
   bool is_initialized(AttrP attribute);
   // std::vector<AttrP> *get_attributes();
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

