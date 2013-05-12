

#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>
#include "semant.h"
#include "utilities.h"

#include <set>


extern int semant_debug;
extern char *curr_filename;

//////////////////////////////////////////////////////////////////////
//
// Symbols
//
// For convenience, a large number of symbols are predefined here.
// These symbols include the primitive type and method names, as well
// as fixed names used by the runtime system.
//
//////////////////////////////////////////////////////////////////////
static Symbol 
    arg,
    arg2,
    Bool,
    concat,
    cool_abort,
    copy,
    Int,
    in_int,
    in_string,
    IO,
    length,
    Main,
    main_meth,
    No_class,
    No_type,
    Object,
    out_int,
    out_string,
    prim_slot,
    self,
    SELF_TYPE,
    Str,
    str_field,
    substr,
    type_name,
    val;
//
// Initializing the predefined symbols.
//
static void initialize_constants(void)
{
    arg         = idtable.add_string("arg");
    arg2        = idtable.add_string("arg2");
    Bool        = idtable.add_string("Bool");
    concat      = idtable.add_string("concat");
    cool_abort  = idtable.add_string("abort");
    copy        = idtable.add_string("copy");
    Int         = idtable.add_string("Int");
    in_int      = idtable.add_string("in_int");
    in_string   = idtable.add_string("in_string");
    IO          = idtable.add_string("IO");
    length      = idtable.add_string("length");
    Main        = idtable.add_string("Main");
    main_meth   = idtable.add_string("main");
    //   _no_class is a symbol that can't be the name of any 
    //   user-defined class.
    No_class    = idtable.add_string("_no_class");
    No_type     = idtable.add_string("_no_type");
    Object      = idtable.add_string("Object");
    out_int     = idtable.add_string("out_int");
    out_string  = idtable.add_string("out_string");
    prim_slot   = idtable.add_string("_prim_slot");
    self        = idtable.add_string("self");
    SELF_TYPE   = idtable.add_string("SELF_TYPE");
    Str         = idtable.add_string("String");
    str_field   = idtable.add_string("_str_field");
    substr      = idtable.add_string("substr");
    type_name   = idtable.add_string("type_name");
    val         = idtable.add_string("_val");
}

ClassTable::ClassTable(Classes classes) : semant_errors(0) , error_stream(cerr) {
    for(int i = classes->first(); classes->more(i); i = classes->next(i)) {
	Class_ class_ptr = classes->nth(i);
	Symbol name = class_ptr->get_name();
	Symbol parent = class_ptr->get_parent();
	if(class_map.count(name) > 0) { // redefining user-defined class
		semant_error(class_ptr) << "Class " << name << " was previously defined." << endl;
	} else if(name == Object || name == IO || name == Str || name == Int || name == Bool) { // redefined basic class
		semant_error(class_ptr) << "Redefinition of basic class " << name << "." << endl;
	} else {
	    // cannot inherit from Bool, Int or String
	    if(parent == Bool || parent == Int || parent == Str) {
		    semant_error(class_ptr) << "Class " << name << " cannot inherit class " << parent << "." << endl;
		    continue;
	    }
	    // class either inherits from Object or IO or some other user-defined class or from an undefined class (checked below)
	    // add to map		
	    map_val val;
	    val.parent = parent;
	    val.c = class_ptr;
	    class_map[name] = val;
	}
    }

    // go through values and make sure they are all also present as keys in the map i.e. all classes inherit from defined classes
    for(std::map<Symbol, map_val>::iterator it=class_map.begin(); it != class_map.end(); ++it) {
	Symbol parent = (it->second).parent;
	if(class_map.count(parent) == 0 && parent != Object && parent != IO) {
	    // inheriting from a class that is not defined at all so report error
	    semant_error((it->second).c) << "Class " << it->first << " inherits from an undefined class " << (it->second).parent << "." << endl;
	}
    }
}

void ClassTable::populate_symbol_table(SymbolTable<char *,int> *sym_tab) {


}


void ClassTable::populate_method_table(MethodTable *method_tab) {

}

bool ClassTable::check_for_cycles() {
    bool cycle_exists = false;	
    for(std::map<Symbol, map_val>::iterator it=class_map.begin(); it != class_map.end(); ++it) {
	std::set<Symbol> ancestors;
	ancestors.insert(it->first);
	Symbol curr_parent = (it->second).parent;
	while(class_map.count(curr_parent) > 0) { // max count will be 1 because if try to add second one, will already have been caught in main if check
	    map_val val = class_map[curr_parent];
	    if(ancestors.count(val.parent) > 0) { // report error because will introduce cycle
		semant_error((it->second).c) << "Class " << it->first << ", or an ancestor of " << it->first << ", is involved in an inheritance cycle." << endl;
		cycle_exists = true;
		break;
	    } else {
		ancestors.insert(curr_parent);
		curr_parent = val.parent;
	    }
	}
    }
    return cycle_exists;
}

