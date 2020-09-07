import java.io.*;
import java.util.*;
import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.util.DefaultTreeForTreeLayout;
import org.abego.treelayout.demo.*;

// **********************************************************************
// The ASTnode class defines the nodes of the abstract-syntax tree that
// represents a Wumbo program.
//
// Internal nodes of the tree contain pointers to children, organized
// either in a list (for nodes that may have a variable number of
// children) or as a fixed set of fields.
//
// The nodes for literals and ids contain line and character number
// information; for string literals and identifiers, they also contain a
// string; for integer literals, they also contain an integer value.
//
// Here are all the different kinds of AST nodes and what kinds of children
// they have.  All of these kinds of AST nodes are subclasses of "ASTnode".
// Indentation indicates further subclassing:
//
//     Subclass            Kids
//     --------            ----
//     ProgramNode         DeclListNode
//     DeclListNode        linked list of DeclNode
//     DeclNode:
//       VarDeclNode       TypeNode, IdNode, int
//       FnDeclNode        TypeNode, IdNode, FormalsListNode, FnBodyNode
//       FormalDeclNode    TypeNode, IdNode
//       StructDeclNode    IdNode, DeclListNode
//
//     FormalsListNode     linked list of FormalDeclNode
//     FnBodyNode          DeclListNode, StmtListNode
//     StmtListNode        linked list of StmtNode
//     ExpListNode         linked list of ExpNode
//
//     TypeNode:
//       IntNode           -- none --
//       BoolNode          -- none --
//       VoidNode          -- none --
//       StructNode        IdNode
//
//     StmtNode:
//       AssignStmtNode      AssignNode
//       PostIncStmtNode     ExpNode
//       PostDecStmtNode     ExpNode
//       ReadStmtNode        ExpNode
//       WriteStmtNode       ExpNode
//       IfStmtNode          ExpNode, DeclListNode, StmtListNode
//       IfElseStmtNode      ExpNode, DeclListNode, StmtListNode,
//                                    DeclListNode, StmtListNode
//       WhileStmtNode       ExpNode, DeclListNode, StmtListNode
//       RepeatStmtNode      ExpNode, DeclListNode, StmtListNode
//       CallStmtNode        CallExpNode
//       ReturnStmtNode      ExpNode
//
//     ExpNode:
//       IntLitNode          -- none --
//       StrLitNode          -- none --
//       TrueNode            -- none --
//       FalseNode           -- none --
//       IdNode              -- none --
//       DotAccessNode       ExpNode, IdNode
//       AssignNode          ExpNode, ExpNode
//       CallExpNode         IdNode, ExpListNode
//       UnaryExpNode        ExpNode
//         UnaryMinusNode
//         NotNode
//       BinaryExpNode       ExpNode ExpNode
//         PlusNode
//         MinusNode
//         TimesNode
//         DivideNode
//         AndNode
//         OrNode
//         EqualsNode
//         NotEqualsNode
//         LessNode
//         GreaterNode
//         LessEqNode
//         GreaterEqNode
//
// Here are the different kinds of AST nodes again, organized according to
// whether they are leaves, internal nodes with linked lists of kids, or
// internal nodes with a fixed number of kids:
//
// (1) Leaf nodes:
//        IntNode,   BoolNode,  VoidNode,  IntLitNode,  StrLitNode,
//        TrueNode,  FalseNode, IdNode
//
// (2) Internal nodes with (possibly empty) linked lists of children:
//        DeclListNode, FormalsListNode, StmtListNode, ExpListNode
//
// (3) Internal nodes with fixed numbers of kids:
//        ProgramNode,     VarDeclNode,     FnDeclNode,     FormalDeclNode,
//        StructDeclNode,  FnBodyNode,      StructNode,     AssignStmtNode,
//        PostIncStmtNode, PostDecStmtNode, ReadStmtNode,   WriteStmtNode
//        IfStmtNode,      IfElseStmtNode,  WhileStmtNode,  CallStmtNode
//        ReturnStmtNode,  DotAccessNode,   AssignExpNode,  CallExpNode,
//        UnaryExpNode,    BinaryExpNode,   UnaryMinusNode, NotNode,
//        PlusNode,        MinusNode,       TimesNode,      DivideNode,
//        AndNode,         OrNode,          EqualsNode,     NotEqualsNode,
//        LessNode,        GreaterNode,     LessEqNode,     GreaterEqNode
//
// **********************************************************************

// **********************************************************************
// %%%ASTnode class (base class for all other kinds of nodes)
// **********************************************************************

abstract class ASTnode {

    // every subclass must provide an unparse operation
    abstract public void unparse(PrintWriter p, int indent);

    // this method can be used by the unparse methods to do indenting
    protected void addIndentation(PrintWriter p, int indent) {
        for (int k=0; k<indent; k++) p.print(" ");
    }

    /**
    abstract public TreeForTreeLayout<TextInBox> buildTree(TreeForTreeLayout<TextInBox> tree) {
        
    }
    */


}

// **********************************************************************
// ProgramNode,  DeclListNode, FormalsListNode, FnBodyNode,
// StmtListNode, ExpListNode
// **********************************************************************

class ProgramNode extends ASTnode {
    public ProgramNode(DeclListNode L) {
        myDeclList = L;
    }
    
    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("declList", 50, 20);
        
        tree.addChild(parent,n1);

