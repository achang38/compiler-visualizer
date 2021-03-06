import java.awt.Container;
import java.awt.Toolkit;
import java.awt.Dialog;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTable;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;
import org.abego.treelayout.demo.SampleTreeFactory;
import org.abego.treelayout.demo.TextInBox;
import org.abego.treelayout.demo.TextInBoxNodeExtentProvider;
import org.abego.treelayout.util.*;
import org.abego.treelayout.demo.swing.TextInBoxTreePane;

import java.util.*;
import java.io.*;
import java.nio.file.*;
import java_cup.runtime.*;
import project.*;

/**
 * 
 * @author Allen Chang
 * This class opens a window that teaches the user how a compiler works
 * It extends the JFrame class of Java Swing
 * 
 * Credits: 
 * Copyright (c) 2011, abego Software GmbH, Germany (http://www.abego.org)
 * for the use of the treelayout package and the setting up of the tree in a JPanel
 * within the buildTree() method
 */

public class CompilerVisualizer extends javax.swing.JFrame {

    /**
     * Constructor initalizes components
     */
    public CompilerVisualizer() {
        initComponents();
        setExtendedState(CompilerVisualizer.MAXIMIZED_BOTH);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    @SuppressWarnings("unchecked")
    private void initComponents() {

        errorDialog = new javax.swing.JDialog();
        errorTextPane = new javax.swing.JScrollPane();
        errorTextArea = new javax.swing.JTextArea();
        errorTablePane = new javax.swing.JScrollPane();
        errorTable = new javax.swing.JTable();
        tokenDialog = new javax.swing.JDialog();
        regHolder = new javax.swing.JScrollPane();
        regExpText = new javax.swing.JTextArea();
        grammarDialog = new javax.swing.JDialog();
        grammarHolder = new javax.swing.JScrollPane();
        grammarText = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        startButton = new javax.swing.JButton();
        backButton = new javax.swing.JButton();
        nextButton = new javax.swing.JButton();
        tokenButton = new javax.swing.JButton();
        grammarButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        startPanel = new javax.swing.JPanel();
        titleText = new javax.swing.JTextField();
        instructionsHolder = new javax.swing.JScrollPane();
        instructionText = new javax.swing.JTextArea();
        authorText = new javax.swing.JTextField();
        panel1 = new javax.swing.JPanel();
        programHolder = new javax.swing.JScrollPane();
        enterCodeArea = new javax.swing.JTextArea();
        programInstructions = new javax.swing.JTextField();
        panel2 = new javax.swing.JPanel();
        lexicalHolder = new javax.swing.JScrollPane();
        lexicalText = new javax.swing.JTextArea();
        panel3 = new javax.swing.JPanel();
        tokenHolder = new javax.swing.JScrollPane();
        tokenTable = new javax.swing.JTable();
        tokenText = new javax.swing.JTextField();
        panel4 = new javax.swing.JPanel();
        syntaxHolder = new javax.swing.JScrollPane();
        syntaxText = new javax.swing.JTextArea();
        panel5 = new javax.swing.JPanel();
        treeTextHolder = new javax.swing.JScrollPane();
        treeText = new javax.swing.JTextArea();
        treeHolder = new javax.swing.JScrollPane();
        panel6 = new javax.swing.JPanel();
        semanticHolder = new javax.swing.JScrollPane();
        semanticText = new javax.swing.JTextArea();
        panel7 = new javax.swing.JPanel();
        symbolHolder = new javax.swing.JScrollPane();
        symbolTable = new javax.swing.JTable();
        symbolTextHolder = new javax.swing.JScrollPane();
        symbolText = new javax.swing.JTextArea();
        panel8 = new javax.swing.JPanel();
        genHolder = new javax.swing.JScrollPane();
        genText = new javax.swing.JTextArea();
        panel9 = new javax.swing.JPanel();
        assemblyHolder = new javax.swing.JScrollPane();
        assemblyCode = new javax.swing.JTextArea();
        assemblyText = new javax.swing.JTextField();
        panel10 = new javax.swing.JPanel();
        endHolder = new javax.swing.JScrollPane();
        endText = new javax.swing.JTextArea();

        errorTextPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        errorTextArea.setEditable(false);
        errorTextArea.setColumns(20);
        errorTextArea.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        errorTextArea.setLineWrap(true);
        errorTextArea.setRows(5);
        errorTextArea.setText("The compiler encountered issues at this step. Please go back and fix your program.");
        errorTextPane.setViewportView(errorTextArea);

        javax.swing.GroupLayout errorDialogLayout = new javax.swing.GroupLayout(errorDialog.getContentPane());
        errorDialog.getContentPane().setLayout(errorDialogLayout);
        errorDialogLayout.setHorizontalGroup(
            errorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(errorDialogLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(errorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(errorTextPane)
                    .addComponent(errorTablePane, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
        errorDialogLayout.setVerticalGroup(
            errorDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(errorDialogLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(errorTextPane, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(errorTablePane, javax.swing.GroupLayout.PREFERRED_SIZE, 338, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        regExpText.setColumns(20);
        regExpText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 14)); // NOI18N
        regExpText.setRows(5);
        regExpText.setText("BOOL = \"bool\"\nINT = \"int\"\nVOID = \"void\"\nTRUE = \"true\"\nFALSE = \"false\"\nSTRUCT = \"struct\"\nCIN = \"cin\"\nCOUT = \"cout\"\nIF = \"if\"\nELSE = \"else\"\nWHILE = \"while\"\nRETURN = \"return\"\nREPEAT = \"repeat\"\nID = (([A-Z]|[a-z])|_)(([A-Z]|[a-z])|_|[0-9])*\nSTRINGLITERAL = [\\\"]([^\\n\\\"\\\\]|([\\\\](n|t|[\\?]|[\\\"]|[\\']|[\\\\])))*[\\\"]\nINTLITERAL = [0-9]+\nLCURLY = \"{\"\nRCURLY = \"}\"\nLPAREN = \"(\"\nRPAREN = \")\"\nSEMICOLON = \";\"\nCOMMA = \",\"\nDOT = \".\"\nWRITE = \"<<\"\nREAD = \">>\"\nPLUSPLUS = \"++\"\nMINUSMINUS = \"--\"\nPLUS = \"+\"\nMINUS = \"-\"\nTIMES = \"*\"\nDIVIDE = \"/\"\nNOT = \"!\"\nAND = \"&&\"\nOR = \"||\"\nEQUALS = \"==\"\nNOTEQUALS = \"!=\"\nLESSEQ = \"<=\"\nGREATEREQ = \">=\"\nLESS = \"<\"\nGREATER = \">\"\nASSIGN = \"=\"\n");
        regHolder.setViewportView(regExpText);

        javax.swing.GroupLayout tokenDialogLayout = new javax.swing.GroupLayout(tokenDialog.getContentPane());
        tokenDialog.getContentPane().setLayout(tokenDialogLayout);
        tokenDialogLayout.setHorizontalGroup(
            tokenDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tokenDialogLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(regHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 584, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        tokenDialogLayout.setVerticalGroup(
            tokenDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tokenDialogLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(regHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 628, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        grammarText.setEditable(false);
        grammarText.setColumns(20);
        grammarText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 13)); // NOI18N
        grammarText.setRows(5);
        grammarText.setText("program     -> dList\n\ndList          -> dList decl\n                | E(epsilon)\n\ndecl          -> vDecl\n                | fnDecl\n                | sDecl\n\nvList          -> vList vDecl\n                | E(epsilon)\n\nvDecl        -> type id SEMICOLON\n                | STRUCT id id SEMICOLON\n\nfnDecl       -> type id formals fnBody\n                \nsDecl        -> STRUCT id LCURLY sBody RCURLY SEMICOLON\n                \nsBody        -> sBody vDecl\n                | vDecl\n                \nformals      -> LPAREN RPAREN\n                | LPAREN fList RPAREN\n\nfList           -> fDecl\n                | fDecl COMMA fList\n\nfDecl         -> type id\n\nfnBody       -> LCURLY vList sList RCURLY\n\nsList          -> sList stmt\n                | E(epsilon)\n\nstmt          -> aExp SEMICOLON\n                | loc PLUSPLUS SEMICOLON\n                | loc MINUSMINUS SEMICOLON\n                | CIN READ loc SEMICOLON\n                | COUT WRITE exp SEMICOLON\n                | IF LPAREN exp RPAREN LCURLY vList sList RCURLY\n                | IF LPAREN exp RPAREN LCURLY vList sList RCURLY ELSE LCURLY vList sList RCURLY\n                | WHILE LPAREN exp RPAREN LCURLY vList sList RCURLY\n                | REPEAT LPAREN exp RPAREN LCURLY vList sList RCURLY\n                | RETURN exp SEMICOLON\n                | RETURN SEMICOLON\n                | fncall SEMICOLON\n\naExp         -> loc ASSIGN exp\n                \nexp           -> aExp\n                | exp PLUS exp\n                | exp MINUS exp\n                | exp TIMES exp\n                | exp DIVIDE exp\n                | NOT exp\n                | exp AND exp\n                | exp OR exp\n                | exp EQUALS exp\n                | exp NOTEQUALS exp\n                | exp LESS exp\n                | exp GREATER exp\n                | exp LESSEQ exp\n                | exp GREATEREQ exp\n                | MINUS term\n                | term\n\nterm         -> loc\n                | INTLITERAL\n                | STRINGLITERAL\n                | TRUE\n                | FALSE\n                | LPAREN exp RPAREN\n                | fncall\n\nfncall        ->  id LPAREN RPAREN\n                | id LPAREN aList RPAREN\n\naList         -> exp\n                | aList COMMA exp\n\ntype          -> INT\n                | BOOL\n                | VOID\n\nloc            -> id\n                | loc DOT id\n\nid             -> ID");
        grammarHolder.setViewportView(grammarText);

        javax.swing.GroupLayout grammarDialogLayout = new javax.swing.GroupLayout(grammarDialog.getContentPane());
        grammarDialog.getContentPane().setLayout(grammarDialogLayout);
        grammarDialogLayout.setHorizontalGroup(
            grammarDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grammarDialogLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(grammarHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 640, Short.MAX_VALUE)
                .addGap(20, 20, 20))
        );
        grammarDialogLayout.setVerticalGroup(
            grammarDialogLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(grammarDialogLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(grammarHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 638, Short.MAX_VALUE)
                .addGap(22, 22, 22))
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(83, 104, 120));
        jPanel1.setPreferredSize(new java.awt.Dimension(1000, 70));

        startButton.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 12)); // NOI18N
        startButton.setText("Start");
        startButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButtonActionPerformed(evt);
            }
        });

        backButton.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 12)); // NOI18N
        backButton.setText("Back");
        backButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backButtonActionPerformed(evt);
            }
        });

        nextButton.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 12)); // NOI18N
        nextButton.setText("Next");
        nextButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextButtonActionPerformed(evt);
            }
        });

        tokenButton.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 12)); // NOI18N
        tokenButton.setText("Tokens");
        tokenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tokenButtonActionPerformed(evt);
            }
        });

        grammarButton.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 12)); // NOI18N
        grammarButton.setText("Grammar");
        grammarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                grammarButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(startButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tokenButton)
                .addGap(18, 18, 18)
                .addComponent(grammarButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(backButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(nextButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(grammarButton)
                        .addComponent(tokenButton))
                    .addComponent(startButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        jPanel2.setBackground(new java.awt.Color(173, 216, 230));
        jPanel2.setLayout(new java.awt.CardLayout());

        startPanel.setBackground(new java.awt.Color(173, 216, 230));

        titleText.setEditable(false);
        titleText.setBackground(new java.awt.Color(173, 216, 230));
        titleText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 48)); // NOI18N
        titleText.setText("Compiler Visualizer");
        titleText.setBorder(null);
        

        instructionsHolder.setBorder(null);

        instructionText.setEditable(false);
        instructionText.setColumns(20);
        instructionText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        instructionText.setLineWrap(true);
        instructionText.setRows(5);
        instructionText.setText(" This application shows how Wumbo, a programming language that is a subset of C++ is compiled.\n\n Instructions:\n 1. The Tokens button"+ 
        "below shows the regular expressions used for parsing the program into tokens. \n 2. The grammar button shows the context free grammar that Wumbo uses for its syntax."+
        " \n 3. Wumbo allows for declarations: variables of int or bool, structs, struct variables, and functions. \n 4. Declaration names must be unique within their scope."+
        " There is the global scope and a new scope\n for each function and if/while/repeat loops. \n 5. Struct and function declarations may only occur in the global scope."+
        " \n 6. The global scope can only have declarations and no statements. Within other scopes, all variable\n declaration must occur before any statements. All variables"+
        " must be declared before they may be used. \n 7. If and while clauses must hold an boolean expression, repeat clauses must hold an integer expression.\n "+
        "8. Arithmetic and relational operators must have only integers as operands. Logical operators must\n have only booleans as operands.\n 9. Statements include"+
        " if/while/repeat loops, assignments, function calls, read, write, and increment/decrement.\n 10. Wumbo programs must have a function called \"main\", its parameters"+
        " and return types are flexible.\n 11. If there is a mistake in your program, this compiler will notify you at the appropriate section.");
        instructionText.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        instructionsHolder.setViewportView(instructionText);

        authorText.setEditable(false);
        authorText.setBackground(new java.awt.Color(173, 216, 230));
        authorText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 24)); // NOI18N
        authorText.setText("Created by Allen Chang");
        authorText.setBorder(null);

        javax.swing.GroupLayout startPanelLayout = new javax.swing.GroupLayout(startPanel);
        startPanel.setLayout(startPanelLayout);
        startPanelLayout.setHorizontalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addContainerGap(179, Short.MAX_VALUE)
                .addGroup(startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(instructionsHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 887, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(startPanelLayout.createSequentialGroup()
                        .addGap(222, 222, 222)
                        .addComponent(titleText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(startPanelLayout.createSequentialGroup()
                        .addGap(300, 300, 300)
                        .addComponent(authorText, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(179, Short.MAX_VALUE))
        );
        startPanelLayout.setVerticalGroup(
            startPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(startPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(titleText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(authorText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45)
                .addComponent(instructionsHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.add(startPanel, "card2");

        panel1.setBackground(new java.awt.Color(173, 216, 230));
        panel1.setPreferredSize(new java.awt.Dimension(700, 500));

        enterCodeArea.setColumns(20);
        enterCodeArea.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        enterCodeArea.setRows(5);
        programHolder.setViewportView(enterCodeArea);

        programInstructions.setEditable(false);
        programInstructions.setBackground(new java.awt.Color(173, 216, 230));
        programInstructions.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        programInstructions.setText("Please enter your program below to begin. An example is shown to help you begin.");
        programInstructions.setBorder(null);

        javax.swing.GroupLayout panel1Layout = new javax.swing.GroupLayout(panel1);
        panel1.setLayout(panel1Layout);
        panel1Layout.setHorizontalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(programHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 1185, Short.MAX_VALUE)
                .addGap(30, 30, 30))
            .addGroup(panel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(programInstructions, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel1Layout.setVerticalGroup(
            panel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(programInstructions, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(programHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );

        jPanel2.add(panel1, "card3");

        panel2.setBackground(new java.awt.Color(173, 216, 230));
        panel2.setPreferredSize(new java.awt.Dimension(1000, 700));

        lexicalHolder.setBorder(null);

        lexicalText.setEditable(false);
        lexicalText.setBackground(new java.awt.Color(173, 216, 230));
        lexicalText.setColumns(20);
        lexicalText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        lexicalText.setLineWrap(true);
        lexicalText.setRows(5);
        lexicalText.setText("The first step in this compiler is the lexical analysis. This is when the source code is scanned into tokens.\nWith the use of regular"+
        " expressions, the compiler identifies meaningful words in the code while ignoring\nwhite space and comments. For Wumbo, any line that starts with \"//\" or"+
        " \"#\" is a comment. The lexical\nanalysis makes sure there are not any bad characters or bad strings in your program.");
        lexicalText.setBorder(null);
        lexicalHolder.setViewportView(lexicalText);

        javax.swing.GroupLayout panel2Layout = new javax.swing.GroupLayout(panel2);
        panel2.setLayout(panel2Layout);
        panel2Layout.setHorizontalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
                .addContainerGap(205, Short.MAX_VALUE)
                .addComponent(lexicalHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 834, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(206, Short.MAX_VALUE))
        );
        panel2Layout.setVerticalGroup(
            panel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel2Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lexicalHolder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(475, Short.MAX_VALUE))
        );

        jPanel2.add(panel2, "card4");

        panel3.setBackground(new java.awt.Color(173, 216, 230));

        tokenText.setEditable(false);
        tokenText.setBackground(new java.awt.Color(173, 216, 230));
        tokenText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        tokenText.setText("The table below shows the tokens that were scanned from your program. The format of each entry is Name(line number, character number)");
        tokenText.setBorder(null);

        javax.swing.GroupLayout panel3Layout = new javax.swing.GroupLayout(panel3);
        panel3.setLayout(panel3Layout);
        panel3Layout.setHorizontalGroup(
            panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel3Layout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addComponent(tokenHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 945, Short.MAX_VALUE)
                .addGap(150, 150, 150))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tokenText, javax.swing.GroupLayout.PREFERRED_SIZE, 1108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel3Layout.setVerticalGroup(
            panel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel3Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(tokenText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(tokenHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );

        jPanel2.add(panel3, "card5");

        panel4.setBackground(new java.awt.Color(173, 216, 230));

        syntaxHolder.setBorder(null);

        syntaxText.setEditable(false);
        syntaxText.setBackground(new java.awt.Color(173, 216, 230));
        syntaxText.setColumns(20);
        syntaxText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        syntaxText.setLineWrap(true);
        syntaxText.setRows(5);
        syntaxText.setText("The next step of this compiler is the syntax analysis. This is where the tokens created from the text scanner are\nparsed into an"+
        " abstract syntax tree. This is done by using a grammar. Wumbo's grammar is a set of rules that\ndescribe what sequence of tokens may occur in order for"+
        " the program to be syntactically correct. This is done\nwith terminals and nonterminals. Terminals are the base units which are the tokens that were scanned."+
        "\nNonterminals may be composed of other nonterminals, terminals, or the empty string. I will be using a capitol\nletter E to denote epsilon, which represents"+
        " the empty stringEach rule describes how nonterminals can broken\ndown until there are only terminals remaining. Additionally it's grammar is context free."+
        " This means for the rules\nthat describe how a nonterminal is composed, the nonterminal's position in relation to other tokens does\nnot matter.");
        syntaxText.setBorder(null);
        syntaxHolder.setViewportView(syntaxText);

        javax.swing.GroupLayout panel4Layout = new javax.swing.GroupLayout(panel4);
        panel4.setLayout(panel4Layout);
        panel4Layout.setHorizontalGroup(
            panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel4Layout.createSequentialGroup()
                .addContainerGap(175, Short.MAX_VALUE)
                .addComponent(syntaxHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 894, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(176, Short.MAX_VALUE))
        );
        panel4Layout.setVerticalGroup(
            panel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(syntaxHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 244, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(356, Short.MAX_VALUE))
        );

        jPanel2.add(panel4, "card8");

        panel5.setBackground(new java.awt.Color(173, 216, 230));

        treeTextHolder.setBorder(null);
        treeTextHolder.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        treeText.setEditable(false);
        treeText.setBackground(new java.awt.Color(173, 216, 230));
        treeText.setColumns(20);
        treeText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        treeText.setLineWrap(true);
        treeText.setRows(5);
        treeText.setText("This is the abstract syntax tree of your program. It shows how your program was parsed into all terminals using\nWumbo's grammar."+
        " Nodes that begin with a lower case letter and feature only alphabetic characters are\nnonterminals. Nodes that are all upper case letters or contain"+
        " non-alphabetic characters are terminals.");
        treeText.setBorder(null);
        treeTextHolder.setViewportView(treeText);

        javax.swing.GroupLayout panel5Layout = new javax.swing.GroupLayout(panel5);
        panel5.setLayout(panel5Layout);
        panel5Layout.setHorizontalGroup(
            panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel5Layout.createSequentialGroup()
                .addContainerGap(175, Short.MAX_VALUE)
                .addComponent(treeTextHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 894, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(176, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(treeHolder)
                .addGap(30, 30, 30))
        );
        panel5Layout.setVerticalGroup(
            panel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel5Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(treeTextHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(treeHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );

        jPanel2.add(panel5, "card7");

        panel6.setBackground(new java.awt.Color(173, 216, 230));

        semanticHolder.setBorder(null);

        semanticText.setEditable(false);
        semanticText.setBackground(new java.awt.Color(173, 216, 230));
        semanticText.setColumns(20);
        semanticText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        semanticText.setLineWrap(true);
        semanticText.setRows(5);
        semanticText.setText("The next step of the compiler is semantic analysis. In this stage, the compiler checks that all identifiers in\nthe program are"+
        " being used correctly. The semantic rules are as follows:\n1. Identifiers must be declared before they can be used in any other part of the program.\n"+
        "2. Identifiers must be unique within the same scope.\n3. Variable declarations cannot be of type void only function return types can.\n4. Dot access"+
        " can only occur on struct variables.\n5. Dot access on a struct variable must access a valid field of the struct.\n6. Function calls may only be use"+
        " identifiers declared as a function.\n7. Function identifiers can only be used in function calls.\n8. Only identifiers that are integer or boolean"+
        " variables can be used in read and write statements.\n9. Function call argument types must match function declaration types.\n10. Void functions cannot"+
        " return a value.\n11. Only integer variable identifiers can be used in arithmetic and relational operators.\n12. Only boolean variable identifiers can"+
        " be used in logical operators.\n13. Identifiers used in equality operators must have the same type on the left and right hand side.\n14."+
        " There must be a function called \"main\".");
        semanticText.setBorder(null);
        semanticHolder.setViewportView(semanticText);

        javax.swing.GroupLayout panel6Layout = new javax.swing.GroupLayout(panel6);
        panel6.setLayout(panel6Layout);
        panel6Layout.setHorizontalGroup(
            panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel6Layout.createSequentialGroup()
                .addContainerGap(195, Short.MAX_VALUE)
                .addComponent(semanticHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 855, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(195, Short.MAX_VALUE))
        );
        panel6Layout.setVerticalGroup(
            panel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel6Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(semanticHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(169, Short.MAX_VALUE))
        );

        jPanel2.add(panel6, "card6");

        panel7.setBackground(new java.awt.Color(173, 216, 230));

        symbolTextHolder.setBorder(null);
        symbolTextHolder.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        symbolText.setEditable(false);
        symbolText.setBackground(new java.awt.Color(173, 216, 230));
        symbolText.setColumns(20);
        symbolText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        symbolText.setLineWrap(true);
        symbolText.setRows(5);
        symbolText.setText("This is the symbol table that was used to do semantic analysis. Each identifier that is used in your program \nhas an entry in the symbol table."+
        " The information stored includes the type, the category, and the scope. ");
        symbolText.setBorder(null);
        symbolTextHolder.setViewportView(symbolText);

        javax.swing.GroupLayout panel7Layout = new javax.swing.GroupLayout(panel7);
        panel7.setLayout(panel7Layout);
        panel7Layout.setHorizontalGroup(
            panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel7Layout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(symbolHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
                .addGap(178, 178, 178))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(symbolTextHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 844, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel7Layout.setVerticalGroup(
            panel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel7Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(symbolTextHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(symbolHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );

        jPanel2.add(panel7, "card12");

        panel8.setBackground(new java.awt.Color(173, 216, 230));

        genHolder.setBorder(null);

        genText.setEditable(false);
        genText.setBackground(new java.awt.Color(173, 216, 230));
        genText.setColumns(20);
        genText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        genText.setLineWrap(true);
        genText.setRows(5);
        genText.setText("The final step of this compiler is code generation. This is where low level assembly code is generated that allocates\nregisters and memory"+
        " locations so that the computer will know how to run the program. This compiler will compile\nfor the MIPS instruction set architecture, therefore the code"+
        " generated will be in MIPS assembly language. The\nassembly language program can then be directly converted to binary machine code which is what is actually"+
        " used\nby the computer to execute your program.");
        genText.setBorder(null);
        genHolder.setViewportView(genText);

        javax.swing.GroupLayout panel8Layout = new javax.swing.GroupLayout(panel8);
        panel8.setLayout(panel8Layout);
        panel8Layout.setHorizontalGroup(
            panel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel8Layout.createSequentialGroup()
                .addContainerGap(173, Short.MAX_VALUE)
                .addComponent(genHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 898, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(174, Short.MAX_VALUE))
        );
        panel8Layout.setVerticalGroup(
            panel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel8Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(genHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(450, Short.MAX_VALUE))
        );

        jPanel2.add(panel8, "card11");

        panel9.setBackground(new java.awt.Color(173, 216, 230));

        assemblyCode.setEditable(false);
        assemblyCode.setColumns(20);
        assemblyCode.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        assemblyCode.setRows(5);
        assemblyHolder.setViewportView(assemblyCode);

        assemblyText.setEditable(false);
        assemblyText.setBackground(new java.awt.Color(173, 216, 230));
        assemblyText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        assemblyText.setText("This is the MIPS assembly language translation of your program.");
        assemblyText.setBorder(null);

        javax.swing.GroupLayout panel9Layout = new javax.swing.GroupLayout(panel9);
        panel9.setLayout(panel9Layout);
        panel9Layout.setHorizontalGroup(
            panel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel9Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(assemblyHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 1185, Short.MAX_VALUE)
                .addGap(30, 30, 30))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel9Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(assemblyText, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panel9Layout.setVerticalGroup(
            panel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel9Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(assemblyText, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(assemblyHolder, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                .addGap(30, 30, 30))
        );

        jPanel2.add(panel9, "card10");

        panel10.setBackground(new java.awt.Color(173, 216, 230));

        endHolder.setBorder(null);
        endHolder.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        endText.setEditable(false);
        endText.setBackground(new java.awt.Color(173, 216, 230));
        endText.setColumns(20);
        endText.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 18)); // NOI18N
        endText.setLineWrap(true);
        endText.setRows(5);
        endText.setText("That is the end of the compilers task. The assembly code generated on the previous page will also show up in a text\nfile in "+
        "the directory of this program called FinalCode.out. I hope this program was helpful for learning about how\na compiler works! This project idea"+
        " was inspired from my work I did in UW Madison's Programming Languages\nand Compilers course, Spring 2020. Please email me with any comments and feedback"+
        " at: allenc780@gmail.com");
        endText.setBorder(null);
        endHolder.setViewportView(endText);

        javax.swing.GroupLayout panel10Layout = new javax.swing.GroupLayout(panel10);
        panel10.setLayout(panel10Layout);
        panel10Layout.setHorizontalGroup(
            panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel10Layout.createSequentialGroup()
                .addContainerGap(169, Short.MAX_VALUE)
                .addComponent(endHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 907, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(169, Short.MAX_VALUE))
        );
        panel10Layout.setVerticalGroup(
            panel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel10Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(endHolder, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(430, Short.MAX_VALUE))
        );

        jPanel2.add(panel10, "card9");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1245, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 630, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }

    /**
     * This method executes whenever the start button is pressed. It takes the user back to the first page
     * @param evt
     */
    private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
        
        
        
        jPanel2.removeAll();
        
        curPanel = 0;
        
        jPanel2.add(panelList[0]);
        jPanel2.repaint();
        jPanel2.revalidate();
        ErrMsg.clearErrors();
        
    }

    /**
     * This method executes whenver the back button is pressed, It takes the user back one page
     * @param evt
     */
    private void backButtonActionPerformed(java.awt.event.ActionEvent evt) {
        
        if(curPanel > 0) {
            curPanel--;
            jPanel2.removeAll();
            jPanel2.add(panelList[curPanel]);
            jPanel2.repaint();
            jPanel2.revalidate();
        }

        
        ErrMsg.clearErrors();
        
        
    
    }

    /**
     * When next button is pressed, call appropriate step of compiler, then go to next page unless there is an error
     * If there is an error, do not go to next page, instead pop up dialog
     * @param evt
     */
    private void nextButtonActionPerformed(java.awt.event.ActionEvent evt) {
        ErrMsg.clearErrors();
        if(curPanel == 2) {
            buildTokens();
        } else if(curPanel == 4) {
            buildTree();
        } else if(curPanel == 6) {
            buildSymbols();
        } else if(curPanel == 8) {
            buildCode();
        }


        if(ErrMsg.getErr()) {
            buildErrors();
            
        } else if(curPanel<10) {
            curPanel++;
            jPanel2.removeAll();
            jPanel2.add(panelList[curPanel]);
            jPanel2.repaint();
            jPanel2.revalidate();
        }
        
    }

    /**
     * This method is called when the token button is pressed, it shows all regular expressions for each token
     * @param evt
     */
    private void tokenButtonActionPerformed(java.awt.event.ActionEvent evt) {
        
        
        tokenDialog.pack();
        tokenDialog.setBounds(X-tokenDialog.getWidth()/2,Y-tokenDialog.getHeight()/2,tokenDialog.getWidth(),tokenDialog.getHeight());
        tokenDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        tokenDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
        tokenDialog.setVisible(true);
    }

    /**
     * This method is called when the grammar button is pressed, it shows the context free grammar of Wumbo
     * @param evt
     */
    private void grammarButtonActionPerformed(java.awt.event.ActionEvent evt) {
        grammarDialog.pack();
        grammarDialog.setBounds(X-grammarDialog.getWidth()/2,Y-grammarDialog.getHeight()/2,grammarDialog.getWidth(),grammarDialog.getHeight());
        grammarDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
        grammarDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
        grammarDialog.setVisible(true);
    }

    /**
     * This sets the titles of dialog windows
     */
    public void initDialogs() {
        errorDialog.setTitle("Error");
        grammarDialog.setTitle("Grammar");
        tokenDialog.setTitle("Tokens");
    }

    /**
     * This array of panels is used to keep track of which panel to go to when Next and Back buttons are pressed
     */
    public void initPanelList() {
        javax.swing.JPanel panelInitial[] = {startPanel,panel1,panel2,panel3,panel4,panel5,panel6,panel7,panel8,panel9,panel10};
        panelList = panelInitial;
    }

    /**
     * This method scans the input program into tokens, then shows their information in a table
     */
    public void buildTokens() {
        try {
            String text = enterCodeArea.getText();
            sr = new StringReader(text);
            scanner = new Yylex(sr);
            Symbol token = scanner.next_token();
            ArrayList<String> tokens = new ArrayList<String>();
            CharNum.num = 1;
            while (token.sym != sym.EOF) {
                tokens.add(sym.terminalNames[token.sym]+"("+((TokenVal)token.value).linenum+","+((TokenVal)token.value).charnum+")");
                token = scanner.next_token();
            }

            double rows = Math.ceil((float)tokens.size()/(float)8);
            int numRows = (int)rows;

            String[][] data = new String[numRows][8];

            for(int i = 0;i<numRows;i++) {
                for(int j = 0;j<8;j++) {
                    if(i*8+j < tokens.size()) {
                        data[i][j] = tokens.get(i*8+j);
                    }
                }
            }

            tokenTable.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 14)); 
            tokenTable.setModel(new javax.swing.table.DefaultTableModel(
                data,
            new String [] {
                "Title x", "Title 2", "Title 3", "Title 4", "null", "null", "null", "null"
            }
            )   {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
            }
        });
        tokenTable.setTableHeader(null);
        tokenTable.setShowGrid(true);
        tokenTable.setShowHorizontalLines(true);
        tokenTable.setShowVerticalLines(true);
        tokenHolder.setViewportView(tokenTable);

        } catch (Exception e) {
            
        }
    }

    /**
     * This method builds the abstract syntax tree by parsing the program, then going through the tree to create the visual representation
     */
    public void buildTree() {

            try {
                String text = enterCodeArea.getText();
                sr = new StringReader(text);
                scanner = new Yylex(sr);
                p = new parser(scanner);
                Symbol CFGroot = p.parse();
                astRoot = (ProgramNode)CFGroot.value;
                TextInBox root = new TextInBox("program",55,20);
                DefaultTreeForTreeLayout<TextInBox> tree = new DefaultTreeForTreeLayout<TextInBox>(root);
                astRoot.buildTree(tree,root);

                // setup the tree layout configuration
		        double gapBetweenLevels = 20;
		        double gapBetweenNodes = 10;
		        DefaultConfiguration<TextInBox> configuration = new DefaultConfiguration<TextInBox>(
				gapBetweenLevels, gapBetweenNodes);

		        // create the NodeExtentProvider for TextInBox nodes
		        TextInBoxNodeExtentProvider nodeExtentProvider = new TextInBoxNodeExtentProvider();

		        // create the layout
		        TreeLayout<TextInBox> treeLayout = new TreeLayout<TextInBox>(tree,
				nodeExtentProvider, configuration);

		        // Create a panel that draws the nodes and edges and show the panel
                TextInBoxTreePane panel = new TextInBoxTreePane(treeLayout);
                treeHolder.setViewportView(panel);
                
            } catch(Exception e) {
                System.err.println("Unexpected error in buildTree method");
            }
    }

    /**
     * This method does name and type analysis by using a symbol table. The symbol table is then converted to a visual table
     */
    public void buildSymbols() {
        SymTable symT = astRoot.analyze();
        if(ErrMsg.getErr()) {
            return;
        }
        astRoot.nameAnalysis();
        astRoot.typeCheck();
        if(ErrMsg.getErr()) {
            return;
        }

        List<HashMap<String, Sym>> list = symT.getList();
        List<HashMap<String, Sym>> garbage = symT.getGarbage();
        String[] columnNames = {"ID","Type","Category","Scope"};
        ArrayList<String[]> tData = new ArrayList<String[]>();

        for (HashMap<String, Sym> symTab : list) {
            for(Map.Entry m : symTab.entrySet()) {
                String[] entry = new String[4];
                entry[0] = (String)m.getKey();
                entry[1] = ((Sym)m.getValue()).getType2();
                entry[2] = ((Sym)m.getValue()).getKind();
                entry[3] = "global";

                tData.add(entry);

            }
            
        }

        for (HashMap<String, Sym> symTab : garbage) {
            String scope = "null";
            if(symTab.containsKey("$scope")) {
                scope = symTab.get("$scope").toString();
                symTab.remove("$scope");

                for(Map.Entry m : symTab.entrySet()) {
                    String[] entry = new String[4];
                    entry[0] = (String)m.getKey();
                    entry[1] = ((Sym)m.getValue()).getType2();
                    entry[2] = ((Sym)m.getValue()).getKind();
                    entry[3] = scope;
    
                    tData.add(entry);
    
                }
            }

            
        }

        

        String[][] data = new String[tData.size()][3];

        for(int i = 0; i<tData.size();i++) {
            data[i] = tData.get(i);
        }

        symbolTable.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 14)); 
        symbolTable.setModel(new javax.swing.table.DefaultTableModel(
            data,
            columnNames
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        symbolTable.setShowGrid(true);
        symbolTable.setShowVerticalLines(false);
        symbolHolder.setViewportView(symbolTable);
    }

    /**
     * This method generates the assembly code for the user's program
     */
    public void buildCode() {
        
        try {
            PrintWriter outFile = new PrintWriter("newcode.out");
            Codegen.p = new PrintWriter(outFile);
            astRoot.codeGen(Codegen.p);
            outFile.close();
            String fileString = new String(Files.readAllBytes(Paths.get("newcode.out")));
            assemblyCode.setText(fileString);
        } catch (FileNotFoundException fe) {
            System.err.println("Unexpected error in buildCode");
        } catch (IOException ie) {
            System.err.println("Unexpected error in buildCode");
        }
    }

    /**
     * This method retrieves all the errors the user has made and shows them in a dialog box
     */
    public void buildErrors() {
        ArrayList<String> errors = ErrMsg.getList();
            String[][] eData = new String[errors.size()][1];
            for(int i = 0; i<errors.size();i++) {
                eData[i][0] = errors.get(i);
            }

            errorTable.setFont(new java.awt.Font("Nirmala UI Semilight", 0, 14)); 
            errorTable.setModel(new javax.swing.table.DefaultTableModel(
                eData,
                new String [] {
                    "Errors"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false
                };
    
                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
    
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            errorTablePane.setViewportView(errorTable);

            errorDialog.pack();
            errorDialog.setBounds(X-errorDialog.getWidth()/2,Y-errorDialog.getHeight()/2,errorDialog.getWidth(),errorDialog.getHeight());
            errorDialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
            errorDialog.setDefaultCloseOperation(javax.swing.JDialog.DISPOSE_ON_CLOSE);
            errorDialog.setVisible(true);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CompilerVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CompilerVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CompilerVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CompilerVisualizer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CompilerVisualizer Driver = new CompilerVisualizer();
                Driver.setTitle("Compiler Visualizer");
                Driver.initDialogs();
                Driver.setVisible(true);
                Driver.initPanelList();
            }
        });
        
        
    }

    // Variables declarations
    private javax.swing.JTextArea assemblyCode;
    private javax.swing.JScrollPane assemblyHolder;
    private javax.swing.JTextField assemblyText;
    private javax.swing.JTextField authorText;
    private javax.swing.JButton backButton;
    private javax.swing.JScrollPane endHolder;
    private javax.swing.JTextArea endText;
    private javax.swing.JTextArea enterCodeArea;
    private javax.swing.JDialog errorDialog;
    private javax.swing.JTable errorTable;
    private javax.swing.JScrollPane errorTablePane;
    private javax.swing.JTextArea errorTextArea;
    private javax.swing.JScrollPane errorTextPane;
    private javax.swing.JScrollPane genHolder;
    private javax.swing.JTextArea genText;
    private javax.swing.JButton grammarButton;
    private javax.swing.JDialog grammarDialog;
    private javax.swing.JScrollPane grammarHolder;
    private javax.swing.JTextArea grammarText;
    private javax.swing.JTextArea instructionText;
    private javax.swing.JScrollPane instructionsHolder;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane lexicalHolder;
    private javax.swing.JTextArea lexicalText;
    private javax.swing.JButton nextButton;
    private javax.swing.JPanel panel1;
    private javax.swing.JPanel panel10;
    private javax.swing.JPanel panel2;
    private javax.swing.JPanel panel3;
    private javax.swing.JPanel panel4;
    private javax.swing.JPanel panel5;
    private javax.swing.JPanel panel6;
    private javax.swing.JPanel panel7;
    private javax.swing.JPanel panel8;
    private javax.swing.JPanel panel9;
    private javax.swing.JScrollPane programHolder;
    private javax.swing.JTextField programInstructions;
    private javax.swing.JTextArea regExpText;
    private javax.swing.JScrollPane regHolder;
    private javax.swing.JScrollPane semanticHolder;
    private javax.swing.JTextArea semanticText;
    private javax.swing.JButton startButton;
    private javax.swing.JPanel startPanel;
    private javax.swing.JScrollPane symbolHolder;
    private javax.swing.JTable symbolTable;
    private javax.swing.JTextArea symbolText;
    private javax.swing.JScrollPane symbolTextHolder;
    private javax.swing.JScrollPane syntaxHolder;
    private javax.swing.JTextArea syntaxText;
    private javax.swing.JTextField titleText;
    private javax.swing.JButton tokenButton;
    private javax.swing.JDialog tokenDialog;
    private javax.swing.JScrollPane tokenHolder;
    private javax.swing.JTable tokenTable;
    private javax.swing.JTextField tokenText;
    private javax.swing.JScrollPane treeHolder;
    private javax.swing.JTextArea treeText;
    private javax.swing.JScrollPane treeTextHolder;

    private javax.swing.JPanel panelList[] = new javax.swing.JPanel[11];
    private int curPanel = 0;
    static final Dimension SCREEN_DIMENSION = Toolkit.getDefaultToolkit().getScreenSize();
    private int X = SCREEN_DIMENSION.width / 2; //position right in the middle of the screen
    private int Y = SCREEN_DIMENSION.height / 2;
    private Yylex scanner;
    private parser p;
    private ProgramNode astRoot;
    private StringReader sr;
}
