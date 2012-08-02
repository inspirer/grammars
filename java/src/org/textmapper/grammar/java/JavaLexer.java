package org.textmapper.grammar.java;

import java.io.IOException;
import java.io.Reader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class JavaLexer {

	public static class LapgSymbol {
		public Object sym;
		public int lexem;
		public int state;
		public int line;
		public int offset;
		public int endoffset;
	}

	public interface Lexems {
		public static final int eoi = 0;
		public static final int Identifier = 1;
		public static final int WhiteSpace = 2;
		public static final int EndOfLineComment = 3;
		public static final int kw_abstract = 4;
		public static final int kw_assert = 5;
		public static final int kw_boolean = 6;
		public static final int kw_break = 7;
		public static final int kw_byte = 8;
		public static final int kw_case = 9;
		public static final int kw_catch = 10;
		public static final int kw_char = 11;
		public static final int kw_class = 12;
		public static final int kw_const = 13;
		public static final int kw_continue = 14;
		public static final int kw_default = 15;
		public static final int kw_do = 16;
		public static final int kw_double = 17;
		public static final int kw_else = 18;
		public static final int kw_enum = 19;
		public static final int kw_extends = 20;
		public static final int kw_final = 21;
		public static final int kw_finally = 22;
		public static final int kw_float = 23;
		public static final int kw_for = 24;
		public static final int kw_goto = 25;
		public static final int kw_if = 26;
		public static final int kw_implements = 27;
		public static final int kw_import = 28;
		public static final int kw_instanceof = 29;
		public static final int kw_int = 30;
		public static final int kw_interface = 31;
		public static final int kw_long = 32;
		public static final int kw_native = 33;
		public static final int kw_new = 34;
		public static final int kw_package = 35;
		public static final int kw_private = 36;
		public static final int kw_protected = 37;
		public static final int kw_public = 38;
		public static final int kw_return = 39;
		public static final int kw_short = 40;
		public static final int kw_static = 41;
		public static final int kw_strictfp = 42;
		public static final int kw_super = 43;
		public static final int kw_switch = 44;
		public static final int kw_synchronized = 45;
		public static final int kw_this = 46;
		public static final int kw_throw = 47;
		public static final int kw_throws = 48;
		public static final int kw_transient = 49;
		public static final int kw_try = 50;
		public static final int kw_void = 51;
		public static final int kw_volatile = 52;
		public static final int kw_while = 53;
		public static final int IntegerLiteral = 54;
		public static final int FloatingPointLiteral = 55;
		public static final int BooleanLiteral = 56;
		public static final int CharacterLiteral = 57;
		public static final int StringLiteral = 58;
		public static final int NullLiteral = 59;
		public static final int LPAREN = 60;
		public static final int RPAREN = 61;
		public static final int LCURLY = 62;
		public static final int RCURLY = 63;
		public static final int LSQUARE = 64;
		public static final int RSQUARE = 65;
		public static final int SEMICOLON = 66;
		public static final int COMMA = 67;
		public static final int DOT = 68;
		public static final int EQUAL = 69;
		public static final int GREATER = 70;
		public static final int LESS = 71;
		public static final int EXCLAMATION = 72;
		public static final int TILDE = 73;
		public static final int QUESTIONMARK = 74;
		public static final int COLON = 75;
		public static final int EQUALEQUAL = 76;
		public static final int LESSEQUAL = 77;
		public static final int GREATEREQUAL = 78;
		public static final int EXCLAMATIONEQUAL = 79;
		public static final int AMPERSANDAMPERSAND = 80;
		public static final int OROR = 81;
		public static final int PLUSPLUS = 82;
		public static final int MINUSMINUS = 83;
		public static final int PLUS = 84;
		public static final int MINUS = 85;
		public static final int MULT = 86;
		public static final int SLASH = 87;
		public static final int AMPERSAND = 88;
		public static final int OR = 89;
		public static final int XOR = 90;
		public static final int PERCENT = 91;
		public static final int LESSLESS = 92;
		public static final int GREATERGREATER = 93;
		public static final int GREATERGREATERGREATER = 94;
		public static final int PLUSEQUAL = 95;
		public static final int MINUSEQUAL = 96;
		public static final int MULTEQUAL = 97;
		public static final int SLASHEQUAL = 98;
		public static final int AMPERSANDEQUAL = 99;
		public static final int OREQUAL = 100;
		public static final int XOREQUAL = 101;
		public static final int PERCENTEQUAL = 102;
		public static final int LESSLESSEQUAL = 103;
		public static final int GREATERGREATEREQUAL = 104;
		public static final int GREATERGREATERGREATEREQUAL = 105;
	}

	public interface ErrorReporter {
		void error(int start, int end, int line, String s);
	}

	public static final int TOKEN_SIZE = 2048;

	private Reader stream;
	final private ErrorReporter reporter;

	final private char[] data = new char[2048];
	private int datalen, l, tokenStart;
	private char chr;

	private int group;

	final private StringBuilder token = new StringBuilder(TOKEN_SIZE);

	private int tokenLine = 1;
	private int currLine = 1;
	private int currOffset = 0;

	public JavaLexer(Reader stream, ErrorReporter reporter) throws IOException {
		this.reporter = reporter;
		reset(stream);
	}

	public void reset(Reader stream) throws IOException {
		this.stream = stream;
		this.group = 0;
		datalen = stream.read(data);
		l = 0;
		tokenStart = -1;
		chr = l < datalen ? data[l++] : 0;
	}

	protected void advance() throws IOException {
		if (chr == 0) return;
		currOffset++;
		if (chr == '\n') {
			currLine++;
		}
		if (l >= datalen) {
			if (tokenStart >= 0) {
				token.append(data, tokenStart, l - tokenStart);
				tokenStart = 0;
			}
			l = 0;
			datalen = stream.read(data);
		}
		chr = l < datalen ? data[l++] : 0;
	}

	public int getState() {
		return group;
	}

	public void setState(int state) {
		this.group = state;
	}

	public int getTokenLine() {
		return tokenLine;
	}

	public int getLine() {
		return currLine;
	}

	public void setLine(int currLine) {
		this.currLine = currLine;
	}

	public int getOffset() {
		return currOffset;
	}

	public void setOffset(int currOffset) {
		this.currOffset = currOffset;
	}

	public String current() {
		return token.toString();
	}

	private static final char[] lapg_char2no = unpack_vc_char(262144,
		"\1\0\10\1\1\47\1\6\1\1\1\47\1\5\14\1\1\4\5\1\1\47\1\30\1\14\1\1\1\43\1\42\1\34\1" +
		"\13\1\15\1\16\1\40\1\36\1\24\1\37\1\12\1\7\1\10\1\54\2\63\4\52\2\44\1\33\1\23\1\27" +
		"\1\25\1\26\1\32\1\1\1\45\1\53\1\45\1\56\1\55\1\56\5\43\1\50\3\43\1\57\7\43\1\51\2" +
		"\43\1\21\1\2\1\22\1\41\1\11\1\1\1\45\1\61\1\45\1\56\1\55\1\62\5\43\1\50\1\43\1\60" +
		"\1\43\1\57\1\43\1\60\1\43\1\60\1\3\2\43\1\51\2\43\1\17\1\35\1\20\1\31\53\1\1\43\12" +
		"\1\1\43\4\1\1\43\5\1\27\43\1\1\37\43\1\1\u01ca\43\4\1\14\43\16\1\5\43\7\1\1\43\1" +
		"\1\1\43\201\1\5\43\1\1\2\43\2\1\4\43\10\1\1\43\1\1\3\43\1\1\1\43\1\1\24\43\1\1\123" +
		"\43\1\1\213\43\10\1\236\43\11\1\46\43\2\1\1\43\7\1\47\43\110\1\33\43\5\1\3\43\55" +
		"\1\53\43\25\1\12\46\4\1\2\43\1\1\143\43\1\1\1\43\17\1\2\43\7\1\2\43\12\46\3\43\2" +
		"\1\1\43\20\1\1\43\1\1\36\43\35\1\131\43\13\1\1\43\16\1\12\46\41\43\11\1\2\43\4\1" +
		"\1\43\5\1\26\43\4\1\1\43\11\1\1\43\3\1\1\43\27\1\31\43\107\1\1\43\1\1\13\43\127\1" +
		"\66\43\3\1\1\43\22\1\1\43\7\1\12\43\4\1\12\46\1\1\7\43\1\1\7\43\5\1\10\43\2\1\2\43" +
		"\2\1\26\43\1\1\7\43\1\1\1\43\3\1\4\43\3\1\1\43\20\1\1\43\15\1\2\43\1\1\3\43\4\1\12" +
		"\46\2\43\23\1\6\43\4\1\2\43\2\1\26\43\1\1\7\43\1\1\2\43\1\1\2\43\1\1\2\43\37\1\4" +
		"\43\1\1\1\43\7\1\12\46\2\1\3\43\20\1\11\43\1\1\3\43\1\1\26\43\1\1\7\43\1\1\2\43\1" +
		"\1\5\43\3\1\1\43\22\1\1\43\17\1\2\43\4\1\12\46\25\1\10\43\2\1\2\43\2\1\26\43\1\1" +
		"\7\43\1\1\2\43\1\1\5\43\3\1\1\43\36\1\2\43\1\1\3\43\4\1\12\46\1\1\1\43\21\1\1\43" +
		"\1\1\6\43\3\1\3\43\1\1\4\43\3\1\2\43\1\1\1\43\1\1\2\43\3\1\2\43\3\1\3\43\3\1\14\43" +
		"\26\1\1\43\25\1\12\46\25\1\10\43\1\1\3\43\1\1\27\43\1\1\12\43\1\1\5\43\3\1\1\43\32" +
		"\1\2\43\6\1\2\43\4\1\12\46\25\1\10\43\1\1\3\43\1\1\27\43\1\1\12\43\1\1\5\43\3\1\1" +
		"\43\40\1\1\43\1\1\2\43\4\1\12\46\1\1\2\43\22\1\10\43\1\1\3\43\1\1\51\43\2\1\1\43" +
		"\20\1\1\43\21\1\2\43\4\1\12\46\12\1\6\43\5\1\22\43\3\1\30\43\1\1\11\43\1\1\1\43\2" +
		"\1\7\43\72\1\60\43\1\1\2\43\14\1\7\43\11\1\12\46\47\1\2\43\1\1\1\43\2\1\2\43\1\1" +
		"\1\43\2\1\1\43\6\1\4\43\1\1\7\43\1\1\3\43\1\1\1\43\1\1\1\43\2\1\2\43\1\1\4\43\1\1" +
		"\2\43\11\1\1\43\2\1\5\43\1\1\1\43\11\1\12\46\2\1\4\43\40\1\1\43\37\1\12\46\26\1\10" +
		"\43\1\1\44\43\33\1\5\43\163\1\53\43\24\1\1\43\12\46\6\1\6\43\4\1\4\43\3\1\1\43\3" +
		"\1\2\43\7\1\3\43\4\1\15\43\14\1\1\43\1\1\12\46\6\1\46\43\1\1\1\43\5\1\1\43\2\1\53" +
		"\43\1\1\u014d\43\1\1\4\43\2\1\7\43\1\1\1\43\1\1\4\43\2\1\51\43\1\1\4\43\2\1\41\43" +
		"\1\1\4\43\2\1\7\43\1\1\1\43\1\1\4\43\2\1\17\43\1\1\71\43\1\1\4\43\2\1\103\43\45\1" +
		"\20\43\20\1\125\43\14\1\u026c\43\2\1\21\43\1\1\32\43\5\1\113\43\3\1\3\43\17\1\15" +
		"\43\1\1\4\43\16\1\22\43\16\1\22\43\16\1\15\43\1\1\3\43\17\1\64\43\43\1\1\43\4\1\1" +
		"\43\3\1\12\46\46\1\12\46\6\1\130\43\10\1\51\43\1\1\1\43\5\1\106\43\12\1\35\43\51" +
		"\1\12\46\36\43\2\1\5\43\13\1\54\43\25\1\7\43\10\1\12\46\46\1\27\43\11\1\65\43\53" +
		"\1\12\46\6\1\12\46\15\1\1\43\135\1\57\43\21\1\7\43\4\1\12\46\51\1\36\43\15\1\2\43" +
		"\12\46\54\43\32\1\44\43\34\1\12\46\3\1\3\43\12\46\44\43\153\1\4\43\1\1\4\43\3\1\2" +
		"\43\11\1\300\43\100\1\u0116\43\2\1\6\43\2\1\46\43\2\1\6\43\2\1\10\43\1\1\1\43\1\1" +
		"\1\43\1\1\1\43\1\1\37\43\2\1\65\43\1\1\7\43\1\1\1\43\3\1\3\43\1\1\7\43\3\1\4\43\2" +
		"\1\6\43\4\1\15\43\5\1\3\43\1\1\7\43\164\1\1\43\15\1\1\43\20\1\15\43\145\1\1\43\4" +
		"\1\1\43\2\1\12\43\1\1\1\43\3\1\5\43\6\1\1\43\1\1\1\43\1\1\1\43\1\1\4\43\1\1\13\43" +
		"\2\1\4\43\5\1\5\43\4\1\1\43\21\1\51\43\u0a77\1\57\43\1\1\57\43\1\1\205\43\6\1\4\43" +
		"\3\1\2\43\14\1\46\43\1\1\1\43\5\1\1\43\2\1\70\43\7\1\1\43\20\1\27\43\11\1\7\43\1" +
		"\1\7\43\1\1\7\43\1\1\7\43\1\1\7\43\1\1\7\43\1\1\7\43\1\1\7\43\120\1\1\43\u01d5\1" +
		"\3\43\31\1\11\43\7\1\5\43\2\1\5\43\4\1\126\43\6\1\3\43\1\1\132\43\1\1\4\43\5\1\51" +
		"\43\3\1\136\43\21\1\33\43\65\1\20\43\u0200\1\u19b6\43\112\1\u51cd\43\63\1\u048d\43" +
		"\103\1\56\43\2\1\u010d\43\3\1\20\43\12\46\2\43\24\1\57\43\20\1\31\43\10\1\120\43" +
		"\47\1\11\43\2\1\147\43\2\1\4\43\1\1\4\43\14\1\13\43\115\1\12\43\1\1\3\43\1\1\4\43" +
		"\1\1\27\43\35\1\64\43\16\1\62\43\34\1\12\46\30\1\6\43\3\1\1\43\4\1\12\46\34\43\12" +
		"\1\27\43\31\1\35\43\7\1\57\43\34\1\1\43\12\46\46\1\51\43\27\1\3\43\1\1\10\43\4\1" +
		"\12\46\6\1\27\43\3\1\1\43\5\1\60\43\1\1\1\43\3\1\2\43\2\1\5\43\2\1\1\43\1\1\1\43" +
		"\30\1\3\43\2\1\13\43\7\1\3\43\14\1\6\43\2\1\6\43\2\1\6\43\11\1\7\43\1\1\7\43\221" +
		"\1\43\43\15\1\12\46\6\1\u2ba4\43\14\1\27\43\4\1\61\43\u2104\1\u016e\43\2\1\152\43" +
		"\46\1\7\43\14\1\5\43\5\1\1\43\1\1\12\43\1\1\15\43\1\1\5\43\1\1\1\43\1\1\2\43\1\1" +
		"\2\43\1\1\154\43\41\1\u016b\43\22\1\100\43\2\1\66\43\50\1\14\43\164\1\5\43\1\1\207" +
		"\43\23\1\12\46\7\1\32\43\6\1\32\43\13\1\131\43\3\1\6\43\2\1\6\43\2\1\6\43\2\1\3\43" +
		"\43\1\14\43\1\1\32\43\1\1\23\43\1\1\2\43\1\1\17\43\2\1\16\43\42\1\173\43\105\1\65" +
		"\43\u010b\1\35\43\3\1\61\43\57\1\37\43\21\1\33\43\65\1\36\43\2\1\44\43\4\1\10\43" +
		"\1\1\5\43\52\1\236\43\2\1\12\46\u0356\1\6\43\2\1\1\43\1\1\54\43\1\1\2\43\3\1\1\43" +
		"\2\1\27\43\252\1\26\43\12\1\32\43\106\1\70\43\6\1\2\43\100\1\1\43\17\1\4\43\1\1\3" +
		"\43\1\1\33\43\54\1\35\43\203\1\66\43\12\1\26\43\12\1\23\43\215\1\111\43\u03ba\1\65" +
		"\43\56\1\12\46\23\1\55\43\40\1\31\43\7\1\12\46\11\1\44\43\17\1\12\46\103\1\60\43" +
		"\16\1\4\43\13\1\12\46\u04a6\1\53\43\25\1\12\46\u0936\1\u036f\43\221\1\143\43\u0b9d" +
		"\1\u042f\43\u33d1\1\u0239\43\u04c7\1\105\43\13\1\1\43\102\1\15\43\u4060\1\2\43\u23fe" +
		"\1\125\43\1\1\107\43\1\1\2\43\2\1\1\43\2\1\2\43\2\1\4\43\1\1\14\43\1\1\1\43\1\1\7" +
		"\43\1\1\101\43\1\1\4\43\2\1\10\43\1\1\7\43\1\1\34\43\1\1\4\43\1\1\5\43\1\1\1\43\3" +
		"\1\7\43\1\1\u0154\43\2\1\31\43\1\1\31\43\1\1\37\43\1\1\31\43\1\1\37\43\1\1\31\43" +
		"\1\1\37\43\1\1\31\43\1\1\37\43\1\1\31\43\1\1\10\43\2\1\62\46\u1600\1\4\43\1\1\33" +
		"\43\1\1\2\43\1\1\1\43\2\1\1\43\1\1\12\43\1\1\4\43\1\1\1\43\1\1\1\43\6\1\1\43\4\1" +
		"\1\43\1\1\1\43\1\1\1\43\1\1\3\43\1\1\2\43\1\1\1\43\2\1\1\43\1\1\1\43\1\1\1\43\1\1" +
		"\1\43\1\1\1\43\1\1\2\43\1\1\1\43\2\1\4\43\1\1\7\43\1\1\4\43\1\1\4\43\1\1\1\43\1\1" +
		"\12\43\1\1\21\43\5\1\3\43\1\1\5\43\1\1\21\43\u1144\1\ua6d7\43\51\1\u1035\43\13\1" +
		"\336\43\u3fe2\1\u021e\43\uffff\1\u05e3\1");

	private static char[] unpack_vc_char(int size, String... st) {
		char[] res = new char[size];
		int t = 0;
		int count = 0;
		for (String s : st) {
			int slen = s.length();
			for (int i = 0; i < slen; ) {
				count = i > 0 || count == 0 ? s.charAt(i++) : count;
				if (i < slen) {
					char val = s.charAt(i++);
					while (count-- > 0) res[t++] = val;
				}
			}
		}
		assert res.length == t;
		return res;
	}

	private static final short[] lapg_lexemnum = unpack_short(113,
		"\1\0\2\3\4\5\6\7\10\11\12\13\14\15\16\17\20\21\22\23\24\25\26\27\30\31\32\33\34\35" +
		"\36\37\40\41\42\43\44\45\46\47\50\51\52\53\54\55\56\57\60\61\62\63\64\65\66\66\66" +
		"\66\67\67\67\67\70\70\71\72\73\74\75\76\77\100\101\102\103\104\105\106\107\110\111" +
		"\112\113\114\115\116\117\120\121\122\123\124\125\126\127\130\131\132\133\134\135" +
		"\136\137\140\141\142\143\144\145\146\147\150\151");

	private static final short[] lapg_lexem = unpack_vc_short(6552,
		"\1\ufffe\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\2\1\10\1\11\1\12\1\13\1\14\1\15\1" +
		"\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31\1\32\1\33\1\34\1\35\1" +
		"\36\1\37\1\40\1\2\1\41\1\2\1\uffff\1\5\2\2\1\41\1\2\1\41\6\2\1\41\3\uffff\1\42\60" +
		"\uffff\2\ufffd\1\43\1\2\4\ufffd\2\2\31\ufffd\4\2\1\ufffd\14\2\64\ufffc\6\ufffb\1" +
		"\5\141\ufffb\7\uff9f\1\44\15\uff9f\1\45\36\uff9f\10\uffc7\1\46\1\47\1\50\31\uffc7" +
		"\1\51\3\uffc7\1\52\1\53\1\46\1\54\1\46\1\55\1\56\2\uffc7\1\54\1\56\1\46\10\uffb2" +
		"\1\57\33\uffb2\1\57\5\uffb2\1\57\1\uffb2\1\57\6\uffb2\1\57\1\uffff\1\60\1\61\2\60" +
		"\2\uffff\4\60\1\uffff\50\60\1\uffff\1\12\1\62\2\12\2\uffff\5\12\1\63\47\12\64\uffba" +
		"\64\uffb9\64\uffb8\64\uffb7\64\uffb6\64\uffb5\64\uffb4\64\uffb3\25\uffb1\1\64\36" +
		"\uffb1\25\uffb0\1\65\1\66\35\uffb0\25\uffaf\1\67\1\uffaf\1\70\34\uffaf\25\uffae\1" +
		"\71\36\uffae\64\uffad\64\uffac\64\uffab\25\uff9e\1\72\6\uff9e\1\73\27\uff9e\25\uff9d" +
		"\1\74\7\uff9d\1\75\26\uff9d\25\uffa2\1\76\10\uffa2\1\77\25\uffa2\25\uffa1\1\100\11" +
		"\uffa1\1\101\24\uffa1\25\uffa0\1\102\36\uffa0\25\uff9c\1\103\36\uff9c\25\uff9b\1" +
		"\104\36\uff9b\10\uffc7\1\105\1\106\1\50\31\uffc7\1\105\3\uffc7\1\52\1\uffc7\1\105" +
		"\1\uffc7\1\105\1\55\1\56\3\uffc7\1\56\1\105\3\uffff\1\42\4\uffff\1\107\33\uffff\2" +
		"\107\4\uffff\5\107\2\uffff\3\107\3\uffff\1\110\60\uffff\1\ufffa\4\44\2\ufffa\55\44" +
		"\64\uff94\10\uffc5\1\46\1\111\1\50\31\uffc5\1\51\3\uffc5\1\112\1\uffc5\1\46\1\uffc5" +
		"\1\46\1\55\1\56\3\uffc5\1\56\1\46\10\uffff\1\46\1\47\32\uffff\1\51\5\uffff\1\46\1" +
		"\uffff\1\46\6\uffff\1\46\10\uffc3\1\113\33\uffc3\1\113\5\uffc3\1\113\1\uffc3\1\113" +
		"\1\114\1\115\3\uffc3\1\115\1\113\10\uffff\1\51\1\116\1\50\31\uffff\1\51\5\uffff\1" +
		"\51\1\uffff\1\51\1\55\1\56\3\uffff\1\56\1\51\64\uffc7\10\uffff\1\117\1\uffff\1\120" +
		"\31\uffff\2\117\4\uffff\5\117\2\uffff\3\117\10\uffff\1\121\43\uffff\1\121\17\uffff" +
		"\1\122\25\uffff\2\123\4\uffff\1\122\5\uffff\1\122\1\uffff\1\122\6\uffff\1\122\64" +
		"\uffc1\10\uffc3\1\57\1\124\32\uffc3\1\57\5\uffc3\1\57\1\uffc3\1\57\1\114\1\115\3" +
		"\uffc3\1\115\1\57\13\uffff\1\125\52\uffff\1\60\1\126\4\uffff\1\127\2\uffff\2\60\35" +
		"\uffff\1\130\1\uffff\1\127\3\uffff\3\60\1\127\2\uffff\1\12\1\131\4\uffff\1\132\2" +
		"\uffff\2\12\35\uffff\1\133\1\uffff\1\132\3\uffff\3\12\1\132\64\uffbc\64\uffaa\64" +
		"\uffa8\25\uff99\1\134\1\135\35\uff99\64\uffa9\25\uff9a\1\136\36\uff9a\64\uffa7\64" +
		"\uff93\64\uffa6\64\uff92\64\uffa5\64\uff97\64\uffa4\64\uff96\64\uffa3\64\uff95\64" +
		"\uff91\64\uff90\10\uffc7\1\105\1\137\1\50\31\uffc7\1\105\3\uffc7\1\52\1\uffc7\1\105" +
		"\1\uffc7\1\105\1\55\1\56\3\uffc7\1\56\1\105\10\uffff\1\105\1\106\32\uffff\1\105\5" +
		"\uffff\1\105\1\uffff\1\105\6\uffff\1\105\10\uffff\1\140\33\uffff\2\140\4\uffff\5" +
		"\140\2\uffff\3\140\3\uffff\1\110\4\uffff\1\141\33\uffff\2\141\4\uffff\5\141\2\uffff" +
		"\3\141\10\uffff\1\46\1\111\32\uffff\1\51\5\uffff\1\46\1\uffff\1\46\6\uffff\1\46\64" +
		"\uffc5\10\uffc3\1\113\1\142\32\uffc3\1\113\5\uffc3\1\113\1\uffc3\1\113\1\114\1\115" +
		"\3\uffc3\1\115\1\113\10\uffff\1\143\25\uffff\2\144\4\uffff\1\143\5\uffff\1\143\1" +
		"\uffff\1\143\6\uffff\1\143\64\uffc3\10\uffff\1\51\1\116\32\uffff\1\51\5\uffff\1\51" +
		"\1\uffff\1\51\6\uffff\1\51\10\uffc6\1\117\1\145\1\146\31\uffc6\2\117\2\uffc6\1\147" +
		"\1\uffc6\5\117\1\150\1\uffc6\3\117\10\uffff\1\151\33\uffff\2\151\4\uffff\5\151\2" +
		"\uffff\3\151\10\uffc4\1\121\1\152\36\uffc4\1\153\3\uffc4\1\121\7\uffc4\10\uffc2\1" +
		"\122\1\154\32\uffc2\1\122\5\uffc2\1\122\1\uffc2\1\122\1\uffc2\1\155\3\uffc2\1\155" +
		"\1\122\10\uffff\1\122\33\uffff\1\122\5\uffff\1\122\1\uffff\1\122\6\uffff\1\122\10" +
		"\uffff\1\57\1\124\32\uffff\1\57\5\uffff\1\57\1\uffff\1\57\6\uffff\1\57\64\uffbd\3" +
		"\uffff\1\126\4\uffff\1\156\33\uffff\2\156\4\uffff\5\156\2\uffff\3\156\10\uffff\1" +
		"\130\2\uffff\1\125\36\uffff\1\130\1\uffff\1\130\6\uffff\1\130\10\uffff\1\60\2\uffff" +
		"\1\125\36\uffff\1\60\1\uffff\1\60\6\uffff\1\60\3\uffff\1\131\4\uffff\1\157\33\uffff" +
		"\2\157\4\uffff\5\157\2\uffff\3\157\1\uffff\1\12\1\62\2\12\2\uffff\1\12\1\133\3\12" +
		"\1\63\35\12\1\133\1\12\1\133\6\12\1\133\1\uffff\1\12\1\62\2\12\2\uffff\5\12\1\63" +
		"\47\12\64\uff8e\25\uff98\1\160\36\uff98\64\uff8f\10\uffff\1\105\1\137\32\uffff\1" +
		"\105\5\uffff\1\105\1\uffff\1\105\6\uffff\1\105\10\uffff\1\161\33\uffff\2\161\4\uffff" +
		"\5\161\2\uffff\3\161\10\uffff\1\162\33\uffff\2\162\4\uffff\5\162\2\uffff\3\162\10" +
		"\uffff\1\113\1\142\32\uffff\1\113\5\uffff\1\113\1\uffff\1\113\6\uffff\1\113\10\uffc3" +
		"\1\143\1\163\32\uffc3\1\143\5\uffc3\1\143\1\uffc3\1\143\1\uffc3\1\115\3\uffc3\1\115" +
		"\1\143\10\uffff\1\143\33\uffff\1\143\5\uffff\1\143\1\uffff\1\143\6\uffff\1\143\10" +
		"\uffff\1\117\1\145\32\uffff\2\117\4\uffff\5\117\2\uffff\3\117\10\uffff\1\151\33\uffff" +
		"\2\151\4\uffff\5\151\1\150\1\uffff\3\151\64\uffc6\10\uffff\1\164\25\uffff\2\165\4" +
		"\uffff\1\164\5\uffff\1\164\1\uffff\1\164\6\uffff\1\164\10\uffff\1\151\1\166\32\uffff" +
		"\2\151\4\uffff\5\151\1\150\1\uffff\3\151\10\uffff\1\121\1\152\42\uffff\1\121\7\uffff" +
		"\64\uffc4\10\uffff\1\122\1\154\32\uffff\1\122\5\uffff\1\122\1\uffff\1\122\6\uffff" +
		"\1\122\64\uffc2\10\uffff\1\167\33\uffff\2\167\4\uffff\5\167\2\uffff\3\167\10\uffff" +
		"\1\170\33\uffff\2\170\4\uffff\5\170\2\uffff\3\170\64\uff8d\10\uffff\1\2\33\uffff" +
		"\2\2\4\uffff\5\2\2\uffff\3\2\10\uffff\1\171\33\uffff\2\171\4\uffff\5\171\2\uffff" +
		"\3\171\10\uffff\1\143\1\163\32\uffff\1\143\5\uffff\1\143\1\uffff\1\143\6\uffff\1" +
		"\143\10\uffc0\1\164\1\172\32\uffc0\1\164\5\uffc0\1\164\1\uffc0\1\164\1\uffc0\1\173" +
		"\3\uffc0\1\173\1\164\10\uffff\1\164\33\uffff\1\164\5\uffff\1\164\1\uffff\1\164\6" +
		"\uffff\1\164\10\uffff\1\151\1\166\32\uffff\2\151\4\uffff\5\151\2\uffff\3\151\10\uffff" +
		"\1\174\33\uffff\2\174\4\uffff\5\174\2\uffff\3\174\10\uffff\1\175\33\uffff\2\175\4" +
		"\uffff\5\175\2\uffff\3\175\10\uffff\1\2\33\uffff\2\2\4\uffff\5\2\2\uffff\3\2\10\uffff" +
		"\1\164\1\172\32\uffff\1\164\5\uffff\1\164\1\uffff\1\164\6\uffff\1\164\64\uffc0\10" +
		"\uffff\1\60\33\uffff\2\60\4\uffff\5\60\2\uffff\3\60\10\uffff\1\12\33\uffff\2\12\4" +
		"\uffff\5\12\2\uffff\3\12");

	private static short[] unpack_vc_short(int size, String... st) {
		short[] res = new short[size];
		int t = 0;
		int count = 0;
		for (String s : st) {
			int slen = s.length();
			for (int i = 0; i < slen; ) {
				count = i > 0 || count == 0 ? s.charAt(i++) : count;
				if (i < slen) {
					short val = (short) s.charAt(i++);
					while (count-- > 0) res[t++] = val;
				}
			}
		}
		assert res.length == t;
		return res;
	}

	private static int mapCharacter(int chr) {
		if (chr >= 0 && chr < 262144) {
			return lapg_char2no[chr];
		}
		return 1;
	}

	public LapgSymbol next() throws IOException {
		LapgSymbol lapg_n = new LapgSymbol();
		int state;

		do {
			lapg_n.offset = currOffset;
			tokenLine = lapg_n.line = currLine;
			if (token.length() > TOKEN_SIZE) {
				token.setLength(TOKEN_SIZE);
				token.trimToSize();
			}
			token.setLength(0);
			tokenStart = l - 1;

			for (state = group; state >= 0; ) {
				state = lapg_lexem[state * 52 + mapCharacter(chr)];
				if (state == -1 && chr == 0) {
					lapg_n.endoffset = currOffset;
					lapg_n.lexem = 0;
					lapg_n.sym = null;
					reporter.error(lapg_n.offset, lapg_n.endoffset, lapg_n.line, "Unexpected end of input reached");
					tokenStart = -1;
					return lapg_n;
				}
				if (state >= -1 && chr != 0) {
					currOffset++;
					if (chr == '\n') {
						currLine++;
					}
					if (l >= datalen) {
						token.append(data, tokenStart, l - tokenStart);
						tokenStart = l = 0;
						datalen = stream.read(data);
					}
					chr = l < datalen ? data[l++] : 0;
				}
			}
			lapg_n.endoffset = currOffset;

			if (state == -1) {
				if (l - 1 > tokenStart) {
					token.append(data, tokenStart, l - 1 - tokenStart);
				}
				reporter.error(lapg_n.offset, lapg_n.endoffset, lapg_n.line, MessageFormat.format("invalid lexem at line {0}: `{1}`, skipped", currLine, current()));
				lapg_n.lexem = -1;
				continue;
			}

			if (state == -2) {
				lapg_n.lexem = 0;
				lapg_n.sym = null;
				tokenStart = -1;
				return lapg_n;
			}

			if (l - 1 > tokenStart) {
				token.append(data, tokenStart, l - 1 - tokenStart);
			}

			lapg_n.lexem = lapg_lexemnum[-state - 3];
			lapg_n.sym = null;

		} while (lapg_n.lexem == -1 || !createToken(lapg_n, -state - 3));
		tokenStart = -1;
		return lapg_n;
	}

	protected boolean createToken(LapgSymbol lapg_n, int lexemIndex) throws IOException {
		switch (lexemIndex) {
			case 0:
				return createIdentifierToken(lapg_n, lexemIndex);
			case 2:
				return false;
			case 3:
				return false;
		}
		return true;
	}

	private static Map<String,Integer> subTokensOfIdentifier = new HashMap<String,Integer>();
	static {
		subTokensOfIdentifier.put("abstract", 4);
		subTokensOfIdentifier.put("assert", 5);
		subTokensOfIdentifier.put("boolean", 6);
		subTokensOfIdentifier.put("break", 7);
		subTokensOfIdentifier.put("byte", 8);
		subTokensOfIdentifier.put("case", 9);
		subTokensOfIdentifier.put("catch", 10);
		subTokensOfIdentifier.put("char", 11);
		subTokensOfIdentifier.put("class", 12);
		subTokensOfIdentifier.put("const", 13);
		subTokensOfIdentifier.put("continue", 14);
		subTokensOfIdentifier.put("default", 15);
		subTokensOfIdentifier.put("do", 16);
		subTokensOfIdentifier.put("double", 17);
		subTokensOfIdentifier.put("else", 18);
		subTokensOfIdentifier.put("enum", 19);
		subTokensOfIdentifier.put("extends", 20);
		subTokensOfIdentifier.put("final", 21);
		subTokensOfIdentifier.put("finally", 22);
		subTokensOfIdentifier.put("float", 23);
		subTokensOfIdentifier.put("for", 24);
		subTokensOfIdentifier.put("goto", 25);
		subTokensOfIdentifier.put("if", 26);
		subTokensOfIdentifier.put("implements", 27);
		subTokensOfIdentifier.put("import", 28);
		subTokensOfIdentifier.put("instanceof", 29);
		subTokensOfIdentifier.put("int", 30);
		subTokensOfIdentifier.put("interface", 31);
		subTokensOfIdentifier.put("long", 32);
		subTokensOfIdentifier.put("native", 33);
		subTokensOfIdentifier.put("new", 34);
		subTokensOfIdentifier.put("package", 35);
		subTokensOfIdentifier.put("private", 36);
		subTokensOfIdentifier.put("protected", 37);
		subTokensOfIdentifier.put("public", 38);
		subTokensOfIdentifier.put("return", 39);
		subTokensOfIdentifier.put("short", 40);
		subTokensOfIdentifier.put("static", 41);
		subTokensOfIdentifier.put("strictfp", 42);
		subTokensOfIdentifier.put("super", 43);
		subTokensOfIdentifier.put("switch", 44);
		subTokensOfIdentifier.put("synchronized", 45);
		subTokensOfIdentifier.put("this", 46);
		subTokensOfIdentifier.put("throw", 47);
		subTokensOfIdentifier.put("throws", 48);
		subTokensOfIdentifier.put("transient", 49);
		subTokensOfIdentifier.put("try", 50);
		subTokensOfIdentifier.put("void", 51);
		subTokensOfIdentifier.put("volatile", 52);
		subTokensOfIdentifier.put("while", 53);
		subTokensOfIdentifier.put("true", 62);
		subTokensOfIdentifier.put("false", 63);
		subTokensOfIdentifier.put("null", 66);
	}

	protected boolean createIdentifierToken(LapgSymbol lapg_n, int lexemIndex) {
		Integer replacement = subTokensOfIdentifier.get(current());
		if (replacement != null) {
			lexemIndex = replacement;
			lapg_n.lexem = lapg_lexemnum[lexemIndex];
		}
		return true;
	}

	/* package */ static int[] unpack_int(int size, String... st) {
		int[] res = new int[size];
		boolean second = false;
		char first = 0;
		int t = 0;
		for (String s : st) {
			int slen = s.length();
			for (int i = 0; i < slen; i++) {
				if (second) {
					res[t++] = (s.charAt(i) << 16) + first;
				} else {
					first = s.charAt(i);
				}
				second = !second;
			}
		}
		assert !second;
		assert res.length == t;
		return res;
	}

	/* package */ static short[] unpack_short(int size, String... st) {
		short[] res = new short[size];
		int t = 0;
		for (String s : st) {
			int slen = s.length();
			for (int i = 0; i < slen; i++) {
				res[t++] = (short) s.charAt(i);
			}
		}
		assert res.length == t;
		return res;
	}
}
