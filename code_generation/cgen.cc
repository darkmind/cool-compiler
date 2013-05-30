
//**************************************************************
//
// Code generator SKELETON
//
// Read the comments carefully. Make sure to
//    initialize the base class tags in
//       `CgenClassTable::CgenClassTable'
//
//    Add the label for the dispatch tables to
//       `IntEntry::code_def'
//       `StringEntry::code_def'
//       `BoolConst::code_def'
//
//    Add code to emit everyting else that is needed
//       in `CgenClassTable::code'
//
//
// The files as provided will produce code to begin the code
// segments, declare globals, and emit constants.  You must
// fill in the rest.
//
//**************************************************************

#include "cgen.h"
#include "cgen_gc.h"

#include <sstream>
#include <cstring>

extern void emit_string_constant(ostream& str, char *s);
extern int cgen_debug;

//
// Three symbols from the semantic analyzer (semant.cc) are used.
// If e : No_type, then no code is generated for e.
// Special code is generated for new SELF_TYPE.
// The name "self" also generates code different from other references.
//
//////////////////////////////////////////////////////////////////////
//
// Symbols
//
// For convenience, a large number of symbols are predefined here.
// These symbols include the primitive type and method names, as well
// as fixed names used by the runtime system.
//
//////////////////////////////////////////////////////////////////////
Symbol 
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

static char *gc_init_names[] =
  { "_NoGC_Init", "_GenGC_Init", "_ScnGC_Init" };
static char *gc_collect_names[] =
  { "_NoGC_Collect", "_GenGC_Collect", "_ScnGC_Collect" };


//  BoolConst is a class that implements code generation for operations
//  on the two booleans, which are given global names here.
BoolConst falsebool(FALSE);
BoolConst truebool(TRUE);

// curr_label is used for indexing all of our labels
static int curr_label = 0;

// we keep track of the current offset with respect to fp for every new stack variable
static int stack_offset = -1;


/*********************************************************
//
// Define method for code generation
//
// This is the method called by the compiler driver
// `cgtest.cc'. cgen takes an `ostream' to which the assembly will be
// emmitted, and it passes this and the class list of the
// code generator tree to the constructor for `CgenClassTable'.
// That constructor performs all of the work of the code
// generator.
//
*********************************************************/

void program_class::cgen(ostream &os) 
{
  // spim wants comments to start with '#'
  os << "# start of generated code\n";

  initialize_constants();
  CgenClassTable *codegen_classtable = new CgenClassTable(classes,os);

  os << "\n# end of generated code\n";
}


//////////////////////////////////////////////////////////////////////////////
//
//  emit_* procedures
//
//  emit_X  writes code for operation "X" to the output stream.
//  There is an emit_X for each opcode X, as well as emit_ functions
//  for generating names according to the naming conventions (see emit.h)
//  and calls to support functions defined in the trap handler.
//
//  Register names and addresses are passed as strings.  See `emit.h'
//  for symbolic names you can use to refer to the strings.
//
//////////////////////////////////////////////////////////////////////////////

static void emit_load(char *dest_reg, int offset, char *source_reg, ostream& s)
{
  s << LW << dest_reg << " " << offset * WORD_SIZE << "(" << source_reg << ")" 
    << endl;
}

static void emit_store(char *source_reg, int offset, char *dest_reg, ostream& s)
{
  s << SW << source_reg << " " << offset * WORD_SIZE << "(" << dest_reg << ")"
      << endl;
}

static void emit_load_imm(char *dest_reg, int val, ostream& s)
{ s << LI << dest_reg << " " << val << endl; }

static void emit_load_address(char *dest_reg, char *address, ostream& s)
{ s << LA << dest_reg << " " << address << endl; }

static void emit_partial_load_address(char *dest_reg, ostream& s)
{ s << LA << dest_reg << " "; }

static void emit_load_bool(char *dest, const BoolConst& b, ostream& s)
{
  emit_partial_load_address(dest,s);
  b.code_ref(s);
  s << endl;
}

static void emit_load_string(char *dest, StringEntry *str, ostream& s)
{
  emit_partial_load_address(dest,s);
  str->code_ref(s);
  s << endl;
}

static void emit_load_int(char *dest, IntEntry *i, ostream& s)
{
  emit_partial_load_address(dest,s);
  i->code_ref(s);
  s << endl;
}

static void emit_move(char *dest_reg, char *source_reg, ostream& s)
{ s << MOVE << dest_reg << " " << source_reg << endl; }

static void emit_neg(char *dest, char *src1, ostream& s)
{ s << NEG << dest << " " << src1 << endl; }

static void emit_add(char *dest, char *src1, char *src2, ostream& s)
{ s << ADD << dest << " " << src1 << " " << src2 << endl; }

static void emit_addu(char *dest, char *src1, char *src2, ostream& s)
{ s << ADDU << dest << " " << src1 << " " << src2 << endl; }

static void emit_addiu(char *dest, char *src1, int imm, ostream& s)
{ s << ADDIU << dest << " " << src1 << " " << imm << endl; }

static void emit_div(char *dest, char *src1, char *src2, ostream& s)
{ s << DIV << dest << " " << src1 << " " << src2 << endl; }

static void emit_mul(char *dest, char *src1, char *src2, ostream& s)
{ s << MUL << dest << " " << src1 << " " << src2 << endl; }

static void emit_sub(char *dest, char *src1, char *src2, ostream& s)
{ s << SUB << dest << " " << src1 << " " << src2 << endl; }

static void emit_sll(char *dest, char *src1, int num, ostream& s)
{ s << SLL << dest << " " << src1 << " " << num << endl; }

static void emit_jalr(char *dest, ostream& s)
{ s << JALR << "\t" << dest << endl; }

static void emit_jal(char *address,ostream &s)
{ s << JAL << address << endl; }

static void emit_return(ostream& s)
{ s << RET << endl; }

static void emit_gc_assign(ostream& s)
{ s << JAL << "_GenGC_Assign" << endl; }

static void emit_disptable_ref(Symbol sym, ostream& s)
{  s << sym << DISPTAB_SUFFIX; }

static void emit_init_ref(Symbol sym, ostream& s)
{ s << sym << CLASSINIT_SUFFIX; }

static void emit_label_ref(int l, ostream &s)
{ s << "label" << l; }

static void emit_protobj_ref(Symbol sym, ostream& s)
{ s << sym << PROTOBJ_SUFFIX; }

static void emit_method_ref(Symbol classname, Symbol methodname, ostream& s)
{ s << classname << METHOD_SEP << methodname; }

static void emit_label_def(int l, ostream &s)
{
  emit_label_ref(l,s);
  s << ":" << endl;
}

static void emit_beqz(char *source, int label, ostream &s)
{
  s << BEQZ << source << " ";
  emit_label_ref(label,s);
  s << endl;
}

static void emit_beq(char *src1, char *src2, int label, ostream &s)
{
  s << BEQ << src1 << " " << src2 << " ";
  emit_label_ref(label,s);
  s << endl;
}

static void emit_bne(char *src1, char *src2, int label, ostream &s)
{
  s << BNE << src1 << " " << src2 << " ";
  emit_label_ref(label,s);
  s << endl;
}

static void emit_bleq(char *src1, char *src2, int label, ostream &s)
{
  s << BLEQ << src1 << " " << src2 << " ";
  emit_label_ref(label,s);
  s << endl;
}

static void emit_blt(char *src1, char *src2, int label, ostream &s)
{
  s << BLT << src1 << " " << src2 << " ";
  emit_label_ref(label,s);
  s << endl;
}

static void emit_blti(char *src1, int imm, int label, ostream &s)
{
  s << BLT << src1 << " " << imm << " ";
  emit_label_ref(label,s);
  s << endl;
}

static void emit_bgti(char *src1, int imm, int label, ostream &s)
{
  s << BGT << src1 << " " << imm << " ";
  emit_label_ref(label,s);
  s << endl;
}

static void emit_branch(int l, ostream& s)
{
  s << BRANCH;
  emit_label_ref(l,s);
  s << endl;
}

//
// Push a register on the stack. The stack grows towards smaller addresses.
//
static void emit_push(char *reg, ostream& str)
{
  emit_store(reg,0,SP,str);
  emit_addiu(SP,SP,-4,str);
}

//
// Fetch the integer value in an Int object.
// Emits code to fetch the integer value of the Integer object pointed
// to by register source into the register dest
//
static void emit_fetch_int(char *dest, char *source, ostream& s)
{ emit_load(dest, DEFAULT_OBJFIELDS, source, s); }