        myDeclList.buildTree(tree,n1,1);
        return;
    }

    public SymTable analyze() {

        SymTable symT = new SymTable();
        symT = myDeclList.analyze(symT);
        return symT;
    }


    /**
     * nameAnalysis
     * Creates an empty symbol table for the outermost scope, then processes
     * all of the globals, struct defintions, and functions in the program.
     */
    public void nameAnalysis() {
        SymTable symTab = new SymTable();
        myDeclList.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck() {
        myDeclList.typeCheck();
        if(!myDeclList.checkMain()){
            ErrMsg.fatal(0,0,"No main function");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
    }
    
    public void codeGen(PrintWriter p) {
        myDeclList.codeGen(p);
    }  

    // 1 kid
    private DeclListNode myDeclList;
}

class DeclListNode extends ASTnode {
    public DeclListNode(List<DeclNode> S) {
        myDecls = S;
    }
    
    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent, int nameCode) {

        if(nameCode == 1) {
            for (DeclNode node: myDecls) {
                TextInBox n1 = new TextInBox("decl",30,20);
                TextInBox n2 = new TextInBox("declList",50,20);

                tree.addChild(parent,n1);
                tree.addChild(parent,n2);

                parent = n2;

                node.buildTree(tree,n1);

            }

            tree.addChild(parent,new TextInBox("E",20,20));
        }

        if(nameCode == 2) {
            for (DeclNode node: myDecls) {
                TextInBox n2 = new TextInBox("vList",40,20);
                tree.addChild(parent,n2);
                node.buildTree(tree,parent);

                parent = n2;


            }

            tree.addChild(parent,new TextInBox("E",20,20));
        }

        if(nameCode == 3) {
            
            
            for(int i=0; i<myDecls.size();i++) {
                TextInBox n2 = new TextInBox("strBody",50,20);
                if(i != myDecls.size()-1) {
                    tree.addChild(parent,n2);
                }
                myDecls.get(i).buildTree(tree,parent);

                parent = n2;
            }

        }

        return;
    }

    public SymTable analyze(SymTable symT) {

        Iterator it = myDecls.iterator();
        try {
            while (it.hasNext()) {
            symT = ((DeclNode)it.next()).analyze(symT);
            }
            
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in DeclListNode.analyze");
            System.exit(-1);
        }
        return symT;
    }

    public SymTable createTable(SymTable symT) {
        SymTable structT = new SymTable();
        Iterator it = myDecls.iterator();
	      try {
	        while (it.hasNext()) {
		        structT = ((VarDeclNode)it.next()).create(symT,structT);
	        }
	      } catch (NoSuchElementException ex) {
	        System.err.println("unexpected NoSuchElementException in DeclListNode.analyze");
	        System.exit(-1);
	      }
	    return structT;
    }

    public void codeGen(PrintWriter p) {
        for (DeclNode node: myDecls) {
            node.codeGen(p);
        }
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, process all of the decls in the list.
     */
    public void nameAnalysis(SymTable symTab) {
        nameAnalysis(symTab, symTab);
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab and a global symbol table globalTab
     * (for processing struct names in variable decls), process all of the
     * decls in the list.
     */
    public void nameAnalysis(SymTable symTab, SymTable globalTab) {
        for (DeclNode node : myDecls) {
            if (node instanceof VarDeclNode) {
                ((VarDeclNode)node).nameAnalysis(symTab, globalTab);
            } else {
                node.nameAnalysis(symTab);
            }
        }
    }
    
    public int computeOffsets(SymTable symTab, int offset) {
        for (DeclNode node: myDecls) {
            if(node instanceof VarDeclNode) {
              //try {
                Sym s = ((VarDeclNode)node).getId().sym();
                s.setOffset(offset);
                offset -= 4;
              //} catch (EmptySymTableException ee) {
              //  System.err.println("unexpected EmptySymTableException in FromalsListnode.computeOffsets");
              //  System.exit(-1);
              //} 
            }
        }
        return offset;
    }

    /**
     * typeCheck
     */
    public void typeCheck() {
        for (DeclNode node : myDecls) {
            node.typeCheck();
        }
    }
    
    public boolean checkMain() {
        for(DeclNode node: myDecls) {
          if(node instanceof FnDeclNode) {
              if(((FnDeclNode)node).getId().name().equals("main")) {
                  return true;
              }
          }
        }
        return false;
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator it = myDecls.iterator();
        try {
            while (it.hasNext()) {
                ((DeclNode)it.next()).unparse(p, indent);
            }
        } catch (NoSuchElementException ex) {
            System.err.println("unexpected NoSuchElementException in DeclListNode.print");
            System.exit(-1);
        }
    }

    // list of kids (DeclNodes)
    private List<DeclNode> myDecls;
}

class FormalsListNode extends ASTnode {
    public FormalsListNode(List<FormalDeclNode> S) {
        myFormals = S;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        tree.addChild(parent, new TextInBox("(",15,20));
        if(myFormals.size()>0) {
            TextInBox oParent = parent;
            TextInBox cParent = new TextInBox("fList",40,20);
            
            for(FormalDeclNode node: myFormals) {
                if(!oParent.equals(parent)) {
                    tree.addChild(oParent,new TextInBox(",",10,20));
                }
                tree.addChild(oParent,cParent);
                
                TextInBox cDecl = new TextInBox("fDecl",40,20);
                tree.addChild(cParent,cDecl);
                node.buildTree(tree,cDecl);
                oParent = cParent;
                cParent = new TextInBox("fList",40,20);
            }
        }

        tree.addChild(parent, new TextInBox(")",15,20));


        return;
    }

    public SymTable analyze(SymTable symT) {

        Iterator<FormalDeclNode> it = myFormals.iterator();
        if(it.hasNext()) {
            symT = it.next().analyze(symT);
            while (it.hasNext()) {
            symT = it.next().analyze(symT);
            }
        }
        return symT;
    }

    public String toString(SymTable symT) {
        String param = "";
        Iterator<FormalDeclNode> it = myFormals.iterator();
        if(it.hasNext()) {
            param = it.next().toString(symT);
            while (it.hasNext()) { 
                param = param+","+it.next().toString(symT);
            }
        }
        return param;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * for each formal decl in the list
     *     process the formal decl
     *     if there was no error, add type of formal decl to list
     */
    public List<Type> nameAnalysis(SymTable symTab) {
        List<Type> typeList = new LinkedList<Type>();
        for (FormalDeclNode node : myFormals) {
            Sym sym = node.nameAnalysis(symTab);
            if (sym != null) {
                typeList.add(sym.getType());
            }
        }
        return typeList;
    }
    
    public void computeOffsets(SymTable symTab) {
        int offset = 4;
        for (FormalDeclNode node: myFormals) {
            try {
              Sym s = symTab.lookupLocal(node.getId().name());
              s.setOffset(offset);
              offset += 4;
            } catch (EmptySymTableException ee) {
                System.err.println("unexpected EmptySymTableException in FromalsListnode.computeOffsets");
                System.exit(-1);
            } 
        }
    }

    /**
     * Return the number of formals in this list.
     */
    public int length() {
        return myFormals.size();
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<FormalDeclNode> it = myFormals.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        }
    }

    // list of kids (FormalDeclNodes)
    private List<FormalDeclNode> myFormals;
}

class FnBodyNode extends ASTnode {
    public FnBodyNode(DeclListNode declList, StmtListNode stmtList) {
        myDeclList = declList;
        myStmtList = stmtList;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox nDecl = new TextInBox("vList",40,20);
        TextInBox nStmt = new TextInBox("sList",40,20);
        tree.addChild(parent,nDecl);
        tree.addChild(parent,nStmt);

        myDeclList.buildTree(tree,nDecl,2);
        myStmtList.buildTree(tree,nStmt);

        return;
    }
    

    public void codeGen(PrintWriter p, String name) {
        myStmtList.codeGen(p,name);
    }

    public SymTable analyze(SymTable symT) {

        symT = myDeclList.analyze(symT);
        symT = myStmtList.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * - process the declaration list
     * - process the statement list
     */
    public void nameAnalysis(SymTable symTab) {
        myDeclList.nameAnalysis(symTab);
        myStmtList.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
	    myStmtList.typeCheck(t);
    }

    public void unparse(PrintWriter p, int indent) {
        myDeclList.unparse(p, indent);
        myStmtList.unparse(p, indent);
    }
    
    public int computeOffsets(SymTable symTab) {
        int decls = myDeclList.computeOffsets(symTab,-8);
        int total = myStmtList.computeOffsets(symTab,decls);

        return -(total+8);
    }

    // 2 kids
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class StmtListNode extends ASTnode {
    public StmtListNode(List<StmtNode> S) {
        myStmts = S;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        for(StmtNode node: myStmts) {
            TextInBox nStmt = new TextInBox("stmt",35,20);
            TextInBox nParent = new TextInBox("sList",40,20);

            tree.addChild(parent,nStmt);
            tree.addChild(parent,nParent);

            node.buildTree(tree,nStmt);

            parent = nParent;
        }
            
        tree.addChild(parent,new TextInBox("E",20,20));
        return;
    }

    public int computeOffsets(SymTable symTab, int offset) {
        for(StmtNode node: myStmts) {
            if(node instanceof IfStmtNode) {
                offset = ((IfStmtNode)node).computeOffsets(symTab,offset);
            } else if(node instanceof IfElseStmtNode) {
                offset = ((IfElseStmtNode)node).computeOffsets(symTab,offset);
            } else if(node instanceof WhileStmtNode) {
                offset = ((WhileStmtNode)node).computeOffsets(symTab,offset);

            }
        }
        return offset;
    }
    
    public void codeGen(PrintWriter p, String name) {
        for (StmtNode node: myStmts) {
            node.codeGen(p,name);
        }
    }

    public SymTable analyze(SymTable symT) {
     
        Iterator<StmtNode> it = myStmts.iterator();
          while (it.hasNext()) {
              symT = it.next().analyze(symT);
          }
          return symT;
    }


    /**
     * nameAnalysis
     * Given a symbol table symTab, process each statement in the list.
     */
    public void nameAnalysis(SymTable symTab) {
        for (StmtNode node : myStmts) {
            node.nameAnalysis(symTab);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        for (StmtNode node: myStmts) {
            node.typeCheck(t);
        }
      }

    public void unparse(PrintWriter p, int indent) {
        Iterator<StmtNode> it = myStmts.iterator();
        while (it.hasNext()) {
            it.next().unparse(p, indent);
        }
    }

    // list of kids (StmtNodes)
    private List<StmtNode> myStmts;
}

class ExpListNode extends ASTnode {
    public ExpListNode(List<ExpNode> S) {
        myExps = S;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        
        
            TextInBox oParent = parent;
            TextInBox cParent = new TextInBox("aList",40,20);
            
            for(ExpNode myExp: myExps) {
                if(!oParent.equals(parent)) {
                    tree.addChild(oParent,new TextInBox(",",10,20));
                }
                tree.addChild(oParent,cParent);
                
                TextInBox n1 = new TextInBox("exp",30,20);
                tree.addChild(cParent,n1);

                if(myExp instanceof IdNode) {
                    TextInBox nT = new TextInBox("term",35,20);
                    TextInBox nL = new TextInBox("loc",30,20);
                    TextInBox nID = new TextInBox("id",20,20);
                    tree.addChild(n1,nT);
                    tree.addChild(nT,nL);
                    tree.addChild(nL,nID);
                    myExp.buildTree(tree,nID);
                } else if(myExp instanceof CallExpNode) {
                    TextInBox nT = new TextInBox("term",35,20);
                    TextInBox nC = new TextInBox("fncall",50,20);
                    tree.addChild(n1,nT);
                    tree.addChild(nT,nC);
                    myExp.buildTree(tree,nC);
                } else if(myExp instanceof DotAccessExpNode) {
                    TextInBox nT = new TextInBox("term",35,20);
                    TextInBox nL = new TextInBox("loc",30,20);
                    tree.addChild(n1,nT);
                    tree.addChild(nT,nL);
                    myExp.buildTree(tree,nL);
                } else if (myExp instanceof AssignNode) {
                    TextInBox nA = new TextInBox("aExp",30,20);
                    tree.addChild(n1,nA);
                    myExp.buildTree(tree,nA);
                } else {
                    myExp.buildTree(tree,n1);
                }


                oParent = cParent;
                cParent = new TextInBox("aList",40,20);
            }
        

        


        return;
    }

    public int size() {
        return myExps.size();
    }

    public void codeGen(PrintWriter p) {
        for(int i = myExps.size()-1; i >= 0; i--) {
                myExps.get(i).codeGen(p);
            
        }
    }

    public SymTable analyze(SymTable symT) {
        Iterator<ExpNode> it = myExps.iterator();
        if (it.hasNext()) {
            symT = it.next().analyze(symT);
            while(it.hasNext()) {
                symT = it.next().analyze(symT);
            }
        }    
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, process each exp in the list.
     */
    public void nameAnalysis(SymTable symTab) {
        for (ExpNode node : myExps) {
            node.nameAnalysis(symTab);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(List<Type> typeList) {
        int k = 0;
        try {
            for (ExpNode node : myExps) {
                Type actualType = node.typeCheck();     // actual type of arg

                if (!actualType.isErrorType()) {        // if this is not an error
                    Type formalType = typeList.get(k);  // get the formal type
                    if (!formalType.equals(actualType)) {
                        ErrMsg.fatal(node.lineNum(), node.charNum(),
                                     "Type of actual does not match type of formal");
                    }
                }
                k++;
            }
        } catch (NoSuchElementException e) {
            System.err.println("unexpected NoSuchElementException in ExpListNode.typeCheck");
            System.exit(-1);
        }
    }

    public void unparse(PrintWriter p, int indent) {
        Iterator<ExpNode> it = myExps.iterator();
        if (it.hasNext()) { // if there is at least one element
            it.next().unparse(p, indent);
            while (it.hasNext()) {  // print the rest of the list
                p.print(", ");
                it.next().unparse(p, indent);
            }
        }
    }

    public List<ExpNode> getList() {
        return myExps;
    }

    // list of kids (ExpNodes)
    private List<ExpNode> myExps;
}

// **********************************************************************
// DeclNode and its subclasses
// **********************************************************************

abstract class DeclNode extends ASTnode {

    abstract public void codeGen(PrintWriter p);
    /**
     * Note: a formal decl needs to return a sym
     */
    abstract public Sym nameAnalysis(SymTable symTab);

    abstract public SymTable analyze(SymTable symT);

    abstract public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent);

    // default version of typeCheck for non-function decls
    public void typeCheck() { }
}

class VarDeclNode extends DeclNode {
    public VarDeclNode(TypeNode type, IdNode id, int size) {
        myType = type;
        myId = id;
        mySize = size;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("varDecl",50,20);
        tree.addChild(parent,n1);
        TextInBox n3 = new TextInBox("id",20,20);

        if(myType instanceof StructNode) {
            TextInBox n2 = new TextInBox("id",20,20);
            

            tree.addChild(n1,new TextInBox("STRUCT",60,20));

            tree.addChild(n1,n2);
            tree.addChild(n1,n3);

            tree.addChild(n1,new TextInBox(";",10,20));

            ((StructNode)myType).idNode().buildTree(tree,n2);
            myId.buildTree(tree,n3);

        } else {
            TextInBox n4 = new TextInBox("type",30,20);


            tree.addChild(n1,n4);
            tree.addChild(n1,n3);
            tree.addChild(n1,new TextInBox(";",10,20));

            myType.buildTree(tree,n4);
            myId.buildTree(tree,n3);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        Sym s = myId.sym();
        if(s.getOffset() == 0) {
            p.println("\t.data");
            p.println("\t.align 2");
            p.println("_"+myId.name()+":\t.space 4");
            p.println();
        } else {
            
        }
            
    }

    public SymTable analyze(SymTable symT) {
      
	    try {
	      String type = myType.toString();
	      if(type.equals("void")) {
          ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Non-function declared void");
          if(symT.lookupLocal(myId.toString()) != null) {
              ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
          }
	      } else if(myType instanceof StructNode) {
             
             if(symT.lookupGlobal(type) != null) {
              
               if(symT.lookupGlobal(type).getKind().equals("struct")) {
                
                  Sym s = new Sym(type);
                  s.setKind("struct variable");
                  s.setTable(symT.lookupGlobal(type).getTable());
                  symT.addDecl(myId.toString(),s);
                  
                } else {
                  ErrMsg.fatal(((StructNode)myType).getIdNode().getLineNum(),((StructNode)myType).getIdNode().getCharNum(),"Invalid name of struct type");
                  if(symT.lookupLocal(myId.toString()) != null) {
                    ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
                  }
                }
            } else {
              ErrMsg.fatal(((StructNode)myType).getIdNode().getLineNum(),((StructNode)myType).getIdNode().getCharNum(),"Invalid name of struct type");
              if(symT.lookupLocal(myId.toString()) != null) {
                ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
              }
            } 
        } else {
	        Sym s = new Sym(type);
	        s.setKind("variable");
	        symT.addDecl(myId.toString(),s);
       }
	   
	    } catch (DuplicateSymException de) {
	      ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
	    } catch (IllegalArgumentException ie) {
	      System.err.println("Unexpected illegal argument in VarDeclNode");
	      System.exit(-1);
	    } catch (EmptySymTableException ee) {
	      System.err.println("Unexpected empty sym table in VarDeclNode");
	      System.exit(-1);
	    }
	    return symT;
    }

    public SymTable create(SymTable symT, SymTable structT) {
      
	    try {
	      String type = myType.toString();
	      if(type.equals("void")) {
         
          ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Non-function declared void");
          if(structT.lookupLocal(myId.toString()) != null) {
              ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
          }
	      } else if(myType instanceof StructNode) {
             
            if(symT.lookupGlobal(type) != null) {
                if(symT.lookupGlobal(type).getKind().equals("struct")) {
                    Sym s = new Sym(type);
                    s.setKind("struct variable");
                    s.setTable(symT.lookupGlobal(type).getTable());
                    structT.addDecl(myId.toString(),s);
                } else {
                    ErrMsg.fatal(((StructNode)myType).getIdNode().getLineNum(),((StructNode)myType).getIdNode().getCharNum(),"Invalid name of struct type");
                  }  
          } else {
            ErrMsg.fatal(((StructNode)myType).getIdNode().getLineNum(),((StructNode)myType).getIdNode().getCharNum(),"Invalid name of struct type");
            if(structT.lookupLocal(myId.toString()) != null) {
              ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
            }
          } 
        } else {
          if(structT.lookupLocal(myId.toString()) == null) {
            Sym s = new Sym(type);
	          s.setKind("variable");
	          structT.addDecl(myId.toString(),s);
          } else {
              ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
            }
       }
	   
	    } catch (DuplicateSymException de) {
	      ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
	    } catch (IllegalArgumentException ie) {
	      System.err.println("Unexpected illegal argument in VarDeclNode");
	      System.exit(-1);
	    } catch (EmptySymTableException ee) {
	      System.err.println("Unexpected empty sym table in VarDeclNode");
	      System.exit(-1);
	    }
	    return structT;
    }


    /**
     * nameAnalysis (overloaded)
     * Given a symbol table symTab, do:
     * if this name is declared void, then error
     * else if the declaration is of a struct type,
     *     lookup type name (globally)
     *     if type name doesn't exist, then error
     * if no errors so far,
     *     if name has already been declared in this scope, then error
     *     else add name to local symbol table
     *
     * symTab is local symbol table (say, for struct field decls)
     * globalTab is global symbol table (for struct type names)
     * symTab and globalTab can be the same
     */
    public Sym nameAnalysis(SymTable symTab) {
        return nameAnalysis(symTab, symTab);
    }

    public Sym nameAnalysis(SymTable symTab, SymTable globalTab) {
        boolean badDecl = false;
        String name = myId.name();
        Sym sym = null;
        IdNode structId = null;

        if (myType instanceof VoidNode) {  // check for void type
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                         "Non-function declared void");
            badDecl = true;
        }

        else if (myType instanceof StructNode) {
            structId = ((StructNode)myType).idNode();

            try {
                sym = globalTab.lookupGlobal(structId.name());
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException " +
                                   " in VarDeclNode.nameAnalysis");
            }

            // if the name for the struct type is not found,
            // or is not a struct type
            if (sym == null || !(sym instanceof StructDefSym)) {
                ErrMsg.fatal(structId.lineNum(), structId.charNum(),
                             "Invalid name of struct type");
                badDecl = true;
            }
            else {
                structId.link(sym);
            }
        }

        Sym symCheckMul = null;

        try {
            symCheckMul = symTab.lookupLocal(name);
        } catch (EmptySymTableException ex) {
                            System.err.println("Unexpected EmptySymTableException " +
                                   " in VarDeclNode.nameAnalysis");
        }

        if (symCheckMul != null) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                         "Multiply declared identifier");
            badDecl = true;
        }

        if (!badDecl) {  // insert into symbol table
            try {
                if (myType instanceof StructNode) {
                    sym = new StructSym(structId);
                }
                else {
                    sym = new Sym(myType.type());
                }
                symTab.addDecl(name, sym);
                myId.link(sym);
            } catch (DuplicateSymException ex) {
                System.err.println("Unexpected DuplicateSymException " +
                                   " in VarDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException " +
                                   " in VarDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (IllegalArgumentException ex) {
                System.err.println("Unexpected IllegalArgumentException " +
                                   " in VarDeclNode.nameAnalysis");
                System.exit(-1);
            }
        }

        return sym;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        p.print(myId.name());
        //p.print(myId.getOffset());
        p.println(";");
    }

    public IdNode getId() {
        return myId;
    }
    // 3 kids
    private TypeNode myType;
    private IdNode myId;
    private int mySize;  // use value NOT_STRUCT if this is not a struct type

    public static int NOT_STRUCT = -1;
}

class FnDeclNode extends DeclNode {
    public FnDeclNode(TypeNode type,
                      IdNode id,
                      FormalsListNode formalList,
                      FnBodyNode body) {
        myType = type;
        myId = id;
        myFormalsList = formalList;
        myBody = body;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox nF = new TextInBox("fnDecl",45,20);
        tree.addChild(parent,nF);

        TextInBox nType = new TextInBox("type",30,20);
        TextInBox nID = new TextInBox("id",20,20);
        TextInBox nFormals = new TextInBox("formals",50,20);
        TextInBox nBody = new TextInBox("fnBody",50,20);
        tree.addChild(nF,nType);
        tree.addChild(nF,nID);        
        tree.addChild(nF,nFormals);        
        tree.addChild(nF,nBody);
        
        myType.buildTree(tree,nType);
        myId.buildTree(tree,nID);
        myFormalsList.buildTree(tree,nFormals);
        myBody.buildTree(tree,nBody);

        return;
    }
    
    public void codeGen(PrintWriter p) {
        p.println("\t.text");
        if(myId.name().equals("main")) {
            
            p.println("\t.globl main");
            p.println("main:");
            p.println("__start:");
        } else {
            p.println("_"+myId.name()+":");
        }
        Codegen.genPush("$ra");
        Codegen.genPush("$fp");
        Codegen.generate("add","$fp","$sp",8);
        Codegen.generate("sub","$sp","$sp",myId.sym().getOffset());

        
        myBody.codeGen(p,myId.name());
        
        p.println("_"+myId.name()+"_Exit:");
        Codegen.generate("lw","$ra","0($fp)");
        Codegen.generate("move","$t0","$fp");
        Codegen.generate("lw","$fp","-4($fp)");
        Codegen.generate("move","$sp","$t0");
        
        if(myId.name().equals("main")) {
            Codegen.generate("li","$v0","10");
            Codegen.generate("syscall");
        } else {
            Codegen.generate("jr","$ra");
        }
        
        
        p.println();
    }

    public SymTable analyze(SymTable symT) {
	    
	    try {
        String type = myType.toString();
	      Sym s = new Sym(type);
	      s.setKind("function");
        symT.addScope();
	      String param = myFormalsList.toString(symT);
	    symT.removeScope();
        s.setFunc(param);
        
	      symT.addDecl(myId.toString(),s);
          symT.addScope();
          
	      symT = myFormalsList.analyze(symT);
          symT = myBody.analyze(symT);
          symT.addDecl("$scope",new Sym(myId.toString()));
	      symT.removeScope();
	    } catch(IllegalArgumentException ie) {
	      System.err.println("Unexpected IllegalArgumentException in FnDeclNode.analyze");
	      System.exit(-1);
	    } catch(DuplicateSymException de) {
        ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
        try {
          symT.addScope();
            
	        symT = myFormalsList.analyze(symT);
            symT = myBody.analyze(symT);
            symT.addDecl("$scope",new Sym(myId.toString()));
	        symT.removeScope();
        } catch(EmptySymTableException ee) {
          System.err.println("Unexpected EmptySymTableException in FnDeclNode.analyze");
          System.exit(-1);
        } catch(DuplicateSymException de2) {
            System.err.println("Unexpeced exception in FnDeclNode.analyze");
        }
	    } catch(EmptySymTableException ee) {
	      System.err.println("Unexpected EmptySymTableException in FnDeclNode.analyze");
	      System.exit(-1);
	    } 
    
	  return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * if this name has already been declared in this scope, then error
     * else add name to local symbol table
     * in any case, do the following:
     *     enter new scope
     *     process the formals
     *     if this function is not multiply declared,
     *         update symbol table entry with types of formals
     *     process the body of the function
     *     exit scope
     */
    public Sym nameAnalysis(SymTable symTab) {
        String name = myId.name();
        FnSym sym = null;
        Sym symCheckMul = null;

        try {
            symCheckMul = symTab.lookupLocal(name);
        } catch (EmptySymTableException ex) {
                            System.err.println("Unexpected EmptySymTableException " +
                                   " in FnDeclNode.nameAnalysis");
        }

        if (symCheckMul != null) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                         "Multiply declared identifier");
        }

        else { // add function name to local symbol table
            try {
                sym = new FnSym(myType.type(), myFormalsList.length());
                symTab.addDecl(name, sym);
                myId.link(sym);
            } catch (DuplicateSymException ex) {
                System.err.println("Unexpected DuplicateSymException " +
                                   " in FnDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException " +
                                   " in FnDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (IllegalArgumentException ex) {
                System.err.println("Unexpected IllegalArgumentException " +
                                   " in FnDeclNode.nameAnalysis");
                System.exit(-1);
            }
        }

        symTab.addScope();  // add a new scope for locals and params

        // process the formals
        List<Type> typeList = myFormalsList.nameAnalysis(symTab);
        if (sym != null) {
            sym.addFormals(typeList);
        }

        myBody.nameAnalysis(symTab); // process the function body
        myFormalsList.computeOffsets(symTab);
        myId.sym().setOffset(myBody.computeOffsets(symTab));

        try {
            symTab.removeScope();  // exit scope
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException " +
                               " in FnDeclNode.nameAnalysis");
            System.exit(-1);
        }
        
        

        return null;
    }

    /**
     * typeCheck
     */
    public void typeCheck() {
        myBody.typeCheck(myType.type());
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myType.unparse(p, 0);
        p.print(" ");
        p.print(myId.name());
        p.print("(");
        myFormalsList.unparse(p, 0);
        p.println(") {");
        myBody.unparse(p, indent+4);
        p.println("}\n");
    }
    
    public IdNode getId() {
        return myId;
    }

    // 4 kids
    private TypeNode myType;
    private IdNode myId;
    private FormalsListNode myFormalsList;
    private FnBodyNode myBody;
}

class FormalDeclNode extends DeclNode {
    public FormalDeclNode(TypeNode type, IdNode id) {
        myType = type;
        myId = id;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox nT = new TextInBox("type",30,20);
        TextInBox nID = new TextInBox("id",20,20);

        tree.addChild(parent,nT);
        tree.addChild(parent,nID);

        myType.buildTree(tree,nT);
        myId.buildTree(tree,nID);

        return;
    }
    
    public void codeGen(PrintWriter p) {
    
    }

    public SymTable analyze(SymTable symT) {
	    
	    try {
        if(myType.toString().equals("void")) {
            ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Non-function declared void");
            if(symT.lookupLocal(myId.toString()) != null) {
              ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
            }
        }
	      else {
            Sym s = new Sym(myType.toString());
	          s.setKind("variable");
	          symT.addDecl(myId.toString(),s);
       }
	    } catch(IllegalArgumentException ie) {
	      System.err.println("Unexpected IllegalArgumentException in FormalDeclNode.analyze");
	      System.exit(-1);
	    } catch(EmptySymTableException ee) {
	      System.err.println("Unexpected EmptySymTableException in FormalDeclNode.analyze");
	    } catch(DuplicateSymException de) {
        ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
	    }
    	return symT;
    }

    public String toString(SymTable symT) {
        String paran = "";
        SymTable placholder = symT;
        try {
          if(myType.toString().equals("void")) {
        
          }
            else {
              Sym s = new Sym(myType.toString());
                symT.addDecl(myId.toString(),s);
              paran = myType.toString();         
         }
          } catch(IllegalArgumentException ie) {
            System.err.println("Unexpected IllegalArgumentException in FormalDeclNode.analyze");
            System.exit(-1);
          } catch(EmptySymTableException ee) {
            System.err.println("Unexpected EmptySymTableException in FormalDeclNode.analyze");
          } catch(DuplicateSymException de) {
          }
          return paran;
      
      }



    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * if this formal is declared void, then error
     * else if this formal is already in the local symble table,
     *     then issue multiply declared error message and return null
     * else add a new entry to the symbol table and return that Sym
     */
    public Sym nameAnalysis(SymTable symTab) {
        String name = myId.name();
        boolean badDecl = false;
        Sym sym = null;

        if (myType instanceof VoidNode) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                         "Non-function declared void");
            badDecl = true;
        }

        Sym symCheckMul = null;

        try {
            symCheckMul = symTab.lookupLocal(name);
        } catch (EmptySymTableException ex) {
                            System.err.println("Unexpected EmptySymTableException " +
                                   " in FormalDeclNode.nameAnalysis");
        }

        if (symCheckMul != null) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                         "Multiply declared identifier");
            badDecl = true;
        }

        if (!badDecl) {  // insert into symbol table
            try {
                sym = new Sym(myType.type());
                symTab.addDecl(name, sym);
                myId.link(sym);
            } catch (DuplicateSymException ex) {
                System.err.println("Unexpected DuplicateSymException " +
                                   " in FormalDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException " +
                                   " in FormalDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (IllegalArgumentException ex) {
                System.err.println("Unexpected IllegalArgumentException " +
                                   " in FormalDeclNode.nameAnalysis");
                System.exit(-1);
            }
	      }

        return sym;
    }
    
    public IdNode getId() {
        return myId;
    }

    public void unparse(PrintWriter p, int indent) {
        myType.unparse(p, 0);
        p.print(" ");
        p.print(myId.name());
        //p.print(myId.getOffset());
    }

    // 2 kids
    private TypeNode myType;
    private IdNode myId;
}

class StructDeclNode extends DeclNode {
    public StructDeclNode(IdNode id, DeclListNode declList) {
        myId = id;
        myDeclList = declList;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox nStruct = new TextInBox("strDecl",50,20);
        TextInBox nID = new TextInBox("id",20,20);
        TextInBox nBody = new TextInBox("strBody",50,20);
        

        tree.addChild(parent,nStruct);

        tree.addChild(nStruct,new TextInBox("STRUCT",60,20));
        tree.addChild(nStruct,nID);

        tree.addChild(nStruct,new TextInBox("{",15,20));     
        tree.addChild(nStruct,nBody);   
        tree.addChild(nStruct,new TextInBox("}",15,20));  

        tree.addChild(nStruct,new TextInBox(";",10,20));
        
        myId.buildTree(tree,nID);
        myDeclList.buildTree(tree,nBody,3);
        return;
    }
    
    public void codeGen(PrintWriter p) {
    
    }

    public SymTable analyze(SymTable symT) {
      
	    try {
	      String type = myId.toString();
       
        if(symT.lookupGlobal(type) == null) {
          Sym s = new Sym(type);
          s.setKind("struct");
          symT.addScope();
          SymTable structT = myDeclList.createTable(symT);
          structT.addDecl("$scope",new Sym(myId.toString()));
          symT.addGarbage(structT);
          symT.removeScope();
          s.setTable(structT);
          symT.addDecl(type,s);
        } else {
          ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
          symT.addScope();
          SymTable structT = myDeclList.createTable(symT);
          structT.addDecl("$scope",new Sym(myId.toString()));
          symT.addGarbage(structT);
          symT.removeScope();
        }
        
	   
	    } catch (DuplicateSymException de) {
	      ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Multiply declared identifier");
	    } catch (IllegalArgumentException ie) {
	      System.err.println("Unexpected illegal argument in VarDeclNode");
	      System.exit(-1);
	    } catch (EmptySymTableException ee) {
	      System.err.println("Unexpected empty sym table in VarDeclNode");
	      System.exit(-1);
	    }
	    return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * if this name is already in the symbol table,
     *     then multiply declared error (don't add to symbol table)
     * create a new symbol table for this struct definition
     * process the decl list
     * if no errors
     *     add a new entry to symbol table for this struct
     */
    public Sym nameAnalysis(SymTable symTab) {
        String name = myId.name();
        boolean badDecl = false;

        Sym symCheckMul = null;

        try {
            symCheckMul = symTab.lookupLocal(name);
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException " +
                           " in StructDeclNode.nameAnalysis");
        }

        if (symCheckMul != null) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                         "Multiply declared identifier");
            badDecl = true;
        }


        if (!badDecl) {
            try {   // add entry to symbol table
                SymTable structSymTab = new SymTable();
                myDeclList.nameAnalysis(structSymTab, symTab);
                StructDefSym sym = new StructDefSym(structSymTab);
                symTab.addDecl(name, sym);
                myId.link(sym);
            } catch (DuplicateSymException ex) {
                System.err.println("Unexpected DuplicateSymException " +
                                   " in StructDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException " +
                                   " in StructDeclNode.nameAnalysis");
                System.exit(-1);
            } catch (IllegalArgumentException ex) {
                System.err.println("Unexpected IllegalArgumentException " +
                                   " in StructDeclNode.nameAnalysis");
                System.exit(-1);
            }
        }

        return null;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("struct ");
        p.print(myId.name());
        p.println("{");
        myDeclList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("};\n");

    }

    // 2 kids
    private IdNode myId;
    private DeclListNode myDeclList;
}

