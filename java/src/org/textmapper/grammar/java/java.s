# The Javaª Language Specification
# Java SE 7 Edition

#lang = "last"
lang = "java"
prefix = "Java"
package = "org.textmapper.grammar.java"
positions = "line,offset"
endpositions = "offset"

# 3.5. Input Elements and Tokens

eoi: /\x1a/

# 3.6 White Space

WhiteSpace: /[\r\n\t\f\x20]|\r\n/	(space)

# 3.7 Comments

EndOfLineComment: /\/\/[^\r\n]*/ (space)

TraditionalComment: /\/\*([^*]|\*+[^\/*])*\*+\// (space)

# 3.8 Identifiers

Identifier: /{JavaLetter}{JavaLetterOrDigit}*/  (class)

JavaLetter = /[a-zA-Z_$\p{L}\p{Nl}]|{UnicodeEscape}/
JavaLetterOrDigit = /[a-zA-Z0-9_$\p{L}\p{Nl}\p{Nd}]|{UnicodeEscape}/

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

IntegerLiteral: /(0|[1-9](_*{Digits})?)[lL]?/
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

BooleanLiteral: /true/
BooleanLiteral: /false/

# 3.10.4-6 String Literals

EscapeSequence = /\\[btnfr"'\\]|{OctalEscape}/
OctalEscape = /\\([0-3]?[0-7])?[0-7]/

CharacterLiteral: /'([^\r\n'\\]|{EscapeSequence}|{UnicodeEscape})'/

StringLiteral: /"([^\r\n"\\]|{EscapeSequence}|{UnicodeEscape})*"/

UnicodeEscape = /\\u+{HexDigit}{4}/

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
'...': /\.\.\./

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
'@': /@/

%input CompilationUnit, MethodBody, GenericMethodDeclaration, ClassBodyDeclarations, Expression, Statement;

QualifiedIdentifier ::=
	  Identifier
	| QualifiedIdentifier '.' Identifier
;

CompilationUnit ::=
	  PackageDeclaration? ImportDeclaration+? TypeDeclaration+? ;

PackageDeclaration ::=
	  Modifiers? kw_package QualifiedIdentifier ';' ;

ImportDeclaration ::=
	kw_import kw_static? QualifiedIdentifier ('.' '*')? ';' ;

TypeDeclaration ::=
	  ClassDeclaration
	| InterfaceDeclaration
	| EnumDeclaration
	| AnnotationTypeDeclaration
	| ';'
;

ClassDeclaration ::=
	  Modifiersopt kw_class Identifier TypeParameters? (kw_extends ClassType)? (kw_implements InterfaceTypeList)? ClassBody ;

EnumDeclaration ::=
	  Modifiersopt kw_enum Identifier TypeParameters? (kw_implements InterfaceTypeList)? EnumBody ;

InterfaceDeclaration ::=
	  Modifiersopt kw_interface Identifier TypeParameters? (kw_extends InterfaceTypeList)? InterfaceBody ;

AnnotationTypeDeclaration ::=
	  Modifiers? '@' kw_interface Identifier TypeParameters? (kw_extends ClassType)? (kw_implements InterfaceTypeList)? AnnotationTypeBody ;

Literal ::=
	  IntegerLiteral
	| FloatingPointLiteral
	| CharacterLiteral
	| StringLiteral
	| NullLiteral
	| BooleanLiteral
;

Type ::=
	  PrimitiveType
	| ReferenceType
;

PrimitiveType ::=
	  kw_byte
	| kw_short
	| kw_int
	| kw_long
	| kw_char
	| kw_float
	| kw_double
	| kw_boolean
	| kw_void
;

ReferenceType ::=
	  ClassOrInterfaceType
	| ArrayType
;

ClassOrInterfaceType ::=
	  ClassOrInterface
	| GenericType
;

ClassOrInterface ::=
	  QualifiedIdentifier
	| GenericType '.' QualifiedIdentifier
;

GenericType ::=
	  ClassOrInterface TypeArguments
	| ClassOrInterface '<' '>'
;

ArrayTypeWithTypeArgumentsName ::=
	  GenericType '.' QualifiedIdentifier ;