//
// Emits code to store the integer value contained in register source
// into the Integer object pointed to by dest.
//
static void emit_store_int(char *source, char *dest, ostream& s)
{ emit_store(source, DEFAULT_OBJFIELDS, dest, s); }


static void emit_test_collector(ostream &s)
{
  emit_push(ACC, s);
  emit_move(ACC, SP, s); // stack end
  emit_move(A1, ZERO, s); // allocate nothing
  s << JAL << gc_collect_names[cgen_Memmgr] << endl;
  emit_addiu(SP,SP,4,s);
  emit_load(ACC,0,SP,s);
}

static void emit_gc_check(char *source, ostream &s)
{
  if (source != (char*)A1) emit_move(A1, source, s);
  s << JAL << "_gc_check" << endl;
}


///////////////////////////////////////////////////////////////////////////////
//
// coding strings, ints, and booleans
//
// Cool has three kinds of constants: strings, ints, and booleans.
// This section defines code generation for each type.
//
// All string constants are listed in the global "stringtable" and have
// type StringEntry.  StringEntry methods are defined both for String
// constant definitions and references.
//
// All integer constants are listed in the global "inttable" and have
// type IntEntry.  IntEntry methods are defined for Int
// constant definitions and references.
//
// Since there are only two Bool values, there is no need for a table.
// The two booleans are represented by instances of the class BoolConst,
// which defines the definition and reference methods for Bools.
//
///////////////////////////////////////////////////////////////////////////////

//
// Strings
//
void StringEntry::code_ref(ostream& s)
{
  s << STRCONST_PREFIX << index;
}

//
// Emit code for a constant String.
// You should fill in the code naming the dispatch table.
//

void StringEntry::code_def(ostream& s, int stringclasstag)
{
  IntEntryP lensym = inttable.add_int(len);

  // Add -1 eye catcher
  s << WORD << "-1" << endl;

  code_ref(s);  s  << LABEL                                             // label
      << WORD << stringclasstag << endl                                 // tag
      << WORD << (DEFAULT_OBJFIELDS + STRING_SLOTS + (len+4)/4) << endl // size
      << WORD;
      s << "String"DISPTAB_SUFFIX << endl; 
      s << WORD;  lensym->code_ref(s);  s << endl;            // string length
  emit_string_constant(s,str);                                // ascii string
  s << ALIGN;                                                 // align to word
}

//
// StrTable::code_string
// Generate a string object definition for every string constant in the 
// stringtable.
//
void StrTable::code_string_table(ostream& s, int stringclasstag)
{  
  for (List<StringEntry> *l = tbl; l; l = l->tl())
    l->hd()->code_def(s,stringclasstag);
}

//
// Ints
//
void IntEntry::code_ref(ostream &s)
{
  s << INTCONST_PREFIX << index;
}

//
// Emit code for a constant Integer.
// You should fill in the code naming the dispatch table.
//

void IntEntry::code_def(ostream &s, int intclasstag)
{
  // Add -1 eye catcher
  s << WORD << "-1" << endl;

  code_ref(s);  s << LABEL                                // label
      << WORD << intclasstag << endl                      // class tag
      << WORD << (DEFAULT_OBJFIELDS + INT_SLOTS) << endl  // object size
      << WORD; 
      s << "Int"DISPTAB_SUFFIX << endl;
      s << WORD << str << endl;                           // integer value
}


//
// IntTable::code_string_table
// Generate an Int object definition for every Int constant in the
// inttable.
//
void IntTable::code_string_table(ostream &s, int intclasstag)
{
  for (List<IntEntry> *l = tbl; l; l = l->tl())
    l->hd()->code_def(s,intclasstag);
}


//
// Bools
//
BoolConst::BoolConst(int i) : val(i) { assert(i == 0 || i == 1); }

void BoolConst::code_ref(ostream& s) const
{
  s << BOOLCONST_PREFIX << val;
}
  
//
// Emit code for a constant Bool.
// You should fill in the code naming the dispatch table.
//

void BoolConst::code_def(ostream& s, int boolclasstag)
{
  // Add -1 eye catcher
  s << WORD << "-1" << endl;

  code_ref(s);  s << LABEL                                  // label
      << WORD << boolclasstag << endl                       // class tag
      << WORD << (DEFAULT_OBJFIELDS + BOOL_SLOTS) << endl   // object size
      << WORD;
      s << "Bool"DISPTAB_SUFFIX << endl;
      s << WORD << val << endl;                             // value (0 or 1)
}

//////////////////////////////////////////////////////////////////////////////
//
//  CgenClassTable methods
//
//////////////////////////////////////////////////////////////////////////////

//***************************************************
//
//  Emit code to start the .data segment and to
//  declare the global names.
//
//***************************************************

void CgenClassTable::code_global_data()
{
  Symbol main    = idtable.lookup_string(MAINNAME);
  Symbol string  = idtable.lookup_string(STRINGNAME);
  Symbol integer = idtable.lookup_string(INTNAME);
  Symbol boolc   = idtable.lookup_string(BOOLNAME);

  str << "\t.data\n" << ALIGN;
  //
  // The following global names must be defined first.
  //
  str << GLOBAL << CLASSNAMETAB << endl;
  str << GLOBAL; emit_protobj_ref(main,str);    str << endl;
  str << GLOBAL; emit_protobj_ref(integer,str); str << endl;
  str << GLOBAL; emit_protobj_ref(string,str);  str << endl;
  str << GLOBAL; falsebool.code_ref(str);  str << endl;
  str << GLOBAL; truebool.code_ref(str);   str << endl;
  str << GLOBAL << INTTAG << endl;
  str << GLOBAL << BOOLTAG << endl;
  str << GLOBAL << STRINGTAG << endl;

  //
  // We also need to know the tag of the Int, String, and Bool classes
  // during code generation.
  //
  str << INTTAG << LABEL
      << WORD << intclasstag << endl;
  str << BOOLTAG << LABEL 
      << WORD << boolclasstag << endl;
  str << STRINGTAG << LABEL 
      << WORD << stringclasstag << endl;    
}


//***************************************************
//
//  Emit code to start the .text segment and to
//  declare the global names.
//
//***************************************************

void CgenClassTable::code_global_text()
{
  str << GLOBAL << HEAP_START << endl
      << HEAP_START << LABEL 
      << WORD << 0 << endl
      << "\t.text" << endl
      << GLOBAL;
  emit_init_ref(idtable.add_string("Main"), str);
  str << endl << GLOBAL;
  emit_init_ref(idtable.add_string("Int"),str);
  str << endl << GLOBAL;
  emit_init_ref(idtable.add_string("String"),str);
  str << endl << GLOBAL;
  emit_init_ref(idtable.add_string("Bool"),str);
  str << endl << GLOBAL;
  emit_method_ref(idtable.add_string("Main"), idtable.add_string("main"), str);
  str << endl;
}

void CgenClassTable::code_bools(int boolclasstag)
{
  falsebool.code_def(str,boolclasstag);
  truebool.code_def(str,boolclasstag);
}

void CgenClassTable::code_select_gc()
{
  //
  // Generate GC choice constants (pointers to GC functions)
  //
  str << GLOBAL << "_MemMgr_INITIALIZER" << endl;
  str << "_MemMgr_INITIALIZER:" << endl;
  str << WORD << gc_init_names[cgen_Memmgr] << endl;
  str << GLOBAL << "_MemMgr_COLLECTOR" << endl;
  str << "_MemMgr_COLLECTOR:" << endl;
  str << WORD << gc_collect_names[cgen_Memmgr] << endl;
  str << GLOBAL << "_MemMgr_TEST" << endl;
  str << "_MemMgr_TEST:" << endl;
  str << WORD << (cgen_Memmgr_Test == GC_TEST) << endl;
}


//********************************************************
//
// Emit code to reserve space for and initialize all of
// the constants.  Class names should have been added to
// the string table (in the supplied code, is is done
// during the construction of the inheritance graph), and
// code for emitting string constants as a side effect adds
// the string's length to the integer table.  The constants
// are emmitted by running through the stringtable and inttable
// and producing code for each entry.
//
//********************************************************

void CgenClassTable::code_constants()
{
  //
  // Add constants that are required by the code generator.
  //
  stringtable.add_string("");
  inttable.add_string("0");

  stringtable.code_string_table(str,stringclasstag);
  inttable.code_string_table(str,intclasstag);
  code_bools(boolclasstag);
}


