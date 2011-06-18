# js

SourceCharacter ::
	[anychar]

WhiteSpace :: (lexem)
	<TAB>
	<VT>
	<FF>
	<SP>
	<NBSP>
	<BOM>
	<Zs>

LineTerminator :: (lexem)
	<LF>
	<CR>
	<LS>
	<PS>

LineTerminatorSequence ::
	<LF>
	<CR> [lookahead != <LF>]
	<LS>
	<PS>
	<CR> <LF>

Comment :: (lexem)
	MultiLineComment
	SingleLineComment

MultiLineComment ::
	/ * MultiLineCommentCharsopt * /

MultiLineCommentChars ::
	MultiLineNotAsteriskChar MultiLineCommentCharsopt
	* PostAsteriskCommentCharsopt

PostAsteriskCommentChars ::
	MultiLineNotForwardSlashOrAsteriskChar MultiLineCommentCharsopt
	* PostAsteriskCommentCharsopt

MultiLineNotAsteriskChar ::
	SourceCharacter but not *

MultiLineNotForwardSlashOrAsteriskChar ::
	SourceCharacter but not / or *

SingleLineComment ::
	/ / SingleLineCommentCharsopt

SingleLineCommentChars ::
	SingleLineCommentChar SingleLineCommentCharsopt

SingleLineCommentChar ::
	SourceCharacter but not LineTerminator

Identifier :: (lexem)
	IdentifierName but not ReservedWord

IdentifierName :: (lexem)
	IdentifierStart
	IdentifierName IdentifierPart

IdentifierStart ::
	UnicodeLetter
	$
	_
	\ UnicodeEscapeSequence

IdentifierPart ::
	IdentifierStart
	UnicodeCombiningMark
	UnicodeDigit
	UnicodeConnectorPunctuation
	<ZWNJ>
	<ZWJ>

UnicodeLetter ::
	<Lu> or <Ll> or <Lt> or <Lm> or <Lo> or <Nl>

UnicodeCombiningMark ::
	<Mn> or <Mc>

UnicodeDigit ::
	<Nd>

UnicodeConnectorPunctuation ::
	<Pc>

ReservedWord ::
	Keyword
	FutureReservedWord
	NullLiteral
	BooleanLiteral

Keyword :: one of
	break	do	instanceof	typeof
	case	else	new	var
	catch	finally	return	void
	continue	for	switch	while
	debugger	function	this	with
	default	if	throw	delete
	in	try	get	set

break :: (lexem)
	b r e a k

do :: (lexem)
	d o

instanceof :: (lexem)
	i n s t a n c e o f

typeof :: (lexem)
	t y p e o f

case :: (lexem)
	c a s e

else :: (lexem)
	e l s e

new :: (lexem)
	n e w

var :: (lexem)
	v a r

catch :: (lexem)
	c a t c h

finally :: (lexem)
	f i n a l l y

return :: (lexem)
	r e t u r n

void :: (lexem)
	v o i d

continue :: (lexem)
	c o n t i n u e

for :: (lexem)
	f o r

switch :: (lexem)
	s w i t c h

while :: (lexem)
	w h i l e

debugger :: (lexem)
	d e b u g g e r

function :: (lexem)
	f u n c t i o n

this :: (lexem)
	t h i s

with :: (lexem)
	w i t h

default :: (lexem)
	d e f a u l t

if :: (lexem)
	i f

throw :: (lexem)
	t h r o w

delete :: (lexem)
	d e l e t e

in :: (lexem)
	i n

try :: (lexem)
	t r y

get :: (lexem)
	g e t

set :: (lexem)
	s e t

FutureReservedWord :: one of
	class	enum	extends	super
	const	export	import

class :: (lexem)
	c l a s s

enum :: (lexem)
	e n u m

extends :: (lexem)
	e x t e n d s

super :: (lexem)
	s u p e r

const :: (lexem)
	c o n s t

export :: (lexem)
	e x p o r t

import :: (lexem)
	i m p o r t

{ :: (lexem)
	{

} :: (lexem)
	}

( :: (lexem)
	(

) :: (lexem)
	)

[ :: (lexem)
	[

] :: (lexem)
	]

. :: (lexem)
	.

; :: (lexem)
	;

, :: (lexem)
	,

< :: (lexem)
	<

> :: (lexem)
	>

<= :: (lexem)
	< =

>= :: (lexem)
	> =

== :: (lexem)
	= =

!= :: (lexem)
	! =

=== :: (lexem)
	= = =

!== :: (lexem)
	! = =

+ :: (lexem)
	+

- :: (lexem)
	-

* :: (lexem)
	*

% :: (lexem)
	%

++ :: (lexem)
	+ +

-- :: (lexem)
	- -

<< :: (lexem)
	< <

>> :: (lexem)
	> >