ArrayType ::=
	  PrimitiveType Dims
	| QualifiedIdentifier Dims
	| ArrayTypeWithTypeArgumentsName Dims
	| GenericType Dims
;

ClassType ::=
	  ClassOrInterfaceType ;

Modifiers ::=
	  Modifier
	| Modifiers Modifier
;

Modifier ::=
	  kw_public
	| kw_protected
	| kw_private
	| kw_static
	| kw_abstract
	| kw_final
	| kw_native
	| kw_synchronized
	| kw_transient
	| kw_volatile
	| kw_strictfp
	| Annotation
;

InterfaceTypeList ::=
	  InterfaceType
	| InterfaceTypeList ',' InterfaceType
;

InterfaceType ::=
	  ClassOrInterfaceType ;

ClassBody ::=
	  '{' ClassBodyDeclarationsopt '}' ;

ClassBodyDeclarations ::=
	  ClassBodyDeclaration
	| ClassBodyDeclarations ClassBodyDeclaration
;

ClassBodyDeclaration ::=
	  ClassMemberDeclaration
	| StaticInitializer
	| ConstructorDeclaration
	| Block
;

Initializer ::=
	  Block ;

ClassMemberDeclaration ::=
	  FieldDeclaration
	| MethodDeclaration
	| ClassDeclaration
	| InterfaceDeclaration
	| EnumDeclaration
	| AnnotationTypeDeclaration
	| ';'
;

GenericMethodDeclaration ::=
	  MethodDeclaration
	| ConstructorDeclaration
;

FieldDeclaration ::=
	  Modifiersopt Type VariableDeclarators ';' ;

VariableDeclarators ::=
	  VariableDeclarator
	| VariableDeclarators ',' VariableDeclarator
;

VariableDeclarator ::=
	  VariableDeclaratorId ('=' VariableInitializer)? ;

VariableDeclaratorId ::=
	  Identifier Dimsopt ;

VariableInitializer ::=
	  Expression
	| ArrayInitializer
;

MethodDeclaration ::=
	  AbstractMethodDeclaration
	| MethodHeader MethodBody
;

AbstractMethodDeclaration ::=
	  MethodHeader ';' ;

MethodHeader ::=
	  Modifiersopt TypeParameters? Type Identifier '(' (FormalParameter separator ',')* ')' Dimsopt MethodHeaderThrowsClauseopt ;

MethodHeaderThrowsClause ::=
	  kw_throws (ClassType separator ',')+ ;

FormalParameter ::=
	  Modifiersopt Type '...'? VariableDeclaratorId ;

CatchFormalParameter ::=
	  Modifiersopt CatchType VariableDeclaratorId ;

CatchType ::=
	  (Type separator '|')+ ;

MethodBody ::=
	  '{' BlockStatementsopt '}'  ;

StaticInitializer ::=
	  kw_static Block ;

ConstructorDeclaration ::=
	  ConstructorHeader MethodBody
	| ConstructorHeader ';'
;

ConstructorHeader ::=
	  Modifiersopt TypeParameters? Identifier '(' (FormalParameter separator ',')* ')' MethodHeaderThrowsClauseopt ;

ExplicitConstructorInvocation ::=
	  ExplicitConstructorId '(' ArgumentListopt ')' ';'
;

ExplicitConstructorId ::=
	  (Primary '.')? TypeArguments? ThisOrSuper
	| QualifiedIdentifier '.' TypeArguments? ThisOrSuper
;

ThisOrSuper ::=
	  kw_this | kw_super ;

InterfaceBody ::=
	  '{' InterfaceMemberDeclaration* '}'  ;

InterfaceMemberDeclaration ::=
	  ConstantDeclaration
	| MethodHeader MethodBody
	| AbstractMethodDeclaration
	| ClassDeclaration
	| InterfaceDeclaration
	| EnumDeclaration
	| AnnotationTypeDeclaration
	| ';'
;

ConstantDeclaration ::=
	  FieldDeclaration ;

ArrayInitializer ::=
	  '{' (VariableInitializer separator ',')+? ','? '}' ;

Block ::=
	  '{' BlockStatementsopt '}' ;

BlockStatements ::=
	  BlockStatement
	| BlockStatements BlockStatement
;