CgenClassTable::CgenClassTable(Classes classes, ostream& s) : nds(NULL) , str(s)
{
   // Debug: We don't know if these tags are used prior to assigning tags, so we set a 
   // weird value here and see if they ever come up. They should be set to correct values
   // immediately when code is called, so it should be fine.
   stringclasstag = 1337 /* Change to your String class tag here */;
   intclasstag =    1337 /* Change to your Int class tag here */;
   boolclasstag =   1337 /* Change to your Bool class tag here */;

   // Here we make the new maps for retrieving class nodes and feature lists, respectively
   class_tags = new std::map<Symbol, CgenNodeP>();
   attr_map = new std::map<Symbol, std::vector<AttrP> *>();
   meth_map = new std::map<Symbol, std::vector<method_dispatch> *>();
   
   // Here we make a map to hold the maximum tag of the descendants of all the classes in the inheritance hierarchy
   max_tags_map = new std::map<Symbol, int>();

   // Symbol table for storing offsets of variables in stack/heap memory
   var_map = new SymbolTable<Symbol, MemoryInfo>();

   enterscope();
   if (cgen_debug) cout << "Building CgenClassTable" << endl;
   install_basic_classes();
   install_classes(classes);
   build_inheritance_tree();

   code();
   exitscope();
}

void CgenClassTable::install_basic_classes()
{

// The tree package uses these globals to annotate the classes built below.
  //curr_lineno  = 0;
  Symbol filename = stringtable.add_string("<basic class>");

//
// A few special class names are installed in the lookup table but not
// the class list.  Thus, these classes exist, but are not part of the
// inheritance hierarchy.
// No_class serves as the parent of Object and the other special classes.
// SELF_TYPE is the self class; it cannot be redefined or inherited.
// prim_slot is a class known to the code generator.
//
  addid(No_class,
	new CgenNode(class_(No_class,No_class,nil_Features(),filename),
			    Basic,this));
  addid(SELF_TYPE,
	new CgenNode(class_(SELF_TYPE,No_class,nil_Features(),filename),
			    Basic,this));
  addid(prim_slot,
	new CgenNode(class_(prim_slot,No_class,nil_Features(),filename),
			    Basic,this));

// 
// The Object class has no parent class. Its methods are
//        cool_abort() : Object    aborts the program
//        type_name() : Str        returns a string representation of class name
//        copy() : SELF_TYPE       returns a copy of the object
//
// There is no need for method bodies in the basic classes---these
// are already built in to the runtime system.
//
  install_class(
   new CgenNode(
    class_(Object, 
	   No_class,
	   append_Features(
           append_Features(
           single_Features(method(cool_abort, nil_Formals(), Object, no_expr())),
           single_Features(method(type_name, nil_Formals(), Str, no_expr()))),
           single_Features(method(copy, nil_Formals(), SELF_TYPE, no_expr()))),
	   filename),
    Basic,this));

// 
// The IO class inherits from Object. Its methods are
//        out_string(Str) : SELF_TYPE          writes a string to the output
//        out_int(Int) : SELF_TYPE               "    an int    "  "     "
//        in_string() : Str                    reads a string from the input
//        in_int() : Int                         "   an int     "  "     "
//
   install_class(
    new CgenNode(
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
	   filename),	    
    Basic,this));

//
// The Int class has no methods and only a single attribute, the
// "val" for the integer. 
//
   install_class(
    new CgenNode(
     class_(Int, 
	    Object,
            single_Features(attr(val, prim_slot, no_expr())),
	    filename),
     Basic,this));

//
// Bool also has only the "val" slot.
//
    install_class(
     new CgenNode(
      class_(Bool, Object, single_Features(attr(val, prim_slot, no_expr())),filename),
      Basic,this));

//
// The class Str has a number of slots and operations:
//       val                                  ???
//       str_field                            the string itself
//       length() : Int                       length of the string
//       concat(arg: Str) : Str               string concatenation
//       substr(arg: Int, arg2: Int): Str     substring
//       
   install_class(
    new CgenNode(
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
	     filename),
        Basic,this));

}

// CgenClassTable::install_class
// CgenClassTable::install_classes
//
// install_classes enters a list of classes in the symbol table.
//
void CgenClassTable::install_class(CgenNodeP nd)
{
  Symbol name = nd->get_name();

  if (probe(name))
    {
      return;
    }

  // The class name is legal, so add it to the list of classes
  // and the symbol table.
  nds = new List<CgenNode>(nd,nds);
  addid(name,nd);
}

void CgenClassTable::install_classes(Classes cs)
{
  for(int i = cs->first(); cs->more(i); i = cs->next(i))
    install_class(new CgenNode(cs->nth(i),NotBasic,this));
}

//
// CgenClassTable::build_inheritance_tree
//
void CgenClassTable::build_inheritance_tree()
{
  for(List<CgenNode> *l = nds; l; l = l->tl())
      set_relations(l->hd());
}

//
// CgenClassTable::set_relations
//
// Takes a CgenNode and locates its, and its parent's, inheritance nodes
// via the class table.  Parent and child pointers are added as appropriate.
//
void CgenClassTable::set_relations(CgenNodeP nd)
{
  CgenNode *parent_node = probe(nd->get_parent());
  nd->set_parentnd(parent_node);
  parent_node->add_child(nd);
}

void CgenNode::add_child(CgenNodeP n)
{
  children = new List<CgenNode>(n,children);
}

void CgenNode::set_parentnd(CgenNodeP p)
{
  assert(parentnd == NULL);
  assert(p != NULL);
  parentnd = p;
}



void CgenClassTable::code()
{
  if (cgen_debug) cout << "assigning class tags" << endl;
  assign_class_tags();

  if (cgen_debug) cout << "populating max tags map" << endl;
  populate_max_tags_map();

  stringclasstag = map_get_class(Str)->nd_get_tag();
  boolclasstag = map_get_class(Bool)->nd_get_tag();
  intclasstag = map_get_class(Int)->nd_get_tag();

  if (cgen_debug) cout << "strings: " << stringclasstag << "; bools: " << boolclasstag << "; ints: " << intclasstag << endl;

  if (cgen_debug) cout << "coding global data" << endl;
  code_global_data();

  if (cgen_debug) cout << "choosing gc" << endl;
  code_select_gc();

  if (cgen_debug) cout << "coding constants" << endl;
  code_constants();

  if (cgen_debug) cout << "coding class names" << endl;
  code_class_names();

  if (cgen_debug) cout << "coding object table" << endl;
  code_object_table();

  if (cgen_debug) cout << "coding dispatch tables" << endl;
  code_dispatch_tables();

  if (cgen_debug) cout << "coding prototypes" << endl;
  code_prototypes();

  if (cgen_debug) cout << "coding global text" << endl;
  code_global_text();

//                 Add your code to emit
//                   - object initializer
//                   - the class methods
//                   - etc...

  if (cgen_debug) cout << "coding object initialization" << endl;
  code_object_inits();

  if (cgen_debug) cout << "coding methods" << endl;
  code_methods();
}

///////////////////////////////////////////////////////////////////////
//
// Coding the global information: class names, object names, dispatch tables, prototypes
//
///////////////////////////////////////////////////////////////////////

// Coding Object Table stuff

// Output the table of class prototypes and init methods
void CgenClassTable::code_object_table() {
    if (cgen_debug) {
        cerr << "emitting class_objTab" << endl;
    }

    str << CLASSOBJTAB << LABEL; // label of class object table section

    // Find the Object CgenNodeP (in list form)
    List<CgenNode> *l = nds;
    while (l && (l->hd()->name != Object )) l = l->tl();

    recurse_object_table(l);
}

void CgenClassTable::recurse_object_table(List<CgenNode> *list) {
    for(List<CgenNode> *l = list; l; l = l->tl()) {
	CgenNodeP nd = l->hd();
	
	// Get current node, look up its entry in the string table and output its str_const
	str << WORD << nd->name << PROTOBJ_SUFFIX << endl;
	str << WORD << nd->name << CLASSINIT_SUFFIX << endl;

        // Get children of the current node
        List<CgenNode> *children = nd->get_children();
    
    	if (cgen_debug) {
	    cerr << "emitting object table stuff for class " << nd->name << ": " << endl;
	    cerr << WORD << nd->name << PROTOBJ_SUFFIX << endl;
	    cerr << WORD << nd->name << CLASSINIT_SUFFIX << endl;
        }
	
        recurse_object_table(children);
    }
}

// Coding Name Table stuff