// **********************************************************************
// TypeNode and its Subclasses
// **********************************************************************

abstract class TypeNode extends ASTnode {
    /* all subclasses must provide a type method */
    abstract public Type type();

    abstract public String toString();

    abstract public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent);
}

class IntNode extends TypeNode {
    public IntNode() {
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        tree.addChild(parent,new TextInBox("INT",30,20));
        return;
    }

    public String toString() {
        return "int";
    }

    /**
     * type
     */
    public Type type() {
        return new IntType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("int");
    }
}

class BoolNode extends TypeNode {
    public BoolNode() {
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        tree.addChild(parent,new TextInBox("BOOL",40,20));
        return;
    }

    public String toString() {
        return "bool";
    }

    /**
     * type
     */
    public Type type() {
        return new BoolType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("bool");
    }
}

class VoidNode extends TypeNode {
    public VoidNode() {
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        tree.addChild(parent,new TextInBox("VOID",40,20));
        return;
    }

    public String toString() {
        return "void";
    }

    /**
     * type
     */
    public Type type() {
        return new VoidType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("void");
    }
}

class StructNode extends TypeNode {
    public StructNode(IdNode id) {
        myId = id;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {

        return;
    }

    public String toString() {
	    return myId.toString();
    }

    public IdNode idNode() {
        return myId;
    }

    public IdNode getIdNode() {
        return myId;
    }


    /**
     * type
     */
    public Type type() {
        return new StructType(myId);
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("struct ");
        p.print(myId.name());
    }

    // 1 kid
    private IdNode myId;
}

// **********************************************************************
// StmtNode and its subclasses
// **********************************************************************

abstract class StmtNode extends ASTnode {
    abstract public void nameAnalysis(SymTable symTab);
    abstract public SymTable analyze(SymTable symT);
    abstract public void typeCheck(Type t);
    abstract public void codeGen(PrintWriter p, String name);
    abstract public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent);
}

