import java.util.*;
import java.io.*;
import java_cup.runtime.*;  // defines Symbol
import project.*;

/**
 * This program is to be used to test the Wumbo scanner.
 * This version is set up to test all tokens, but you are required to test 
 * other aspects of the scanner (e.g., input that causes errors, character 
 * numbers, values associated with tokens)
 */
public class P2 {
    public static void main(String[] args) throws IOException {
                                           // exception may be thrown by yylex
        // test all tokens
	System.out.println("For testing warnings and testing comments, there will\n"+
			"be a solution file to be compared w/ that contains sym\n"+
			"ints to check for correctness. For testing char and line\n"+
			"nums, the solution file format is \"[line num] [char num]");
        testAllTokens();
        CharNum.num = 1;
    
	testWarnings();
	CharNum.num = 1;

	testComments();
	CharNum.num = 1;

	testCharAndLineNum();
	CharNum.num = 1;
       
    }

    /**
     * testCharAndLineNum
     *
     * This method reads in testCharLine.in which contains
     * a bunch of various symbols and white space
     * It outputs a file format where each symbol outputs
     * one line with its line number and char number
     * then compare to a solution file
     */
    private static void testCharAndLineNum() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("testCharLine.in");
            outFile = new PrintWriter(new FileWriter("testCharLine.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File testCharLine.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("testCharLine.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex scanner = new Yylex(inFile);
        Symbol token = scanner.next_token();

        while (token.sym != sym.EOF) {
		outFile.println(((TokenVal)token.value).linenum+" "+((TokenVal)token.value).charnum);
                token = scanner.next_token();
        }

	outFile.close();

    }


    /**
     * testWarnings
     *
     * Open and read from badTokens.in which contains
     * several errors in tokens as well as some correct ones
     * Make sure warning messages are sent properly and also
     * make sure correct tokens are still accounted for
     * */
    private static void testWarnings() throws IOException {
	System.out.println("Testing errors that should be thrown");
	System.out.println("Sequence of errors:");
	System.out.println("unterminated+bad escape,bad escape, unterminated,\n" 
			+" max int, unterminated+bad escape, unterminated");
	// open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("badTokens.in");
            outFile = new PrintWriter(new FileWriter("badTokens.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File badTokens.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("badTokens.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex scanner = new Yylex(inFile);
        Symbol token = scanner.next_token();

	while (token.sym != sym.EOF) {
		outFile.println(token.sym);
		token = scanner.next_token();
	}

	outFile.close();
    }

    /**
     * testComments
     *
     * Open and read from file testComments.in
     * Write all sym ints for each token and make sure all
     * comments are ignored and don't affect tokens
     * compare output to a solution file
     */
    private static void testComments() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("testComments.in");
            outFile = new PrintWriter(new FileWriter("testComments.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File testComments.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("testComments.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex scanner = new Yylex(inFile);
        Symbol token = scanner.next_token();

        while (token.sym != sym.EOF) {
                outFile.println(token.sym);
                token = scanner.next_token();
        }
	
	outFile.close();
    }

    /**
     * testAllTokens
     *
     * Open and read from file allTokens.txt
     * For each token read, write the corresponding string to allTokens.out
     * If the input file contains all tokens, one per line, we can verify
     * correctness of the scanner by comparing the input and output files
     * (e.g., using a 'diff' command).
     */
    private static void testAllTokens() throws IOException {
        // open input and output files
        FileReader inFile = null;
        PrintWriter outFile = null;
        try {
            inFile = new FileReader("allTokens.in");
            outFile = new PrintWriter(new FileWriter("allTokens.out"));
        } catch (FileNotFoundException ex) {
            System.err.println("File allTokens.in not found.");
            System.exit(-1);
        } catch (IOException ex) {
            System.err.println("allTokens.out cannot be opened.");
            System.exit(-1);
        }

        // create and call the scanner
        Yylex scanner = new Yylex(inFile);
        Symbol token = scanner.next_token();
        while (token.sym != sym.EOF) {
            switch (token.sym) {
            case sym.BOOL:
                outFile.println("bool"); 
                break;
            case sym.INT:
                outFile.println("int");
                break;
            case sym.VOID:
                outFile.println("void");
                break;
            case sym.TRUE:
                outFile.println("true"); 
                break;
            case sym.FALSE:
                outFile.println("false"); 
                break;
            case sym.STRUCT:
                outFile.println("struct"); 
                break;
            case sym.CIN:
                outFile.println("cin"); 
                break;
            case sym.COUT:
                outFile.println("cout");
                break;				
            case sym.IF:
                outFile.println("if");
                break;
            case sym.ELSE:
                outFile.println("else");
                break;
            case sym.WHILE:
                outFile.println("while");
                break;
            case sym.RETURN:
                outFile.println("return");
                break;
            case sym.ID:
                outFile.println(((IdTokenVal)token.value).idVal);
                break;
            case sym.INTLITERAL:  
                outFile.println(((IntLitTokenVal)token.value).intVal);
                break;
            case sym.STRINGLITERAL: 
                outFile.println(((StrLitTokenVal)token.value).strVal);
                break;    
            case sym.LCURLY:
                outFile.println("{");
                break;
            case sym.RCURLY:
                outFile.println("}");
                break;
            case sym.LPAREN:
                outFile.println("(");
                break;
            case sym.RPAREN:
                outFile.println(")");
                break;
            case sym.SEMICOLON:
                outFile.println(";");
                break;
            case sym.COMMA:
                outFile.println(",");
                break;
            case sym.DOT:
                outFile.println(".");
                break;
            case sym.WRITE:
                outFile.println("<<");
                break;
            case sym.READ:
                outFile.println(">>");
                break;				
            case sym.PLUSPLUS:
                outFile.println("++");
                break;
            case sym.MINUSMINUS:
                outFile.println("--");
                break;	
            case sym.PLUS:
                outFile.println("+");
                break;
            case sym.MINUS:
                outFile.println("-");
                break;
            case sym.TIMES:
                outFile.println("*");
                break;
            case sym.DIVIDE:
                outFile.println("/");
                break;
            case sym.NOT:
                outFile.println("!");
                break;
            case sym.AND:
                outFile.println("&&");
                break;
            case sym.OR:
                outFile.println("||");
                break;
            case sym.EQUALS:
                outFile.println("==");
                break;
            case sym.NOTEQUALS:
                outFile.println("!=");
                break;
            case sym.LESS:
                outFile.println("<");
                break;
            case sym.GREATER:
                outFile.println(">");
                break;
            case sym.LESSEQ:
                outFile.println("<=");
                break;
            case sym.GREATEREQ:
                outFile.println(">=");
                break;
			case sym.ASSIGN:
                outFile.println("=");
                break;
			default:
				outFile.println("UNKNOWN TOKEN");
            } // end switch

            token = scanner.next_token();
        } // end while
        outFile.close();
    }
}