// Output the table of class names
void CgenClassTable::code_class_names() {
    if (cgen_debug)
        cerr << "emitting class_nameTab" << endl;
    
    str << CLASSNAMETAB << LABEL; // label of class name table section

    // Find the Object CgenNodeP (in list form)
    List<CgenNode> *l = nds;
    while (l && (l->hd()->name != Object )) l = l->tl();

    recurse_class_names(l);
}

/* 
 * This is initially called on the Object Class.
 * It outputs str_consts for each class, by recursing down the inheritance hierarchy.
 * Since each node is already in the inheritance hierarchy, it is guaranteed to be a
 * valid class, so we can just output it.
 */
void CgenClassTable::recurse_class_names(List<CgenNode> *list) {
    StrTable *st = &stringtable;
    for(List<CgenNode> *l = list; l; l = l->tl()) {
	CgenNodeP nd = l->hd();
	
	// Get current node, look up its entry in the string table and output its str_const
	str << WORD;
	st->lookup_string(nd->name->get_string())->code_ref(str);
	str << endl;	

        // Get children of the current node
        List<CgenNode> *children = nd->get_children();
    
    	if (cgen_debug) {
	    cerr << "emitting str_const for class " << nd->name << ": " << endl;
	    st->lookup_string(nd->name->get_string())->code_ref(cerr);
        }
	
        recurse_class_names(children);
    }
}

/*
 * With our method map completed, we can go through and print out each class and its associated methods
 */
void CgenClassTable::code_dispatch_tables() {

    // Iterate through all classes
    for (List<CgenNode> *l = nds; l; l = l->tl()) {

	CgenNodeP nd = l->hd();
	std::vector<method_dispatch> *meth_list = map_get_meth_list(nd->name);

	str << nd->name << DISPTAB_SUFFIX << LABEL; 			// label

	//Iterate through the vector of methods
	for (std::vector<method_dispatch>::iterator it = meth_list->begin(); it != meth_list->end(); ++it) {
	    str << WORD << it->class_name << METHOD_SEP << it->method_name << endl;
	}
    }
}

/*
 * Given our classes, this function goes through and builds prototypes for each of the classes
 * and populates the mapping from class names to tags at the same time
 */
void CgenClassTable::code_prototypes() {

    // iterate through all classes and emit code for all of them
    for (List<CgenNode> *l = nds; l; l = l->tl()) {
	CgenNodeP nd = l->hd();

	if (cgen_debug)
	    cerr << "emitting prototype for " << nd->name << endl;

	// Get the attr_list
	std::vector<AttrP> *attr_list = map_get_attr_list(nd->name);
	
	str << WORD << "-1" << endl;
	str << nd->name << PROTOBJ_SUFFIX << LABEL			// label
	    << WORD << nd->nd_get_tag() << endl         		// class tag
	    << WORD << DEFAULT_OBJFIELDS + attr_list->size() << endl	// object size
	    << WORD << nd->name << DISPTAB_SUFFIX << endl;

	for (std::vector<AttrP>::iterator it = attr_list->begin(); it != attr_list->end(); ++it) {

	    // Printing out all of things that we're pushing to str
	    if (cgen_debug) {
		cerr << "attribute: " << (*it)->name << " of class: " << nd->name << endl;
	    }

	    Symbol type = (*it)->type_decl;
	    str << WORD;

	    // Print out the appropriate initial value for type.
	    if (type == Bool) {
		falsebool.code_ref(str);
	    } else if (type == Str) {
		stringtable.lookup_string("")->code_ref(str);
	    } else if (type == Int) {
		inttable.lookup_string("0")->code_ref(str);
	    } else str << 0;
	    str << endl;
	}
    }
}

/*
 * Walks through our classes, and for each class,
 * generates the assembly code used to initialize them
 */
void CgenClassTable::code_object_inits() {
    List<CgenNode> *l = nds;
    while (l && (l->hd()->name != Object )) l = l->tl();

    recurse_object_inits(l, DEFAULT_OBJFIELDS);
}

void CgenClassTable::recurse_object_inits(List<CgenNode> *list, int counter) {
    for(List<CgenNode> *l = list; l; l = l->tl()) {
    	CgenNodeP nd = l->hd();

    	if (cgen_debug) {
  	    cerr << "emitting object init stuff for class " << nd->name << endl;
      }

    	// Output all the code for the attributes of this node
    	int c = nd->code_init(str, counter, this);

        // Get children of the current node
        List<CgenNode> *children = nd->get_children();

        recurse_object_inits(children, c);
    }
}

/*
 * Generates initialization code for a class. Returns the current value of the counter (attributes written)
 */
int CgenNode::code_init(ostream& str, int counter, CgenClassTableP class_table) {

    str << name << CLASSINIT_SUFFIX << LABEL; //label
    generate_disp_head(str);

    if (cgen_debug) cerr << "resetting the stack offset counter to -1 from: " << stack_offset << endl;
    stack_offset = -1;

    /* Tasks
     * 1. If this isn't Object, call the init of its parent -- DONE
     * 2. If this is a basic class, then we're done. If it's not a basic class, then do the following:
         a. First, load list of attributes. -- DONE

         b. Keep track of which attribute we are at (counter) so that we know which block of memory each attribute is at.
	    Note that this counter is preserved between classes, so if class B inherits A, then if A has 5 attributes, then
	    the counter starts at 5 for class B. I suggest that we modify this function so that we pass in an int for
	    curr_counter.
	 c. For each attribute, if there is an expression that is the initial value, then
	    evaluate the expression into $a0, then stick it into the appropriate attribute slot. The code for this will be filled in later
	    so don't do anything here, with two exceptions:

	    i. If the expression is a string or int or bool, look it up in appropriate table, load into $a0, save into slot
	    ii. If the expression is an ID, then it must be an attribute or inherited attribute. Look it up in the list of attributes,
	        load into $a0, save into slot.

		(actually I'm not sure if they're exceptions)

	 d. I believe that the first attribute starts at 12 + $s0, so when you do SW, the offset
	    should be 3 (DEFAULT_OBJFIELDS) + counter (counter is 0-indexed)

	After all this is done, object initialization is done and we move onto code gen.

     */

    // TODO: EVERYTHING IN BETWEEN
    
    // emit code to jump to init method of the parent of this node
    if (name != Object) {
        CgenNodeP parentnd = get_parentnd();
	
	// emit 'jal parent_init' to initialize the newly instantiated object
        str << JAL;
        emit_init_ref(parentnd->name, str);
	str << endl;
    }

    int initial_counter = counter;
    // not a basic class so we need to get its initialized attributes and output them
    if (!basic()) {
	// get attributes of this class
        std::vector<AttrP> *attributes = get_attributes();

	// we first add all the attributes of this class to our symbol table
        SymbolTable<Symbol, MemoryInfo> *var_map = class_table->var_map;
        var_map->enterscope();

	// Add all the attributes to the current scope, in the heap memory, at their offsets
    	for (std::vector<AttrP>::iterator it = attributes->begin(); it != attributes->end(); ++it) {
    	    AttrP attribute = *it;
	    var_map->addid((*it)->name, new MemoryInfo(counter, Heap));
	    counter++;
	}

	// reset counter
	counter = initial_counter;
	// for each initialized attribute, call the code() function on its init expression to render it to assembly
    	for (std::vector<AttrP>::iterator it = attributes->begin(); it != attributes->end(); ++it) {
    	    AttrP attribute = *it;
    	    if (is_initialized(attribute)) {
		(*it)->init->code(str, class_table); // Returns a pointer to the object that contains the created object
		emit_store(ACC, counter, SELF, str);
    		
      		// for the gc - only run this if the gc is activated (need to put this check in)
      		if (strcmp(gc_init_names[cgen_Memmgr], gc_init_names[1]) == 0) {
		    if (cgen_debug) {
			cerr << "garbage collector is activated. assigning to gc.." << endl;
      		    }
		    emit_addiu(A1, SELF, counter*WORD_SIZE, str);
      		    emit_gc_assign(str);
		}
            }
    	    counter++;
    	}
        var_map->exitscope();

    }

    emit_move(ACC, SELF, str);

    generate_disp_end(str, 0);
    if (cgen_debug) {
	cerr << "Just finished emitting code for class " << name << "; started from offset " << initial_counter*4 << "; ended at offset " << counter*4 << endl;
    }
    return counter;
}

//////////////////////////////////////////////////////////////
//
// Stack Machine Stuff
//
//////////////////////////////////////////////////////////////
// TODO: finish these three functions for method dispatches
void CgenClassTable::code_methods() {
    List<CgenNode> *l = nds;
    while (l && (l->hd()->name != Object )) l = l->tl();

    recurse_methods(l);
}

