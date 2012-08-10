package org.textmapper.grammar.java;

import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.fail;
import static org.textmapper.grammar.java.JavaLexer.*;

/**
 * evgeny, 8/1/12
 */
public class JavaLexerTest {

    @Test
    public void testIdentifiers() {
        assertJava(" String    ", Lexems.Identifier);
        assertJava("i3", Lexems.Identifier);
        assertJava(/* In greek */"\u03b1\u03c1\u03b5\u03c4\u03b7", Lexems.Identifier);
        assertJava("MAX_VALUE", Lexems.Identifier);
        assertJava("isLetterOrDigit", Lexems.Identifier);
        assertJava("is\\u00aa", Lexems.Identifier);
    }

    @Test
    public void testKeywords() {
        assertJava("abstract", Lexems.kw_abstract);
        assertJava("continue", Lexems.kw_continue);
        assertJava("for", Lexems.kw_for);
        assertJava("new", Lexems.kw_new);
        assertJava("switch", Lexems.kw_switch);
        assertJava("assert", Lexems.kw_assert);
        assertJava("default", Lexems.kw_default);
        assertJava("if", Lexems.kw_if);
        assertJava("package", Lexems.kw_package);
        assertJava("synchronized", Lexems.kw_synchronized);
        assertJava("boolean", Lexems.kw_boolean);
        assertJava("do", Lexems.kw_do);
        assertJava("goto", Lexems.kw_goto);
        assertJava("private", Lexems.kw_private);
        assertJava("this", Lexems.kw_this);
        assertJava("break", Lexems.kw_break);
        assertJava("double", Lexems.kw_double);
        assertJava("implements", Lexems.kw_implements);
        assertJava("protected", Lexems.kw_protected);
        assertJava("throw", Lexems.kw_throw);
        assertJava("byte", Lexems.kw_byte);
        assertJava("else", Lexems.kw_else);
        assertJava("import", Lexems.kw_import);
        assertJava("public", Lexems.kw_public);
        assertJava("throws", Lexems.kw_throws);
        assertJava("case", Lexems.kw_case);
        assertJava("enum", Lexems.kw_enum);
        assertJava("instanceof", Lexems.kw_instanceof);
        assertJava("return", Lexems.kw_return);
        assertJava("transient", Lexems.kw_transient);
        assertJava("catch", Lexems.kw_catch);
        assertJava("extends", Lexems.kw_extends);
        assertJava("int", Lexems.kw_int);
        assertJava("short", Lexems.kw_short);
        assertJava("try", Lexems.kw_try);
        assertJava("char", Lexems.kw_char);
        assertJava("final", Lexems.kw_final);
        assertJava("interface", Lexems.kw_interface);
        assertJava("static", Lexems.kw_static);
        assertJava("void", Lexems.kw_void);
        assertJava("class", Lexems.kw_class);
        assertJava("finally", Lexems.kw_finally);
        assertJava("long", Lexems.kw_long);
        assertJava("strictfp", Lexems.kw_strictfp);
        assertJava("volatile", Lexems.kw_volatile);
        assertJava("const", Lexems.kw_const);
        assertJava("float", Lexems.kw_float);
        assertJava("native", Lexems.kw_native);
        assertJava("super", Lexems.kw_super);
        assertJava("while", Lexems.kw_while);
    }

    @Test
    public void testInts() {
        assertJava("0", Lexems.IntegerLiteral);
        assertJava("2", Lexems.IntegerLiteral);
        assertJava("0372", Lexems.IntegerLiteral);
        assertJava("0xDada_Cafe", Lexems.IntegerLiteral);
        assertJava("1996", Lexems.IntegerLiteral);
        assertJava("0x00_FF__00_FF", Lexems.IntegerLiteral);

        assertJava("0l", Lexems.IntegerLiteral);
        assertJava("0777L", Lexems.IntegerLiteral);
        assertJava("0x100000000L", Lexems.IntegerLiteral);
        assertJava("2_147_483_648L", Lexems.IntegerLiteral);
        assertJava("0xC0B0L", Lexems.IntegerLiteral);
    }

    @Test
    public void testFloat() {
        assertJava("1e1f", Lexems.FloatingPointLiteral);
        assertJava("2.f", Lexems.FloatingPointLiteral);
        assertJava(".3f", Lexems.FloatingPointLiteral);
        assertJava("0f", Lexems.FloatingPointLiteral);
        assertJava("3.14f", Lexems.FloatingPointLiteral);
        assertJava("6.022137e+23f", Lexems.FloatingPointLiteral);

        assertJava("1e1", Lexems.FloatingPointLiteral);
        assertJava("2.", Lexems.FloatingPointLiteral);
        assertJava(".3", Lexems.FloatingPointLiteral);
        assertJava("0.0", Lexems.FloatingPointLiteral);
        assertJava("3.14", Lexems.FloatingPointLiteral);
        assertJava("1e-9d", Lexems.FloatingPointLiteral);
        assertJava("1e137", Lexems.FloatingPointLiteral);
    }

    @Test
    public void testChars() {
        assertJava("'a'", Lexems.CharacterLiteral);
        assertJava("'%'", Lexems.CharacterLiteral);
        assertJava("'\\t'", Lexems.CharacterLiteral);
        assertJava("'\\\\'", Lexems.CharacterLiteral);
        assertJava("'\\''", Lexems.CharacterLiteral);
        assertJava("'\\u03a9'", Lexems.CharacterLiteral);
        assertJava("'\\uFFFF'", Lexems.CharacterLiteral);
        assertJava("'\\177'", Lexems.CharacterLiteral);
        assertJava("'\u03a9'", Lexems.CharacterLiteral);
        assertJava("'\u2297'", Lexems.CharacterLiteral);
    }

