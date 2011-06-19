# js

WhiteSpace: /[\t\u000b\u000c \u00a0\ufeff\p{Zs}]/
LineTerminator: /[\n\r\u2028\u2029]/
Comment: /\/\*(([^\*]*\*+[^\*\/])*([^\*]*\**)?)?\*\//
Comment: /\/\/[^\n\r\u2028\u2029]*/

Identifier: /<unknown org.textway.tools.converter.spec.SSetDiff@2c61ec49 >/
IdentifierName: /(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\p{Nl}|$|_|\\(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))((\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\p{Nl}|$|_|\\(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|\p{Mn}|\p{Mc}|\p{Nd}|\p{Pc}|\u200c|\u200d)*/
break: /break/
do: /do/
instanceof: /instanceof/
typeof: /typeof/
case: /case/
else: /else/
new: /new/
var: /var/
catch: /catch/
finally: /finally/
return: /return/
void: /void/
continue: /continue/
for: /for/
switch: /switch/
while: /while/
debugger: /debugger/
function: /function/
this: /this/
with: /with/
default: /default/
if: /if/
throw: /throw/
delete: /delete/
in: /in/
try: /try/
get: /get/
set: /set/
class: /class/
enum: /enum/
extends: /extends/
super: /super/
const: /const/
export: /export/
import: /import/
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
null: /null/
true: /true/
false: /false/
NumericLiteral: /(0|[1-9][0-9]+?)\.[0-9]+?([Ee][\+\-]?[0-9]+)?/
NumericLiteral: /\.[0-9]+([Ee][\+\-]?[0-9]+)?/
NumericLiteral: /(0|[1-9][0-9]+?)([Ee][\+\-]?[0-9]+)?/
NumericLiteral: /0[Xx][0-9A-Fa-f]+/

StringLiteral: /"([^\n\r"\\\u2028\u2029]|\\(["'\\bfnrtv]|[^\n\r"'0-9\\bfnrt-vx\u2028\u2029]|0(?![0-9])|x[0-9A-Fa-f][0-9A-Fa-f]|(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|(\\(\n|\r(?!\n)|\u2028|\u2029|\r\n)))*"/
StringLiteral: /'([^\n\r'\\\u2028\u2029]|\\(["'\\bfnrtv]|[^\n\r"'0-9\\bfnrt-vx\u2028\u2029]|0(?![0-9])|x[0-9A-Fa-f][0-9A-Fa-f]|(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|(\\(\n|\r(?!\n)|\u2028|\u2029|\r\n)))*'/

RegularExpressionLiteral: /\/([^\n\r\*\/\[\\\u2028\u2029]|(\\[^\n\r\u2028\u2029])|(\[([^\n\r\\\]\u2028\u2029]|(\\[^\n\r\u2028\u2029]))*\]))([^\n\r\/\[\\\u2028\u2029]|(\\[^\n\r\u2028\u2029])|(\[([^\n\r\\\]\u2028\u2029]|(\\[^\n\r\u2028\u2029]))*\]))*\/((\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\p{Nl}|$|_|\\(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|\p{Mn}|\p{Mc}|\p{Nd}|\p{Pc}|\u200c|\u200d)*/

# parser

Literal ::=
	  NullLiteral
	| BooleanLiteral
	| NumericLiteral
	| StringLiteral
	| RegularExpressionLiteral
;

NullLiteral ::=
	  null
;

BooleanLiteral ::=
	  true
	| false
;

PrimaryExpression ::=
	  this
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
	| get PropertyName '(' ')' '{' FunctionBody '}'
	| set PropertyName '(' PropertySetParameterList ')' '{' FunctionBody '}'
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
	| new MemberExpression Arguments
;

NewExpression ::=
	  MemberExpression
	| new NewExpression
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
	| LeftHandSideExpression [no LineTerminator here] '++'
	| LeftHandSideExpression [no LineTerminator here] '--'
;

UnaryExpression ::=
	  PostfixExpression
	| delete UnaryExpression
	| void UnaryExpression
	| typeof UnaryExpression
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
	| RelationalExpression instanceof ShiftExpression
	| RelationalExpression in ShiftExpression
;

RelationalExpressionNoIn ::=
	  ShiftExpression
	| RelationalExpressionNoIn '<' ShiftExpression
	| RelationalExpressionNoIn '>' ShiftExpression
	| RelationalExpressionNoIn '<=' ShiftExpression
	| RelationalExpressionNoIn '>=' ShiftExpression
	| RelationalExpressionNoIn instanceof ShiftExpression
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
	  var VariableDeclarationList ';'
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
	  [lookahead != '{' function] Expression ';'
;

IfStatement ::=
	  if '(' Expression ')' Statement else Statement
	| if '(' Expression ')' Statement
;

IterationStatement ::=
	  do Statement while '(' Expression ')' ';'
	| while '(' Expression ')' Statement
	| for '(' ExpressionNoInopt ';' Expressionopt ';' Expressionopt ')' Statement
	| for '(' var VariableDeclarationListNoIn ';' Expressionopt ';' Expressionopt ')' Statement
	| for '(' LeftHandSideExpression in Expression ')' Statement
	| for '(' var VariableDeclarationNoIn in Expression ')' Statement
;

ContinueStatement ::=
	  continue [no LineTerminator here] Identifieropt ';'
;

BreakStatement ::=
	  break [no LineTerminator here] Identifieropt ';'
;

ReturnStatement ::=
	  return [no LineTerminator here] Expressionopt ';'
;

WithStatement ::=
	  with '(' Expression ')' Statement
;

SwitchStatement ::=
	  switch '(' Expression ')' CaseBlock
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
	  case Expression ':' StatementListopt
;

DefaultClause ::=
	  default ':' StatementListopt
;

LabelledStatement ::=
	  Identifier ':' Statement
;

ThrowStatement ::=
	  throw [no LineTerminator here] Expression ';'
;

TryStatement ::=
	  try Block Catch
	| try Block Finally
	| try Block Catch Finally
;

Catch ::=
	  catch '(' Identifier ')' Block
;

Finally ::=
	  finally Block
;

DebuggerStatement ::=
	  debugger ';'
;

FunctionDeclaration ::=
	  function Identifier '(' FormalParameterListopt ')' '{' FunctionBody '}'
;

FunctionExpression ::=
	  function Identifieropt '(' FormalParameterListopt ')' '{' FunctionBody '}'
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