class AssignStmtNode extends StmtNode {
    public AssignStmtNode(AssignNode assign) {
        myAssign = assign;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n = new TextInBox("aExp",40,20);
        tree.addChild(parent,n);
        myAssign.buildTree(tree,n);
        return;
    }
    
    public void codeGen(PrintWriter p, String name) {
            p.println("\t\t#ASSIGN STMT");
            myAssign.codeGen(p);
            Codegen.genPop("$t0");
    }

    public SymTable analyze(SymTable symT) {
        
        symT = myAssign.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myAssign.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        myAssign.getType();
  }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myAssign.unparse(p, -1); // no parentheses
        p.println(";");
    }

    // 1 kid
    private AssignNode myAssign;
}

class PostIncStmtNode extends StmtNode {
    public PostIncStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n = new TextInBox("loc",30,20);
        
        tree.addChild(parent,n);
        tree.addChild(parent,new TextInBox("++",20,20));
        tree.addChild(parent,new TextInBox(";",10,20));

        if(myExp instanceof IdNode) {
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n,nID);
            myExp.buildTree(tree,nID);
        } else {
            myExp.buildTree(tree,n);
        }

        return;
    }
    
    public void codeGen(PrintWriter p, String name) {
        p.println("\t\t#iNCREMENT STMT");
        myExp.codeGen(p);
        ((IdNode)myExp).genAddr(p);
        Codegen.genPop("$t1");
        Codegen.genPop("$t0");
        Codegen.generate("addi","$t0","$t0","1");
        Codegen.generateIndexed("sw","$t0","$t1",0);
    
    }

    public SymTable analyze(SymTable symT) {
        symT = myExp.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        Type t1 = myExp.getType();
        if(t1.equals(new ErrorType()) || t1.equals(new IntType())) {
            return;
        } else {
            ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Arithmetic operator applied to non-numeric operand");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myExp.unparse(p, 0);
        p.println("++;");
    }

    // 1 kid
    private ExpNode myExp;
}

class PostDecStmtNode extends StmtNode {
    public PostDecStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n = new TextInBox("loc",30,20);
        
        tree.addChild(parent,n);
        tree.addChild(parent,new TextInBox("--",20,20));
        tree.addChild(parent,new TextInBox(";",10,20));

        if(myExp instanceof IdNode) {
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n,nID);
            myExp.buildTree(tree,nID);
        } else {
            myExp.buildTree(tree,n);
        }


        return;
    }
    
    public void codeGen(PrintWriter p, String name) {
        p.println("\t\t#DECREMENT STMT");
        myExp.codeGen(p);
        ((IdNode)myExp).genAddr(p);
        Codegen.genPop("$t1");
        Codegen.genPop("$t0");
        Codegen.generate("addi","$t0","$t0","-1");
        Codegen.generateIndexed("sw","$t0","$t1",0);
    
    }

    public SymTable analyze(SymTable symT) {
        symT = myExp.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        Type t1 = myExp.getType();
        if(t1.equals(new ErrorType()) || t1.equals(new IntType())) {
            return;
        } else {
            ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Arithmetic operator applied to non-numeric operand");
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myExp.unparse(p, 0);
        p.println("--;");
    }

    // 1 kid
    private ExpNode myExp;
}

class ReadStmtNode extends StmtNode {
    public ReadStmtNode(ExpNode e) {
        myExp = e;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox nLoc = new TextInBox("loc",30,20);

        tree.addChild(parent,new TextInBox("CIN",30,20));
        tree.addChild(parent,new TextInBox(">>",20,20));
        tree.addChild(parent,nLoc);

        tree.addChild(parent,new TextInBox(";",10,20));

        if(myExp instanceof IdNode) {
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(nLoc,nID);
            myExp.buildTree(tree,nID);
        } else {
            myExp.buildTree(tree,nLoc);
        }

        return;
    }

    public SymTable analyze(SymTable symT) {
        symT = myExp.analyze(symT);
        return symT;
    }
    
    public void codeGen(PrintWriter p, String name) {
        p.println("\t\t#READ STMT");
        Codegen.generate("li","$v0",5);
        Codegen.generate("syscall");
        Codegen.genPush("$v0");
        ((IdNode)myExp).genAddr(p);
        Codegen.genPop("$t0");
        Codegen.genPop("$t1");
        Codegen.generateIndexed("sw","$t1","$t0",0);

    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        if(myExp.getType().equals(new ErrorType())) {
            return;
        }
        if(myExp instanceof IdNode) {
            Sym s = ((IdNode)myExp).sym();
            if(s instanceof FnSym) {
                ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Attempt to read a function");
            } else if(s instanceof StructDefSym) {
                ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Attempt to read a struct name");
            } else if(s instanceof StructSym) {
                ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Attempt to read a struct variable");
            }
        } 
        return;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("cin >> ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    // 1 kid (actually can only be an IdNode or an ArrayExpNode)
    private ExpNode myExp;
}

class WriteStmtNode extends StmtNode {
    public WriteStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);

        tree.addChild(parent,new TextInBox("COUT",40,20));
        tree.addChild(parent,new TextInBox("<<",20,20));
        tree.addChild(parent,n1);

        tree.addChild(parent,new TextInBox(";",10,20));

        if(myExp instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp.buildTree(tree,nID);
        } else if(myExp instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp.buildTree(tree,nC);
        } else if(myExp instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp.buildTree(tree,nL);
        } else if (myExp instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp.buildTree(tree,nA);
        } else {
            myExp.buildTree(tree,n1);
        }
        return;
    }
    
    public void codeGen(PrintWriter p, String name) {
        p.println("\t\t#WRITE STMT");
        myExp.codeGen(p);
        int code = 1;
        if(myExp.typeCheck().equals(new IntType()) || myExp.typeCheck().equals(new BoolType())) {
            code = 1;
        } else if(myExp.typeCheck().equals(new StringType())) {
            code = 4;
        }
        
        Codegen.genPop("$a0");
        Codegen.generate("li","$v0",code);
        Codegen.generate("syscall");
    }

    public SymTable analyze(SymTable symT) {
        symT = myExp.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        if(myExp.getType().equals(new ErrorType())) {
            return;
        }
        if(myExp instanceof IdNode) {
            Sym s = ((IdNode)myExp).sym();
            if(s instanceof FnSym) {
                ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Attempt to write a function");
            } else if(s instanceof StructDefSym) {
                ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Attempt to write a struct name");
            } else if(s instanceof StructSym) {
                ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Attempt to write a struct variable");
            }
        } else if(myExp instanceof CallExpNode) {
            IdNode myId = ((CallExpNode)myExp).getId();
            Sym s = myId.sym();
            if(((FnSym)s).getReturnType().equals(new VoidType())) {
                ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Attempt to write void");
            }                
        }
        return;
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("cout << ");
        myExp.unparse(p, 0);
        p.println(";");
    }

    // 1 kid
    private ExpNode myExp;
}

class IfStmtNode extends StmtNode {
    public IfStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myDeclList = dlist;
        myExp = exp;
        myStmtList = slist;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox nVlist = new TextInBox("vDecList",60,20);
        TextInBox nSlist = new TextInBox("stmtList",60,20);

        tree.addChild(parent,new TextInBox("IF",30,20));
        tree.addChild(parent,new TextInBox("(",30,20));
        tree.addChild(parent,n1);

        tree.addChild(parent,new TextInBox(")",20,20));
        tree.addChild(parent,new TextInBox("{",30,20));
        tree.addChild(parent,nVlist);
        tree.addChild(parent,nSlist);

        tree.addChild(parent,new TextInBox("}",30,20));

        if(myExp instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp.buildTree(tree,nID);
        } else if(myExp instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp.buildTree(tree,nC);
        } else if(myExp instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp.buildTree(tree,nL);
        } else if (myExp instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp.buildTree(tree,nA);
        } else {
            myExp.buildTree(tree,n1);
        }

        myDeclList.buildTree(tree,nVlist,2);
        myStmtList.buildTree(tree,nSlist);
        return;
    }

    public SymTable analyze(SymTable symT) {
        try{
          symT = myExp.analyze(symT);
          symT.addScope();
          symT = myDeclList.analyze(symT);
          symT = myStmtList.analyze(symT);
          symT.addDecl("$scope",new Sym("if loop-"+symT.ifcount));
          symT.ifcount++;
          symT.removeScope();
        } catch (EmptySymTableException ee) {
          System.err.println("Unexpected EmptySymTableException in IfStmtNode.analyze");
          System.exit(-1);
        } catch (DuplicateSymException de) {
            System.err.println("Unexpected exception in IfStmtNode.analyze");
        }
    
        return symT;
    }

    public int computeOffsets(SymTable symTab, int offset) {
        offset = myDeclList.computeOffsets(symTab,offset);
        offset = myStmtList.computeOffsets(symTab,offset);
        return offset;
    }
    
    public void codeGen(PrintWriter p, String name) {
        p.println("\t\t#IF STMT");
        String falselabel = Codegen.nextLabel();

        myExp.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.generateWithComment("beq","check condition","$t0","$0",falselabel);

        myStmtList.codeGen(p,name);

        Codegen.genLabel(falselabel);

    
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * - process the condition
     * - enter a new scope
     * - process the decls and stmts
     * - exit the scope
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
        symTab.addScope();
        myDeclList.nameAnalysis(symTab);
        myStmtList.nameAnalysis(symTab);
        try {
            symTab.removeScope();
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException " +
                               " in IfStmtNode.nameAnalysis");
            System.exit(-1);
        }
    }

     /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        Type t2 = myExp.getType();
        if((!t2.equals(new ErrorType()))&&(!t2.equals(new BoolType()))) {
            ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Non-bool expression used as an if condition");
        }
        myDeclList.typeCheck();
        myStmtList.typeCheck(t);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("}");
    }

    // e kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class IfElseStmtNode extends StmtNode {
    public IfElseStmtNode(ExpNode exp, DeclListNode dlist1,
                          StmtListNode slist1, DeclListNode dlist2,
                          StmtListNode slist2) {
        myExp = exp;
        myThenDeclList = dlist1;
        myThenStmtList = slist1;
        myElseDeclList = dlist2;
        myElseStmtList = slist2;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox nVlist = new TextInBox("vDecList",60,20);
        TextInBox nSlist = new TextInBox("stmtList",60,20);
        TextInBox nVlist2 = new TextInBox("vDecList",60,20);
        TextInBox nSlist2 = new TextInBox("stmtList",60,20);

        tree.addChild(parent,new TextInBox("IF",30,20));
        tree.addChild(parent,new TextInBox("(",30,20));
        tree.addChild(parent,n1);

        tree.addChild(parent,new TextInBox(")",20,20));
        tree.addChild(parent,new TextInBox("{",30,20));
        tree.addChild(parent,nVlist);
        tree.addChild(parent,nSlist);

        tree.addChild(parent,new TextInBox("}",30,20));
        tree.addChild(parent,new TextInBox("ELSE",30,20));
        tree.addChild(parent,new TextInBox("{",30,20));
        tree.addChild(parent,nVlist2);
        tree.addChild(parent,nSlist2);

        tree.addChild(parent,new TextInBox("}",30,20));


        if(myExp instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp.buildTree(tree,nID);
        } else if(myExp instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp.buildTree(tree,nC);
        } else if(myExp instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp.buildTree(tree,nL);
        } else if (myExp instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp.buildTree(tree,nA);
        } else {
            myExp.buildTree(tree,n1);
        }

        myThenDeclList.buildTree(tree,nVlist,2);
        myThenStmtList.buildTree(tree,nSlist);
        myElseDeclList.buildTree(tree,nVlist2,2);
        myElseStmtList.buildTree(tree,nSlist2);
        return;
    }

    public SymTable analyze(SymTable symT) {
        try{
          symT = myExp.analyze(symT);
          symT.addScope();
          symT = myThenDeclList.analyze(symT);
          symT = myThenStmtList.analyze(symT);
          symT.addDecl("$scope",new Sym("if loop-"+symT.ifcount));
          symT.ifcount++;
          symT.removeScope();
          symT.addScope();
          symT = myElseDeclList.analyze(symT);
          symT = myElseStmtList.analyze(symT);
          symT.addDecl("$scope",new Sym("else loop-"+symT.elsecount));
          symT.elsecount++;
          symT.removeScope();
        } catch (EmptySymTableException ee) {
          System.err.println("Unexpected EmptySymTableException in IfStmtNode.analyze");
          System.exit(-1);
        } catch (DuplicateSymException de) {
            System.err.println("Unexpected exception in IfElseStmtNode.analyze");
        }
    
        return symT;
    }

    
    public int computeOffsets(SymTable symTab, int offset) {
        offset = myThenDeclList.computeOffsets(symTab,offset);
        offset = myThenStmtList.computeOffsets(symTab,offset);
        offset = myElseDeclList.computeOffsets(symTab,offset);
        offset = myElseStmtList.computeOffsets(symTab,offset);
        return offset;
    }

    public void codeGen(PrintWriter p, String name) {
        p.println("\t\t#IF ELSE STMT");
        String falselabel = Codegen.nextLabel();
        String endlabel = Codegen.nextLabel();

        myExp.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.generateWithComment("beq","check condition","$t0","$0",falselabel);

        myThenStmtList.codeGen(p,name);
        Codegen.generate("b",endlabel);

        Codegen.genLabel(falselabel);
        myElseStmtList.codeGen(p,name);

        Codegen.genLabel(endlabel);
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * - process the condition
     * - enter a new scope
     * - process the decls and stmts of then
     * - exit the scope
     * - enter a new scope
     * - process the decls and stmts of else
     * - exit the scope
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
        symTab.addScope();
        myThenDeclList.nameAnalysis(symTab);
        myThenStmtList.nameAnalysis(symTab);
        try {
            symTab.removeScope();
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException " +
                               " in IfElseStmtNode.nameAnalysis");
            System.exit(-1);
        }
        symTab.addScope();
        myElseDeclList.nameAnalysis(symTab);
        myElseStmtList.nameAnalysis(symTab);
        try {
            symTab.removeScope();
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException " +
                               " in IfElseStmtNode.nameAnalysis");
            System.exit(-1);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        Type t2 = myExp.getType();
        if((!t2.equals(new ErrorType()))&&(!t2.equals(new BoolType()))) {
            ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Non-bool expression used as an if condition");
        }
        myThenDeclList.typeCheck();
        myThenStmtList.typeCheck(t);
        myElseDeclList.typeCheck();
        myElseStmtList.typeCheck(t);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("if (");
        myExp.unparse(p, 0);
        p.println(") {");
        myThenDeclList.unparse(p, indent+4);
        myThenStmtList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("}");
        addIndentation(p, indent);
        p.println("else {");
        myElseDeclList.unparse(p, indent+4);
        myElseStmtList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("}");
    }

    // 5 kids
    private ExpNode myExp;
    private DeclListNode myThenDeclList;
    private StmtListNode myThenStmtList;
    private StmtListNode myElseStmtList;
    private DeclListNode myElseDeclList;
}