    @Test
    public void testStrings() {
        assertJava("\"\"", Lexems.StringLiteral);
        assertJava("\"\\\"\"", Lexems.StringLiteral);
        assertJava("\"This is a string\"", Lexems.StringLiteral);
        assertJava("\"\\n\"", Lexems.StringLiteral);
    }

    @Test
    public void testBooleanAndNull() {
        assertJava("null", Lexems.NullLiteral);
        assertJava("true", Lexems.BooleanLiteral);
        assertJava("false", Lexems.BooleanLiteral);
    }

    @Test
    public void testComments() {
        assertComment("x//asdad\n    \ny",
                Lexems.Identifier, Lexems.EndOfLineComment, Lexems.Identifier);

        assertComment("  /**/   ",
                Lexems.TraditionalComment);

        assertComment("x /* this comment /* // /** ends here: */ y",
                Lexems.Identifier, Lexems.TraditionalComment, Lexems.Identifier);
    }

    @Test
    public void testSeparators() {
        assertJava(" (    )    {    }    [    ]    ;    ,    . ",
                Lexems.LPAREN, Lexems.RPAREN, Lexems.LCURLY, Lexems.RCURLY, Lexems.LSQUARE, Lexems.RSQUARE,
                Lexems.SEMICOLON, Lexems.COMMA, Lexems.DOT);
    }

    @Test
    public void testOperators() {
        assertJava("=   >   <   !   ~   ?   :",
                Lexems.EQUAL, Lexems.GREATER, Lexems.LESS, Lexems.EXCLAMATION, Lexems.TILDE,
                Lexems.QUESTIONMARK, Lexems.COLON);

        assertJava("==  <=  >=  !=  &&  ||  ++  --",
                Lexems.EQUALEQUAL, Lexems.LESSEQUAL, Lexems.GREATEREQUAL, Lexems.EXCLAMATIONEQUAL,
                Lexems.AMPERSANDAMPERSAND, Lexems.OROR, Lexems.PLUSPLUS, Lexems.MINUSMINUS);

        assertJava("+   -   *   /   &   |   ^   %   <<   >>   >>>	@",
                Lexems.PLUS, Lexems.MINUS, Lexems.MULT, Lexems.SLASH,
                Lexems.AMPERSAND, Lexems.OR, Lexems.XOR, Lexems.PERCENT,
                Lexems.LESSLESS, Lexems.GREATERGREATER, Lexems.GREATERGREATERGREATER, Lexems.ATSIGN);

        assertJava("+=  -=  *=  /=  &=  |=  ^=  %=  <<=  >>=  >>>=",
                Lexems.PLUSEQUAL, Lexems.MINUSEQUAL, Lexems.MULTEQUAL, Lexems.SLASHEQUAL,
                Lexems.AMPERSANDEQUAL, Lexems.OREQUAL, Lexems.XOREQUAL, Lexems.PERCENTEQUAL,
                Lexems.LESSLESSEQUAL, Lexems.GREATERGREATEREQUAL, Lexems.GREATERGREATERGREATEREQUAL);
    }

    private void assertJava(String text, int... lexems) {
        try {
            JavaLexer javaLexer = new JavaLexer(new StringReader(text), testReporter());
            LapgSymbol next;
            int index = 0;
            while ((next = javaLexer.next()).lexem != Lexems.eoi) {
                if (lexems.length == index) {
                    fail("unexpected lexem after eoi: " + next.lexem + "(" + text.substring(next.offset, next.endoffset) + ")");
                }
                if (lexems[index] != next.lexem) {
                    fail(next.line + ": got " + next.lexem + "(" + text.substring(next.offset, next.endoffset) + ") instead of " + lexems[index]);
                }
                index++;
            }
            if (index < lexems.length) {
                fail("expected lexem: " + lexems[index]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException: " + e.getMessage());
        }
    }

    private void assertComment(String text, int... lexems) {
        try {
            JavaLexer javaLexer = new JavaLexer(new StringReader(text), testReporter()) {
                @Override
                protected boolean createToken(LapgSymbol lapg_n, int lexemIndex) throws IOException {
                    if (lexemIndex == Lexems.EndOfLineComment || lexemIndex == Lexems.TraditionalComment) {
                        return true;
                    }
                    return super.createToken(lapg_n, lexemIndex);
                }
            };
            LapgSymbol next;
            int index = 0;
            while ((next = javaLexer.next()).lexem != Lexems.eoi) {
                if (lexems.length == index) {
                    fail("unexpected lexem after eoi: " + next.lexem + "(" + text.substring(next.offset, next.endoffset) + ")");
                }
                if (lexems[index] != next.lexem) {
                    fail(next.line + ": got " + next.lexem + "(" + text.substring(next.offset, next.endoffset) + ") instead of " + lexems[index]);
                }
                index++;
            }
            if (index < lexems.length) {
                fail("expected lexem: " + lexems[index]);
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException: " + e.getMessage());
        }
    }

    private ErrorReporter testReporter() {
        return new ErrorReporter() {
            public void error(int start, int end, int line, String s) {
                fail(line + ": " + s);
            }
        };
    }
}