void CgenClassTable::recurse_methods(List<CgenNode> *list) {
    for(List<CgenNode> *l = list; l; l = l->tl()) {
    	CgenNodeP nd = l->hd();

    	if (cgen_debug) {
  	    cerr << "emitting methods for class " << nd->name << endl;
        }

    	// Output all the code for the methods of this node
	nd->code_all_methods(str, this);

        // Get children of the current node
        List<CgenNode> *children = nd->get_children();

        recurse_methods(children);
    }
}

// Iterate through all the methods for a class, calling code_meth
void CgenNode::code_all_methods(ostream& str, CgenClassTableP c) {
    c->curr_class = name;
    
    std::vector<method_dispatch> *methods = c->map_get_meth_list(name);
    for (std::vector<method_dispatch>::iterator it = methods->begin(); it != methods->end(); ++it) {
	CgenNodeP curr_class = c->map_get_class(it->class_name);
	if (!curr_class->basic()) code_meth(str, c, *it);
    }
}

// Generates the stack machine assembly code for a method
void CgenNode::code_meth(ostream& str, CgenClassTableP c, method_dispatch meth) {

    str << meth.class_name << METHOD_SEP << meth.method_name << LABEL; //label
    generate_disp_head(str);

    if (cgen_debug) cerr << "resetting the stack offset counter to -1 from: " << stack_offset << endl;
    stack_offset = -1;

    if (cgen_debug) cerr << "Currently emitting code for method" << meth.class_name << METHOD_SEP << meth.method_name << endl;

    // Using our symbol table, we prepare to store the offsets of the class' attributes and the method's formals
    SymbolTable<Symbol, MemoryInfo> *var_map = c->var_map;
    var_map->enterscope();

    std::vector<AttrP> *attr_list= c->map_get_attr_list(meth.class_name); // Get all attributes in the current class

    // Iterate through all attributes, record their offset in the heap
    int offset = DEFAULT_OBJFIELDS;
    for (std::vector<AttrP>::iterator it = attr_list->begin(); it != attr_list->end(); ++it) {
	if (cgen_debug) cerr << "Heap contains attribute " << (*it)->name << " at offset" << offset << endl;
	var_map->addid((*it)->name, new MemoryInfo(offset, Heap));
	offset++;
    }

    // TODO: Record the locations of formals in the stack
    Formals formals = meth.method_p->formals;
    offset = DEFAULT_OBJFIELDS + formals->len() - 1; //off by one error
    for (int i = formals->first(); formals->more(i); i = formals->next(i)) {
	Formal formal = formals->nth(i);
	if (cgen_debug) cerr << "Stack contains var " << formal->get_name() << " at offset" << offset << endl;
	var_map->addid(formal->get_name(), new MemoryInfo(offset, Stack));
	offset--;
    }

    if (cgen_debug) cerr << "generating code for body of method" << endl;
    // Generate code for the expression
    meth.method_p->expr->code(str, c);
    var_map->exitscope();
    generate_disp_end(str, formals->len());
}



// Three grossly inefficient functions for looking up shit in the various stringtables

/*
 * TODO: OH LOOK WE SHOULD DELETE THIS

int CgenClassTable::find_in_stringtable(char * str) {
    StrTable *st = &stringtable;
    for (int i = st->first(); st->more(i); i = st->next(i)) {
	if (st->lookup(i) == st->lookup_string(str)) return i;
    }
    return -1; // Not found
}

int CgenClassTable::find_in_inttable(char * str) {
    IntTable *st = &inttable;
    for (int i = st->first(); st->more(i); i = st->next(i)) {
	if (st->lookup(i) == st->lookup_string(str)) return i;
    }
    return -1; // Not found
}

int CgenClassTable::find_in_idtable(char * str) {
    IdTable *st = &idtable;
    for (int i = st->first(); st->more(i); i = st->next(i)) {
	if (st->lookup(i) == st->lookup_string(str)) return i;
    }
    return -1; // Not found
}

*/

/*
 * 1. Iterate through the inheritance graph
 * 2. Add every class to the map with an appropriate tag.
 * 3. After tagging, populate the attributes and methods for each class into the attr_map and meth_map, respectively
 */
void CgenClassTable::assign_class_tags() {

    int curr_tag = 0;
    List<CgenNode> *l = nds;
    
    // Find the Object CgenNodeP (in list form)
    while (l && (l->hd()->name != Object )) l = l->tl();
    recurse_class_tags(l, &curr_tag);
    
    // Create an empty vector representing the zero attributes of Object, and recursively build the map
    // using this as the "seed".
    std::vector<AttrP> *object_attr = new std::vector<AttrP>();
    add_attr_list(Object, object_attr);

    // We populate each of the Object class' children into the attr_map
    populate_attr_map(l->hd()->get_children(), object_attr);

    // Create a vector and populate it with the methods of Object, and recursively build the map
    // using this as the "seed"
    // Note that since Objects have methods, we must call nd_populate_meth_list on this first.
    std::vector<method_dispatch> *object_meth = new std::vector<method_dispatch>();
    l->hd()->nd_populate_meth_list(object_meth);
    add_meth_list(Object, object_meth);

    // We populate each of the Object class' children into the meth_map
    populate_meth_map(l->hd()->get_children(), object_meth);

}

/* 
 * This is initially called on the Object Class.
 * It labels the class, adds it to the class->tag mapping, and outputs the stuff to the stream.
 */
void CgenClassTable::recurse_class_tags(List<CgenNode> *list, int *curr_tag) {

    for(List<CgenNode> *l = list; l; l = l->tl()) {
	CgenNodeP nd = l->hd();
	if (cgen_debug)
	    cerr << "current node: " << nd->name << " ; num siblings: " << list_length<CgenNode>(l) << endl;
	nd->nd_set_tag(*curr_tag);
	map_add_tag(nd->name, nd);
	(*curr_tag)++;
	
	// Get children of the current class
	List<CgenNode> *children = nd->get_children();

	if (cgen_debug) {
	    cerr << "name: " << nd->name << "; tag = " << nd->nd_get_tag() << endl;
	    if (nd->basic()) cerr << "basic class!" << endl;
	    cerr << "CHILDREN" << endl;
	    for (List<CgenNode> *c = children; c; c = c->tl()) {
		cerr << c->hd()->name << endl;
	    }
	}

	// Recurse on the children classes
	recurse_class_tags(children, curr_tag);
    }
}

/* 
 * This is initially called on the Object Class.
 * It labels the class, adds it to the class->tag mapping, and outputs the stuff to the stream.
 */
void CgenClassTable::populate_attr_map(List<CgenNode> *list, std::vector<AttrP> *parent_list) {

    for(List<CgenNode> *l = list; l; l = l->tl()) {
	CgenNodeP nd = l->hd();
	std::vector<AttrP> *child_list = new std::vector<AttrP>(*parent_list);
	nd->nd_populate_attr_list(child_list);
	add_attr_list(nd->name, child_list);
	
	
	// Get children of the current class
	List<CgenNode> *children = nd->get_children();

	if (cgen_debug) {
	    cerr << "name: " << nd->name << endl;
	    // Debug: check the attributes that are being added for each class
	    cerr << "THE ATTRIBUTES OHOHOHOHO" << endl;
	    for (std::vector<AttrP>::iterator it = child_list->begin(); it != child_list->end(); ++it) {
		cerr << (*it)->name << endl;
	    }
	}  

	// Recurse on the children classes
	populate_attr_map(children, child_list);
    }
}

// Adds a CgenNode's attributes to an attr_list
void CgenNode::nd_populate_attr_list(std::vector<AttrP> *attr_list) {
    for (int j = features->first(); features->more(j); j = features->next(j)) {
	Feature feature_ptr = features->nth(j);
	AttrP attribute = dynamic_cast<AttrP>(feature_ptr);
	if (attribute) { // i.e. attribute
	    attr_list->push_back(attribute);
	}
    }
}

