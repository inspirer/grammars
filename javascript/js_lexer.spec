# Standard ECMA-262, 5th Edition, December 2009
# ECMAScript Language Specification
#
# http://www.ecma-international.org/publications/standards/Ecma-262.htm

# A.1	Lexical Grammar

SourceCharacter ::
	any Unicode code unit

InputElementDiv ::
	WhiteSpace
	LineTerminator
	Comment
	Token
	DivPunctuator

InputElementRegExp ::
	WhiteSpace
	LineTerminator
	Comment
	Token
	RegularExpressionLiteral

WhiteSpace ::
	<TAB>
	<VT>
	<FF>
	<SP>
	<NBSP>
	<BOM>
	<USP>

LineTerminator ::
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

Comment ::
	MultiLineComment
	SingleLineComment

MultiLineComment ::
	/* MultiLineCommentCharsopt */

MultiLineCommentChars :: 
	MultiLineNotAsteriskChar MultiLineCommentCharsopt 
	* PostAsteriskCommentCharsopt

PostAsteriskCommentChars ::
	MultiLineNotForwardSlashOrAsteriskChar MultiLineCommentCharsopt
	* PostAsteriskCommentCharsopt

MultiLineNotAsteriskChar ::
	SourceCharacter but not asterisk *

MultiLineNotForwardSlashOrAsteriskChar ::
	SourceCharacter but not forward-slash / or asterisk *

SingleLineComment ::
	// SingleLineCommentCharsopt

SingleLineCommentChars ::
	SingleLineCommentChar SingleLineCommentCharsopt

SingleLineCommentChar ::
	SourceCharacter but not LineTerminator

Token ::
	IdentifierName
	Punctuator
	NumericLiteral
	StringLiteral

Identifier ::
	IdentifierName but not ReservedWord

IdentifierName ::
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
	any character in the Unicode categories "Uppercase letter (Lu)", "Lowercase letter (Ll)", "Titlecase letter (Lt)", "Modifier letter (Lm)", "Other letter (Lo)", or "Letter number (Nl)".

UnicodeCombiningMark ::
	any character in the Unicode categories "Non-spacing mark (Mn)" or "Combining spacing mark (Mc)"

UnicodeDigit ::
	any character in the Unicode category "Decimal number (Nd)"

UnicodeConnectorPunctuation ::
	any character in the Unicode category "Connector punctuation (Pc)"

ReservedWord ::
	Keyword
	FutureReservedWord
	NullLiteral
	BooleanLiteral

Keyword :: one of
	break		do			instanceof	typeof
	case		else		new			var
	catch		finally		return		void 
	continue	for			switch		while
	debugger	function	this		with
	default		if			throw
	delete		in			try


FutureReservedWord :: one of
	class		enum		extends		super
	const		export		import
#	or in strict mode code one of
#	implements	let			private		public
#	interface	package		protected	static
#	yield

Punctuator :: one of
	{	}	(	)	[	]
	.	;	,	<	>	<=
	>=	==	!=	===	!==	
	+	-	*	%	++	--
	<<	>>	>>>	&	|	^
	!	~	&&	||	?	:
	=	+=	-=	*=	%=	<<=
	>>=	>>>= &=	|=	^=

DivPunctuator :: one of
	/
	/=

Literal ::
	NullLiteral
	BooleanLiteral
	NumericLiteral
	StringLiteral

NullLiteral ::
	null

BooleanLiteral ::
	true
	false

NumericLiteral ::
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
	0 1 2 3 4 5 6 7 8 9

ExponentIndicator :: one of
	e E

SignedInteger ::
	DecimalDigits
	+ DecimalDigits 
	- DecimalDigits

HexIntegerLiteral ::
	0 x HexDigit 
	0 X HexDigit
	HexIntegerLiteral HexDigit

HexDigit :: one of
	0 1 2 3 4 5 6 7 8 9 a b c d e f A B C D E F

StringLiteral ::
	" DoubleStringCharactersopt "
	' SingleStringCharactersopt '

DoubleStringCharacters ::
	DoubleStringCharacter DoubleStringCharactersopt

SingleStringCharacters ::
	SingleStringCharacter SingleStringCharactersopt

DoubleStringCharacter :: 
	SourceCharacter but not double-quote " or backslash \ or LineTerminator
	\ EscapeSequence
	LineContinuation

SingleStringCharacter :: 
	SourceCharacter but not single-quote ' or backslash \ or LineTerminator
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
	' " \ b f n r t v

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

RegularExpressionLiteral ::
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
	\ NonTerminator

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

