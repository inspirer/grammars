# js

lang = "java"
prefix = "Lapg"
package = "org.textway.lapg.parser"

WhiteSpace: /[\t\u000b\u000c \u00a0\ufeff]/
LineTerminator: /[\n\r\u2028\u2029]/
Comment: /\/\*(([^\*]*\*+[^\*\/])*([^\*]*\**)?)?\*\//
Comment: /\/\/[^\n\r\u2028\u2029]*/

Identifier: /<unknown org.textway.tools.converter.spec.SSetDiff@707efa96 >/
IdentifierName: /($|_|\\(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))(($|_|\\(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|\u200c|\u200d)*/
kw_break: /break/
kw_do: /do/
kw_instanceof: /instanceof/
kw_typeof: /typeof/
kw_case: /case/
kw_else: /else/
kw_new: /new/
kw_var: /var/
kw_catch: /catch/
kw_finally: /finally/
kw_return: /return/
kw_void: /void/
kw_continue: /continue/
kw_for: /for/
kw_switch: /switch/
kw_while: /while/
kw_debugger: /debugger/
kw_function: /function/
kw_this: /this/
kw_with: /with/
kw_default: /default/
kw_if: /if/
kw_throw: /throw/
kw_delete: /delete/
kw_in: /in/
kw_try: /try/
kw_get: /get/
kw_set: /set/
kw_class: /class/
kw_enum: /enum/
kw_extends: /extends/
kw_super: /super/
kw_const: /const/
kw_export: /export/
kw_import: /import/
'{': /\{/
'}': /\}/
'(': /\(/
')': /\)/
'[': /\[/
']': /\]/
'.': /\./
';': /;/
',': /,/
'<': /</
'>': />/
'<=': /<=/
'>=': />=/
'==': /==/
'!=': /!=/
'===': /===/
'!==': /!==/
'+': /\+/
'-': /-/
'*': /\*/
'%': /%/
'++': /\+\+/
'--': /--/
'<<': /<</
'>>': />>/
'>>>': />>>/
'&': /&/
'|': /\|/
'^': /^/
'!': /!/
'~': /~/
'&&': /&&/
'||': /\|\|/
'?': /\?/
':': /:/
'=': /=/
'+=': /\+=/
'-=': /-=/
'*=': /\*=/
'%=': /%=/
'<<=': /<<=/
'>>=': />>=/
'>>>=': />>>=/
'&=': /&=/
'|=': /\|=/
'^=': /^=/
'/': /\//
'/=': /\/=/
kw_null: /null/
kw_true: /true/
kw_false: /false/
NumericLiteral: /(0|[1-9][0-9]+?)\.[0-9]+?([Ee][\+\-]?[0-9]+)?/
NumericLiteral: /\.[0-9]+([Ee][\+\-]?[0-9]+)?/
NumericLiteral: /(0|[1-9][0-9]+?)([Ee][\+\-]?[0-9]+)?/
NumericLiteral: /0[Xx][0-9A-Fa-f]+/

StringLiteral: /"([^\n\r"\\\u2028\u2029]|\\(["'\\bfnrtv]|[^\n\r"'0-9\\bfnrt-vx\u2028\u2029]|0(?![0-9])|x[0-9A-Fa-f][0-9A-Fa-f]|(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|(\\(\n|\r(?!\n)|\u2028|\u2029|\r\n)))*"/
StringLiteral: /'([^\n\r'\\\u2028\u2029]|\\(["'\\bfnrtv]|[^\n\r"'0-9\\bfnrt-vx\u2028\u2029]|0(?![0-9])|x[0-9A-Fa-f][0-9A-Fa-f]|(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|(\\(\n|\r(?!\n)|\u2028|\u2029|\r\n)))*'/

RegularExpressionLiteral: /\/([^\n\r\*\/\[\\\u2028\u2029]|(\\[^\n\r\u2028\u2029])|(\[([^\n\r\\\]\u2028\u2029]|(\\[^\n\r\u2028\u2029]))*\]))([^\n\r\/\[\\\u2028\u2029]|(\\[^\n\r\u2028\u2029])|(\[([^\n\r\\\]\u2028\u2029]|(\\[^\n\r\u2028\u2029]))*\]))*\/(($|_|\\(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|\u200c|\u200d)*/

# parser

Literal ::=
	  NullLiteral
	| BooleanLiteral
	| NumericLiteral
	| StringLiteral
	| RegularExpressionLiteral
;

NullLiteral ::=
	  kw_null
;

BooleanLiteral ::=
	  kw_true
	| kw_false
;

PrimaryExpression ::=
	  kw_this
	| Identifier
	| Literal
	| ArrayLiteral
	| ObjectLiteral
	| '(' Expression ')'
;

ArrayLiteral ::=
	  '[' Elisionopt ']'
	| '[' ElementList ']'
	| '[' ElementList ',' Elisionopt ']'
;

ElementList ::=
	  Elisionopt AssignmentExpression
	| ElementList ',' Elisionopt AssignmentExpression
;

Elision ::=
	  ','
	| Elision ','
;

ObjectLiteral ::=
	  '{' '}'
	| '{' PropertyNameAndValueList '}'
	| '{' PropertyNameAndValueList ',' '}'
;

PropertyNameAndValueList ::=
	  PropertyAssignment
	| PropertyNameAndValueList ',' PropertyAssignment
;

PropertyAssignment ::=
	  PropertyName ':' AssignmentExpression
	| kw_get PropertyName '(' ')' '{' FunctionBody '}'
	| kw_set PropertyName '(' PropertySetParameterList ')' '{' FunctionBody '}'
;

PropertyName ::=
	  IdentifierName
	| StringLiteral
	| NumericLiteral
;

PropertySetParameterList ::=
	  Identifier
;

MemberExpression ::=
	  PrimaryExpression
	| FunctionExpression
	| MemberExpression '[' Expression ']'
	| MemberExpression '.' IdentifierName
	| kw_new MemberExpression Arguments
;

NewExpression ::=
	  MemberExpression
	| kw_new NewExpression
;

CallExpression ::=
	  MemberExpression Arguments
	| CallExpression Arguments
	| CallExpression '[' Expression ']'
	| CallExpression '.' IdentifierName
;

Arguments ::=
	  '(' ')'
	| '(' ArgumentList ')'
;

ArgumentList ::=
	  AssignmentExpression
	| ArgumentList ',' AssignmentExpression
;

LeftHandSideExpression ::=
	  NewExpression
	| CallExpression
;

PostfixExpression ::=
	  LeftHandSideExpression
	| LeftHandSideExpression '++'
	| LeftHandSideExpression '--'
;

UnaryExpression ::=
	  PostfixExpression
	| kw_delete UnaryExpression
	| kw_void UnaryExpression
	| kw_typeof UnaryExpression
	| '++' UnaryExpression
	| '--' UnaryExpression
	| '+' UnaryExpression
	| '-' UnaryExpression
	| '~' UnaryExpression
	| '!' UnaryExpression
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
	| RelationalExpression kw_instanceof ShiftExpression
	| RelationalExpression kw_in ShiftExpression
;

RelationalExpressionNoIn ::=
	  ShiftExpression
	| RelationalExpressionNoIn '<' ShiftExpression
	| RelationalExpressionNoIn '>' ShiftExpression
	| RelationalExpressionNoIn '<=' ShiftExpression
	| RelationalExpressionNoIn '>=' ShiftExpression
	| RelationalExpressionNoIn kw_instanceof ShiftExpression
;

EqualityExpression ::=
	  RelationalExpression
	| EqualityExpression '==' RelationalExpression
	| EqualityExpression '!=' RelationalExpression
	| EqualityExpression '===' RelationalExpression
	| EqualityExpression '!==' RelationalExpression
;

EqualityExpressionNoIn ::=
	  RelationalExpressionNoIn
	| EqualityExpressionNoIn '==' RelationalExpressionNoIn
	| EqualityExpressionNoIn '!=' RelationalExpressionNoIn
	| EqualityExpressionNoIn '===' RelationalExpressionNoIn
	| EqualityExpressionNoIn '!==' RelationalExpressionNoIn
;

BitwiseANDExpression ::=
	  EqualityExpression
	| BitwiseANDExpression '&' EqualityExpression
;

BitwiseANDExpressionNoIn ::=
	  EqualityExpressionNoIn
	| BitwiseANDExpressionNoIn '&' EqualityExpressionNoIn
;

BitwiseXORExpression ::=
	  BitwiseANDExpression
	| BitwiseXORExpression '^' BitwiseANDExpression
;

BitwiseXORExpressionNoIn ::=
	  BitwiseANDExpressionNoIn
	| BitwiseXORExpressionNoIn '^' BitwiseANDExpressionNoIn
;

BitwiseORExpression ::=
	  BitwiseXORExpression
	| BitwiseORExpression '|' BitwiseXORExpression
;

BitwiseORExpressionNoIn ::=
	  BitwiseXORExpressionNoIn
	| BitwiseORExpressionNoIn '|' BitwiseXORExpressionNoIn
;

LogicalANDExpression ::=
	  BitwiseORExpression
	| LogicalANDExpression '&&' BitwiseORExpression
;

LogicalANDExpressionNoIn ::=
	  BitwiseORExpressionNoIn
	| LogicalANDExpressionNoIn '&&' BitwiseORExpressionNoIn
;

LogicalORExpression ::=
	  LogicalANDExpression
	| LogicalORExpression '||' LogicalANDExpression
;

LogicalORExpressionNoIn ::=
	  LogicalANDExpressionNoIn
	| LogicalORExpressionNoIn '||' LogicalANDExpressionNoIn
;

ConditionalExpression ::=
	  LogicalORExpression
	| LogicalORExpression '?' AssignmentExpression ':' AssignmentExpression
;

ConditionalExpressionNoIn ::=
	  LogicalORExpressionNoIn
	| LogicalORExpressionNoIn '?' AssignmentExpressionNoIn ':' AssignmentExpressionNoIn
;

AssignmentExpression ::=
	  ConditionalExpression
	| LeftHandSideExpression AssignmentOperator AssignmentExpression
;

AssignmentExpressionNoIn ::=
	  ConditionalExpressionNoIn
	| LeftHandSideExpression AssignmentOperator AssignmentExpressionNoIn
;

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
	  AssignmentExpression
	| Expression ',' AssignmentExpression
;

ExpressionNoIn ::=
	  AssignmentExpressionNoIn
	| ExpressionNoIn ',' AssignmentExpressionNoIn
;

Statement ::=
	  Block
	| VariableStatement
	| EmptyStatement
	| ExpressionStatement
	| IfStatement
	| IterationStatement
	| ContinueStatement
	| BreakStatement
	| ReturnStatement
	| WithStatement
	| LabelledStatement
	| SwitchStatement
	| ThrowStatement
	| TryStatement
	| DebuggerStatement
;

Block ::=
	  '{' StatementListopt '}'
;

StatementList ::=
	  Statement
	| StatementList Statement
;

VariableStatement ::=
	  kw_var VariableDeclarationList ';'
;

VariableDeclarationList ::=
	  VariableDeclaration
	| VariableDeclarationList ',' VariableDeclaration
;

VariableDeclarationListNoIn ::=
	  VariableDeclarationNoIn
	| VariableDeclarationListNoIn ',' VariableDeclarationNoIn
;

VariableDeclaration ::=
	  Identifier Initialiseropt
;

VariableDeclarationNoIn ::=
	  Identifier InitialiserNoInopt
;

Initialiser ::=
	  '=' AssignmentExpression
;

InitialiserNoIn ::=
	  '=' AssignmentExpressionNoIn
;

EmptyStatement ::=
	  ';'
;

ExpressionStatement ::=
#	  [lookahead != '{' kw_function]
	  Expression ';'
;

IfStatement ::=
	  kw_if '(' Expression ')' Statement kw_else Statement
	| kw_if '(' Expression ')' Statement
;

IterationStatement ::=
	  kw_do Statement kw_while '(' Expression ')' ';'
	| kw_while '(' Expression ')' Statement
	| kw_for '(' ExpressionNoInopt ';' Expressionopt ';' Expressionopt ')' Statement
	| kw_for '(' kw_var VariableDeclarationListNoIn ';' Expressionopt ';' Expressionopt ')' Statement
	| kw_for '(' LeftHandSideExpression kw_in Expression ')' Statement
	| kw_for '(' kw_var VariableDeclarationNoIn kw_in Expression ')' Statement
;

ContinueStatement ::=
	  kw_continue Identifieropt ';'
;

BreakStatement ::=
	  kw_break Identifieropt ';'
;

ReturnStatement ::=
	  kw_return Expressionopt ';'
;

WithStatement ::=
	  kw_with '(' Expression ')' Statement
;

SwitchStatement ::=
	  kw_switch '(' Expression ')' CaseBlock
;

CaseBlock ::=
	  '{' CaseClausesopt '}'
	| '{' CaseClausesopt DefaultClause CaseClausesopt '}'
;

CaseClauses ::=
	  CaseClause
	| CaseClauses CaseClause
;

CaseClause ::=
	  kw_case Expression ':' StatementListopt
;

DefaultClause ::=
	  kw_default ':' StatementListopt
;

LabelledStatement ::=
	  Identifier ':' Statement
;

ThrowStatement ::=
	  kw_throw Expression ';'
;

TryStatement ::=
	  kw_try Block Catch
	| kw_try Block Finally
	| kw_try Block Catch Finally
;

Catch ::=
	  kw_catch '(' Identifier ')' Block
;

Finally ::=
	  kw_finally Block
;

DebuggerStatement ::=
	  kw_debugger ';'
;

FunctionDeclaration ::=
	  kw_function Identifier '(' FormalParameterListopt ')' '{' FunctionBody '}'
;

FunctionExpression ::=
	  kw_function Identifieropt '(' FormalParameterListopt ')' '{' FunctionBody '}'
;

FormalParameterList ::=
	  Identifier
	| FormalParameterList ',' Identifier
;

FunctionBody ::=
	  SourceElementsopt
;

Program ::=
	  SourceElementsopt
;

SourceElements ::=
	  SourceElement
	| SourceElements SourceElement
;

SourceElement ::=
	  Statement
	| FunctionDeclaration
;

%input Program;
