import java_cup.runtime.*; // defines the Symbol class
import project.*;
// The generated scanner will return a Symbol for each token that it finds.
// A Symbol contains an Object field named value; that field will be of type
// TokenVal, defined below.
//
// A TokenVal object contains the line number on which the token occurs as
// well as the number of the character on that line that starts the token.
// Some tokens (literals and IDs) also include the value of the token.
class TokenVal {
  // fields
    int linenum;
    int charnum;
  // constructor
    TokenVal(int line, int ch) {
        linenum = line;
        charnum = ch;
    }
}
class IntLitTokenVal extends TokenVal {
  // new field: the value of the integer literal
    int intVal;
  // constructor
    IntLitTokenVal(int line, int ch, int val) {
        super(line, ch);
        intVal = val;
    }
}
class IdTokenVal extends TokenVal {
  // new field: the value of the identifier
    String idVal;
  // constructor
    IdTokenVal(int line, int ch, String val) {
        super(line, ch);
    idVal = val;
    }
}
class StrLitTokenVal extends TokenVal {
  // new field: the value of the string literal
    String strVal;
  // constructor
    StrLitTokenVal(int line, int ch, String val) {
        super(line, ch);
        strVal = val;
    }
}
// The following class is used to keep track of the character number at which
// the current token starts on its line.
class CharNum {
    static int num=1;
}