>>> :: (lexem)
	> > >

& :: (lexem)
	&

| :: (lexem)
	|

^ :: (lexem)
	^

! :: (lexem)
	!

~ :: (lexem)
	~

&& :: (lexem)
	& &

|| :: (lexem)
	| |

? :: (lexem)
	?

: :: (lexem)
	:

= :: (lexem)
	=

+= :: (lexem)
	+ =

-= :: (lexem)
	- =

*= :: (lexem)
	* =

%= :: (lexem)
	% =

<<= :: (lexem)
	< < =

>>= :: (lexem)
	> > =

>>>= :: (lexem)
	> > > =

&= :: (lexem)
	& =

|= :: (lexem)
	| =

^= :: (lexem)
	^ =

/ :: (lexem)
	/

/= :: (lexem)
	/ =

Literal :
	NullLiteral
	BooleanLiteral
	NumericLiteral
	StringLiteral
	RegularExpressionLiteral

NullLiteral :
	null

null :: (lexem)
	n u l l

BooleanLiteral :
	true
	false

true :: (lexem)
	t r u e

false :: (lexem)
	f a l s e

NumericLiteral :: (lexem)
	DecimalLiteral
	HexIntegerLiteral

DecimalLiteral ::
	DecimalIntegerLiteral . DecimalDigitsopt ExponentPartopt
	. DecimalDigits ExponentPartopt
	DecimalIntegerLiteral ExponentPartopt

DecimalIntegerLiteral ::
	0
	NonZeroDigit DecimalDigitsopt

DecimalDigits ::
	DecimalDigit
	DecimalDigits DecimalDigit

DecimalDigit :: one of
	0	1	2	3	4	5
	6	7	8	9

NonZeroDigit :: one of
	1	2	3	4	5	6
	7	8	9

ExponentPart ::
	ExponentIndicator SignedInteger

ExponentIndicator ::
	e
	E

SignedInteger ::
	DecimalDigits
	+ DecimalDigits
	- DecimalDigits

HexIntegerLiteral ::
	0 x HexDigit
	0 X HexDigit
	HexIntegerLiteral HexDigit

HexDigit :: one of
	0	1	2	3	4	5
	6	7	8	9	a	b
	c	d	e	f	A	B
	C	D	E	F

StringLiteral :: (lexem)
	" DoubleStringCharactersopt "
	' SingleStringCharactersopt '

DoubleStringCharacters ::
	DoubleStringCharacter DoubleStringCharactersopt

SingleStringCharacters ::
	SingleStringCharacter SingleStringCharactersopt

DoubleStringCharacter ::
	SourceCharacter but not " or \ or LineTerminator
	\ EscapeSequence
	LineContinuation

SingleStringCharacter ::
	SourceCharacter but not ' or \ or LineTerminator
	\ EscapeSequence
	LineContinuation

LineContinuation ::
	\ LineTerminatorSequence

EscapeSequence ::
	CharacterEscapeSequence
	0 [lookahead != DecimalDigit]
	HexEscapeSequence
	UnicodeEscapeSequence

CharacterEscapeSequence ::
	SingleEscapeCharacter
	NonEscapeCharacter

SingleEscapeCharacter :: one of
	'	"	\	b	f	n
	r	t	v

NonEscapeCharacter ::
	SourceCharacter but not EscapeCharacter or LineTerminator

EscapeCharacter ::
	SingleEscapeCharacter
	DecimalDigit
	x
	u

HexEscapeSequence ::
	x HexDigit HexDigit

UnicodeEscapeSequence ::
	u HexDigit HexDigit HexDigit HexDigit

RegularExpressionLiteral :: (lexem)
	/ RegularExpressionBody / RegularExpressionFlags

RegularExpressionBody ::
	RegularExpressionFirstChar RegularExpressionChars

RegularExpressionChars ::
	[empty]
	RegularExpressionChars RegularExpressionChar

RegularExpressionFirstChar ::
	RegularExpressionNonTerminator but not * or \ or / or [
	RegularExpressionBackslashSequence
	RegularExpressionClass

RegularExpressionChar ::
	RegularExpressionNonTerminator but not \ or / or [
	RegularExpressionBackslashSequence
	RegularExpressionClass

RegularExpressionBackslashSequence ::
	\ RegularExpressionNonTerminator

RegularExpressionNonTerminator ::
	SourceCharacter but not LineTerminator

RegularExpressionClass ::
	[ RegularExpressionClassChars ]

RegularExpressionClassChars ::
	[empty]
	RegularExpressionClassChars RegularExpressionClassChar

RegularExpressionClassChar ::
	RegularExpressionNonTerminator but not ] or \
	RegularExpressionBackslashSequence

RegularExpressionFlags ::
	[empty]
	RegularExpressionFlags IdentifierPart

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