// Similar to the populate_attr_map, but for methods
void CgenClassTable::populate_meth_map(List<CgenNode> *list, std::vector<method_dispatch> *parent_list) {

    for(List<CgenNode> *l = list; l; l = l->tl()) {
	CgenNodeP nd = l->hd();
	std::vector<method_dispatch> *child_list = new std::vector<method_dispatch>(*parent_list);
	nd->nd_populate_meth_list(child_list);
	add_meth_list(nd->name, child_list);
	
	
	// Get children of the current class
	List<CgenNode> *children = nd->get_children();

	if (cgen_debug) {
	    cerr << "name: " << nd->name << endl;
	    // Debug: check the attributes that are being added for each class
	    cerr << "THE METHODS OHOHOHOHO" << endl;
	    for (std::vector<method_dispatch>::iterator it = child_list->begin(); it != child_list->end(); ++it) {
		cerr << it->class_name << ": " << it->method_name << endl;
	    }
	}  

	// Recurse on the children classes
	populate_meth_map(children, child_list);
    }
}

// Adds a CgenNode's methods to a meth_list
void CgenNode::nd_populate_meth_list(std::vector<method_dispatch> *meth_list) {
    for (int j = features->first(); features->more(j); j = features->next(j)) {
	Feature feature_ptr = features->nth(j);
	MethP method = dynamic_cast<MethP>(feature_ptr);
	if (method) { // i.e. method
	    method_dispatch to_add;
	    to_add.method_name = method->name;
	    to_add.method_p = method;
	    to_add.class_name = name;
	    // First, check if method already exists in the vector. If it does, remove it.
	    for (std::vector<method_dispatch>::iterator it = meth_list->begin(); it != meth_list->end(); ++it) {
		if (it->method_name == to_add.method_name) { // Method already exists, so we remove this old method to be overwritten
		    it = meth_list->erase(it) - 1; // Erase old method and move it back one so that it doesn't skip the next method
		}
	    }
	    meth_list->push_back(to_add);
	}
    }
}

// for each class in the class hierarchy, compute the maximum tag of all its children
void CgenClassTable::populate_max_tags_map() {
    if (cgen_debug) {
        cerr << "computing max tag descendant of each class in the class hierarchy" << endl;
    }

    // Find the Object CgenNodeP (in list form)
    List<CgenNode> *l = nds;
    while (l && (l->hd()->name != Object )) l = l->tl();

    recurse_max_tags(l->hd());
}

int CgenClassTable::recurse_max_tags(CgenNodeP nd) {
    // Get children of the current node
    List<CgenNode> *children = nd->get_children();

    // if no children of this node, then the max tag is its own tag
    if (list_length<CgenNode>(children) == 0) {
	(*max_tags_map)[nd->name] = nd->tag;
	if (cgen_debug) {
	    cerr << "maximum tag of children of node " << nd->name << ": " << nd->tag << endl;
        }
	return nd->tag;
    }
	
    // This holds the maximum tag of all descendants of the current node
    int max_tag = 0;
	
    for(List<CgenNode> *c = children; c; c = c->tl()) {
	int nd_max = recurse_max_tags(c->hd());
	if (nd_max > max_tag)
	    max_tag = nd_max;
    }

    // store the max_tag in the hashmap with key as the current node
    (*max_tags_map)[nd->name] = max_tag;

    if (cgen_debug) {
	cerr << "maximum tag of children of node " << nd->name << ": " << max_tag << endl;
    }

    return max_tag;
}

// Since apparently the symbols from different tables don't compare very well, we do this workaround
bool CgenClassTable::classes_contains_name(Symbol symbol) {
    for (std::map<Symbol, CgenNodeP>::iterator j = class_tags->begin(); j != class_tags->end(); ++j) {
	if (symbol->equal_string(j->first->get_string(), j->first->get_len())) return true;
    }
    return false;
}


///////////////////////////////////////////////////////////////////////
//
// CgenNode methods
//
///////////////////////////////////////////////////////////////////////


// Generates the assembly code used in the beginning of every method call
void CgenNode::generate_disp_head(ostream& str) {

    // Move the stack pointer down
    emit_addiu(SP, SP, -WORD_SIZE * DEFAULT_REG, str);

    // Store the frame pointer, self pointer, and return address
    emit_store(FP, 3, SP, str);
    emit_store(SELF, 2, SP, str);
    emit_store(RA, 1, SP, str);

    // ????
    emit_addiu(FP, SP, 4, str);
    emit_move(SELF, ACC, str);
}

// Generates the assembly code used in the end of every method call
void CgenNode::generate_disp_end(ostream& str, int args) {

    // ???????

    // Recover the frame pointer, self pointer, and return address
    emit_load(FP, 3, SP, str);
    emit_load(SELF, 2, SP, str);
    emit_load(RA, 1, SP, str);

    // Move stack pointer back, but also shift it up by an additional number of words equal to the number of arguments
    emit_addiu(SP, SP, WORD_SIZE * (DEFAULT_REG + args), str);

    // return
    emit_return(str);
}

std::vector<attr_class *> *CgenNode::get_attributes() {
    std::vector<AttrP> *attributes = new std::vector<AttrP>();
    for (int j = features->first(); features->more(j); j = features->next(j)) {
        // get jth feature of this node
	Feature feature_ptr = features->nth(j);
	// check if it is an attribute by first trying to dynamic_cast it to attr_class
	attr_class *attribute = dynamic_cast<attr_class *>(feature_ptr);
	// if dynamic_cast is successful, then we know that it is indeed an attribute
	// so we can access its 'init' expression variable
	if(attribute)
	    attributes->push_back(attribute);
    }
    return attributes;
}

bool CgenNode::is_initialized(AttrP attribute) {
    no_expr_class *no_init_expr = dynamic_cast<no_expr_class *>(attribute->init);
    if (cgen_debug) {
	if (!no_init_expr) {
	    cerr << "init expression found for attribute " << attribute->name << endl;
	} else {
	    cerr << "init expression not found for attribute " << attribute->name << endl;
	}
    }
    if (!no_init_expr)
	return true;
    return false;
}

//////////////////////////////////////////////////////////////////////
// Holy fucking shit we didn't realize this thing existed.
//////////////////////////////////////////////////////////////////////
CgenNodeP CgenClassTable::root()
{
   return probe(Object);
}


CgenNode::CgenNode(Class_ nd, Basicness bstatus, CgenClassTableP ct) :
   class__class((const class__class &) *nd),
   parentnd(NULL),
   children(NULL),
   basic_status(bstatus)
{ 
   stringtable.add_string(name->get_string());          // Add class name to string table
}

//******************************************************************
//
//   Fill in the following methods to produce code for the
//   appropriate expression.  You may add or remove parameters
//   as you wish, but if you do, remember to change the parameters
//   of the declarations in `cool-tree.h'  Sample code for
//   constant integers, strings, and booleans are provided.
//
//*****************************************************************

void assign_class::code(ostream &s, CgenClassTableP c) {
    // emit code for the expression that is being assigned
    // the return value is the address of the object on the heap that contains
    // the result of the expression
    expr->code(s, c);

    // look up the name in the symbol table to get its position on the heap/stack
    MemoryInfo *info = c->var_map->lookup(name);
    if (info) {
	// copy from $a0 to the offset from FP
	if (info->mem_type == Stack) {
	    // TODO: get the value in the object and store in ACC before next line
	    emit_store(ACC, info->offset, FP, s);
	    if (strcmp(gc_init_names[cgen_Memmgr], gc_init_names[1]) == 0) {
                emit_addiu(A1, FP, info->offset*WORD_SIZE, s);
                emit_gc_assign(s);
            }
	} else {
	    // TODO: get the value in the object and store in ACC before next line
	    emit_store(ACC, info->offset, SELF, s);
	    if (strcmp(gc_init_names[cgen_Memmgr], gc_init_names[1]) == 0) {
                emit_addiu(A1, SELF, info->offset*WORD_SIZE, s);
                emit_gc_assign(s);
            }
	}
    }    
}

// Helper method used to find the index of the method being called. Used in dispatch and static dispatch
static int find_method_index(Symbol method_name, Symbol caller, CgenClassTableP c) {
    if (caller == SELF_TYPE) caller = c->curr_class;
    if (cgen_debug) cerr << "We currently have method: " << method_name << " being called from: " << caller << endl;
    std::vector<method_dispatch> *meth_list = c->map_get_meth_list(caller);
    int offset = 0;

    // Loop through all the methods, return the index when match is found.
    for (std::vector<method_dispatch>::iterator it = meth_list->begin(); it != meth_list->end(); ++it) {
	if (it->method_name == method_name) return offset;
	offset++;
    }
    return -1; // Method not found. This should never happen.
}

