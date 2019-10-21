grammar ICSS;

//--- LEXER: ---
// IF support:
IF: 'if';
BOX_BRACKET_OPEN: '[';
BOX_BRACKET_CLOSE: ']';


//Literals
TRUE: 'TRUE';
FALSE: 'FALSE';
PIXELSIZE: [0-9]+ 'px';
PERCENTAGE: [0-9]+ '%';
SCALAR: [0-9]+;

//Color value takes precedence over id idents
COLOR: '#' [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f] [0-9a-f];

//Specific identifiers for id's and css classes
ID_IDENT: '#' [a-z0-9\-]+;
CLASS_IDENT: '.' [a-z0-9\-]+;

//General identifiers
LOWER_IDENT: [a-z] [a-z0-9\-]*;
CAPITAL_IDENT: [A-Z] [A-Za-z0-9_]*;

//All whitespace is skipped
WS: [ \t\r\n]+ -> skip;

//
OPEN_BRACE: '{';
CLOSE_BRACE: '}';
SEMICOLON: ';';
COLON: ':';
PLUS: '+';
MIN: '-';
MUL: '*';
ASSIGNMENT_OPERATOR: ':=';

//--- PARSER: ---

stylesheet: variableAssignment* stylerule* EOF;
stylerule: (tagSelector | idSelector | classSelector) body;
tagSelector: (CAPITAL_IDENT | LOWER_IDENT);
idSelector: ID_IDENT;
classSelector: CLASS_IDENT;
body: OPEN_BRACE declaration* ifclause* CLOSE_BRACE;
declaration : propertyname COLON expression SEMICOLON;
propertyname: LOWER_IDENT;
expression: literal | operation;
literal: (colorLiteral|boolLiteral|percentageLiteral|pixelLiteral|scalarLiteral|variableReference);
colorLiteral: COLOR ;
boolLiteral: TRUE | FALSE;
percentageLiteral: PERCENTAGE;
pixelLiteral: PIXELSIZE;
scalarLiteral: SCALAR;
variableReference: CAPITAL_IDENT;
variableAssignment: variableReference ASSIGNMENT_OPERATOR expression SEMICOLON;
operation : addoperation | multiplyoperation | subtractoperation;
addoperation: literal PLUS expression;
multiplyoperation:literal MUL expression;
subtractoperation: literal MIN expression;

ifclause:IF BOX_BRACKET_OPEN expression BOX_BRACKET_CLOSE body;