BlockStatement ::=
	  LocalVariableDeclarationStatement
	| Statement
	| ClassDeclaration
	| InterfaceDeclaration
	| AnnotationTypeDeclaration
	| EnumDeclaration
;

LocalVariableDeclarationStatement ::=
	  LocalVariableDeclaration ';' ;

LocalVariableDeclaration ::=
	  Modifiers? Type VariableDeclarators ;

Statement ::=
	  AssertStatement
	| Block
	| EmptyStatement
	| ExpressionStatement
	| SwitchStatement
	| DoStatement
	| BreakStatement
	| ContinueStatement
	| ReturnStatement
	| SynchronizedStatement
	| ThrowStatement
	| TryStatement
	| LabeledStatement
	| IfStatement
	| WhileStatement
	| ForStatement
	| EnhancedForStatement
;

EmptyStatement ::=
	  ';' ;

LabeledStatement ::=
	  Label ':' Statement ;

Label ::=
	  Identifier ;

ExpressionStatement ::=
	  StatementExpression ';'
	| ExplicitConstructorInvocation
;

StatementExpression ::=
	  Assignment
	| PreIncrementExpression
	| PreDecrementExpression
	| PostIncrementExpression
	| PostDecrementExpression
	| MethodInvocation
	| ClassInstanceCreationExpression
;

%right kw_else;

IfStatement ::=
	  kw_if '(' Expression ')' Statement (kw_else Statement)? %prio kw_else ;

SwitchStatement ::=
	  kw_switch '(' Expression ')' SwitchBlock ;

SwitchBlock ::=
	  '{' SwitchBlockStatementGroup* SwitchLabel+? '}' ;

SwitchBlockStatementGroup ::=
	SwitchLabel+ BlockStatements ;

SwitchLabel ::=
	  kw_case ConstantExpression ':'
	| kw_default ':'
;

WhileStatement ::=
	  kw_while '(' Expression ')' Statement ;

DoStatement ::=
	  kw_do Statement kw_while '(' Expression ')' ';' ;

ForStatement ::=
	  kw_for '(' ForInitopt ';' Expressionopt ';' (StatementExpression separator ',')* ')' Statement ;

EnhancedForStatement ::=
	  kw_for '(' Modifiers? Type Identifier Dimsopt ':' Expression ')' Statement ;

ForInit ::=
	  (StatementExpression separator ',')+
	| LocalVariableDeclaration
;

AssertStatement ::=
	  kw_assert Expression (':' Expression)? ';' ;

BreakStatement ::=
	  kw_break Identifieropt ';' ;

ContinueStatement ::=
	  kw_continue Identifieropt ';' ;

ReturnStatement ::=
	  kw_return Expressionopt ';' ;

ThrowStatement ::=
	  kw_throw Expression ';' ;

SynchronizedStatement ::=
	  kw_synchronized '(' Expression ')' Block ;

TryStatement ::=
	  kw_try ('(' (Resource separator ';')+ (';')? ')')? Block CatchClause* Finallyopt ;

Resource ::=
	  Modifiersopt Type VariableDeclaratorId '=' VariableInitializer ;

CatchClause ::=
	  kw_catch '(' CatchFormalParameter ')' Block ;

Finally ::=
	  kw_finally Block ;

Primary ::=
	  PrimaryNoNewArray
	| ArrayCreationWithArrayInitializer
	| ArrayCreationWithoutArrayInitializer
;

PrimaryNoNewArray ::=
	  Literal
	| kw_this
	| '(' Expression_NotName ')'
	| '(' QualifiedIdentifier ')'
	| ClassInstanceCreationExpression
	| FieldAccess
	| QualifiedIdentifier '.' ThisOrSuper
	| QualifiedIdentifier Dims? '.' kw_class
	| PrimitiveType Dims? '.' kw_class
	| MethodInvocation
	| ArrayAccess
;

ClassInstanceCreationExpression ::=
	  (Primary '.')? kw_new TypeArguments? ClassType '(' ArgumentListopt ')' ClassBodyopt
	| QualifiedIdentifier '.' kw_new TypeArguments? ClassType '(' ArgumentListopt ')' ClassBodyopt
;

ArgumentList ::=
	  Expression
	| ArgumentList ',' Expression
;

