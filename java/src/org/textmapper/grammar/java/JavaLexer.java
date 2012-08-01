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
		"\1\0\10\1\1\44\1\4\1\1\1\44\1\3\14\1\1\2\5\1\1\44\1\27\1\13\1\1\1\42\1\41\1\33\1" +
		"\11\1\14\1\15\1\37\1\35\1\23\1\36\1\10\1\5\1\6\1\53\2\62\4\51\2\45\1\32\1\22\1\26" +
		"\1\24\1\25\1\31\1\1\1\50\1\52\1\50\1\55\1\54\1\55\5\42\1\46\3\42\1\56\7\42\1\47\2" +
		"\42\1\20\1\12\1\21\1\40\1\7\1\1\1\50\1\60\1\50\1\55\1\54\1\61\5\42\1\46\1\42\1\57" +
		"\1\42\1\56\1\42\1\57\1\42\1\57\3\42\1\47\2\42\1\16\1\34\1\17\1\30\53\1\1\42\12\1" +
		"\1\42\4\1\1\42\5\1\27\42\1\1\37\42\1\1\u01ca\42\4\1\14\42\16\1\5\42\7\1\1\42\1\1" +
		"\1\42\201\1\5\42\1\1\2\42\2\1\4\42\10\1\1\42\1\1\3\42\1\1\1\42\1\1\24\42\1\1\123" +
		"\42\1\1\213\42\10\1\236\42\11\1\46\42\2\1\1\42\7\1\47\42\110\1\33\42\5\1\3\42\55" +
		"\1\53\42\25\1\12\43\4\1\2\42\1\1\143\42\1\1\1\42\17\1\2\42\7\1\2\42\12\43\3\42\2" +
		"\1\1\42\20\1\1\42\1\1\36\42\35\1\131\42\13\1\1\42\16\1\12\43\41\42\11\1\2\42\4\1" +
		"\1\42\5\1\26\42\4\1\1\42\11\1\1\42\3\1\1\42\27\1\31\42\107\1\1\42\1\1\13\42\127\1" +
		"\66\42\3\1\1\42\22\1\1\42\7\1\12\42\4\1\12\43\1\1\7\42\1\1\7\42\5\1\10\42\2\1\2\42" +
		"\2\1\26\42\1\1\7\42\1\1\1\42\3\1\4\42\3\1\1\42\20\1\1\42\15\1\2\42\1\1\3\42\4\1\12" +
		"\43\2\42\23\1\6\42\4\1\2\42\2\1\26\42\1\1\7\42\1\1\2\42\1\1\2\42\1\1\2\42\37\1\4" +
		"\42\1\1\1\42\7\1\12\43\2\1\3\42\20\1\11\42\1\1\3\42\1\1\26\42\1\1\7\42\1\1\2\42\1" +
		"\1\5\42\3\1\1\42\22\1\1\42\17\1\2\42\4\1\12\43\25\1\10\42\2\1\2\42\2\1\26\42\1\1" +
		"\7\42\1\1\2\42\1\1\5\42\3\1\1\42\36\1\2\42\1\1\3\42\4\1\12\43\1\1\1\42\21\1\1\42" +
		"\1\1\6\42\3\1\3\42\1\1\4\42\3\1\2\42\1\1\1\42\1\1\2\42\3\1\2\42\3\1\3\42\3\1\14\42" +
		"\26\1\1\42\25\1\12\43\25\1\10\42\1\1\3\42\1\1\27\42\1\1\12\42\1\1\5\42\3\1\1\42\32" +
		"\1\2\42\6\1\2\42\4\1\12\43\25\1\10\42\1\1\3\42\1\1\27\42\1\1\12\42\1\1\5\42\3\1\1" +
		"\42\40\1\1\42\1\1\2\42\4\1\12\43\1\1\2\42\22\1\10\42\1\1\3\42\1\1\51\42\2\1\1\42" +
		"\20\1\1\42\21\1\2\42\4\1\12\43\12\1\6\42\5\1\22\42\3\1\30\42\1\1\11\42\1\1\1\42\2" +
		"\1\7\42\72\1\60\42\1\1\2\42\14\1\7\42\11\1\12\43\47\1\2\42\1\1\1\42\2\1\2\42\1\1" +
		"\1\42\2\1\1\42\6\1\4\42\1\1\7\42\1\1\3\42\1\1\1\42\1\1\1\42\2\1\2\42\1\1\4\42\1\1" +
		"\2\42\11\1\1\42\2\1\5\42\1\1\1\42\11\1\12\43\2\1\4\42\40\1\1\42\37\1\12\43\26\1\10" +
		"\42\1\1\44\42\33\1\5\42\163\1\53\42\24\1\1\42\12\43\6\1\6\42\4\1\4\42\3\1\1\42\3" +
		"\1\2\42\7\1\3\42\4\1\15\42\14\1\1\42\1\1\12\43\6\1\46\42\1\1\1\42\5\1\1\42\2\1\53" +
		"\42\1\1\u014d\42\1\1\4\42\2\1\7\42\1\1\1\42\1\1\4\42\2\1\51\42\1\1\4\42\2\1\41\42" +
		"\1\1\4\42\2\1\7\42\1\1\1\42\1\1\4\42\2\1\17\42\1\1\71\42\1\1\4\42\2\1\103\42\45\1" +
		"\20\42\20\1\125\42\14\1\u026c\42\2\1\21\42\1\1\32\42\5\1\113\42\3\1\3\42\17\1\15" +
		"\42\1\1\4\42\16\1\22\42\16\1\22\42\16\1\15\42\1\1\3\42\17\1\64\42\43\1\1\42\4\1\1" +
		"\42\3\1\12\43\46\1\12\43\6\1\130\42\10\1\51\42\1\1\1\42\5\1\106\42\12\1\35\42\51" +
		"\1\12\43\36\42\2\1\5\42\13\1\54\42\25\1\7\42\10\1\12\43\46\1\27\42\11\1\65\42\53" +
		"\1\12\43\6\1\12\43\15\1\1\42\135\1\57\42\21\1\7\42\4\1\12\43\51\1\36\42\15\1\2\42" +
		"\12\43\54\42\32\1\44\42\34\1\12\43\3\1\3\42\12\43\44\42\153\1\4\42\1\1\4\42\3\1\2" +
		"\42\11\1\300\42\100\1\u0116\42\2\1\6\42\2\1\46\42\2\1\6\42\2\1\10\42\1\1\1\42\1\1" +
		"\1\42\1\1\1\42\1\1\37\42\2\1\65\42\1\1\7\42\1\1\1\42\3\1\3\42\1\1\7\42\3\1\4\42\2" +
		"\1\6\42\4\1\15\42\5\1\3\42\1\1\7\42\164\1\1\42\15\1\1\42\20\1\15\42\145\1\1\42\4" +
		"\1\1\42\2\1\12\42\1\1\1\42\3\1\5\42\6\1\1\42\1\1\1\42\1\1\1\42\1\1\4\42\1\1\13\42" +
		"\2\1\4\42\5\1\5\42\4\1\1\42\21\1\51\42\u0a77\1\57\42\1\1\57\42\1\1\205\42\6\1\4\42" +
		"\3\1\2\42\14\1\46\42\1\1\1\42\5\1\1\42\2\1\70\42\7\1\1\42\20\1\27\42\11\1\7\42\1" +
		"\1\7\42\1\1\7\42\1\1\7\42\1\1\7\42\1\1\7\42\1\1\7\42\1\1\7\42\120\1\1\42\u01d5\1" +
		"\3\42\31\1\11\42\7\1\5\42\2\1\5\42\4\1\126\42\6\1\3\42\1\1\132\42\1\1\4\42\5\1\51" +
		"\42\3\1\136\42\21\1\33\42\65\1\20\42\u0200\1\u19b6\42\112\1\u51cd\42\63\1\u048d\42" +
		"\103\1\56\42\2\1\u010d\42\3\1\20\42\12\43\2\42\24\1\57\42\20\1\31\42\10\1\120\42" +
		"\47\1\11\42\2\1\147\42\2\1\4\42\1\1\4\42\14\1\13\42\115\1\12\42\1\1\3\42\1\1\4\42" +
		"\1\1\27\42\35\1\64\42\16\1\62\42\34\1\12\43\30\1\6\42\3\1\1\42\4\1\12\43\34\42\12" +
		"\1\27\42\31\1\35\42\7\1\57\42\34\1\1\42\12\43\46\1\51\42\27\1\3\42\1\1\10\42\4\1" +
		"\12\43\6\1\27\42\3\1\1\42\5\1\60\42\1\1\1\42\3\1\2\42\2\1\5\42\2\1\1\42\1\1\1\42" +
		"\30\1\3\42\2\1\13\42\7\1\3\42\14\1\6\42\2\1\6\42\2\1\6\42\11\1\7\42\1\1\7\42\221" +
		"\1\43\42\15\1\12\43\6\1\u2ba4\42\14\1\27\42\4\1\61\42\u2104\1\u016e\42\2\1\152\42" +
		"\46\1\7\42\14\1\5\42\5\1\1\42\1\1\12\42\1\1\15\42\1\1\5\42\1\1\1\42\1\1\2\42\1\1" +
		"\2\42\1\1\154\42\41\1\u016b\42\22\1\100\42\2\1\66\42\50\1\14\42\164\1\5\42\1\1\207" +
		"\42\23\1\12\43\7\1\32\42\6\1\32\42\13\1\131\42\3\1\6\42\2\1\6\42\2\1\6\42\2\1\3\42" +
		"\43\1\14\42\1\1\32\42\1\1\23\42\1\1\2\42\1\1\17\42\2\1\16\42\42\1\173\42\105\1\65" +
		"\42\u010b\1\35\42\3\1\61\42\57\1\37\42\21\1\33\42\65\1\36\42\2\1\44\42\4\1\10\42" +
		"\1\1\5\42\52\1\236\42\2\1\12\43\u0356\1\6\42\2\1\1\42\1\1\54\42\1\1\2\42\3\1\1\42" +
		"\2\1\27\42\252\1\26\42\12\1\32\42\106\1\70\42\6\1\2\42\100\1\1\42\17\1\4\42\1\1\3" +
		"\42\1\1\33\42\54\1\35\42\203\1\66\42\12\1\26\42\12\1\23\42\215\1\111\42\u03ba\1\65" +
		"\42\56\1\12\43\23\1\55\42\40\1\31\42\7\1\12\43\11\1\44\42\17\1\12\43\103\1\60\42" +
		"\16\1\4\42\13\1\12\43\u04a6\1\53\42\25\1\12\43\u0936\1\u036f\42\221\1\143\42\u0b9d" +
		"\1\u042f\42\u33d1\1\u0239\42\u04c7\1\105\42\13\1\1\42\102\1\15\42\u4060\1\2\42\u23fe" +
		"\1\125\42\1\1\107\42\1\1\2\42\2\1\1\42\2\1\2\42\2\1\4\42\1\1\14\42\1\1\1\42\1\1\7" +
		"\42\1\1\101\42\1\1\4\42\2\1\10\42\1\1\7\42\1\1\34\42\1\1\4\42\1\1\5\42\1\1\1\42\3" +
		"\1\7\42\1\1\u0154\42\2\1\31\42\1\1\31\42\1\1\37\42\1\1\31\42\1\1\37\42\1\1\31\42" +
		"\1\1\37\42\1\1\31\42\1\1\37\42\1\1\31\42\1\1\10\42\2\1\62\43\u1600\1\4\42\1\1\33" +
		"\42\1\1\2\42\1\1\1\42\2\1\1\42\1\1\12\42\1\1\4\42\1\1\1\42\1\1\1\42\6\1\1\42\4\1" +
		"\1\42\1\1\1\42\1\1\1\42\1\1\3\42\1\1\2\42\1\1\1\42\2\1\1\42\1\1\1\42\1\1\1\42\1\1" +
		"\1\42\1\1\1\42\1\1\2\42\1\1\1\42\2\1\4\42\1\1\7\42\1\1\4\42\1\1\4\42\1\1\1\42\1\1" +
		"\12\42\1\1\21\42\5\1\3\42\1\1\5\42\1\1\21\42\u1144\1\ua6d7\42\51\1\u1035\42\13\1" +
		"\336\42\u3fe2\1\u021e\42\uffff\1\u05e3\1");

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

	private static final short[] lapg_lexem = unpack_vc_short(6120,
		"\1\ufffe\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\uffff\1\11\1\12\1\13\1\14\1\15" +
		"\1\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31\1\32\1\33\1\34\1\35" +
		"\1\36\1\37\1\6\1\uffff\1\3\1\40\3\6\1\40\1\6\1\40\6\6\1\40\63\ufffc\4\ufffb\1\3\141" +
		"\ufffb\5\uff9f\1\41\16\uff9f\1\42\36\uff9f\6\uffc7\1\43\1\44\1\45\34\uffc7\1\46\1" +
		"\47\1\50\1\uffc7\1\43\1\51\1\43\1\52\1\53\2\uffc7\1\51\1\53\1\43\6\ufffd\2\6\32\ufffd" +
		"\2\6\1\ufffd\16\6\6\uffb2\1\54\36\uffb2\1\54\3\uffb2\1\54\1\uffb2\1\54\6\uffb2\1" +
		"\54\1\uffff\2\55\2\uffff\4\55\1\uffff\1\56\50\55\1\uffff\2\11\2\uffff\5\11\1\57\1" +
		"\60\47\11\63\uffba\63\uffb9\63\uffb8\63\uffb7\63\uffb6\63\uffb5\63\uffb4\63\uffb3" +
		"\24\uffb1\1\61\36\uffb1\24\uffb0\1\62\1\63\35\uffb0\24\uffaf\1\64\1\uffaf\1\65\34" +
		"\uffaf\24\uffae\1\66\36\uffae\63\uffad\63\uffac\63\uffab\24\uff9e\1\67\6\uff9e\1" +
		"\70\27\uff9e\24\uff9d\1\71\7\uff9d\1\72\26\uff9d\24\uffa2\1\73\10\uffa2\1\74\25\uffa2" +
		"\24\uffa1\1\75\11\uffa1\1\76\24\uffa1\24\uffa0\1\77\36\uffa0\24\uff9c\1\100\36\uff9c" +
		"\24\uff9b\1\101\36\uff9b\6\uffc7\1\102\1\103\1\45\34\uffc7\1\102\1\47\2\uffc7\1\102" +
		"\1\uffc7\1\102\1\52\1\53\3\uffc7\1\53\1\102\1\ufffa\2\41\2\ufffa\56\41\63\uff94\6" +
		"\uffc5\1\104\1\105\35\uffc5\1\106\1\107\2\uffc5\1\104\1\uffc5\1\104\6\uffc5\1\104" +
		"\6\uffff\1\110\1\44\35\uffff\1\106\3\uffff\1\110\1\uffff\1\110\6\uffff\1\110\6\uffff" +
		"\1\111\36\uffff\1\111\3\uffff\1\111\1\uffff\1\111\6\uffff\1\111\6\uffff\1\106\1\46" +
		"\35\uffff\1\106\3\uffff\1\106\1\uffff\1\106\6\uffff\1\106\63\uffc7\6\uffff\1\112" +
		"\36\uffff\1\112\2\uffff\6\112\2\uffff\3\112\6\uffff\1\113\44\uffff\1\113\15\uffff" +
		"\1\114\26\uffff\2\115\6\uffff\1\114\3\uffff\1\114\1\uffff\1\114\6\uffff\1\114\63" +
		"\uffc1\6\uffff\2\116\35\uffff\1\116\3\uffff\1\116\1\uffff\1\116\1\117\5\uffff\1\116" +
		"\11\uffff\1\120\57\uffff\1\121\2\uffff\3\55\35\uffff\1\122\1\uffff\1\121\3\uffff" +
		"\3\55\1\121\6\uffff\1\123\2\uffff\3\11\35\uffff\1\124\1\uffff\1\123\3\uffff\3\11" +
		"\1\123\63\uffbc\63\uffaa\63\uffa8\24\uff99\1\125\1\126\35\uff99\63\uffa9\24\uff9a" +
		"\1\127\36\uff9a\63\uffa7\63\uff93\63\uffa6\63\uff92\63\uffa5\63\uff97\63\uffa4\63" +
		"\uff96\63\uffa3\63\uff95\63\uff91\63\uff90\6\uffc7\1\130\1\131\35\uffc7\1\130\1\47" +
		"\2\uffc7\1\130\1\uffc7\1\130\6\uffc7\1\130\6\uffff\1\132\1\103\35\uffff\1\132\3\uffff" +
		"\1\132\1\uffff\1\132\6\uffff\1\132\6\uffff\1\133\1\105\1\45\34\uffff\1\106\3\uffff" +
		"\1\133\1\uffff\1\133\1\52\1\53\3\uffff\1\53\1\133\6\uffff\1\133\1\105\35\uffff\1" +
		"\106\3\uffff\1\133\1\uffff\1\133\6\uffff\1\133\6\uffff\1\106\1\46\1\45\34\uffff\1" +
		"\106\3\uffff\1\106\1\uffff\1\106\1\52\1\53\3\uffff\1\53\1\106\71\uffc5\1\104\1\105" +
		"\1\45\34\uffc5\1\106\1\107\2\uffc5\1\104\1\uffc5\1\104\1\52\1\53\3\uffc5\1\53\1\104" +
		"\6\uffff\2\134\35\uffff\1\134\3\uffff\1\134\1\uffff\1\134\1\117\5\uffff\1\134\6\uffc6" +
		"\2\135\1\136\34\uffc6\1\135\1\137\1\uffc6\6\135\1\140\1\uffc6\3\135\6\uffc4\1\113" +
		"\1\141\36\uffc4\1\142\4\uffc4\1\113\7\uffc4\6\uffc2\2\143\35\uffc2\1\143\3\uffc2" +
		"\1\143\1\uffc2\1\143\1\uffc2\1\144\3\uffc2\1\144\1\143\6\uffff\1\114\36\uffff\1\114" +
		"\3\uffff\1\114\1\uffff\1\114\6\uffff\1\114\6\uffff\1\145\1\116\35\uffff\1\145\3\uffff" +
		"\1\145\1\uffff\1\145\6\uffff\1\145\6\uffff\1\146\26\uffff\2\147\6\uffff\1\146\3\uffff" +
		"\1\146\1\uffff\1\146\6\uffff\1\146\63\uffbd\6\uffff\1\122\2\uffff\1\120\37\uffff" +
		"\1\122\1\uffff\1\122\6\uffff\1\122\6\uffff\1\55\2\uffff\1\120\37\uffff\1\55\1\uffff" +
		"\1\55\6\uffff\1\55\1\uffff\2\11\2\uffff\1\11\1\124\3\11\1\57\1\60\35\11\1\124\1\11" +
		"\1\124\6\11\1\124\1\uffff\2\11\2\uffff\5\11\1\57\1\60\47\11\63\uff8e\24\uff98\1\150" +
		"\36\uff98\63\uff8f\6\uffff\1\151\1\131\1\45\34\uffff\1\151\3\uffff\1\151\1\uffff" +
		"\1\151\1\52\1\53\3\uffff\1\53\1\151\6\uffff\1\151\1\131\35\uffff\1\151\3\uffff\1" +
		"\151\1\uffff\1\151\6\uffff\1\151\6\uffc7\1\130\1\131\1\45\34\uffc7\1\130\1\47\2\uffc7" +
		"\1\130\1\uffc7\1\130\1\52\1\53\3\uffc7\1\53\1\130\6\uffc5\1\133\1\105\1\45\34\uffc5" +
		"\1\106\1\107\2\uffc5\1\133\1\uffc5\1\133\1\52\1\53\3\uffc5\1\53\1\133\6\uffff\1\152" +
		"\1\134\35\uffff\1\152\3\uffff\1\152\1\uffff\1\152\6\uffff\1\152\6\uffff\1\153\1\135" +
		"\35\uffff\1\153\2\uffff\6\153\2\uffff\3\153\6\uffff\1\154\36\uffff\1\154\2\uffff" +
		"\6\154\1\140\1\uffff\3\154\63\uffc6\6\uffff\1\155\26\uffff\2\156\6\uffff\1\155\3" +
		"\uffff\1\155\1\uffff\1\155\6\uffff\1\155\6\uffff\1\113\1\141\43\uffff\1\113\7\uffff" +
		"\63\uffc4\6\uffff\1\157\1\143\35\uffff\1\157\3\uffff\1\157\1\uffff\1\157\6\uffff" +
		"\1\157\63\uffc2\6\uffff\1\145\1\116\35\uffff\1\145\3\uffff\1\145\1\uffff\1\145\1" +
		"\117\5\uffff\1\145\6\uffc3\2\160\35\uffc3\1\160\3\uffc3\1\160\1\uffc3\1\160\1\uffc3" +
		"\1\161\3\uffc3\1\161\1\160\6\uffff\1\146\36\uffff\1\146\3\uffff\1\146\1\uffff\1\146" +
		"\6\uffff\1\146\63\uff8d\6\uffc7\1\151\1\131\1\45\34\uffc7\1\151\1\47\2\uffc7\1\151" +
		"\1\uffc7\1\151\1\52\1\53\3\uffc7\1\53\1\151\6\uffff\1\152\1\134\35\uffff\1\152\3" +
		"\uffff\1\152\1\uffff\1\152\1\117\5\uffff\1\152\6\uffc6\1\153\1\135\1\136\34\uffc6" +
		"\1\153\1\137\1\uffc6\6\153\1\140\1\uffc6\3\153\6\uffff\2\162\35\uffff\1\162\2\uffff" +
		"\6\162\1\140\1\uffff\3\162\6\uffc0\2\163\35\uffc0\1\163\3\uffc0\1\163\1\uffc0\1\163" +
		"\1\uffc0\1\164\3\uffc0\1\164\1\163\6\uffff\1\155\36\uffff\1\155\3\uffff\1\155\1\uffff" +
		"\1\155\6\uffff\1\155\6\uffc2\1\157\1\143\35\uffc2\1\157\3\uffc2\1\157\1\uffc2\1\157" +
		"\1\uffc2\1\144\3\uffc2\1\144\1\157\6\uffff\1\165\1\160\35\uffff\1\165\3\uffff\1\165" +
		"\1\uffff\1\165\6\uffff\1\165\63\uffc3\6\uffff\1\166\1\162\35\uffff\1\166\2\uffff" +
		"\6\166\2\uffff\3\166\6\uffff\1\167\1\163\35\uffff\1\167\3\uffff\1\167\1\uffff\1\167" +
		"\6\uffff\1\167\63\uffc0\6\uffc3\1\165\1\160\35\uffc3\1\165\3\uffc3\1\165\1\uffc3" +
		"\1\165\1\uffc3\1\161\3\uffc3\1\161\1\165\6\uffff\1\166\1\162\35\uffff\1\166\2\uffff" +
		"\6\166\1\140\1\uffff\3\166\6\uffc0\1\167\1\163\35\uffc0\1\167\3\uffc0\1\167\1\uffc0" +
		"\1\167\1\uffc0\1\164\3\uffc0\1\164\1\167");

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
				state = lapg_lexem[state * 51 + mapCharacter(chr)];
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
