import java.util.*;

/**
 * The Sym class defines a symbol-table entry.
 * Each Sym contains a type (a Type).
 */
public class Sym {
    private Type type;
    private int offset;

    private String type2;
    private String kind;
    private SymTable table;
    private boolean dummy;
    private String funcstring;

    public Sym(Type type) {
        this.type = type;
    }

    public Sym(String type) {
        this.type2 = type;
        this.table = new SymTable();
    }

    public Type getType() {
        return type;
    }
    
    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public int getOffset() {
        return this.offset;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }
    
    public String getKind() {
        return kind;
    }

    
    public String toString() {
        
        if(type != null) {
            return type.toString();
        }

        return this.type2;
    }
    

    public void setTable(SymTable symT) {
	    this.table = symT;
    }
    
    public SymTable getTable() {
      return this.table;
    }

    public String getType2() {
        return this.type2;
    }
    
    public void setFunc(String s) {
        this.funcstring = s;
    }
    
    public String getFunc() {
        return funcstring;
    }
    
    public String toString2() {
        return this.type2;
    }



}

/**
 * The FnSym class is a subclass of the Sym class just for functions.
 * The returnType field holds the return type and there are fields to hold
 * information about the parameters.
 */
class FnSym extends Sym {
    // new fields
    private Type returnType;
    private int numParams;
    private List<Type> paramTypes;

    public FnSym(Type type, int numparams) {
        super(new FnType());
        returnType = type;
        numParams = numparams;
    }

    public void addFormals(List<Type> L) {
        paramTypes = L;
    }

    public Type getReturnType() {
        return returnType;
    }

    public int getNumParams() {
        return numParams;
    }

    public List<Type> getParamTypes() {
        return paramTypes;
    }

    public String toString() {
        // make list of formals
        String str = "";
        boolean notfirst = false;
        for (Type type : paramTypes) {
            if (notfirst)
                str += ",";
            else
                notfirst = true;
            str += type.toString();
        }

        str += "->" + returnType.toString();
        return str;
    }
}

/**
 * The StructSym class is a subclass of the Sym class just for variables
 * declared to be a struct type.
 * Each StructSym contains a symbol table to hold information about its
 * fields.
 */
class StructSym extends Sym {
    // new fields
    private IdNode structType;  // name of the struct type

    public StructSym(IdNode id) {
        super(new StructType(id));
        structType = id;
    }

    public IdNode getStructType() {
        return structType;
    }
}

/**
 * The StructDefSym class is a subclass of the Sym class just for the
 * definition of a struct type.
 * Each StructDefSym contains a symbol table to hold information about its
 * fields.
 */
class StructDefSym extends Sym {
    // new fields
    private SymTable symTab;

    public StructDefSym(SymTable table) {
        super(new StructDefType());
        symTab = table;
    }

    public SymTable getSymTable() {
        return symTab;
    }
}