ArrayCreationWithoutArrayInitializer ::=
	  kw_new PrimitiveType DimWithOrWithOutExprs
	| kw_new ClassOrInterfaceType DimWithOrWithOutExprs
;

ArrayCreationWithArrayInitializer ::=
	  kw_new PrimitiveType DimWithOrWithOutExprs ArrayInitializer
	| kw_new ClassOrInterfaceType DimWithOrWithOutExprs ArrayInitializer
;

DimWithOrWithOutExprs ::=
	  DimWithOrWithOutExpr
	| DimWithOrWithOutExprs DimWithOrWithOutExpr
;

DimWithOrWithOutExpr ::=
	  '[' Expression? ']' ;

Dims ::=
	  ('[' ']')+ ;

FieldAccess ::=
	  Primary '.' Identifier
	| kw_super '.' Identifier
;

MethodInvocation ::=
	  QualifiedIdentifier ('.' TypeArguments Identifier)? '(' ArgumentListopt ')'
	| Primary '.' TypeArguments? Identifier '(' ArgumentListopt ')'
	| kw_super '.' TypeArguments? Identifier '(' ArgumentListopt ')'
;

ArrayAccess ::=
	  QualifiedIdentifier '[' Expression ']'
	| PrimaryNoNewArray '[' Expression ']'
	| ArrayCreationWithArrayInitializer '[' Expression ']'
;

PostfixExpression ::=
	  Primary
	| QualifiedIdentifier
	| PostIncrementExpression
	| PostDecrementExpression
;

PostIncrementExpression ::=
	  PostfixExpression '++' ;

PostDecrementExpression ::=
	  PostfixExpression '--' ;

UnaryExpression ::=
	  PreIncrementExpression
	| PreDecrementExpression
	| '+' UnaryExpression
	| '-' UnaryExpression
	| UnaryExpressionNotPlusMinus
;

PreIncrementExpression ::=
	  '++' UnaryExpression ;

PreDecrementExpression ::=
	  '--' UnaryExpression ;

UnaryExpressionNotPlusMinus ::=
	  PostfixExpression
	| '~' UnaryExpression
	| '!' UnaryExpression
	| CastExpression
;

CastExpression ::=
	  '(' PrimitiveType Dimsopt ')' UnaryExpression
	| '(' QualifiedIdentifier TypeArguments Dimsopt ')' UnaryExpressionNotPlusMinus
	| '(' QualifiedIdentifier TypeArguments '.' ClassOrInterfaceType Dimsopt ')' UnaryExpressionNotPlusMinus
	| '(' QualifiedIdentifier ')' UnaryExpressionNotPlusMinus
	| '(' QualifiedIdentifier Dims ')' UnaryExpressionNotPlusMinus
;

MultiplicativeExpression ::=
	  UnaryExpression
	| MultiplicativeExpression '*' UnaryExpression
	| MultiplicativeExpression '/' UnaryExpression
	| MultiplicativeExpression '%' UnaryExpression
;

AdditiveExpression ::=
	  MultiplicativeExpression
	| AdditiveExpression '+' MultiplicativeExpression
	| AdditiveExpression '-' MultiplicativeExpression
;

ShiftExpression ::=
	  AdditiveExpression
	| ShiftExpression '<<' AdditiveExpression
	| ShiftExpression '>>' AdditiveExpression
	| ShiftExpression '>>>' AdditiveExpression
;

RelationalExpression ::=
	  ShiftExpression
	| RelationalExpression '<' ShiftExpression
	| RelationalExpression '>' ShiftExpression
	| RelationalExpression '<=' ShiftExpression
	| RelationalExpression '>=' ShiftExpression
;

InstanceofExpression ::=
	  RelationalExpression
	| InstanceofExpression kw_instanceof ReferenceType
;

EqualityExpression ::=
	  InstanceofExpression
	| EqualityExpression '==' InstanceofExpression
	| EqualityExpression '!=' InstanceofExpression
;

AndExpression ::=
	  EqualityExpression
	| AndExpression '&' EqualityExpression
;

ExclusiveOrExpression ::=
	  AndExpression
	| ExclusiveOrExpression '^' AndExpression
;

InclusiveOrExpression ::=
	  ExclusiveOrExpression
	| InclusiveOrExpression '|' ExclusiveOrExpression