void ClassTable::install_basic_classes() {

    // The tree package uses these globals to annotate the classes built below.
   // curr_lineno  = 0;
    Symbol filename = stringtable.add_string("<basic class>");
    
    // The following demonstrates how to create dummy parse trees to
    // refer to basic Cool classes.  There's no need for method
    // bodies -- these are already built into the runtime system.
    
    // IMPORTANT: The results of the following expressions are
    // stored in local variables.  You will want to do something
    // with those variables at the end of this method to make this
    // code meaningful.

    // 
    // The Object class has no parent class. Its methods are
    //        abort() : Object    aborts the program
    //        type_name() : Str   returns a string representation of class name
    //        copy() : SELF_TYPE  returns a copy of the object
    //
    // There is no need for method bodies in the basic classes---these
    // are already built in to the runtime system.

    Class_ Object_class =
	class_(Object, 
	       No_class,
	       append_Features(
			       append_Features(
					       single_Features(method(cool_abort, nil_Formals(), Object, no_expr())),
					       single_Features(method(type_name, nil_Formals(), Str, no_expr()))),
			       single_Features(method(copy, nil_Formals(), SELF_TYPE, no_expr()))),
	       filename);

    // 
    // The IO class inherits from Object. Its methods are
    //        out_string(Str) : SELF_TYPE       writes a string to the output
    //        out_int(Int) : SELF_TYPE            "    an int    "  "     "
    //        in_string() : Str                 reads a string from the input
    //        in_int() : Int                      "   an int     "  "     "
    //
    Class_ IO_class = 
	class_(IO, 
	       Object,
	       append_Features(
			       append_Features(
					       append_Features(
							       single_Features(method(out_string, single_Formals(formal(arg, Str)),
										      SELF_TYPE, no_expr())),
							       single_Features(method(out_int, single_Formals(formal(arg, Int)),
										      SELF_TYPE, no_expr()))),
					       single_Features(method(in_string, nil_Formals(), Str, no_expr()))),
			       single_Features(method(in_int, nil_Formals(), Int, no_expr()))),
	       filename);  

    //
    // The Int class has no methods and only a single attribute, the
    // "val" for the integer. 
    //
    Class_ Int_class =
	class_(Int, 
	       Object,
	       single_Features(attr(val, prim_slot, no_expr())),
	       filename);

    //
    // Bool also has only the "val" slot.
    //
    Class_ Bool_class =
	class_(Bool, Object, single_Features(attr(val, prim_slot, no_expr())),filename);

    //
    // The class Str has a number of slots and operations:
    //       val                                  the length of the string
    //       str_field                            the string itself
    //       length() : Int                       returns length of the string
    //       concat(arg: Str) : Str               performs string concatenation
    //       substr(arg: Int, arg2: Int): Str     substring selection
    //       
    Class_ Str_class =
	class_(Str, 
	       Object,
	       append_Features(
			       append_Features(
					       append_Features(
							       append_Features(
									       single_Features(attr(val, Int, no_expr())),
									       single_Features(attr(str_field, prim_slot, no_expr()))),
							       single_Features(method(length, nil_Formals(), Int, no_expr()))),
					       single_Features(method(concat, 
								      single_Formals(formal(arg, Str)),
								      Str, 
								      no_expr()))),
			       single_Features(method(substr, 
						      append_Formals(single_Formals(formal(arg, Int)), 
								     single_Formals(formal(arg2, Int))),
						      Str, 
						      no_expr()))),
	       filename);
}

////////////////////////////////////////////////////////////////////
//
// semant_error is an overloaded function for reporting errors
// during semantic analysis.  There are three versions:
//
//    ostream& ClassTable::semant_error()                
//
//    ostream& ClassTable::semant_error(Class_ c)
//       print line number and filename for `c'
//
//    ostream& ClassTable::semant_error(Symbol filename, tree_node *t)  
//       print a line number and filename
//
///////////////////////////////////////////////////////////////////

ostream& ClassTable::semant_error(Class_ c)
{                                                             
    return semant_error(c->get_filename(),c);
}    

ostream& ClassTable::semant_error(Symbol filename, tree_node *t)
{
    error_stream << filename << ":" << t->get_line_number() << ": ";
    return semant_error();
}

ostream& ClassTable::semant_error()                  
{                                                 
    semant_errors++;                            
    return error_stream;
} 

/*   This is the entry point to the semantic checker.

     Your checker should do the following two things:

     1) Check that the program is semantically correct
     2) Decorate the abstract syntax tree with type information
        by setting the `type' field in each Expression node.
        (see `tree.h')

     You are free to first do 1), make sure you catch all semantic
     errors. Part 2) can be done in a second stage, when you want
     to build mycoolc.
 */
void program_class::semant()
{
    initialize_constants();

    /* ClassTable constructor may do some semantic analysis */
    ClassTable *classtable = new ClassTable(classes);
    if (classtable->errors()) {
	cerr << "Compilation halted due to static semantic errors." << endl;
	exit(1);
    }

    // if code reaches here, then the map is well-formed so now check for cycles
    if (classtable->check_for_cycles()){
	cerr << "Compilation halted due to static semantic errors." << endl;
	exit(1);
    }

    SymbolTable<char *, int> *sym_tab = new SymbolTable<char *, int>();
    MethodTable *method_tab = new MethodTable();
    
    classtable->populate_symbol_table(sym_tab);
    classtable->populate_method_table(method_tab);
}

MethodTable::MethodTable() {
    // nothing to initialize
}

std::set<Symbol> MethodTable::getMethods(Symbol class_name) {
    return class_methods[class_name].methods;
}

bool MethodTable::addMethod(Symbol method_name, Symbol class_name, Class_ c) {
    if(class_methods.count(class_name) > 0) {
        class_methods[class_name].methods.insert(method_name);
    } else {
	method_val val;
	std::set<Symbol> methods;
	methods.insert(method_name);
	val.methods = methods;
	val.c = c;
	class_methods[class_name] = val;
    }
}