class WhileStmtNode extends StmtNode {
    public WhileStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox nVlist = new TextInBox("vDecList",60,20);
        TextInBox nSlist = new TextInBox("stmtList",60,20);

        tree.addChild(parent,new TextInBox("WHILE",30,20));
        tree.addChild(parent,new TextInBox("(",30,20));
        tree.addChild(parent,n1);

        tree.addChild(parent,new TextInBox(")",20,20));
        tree.addChild(parent,new TextInBox("{",30,20));
        tree.addChild(parent,nVlist);
        tree.addChild(parent,nSlist);

        tree.addChild(parent,new TextInBox("}",30,20));

        if(myExp instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp.buildTree(tree,nID);
        } else if(myExp instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp.buildTree(tree,nC);
        } else if(myExp instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp.buildTree(tree,nL);
        } else if (myExp instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp.buildTree(tree,nA);
        } else {
            myExp.buildTree(tree,n1);
        }

        myDeclList.buildTree(tree,nVlist,2);
        myStmtList.buildTree(tree,nSlist);
        return;
   
    }

    public int computeOffsets(SymTable symTab, int offset) {
        offset = myDeclList.computeOffsets(symTab,offset);
        offset = myStmtList.computeOffsets(symTab,offset);
        return offset;
    }
    
    public void codeGen(PrintWriter p, String name) {
        p.println("\t\t#WHILE STMT");
        String looplabel = Codegen.nextLabel();
        String falselabel = Codegen.nextLabel();

        Codegen.genLabel(looplabel);
        myExp.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.generateWithComment("beq","evaluate condition","$t0","$0",falselabel);
        myStmtList.codeGen(p,name);
        Codegen.generate("b",looplabel);
        Codegen.genLabel(falselabel);
    
    }

    public SymTable analyze(SymTable symT) {
        try{
          symT = myExp.analyze(symT);
          symT.addScope();
          symT = myDeclList.analyze(symT);
          symT = myStmtList.analyze(symT);
          symT.addDecl("$scope",new Sym("while loop-"+symT.whilecount));
          symT.whilecount++;
          symT.removeScope();
        } catch (EmptySymTableException ee) {
          System.err.println("Unexpected EmptySymTableException in WhileStmtNode.analyze");
          System.exit(-1);
        } catch (DuplicateSymException de) {
            System.err.println("Unexpected exception in IfStmtNode.analyze");
        }
    
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * - process the condition
     * - enter a new scope
     * - process the decls and stmts
     * - exit the scope
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
        symTab.addScope();
        myDeclList.nameAnalysis(symTab);
        myStmtList.nameAnalysis(symTab);
        try {
            symTab.removeScope();
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException " +
                               " in WhileStmtNode.nameAnalysis");
            System.exit(-1);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        Type t2 = myExp.getType();
        if((!t2.equals(new ErrorType()))&&(!t2.equals(new BoolType()))) {
            ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Non-bool expression used as a while condition");
        }
        myDeclList.typeCheck();
        myStmtList.typeCheck(t);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("while (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("}");
    }

    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}

class RepeatStmtNode extends StmtNode {
    public RepeatStmtNode(ExpNode exp, DeclListNode dlist, StmtListNode slist) {
        myExp = exp;
        myDeclList = dlist;
        myStmtList = slist;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox nVlist = new TextInBox("vDecList",60,20);
        TextInBox nSlist = new TextInBox("stmtList",60,20);

        tree.addChild(parent,new TextInBox("REPEAT",30,20));
        tree.addChild(parent,new TextInBox("(",30,20));
        tree.addChild(parent,n1);

        tree.addChild(parent,new TextInBox(")",20,20));
        tree.addChild(parent,new TextInBox("{",30,20));
        tree.addChild(parent,nVlist);
        tree.addChild(parent,nSlist);

        tree.addChild(parent,new TextInBox("}",30,20));

        if(myExp instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp.buildTree(tree,nID);
        } else if(myExp instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp.buildTree(tree,nC);
        } else if(myExp instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp.buildTree(tree,nL);
        } else if (myExp instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp.buildTree(tree,nA);
        } else {
            myExp.buildTree(tree,n1);
        }

        myDeclList.buildTree(tree,nVlist,2);
        myStmtList.buildTree(tree,nSlist);
        return;
    }
    
    public void codeGen(PrintWriter p, String name) {
    
    }

    public SymTable analyze(SymTable symT) {
        try{
          symT = myExp.analyze(symT);
          symT.addScope();
          symT = myDeclList.analyze(symT);
          symT = myStmtList.analyze(symT);
          symT.addDecl("$scope",new Sym("repeat loop-"+symT.repeatcount));
          symT.repeatcount++;
          symT.removeScope();
        } catch (EmptySymTableException ee) {
          System.err.println("Unexpected EmptySymTableException in RepeatStmtNode.analyze");
          System.exit(-1);
        } catch (DuplicateSymException de) {
            System.err.println("Unexpected exception in IfStmtNode.analyze");
        }
    
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * - process the condition
     * - enter a new scope
     * - process the decls and stmts
     * - exit the scope
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
        symTab.addScope();
        myDeclList.nameAnalysis(symTab);
        myStmtList.nameAnalysis(symTab);
        try {
            symTab.removeScope();
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException " +
                               " in RepeatStmtNode.nameAnalysis");
            System.exit(-1);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        Type t2 = myExp.getType();
        if((!t2.equals(new ErrorType()))&&(!t2.equals(new IntType()))) {
            ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Non-integer expression used as a repeat clause");
        }
        myDeclList.typeCheck();
        myStmtList.typeCheck(t);
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("repeat (");
        myExp.unparse(p, 0);
        p.println(") {");
        myDeclList.unparse(p, indent+4);
        myStmtList.unparse(p, indent+4);
        addIndentation(p, indent);
        p.println("}");
    }

    // 3 kids
    private ExpNode myExp;
    private DeclListNode myDeclList;
    private StmtListNode myStmtList;
}


class CallStmtNode extends StmtNode {
    public CallStmtNode(CallExpNode call) {
        myCall = call;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox nCall = new TextInBox("fncall",50,20);

        tree.addChild(parent,nCall);
        tree.addChild(parent,new TextInBox(";",10,20));

        myCall.buildTree(tree,nCall);

        return;
    }
    
    public void codeGen(PrintWriter p, String name) {
        p.println("\t\t#CALL STMT");
        myCall.codeGen(p);
        Codegen.genPop("$v0");
    
    }

    public SymTable analyze(SymTable symT) {
        symT = myCall.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myCall.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        myCall.getType();
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        myCall.unparse(p, indent);
        p.println(";");
    }

    // 1 kid
    private CallExpNode myCall;
}

class ReturnStmtNode extends StmtNode {
    public ReturnStmtNode(ExpNode exp) {
        myExp = exp;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        

        tree.addChild(parent,new TextInBox("RETURN",60,20));
        
        if(myExp != null) {
            tree.addChild(parent,n1);
            if(myExp instanceof IdNode) {
                TextInBox nT = new TextInBox("term",35,20);
                TextInBox nL = new TextInBox("loc",30,20);
                TextInBox nID = new TextInBox("id",20,20);
                tree.addChild(n1,nT);
                tree.addChild(nT,nL);
                tree.addChild(nL,nID);
                myExp.buildTree(tree,nID);
            } else if(myExp instanceof CallExpNode) {
                TextInBox nT = new TextInBox("term",35,20);
                TextInBox nC = new TextInBox("fncall",50,20);
                tree.addChild(n1,nT);
                tree.addChild(nT,nC);
                myExp.buildTree(tree,nC);
            } else if(myExp instanceof DotAccessExpNode) {
                TextInBox nT = new TextInBox("term",35,20);
                TextInBox nL = new TextInBox("loc",30,20);
                tree.addChild(n1,nT);
                tree.addChild(nT,nL);
                myExp.buildTree(tree,nL);
            } else if (myExp instanceof AssignNode) {
                TextInBox nA = new TextInBox("aExp",30,20);
                tree.addChild(n1,nA);
                myExp.buildTree(tree,nA);
            } else {
                myExp.buildTree(tree,n1);
            }
        }

        

        tree.addChild(parent,new TextInBox(";",10,20));

        
        
        
        return;
    }
    
    public void codeGen(PrintWriter p, String name) {
        p.println("\t\t#RETURN STMT");
        myExp.codeGen(p);
        Codegen.genPop("$v0");
        Codegen.generate("b","_"+name+"_Exit");
    
    }

    public SymTable analyze(SymTable symT) {
        symT = myExp.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's child,
     * if it has one
     */
    public void nameAnalysis(SymTable symTab) {
        if (myExp != null) {
            myExp.nameAnalysis(symTab);
        }
    }

    /**
     * typeCheck
     */
    public void typeCheck(Type t) {
        if(myExp == null && (!t.equals(new VoidType()))) {
            ErrMsg.fatal(0,0,"Missing return value");
            return;
        }
        if(t.equals(new VoidType()) && myExp != null) {
            myExp.getType();
            ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Return with a value in a void function");
            return;
        }
        if(myExp != null) {
            Type t2 = myExp.getType();
            if(!t2.equals(new ErrorType())) {
                if(!t2.equals(t)) {
                    ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Bad return value");
                }
            }
        }
    }

    public void unparse(PrintWriter p, int indent) {
        addIndentation(p, indent);
        p.print("return");
        if (myExp != null) {
            p.print(" ");
            myExp.unparse(p, 0);
        }
        p.println(";");
    }

    // 1 kid
    private ExpNode myExp; // possibly null
}

// **********************************************************************
// ExpNode and its subclasses
// **********************************************************************

abstract class ExpNode extends ASTnode {
    /**
     * Default version for nodes with no names
     */
    public void nameAnalysis(SymTable symTab) { }
    abstract public SymTable analyze(SymTable symT);
    abstract public Type typeCheck();
    abstract public Type getType();
    abstract public int lineNum();
    abstract public int charNum();
    abstract public void codeGen(PrintWriter p);
    abstract public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent);
}

class IntLitNode extends ExpNode {
    public IntLitNode(int lineNum, int charNum, int intVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myIntVal = intVal;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        String val = myIntVal+"";
        TextInBox n = new TextInBox("term",35,20);
        tree.addChild(parent,n);
        tree.addChild(n,new TextInBox(val,20,20));
        return;
    }
    
    public void codeGen(PrintWriter p) {
        Codegen.generate("li","$t0",myIntVal);
        Codegen.genPush("$t0");
    }

    public SymTable analyze(SymTable symT) {
        return symT;
    }

    /**
     * Return the line number for this literal.
     */
    public int lineNum() {
        return myLineNum;
    }

    /**
     * Return the char number for this literal.
     */
    public int charNum() {
        return myCharNum;
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        return new IntType();
    }

    public Type getType() {
        return new IntType();
    
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myIntVal);
    }

    private int myLineNum;
    private int myCharNum;
    private int myIntVal;
}

class StringLitNode extends ExpNode {
    public StringLitNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n = new TextInBox("term",35,20);
        tree.addChild(parent,n);
        String val = "\""+myStrVal+"\"";
        tree.addChild(n,new TextInBox(val,60,20));
        return;
    }

    public SymTable analyze(SymTable symT) {
        return symT;
    }
    
    public void codeGen(PrintWriter p) {
        String s = Codegen.nextLabel();
        p.println("\t.data");
        p.println(s+":\t.asciiz "+myStrVal);
        p.println("\t.text");
        Codegen.generate("la","$t0",s);
        Codegen.genPush("$t0");
        
        
        
    }

    /**
     * Return the line number for this literal.
     */
    public int lineNum() {
        return myLineNum;
    }

    /**
     * Return the char number for this literal.
     */
    public int charNum() {
        return myCharNum;
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        return new StringType();
    }

    public Type getType() {
        return new StringType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
    }

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
}

class TrueNode extends ExpNode {
    public TrueNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }
    
    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n = new TextInBox("term",35,20);
        tree.addChild(parent,n);
        tree.addChild(n,new TextInBox("TRUE",20,20));
        return;
    }

    public SymTable analyze(SymTable symT) {
        return symT;
    }

    public void codeGen(PrintWriter p) {
        Codegen.generate("li","$t0",1);
        Codegen.genPush("$t0");
    }

    /**
     * Return the line number for this literal.
     */
    public int lineNum() {
        return myLineNum;
    }

    /**
     * Return the char number for this literal.
     */
    public int charNum() {
        return myCharNum;
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        return new BoolType();
    }

    public Type getType() {
        return new BoolType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("true");
    }

    private int myLineNum;
    private int myCharNum;
}

