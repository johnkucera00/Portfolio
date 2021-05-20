/* Creator: John Kucera (originated from Dr. Duane J. Jarc)
* Filename: scanner.l (for Project 3)
* Date: April 22, 2021
* Code Description: scanner.l is the input file for flex that will generate
* the lexical analyzer. It defines allowed regular expressions, establishes
* translation rules for lexemes/tokens/reserved words, and the main function
* definition.
*/

%{
#include <string>

using namespace std;

#include "values.h"
#include "listing.h"
#include "tokens.h"

%}

%option noyywrap

ws		        [ \t\r]+
comment		    \-\-.*\n
comment2      \/\/.*\n
line		      [\n]
id		        [A-Za-z](_?[A-Za-z0-9])*
digit		      [0-9]
int		        {digit}+
punc		      [\(\),:;]
real          {digit}+\.{digit}*([Ee][+-]?{digit}+)?
%%

{ws}		        { ECHO; }
{comment}	      { ECHO; nextLine();}
{comment2}      { ECHO; nextLine();}
{line}	     	  { ECHO; nextLine();}
"<"		          { ECHO; yylval.oper = LESS; return(RELOP); }
"="             { ECHO; yylval.oper = EQUAL; return(RELOP); }
"/="            { ECHO; yylval.oper = NOT_EQUAL; return(RELOP); }
">"             { ECHO; yylval.oper = GREATER; return(RELOP); }
">="            { ECHO; yylval.oper = GREATER_EQUAL; return(RELOP); }
"<="            { ECHO; yylval.oper = LESS_EQUAL; return(RELOP); }
"+"		          { ECHO; yylval.oper = ADD; return(ADDOP); }
"-"             { ECHO; yylval.oper = SUBTRACT; return(ADDOP); }
"*"		          { ECHO; yylval.oper = MULTIPLY; return(MULOP); }
"/"		          { ECHO; yylval.oper = DIVIDE; return(MULOP); }
rem             { ECHO; yylval.oper = REM; return(REMOP); }
"**"		        { ECHO; yylval.oper = EXP; return(EXPOP); }
"=>"            { ECHO; yylval.oper = ARROW; return(ARROWOP); }
begin	      	  { ECHO; return(BEGIN_); }
boolean	      	{ ECHO; return(BOOLEAN); }
true            { ECHO; yylval.value = TRUE; return(BOOL_LITERAL);}
false           { ECHO; yylval.value = FALSE; return(BOOL_LITERAL);}
end		          { ECHO; return(END); }
endreduce	      { ECHO; return(ENDREDUCE); }
function	      { ECHO; return(FUNCTION); }
integer	      	{ ECHO; return(INTEGER); }
is		          { ECHO; return(IS); }
reduce	     	  { ECHO; return REDUCE; }
returns	      	{ ECHO; return(RETURNS); }
and		          { ECHO; return(ANDOP); }
or              { ECHO; return(OROP); }
not             { ECHO; return(NOTOP); }
case            { ECHO; return(CASE); }
else            { ECHO; return(ELSE); }
endcase         { ECHO; return(ENDCASE); }
endif           { ECHO; return(ENDIF); }
if              { ECHO; return(IF); }
others          { ECHO; return(OTHERS); }
real            { ECHO; return(REAL); }
then            { ECHO; return(THEN); }
when            { ECHO; return(WHEN); }
{id}		        { ECHO; yylval.iden = (CharPtr)malloc(yyleng + 1);
			           strcpy(yylval.iden, yytext); return(IDENTIFIER);}
{int}		        { ECHO; yylval.value = atoi(yytext); return(INT_LITERAL); }
{punc}		      { ECHO; return(yytext[0]); }
{real}          { ECHO; yylval.value = atof(yytext); return(REAL_LITERAL); }
.		{ ECHO; appendError(LEXICAL, yytext); }

%%