// TODO
void static_dispatch_class::code(ostream &s, CgenClassTableP c) {
    // Load the address of the arguments being passed to the dispatch, and push onto stack
    for (int i = actual->first(); actual->more(i); i = actual->next(i)) {
	Expression expr = actual->nth(i);
	
	if (cgen_debug) cerr << "Evaluating an expression in method dispatch " << endl;
	expr->code(s, c);

	// push onto stack
	emit_push(ACC, s);
	stack_offset--;
    }
    
    // evaluate the expression that the method is being called on
    // result will be in $a0
    expr->code(s, c);

    // check if the expression evaluated to something valid
    emit_bne(ACC, ZERO, curr_label, s);

    // if not valid expression, emit code to return file name and line number at which error was found
    emit_load_address(ACC, "str_const0", s); // TODO: check if str_const0 OK

    // Load the line number
    emit_load_imm(T1, get_line_number(), s);

    // Dispatch abort
    emit_jal("_dispatch_abort", s);

    // If expression is valid, do the label============================================= LABEL HERE
    emit_label_def(curr_label, s);
    curr_label++;

    // Load the dispatch table for the parent
    emit_partial_load_address(T1, s);    
    emit_disptable_ref(type_name, s);
    s << endl;

    if (cgen_debug) cerr << "trying to find the method: " << name << " for the static type: " << type_name << endl;

    // Get the method offset in dispatch table and load it
    int offset = find_method_index(name, type_name, c);
    emit_load(T1, offset, T1, s);

    // Call the method
    emit_jalr(T1, s);

}

// TODO
void dispatch_class::code(ostream &s, CgenClassTableP c) {
    // Load the address of the arguments being passed to the dispatch, and push onto stack
    for (int i = actual->first(); actual->more(i); i = actual->next(i)) {
	Expression expr = actual->nth(i);
	
	if (cgen_debug) cerr << "Evaluating an expression in method dispatch " << endl;
	expr->code(s, c);

	// push onto stack
	emit_push(ACC, s);
	stack_offset--;
    }
    
    // evaluate the expression that the method is being called on
    // result will be in $a0
    expr->code(s, c);

    // check if the expression evaluated to something valid
    emit_bne(ACC, ZERO, curr_label, s);

    // if not valid expression, emit code to return file name and line number at which error was found
    emit_load_address(ACC, "str_const0", s); // TODO: check if str_const0 OK

    // Load the line number
    emit_load_imm(T1, get_line_number(), s);

    // Dispatch abort
    emit_jal("_dispatch_abort", s);

    // If expression is valid, do the label============================================= LABEL HERE
    emit_label_def(curr_label, s);
    curr_label++;

    // Load the dispatch table
    emit_load(T1, 2, ACC, s);

    if (cgen_debug) cerr << "trying to find the method: " << name << " for the expr type: " << expr->type << endl;

    // Get the method offset in dispatch table and load it
    int offset = find_method_index(name, expr->type, c);
    emit_load(T1, offset, T1, s);

    // Call the method
    emit_jalr(T1, s);
}

// TODO
void cond_class::code(ostream &s, CgenClassTableP c) {
}

// TODO
void loop_class::code(ostream &s, CgenClassTableP c) {
}

static int highest_case(Cases cases, int *cap, CgenClassTableP c) {
    int curr_cap = -1;
    int index = -1;

    for (int i = cases->first(); cases->more(i); i = cases->next(i)) {
	branch_class curr_branch = cases->nth(i);
	int candidate = c->map_get_class(curr_branch->type_decl)->tag;
	if (candidate > curr_cap && candidate < *cap) {
	    index = i;
	    curr_cap = candidate;
	}
    }

    *cap = curr_cap;
    return index;
}

// TODO
void typcase_class::code(ostream &s, CgenClassTableP c) {

    // Within the confines of this case expression, enter this scope
    SymbolTable<Symbol, MemoryInfo> *var_map = class_table->var_map;
    var_map->enterscope();

    // Save the current label as the "success" label
    int success = curr_label;
    curr_label++;

    // Evaluate our expr
    expr->code(s, c);

    // check if the expression evaluated to something valid
    emit_bne(ACC, ZERO, curr_label, s);

    // if not valid expression, emit code to return file name and line number at which error was found
    emit_load_address(ACC, "str_const0", s); // TODO: check if str_const0 OK

    // Load the line number
    emit_load_imm(T1, get_line_number(), s);

    // Dispatch abort
    emit_jal("_case_abort2", s);

    // Initiate the loop through the branches here
    emit_label_def(curr_label, s);
    curr_label++;

    // Load the class tag of the expr into T2
    emit_load(T2, 0, ACC, s);

    // Set the current highest class tag too high. At every iteration, get the highest class tag that hasn't already been chosen.
    // If it exists, check T2 against this label and its highest descendent. If it is a parent, do the assignment and evaluation.
    // Otherwise, proceed with the next tag.
    int cap = c->num_classes() + 1;
    int index = -1;
    while ((index = highest_case(cases, &cap, c)) > 0) {
	branch_class curr_branch = cases->nth(index);
	int min_tag = c->map_get_class(curr_branch->type_decl)->tag;
	int max_tag = (*(c->max_tags_map))[curr_branch->type_decl];
	emit_blt(T2, min_tag, curr_label, s)
	emit_bgt(T2, max_tag, curr_label, s)

	// If the tag of the class of the current branch is indeed an ancestor of the expression,
	// then we save the expression as the current branch symbol in the stack, then evaluate
	var_map->addid(curr_branch->name, new MemoryInfo(stack_offset, Stack));
	emit_push(ACC, s);
	stack_offset--;

	curr_branch->expr->code(s, c);

	// Fix the stack
        emit_addiu(SP, SP, WORD_SIZE, s);
	stack_offset++;

	// Go to success!
	emit_branch(success, s);

	emit_label_def(curr_label, s);
	curr_label++;
    }

    // The last label is the failure label, no branches were able to match.
    emit_jal("_case_abort")

    emit_label_def(success, s);
    // Exit scope
    var_map->exitscope();

}

void block_class::code(ostream &s, CgenClassTableP c) {
    for (int i = body->first(); body->more(i); i = body->next(i)) {
	Expression expr = body->nth(i);
	if (cgen_debug) cerr << "Coding an expression in block " << endl;
	expr->code(s, c);
    }
}

// TODO
void let_class::code(ostream &s, CgenClassTableP c) {
}

void plus_class::code(ostream &s, CgenClassTableP c) {
    // emit the code for the first expression
    // the return value should be in $a0, and it should be a pointer to an int_constX
    e1->code(s, c);

    // emit 'jal Object.copy' to instantiate the protObj passed in $a0
    emit_jal("Object.copy", s);

    // push the return value (i.e. pointer to newly created object) of e1->code onto the top of the stack
    emit_push(ACC, s);
    stack_offset--;

    // emit the code for the second expression
    e2->code(s, c);

    // load the value of the int in e2 into $a0
    emit_load(ACC, DEFAULT_OBJFIELDS, ACC, s);

    // copy recently pushed onto stack value into register $t1
    emit_load(T1, 1, SP, s);

    // load the value stored in the object pointed to by T1 (i.e. the newly instantiated object) into T1 to carry out addition
    emit_load(T1, DEFAULT_OBJFIELDS, T1, s);

    // add the values stored in $t1 (from eval of first expression) and $a0 (from eval of second expression) and store the result in $a0
    emit_add(ACC, T1, ACC, s);

    // pop the pushed value into register $t1 again and fix the stack
    emit_load(T1, 1, SP, s);
    emit_addiu(SP, SP, WORD_SIZE, s);
    stack_offset++;

    // store the result of the addition into the newly created object i.e. sw $a0 12($t1)
    emit_store(ACC, DEFAULT_OBJFIELDS, T1, s);

    // move the address of the newly created object into $a0 to be returned
    emit_move(ACC, T1, s);
     
}

void sub_class::code(ostream &s, CgenClassTableP c) {
    // emit the code for the first expression
    // the return value should be in $a0, and it should be a pointer to an int object
    e1->code(s, c);

    // emit 'jal Object.copy' to instantiate the protObj passed in $a0
    emit_jal("Object.copy", s);

    // push the return value (i.e. pointer to newly created object) of e1->code onto the top of the stack
    emit_push(ACC, s);
    stack_offset--;

    // emit the code for the second expression
    e2->code(s, c);

    // load the value of the int in e2 into $a0
    emit_load(ACC, DEFAULT_OBJFIELDS, ACC, s);

    // copy recently pushed onto stack value into register $t1
    emit_load(T1, 1, SP, s);

    // load the value stored in the object pointed to by T1 (i.e. the newly instantiated object) into T1 to carry out addition
    emit_load(T1, DEFAULT_OBJFIELDS, T1, s);

    // add the values stored in $t1 (from eval of first expression) and $a0 (from eval of second expression) and store the result in $a0
    emit_sub(ACC, T1, ACC, s);

    // copy recently pushed onto stack value (i.e. address of newly created object) into register $t1 again
    emit_load(T1, 1, SP, s);

    // store the result of the addition into the newly created object
    // i.e. sw $a0 12($t1)
    emit_store(ACC, DEFAULT_OBJFIELDS, T1, s);

    // move the address of the newly created object into $a0 to be returned
    emit_move(ACC, T1, s);
     
    // move the stack back up by WORD_SIZE to get rid of the first intermediary that was pushed onto it
    emit_addiu(SP, SP, WORD_SIZE, s);
    stack_offset++;
}