class FalseNode extends ExpNode {
    public FalseNode(int lineNum, int charNum) {
        myLineNum = lineNum;
        myCharNum = charNum;
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n = new TextInBox("term",35,20);
        tree.addChild(parent,n);
        tree.addChild(n,new TextInBox("FALSE",20,20));
        return;
    }

    public SymTable analyze(SymTable symT) {
        return symT;
    }

    public void codeGen(PrintWriter p) {
        Codegen.generate("li","$t0",0);
        Codegen.genPush("$t0");
    }
    
    /**
     * Return the line number for this literal.
     */
    public int lineNum() {
        return myLineNum;
    }

    /**
     * Return the char number for this literal.
     */
    public int charNum() {
        return myCharNum;
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        return new BoolType();
    }

    public Type getType() {
        return new BoolType();
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("false");
    }

    private int myLineNum;
    private int myCharNum;
}

class IdNode extends ExpNode {
    public IdNode(int lineNum, int charNum, String strVal) {
        myLineNum = lineNum;
        myCharNum = charNum;
        myStrVal = strVal;
    }
    
    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        tree.addChild(parent,new TextInBox("ID",20,20));
        return;
    }

    public SymTable analyze(SymTable symT) {
        try {
            if(symT.lookupGlobal(myStrVal) == null) {
                ErrMsg.fatal(myLineNum,myCharNum,"Undeclared identifier");
            } else {
                link = symT.lookupGlobal(myStrVal);
            }
        } catch (EmptySymTableException ee) {
            System.err.println("Unexpected EmptySymTableException in IdNode.analyze");
            System.exit(-1);
        }
        return symT;
    }

    public void codeGen(PrintWriter p) {
        if(mySym.getOffset() == 0) {
            Codegen.generate("lw","$t0","_"+myStrVal);
        } else {
            Codegen.generateIndexed("lw","$t0","$fp",mySym.getOffset());
        }
        Codegen.genPush("$t0");
    }


    public void genAddr(PrintWriter p) {
        if(mySym.getOffset() == 0) {
            Codegen.generate("la","$t0","_"+myStrVal);
        } else {
            Codegen.generateIndexed("la","$t0","$fp",mySym.getOffset());
        }
        Codegen.genPush("$t0");  
    }

    public void genJumpAndLink(PrintWriter p) {
        Codegen.generate("jal","_"+myStrVal);
    }

    /**
     * Link the given symbol to this ID.
     */
    public void link(Sym sym) {
        mySym = sym;
    }

    /**
     * Return the name of this ID.
     */
    public String name() {
        return myStrVal;
    }

    /**
     * Return the symbol associated with this ID.
     */
    public Sym sym() {
        return mySym;
    }

    /**
     * Return the line number for this ID.
     */
    public int lineNum() {
        return myLineNum;
    }

    /**
     * Return the char number for this ID.
     */
    public int charNum() {
        return myCharNum;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * - check for use of undeclared name
     * - if ok, link to symbol table entry
     */
    public void nameAnalysis(SymTable symTab) {
        Sym sym = null;

        try {
          sym = symTab.lookupGlobal(myStrVal);
        } catch (EmptySymTableException ex) {
            System.err.println("Unexpected EmptySymTableException " +
                               " in IdNode.nameAnalysis");
            System.exit(-1);
        }

        if (sym == null) {
            ErrMsg.fatal(myLineNum, myCharNum, "Undeclared identifier");
        } else {
            link(sym);
        }
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        if (mySym != null) {
            return mySym.getType();
        }
        else {
            System.err.println("ID with null sym field in IdNode.typeCheck");
            System.exit(-1);
        }
        return null;
    }

    public Type getType() {
        return mySym.getType();
    
    }

    public void unparse(PrintWriter p, int indent) {
        p.print(myStrVal);
        if (mySym != null) {
            //p.print("(" + mySym + "("+mySym.getOffset()+"))");
        }
    }

    public String toString() {
	    return myStrVal;
    }
    
    public int getCharNum() {
        return myCharNum;
    }
    
    public int getLineNum() {
        return myLineNum;
    }
    
    public String getOffset() {
        return "("+mySym.getOffset()+")";
    }    

    private int myLineNum;
    private int myCharNum;
    private String myStrVal;
    private Sym mySym;
    private Sym link;
}

class DotAccessExpNode extends ExpNode {
    public DotAccessExpNode(ExpNode loc, IdNode id) {
        myLoc = loc;
        myId = id;
        mySym = null;
    }
    
    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox nLoc = new TextInBox("loc",30,20);
        TextInBox nID = new TextInBox("id",20,20);
        tree.addChild(parent,nLoc);
        tree.addChild(parent,new TextInBox(".",10,20));
        tree.addChild(parent,nID);
        if(myLoc instanceof IdNode) {
            TextInBox nID2 = new TextInBox("id",20,20);
            tree.addChild(nLoc,nID2);
            myLoc.buildTree(tree,nID2);
        } else {
            myLoc.buildTree(tree,nLoc);
        }
        myId.buildTree(tree,nID);
        return;
    }

    public void codeGen(PrintWriter p) {
    
    }

    /**
     * Return the symbol associated with this dot-access node.
     */
    public Sym sym() {
        return mySym;
    }

    /**
     * Return the line number for this dot-access node.
     * The line number is the one corresponding to the RHS of the dot-access.
     */
    public int lineNum() {
        return myId.lineNum();
    }

    /**
     * Return the char number for this dot-access node.
     * The char number is the one corresponding to the RHS of the dot-access.
     */
    public int charNum() {
        return myId.charNum();
    }

    public SymTable analyze(SymTable symT) {
      
        try {
            if(myLoc instanceof DotAccessExpNode) {
               
                symT = myLoc.analyze(symT);
                if(((DotAccessExpNode)myLoc).hasError()) {
                    hasError = true;
                   
                } else {
                  
                  Sym s = this.getTable(symT).lookupGlobal(myId.toString());
                  if(s == null) {
                    
                    SymTable previous = ((DotAccessExpNode)myLoc).getTable(symT);
                    Sym check = previous.lookupGlobal(((DotAccessExpNode)myLoc).getId().toString());
                    if(!check.getKind().equals("struct variable")) {
                        ErrMsg.fatal(((DotAccessExpNode)myLoc).getId().getLineNum(),((DotAccessExpNode)myLoc).getId().getCharNum(),"Dot-access of non-struct type");
                    } else {
                        ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Invalid struct field name");
                    }
                    hasError = true;
                  } 
                    
                    
                    else {
            
                      SymTable analyzeT = this.getTable(symT);
                      analyzeT = myId.analyze(analyzeT);
                    }
                }
            }
            if(myLoc instanceof IdNode) {
                
                Sym s = symT.lookupGlobal(((IdNode)myLoc).toString());
                if(s == null) {
                    
                    symT = myLoc.analyze(symT);
                    ErrMsg.fatal(((IdNode)myLoc).getLineNum(),((IdNode)myLoc).getCharNum(),"Dot-access of non-struct type");
                    hasError = true;
                    
                } else if(!s.getKind().equals("struct variable")) {
                    
                    ErrMsg.fatal(((IdNode)myLoc).getLineNum(),((IdNode)myLoc).getCharNum(),"Dot-access of non-struct type");
                    hasError = true;
                } else {
                    SymTable structT = s.getTable();
                    if(structT.lookupGlobal(myId.toString()) == null) {
                        ErrMsg.fatal(myId.getLineNum(),myId.getCharNum(),"Invalid struct field name");
                        hasError = true;
                    } else {
                        
                        symT = myLoc.analyze(symT);
                        SymTable analyzeT = this.getTable(symT); //symT = myId.analyze(symT);
                        analyzeT = myId.analyze(analyzeT);
                    }
                }
            }
        } catch (EmptySymTableException ee) {
            System.err.println("Unexpected EmptySymTableException in DotAccessExpNode.analyze");
            System.exit(-1);
        }
        return symT;
    }

    public SymTable getTable(SymTable symT) {
        SymTable sol = new SymTable();
        try {
          if(myLoc instanceof DotAccessExpNode) {
            SymTable structT = ((DotAccessExpNode)myLoc).getTable(symT);
            Sym s = structT.lookupGlobal(((DotAccessExpNode)myLoc).getId().toString());
          
            sol = s.getTable();
            
          } else {
            Sym s = symT.lookupGlobal(((IdNode)myLoc).toString());
         
            sol =  s.getTable();
          }
        } catch(EmptySymTableException ee) {
            System.err.println("Unexpected EmptySymTableException in DotAccessExpNode.getTable");
        }
        return sol;
      
    }



    /**
     * nameAnalysis
     * Given a symbol table symTab, do:
     * - process the LHS of the dot-access
     * - process the RHS of the dot-access
     * - if the RHS is of a struct type, set the sym for this node so that
     *   a dot-access "higher up" in the AST can get access to the symbol
     *   table for the appropriate struct definition
     */
    public void nameAnalysis(SymTable symTab) {
        badAccess = false;
        SymTable structSymTab = null; // to lookup RHS of dot-access
        Sym sym = null;

        myLoc.nameAnalysis(symTab);  // do name analysis on LHS

        // if myLoc is really an ID, then sym will be a link to the ID's symbol
        if (myLoc instanceof IdNode) {
            IdNode id = (IdNode)myLoc;
            sym = id.sym();

            // check ID has been declared to be of a struct type

            if (sym == null) { // ID was undeclared
                badAccess = true;
            }
            else if (sym instanceof StructSym) {
                // get symbol table for struct type
                Sym tempSym = ((StructSym)sym).getStructType().sym();
                structSymTab = ((StructDefSym)tempSym).getSymTable();
            }
            else {  // LHS is not a struct type
                ErrMsg.fatal(id.lineNum(), id.charNum(),
                             "Dot-access of non-struct type");
                badAccess = true;
            }
        }

        // if myLoc is really a dot-access (i.e., myLoc was of the form
        // LHSloc.RHSid), then sym will either be
        // null - indicating RHSid is not of a struct type, or
        // a link to the Sym for the struct type RHSid was declared to be
        else if (myLoc instanceof DotAccessExpNode) {
            DotAccessExpNode loc = (DotAccessExpNode)myLoc;

            if (loc.badAccess) {  // if errors in processing myLoc
                badAccess = true; // don't continue proccessing this dot-access
            }
            else { //  no errors in processing myLoc
                sym = loc.sym();

                if (sym == null) {  // no struct in which to look up RHS
                    ErrMsg.fatal(loc.lineNum(), loc.charNum(),
                                 "Dot-access of non-struct type");
                    badAccess = true;
                }
                else {  // get the struct's symbol table in which to lookup RHS
                    if (sym instanceof StructDefSym) {
                        structSymTab = ((StructDefSym)sym).getSymTable();
                    }
                    else {
                        System.err.println("Unexpected Sym type in DotAccessExpNode");
                        System.exit(-1);
                    }
                }
            }

        }

        else { // don't know what kind of thing myLoc is
            System.err.println("Unexpected node type in LHS of dot-access");
            System.exit(-1);
        }

        // do name analysis on RHS of dot-access in the struct's symbol table
        if (!badAccess) {

            try {
                sym = structSymTab.lookupGlobal(myId.name()); // lookup
            } catch (EmptySymTableException ex) {
                System.err.println("Unexpected EmptySymTableException " +
                                 " in DotAccessExpNode.nameAnalysis");
            }

            if (sym == null) { // not found - RHS is not a valid field name
                ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                             "Invalid struct field name");
                badAccess = true;
            }

            else {
                myId.link(sym);  // link the symbol
                // if RHS is itself as struct type, link the symbol for its struct
                // type to this dot-access node (to allow chained dot-access)
                if (sym instanceof StructSym) {
                    mySym = ((StructSym)sym).getStructType().sym();
                }
            }
        }
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        return myId.typeCheck();
    }

    public Type getType() {
        return myId.getType();
    }

    public void unparse(PrintWriter p, int indent) {
        myLoc.unparse(p, 0);
        p.print(".");
        myId.unparse(p, 0);
    }

    public IdNode getId() {
        return myId;
        
    }
    
    public boolean hasError() {
        return hasError;
    }

    // 2 kids
    private ExpNode myLoc;
    private IdNode myId;
    private Sym mySym;          // link to Sym for struct type
    private boolean badAccess;  // to prevent multiple, cascading errors
    private boolean hasError;
}

class AssignNode extends ExpNode {
    public AssignNode(ExpNode lhs, ExpNode exp) {
        myLhs = lhs;
        myExp = exp;
    }
    
    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox nLoc= new TextInBox("loc",30,20);
        TextInBox n1 = new TextInBox("exp",30,20);

        tree.addChild(parent,nLoc);
        tree.addChild(parent,new TextInBox("=",20,20));
        tree.addChild(parent,n1);

        if(myLhs instanceof IdNode) {
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(nLoc,nID);
            myLhs.buildTree(tree,nID);
        } else {
            myLhs.buildTree(tree,nLoc);
        }
        
        if(myExp instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp.buildTree(tree,nID);
        } else if(myExp instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp.buildTree(tree,nC);
        } else if(myExp instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp.buildTree(tree,nL);
        } else if (myExp instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp.buildTree(tree,nA);
        } else {
            myExp.buildTree(tree,n1);
        }