;

ConditionalAndExpression ::=
	  InclusiveOrExpression
	| ConditionalAndExpression '&&' InclusiveOrExpression
;

ConditionalOrExpression ::=
	  ConditionalAndExpression
	| ConditionalOrExpression '||' ConditionalAndExpression
;

ConditionalExpression ::=
	  ConditionalOrExpression
	| ConditionalOrExpression '?' Expression ':' ConditionalExpression
;

AssignmentExpression ::=
	  ConditionalExpression
	| Assignment
;

Assignment ::=
	  PostfixExpression AssignmentOperator AssignmentExpression ;

AssignmentOperator ::=
	  '='
	| '*='
	| '/='
	| '%='
	| '+='
	| '-='
	| '<<='
	| '>>='
	| '>>>='
	| '&='
	| '^='
	| '|='
;

Expression ::=
	  AssignmentExpression ;

ConstantExpression ::=
	  Expression ;

EnumBody ::=
	  '{' (EnumConstant separator ',')+? ','? (';' ClassBodyDeclarationsopt)? '}' ;

EnumConstant ::=
	  Modifiersopt Identifier ('(' ArgumentListopt ')')? ClassBodyopt ;

TypeArguments ::=
	  '<' TypeArgumentList '>'
	| '<' (TypeArgumentList ',')? DeeperTypeArgument '>>'
	| '<' (TypeArgumentList ',')? ('?' (kw_extends|kw_super))? ClassOrInterface '<' (TypeArgumentList ',')? DeeperTypeArgument '>>>'
;

TypeArgumentList ::=
	  TypeArgument
	| TypeArgumentList ',' TypeArgument
;

TypeArgument ::=
	  ReferenceType
	| Wildcard
;

ReferenceType1 ::=
	  ReferenceType '>'
	| ClassOrInterface '<' TypeArgumentList '>>'
	| ClassOrInterface '<' (TypeArgumentList ',')? DeeperTypeArgument '>>>'
;

Wildcard ::=
	  '?' WildcardBounds? ;

WildcardBounds ::=
	  kw_extends ReferenceType
	| kw_super ReferenceType
;

DeeperTypeArgument ::=
	  ('?' (kw_extends|kw_super))? ClassOrInterface '<' TypeArgumentList ;

TypeParameters ::=
	  '<' (TypeParameterList ',')? TypeParameter1 ;

TypeParameterList ::=
	  TypeParameter
	| TypeParameterList ',' TypeParameter
;

TypeParameter ::=
	  Identifier (kw_extends ReferenceType AdditionalBoundList?)? ;

TypeParameter1 ::=
	  Identifier '>'
	| Identifier kw_extends (ReferenceType AdditionalBoundList? '&')? ReferenceType1
;

AdditionalBoundList ::=
	  AdditionalBound
	| AdditionalBoundList AdditionalBound
;

AdditionalBound ::=
	  '&' ReferenceType ;

PostfixExpression_NotName ::=
	  Primary
	| PostIncrementExpression
	| PostDecrementExpression
;

UnaryExpression_NotName ::=
	  PreIncrementExpression
	| PreDecrementExpression
	| '+' UnaryExpression
	| '-' UnaryExpression
	| UnaryExpressionNotPlusMinus_NotName
;

UnaryExpressionNotPlusMinus_NotName ::=
	  PostfixExpression_NotName
	| '~' UnaryExpression
	| '!' UnaryExpression
	| CastExpression
;

MultiplicativeExpression_NotName ::=
	  UnaryExpression_NotName
	| MultiplicativeExpression_NotName '*' UnaryExpression
	| QualifiedIdentifier '*' UnaryExpression
	| MultiplicativeExpression_NotName '/' UnaryExpression
	| QualifiedIdentifier '/' UnaryExpression
	| MultiplicativeExpression_NotName '%' UnaryExpression
	| QualifiedIdentifier '%' UnaryExpression
;

AdditiveExpression_NotName ::=
	  MultiplicativeExpression_NotName
	| AdditiveExpression_NotName '+' MultiplicativeExpression
	| QualifiedIdentifier '+' MultiplicativeExpression
	| AdditiveExpression_NotName '-' MultiplicativeExpression
	| QualifiedIdentifier '-' MultiplicativeExpression