void mul_class::code(ostream &s, CgenClassTableP c) {
    // emit the code for the first expression
    // the return value should be in $a0, and it should be a pointer to an int object
    e1->code(s, c);

    // emit 'jal Object.copy' to instantiate the protObj passed in $a0
    emit_jal("Object.copy", s);

    // push the return value (i.e. pointer to newly created object) of e1->code onto the top of the stack
    emit_push(ACC, s);
    stack_offset--;

    // emit the code for the second expression
    e2->code(s, c);

    // load the value of the int in e2 into $a0
    emit_load(ACC, DEFAULT_OBJFIELDS, ACC, s);

    // copy recently pushed onto stack value into register $t1
    emit_load(T1, 1, SP, s);

    // load the value stored in the object pointed to by T1 (i.e. the newly instantiated object) into T1 to carry out addition
    emit_load(T1, DEFAULT_OBJFIELDS, T1, s);

    // add the values stored in $t1 (from eval of first expression) and $a0 (from eval of second expression) and store the result in $a0
    emit_mul(ACC, T1, ACC, s);

    // copy recently pushed onto stack value (i.e. address of newly created object) into register $t1 again
    emit_load(T1, 1, SP, s);

    // store the result of the addition into the newly created object
    // i.e. sw $a0 12($t1)
    emit_store(ACC, DEFAULT_OBJFIELDS, T1, s);

    // move the address of the newly created object into $a0 to be returned
    emit_move(ACC, T1, s);
     
    // move the stack back up by WORD_SIZE to get rid of the first intermediary that was pushed onto it
    emit_addiu(SP, SP, WORD_SIZE, s);
    stack_offset++;
}

void divide_class::code(ostream &s, CgenClassTableP c) {
    // emit the code for the first expression
    // the return value should be in $a0, and it should be a pointer to an int object
    e1->code(s, c);

    // emit 'jal Object.copy' to instantiate the protObj passed in $a0
    emit_jal("Object.copy", s);

    // push the return value (i.e. pointer to newly created object) of e1->code onto the top of the stack
    emit_push(ACC, s);
    stack_offset--;

    // emit the code for the second expression
    e2->code(s, c);

    // load the value of the int in e2 into $a0
    emit_load(ACC, DEFAULT_OBJFIELDS, ACC, s);

    // copy recently pushed onto stack value into register $t1
    emit_load(T1, 1, SP, s);

    // load the value stored in the object pointed to by T1 (i.e. the newly instantiated object) into T1 to carry out addition
    emit_load(T1, DEFAULT_OBJFIELDS, T1, s);

    // add the values stored in $t1 (from eval of first expression) and $a0 (from eval of second expression) and store the result in $a0
    emit_div(ACC, T1, ACC, s);

    // copy recently pushed onto stack value (i.e. address of newly created object) into register $t1 again
    emit_load(T1, 1, SP, s);

    // store the result of the addition into the newly created object
    // i.e. sw $a0 12($t1)
    emit_store(ACC, DEFAULT_OBJFIELDS, T1, s);

    // move the address of the newly created object into $a0 to be returned
    emit_move(ACC, T1, s);
     
    // move the stack back up by WORD_SIZE to get rid of the first intermediary that was pushed onto it
    emit_addiu(SP, SP, WORD_SIZE, s);
    stack_offset++;
}

void neg_class::code(ostream &s, CgenClassTableP c) {
    // emit the code for the expression
    // the return value should be in $a0, and it should be a pointer to an int object
    e1->code(s, c);

    // emit 'jal Object.copy' to instantiate the protObj passed in $a0
    emit_jal("Object.copy", s);

    // Load the value of e1 into T1
    emit_load(T1, DEFAULT_OBJFIELDS, ACC, s);

    // Take the negative of T1
    emit_neg(T1, T1, s);

    // Replace the value of a0 with the negative value
    emit_store(T1, DEFAULT_OBJFIELDS, ACC, s);

}

//TODO 
void lt_class::code(ostream &s, CgenClassTableP c) {
    //
}

//TODO
void eq_class::code(ostream &s, CgenClassTableP c) {
}

//TODO
void leq_class::code(ostream &s, CgenClassTableP c) {
}

//TODO
void comp_class::code(ostream &s, CgenClassTableP c) {
}

void int_const_class::code(ostream &s, CgenClassTableP c)  
{
  //
  // Need to be sure we have an IntEntry *, not an arbitrary Symbol
  //
  // this code prints out something like 'la $a0 int_const3', which is exactly what we need
    emit_load_int(ACC,inttable.lookup_string(token->get_string()),s);
}

void string_const_class::code(ostream &s, CgenClassTableP c)
{
    emit_load_string(ACC,stringtable.lookup_string(token->get_string()),s);
}

void bool_const_class::code(ostream &s, CgenClassTableP c)
{
    emit_load_bool(ACC, BoolConst(val), s);
}

void new__class::code(ostream &s, CgenClassTableP c) {

    // For handling SELF_TYPE
    if (type_name == SELF_TYPE) {
	// Load the object table
	emit_load_address(T1, CLASSOBJTAB, s);

	// Load the class tag
	emit_load(T2, 0, SELF, s);

	// Shift over the tag by 3 (2 entries/object * 4 bytes/word = 8)
	emit_sll(T2, T2, 3, s);

	// Moves T1 up to the correct prototype object
	emit_addu(T1, T1, T2, s);

	// Push T1 (pointer to prototype object) onto the stack
	emit_push(T1, s);
	stack_offset--;

	// Load the pointer to the prototype object into ACC
	emit_load(ACC, 0, T1, s);

	// instantiate the object
	emit_jal ("Object.copy", s);

	// Pop the stack (pointer to prototype) to T1, then offset by 4 to get the init method
	emit_load(T1, 1, SP, s);
	emit_addiu(SP, SP, WORD_SIZE, s);
	stack_offset++;

	emit_load(T1, 1, T1, s);

	// Call the init function
	emit_jalr(T1, s);
    } else {
	// emit 'la $a0 Helloworld_protObj' to load protObj into $a0
	emit_partial_load_address(ACC, s);
	emit_protobj_ref(type_name, s);
	s << endl;

	// emit 'jal Object.copy' to instantiate the protObj passed in $a0
	emit_jal ("Object.copy", s);

	// emit 'jal Helloworld_init' to initialize the newly instantiated object
	s << JAL;
	emit_init_ref(type_name, s);
	s << endl;
    }
}

//TODO
void isvoid_class::code(ostream &s, CgenClassTableP c) {

}

//TODO
void no_expr_class::code(ostream &s, CgenClassTableP c) {

}

void object_class::code(ostream &s, CgenClassTableP c) {
    
    // Look up the name in our environment
    MemoryInfo *info = c->var_map->lookup(name);
    if (cgen_debug) {
	cerr << "currently evaluating the identifier: " << name << endl;
	cerr << "node result found in address" << info << endl;
    }

    // If we ever look for self, just load SELF into $a0
    if (name == self) {
	emit_move(ACC, SELF, s);
    } else if (info) {
	// copy to $a0 from the offset from FP
	if (info->mem_type == Stack) {
	    emit_load(ACC, info->offset, FP, s);
	    if (strcmp(gc_init_names[cgen_Memmgr], gc_init_names[1]) == 0) {
                emit_addiu(A1, FP, info->offset*WORD_SIZE, s);
                emit_gc_assign(s);
            }
	} else {
	// copy to $a0 from the offset in SELF
	    emit_load(ACC, info->offset, SELF, s);
	    if (strcmp(gc_init_names[cgen_Memmgr], gc_init_names[1]) == 0) {
                emit_addiu(A1, SELF, info->offset*WORD_SIZE, s);
                emit_gc_assign(s);
            }
	}
    }    
}