        return;
    }

    public void codeGen(PrintWriter p) {
        myExp.codeGen(p);
        ((IdNode)myLhs).genAddr(p);
        Codegen.genPop("$t1");
        Codegen.genPop("$t0");
        Codegen.generateIndexed("sw","$t0","$t1",0);
        Codegen.genPush("$t0");
    }

    /**
     * Return the line number for this assignment node.
     * The line number is the one corresponding to the left operand.
     */
    public int lineNum() {
        return myLhs.lineNum();
    }

    /**
     * Return the char number for this assignment node.
     * The char number is the one corresponding to the left operand.
     */
    public int charNum() {
        return myLhs.charNum();
    }

    public SymTable analyze(SymTable symT) {
        symT = myLhs.analyze(symT);
        symT = myExp.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's
     * two children
     */
    public void nameAnalysis(SymTable symTab) {
        myLhs.nameAnalysis(symTab);
        myExp.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type typeLhs = myLhs.typeCheck();
        Type typeExp = myExp.typeCheck();
        Type retType = typeLhs;

        if (typeLhs.isFnType() && typeExp.isFnType()) {
            ErrMsg.fatal(lineNum(), charNum(), "Function assignment");
            retType = new ErrorType();
        }

        if (typeLhs.isStructDefType() && typeExp.isStructDefType()) {
            ErrMsg.fatal(lineNum(), charNum(), "Struct name assignment");
            retType = new ErrorType();
        }

        if (typeLhs.isStructType() && typeExp.isStructType()) {
            ErrMsg.fatal(lineNum(), charNum(), "Struct variable assignment");
            retType = new ErrorType();
        }

        if (!typeLhs.equals(typeExp) && !typeLhs.isErrorType() && !typeExp.isErrorType()) {
            ErrMsg.fatal(lineNum(), charNum(), "Type mismatch");
            retType = new ErrorType();
        }

        if (typeLhs.isErrorType() || typeExp.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public Type getType() {
        Type t1 = myLhs.getType();
        Type t2 = myExp.getType();
        if(t1.equals(new ErrorType()) || t2.equals(new ErrorType())) {
            return new ErrorType();
        }
        else if(t1.equals(t2)) {
            if(t1.equals(new FnType())) {
                ErrMsg.fatal(myLhs.lineNum(),myLhs.charNum(),"Function assignment");
                return new ErrorType();
            } else if(t1.equals(new StructType(null))) {
                ErrMsg.fatal(myLhs.lineNum(),myLhs.charNum(),"Struct variable assignment");
                return new ErrorType();
            } else if(t1.equals(new StructDefType())) {
                ErrMsg.fatal(myLhs.lineNum(),myLhs.charNum(),"Struct name assignment");
                return new ErrorType();
            }
            return myExp.getType();
        } else {
            if(myLhs instanceof IdNode) {
                ErrMsg.fatal(((IdNode)myLhs).lineNum(),((IdNode)myLhs).charNum(),"Type mismatch");
            } else if(myLhs instanceof DotAccessExpNode) {
                ErrMsg.fatal(((DotAccessExpNode)myLhs).lineNum(),((DotAccessExpNode)myLhs).charNum(),"Type mismatch");
            }
            return new ErrorType();
        }
    
    }

    public void unparse(PrintWriter p, int indent) {
        if (indent != -1)  p.print("(");
        myLhs.unparse(p, 0);
        p.print(" = ");
        myExp.unparse(p, 0);
        if (indent != -1)  p.print(")");
    }

    // 2 kids
    private ExpNode myLhs;
    private ExpNode myExp;
}

class CallExpNode extends ExpNode {
    public CallExpNode(IdNode name, ExpListNode elist) {
        myId = name;
        myExpList = elist;
    }

    public CallExpNode(IdNode name) {
        myId = name;
        myExpList = new ExpListNode(new LinkedList<ExpNode>());
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox nID = new TextInBox("id",20,20);
        tree.addChild(parent,nID);
        tree.addChild(parent,new TextInBox("(",15,20));
        if(myExpList != null) {
            myExpList.buildTree(tree,parent);
        }
        tree.addChild(parent,new TextInBox(")",15,20));
        return;
    }

    public void codeGen(PrintWriter p) {
        myExpList.codeGen(p);
        myId.genJumpAndLink(p);
        Codegen.genPush("$v0");


    
    }
    
    /**
     * Return the line number for this call node.
     * The line number is the one corresponding to the function name.
     */
    public int lineNum() {
        return myId.lineNum();
    }

    /**
     * Return the char number for this call node.
     * The char number is the one corresponding to the function name.
     */
    public int charNum() {
        return myId.charNum();
    }

    public SymTable analyze(SymTable symT) {
        symT = myId.analyze(symT);
        symT = myExpList.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's
     * two children
     */
    public void nameAnalysis(SymTable symTab) {
        myId.nameAnalysis(symTab);
        myExpList.nameAnalysis(symTab);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        if (!myId.typeCheck().isFnType()) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                         "Attempt to call a non-function");
            return new ErrorType();
        }

        FnSym fnSym = (FnSym)(myId.sym());

        if (fnSym == null) {
            System.err.println("null sym for Id in CallExpNode.typeCheck");
            System.exit(-1);
        }

        if (myExpList.size() != fnSym.getNumParams()) {
            ErrMsg.fatal(myId.lineNum(), myId.charNum(),
                         "Function call with wrong number of args");
            return fnSym.getReturnType();
        }

        myExpList.typeCheck(fnSym.getParamTypes());
        return fnSym.getReturnType();
    }

    public Type getType() {
        Sym s = myId.sym();
        if(!(s instanceof FnSym)) {
            ErrMsg.fatal(myId.lineNum(),myId.charNum(),"Attempt to call a non-function");
            return new ErrorType();
        }
        if(myExpList == null) {
            if(((FnSym)s).getNumParams() != 0) {
                ErrMsg.fatal(myId.lineNum(),myId.charNum(),"Function call with wrong number of args");
                return ((FnSym)s).getReturnType();   
            }
        } else {
            if(((FnSym)s).getNumParams() != myExpList.size()) {
                ErrMsg.fatal(myId.lineNum(),myId.charNum(),"Function call with wrong number of args");
                return ((FnSym)s).getReturnType();
            }  
        }
        List<Type> params = ((FnSym)s).getParamTypes();
        List<ExpNode> actuals = myExpList.getList();
        List<Type> actualTypes = new LinkedList<Type>();
        for(int i = 0;i<actuals.size();i++) {
            actualTypes.add(actuals.get(i).getType());
        }
        for(int i = 0;i<actualTypes.size();i++) {
            if(!actualTypes.get(i).equals(params.get(i))) {
                if(!actualTypes.get(i).equals(new ErrorType())) {
                    ErrMsg.fatal(actuals.get(i).lineNum(),actuals.get(i).charNum(),"Type of actual does not match type of formal");
                }
            }
        }
        return ((FnSym)s).getReturnType();
    
    }

    public IdNode getId() {
        return myId;
    }

    // ** unparse **
    public void unparse(PrintWriter p, int indent) {
        myId.unparse(p, 0);
        p.print("(");
        if (myExpList != null) {
            myExpList.unparse(p, 0);
        }
        p.print(")");
    }

    // 2 kids
    private IdNode myId;
    private ExpListNode myExpList;  // possibly null
}

abstract class UnaryExpNode extends ExpNode {
    public UnaryExpNode(ExpNode exp) {
        myExp = exp;
    }

    


    /**
     * Return the line number for this unary expression node.
     * The line number is the one corresponding to the  operand.
     */
    public int lineNum() {
        return myExp.lineNum();
    }

    /**
     * Return the char number for this unary expression node.
     * The char number is the one corresponding to the  operand.
     */
    public int charNum() {
        return myExp.charNum();
    }

    public SymTable analyze(SymTable symT) {
        symT = myExp.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's child
     */
    public void nameAnalysis(SymTable symTab) {
        myExp.nameAnalysis(symTab);
    }

    // one child
    protected ExpNode myExp;
}

abstract class BinaryExpNode extends ExpNode {
    public BinaryExpNode(ExpNode exp1, ExpNode exp2) {
        myExp1 = exp1;
        myExp2 = exp2;
    }


    /**
     * Return the line number for this binary expression node.
     * The line number is the one corresponding to the left operand.
     */
    public int lineNum() {
        return myExp1.lineNum();
    }

    /**
     * Return the char number for this binary expression node.
     * The char number is the one corresponding to the left operand.
     */
    public int charNum() {
        return myExp1.charNum();
    }

    public SymTable analyze(SymTable symT) {
        symT = myExp1.analyze(symT);
        symT = myExp2.analyze(symT);
        return symT;
    }

    /**
     * nameAnalysis
     * Given a symbol table symTab, perform name analysis on this node's
     * two children
     */
    public void nameAnalysis(SymTable symTab) {
        myExp1.nameAnalysis(symTab);
        myExp2.nameAnalysis(symTab);
    }

    // two kids
    protected ExpNode myExp1;
    protected ExpNode myExp2;
}

// **********************************************************************
// Subclasses of UnaryExpNode
// **********************************************************************

class UnaryMinusNode extends UnaryExpNode {
    public UnaryMinusNode(ExpNode exp) {
        super(exp);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        if(!(myExp instanceof IntLitNode || myExp instanceof StringLitNode || myExp instanceof TrueNode || myExp instanceof FalseNode || myExp instanceof DotAccessExpNode || myExp instanceof CallExpNode )) {
            TextInBox n = new TextInBox("term",30,20);
            TextInBox n2 = new TextInBox("exp",30,20);

            tree.addChild(parent,new TextInBox("-",20,20));
            tree.addChild(parent,n);
            tree.addChild(n,new TextInBox("(",15,20));
            tree.addChild(n,n2);
            tree.addChild(n,new TextInBox(")",15,20));

            myExp.buildTree(tree,n2);

        
        }
        else {
        TextInBox n1 = new TextInBox("term",30,20);

        tree.addChild(parent,new TextInBox("-",20,20));
        tree.addChild(parent,n1);

        if(myExp instanceof IdNode) {
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nL);
            tree.addChild(nL,nID);
            myExp.buildTree(tree,nID);
        } else if(myExp instanceof CallExpNode) {
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nC);
            myExp.buildTree(tree,nC);
        } else if(myExp instanceof DotAccessExpNode) {
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nL);
            myExp.buildTree(tree,nL);
        } else {
            myExp.buildTree(tree,n1);
        }

        }
        return;
    }
    
    public void codeGen(PrintWriter p) {
        myExp.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.generate("li","$t1",-1);
        Codegen.generate("multu","$t0","$t1");
        Codegen.generate("mflo","$t0");
        Codegen.genPush("$t0");
    
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type = myExp.typeCheck();
        Type retType = new IntType();

        if (!type.isErrorType() && !type.isIntType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                         "Arithmetic operator applied to non-numeric operand");
            retType = new ErrorType();
        }

        if (type.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public Type getType() {
        if(myExp.getType().equals(new ErrorType())) {
            return new ErrorType();
        } else if(myExp.getType().equals(new IntType())) {
            return new IntType();
        } else {
            ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Arithmetic operator applied to non-numeric operand");
            return new ErrorType();
        }
    
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(-");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

class NotNode extends UnaryExpNode {
    public NotNode(ExpNode exp) {
        super(exp);
    }
    
    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);

        tree.addChild(parent,new TextInBox("!",20,20));
        tree.addChild(parent,n1);

        if(myExp instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp.buildTree(tree,nID);
        } else if(myExp instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp.buildTree(tree,nC);
        } else if(myExp instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp.buildTree(tree,nL);
        } else if (myExp instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp.buildTree(tree,nA);
        } else {
            myExp.buildTree(tree,n1);
        }

        return;
    }

    public void codeGen(PrintWriter p) {
        myExp.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.generate("seq","$t0","$t0","$0");
        Codegen.genPush("$t0");
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type = myExp.typeCheck();
        Type retType = new BoolType();

        if (!type.isErrorType() && !type.isBoolType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                         "Logical operator applied to non-bool operand");
            retType = new ErrorType();
        }

        if (type.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public Type getType() {
        if(myExp.getType().equals(new ErrorType())) {
            return new ErrorType();
        } else if(myExp.getType().equals(new BoolType())) {
            return new BoolType();
        } else {
            ErrMsg.fatal(myExp.lineNum(),myExp.charNum(),"Logical operator applied to non-bool operand");
            return new ErrorType();
        }
    
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(!");
        myExp.unparse(p, 0);
        p.print(")");
    }
}

// **********************************************************************
// Subclasses of BinaryExpNode
// **********************************************************************

abstract class ArithmeticExpNode extends BinaryExpNode {
    public ArithmeticExpNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    
    

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type1 = myExp1.typeCheck();
        Type type2 = myExp2.typeCheck();
        Type retType = new IntType();

        if (!type1.isErrorType() && !type1.isIntType()) {
            ErrMsg.fatal(myExp1.lineNum(), myExp1.charNum(),
                         "Arithmetic operator applied to non-numeric operand");
            retType = new ErrorType();
        }

        if (!type2.isErrorType() && !type2.isIntType()) {
            ErrMsg.fatal(myExp2.lineNum(), myExp2.charNum(),
                         "Arithmetic operator applied to non-numeric operand");
            retType = new ErrorType();
        }

        if (type1.isErrorType() || type2.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public Type getType() {
        Type t1 = myExp1.getType();
        Type t2 = myExp2.getType();
        
        if(t1.equals(new IntType()) && t2.equals(new IntType())) {
            return new IntType();
        }
        
        if(!t1.equals(new ErrorType()) && !t1.equals(new IntType())) {
            ErrMsg.fatal(myExp1.lineNum(),myExp1.charNum(),"Arithmetic operator applied to non-numeric operand");
        }
        
        if(!t2.equals(new ErrorType()) && !t2.equals(new IntType())) {
            ErrMsg.fatal(myExp2.lineNum(),myExp2.charNum(),"Arithmetic operator applied to non-numeric operand");
        }
        
        return new ErrorType();
        
    }
}

abstract class LogicalExpNode extends BinaryExpNode {
    public LogicalExpNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }


    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type1 = myExp1.typeCheck();
        Type type2 = myExp2.typeCheck();
        Type retType = new BoolType();

        if (!type1.isErrorType() && !type1.isBoolType()) {
            ErrMsg.fatal(myExp1.lineNum(), myExp1.charNum(),
                         "Logical operator applied to non-bool operand");
            retType = new ErrorType();
        }

        if (!type2.isErrorType() && !type2.isBoolType()) {
            ErrMsg.fatal(myExp2.lineNum(), myExp2.charNum(),
                         "Logical operator applied to non-bool operand");
            retType = new ErrorType();
        }

        if (type1.isErrorType() || type2.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public Type getType() {
        Type t1 = myExp1.getType();
        Type t2 = myExp2.getType();
        
        if(t1.equals(new BoolType()) && t2.equals(new BoolType())) {
            return new BoolType();
        }
        
        if(!t1.equals(new ErrorType()) && !t1.equals(new BoolType())) {
            ErrMsg.fatal(myExp1.lineNum(),myExp1.charNum(),"Logical operator applied to non-bool operand");
        }
        
        if(!t2.equals(new ErrorType()) && !t2.equals(new BoolType())) {
            ErrMsg.fatal(myExp2.lineNum(),myExp2.charNum(),"Logical operator applied to non-bool operand");
        }
        
        return new ErrorType();
        
    }
}

abstract class EqualityExpNode extends BinaryExpNode {
    public EqualityExpNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {

        return;
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type1 = myExp1.typeCheck();
        Type type2 = myExp2.typeCheck();
        Type retType = new BoolType();

        if (type1.isVoidType() && type2.isVoidType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                         "Equality operator applied to void functions");
            retType = new ErrorType();
        }

        if (type1.isFnType() && type2.isFnType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                         "Equality operator applied to functions");
            retType = new ErrorType();
        }

        if (type1.isStructDefType() && type2.isStructDefType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                         "Equality operator applied to struct names");
            retType = new ErrorType();
        }

        if (type1.isStructType() && type2.isStructType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                         "Equality operator applied to struct variables");
            retType = new ErrorType();
        }

        if (!type1.equals(type2) && !type1.isErrorType() && !type2.isErrorType()) {
            ErrMsg.fatal(lineNum(), charNum(),
                         "Type mismatch");
            retType = new ErrorType();
        }

        if (type1.isErrorType() || type2.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public Type getType() {
        Type t1 = myExp1.getType();
        Type t2 = myExp2.getType();
        if((!t1.equals(new ErrorType()))&&(!t2.equals(new ErrorType()))) {
            if(!t1.equals(t2)) {
                ErrMsg.fatal(myExp1.lineNum(),myExp1.charNum(),"Type mismatch");
                return new ErrorType();
            }
            else {
                if(t1.equals(new FnType())) {
                    ErrMsg.fatal(myExp1.lineNum(),myExp1.charNum(),"Equality operator applied to functions");
                    return new ErrorType();
                } else if(t1.equals(new VoidType())) {
                    ErrMsg.fatal(myExp1.lineNum(),myExp1.charNum(),"Equality operator applied to void functions");
                    return new ErrorType();
                } else if(t1.equals(new StructType(null))) {
                    ErrMsg.fatal(myExp1.lineNum(),myExp1.charNum(),"Equality operator applied to struct variables");
                    return new ErrorType();
                } else if(t1.equals(new StructDefType())) {
                    ErrMsg.fatal(myExp1.lineNum(),myExp1.charNum(),"Equality operator applied to struct names");
                    return new ErrorType();
                }
            }
        }
        return new BoolType();
    }
}

abstract class RelationalExpNode extends BinaryExpNode {
    public RelationalExpNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    /**
     * typeCheck
     */
    public Type typeCheck() {
        Type type1 = myExp1.typeCheck();
        Type type2 = myExp2.typeCheck();
        Type retType = new BoolType();

        if (!type1.isErrorType() && !type1.isIntType()) {
            ErrMsg.fatal(myExp1.lineNum(), myExp1.charNum(),
                         "Relational operator applied to non-numeric operand");
            retType = new ErrorType();
        }

        if (!type2.isErrorType() && !type2.isIntType()) {
            ErrMsg.fatal(myExp2.lineNum(), myExp2.charNum(),
                         "Relational operator applied to non-numeric operand");
            retType = new ErrorType();
        }

        if (type1.isErrorType() || type2.isErrorType()) {
            retType = new ErrorType();
        }

        return retType;
    }

    public Type getType() {
        Type t1 = myExp1.getType();
        Type t2 = myExp2.getType();
        
        if(t1.equals(new IntType()) && t2.equals(new IntType())) {
            return new BoolType();
        }
        
        if(!t1.equals(new ErrorType()) && !t1.equals(new IntType())) {
            ErrMsg.fatal(myExp1.lineNum(),myExp1.charNum(),"Relational operator applied to non-numeric operand");
        }
        
        if(!t2.equals(new ErrorType()) && !t2.equals(new IntType())) {
            ErrMsg.fatal(myExp2.lineNum(),myExp2.charNum(),"Relational operator applied to non-numeric operand");
        }
        
        return new ErrorType();
        
    }
}

class PlusNode extends ArithmeticExpNode {
    public PlusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox("+",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        
        

        return;
    }
    
    public void codeGen(PrintWriter p) {
            myExp1.codeGen(p);
            myExp2.codeGen(p);
            Codegen.genPop("$t0");
            Codegen.genPop("$t1");
            Codegen.generate("add","$t0","$t1","$t0");
            Codegen.genPush("$t0");

    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" + ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class MinusNode extends ArithmeticExpNode {
    public MinusNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox("-",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        myExp1.codeGen(p);
        myExp2.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.genPop("$t1");
        Codegen.generate("sub","$t0","$t1","$t0");
        Codegen.genPush("$t0");
    
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" - ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class TimesNode extends ArithmeticExpNode {
    public TimesNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }
    
    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox("*",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }

    public void codeGen(PrintWriter p) {
        myExp1.codeGen(p);
        myExp2.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.genPop("$t1");
        Codegen.generate("mult","$t0","$t1");
        Codegen.generate("mflo","$t0");
        Codegen.genPush("$t0");
    }


    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" * ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class DivideNode extends ArithmeticExpNode {
    public DivideNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox("/",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        myExp1.codeGen(p);
        myExp2.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.genPop("$t1");
        Codegen.generate("div","$t1","$t0");
        Codegen.generate("mflo","$t0");
        Codegen.genPush("$t0");
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" / ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class AndNode extends LogicalExpNode {
    public AndNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox("&&",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        String end = Codegen.nextLabel();
        myExp1.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.generateWithComment("beq","short circuited","$t0","$0",end);

        Codegen.genPush("$t0");
        myExp2.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.genPop("$t1");
        Codegen.generate("and","$t0","$t0","$t1");

        Codegen.genLabel(end);
        Codegen.genPush("$t0");


    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" && ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class OrNode extends LogicalExpNode {
    public OrNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox("||",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        String end = Codegen.nextLabel();
        myExp1.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.generateWithComment("bne","short circuited","$t0","$0",end);

        Codegen.genPush("$t0");
        myExp2.codeGen(p);
        Codegen.genPop("$t0");
        Codegen.genPop("$t1");
        Codegen.generate("or","$t0","$t0","$t1");

        Codegen.genLabel(end);
        Codegen.genPush("$t0");
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" || ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class EqualsNode extends EqualityExpNode {
    public EqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox("==",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        if(myExp1.typeCheck().equals(new StringType())) {
            String loop = Codegen.nextLabel();
            String notequal = Codegen.nextLabel();
            String equal = Codegen.nextLabel();
            String end = Codegen.nextLabel();

            myExp1.codeGen(p);
            myExp2.codeGen(p);
            Codegen.genPop("$t0");
            Codegen.genPop("$t1");

            Codegen.genLabel(loop,"compare chars of string");
            Codegen.generateIndexed("lb","$t2","$t0",0);
            Codegen.generateIndexed("lb","$t3","$t1",0);
            Codegen.generate("bne","$t2","$t3",notequal);
            Codegen.generate("beq","$t2","$0",equal);
            Codegen.generate("addi","$t0","$t0",1);
            Codegen.generate("addi","$t1","$t1",1);
            Codegen.generate("j",loop);

            Codegen.genLabel(notequal,"string not equal");
            Codegen.generate("li","$t0",0);
            Codegen.genPush("$t0");
            Codegen.generate("j",end);

            Codegen.genLabel(equal,"string is equal");
            Codegen.generate("li","$t0",1);
            Codegen.genPush("$t0");

            Codegen.genLabel(end);

        } else {
            myExp1.codeGen(p);
            myExp2.codeGen(p);
            Codegen.genPop("$t0");
            Codegen.genPop("$t1");
            Codegen.generate("seq","$t0","$t0","$t1");
            Codegen.genPush("$t0");

        }
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" == ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class NotEqualsNode extends EqualityExpNode {
    public NotEqualsNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox("!=",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        if(myExp1.typeCheck().equals(new StringType())) {
            String loop = Codegen.nextLabel();
            String notequal = Codegen.nextLabel();
            String equal = Codegen.nextLabel();
            String end = Codegen.nextLabel();

            myExp1.codeGen(p);
            myExp2.codeGen(p);
            Codegen.genPop("$t0");
            Codegen.genPop("$t1");

            Codegen.genLabel(loop,"compare chars of string");
            Codegen.generateIndexed("lb","$t2","$t0",0);
            Codegen.generateIndexed("lb","$t3","$t1",0);
            Codegen.generate("bne","$t2","$t3",notequal);
            Codegen.generate("beq","$t2","$0",equal);
            Codegen.generate("addi","$t0","$t0",1);
            Codegen.generate("addi","$t1","$t1",1);
            Codegen.generate("j",loop);

            Codegen.genLabel(notequal,"string not equal");
            Codegen.generate("li","$t0",1);
            Codegen.genPush("$t0");
            Codegen.generate("j",end);

            Codegen.genLabel(equal,"string is equal");
            Codegen.generate("li","$t0",0);
            Codegen.genPush("$t0");

            Codegen.genLabel(end);

        } else {
            myExp1.codeGen(p);
            myExp2.codeGen(p);
            Codegen.genPop("$t0");
            Codegen.genPop("$t1");
            Codegen.generate("sne","$t0","$t0","$t1");
            Codegen.genPush("$t0");

        }
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" != ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessNode extends RelationalExpNode {
    public LessNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox("<",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        myExp1.codeGen(p);
        myExp2.codeGen(p);
        Codegen.genPop("$t1");
        Codegen.genPop("$t0");
        Codegen.generate("slt","$t0","$t0","$t1");
        Codegen.genPush("$t0");
    
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" < ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterNode extends RelationalExpNode {
    public GreaterNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox(">",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        myExp1.codeGen(p);
        myExp2.codeGen(p);
        Codegen.genPop("$t1");
        Codegen.genPop("$t0");
        Codegen.generate("sgt","$t0","$t0","$t1");
        Codegen.genPush("$t0");
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" > ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class LessEqNode extends RelationalExpNode {
    public LessEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox("<=",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        }  else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        myExp1.codeGen(p);
        myExp2.codeGen(p);
        Codegen.genPop("$t1");
        Codegen.genPop("$t0");
        Codegen.generate("sle","$t0","$t0","$t1");
        Codegen.genPush("$t0");
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" <= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}

class GreaterEqNode extends RelationalExpNode {
    public GreaterEqNode(ExpNode exp1, ExpNode exp2) {
        super(exp1, exp2);
    }

    public void buildTree(DefaultTreeForTreeLayout<TextInBox> tree, TextInBox parent) {
        TextInBox n1 = new TextInBox("exp",30,20);
        TextInBox n2 = new TextInBox("exp",30,20);

        tree.addChild(parent,n1);
        tree.addChild(parent,new TextInBox(">=",20,20));
        tree.addChild(parent,n2);

        if(myExp1 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp1.buildTree(tree,nID);
        } else if(myExp1 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nC);
            myExp1.buildTree(tree,nC);
        } else if(myExp1 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n1,nT);
            tree.addChild(nT,nL);
            myExp1.buildTree(tree,nL);
        } else if (myExp1 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n1,nA);
            myExp1.buildTree(tree,nA);
        } else {
            myExp1.buildTree(tree,n1);
        }

        if(myExp2 instanceof IdNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            TextInBox nID = new TextInBox("id",20,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            tree.addChild(nL,nID);
            myExp2.buildTree(tree,nID);
        } else if(myExp2 instanceof CallExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nC = new TextInBox("fncall",50,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nC);
            myExp2.buildTree(tree,nC);
        } else if(myExp2 instanceof DotAccessExpNode) {
            TextInBox nT = new TextInBox("term",35,20);
            TextInBox nL = new TextInBox("loc",30,20);
            tree.addChild(n2,nT);
            tree.addChild(nT,nL);
            myExp2.buildTree(tree,nL);
        } else if (myExp2 instanceof AssignNode) {
            TextInBox nA = new TextInBox("aExp",30,20);
            tree.addChild(n2,nA);
            myExp2.buildTree(tree,nA);
        } else {
            myExp2.buildTree(tree,n2);
        }

        return;
    }
    
    public void codeGen(PrintWriter p) {
        myExp1.codeGen(p);
        myExp2.codeGen(p);
        Codegen.genPop("$t1");
        Codegen.genPop("$t0");
        Codegen.generate("sge","$t0","$t0","$t1");
        Codegen.genPush("$t0");
    }

    public void unparse(PrintWriter p, int indent) {
        p.print("(");
        myExp1.unparse(p, 0);
        p.print(" >= ");
        myExp2.unparse(p, 0);
        p.print(")");
    }
}
