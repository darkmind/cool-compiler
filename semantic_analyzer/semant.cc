#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>
#include "semant.h"
#include "utilities.h"
#include <iostream>
#include <exception>
#include "list.h"

#include <set>
#include <vector>


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
	} else if(name == Object || name == IO || name == Str || name == Int || name == Bool || name == SELF_TYPE) { // redefined basic class
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

bool ClassTable::is_child(Symbol child_name, Symbol class_name) {
    if(child_name == class_name) return true;
    while(class_map[child_name].parent != Object) {
	if (class_map[child_name].parent == class_name) return true;
	child_name = class_map[child_name].parent;
    }
    return false;
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

bool ClassTable::class_exists(Symbol class_name)
{
    return class_map.count(class_name) > 0;
}

Symbol ClassTable::lca(Symbol class1, Symbol class2)
{
    for (Symbol parent = class1; parent != Object; parent = class_map[parent].parent) {
	if (is_child(class2, parent)) return parent;
    }
    return Object;
}

Symbol ClassTable::get_parent(Symbol class_name) {
    return class_map[class_name].parent;
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


/*
 * Global variables for class table, features table, and scoped symbol table here.
 * This way, we don't have cyclic
ClassTable *class_table;
FeatureTable *feature_table;
SymbolTable<Symbol, Symbol *> *symbol_table;

void program_class::semant()
{
    initialize_constants();

    /* ClassTable constructor may do some semantic analysis */
    class_table = new ClassTable(classes);
    if (class_table->errors()) {
	cerr << "Compilation halted due to static semantic errors." << endl;
	exit(1);
    }

    // if code reaches here, then the map is well-formed so now check for cycles
    if (class_table->check_for_cycles()){
	cerr << "Compilation halted due to static semantic errors." << endl;
	exit(1);
    }

    // TODO: check that there is a Main class with a main() method.
    // TODO: check if any differences between archived project 1 test cases and PA1-tests.
    // TODO: also need to add self, and SELF_TYPE in all these places. KV said there are lot of places with self and SELF_TYPE.
    // QN - in type checking, when assigning expression to attribute, need to check type of expression is valid. then when add to symbol table, add the type of expression or static type of attribute?
    // TODO: return Object everywhere there is an error reported that prevents the expression from being evaluated properly
    // TODO: KV said something about adding the parent's methods/attributes into the current scope/local table
    // TODO: check that code

    // go through classes and collect all methods in a method table
    feature_table = new FeatureTable();
    feature_table->populate(classes);

    SemanticAnalyzer *semantic_analyzer = new SemanticAnalyzer();
    symbol_table = new SymbolTable<Symbol, Symbol *>();
    //semantic_analyzer->set_symbol_table(new SymbolTable<Symbol, Symbol>());
    //semantic_analyzer->set_class_table(class_table);
    //semantic_analyzer->set_feature_table(feature_table);
    semantic_analyzer->traverse(classes);
}

SemanticAnalyzer::SemanticAnalyzer() {
    // nothing to initialize
}
/*
void SemanticAnalyzer::set_symbol_table(SymbolTable<Symbol, Symbol> *symbol_tab) {
    symbol_table = symbol_tab;
}

void SemanticAnalyzer::set_class_table(ClassTable *class_tab) {
    class_table = class_tab;
}

void SemanticAnalyzer::set_feature_table(FeatureTable *feature_tab) {
    feature_table = feature_tab;
}
*/

void SemanticAnalyzer::traverse(Classes classes) {
    // here iterate through all the classes and populate the symbol table
    for(int i = classes->first(); classes->more(i); i = classes->next(i)) {
	symbol_table->enterscope(); // new scope per class
	Class_ class_ptr = classes->nth(i);
	// add current class to symbol table
	symbol_table->addid(self, new Symbol(class_ptr->get_name()));
	Features features = class_ptr->get_features();
	check_attributes(features, class_ptr->get_name()); // first, check all attributes
	check_methods(features, class_ptr->get_name()); // then go through methods one by one
	symbol_table->exitscope(); // exit the scope for the class
    }
}

void SemanticAnalyzer::check_attributes(Features features, Symbol class_name) {
    for(int j = features->first(); features->more(j); j = features->next(j)) {
	Feature feature_ptr = features->nth(j);
	int type = get_type_of_feature(feature_ptr);
	if(type == ATTRIBUTE) {
	    attr_class *attribute = dynamic_cast<attr_class *>(feature_ptr);
	    check_attribute(attribute, class_name);
	}
    }
}

void SemanticAnalyzer::check_attribute(attr_class *attribute, Symbol class_name) {
    // check whether exists in parent classes or not
    if(is_attr_in_parent_classes(attribute, class_name)) {
	// TODO: ERROR - attribute already defined in parent classes
    } else {
    	// check in same class whether attribute has been defined or not
    	if(symbol_table->lookup(attribute->get_name()) == NULL) {
	    Symbol expr_type = (attribute->get_init_expr())->eval();
	    if(class_table->is_child(expr_type, attribute->get_type())) {
	        symbol_table->addid(attribute->get_name(), new Symbol(expr_type));
	    } else {
	        // TODO: ERROR - initializing attribute with type that is not <= of static type
	    }
        } else {
	    // TODO: ERROR - attribute already defined
        }
    }
}

bool SemanticAnalyzer::is_attr_in_parent_classes(attr_class *attribute, Symbol class_name) {
    while(class_table->get_parent(class_name) != Object) {
	Symbol parent_name = class_table->get_parent(class_name);
	if (feature_table->get_attributes(parent_name).count(attribute->get_name()) > 0) return true;
	class_name = parent_name;
    }
    return false;
}

feature_type SemanticAnalyzer::get_type_of_feature(Feature feature) {
    attr_class *attribute = dynamic_cast<attr_class *>(feature);
    if(attribute == 0) {
	return METHOD;
    } else {
	return ATTRIBUTE;
    }
}

void SemanticAnalyzer::check_methods(Features features, Symbol class_name) {
    for(int j = features->first(); features->more(j); j = features->next(j)) {
	Feature feature_ptr = features->nth(j);
	int type = get_type_of_feature(feature_ptr);
	if(type == METHOD) {
	    method_class *method = dynamic_cast<method_class *>(feature_ptr);
	    check_method(method, class_name);
	}
    }
}

void SemanticAnalyzer::check_method(method_class *method, Symbol class_name) {
    symbol_table->enterscope(); // new scope per method
    // check whether exists in parent classes or not
    if (method_redefined_with_different_signature(method, class_name)) {
	// TODO: ERROR - method from ancestor class redefined with different signature
    } else {
	// get formals (arguments) and add to current scope so that they are defined in the expression
	Formals formals = method->get_formals();	
	for (int j = formals->first(); formals->more(j); j = formals->next(j)) {
	    Formal curr_formal = formals->nth(j);
	    if (symbol_table->probe(curr_formal->get_name()) == NULL) {
		if (valid_type(curr_formal->get_type())) {
  	    	    symbol_table->addid(curr_formal->get_name(), new Symbol(curr_formal->get_type()));
		} else {
		    symbol_table->addid(curr_formal->get_name(), new Symbol(Object));
		    // TODO: ERROR - undefined type of formal
		}
	    } else {
	        // TODO: ERROR - formal defined again
	    }
	}
        Symbol expr_type = (method->get_expr())->eval();
	if(class_table->is_child(expr_type, method->get_return_type())) {
	    symbol_table->addid(method->get_name(), new Symbol(expr_type));
	} else {
	    // TODO: ERROR - type of expression returned by method is not <= of declared return type
	}
    }
    symbol_table->exitscope(); // exit scope for method
}

bool SemanticAnalyzer::valid_type(Symbol type) {
    return (class_table->class_exists(type));
}

bool SemanticAnalyzer::method_redefined_with_different_signature(method_class *method, Symbol class_name) {
    while(class_table->get_parent(class_name) != Object) {
	Symbol parent_name = class_table->get_parent(class_name);
	if(feature_table->get_methods(parent_name).count(method->get_name()) > 0) {
	    method_class *other_method = feature_table->get_methods(parent_name)[method->get_name()];
	    if(!have_identical_signatures(method, other_method)) return true;	
	}
	class_name = parent_name;
    }
    return false;
}

bool SemanticAnalyzer::have_identical_signatures(method_class *method_one, method_class *method_two) {
    // check same length of arguments
    if(method_one->get_formals()->len() != method_two->get_formals()->len()) return false;
    
    // check same types of arguments
    Formals formals_one = method_one->get_formals();
    Formals formals_two = method_two->get_formals();
    for(int j = formals_one->first(); formals_one->more(j); j = formals_one->next(j)) {
	Formal curr_formal = formals_one->nth(j);
	Formal other_formal = formals_two->nth(j);
	if(curr_formal->get_type() != other_formal->get_type()) return false;
    }

    // check return type is the same for both methods
    if(method_one->get_return_type() != method_two->get_return_type()) return false;

    return true;
}

FeatureTable::FeatureTable() {
    // nothing to initialize
}

std::map<Symbol, method_class *> FeatureTable::get_methods(Symbol class_name) {
    return features[class_name].methods;
}

std::map<Symbol, Symbol> FeatureTable::get_attributes(Symbol class_name) {
    return features[class_name].attributes;
}

bool FeatureTable::method_exists_in_class(Symbol method_name, Symbol class_name) {
    return features[class_name].methods.count(method_name) > 0;
}

void FeatureTable::add_method(Symbol class_name, method_class *method_ptr, Class_ c) {
    Symbol method_name = method_ptr->get_name();
    if(features.count(class_name) > 0) {
	if (method_exists_in_class(method_name, class_name)) {
	    // TODO: Throw error for having the method twice in one class.
        } else { 
	    features[class_name].methods[method_name] = method_ptr;
	}
    } else {
	features_struct new_features;
	new_features.methods[method_name] = method_ptr;
	new_features.c = c;
	features[class_name] = new_features;
    }
}

void FeatureTable::add_attribute(Symbol class_name, attr_class *attr_ptr, Class_ c) {
    Symbol attribute_name = attr_ptr->get_name();
    if(features.count(class_name) > 0) {
	if (features[class_name].attributes.count(attribute_name) > 0) {
	    // TODO: Throw error for having the attribute twice in one class.
        } else { 
	    features[class_name].attributes[attribute_name] = attr_ptr->get_type();
	}
    } else {
	features_struct new_features;
	new_features.attributes[attribute_name] = attr_ptr->get_type();
	new_features.c = c;
	features[class_name] = new_features;
    }
}

void FeatureTable::populate(Classes classes) {
    for (int i = classes->first(); classes->more(i); i = classes->next(i)) {
	Class_ class_ptr = classes->nth(i);
	Features features = class_ptr->get_features();
	for(int j = features->first(); features->more(j); j = features->next(j)) {
	    Feature feature_ptr = features->nth(j);
	    // try to cast to method class type
	    method_class *method_ptr = dynamic_cast<method_class *>(feature_ptr);
	    if(method_ptr != 0) {
	        // a method
		add_method(class_ptr->get_name(), method_ptr, class_ptr);
		//cerr << meth->get_name() << "; " << class_ptr->get_name() << endl;
	    } else {
		// not a method, must be attribute
		attr_class *attr_ptr = dynamic_cast<attr_class *>(feature_ptr);
		add_attribute(class_ptr->get_name(), attr_ptr, class_ptr);
	    }
	}
    }
}

Symbol assign_class::eval() {
    // check that name is defined in symbol table, and that expression is valid type
    Symbol expr_type = expr->eval();
    Symbol type_of_attr = symbol_table->lookup(name);
    if(type_of_attr) {
	// the attribute being assigned to is defined in the symbol table
	if(!is_child(expr_type, type_of_attr)) {
	    // TODO: ERROR - assigning a non-child value to the attribute
	} else {
	    // add to symbol table
	    symbol_table->addid(name, new Symbol(expr_type));
	}
    } else {
	// TODO: ERROR - attribute being assigned to is not defined
    }
    set_type(expr_type);
    return expr_type;
}

Symbol static_dispatch_class::eval() {
    Symbol specified_parent = type_name;
    Symbol expr_type = expr->eval();
    // check whether expr_type is <= of the explicitly defined parent type
    if(!class_table->is_child(expr_type, specified_parent)) {
	// TODO: ERROR - left-most expression is not a child of specified parent type
    }

    if(!class_table->class_exists(specified_parent)) {
	// TODO: ERROR - specified parent does not exist
    } else {
    	if(!feature_table->method_exists_in_class(name, specified_parent)) {
	    // TODO: ERROR - method does not exist in class
	} else {
	    // check that args are valid
	    std::vector<Symbol> arg_types = new std::vector<Symbol>();
	    for(int j = actual->first(); actual->more(j); j = actual->next(j)) {
		Expression curr_expr = actual->nth(j);
		arg_types.push_back(curr_expr->eval());
	    }
	    if(!valid_dispatch_arguments(feature_table->get_methods(specified_parent)[name], arg_types)) {
		// TODO: ERROR - invalid arguments being fed into dispatch call
	    }
	}
    }
    
    // figure out what type to return
    Symbol ret_type;
    Symbol method_ret_type = feature_table->get_methods(specified_parent)[name]->get_return_type();
    if(method_ret_type == SELF_TYPE) {
	ret_type = expr_type;
    } else {
	if(class_table->class_exists(method_ret_type)) {
	    ret_type = method_ret_type;
	} else {
	    // TODO: ERROR - return type of dispatch is not a defined class
	    ret_type = Object;
	}
    }
    set_type(ret_type);
    return ret_type;
}

Symbol dispatch_class::eval() {
    // check that the method exists in the class, and that it has the same arguments
    Symbol expr_type = expr->eval();
    Symbol curr_class;
    if(expr_type == SELF_TYPE) {
	curr_class = symbol_table->lookup(self);
    } else {
	curr_class = expr_type;
    }

    if(!class_table->class_exists(curr_class)) {
	// TODO: ERROR - class does not exist
    } else {
    	if(!feature_table->method_exists_in_class(name, curr_class)) {
	    // TODO: ERROR - method does not exist in class
	} else {
	    // check that args are valid
	    std::vector<Symbol> arg_types = new std::vector<Symbol>();
	    for(int j = actual->first(); actual->more(j); j = actual->next(j)) {
		Expression curr_expr = actual->nth(j);
		arg_types.push_back(curr_expr->eval());
	    }
	    if(!valid_dispatch_arguments(feature_table->get_methods(curr_class)[name], arg_types)) {
		// TODO: ERROR - invalid arguments being fed into dispatch call
	    }
	}
    }
    
    // figure out what type to return
    Symbol ret_type;
    Symbol method_ret_type = feature_table->get_methods(curr_class)[name]->get_return_type();
    if(method_ret_type == SELF_TYPE) {
	ret_type = curr_class;
    } else {
	if(class_table->class_exists(method_ret_type)) {
	    ret_type = method_ret_type;
	} else {
	    // TODO: ERROR - return type of dispatch is not a defined class
	    ret_type = Object;
	}
    }
    set_type(ret_type);
    return ret_type;
}

bool dispatch_class::valid_dispatch_arguments(method_class *method_defn, std::vector<Symbol>& arg_types) {
    // check same number of arguments
    if(method->get_formals()->len() != arg_types.size()) return false;

    // check valid args provided
    Formals formals = method_defn->get_formals();
    for(int j = formals->first(); formals->more(j); j = formals->next(j)) {
	Formal curr_formal = formals->nth(j);
	if(!class_table->is_child(arg_types[j], curr_formal->type_decl)) return false;
    }

    return true;
}

Symbol cond_class::eval() {
    Symbol pred_type = pred->eval();
    if (pred_type != Bool) {
	// TODO: Error about the predicate not being boolean.
    }
    Symbol then_type = then_exp->eval();
    Symbol else_type = else_exp->eval();
    set_type(class_table->lca(then_type, else_type));
    return class_table->lca(then_type, else_type);
}

Symbol loop_class::eval() {
    Symbol pred_type = pred->eval();
    if(pred_type != Bool) {
	// TODO: ERROR - predicate type has to be Bool
    }
    Symbol body_type = body->eval();
    set_type(Object);
    return Object;
}

Symbol typcase_class::eval() {
    // check that variables in all branches have different types
    std::set<Symbol> types = new std::set<Symbol>();
    for (int i = cases->first(); cases->more(i); i = cases->next(i)) {
	Symbol type = cases->nth(i)->get_type();
	if(types.count(type) > 0) {
	    // TODO: ERROR - two branches have the same type declared
	} else {
	    types.insert(type);
	}
    }

    // QN - since the value of expr0 is assigned to one of the id's, do we need to check that all the id's have types that can be assigned the type of the value of expr0?
    // QN - do we need to enterscope() here on case branch? I think we need to..implemented below already.

    Symbol expr_type = expr->eval();

    // find union class of all the expressions of all the branches
    symbol_table->enterscope();
    Case curr_case = cases->nth(cases->first());
    symbol_table->addid(curr_case->name, new Symbol(expr_type));
    Symbol ret_type = curr_case->expr->eval();
    symbol_table->exitscope();
    for (int i = cases->first(); cases->more(i); i = cases->next(i)) {
	symbol_table->enterscope();
	Case curr_case = cases->nth(i);
    	symbol_table->addid(curr_case->name, new Symbol(expr_type));
	ret_type = class_table->lca(ret_type, curr_case->expr->eval());
	symbol_table->exitscope();
    }

    set_type(ret_type);

    return ret_type;
}

Symbol block_class::eval() {
    for(int i = body->first(); body->more(i); i = body->next(i)) {
	if ( !(body->more(body->next(i))) ) {
	    set_type(body->nth(i)->eval());
	    return body->nth(i)->eval();
	}
	body->nth(i)->eval();
    }
}

Symbol let_class::eval() {
    symbol_table->enterscope();
    // eval-ing this init expression will recursively add all the formals defined in this let expression to the current scope
    Symbol init_expr_type = init->eval(symbol_table, class_table, feature_table);
    Symbol type_of_attr;
    if (type_decl == SELF_TYPE) type_of_attr = symbol_table->lookup(self);
    else type_of_attr = symbol_table->lookup(type_decl);
    // check valid type
    if(!class_table->is_child(init_expr_type, type_decl)) {
	// TODO: ERROR - assigning a non-child value to the attribute
    } else {
	// add to symbol table
	symbol_table->addid(identifier, new Symbol(init_expr_type));
    }

    // at this point, all the formals have been added to the symbol table
    Symbol let_return_type = body->eval();
    symbol_table->exitscope();
    set_type(let_return_type);
    return let_return_type;
}

Symbol plus_class::eval() {
    Symbol e1_type = e1->eval();
    Symbol e2_type = e2->eval();
    if(e1_type != Int || e2_type != Int) {
	// TODO: ERROR - cannot add non-int values
    }
    set_type(Int);
    return Int;
}

Symbol sub_class::eval() {
    Symbol e1_type = e1->eval();
    Symbol e2_type = e2->eval();
    if(e1_type != Int || e2_type != Int) {
	// TODO: ERROR - cannot subtract non-int values
    }
    set_type(Int);
    return Int;
}

Symbol mul_class::eval() {
    Symbol e1_type = e1->eval();
    Symbol e2_type = e2->eval();
    if(e1_type != Int || e2_type != Int) {
	// TODO: ERROR - cannot multiply non-int values
    }
    set_type(Int);
    return Int;
}

Symbol divide_class::eval() {
    Symbol e1_type = e1->eval();
    Symbol e2_type = e2->eval();
    if(e1_type != Int || e2_type != Int) {
	// TODO: ERROR - cannot divide non-int values
    }
    set_type(Int);
    return Int;
}

Symbol neg_class::eval() {
    // applies to integers
    Symbol expr_type = e1->eval();
    if(expr_type != Int) {
	// TODO: ERROR - cannot take negation of non-integer
    }
    set_type(Int);
    return Int;
}

Symbol lt_class::eval() {
    Symbol e1_type = e1->eval();
    Symbol e2_type = e2->eval();
    if(e1_type != Int || e2_type != Int) {
	// TODO: ERROR - cannot order non-int values
    }
    set_type(Bool);
    return Bool;
}

Symbol eq_class::eval() {
    Symbol e1_type = e1->eval();
    Symbol e2_type = e2->eval();
    if(e1_type != e2_type) {
	// TODO: ERROR - cannot compare values of different types
    }
    set_type(Bool);
    return Bool;
}

Symbol leq_class::eval() {
    Symbol e1_type = e1->eval();
    Symbol e2_type = e2->eval();
    if(e1_type != Int || e2_type != Int) {
	// TODO: ERROR - cannot order non-int values
    }
    set_type(Bool);
    return Bool;
}

Symbol comp_class::eval() {
    // applies to booleans
    Symbol expr_type = e1->eval();
    if(expr_type != Bool) {
	// TODO: ERROR - cannot take complement of non-boolean
    }
    set_type(Bool);
    return Bool;
}

Symbol int_const_class::eval() {
    set_type(Int);
    return Int;
}

Symbol bool_const_class::eval() {
    set_type(Bool);
    return Bool;
}

Symbol string_const_class::eval() {
    set_type(Str);
    return Str;
}

Symbol new__class::eval() {
    if(type_name == SELF_TYPE) {
	set_type(symbol_table->lookup(self));
	return symbol_table->lookup(self);
    }
    if (class_table->class_exists(type_name)) {
	set_type(type_name);
	return type_name;
    }
    else {
	// TODO: error about undefined type
	set_type(Object);
	return Object;
    }
}

Symbol isvoid_class::eval() {
    e1->eval();
    set_type(Bool);
    return Bool;
}

Symbol no_expr_class::eval() {
    set_type(No_type);
    return No_type;
}

Symbol object_class::eval() {
    Symbol obj_type = symbol_table->lookup(name);
    if (obj_type == NULL) {
	// TODO: print error about undefined object
	set_type(Object);
	return Object;
    }
    if (obj_type == Object) {
	// TODO: print error about bad type
	set_type(Object);
	return Object;
    }
    set_type(obj_type);
    return obj_type;
}
