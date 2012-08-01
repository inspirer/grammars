# The Javaª Language Specification
# Java SE 7 Edition

#lang = "last"
lang = "java"
prefix = "Java"
gentree = true
positions = "line,offset"
endpositions = "offset"

# 3.5. Input Elements and Tokens

eoi: /\x1a/

# 3.6 White Space

WhiteSpace: /[\r\n\t\f\x20]|\r\n/	(space)

# 3.7 Comments

EndOfLineComment: /\/\/[^\r\n]*/ (space)

# TODO
#TraditionalComment: /\/\*/
#    / * CommentTail
#
#CommentTail:
#    * CommentTailStar
#    NotStar CommentTail
#
#CommentTailStar:
#    /
#    * CommentTailStar
#    NotStarNotSlash CommentTail
#
#NotStar:
#    InputCharacter but not *
#    LineTerminator
#
#NotStarNotSlash:
#    InputCharacter but not * or /
#    LineTerminator

# 3.8 Identifiers

Identifier: /{JavaLetter}{JavaLetterOrDigit}*/  (class)

JavaLetter = /[a-zA-Z_$\p{L}\p{Nl}]/
JavaLetterOrDigit = /[a-zA-Z0-9_$\p{L}\p{Nl}\p{Nd}]/

# 3.9 Keywords

kw_abstract: /abstract/
kw_assert: /assert/
kw_boolean: /boolean/
kw_break: /break/
kw_byte: /byte/
kw_case: /case/
kw_catch: /catch/
kw_char: /char/
kw_class: /class/
kw_const: /const/
kw_continue: /continue/
kw_default: /default/
kw_do: /do/
kw_double: /double/
kw_else: /else/
kw_enum: /enum/
kw_extends: /extends/
kw_final: /final/
kw_finally: /finally/
kw_float: /float/
kw_for: /for/
kw_goto: /goto/
kw_if: /if/
kw_implements: /implements/
kw_import: /import/
kw_instanceof: /instanceof/
kw_int: /int/
kw_interface: /interface/
kw_long: /long/
kw_native: /native/
kw_new: /new/
kw_package: /package/
kw_private: /private/
kw_protected: /protected/
kw_public: /public/
kw_return: /return/
kw_short: /short/
kw_static: /static/
kw_strictfp: /strictfp/
kw_super: /super/
kw_switch: /switch/
kw_synchronized: /synchronized/
kw_this: /this/
kw_throw: /throw/
kw_throws: /throws/
kw_transient: /transient/
kw_try: /try/
kw_void: /void/
kw_volatile: /volatile/
kw_while: /while/

# 3.10.1 Integer Literals

IntegerLiteral: /0|[1-9](_*{Digits})?[lL]?/
IntegerLiteral: /{HexNumeral}[lL]?/
IntegerLiteral: /0_*{OctalDigits}[lL]?/
IntegerLiteral: /0[bB]{BinaryDigits}[lL]?/

Digits = /{Digit}({DigitsAndUnderscores}?{Digit})?/
Digit = /[0-9]/
DigitsAndUnderscores = /[0-9_]+/

HexNumeral = /0[xX]{HexDigits}/
HexDigits = /{HexDigit}({HexDigitsAndUnderscores}?{HexDigit})?/
HexDigit = /[0-9a-fA-F]/
HexDigitsAndUnderscores = /[0-9a-fA-F_]+/

OctalDigits = /{OctalDigit}({OctalDigitsAndUnderscores}?{OctalDigit})?/
OctalDigit = /[0-7]/
OctalDigitsAndUnderscores = /[0-7_]+/

BinaryDigits = /[01]([01_]*[01])?/

# 3.10.2. Floating-Point Literals

FloatingPointLiteral: /({Digits}\.{Digits}?|\.{Digits}){ExponentPart}?{FloatTypeSuffix}?/

FloatingPointLiteral: /{Digits}{ExponentPart}{FloatTypeSuffix}?/
FloatingPointLiteral: /{Digits}{FloatTypeSuffix}/

ExponentPart = /[eE][+-]?{Digits}/
FloatTypeSuffix = /[fFdD]/


FloatingPointLiteral: /{HexSignificand}{BinaryExponent}[fFdD]?/
BinaryExponent = /[pP][+-]?{Digits}/

HexSignificand = /{HexNumeral}\.?|0[xX]{HexDigits}?\.{HexDigits}/


# 3.10.3. Boolean Literals

BooleanLiteral: /true|false/

# 3.10.4-6 String Literals

EscapeSequence = /\\[btnfr"'\\]|{OctalEscape}/
OctalEscape = /\\([0-3]?[0-7])?[0-7]/

CharacterLiteral: /'([^\r\n'\\]|{EscapeSequence})'/

StringLiteral: /"([^\r\n"\\]|{EscapeSequence})*"/

# 3.10.7. The Null Literal

NullLiteral: /null/

# 3.11. Separators

'(': /\(/
')': /\)/
'{': /{/
'}': /}/
'[': /\[/
']': /\]/
';': /;/
',': /,/
'.': /\./

# 3.12. Operators

'=': /=/
'>': />/
'<': /</
'!': /!/
'~': /~/
'?': /?/
':': /:/
'==': /==/
'<=': /<=/
'>=': />=/
'!=': /!=/
'&&': /&&/
'||': /\|\|/
'++': /\+\+/
'--': /--/
'+': /+/
'-': /-/
'*': /*/
'/': /\//
'&': /&/
'|': /\|/
'^': /\^/
'%': /%/
'<<': /<</
'>>': />>/
'>>>': />>>/
'+=': /+=/
'-=': /-=/
'*=': /*=/
'/=': /\/=/
'&=': /&=/
'|=': /\|=/
'^=': /^=/
'%=': /%=/
'<<=': /<<=/
'>>=': />>=/
'>>>=': />>>=/
