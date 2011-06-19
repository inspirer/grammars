# js

WhiteSpace :: (lexem)
	/[\t\u000b\u000c \u00a0\ufeff\p{Zs}]/

LineTerminator :: (lexem)
	/[\n\r\u2028\u2029]/

Comment :: (lexem)
	/\/\*(([^\*]*\*+[^\*\/])*([^\*]*\**)?)?\*\//
	/\/\/[^\n\r\u2028\u2029]*/

Identifier :: (lexem)
	/<unknown org.textway.tools.converter.spec.SSetDiff@707efa96 >/

IdentifierName :: (lexem)
	/(\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\p{Nl}|$|_|\\(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))((\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\p{Nl}|$|_|\\(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|\p{Mn}|\p{Mc}|\p{Nd}|\p{Pc}|\u200c|\u200d)*/

break :: (lexem)
	/break/

do :: (lexem)
	/do/

instanceof :: (lexem)
	/instanceof/

typeof :: (lexem)
	/typeof/

case :: (lexem)
	/case/

else :: (lexem)
	/else/

new :: (lexem)
	/new/

var :: (lexem)
	/var/

catch :: (lexem)
	/catch/

finally :: (lexem)
	/finally/

return :: (lexem)
	/return/

void :: (lexem)
	/void/

continue :: (lexem)
	/continue/

for :: (lexem)
	/for/

switch :: (lexem)
	/switch/

while :: (lexem)
	/while/

debugger :: (lexem)
	/debugger/

function :: (lexem)
	/function/

this :: (lexem)
	/this/

with :: (lexem)
	/with/

default :: (lexem)
	/default/

if :: (lexem)
	/if/

throw :: (lexem)
	/throw/

delete :: (lexem)
	/delete/

in :: (lexem)
	/in/

try :: (lexem)
	/try/

get :: (lexem)
	/get/

set :: (lexem)
	/set/

class :: (lexem)
	/class/

enum :: (lexem)
	/enum/

extends :: (lexem)
	/extends/

super :: (lexem)
	/super/

const :: (lexem)
	/const/

export :: (lexem)
	/export/

import :: (lexem)
	/import/

{ :: (lexem)
	/\{/

} :: (lexem)
	/\}/

( :: (lexem)
	/\(/

) :: (lexem)
	/\)/

[ :: (lexem)
	/\[/

] :: (lexem)
	/\]/

. :: (lexem)
	/\./

; :: (lexem)
	/;/

, :: (lexem)
	/,/

< :: (lexem)
	/</

> :: (lexem)
	/>/

<= :: (lexem)
	/<=/

>= :: (lexem)
	/>=/

== :: (lexem)
	/==/

!= :: (lexem)
	/!=/

=== :: (lexem)
	/===/

!== :: (lexem)
	/!==/

+ :: (lexem)
	/\+/

- :: (lexem)
	/-/

* :: (lexem)
	/\*/

% :: (lexem)
	/%/

++ :: (lexem)
	/\+\+/

-- :: (lexem)
	/--/

<< :: (lexem)
	/<</

>> :: (lexem)
	/>>/

>>> :: (lexem)
	/>>>/

& :: (lexem)
	/&/

| :: (lexem)
	/\|/

^ :: (lexem)
	/^/

! :: (lexem)
	/!/

~ :: (lexem)
	/~/

&& :: (lexem)
	/&&/

|| :: (lexem)
	/\|\|/

? :: (lexem)
	/\?/

: :: (lexem)
	/:/

= :: (lexem)
	/=/

+= :: (lexem)
	/\+=/

-= :: (lexem)
	/-=/

*= :: (lexem)
	/\*=/

%= :: (lexem)
	/%=/

<<= :: (lexem)
	/<<=/

>>= :: (lexem)
	/>>=/

>>>= :: (lexem)
	/>>>=/

&= :: (lexem)
	/&=/

|= :: (lexem)
	/\|=/

^= :: (lexem)
	/^=/

/ :: (lexem)
	/\//

/= :: (lexem)
	/\/=/

Literal :
	NullLiteral
	BooleanLiteral
	NumericLiteral
	StringLiteral
	RegularExpressionLiteral

NullLiteral :
	null

null :: (lexem)
	/null/

BooleanLiteral :
	true
	false

true :: (lexem)
	/true/

false :: (lexem)
	/false/

NumericLiteral :: (lexem)
	/(0|[1-9][0-9]+?)\.[0-9]+?([Ee][\+\-]?[0-9]+)?/
	/\.[0-9]+([Ee][\+\-]?[0-9]+)?/
	/(0|[1-9][0-9]+?)([Ee][\+\-]?[0-9]+)?/
	/0[Xx][0-9A-Fa-f]+/

StringLiteral :: (lexem)
	/"([^\n\r"\\\u2028\u2029]|\\(["'\\bfnrtv]|[^\n\r"'0-9\\bfnrt-vx\u2028\u2029]|0(?![0-9])|x[0-9A-Fa-f][0-9A-Fa-f]|(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|(\\(\n|\r(?!\n)|\u2028|\u2029|\r\n)))*"/
	/'([^\n\r'\\\u2028\u2029]|\\(["'\\bfnrtv]|[^\n\r"'0-9\\bfnrt-vx\u2028\u2029]|0(?![0-9])|x[0-9A-Fa-f][0-9A-Fa-f]|(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|(\\(\n|\r(?!\n)|\u2028|\u2029|\r\n)))*'/

RegularExpressionLiteral :: (lexem)
	/\/([^\n\r\*\/\[\\\u2028\u2029]|(\\[^\n\r\u2028\u2029])|(\[([^\n\r\\\]\u2028\u2029]|(\\[^\n\r\u2028\u2029]))*\]))([^\n\r\/\[\\\u2028\u2029]|(\\[^\n\r\u2028\u2029])|(\[([^\n\r\\\]\u2028\u2029]|(\\[^\n\r\u2028\u2029]))*\]))*\/((\p{Lu}|\p{Ll}|\p{Lt}|\p{Lm}|\p{Lo}|\p{Nl}|$|_|\\(u[0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f][0-9A-Fa-f]))|\p{Mn}|\p{Mc}|\p{Nd}|\p{Pc}|\u200c|\u200d)*/

PrimaryExpression :
	this
	Identifier
	Literal
	ArrayLiteral
	ObjectLiteral
	( Expression )

ArrayLiteral :
	[ Elisionopt ]
	[ ElementList ]
	[ ElementList , Elisionopt ]

ElementList :
	Elisionopt AssignmentExpression
	ElementList , Elisionopt AssignmentExpression

Elision :
	,
	Elision ,

ObjectLiteral :
	{ }
	{ PropertyNameAndValueList }
	{ PropertyNameAndValueList , }

PropertyNameAndValueList :
	PropertyAssignment
	PropertyNameAndValueList , PropertyAssignment

PropertyAssignment :
	PropertyName : AssignmentExpression
	get PropertyName ( ) { FunctionBody }
	set PropertyName ( PropertySetParameterList ) { FunctionBody }

PropertyName :
	IdentifierName
	StringLiteral
	NumericLiteral

PropertySetParameterList :
	Identifier

MemberExpression :
	PrimaryExpression
	FunctionExpression
	MemberExpression [ Expression ]
	MemberExpression . IdentifierName
	new MemberExpression Arguments

NewExpression :
	MemberExpression
	new NewExpression

CallExpression :
	MemberExpression Arguments
	CallExpression Arguments
	CallExpression [ Expression ]
	CallExpression . IdentifierName

Arguments :
	( )
	( ArgumentList )

ArgumentList :
	AssignmentExpression
	ArgumentList , AssignmentExpression

LeftHandSideExpression :
	NewExpression
	CallExpression

PostfixExpression :
	LeftHandSideExpression
	LeftHandSideExpression [no LineTerminator here] ++
	LeftHandSideExpression [no LineTerminator here] --

UnaryExpression :
	PostfixExpression
	delete UnaryExpression
	void UnaryExpression
	typeof UnaryExpression
	++ UnaryExpression
	-- UnaryExpression
	+ UnaryExpression
	- UnaryExpression
	~ UnaryExpression
	! UnaryExpression

MultiplicativeExpression :
	UnaryExpression
	MultiplicativeExpression * UnaryExpression
	MultiplicativeExpression / UnaryExpression
	MultiplicativeExpression % UnaryExpression

AdditiveExpression :
	MultiplicativeExpression
	AdditiveExpression + MultiplicativeExpression
	AdditiveExpression - MultiplicativeExpression

ShiftExpression :
	AdditiveExpression
	ShiftExpression << AdditiveExpression
	ShiftExpression >> AdditiveExpression
	ShiftExpression >>> AdditiveExpression

RelationalExpression :
	ShiftExpression
	RelationalExpression < ShiftExpression
	RelationalExpression > ShiftExpression
	RelationalExpression <= ShiftExpression
	RelationalExpression >= ShiftExpression
	RelationalExpression instanceof ShiftExpression
	RelationalExpression in ShiftExpression

RelationalExpressionNoIn :
	ShiftExpression
	RelationalExpressionNoIn < ShiftExpression
	RelationalExpressionNoIn > ShiftExpression
	RelationalExpressionNoIn <= ShiftExpression
	RelationalExpressionNoIn >= ShiftExpression
	RelationalExpressionNoIn instanceof ShiftExpression

EqualityExpression :
	RelationalExpression
	EqualityExpression == RelationalExpression
	EqualityExpression != RelationalExpression
	EqualityExpression === RelationalExpression
	EqualityExpression !== RelationalExpression

EqualityExpressionNoIn :
	RelationalExpressionNoIn
	EqualityExpressionNoIn == RelationalExpressionNoIn
	EqualityExpressionNoIn != RelationalExpressionNoIn
	EqualityExpressionNoIn === RelationalExpressionNoIn
	EqualityExpressionNoIn !== RelationalExpressionNoIn

BitwiseANDExpression :
	EqualityExpression
	BitwiseANDExpression & EqualityExpression

BitwiseANDExpressionNoIn :
	EqualityExpressionNoIn
	BitwiseANDExpressionNoIn & EqualityExpressionNoIn

BitwiseXORExpression :
	BitwiseANDExpression
	BitwiseXORExpression ^ BitwiseANDExpression

BitwiseXORExpressionNoIn :
	BitwiseANDExpressionNoIn
	BitwiseXORExpressionNoIn ^ BitwiseANDExpressionNoIn

BitwiseORExpression :
	BitwiseXORExpression
	BitwiseORExpression | BitwiseXORExpression

BitwiseORExpressionNoIn :
	BitwiseXORExpressionNoIn
	BitwiseORExpressionNoIn | BitwiseXORExpressionNoIn

LogicalANDExpression :
	BitwiseORExpression
	LogicalANDExpression && BitwiseORExpression

LogicalANDExpressionNoIn :
	BitwiseORExpressionNoIn
	LogicalANDExpressionNoIn && BitwiseORExpressionNoIn

LogicalORExpression :
	LogicalANDExpression
	LogicalORExpression || LogicalANDExpression

LogicalORExpressionNoIn :
	LogicalANDExpressionNoIn
	LogicalORExpressionNoIn || LogicalANDExpressionNoIn

ConditionalExpression :
	LogicalORExpression
	LogicalORExpression ? AssignmentExpression : AssignmentExpression

ConditionalExpressionNoIn :
	LogicalORExpressionNoIn
	LogicalORExpressionNoIn ? AssignmentExpressionNoIn : AssignmentExpressionNoIn

AssignmentExpression :
	ConditionalExpression
	LeftHandSideExpression AssignmentOperator AssignmentExpression

AssignmentExpressionNoIn :
	ConditionalExpressionNoIn
	LeftHandSideExpression AssignmentOperator AssignmentExpressionNoIn

AssignmentOperator :
	=
	*=
	/=
	%=
	+=
	-=
	<<=
	>>=
	>>>=
	&=
	^=
	|=

Expression :
	AssignmentExpression
	Expression , AssignmentExpression

ExpressionNoIn :
	AssignmentExpressionNoIn
	ExpressionNoIn , AssignmentExpressionNoIn

Statement :
	Block
	VariableStatement
	EmptyStatement
	ExpressionStatement
	IfStatement
	IterationStatement
	ContinueStatement
	BreakStatement
	ReturnStatement
	WithStatement
	LabelledStatement
	SwitchStatement
	ThrowStatement
	TryStatement
	DebuggerStatement

Block :
	{ StatementListopt }

StatementList :
	Statement
	StatementList Statement

VariableStatement :
	var VariableDeclarationList ;

VariableDeclarationList :
	VariableDeclaration
	VariableDeclarationList , VariableDeclaration

VariableDeclarationListNoIn :
	VariableDeclarationNoIn
	VariableDeclarationListNoIn , VariableDeclarationNoIn

VariableDeclaration :
	Identifier Initialiseropt

VariableDeclarationNoIn :
	Identifier InitialiserNoInopt

Initialiser :
	= AssignmentExpression

InitialiserNoIn :
	= AssignmentExpressionNoIn

EmptyStatement :
	;

ExpressionStatement :
	[lookahead != { function] Expression ;

IfStatement :
	if ( Expression ) Statement else Statement
	if ( Expression ) Statement

IterationStatement :
	do Statement while ( Expression ) ;
	while ( Expression ) Statement
	for ( ExpressionNoInopt ; Expressionopt ; Expressionopt ) Statement
	for ( var VariableDeclarationListNoIn ; Expressionopt ; Expressionopt ) Statement
	for ( LeftHandSideExpression in Expression ) Statement
	for ( var VariableDeclarationNoIn in Expression ) Statement

ContinueStatement :
	continue [no LineTerminator here] Identifieropt ;

BreakStatement :
	break [no LineTerminator here] Identifieropt ;

ReturnStatement :
	return [no LineTerminator here] Expressionopt ;

WithStatement :
	with ( Expression ) Statement

SwitchStatement :
	switch ( Expression ) CaseBlock

CaseBlock :
	{ CaseClausesopt }
	{ CaseClausesopt DefaultClause CaseClausesopt }

CaseClauses :
	CaseClause
	CaseClauses CaseClause

CaseClause :
	case Expression : StatementListopt

DefaultClause :
	default : StatementListopt

LabelledStatement :
	Identifier : Statement

ThrowStatement :
	throw [no LineTerminator here] Expression ;

TryStatement :
	try Block Catch
	try Block Finally
	try Block Catch Finally

Catch :
	catch ( Identifier ) Block

Finally :
	finally Block

DebuggerStatement :
	debugger ;

FunctionDeclaration :
	function Identifier ( FormalParameterListopt ) { FunctionBody }

FunctionExpression :
	function Identifieropt ( FormalParameterListopt ) { FunctionBody }

FormalParameterList :
	Identifier
	FormalParameterList , Identifier

FunctionBody :
	SourceElementsopt

Program :
	SourceElementsopt

SourceElements :
	SourceElement
	SourceElements SourceElement

SourceElement :
	Statement
	FunctionDeclaration