;

ShiftExpression_NotName ::=
	  AdditiveExpression_NotName
	| ShiftExpression_NotName '<<' AdditiveExpression
	| QualifiedIdentifier '<<' AdditiveExpression
	| ShiftExpression_NotName '>>' AdditiveExpression
	| QualifiedIdentifier '>>' AdditiveExpression
	| ShiftExpression_NotName '>>>' AdditiveExpression
	| QualifiedIdentifier '>>>' AdditiveExpression
;

RelationalExpression_NotName ::=
	  ShiftExpression_NotName
	| ShiftExpression_NotName '<' ShiftExpression
	| QualifiedIdentifier '<' ShiftExpression
	| ShiftExpression_NotName '>' ShiftExpression
	| QualifiedIdentifier '>' ShiftExpression
	| RelationalExpression_NotName '<=' ShiftExpression
	| QualifiedIdentifier '<=' ShiftExpression
	| RelationalExpression_NotName '>=' ShiftExpression
	| QualifiedIdentifier '>=' ShiftExpression
;

InstanceofExpression_NotName ::=
	  RelationalExpression_NotName
	| QualifiedIdentifier kw_instanceof ReferenceType
	| InstanceofExpression_NotName kw_instanceof ReferenceType
;

EqualityExpression_NotName ::=
	  InstanceofExpression_NotName
	| EqualityExpression_NotName '==' InstanceofExpression
	| QualifiedIdentifier '==' InstanceofExpression
	| EqualityExpression_NotName '!=' InstanceofExpression
	| QualifiedIdentifier '!=' InstanceofExpression
;

AndExpression_NotName ::=
	  EqualityExpression_NotName
	| AndExpression_NotName '&' EqualityExpression
	| QualifiedIdentifier '&' EqualityExpression
;

ExclusiveOrExpression_NotName ::=
	  AndExpression_NotName
	| ExclusiveOrExpression_NotName '^' AndExpression
	| QualifiedIdentifier '^' AndExpression
;

InclusiveOrExpression_NotName ::=
	  ExclusiveOrExpression_NotName
	| InclusiveOrExpression_NotName '|' ExclusiveOrExpression
	| QualifiedIdentifier '|' ExclusiveOrExpression
;

ConditionalAndExpression_NotName ::=
	  InclusiveOrExpression_NotName
	| ConditionalAndExpression_NotName '&&' InclusiveOrExpression
	| QualifiedIdentifier '&&' InclusiveOrExpression
;

ConditionalOrExpression_NotName ::=
	  ConditionalAndExpression_NotName
	| ConditionalOrExpression_NotName '||' ConditionalAndExpression
	| QualifiedIdentifier '||' ConditionalAndExpression
;

ConditionalExpression_NotName ::=
	  ConditionalOrExpression_NotName
	| ConditionalOrExpression_NotName '?' Expression ':' ConditionalExpression
	| QualifiedIdentifier '?' Expression ':' ConditionalExpression
;

AssignmentExpression_NotName ::=
	  ConditionalExpression_NotName
	| Assignment
;

Expression_NotName ::=
	  AssignmentExpression_NotName ;

AnnotationTypeBody ::=
	  '{' AnnotationTypeMemberDeclaration* '}' ;

AnnotationTypeMemberDeclaration ::=
	  Modifiersopt TypeParameters? Type Identifier '(' (FormalParameter separator ',')* ')' Dimsopt DefaultValueopt ';'
	| ConstantDeclaration
	| ConstructorDeclaration
	| TypeDeclaration
;

DefaultValue ::=
	  kw_default MemberValue ;

Annotation ::=
	  '@' QualifiedIdentifier
	| '@' QualifiedIdentifier '(' MemberValue ')'
	| '@' QualifiedIdentifier '(' (MemberValuePair separator ',')* ')'
;

MemberValuePair ::=
	  Identifier '=' MemberValue ;

MemberValue ::=
	  ConditionalExpression_NotName
	| QualifiedIdentifier
	| Annotation
	| MemberValueArrayInitializer
;

MemberValueArrayInitializer ::=
	  '{' (MemberValue separator ',')+? ','? '}' ;
