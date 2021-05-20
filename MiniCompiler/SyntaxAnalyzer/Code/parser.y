/* Creator: John Kucera (originated from Dr. Duane J. Jarc)
* Filename: parser.y (for Project 2)
* Date: April 6, 2021
* Code Description: parser.y is the bison input file that will call the lexical
* analyzer created from flex in order to generate a parser. It contains the
* grammar productions, token declarations, and main function to call the parser.
*/

%{

#include <string>

using namespace std;

#include "listing.h"

int yylex();
void yyerror(const char* message);

%}

%define parse.error verbose

%token IDENTIFIER
%token INT_LITERAL REAL_LITERAL BOOL_LITERAL
%token ADDOP MULOP RELOP ANDOP OROP NOTOP REMOP EXPOP
%token BEGIN_ BOOLEAN END ENDREDUCE FUNCTION INTEGER IS REDUCE RETURNS
%token ARROW CASE ELSE ENDCASE ENDIF IF OTHERS REAL THEN WHEN

%%

function:
	function_header optional_variable body ;

function_header:
	FUNCTION IDENTIFIER RETURNS type ';' ; |
  FUNCTION IDENTIFIER parameters RETURNS type ';' |
  error ';' ;

optional_variable:
	optional_variable variable |
  error ';' |
	;

variable:
	IDENTIFIER ':' type IS statement_ ;

parameters:
  parameter optional_parameter ;

optional_parameter:
  optional_parameter ',' parameter |
  ;

parameter:
  IDENTIFIER ':' type ;

type:
	INTEGER |
  REAL |
	BOOLEAN ;

body:
	BEGIN_ statement_ END ';' ;

statement_:
	statement ';' |
	error ';' ;

statement:
	expression |
	REDUCE operator reduction ENDREDUCE |
  IF expression THEN statement_ ELSE statement_ ENDIF |
  CASE expression IS case OTHERS ARROW statement_ ENDCASE ;

operator:
	ADDOP |
	MULOP ;

case:
  case WHEN INT_LITERAL ARROW statement_ |
  ;

reduction:
	reduction statement_ |
	;

expression:
	expression OROP precedence_1 |
	precedence_1 ;

precedence_1:
  precedence_1 ANDOP precedence_2 |
  precedence_2 ;

precedence_2:
	precedence_2 RELOP precedence_3 |
	precedence_3 ;

precedence_3:
	precedence_3 ADDOP precedence_4 |
	precedence_4 ;

precedence_4:
	precedence_4 MULOP precedence_5 |
  precedence_4 REMOP precedence_5 |
	precedence_5 ;

precedence_5:
  precedence_5 EXPOP precedence_6 |
  precedence_5 '(' precedence_6 ')' |
  precedence_6 ;

precedence_6:
  NOTOP primary |
  primary ;

primary:
	'(' expression ')' |
	INT_LITERAL |
  REAL_LITERAL |
  BOOL_LITERAL |
	IDENTIFIER ;

%%

void yyerror(const char* message)
{
	appendError(SYNTAX, message);
}

int main(int argc, char *argv[])
{
	firstLine();
	yyparse();
	lastLine();
	return 0;
}
