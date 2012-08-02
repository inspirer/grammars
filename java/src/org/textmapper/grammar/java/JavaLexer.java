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
		public static final int TraditionalComment = 4;
		public static final int kw_abstract = 5;
		public static final int kw_assert = 6;
		public static final int kw_boolean = 7;
		public static final int kw_break = 8;
		public static final int kw_byte = 9;
		public static final int kw_case = 10;
		public static final int kw_catch = 11;
		public static final int kw_char = 12;
		public static final int kw_class = 13;
		public static final int kw_const = 14;
		public static final int kw_continue = 15;
		public static final int kw_default = 16;
		public static final int kw_do = 17;
		public static final int kw_double = 18;
		public static final int kw_else = 19;
		public static final int kw_enum = 20;
		public static final int kw_extends = 21;
		public static final int kw_final = 22;
		public static final int kw_finally = 23;
		public static final int kw_float = 24;
		public static final int kw_for = 25;
		public static final int kw_goto = 26;
		public static final int kw_if = 27;
		public static final int kw_implements = 28;
		public static final int kw_import = 29;
		public static final int kw_instanceof = 30;
		public static final int kw_int = 31;
		public static final int kw_interface = 32;
		public static final int kw_long = 33;
		public static final int kw_native = 34;
		public static final int kw_new = 35;
		public static final int kw_package = 36;
		public static final int kw_private = 37;
		public static final int kw_protected = 38;
		public static final int kw_public = 39;
		public static final int kw_return = 40;
		public static final int kw_short = 41;
		public static final int kw_static = 42;
		public static final int kw_strictfp = 43;
		public static final int kw_super = 44;
		public static final int kw_switch = 45;
		public static final int kw_synchronized = 46;
		public static final int kw_this = 47;
		public static final int kw_throw = 48;
		public static final int kw_throws = 49;
		public static final int kw_transient = 50;
		public static final int kw_try = 51;
		public static final int kw_void = 52;
		public static final int kw_volatile = 53;
		public static final int kw_while = 54;
		public static final int IntegerLiteral = 55;
		public static final int FloatingPointLiteral = 56;
		public static final int BooleanLiteral = 57;
		public static final int CharacterLiteral = 58;
		public static final int StringLiteral = 59;
		public static final int NullLiteral = 60;
		public static final int LPAREN = 61;
		public static final int RPAREN = 62;
		public static final int LCURLY = 63;
		public static final int RCURLY = 64;
		public static final int LSQUARE = 65;
		public static final int RSQUARE = 66;
		public static final int SEMICOLON = 67;
		public static final int COMMA = 68;
		public static final int DOT = 69;
		public static final int EQUAL = 70;
		public static final int GREATER = 71;
		public static final int LESS = 72;
		public static final int EXCLAMATION = 73;
		public static final int TILDE = 74;
		public static final int QUESTIONMARK = 75;
		public static final int COLON = 76;
		public static final int EQUALEQUAL = 77;
		public static final int LESSEQUAL = 78;
		public static final int GREATEREQUAL = 79;
		public static final int EXCLAMATIONEQUAL = 80;
		public static final int AMPERSANDAMPERSAND = 81;
		public static final int OROR = 82;
		public static final int PLUSPLUS = 83;
		public static final int MINUSMINUS = 84;
		public static final int PLUS = 85;
		public static final int MINUS = 86;
		public static final int MULT = 87;
		public static final int SLASH = 88;
		public static final int AMPERSAND = 89;
		public static final int OR = 90;
		public static final int XOR = 91;
		public static final int PERCENT = 92;
		public static final int LESSLESS = 93;
		public static final int GREATERGREATER = 94;
		public static final int GREATERGREATERGREATER = 95;
		public static final int PLUSEQUAL = 96;
		public static final int MINUSEQUAL = 97;
		public static final int MULTEQUAL = 98;
		public static final int SLASHEQUAL = 99;
		public static final int AMPERSANDEQUAL = 100;
		public static final int OREQUAL = 101;
		public static final int XOREQUAL = 102;
		public static final int PERCENTEQUAL = 103;
		public static final int LESSLESSEQUAL = 104;
		public static final int GREATERGREATEREQUAL = 105;
		public static final int GREATERGREATERGREATEREQUAL = 106;
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
		"\1\0\10\1\1\47\1\6\1\1\1\47\1\5\14\1\1\4\5\1\1\47\1\31\1\15\1\1\1\43\1\42\1\35\1" +
		"\14\1\16\1\17\1\10\1\37\1\25\1\40\1\13\1\7\1\11\1\54\2\63\4\52\2\44\1\34\1\24\1\30" +
		"\1\26\1\27\1\33\1\1\1\45\1\53\1\45\1\56\1\55\1\56\5\43\1\50\3\43\1\57\7\43\1\51\2" +
		"\43\1\22\1\2\1\23\1\41\1\12\1\1\1\45\1\61\1\45\1\56\1\55\1\62\5\43\1\50\1\43\1\60" +
		"\1\43\1\57\1\43\1\60\1\43\1\60\1\3\2\43\1\51\2\43\1\20\1\36\1\21\1\32\53\1\1\43\12" +
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

	private static final short[] lapg_lexemnum = unpack_short(114,
		"\1\0\2\3\4\5\6\7\10\11\12\13\14\15\16\17\20\21\22\23\24\25\26\27\30\31\32\33\34\35" +
		"\36\37\40\41\42\43\44\45\46\47\50\51\52\53\54\55\56\57\60\61\62\63\64\65\66\67\67" +
		"\67\67\70\70\70\70\71\71\72\73\74\75\76\77\100\101\102\103\104\105\106\107\110\111" +
		"\112\113\114\115\116\117\120\121\122\123\124\125\126\127\130\131\132\133\134\135" +
		"\136\137\140\141\142\143\144\145\146\147\150\151\152");

	private static final short[] lapg_lexem = unpack_vc_short(6708,
		"\1\ufffe\1\uffff\1\1\1\2\1\3\1\4\1\5\1\6\1\7\1\10\1\2\1\11\1\12\1\13\1\14\1\15\1" +
		"\16\1\17\1\20\1\21\1\22\1\23\1\24\1\25\1\26\1\27\1\30\1\31\1\32\1\33\1\34\1\35\1" +
		"\36\1\37\1\40\1\2\1\41\1\2\1\uffff\1\5\2\2\1\41\1\2\1\41\6\2\1\41\3\uffff\1\42\60" +
		"\uffff\2\ufffd\1\43\1\2\5\ufffd\2\2\30\ufffd\4\2\1\ufffd\14\2\64\ufffc\6\ufffb\1" +
		"\5\141\ufffb\7\uff9e\1\44\1\45\15\uff9e\1\46\35\uff9e\26\uff9f\1\47\35\uff9f\11\uffc6" +
		"\1\50\1\51\1\52\30\uffc6\1\53\3\uffc6\1\54\1\55\1\50\1\56\1\50\1\57\1\60\2\uffc6" +
		"\1\56\1\60\1\50\11\uffb1\1\61\32\uffb1\1\61\5\uffb1\1\61\1\uffb1\1\61\6\uffb1\1\61" +
		"\1\uffff\1\62\1\63\2\62\2\uffff\5\62\1\uffff\47\62\1\uffff\1\13\1\64\2\13\2\uffff" +
		"\6\13\1\65\46\13\64\uffb9\64\uffb8\64\uffb7\64\uffb6\64\uffb5\64\uffb4\64\uffb3\64" +
		"\uffb2\26\uffb0\1\66\35\uffb0\26\uffaf\1\67\1\70\34\uffaf\26\uffae\1\71\1\uffae\1" +
		"\72\33\uffae\26\uffad\1\73\35\uffad\64\uffac\64\uffab\64\uffaa\26\uff9d\1\74\6\uff9d" +
		"\1\75\26\uff9d\26\uff9c\1\76\7\uff9c\1\77\25\uff9c\26\uffa1\1\100\10\uffa1\1\101" +
		"\24\uffa1\26\uffa0\1\102\11\uffa0\1\103\23\uffa0\26\uff9b\1\104\35\uff9b\26\uff9a" +
		"\1\105\35\uff9a\11\uffc6\1\106\1\107\1\52\30\uffc6\1\106\3\uffc6\1\54\1\uffc6\1\106" +
		"\1\uffc6\1\106\1\57\1\60\3\uffc6\1\60\1\106\3\uffff\1\42\5\uffff\1\110\32\uffff\2" +
		"\110\4\uffff\5\110\2\uffff\3\110\3\uffff\1\111\60\uffff\1\ufffa\4\44\2\ufffa\55\44" +
		"\1\uffff\7\45\1\112\53\45\64\uff93\64\uff94\11\uffc4\1\50\1\113\1\52\30\uffc4\1\53" +
		"\3\uffc4\1\114\1\uffc4\1\50\1\uffc4\1\50\1\57\1\60\3\uffc4\1\60\1\50\11\uffff\1\50" +
		"\1\51\31\uffff\1\53\5\uffff\1\50\1\uffff\1\50\6\uffff\1\50\11\uffc2\1\115\32\uffc2" +
		"\1\115\5\uffc2\1\115\1\uffc2\1\115\1\116\1\117\3\uffc2\1\117\1\115\11\uffff\1\53" +
		"\1\120\1\52\30\uffff\1\53\5\uffff\1\53\1\uffff\1\53\1\57\1\60\3\uffff\1\60\1\53\64" +
		"\uffc6\11\uffff\1\121\1\uffff\1\122\30\uffff\2\121\4\uffff\5\121\2\uffff\3\121\11" +
		"\uffff\1\123\42\uffff\1\123\20\uffff\1\124\25\uffff\2\125\3\uffff\1\124\5\uffff\1" +
		"\124\1\uffff\1\124\6\uffff\1\124\64\uffc0\11\uffc2\1\61\1\126\31\uffc2\1\61\5\uffc2" +
		"\1\61\1\uffc2\1\61\1\116\1\117\3\uffc2\1\117\1\61\14\uffff\1\127\51\uffff\1\62\1" +
		"\130\5\uffff\1\131\2\uffff\2\62\34\uffff\1\132\1\uffff\1\131\3\uffff\3\62\1\131\2" +
		"\uffff\1\13\1\133\5\uffff\1\134\2\uffff\2\13\34\uffff\1\135\1\uffff\1\134\3\uffff" +
		"\3\13\1\134\64\uffbb\64\uffa9\64\uffa7\26\uff98\1\136\1\137\34\uff98\64\uffa8\26" +
		"\uff99\1\140\35\uff99\64\uffa6\64\uff92\64\uffa5\64\uff91\64\uffa4\64\uff96\64\uffa3" +
		"\64\uff95\64\uffa2\64\uff90\64\uff8f\11\uffc6\1\106\1\141\1\52\30\uffc6\1\106\3\uffc6" +
		"\1\54\1\uffc6\1\106\1\uffc6\1\106\1\57\1\60\3\uffc6\1\60\1\106\11\uffff\1\106\1\107" +
		"\31\uffff\1\106\5\uffff\1\106\1\uffff\1\106\6\uffff\1\106\11\uffff\1\142\32\uffff" +
		"\2\142\4\uffff\5\142\2\uffff\3\142\3\uffff\1\111\5\uffff\1\143\32\uffff\2\143\4\uffff" +
		"\5\143\2\uffff\3\143\1\uffff\6\45\1\144\1\112\53\45\11\uffff\1\50\1\113\31\uffff" +
		"\1\53\5\uffff\1\50\1\uffff\1\50\6\uffff\1\50\64\uffc4\11\uffc2\1\115\1\145\31\uffc2" +
		"\1\115\5\uffc2\1\115\1\uffc2\1\115\1\116\1\117\3\uffc2\1\117\1\115\11\uffff\1\146" +
		"\25\uffff\2\147\3\uffff\1\146\5\uffff\1\146\1\uffff\1\146\6\uffff\1\146\64\uffc2" +
		"\11\uffff\1\53\1\120\31\uffff\1\53\5\uffff\1\53\1\uffff\1\53\6\uffff\1\53\11\uffc5" +
		"\1\121\1\150\1\151\30\uffc5\2\121\2\uffc5\1\152\1\uffc5\5\121\1\153\1\uffc5\3\121" +
		"\11\uffff\1\154\32\uffff\2\154\4\uffff\5\154\2\uffff\3\154\11\uffc3\1\123\1\155\35" +
		"\uffc3\1\156\3\uffc3\1\123\7\uffc3\11\uffc1\1\124\1\157\31\uffc1\1\124\5\uffc1\1" +
		"\124\1\uffc1\1\124\1\uffc1\1\160\3\uffc1\1\160\1\124\11\uffff\1\124\32\uffff\1\124" +
		"\5\uffff\1\124\1\uffff\1\124\6\uffff\1\124\11\uffff\1\61\1\126\31\uffff\1\61\5\uffff" +
		"\1\61\1\uffff\1\61\6\uffff\1\61\64\uffbc\3\uffff\1\130\5\uffff\1\161\32\uffff\2\161" +
		"\4\uffff\5\161\2\uffff\3\161\11\uffff\1\132\2\uffff\1\127\35\uffff\1\132\1\uffff" +
		"\1\132\6\uffff\1\132\11\uffff\1\62\2\uffff\1\127\35\uffff\1\62\1\uffff\1\62\6\uffff" +
		"\1\62\3\uffff\1\133\5\uffff\1\162\32\uffff\2\162\4\uffff\5\162\2\uffff\3\162\1\uffff" +
		"\1\13\1\64\2\13\2\uffff\2\13\1\135\3\13\1\65\34\13\1\135\1\13\1\135\6\13\1\135\1" +
		"\uffff\1\13\1\64\2\13\2\uffff\6\13\1\65\46\13\64\uff8d\26\uff97\1\163\35\uff97\64" +
		"\uff8e\11\uffff\1\106\1\141\31\uffff\1\106\5\uffff\1\106\1\uffff\1\106\6\uffff\1" +
		"\106\11\uffff\1\164\32\uffff\2\164\4\uffff\5\164\2\uffff\3\164\11\uffff\1\165\32" +
		"\uffff\2\165\4\uffff\5\165\2\uffff\3\165\64\ufff9\11\uffff\1\115\1\145\31\uffff\1" +
		"\115\5\uffff\1\115\1\uffff\1\115\6\uffff\1\115\11\uffc2\1\146\1\166\31\uffc2\1\146" +
		"\5\uffc2\1\146\1\uffc2\1\146\1\uffc2\1\117\3\uffc2\1\117\1\146\11\uffff\1\146\32" +
		"\uffff\1\146\5\uffff\1\146\1\uffff\1\146\6\uffff\1\146\11\uffff\1\121\1\150\31\uffff" +
		"\2\121\4\uffff\5\121\2\uffff\3\121\11\uffff\1\154\32\uffff\2\154\4\uffff\5\154\1" +
		"\153\1\uffff\3\154\64\uffc5\11\uffff\1\167\25\uffff\2\170\3\uffff\1\167\5\uffff\1" +
		"\167\1\uffff\1\167\6\uffff\1\167\11\uffff\1\154\1\171\31\uffff\2\154\4\uffff\5\154" +
		"\1\153\1\uffff\3\154\11\uffff\1\123\1\155\41\uffff\1\123\7\uffff\64\uffc3\11\uffff" +
		"\1\124\1\157\31\uffff\1\124\5\uffff\1\124\1\uffff\1\124\6\uffff\1\124\64\uffc1\11" +
		"\uffff\1\172\32\uffff\2\172\4\uffff\5\172\2\uffff\3\172\11\uffff\1\173\32\uffff\2" +
		"\173\4\uffff\5\173\2\uffff\3\173\64\uff8c\11\uffff\1\2\32\uffff\2\2\4\uffff\5\2\2" +
		"\uffff\3\2\11\uffff\1\174\32\uffff\2\174\4\uffff\5\174\2\uffff\3\174\11\uffff\1\146" +
		"\1\166\31\uffff\1\146\5\uffff\1\146\1\uffff\1\146\6\uffff\1\146\11\uffbf\1\167\1" +
		"\175\31\uffbf\1\167\5\uffbf\1\167\1\uffbf\1\167\1\uffbf\1\176\3\uffbf\1\176\1\167" +
		"\11\uffff\1\167\32\uffff\1\167\5\uffff\1\167\1\uffff\1\167\6\uffff\1\167\11\uffff" +
		"\1\154\1\171\31\uffff\2\154\4\uffff\5\154\2\uffff\3\154\11\uffff\1\177\32\uffff\2" +
		"\177\4\uffff\5\177\2\uffff\3\177\11\uffff\1\200\32\uffff\2\200\4\uffff\5\200\2\uffff" +
		"\3\200\11\uffff\1\2\32\uffff\2\2\4\uffff\5\2\2\uffff\3\2\11\uffff\1\167\1\175\31" +
		"\uffff\1\167\5\uffff\1\167\1\uffff\1\167\6\uffff\1\167\64\uffbf\11\uffff\1\62\32" +
		"\uffff\2\62\4\uffff\5\62\2\uffff\3\62\11\uffff\1\13\32\uffff\2\13\4\uffff\5\13\2" +
		"\uffff\3\13");

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
			case 4:
				return false;
		}
		return true;
	}

	private static Map<String,Integer> subTokensOfIdentifier = new HashMap<String,Integer>();
	static {
		subTokensOfIdentifier.put("abstract", 5);
		subTokensOfIdentifier.put("assert", 6);
		subTokensOfIdentifier.put("boolean", 7);
		subTokensOfIdentifier.put("break", 8);
		subTokensOfIdentifier.put("byte", 9);
		subTokensOfIdentifier.put("case", 10);
		subTokensOfIdentifier.put("catch", 11);
		subTokensOfIdentifier.put("char", 12);
		subTokensOfIdentifier.put("class", 13);
		subTokensOfIdentifier.put("const", 14);
		subTokensOfIdentifier.put("continue", 15);
		subTokensOfIdentifier.put("default", 16);
		subTokensOfIdentifier.put("do", 17);
		subTokensOfIdentifier.put("double", 18);
		subTokensOfIdentifier.put("else", 19);
		subTokensOfIdentifier.put("enum", 20);
		subTokensOfIdentifier.put("extends", 21);
		subTokensOfIdentifier.put("final", 22);
		subTokensOfIdentifier.put("finally", 23);
		subTokensOfIdentifier.put("float", 24);
		subTokensOfIdentifier.put("for", 25);
		subTokensOfIdentifier.put("goto", 26);
		subTokensOfIdentifier.put("if", 27);
		subTokensOfIdentifier.put("implements", 28);
		subTokensOfIdentifier.put("import", 29);
		subTokensOfIdentifier.put("instanceof", 30);
		subTokensOfIdentifier.put("int", 31);
		subTokensOfIdentifier.put("interface", 32);
		subTokensOfIdentifier.put("long", 33);
		subTokensOfIdentifier.put("native", 34);
		subTokensOfIdentifier.put("new", 35);
		subTokensOfIdentifier.put("package", 36);
		subTokensOfIdentifier.put("private", 37);
		subTokensOfIdentifier.put("protected", 38);
		subTokensOfIdentifier.put("public", 39);
		subTokensOfIdentifier.put("return", 40);
		subTokensOfIdentifier.put("short", 41);
		subTokensOfIdentifier.put("static", 42);
		subTokensOfIdentifier.put("strictfp", 43);
		subTokensOfIdentifier.put("super", 44);
		subTokensOfIdentifier.put("switch", 45);
		subTokensOfIdentifier.put("synchronized", 46);
		subTokensOfIdentifier.put("this", 47);
		subTokensOfIdentifier.put("throw", 48);
		subTokensOfIdentifier.put("throws", 49);
		subTokensOfIdentifier.put("transient", 50);
		subTokensOfIdentifier.put("try", 51);
		subTokensOfIdentifier.put("void", 52);
		subTokensOfIdentifier.put("volatile", 53);
		subTokensOfIdentifier.put("while", 54);
		subTokensOfIdentifier.put("true", 63);
		subTokensOfIdentifier.put("false", 64);
		subTokensOfIdentifier.put("null", 67);
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
