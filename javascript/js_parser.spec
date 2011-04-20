# Standard ECMA-262, 5th Edition, December 2009
# ECMAScript Language Specification
#
# http://www.ecma-international.org/publications/standards/Ecma-262.htm

# A.3 Expressions

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

AssignmentOperator : one of
	=  *=  /=  %=  +=  -=  <<=  >>=  >>>=  &=  ^=  |=

Expression :
	AssignmentExpression
	Expression , AssignmentExpression

ExpressionNoIn :
	AssignmentExpressionNoIn
	ExpressionNoIn , AssignmentExpressionNoIn

# A.4 Statements

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

# A.5	Functions and Programs

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