class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NOT_ACCEPT,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR,
		/* 74 */ YY_NO_ANCHOR,
		/* 75 */ YY_NO_ANCHOR,
		/* 76 */ YY_NO_ANCHOR,
		/* 77 */ YY_NO_ANCHOR,
		/* 78 */ YY_NO_ANCHOR,
		/* 79 */ YY_NO_ANCHOR,
		/* 80 */ YY_NO_ANCHOR,
		/* 81 */ YY_NO_ANCHOR,
		/* 82 */ YY_NO_ANCHOR,
		/* 83 */ YY_NO_ANCHOR,
		/* 84 */ YY_NO_ANCHOR,
		/* 85 */ YY_NO_ANCHOR,
		/* 86 */ YY_NO_ANCHOR,
		/* 87 */ YY_NO_ANCHOR,
		/* 88 */ YY_NO_ANCHOR,
		/* 89 */ YY_NO_ANCHOR,
		/* 90 */ YY_NO_ANCHOR,
		/* 91 */ YY_NO_ANCHOR,
		/* 92 */ YY_NO_ANCHOR,
		/* 93 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"46:9,27,26,46:2,23,46:18,27,41,22,45,46:2,42,25,30,31,39,37,33,38,34,40,21:" +
"10,46,32,35,44,36,25,46,19:26,46,24,46:2,19,46,13,1,15,8,11,12,20,17,4,20:2" +
",3,20,5,2,18,20,9,14,6,10,7,16,20:3,28,43,29,46:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,94,
"0,1,2,3,4,1:2,5,1:7,6,7,8,9,1,10,11,12,13,14,1:11,14:2,15,14:5,16,14:5,17,1" +
"8,19,1,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,14,35,36,37,38,39,40,41" +
",42,43,44,45,46,47,48,49,14,50,51,52,53,54,55,56,57")[0];

	private int yy_nxt[][] = unpackFromString(58,47,
"1,2,85:2,51,85,87,88,85,89,85,90,91,85,92,67,93,85:4,3,4,-1,5:2,6,7,8,9,10," +
"11,12,13,14,15,16,17,18,19,20,21,52,55,22,23,5,-1:48,85,68,85:16,69,85,69,-" +
"1:46,3,-1:26,4:21,25,4,50,4,-1,4:20,-1:27,7,-1:54,26,-1:8,27,-1:38,28,-1:7," +
"29,-1:39,30,-1:47,31,-1:48,23,-1:50,32,-1:46,35,-1:3,23:25,-1,23:20,-1,85:1" +
"8,69,85,69,-1:26,38:21,44,38:3,-1,38:20,-1:24,38,-1:23,38:4,4:2,38:15,4,38," +
"4:2,53,38:20,-1,85:4,54,85:6,24,85:6,69,85,69,-1:67,33,-1:5,85:5,36,85:12,6" +
"9,85,69,-1:68,34,-1:4,85:4,37,85:13,69,85,69,-1:26,85:2,39,85:15,69,85,69,-" +
"1:26,85:10,40,85:7,69,85,69,-1:26,85:7,41,85:10,69,85,69,-1:26,85:10,42,85:" +
"7,69,85,69,-1:26,85:5,43,85:12,69,85,69,-1:26,85:10,45,85:7,69,85,69,-1:26," +
"85:10,46,85:7,69,85,69,-1:26,85:4,47,85:13,69,85,69,-1:26,85:5,48,85:12,69," +
"85,69,-1:26,85:5,49,85:12,69,85,69,-1:26,85,76,85,56,85:14,69,85,69,-1:26,8" +
"5,57,85:16,69,85,69,-1:26,85:9,58,85:8,69,85,69,-1:26,85:3,59,85:14,69,85,6" +
"9,-1:26,85:5,78,85:11,79,69,85,69,-1:26,85:13,60,85:4,69,85,69,-1:26,85:2,8" +
"0,85:15,69,85,69,-1:26,85:8,86,85:9,69,85,69,-1:26,85:9,61,85:8,69,85,69,-1" +
":26,85:3,81,85:14,69,85,69,-1:26,85:9,82,85:8,69,85,69,-1:26,85:10,83,85:7," +
"69,85,69,-1:26,85:13,62,85:4,69,85,69,-1:26,85:2,63,85:15,69,85,69,-1:26,85" +
":8,64,85:9,69,85,69,-1:26,85:12,65,85:5,69,85,69,-1:26,85:14,66,85:3,69,85," +
"69,-1:26,85:9,84,85:8,69,85,69,-1:26,85:8,70,85:9,69,85,69,-1:26,85,71,85:1" +
"6,69,85,69,-1:26,85:10,72,85:7,69,85,69,-1:26,85:2,73,85:15,69,85,69,-1:26," +
"85:12,74,85:5,69,85,69,-1:26,85:5,75,85:12,69,85,69,-1:26,85:16,77,85,69,85" +
",69,-1:25");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {

return new Symbol(sym.EOF);
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -3:
						break;
					case 3:
						{ // NOTE: the following computation of the integer value does NOT
            //       check for overflow.  This must be modified.
	double d = Double.parseDouble(yytext());
	if(d > Integer.MAX_VALUE) {
		ErrMsg.warn(yyline+1,CharNum.num,"literal too large; using max value");
		Symbol s = new Symbol(sym.INTLITERAL,
			new IntLitTokenVal(yyline+1, CharNum.num, Integer.MAX_VALUE));
		CharNum.num += yytext().length();
		return s;
	}
            int val = Integer.parseInt(yytext());
            Symbol s = new Symbol(sym.INTLITERAL,
                             new IntLitTokenVal(yyline+1, CharNum.num, val));
            CharNum.num += yytext().length();
            return s;
          }
					case -4:
						break;
					case 4:
						{
	ErrMsg.fatal(yyline+1, CharNum.num, "unterminated string literal");
}
					case -5:
						break;
					case 5:
						{//NOTE: add warning message for max int
	 ErrMsg.fatal(yyline+1, CharNum.num,
                         "illegal character ignored: " + yytext());
            CharNum.num++;
          }
					case -6:
						break;
					case 6:
						{ CharNum.num = 1; }
					case -7:
						break;
					case 7:
						{ CharNum.num += yytext().length(); }
					case -8:
						break;
					case 8:
						{ Symbol s = new Symbol(sym.LCURLY,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -9:
						break;
					case 9:
						{ Symbol s = new Symbol(sym.RCURLY,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -10:
						break;
					case 10:
						{ Symbol s = new Symbol(sym.LPAREN,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -11:
						break;
					case 11:
						{ Symbol s = new Symbol(sym.RPAREN,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -12:
						break;
					case 12:
						{ Symbol s = new Symbol(sym.SEMICOLON,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -13:
						break;
					case 13:
						{ Symbol s = new Symbol(sym.COMMA,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -14:
						break;
					case 14:
						{ Symbol s = new Symbol(sym.DOT,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -15:
						break;
					case 15:
						{ Symbol s = new Symbol(sym.LESS,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -16:
						break;
					case 16:
						{ Symbol s = new Symbol(sym.GREATER,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -17:
						break;
					case 17:
						{ Symbol s = new Symbol(sym.PLUS, new TokenVal(yyline+1, CharNum.num));
            CharNum.num++;
            return s;
          }
					case -18:
						break;
					case 18:
						{ Symbol s = new Symbol(sym.MINUS,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -19:
						break;
					case 19:
						{ Symbol s = new Symbol(sym.TIMES,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -20:
						break;
					case 20:
						{ Symbol s = new Symbol(sym.DIVIDE,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -21:
						break;
					case 21:
						{ Symbol s = new Symbol(sym.NOT,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -22:
						break;
					case 22:
						{ Symbol s = new Symbol(sym.ASSIGN,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -23:
						break;
					case 23:
						{}
					case -24:
						break;
					case 24:
						{
        Symbol s = new Symbol(sym.IF,new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -25:
						break;
					case 25:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.STRINGLITERAL,
		new StrLitTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;	
}
					case -26:
						break;
					case 26:
						{ Symbol s = new Symbol(sym.WRITE,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -27:
						break;
					case 27:
						{ Symbol s = new Symbol(sym.LESSEQ,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -28:
						break;
					case 28:
						{ Symbol s = new Symbol(sym.READ,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -29:
						break;
					case 29:
						{ Symbol s = new Symbol(sym.GREATEREQ,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -30:
						break;
					case 30:
						{ Symbol s = new Symbol(sym.PLUSPLUS,
		new TokenVal(yyline+1, CharNum.num));
	CharNum.num += yytext().length();
	return s;	
}
					case -31:
						break;
					case 31:
						{ Symbol s = new Symbol(sym.MINUSMINUS,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -32:
						break;
					case 32:
						{ Symbol s = new Symbol(sym.NOTEQUALS,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -33:
						break;
					case 33:
						{ Symbol s = new Symbol(sym.AND,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -34:
						break;
					case 34:
						{ Symbol s = new Symbol(sym.OR,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -35:
						break;
					case 35:
						{ Symbol s = new Symbol(sym.EQUALS,
                new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -36:
						break;
					case 36:
						{
	Symbol s = new Symbol(sym.INT,new TokenVal(yyline+1, CharNum.num));
	CharNum.num += yytext().length();
	return s;
}
					case -37:
						break;
					case 37:
						{
        Symbol s = new Symbol(sym.CIN,new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -38:
						break;
					case 38:
						{
	ErrMsg.fatal(yyline+1, CharNum.num, "unterminated string literal with bad escaped character ignored");
}
					case -39:
						break;
					case 39:
						{
	Symbol s = new Symbol(sym.BOOL,new TokenVal(yyline+1, CharNum.num));
	CharNum.num += yytext().length();
	return s;
}
					case -40:
						break;
					case 40:
						{
	Symbol s = new Symbol(sym.TRUE,new TokenVal(yyline+1, CharNum.num));
	CharNum.num += yytext().length();
	return s;
}
					case -41:
						break;
					case 41:
						{
	Symbol s = new Symbol(sym.VOID,new TokenVal(yyline+1, CharNum.num));
	CharNum.num += yytext().length();
	return s;
}
					case -42:
						break;
					case 42:
						{
        Symbol s = new Symbol(sym.ELSE,new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -43:
						break;
					case 43:
						{
        Symbol s = new Symbol(sym.COUT,new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -44:
						break;
					case 44:
						{
	ErrMsg.fatal(yyline+1, CharNum.num, "string literal with bad escaped character ignored");
}
					case -45:
						break;
					case 45:
						{
	Symbol s = new Symbol(sym.FALSE,new TokenVal(yyline+1, CharNum.num));
	CharNum.num += yytext().length();
	return s;
}
					case -46:
						break;
					case 46:
						{
        Symbol s = new Symbol(sym.WHILE,new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -47:
						break;
					case 47:
						{
        Symbol s = new Symbol(sym.RETURN,new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -48:
						break;
					case 48:
						{
	Symbol s = new Symbol(sym.REPEAT, new TokenVal(yyline+1,CharNum.num));
	CharNum.num += yytext().length();
	return s;
	}
					case -49:
						break;
					case 49:
						{
        Symbol s = new Symbol(sym.STRUCT,new TokenVal(yyline+1, CharNum.num));
        CharNum.num += yytext().length();
        return s;
}
					case -50:
						break;
					case 51:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -51:
						break;
					case 52:
						{//NOTE: add warning message for max int
	 ErrMsg.fatal(yyline+1, CharNum.num,
                         "illegal character ignored: " + yytext());
            CharNum.num++;
          }
					case -52:
						break;
					case 53:
						{
	ErrMsg.fatal(yyline+1, CharNum.num, "unterminated string literal with bad escaped character ignored");
}
					case -53:
						break;
					case 54:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -54:
						break;
					case 55:
						{//NOTE: add warning message for max int
	 ErrMsg.fatal(yyline+1, CharNum.num,
                         "illegal character ignored: " + yytext());
            CharNum.num++;
          }
					case -55:
						break;
					case 56:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -56:
						break;
					case 57:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -57:
						break;
					case 58:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -58:
						break;
					case 59:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -59:
						break;
					case 60:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -60:
						break;
					case 61:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -61:
						break;
					case 62:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -62:
						break;
					case 63:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -63:
						break;
					case 64:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -64:
						break;
					case 65:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -65:
						break;
					case 66:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -66:
						break;
					case 67:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -67:
						break;
					case 68:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -68:
						break;
					case 69:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -69:
						break;
					case 70:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -70:
						break;
					case 71:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -71:
						break;
					case 72:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -72:
						break;
					case 73:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -73:
						break;
					case 74:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -74:
						break;
					case 75:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -75:
						break;
					case 76:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -76:
						break;
					case 77:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -77:
						break;
					case 78:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -78:
						break;
					case 79:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -79:
						break;
					case 80:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -80:
						break;
					case 81:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -81:
						break;
					case 82:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -82:
						break;
					case 83:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -83:
						break;
					case 84:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -84:
						break;
					case 85:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -85:
						break;
					case 86:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -86:
						break;
					case 87:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -87:
						break;
					case 88:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -88:
						break;
					case 89:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -89:
						break;
					case 90:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -90:
						break;
					case 91:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -91:
						break;
					case 92:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -92:
						break;
					case 93:
						{
	String val = yytext();
	Symbol s = new Symbol(sym.ID,
		new IdTokenVal(yyline+1, CharNum.num, val));
	CharNum.num += yytext().length();
	return s;
}
					case -93:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
