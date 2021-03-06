/* Creator: John Kucera (originated from Dr. Duane J. Jarc)
* Filename: scanner.l (for Project 1)
* Date: March 23, 2021
* Code Description: scanner.l is the input file for flex that will generate
* the lexical analyzer. It defines allowed regular expressions, establishes
* translation rules for lexemes/tokens/reserved words, and the main function
* definition.
*/

%{
#include <cstdio>
#include <string>

using namespace std;

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
"<"		          { ECHO; return(RELOP); }
"="             { ECHO; return(RELOP); }
"/="            { ECHO; return(RELOP); }
">"             { ECHO; return(RELOP); }
">="            { ECHO; return(RELOP); }
"<="            { ECHO; return(RELOP); }
"+"		          { ECHO; return(ADDOP); }
"-"		          { ECHO; return(ADDOP); }
"*"		          { ECHO; return(MULOP); }
"/"		          { ECHO; return(MULOP); }
"=>"            { ECHO; return(ARROW); }
rem             { ECHO; return(REMOP); }
"**"            { ECHO; return(EXPOP); }
begin	      	  { ECHO; return(BEGIN_); }
boolean	      	{ ECHO; return(BOOLEAN); }
true            { ECHO; return(BOOL_LITERAL); }
false           { ECHO; return(BOOL_LITERAL); }
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
{id}		        { ECHO; return(IDENTIFIER);}
{int}		        { ECHO; return(INT_LITERAL); }
{punc}		      { ECHO; return(yytext[0]); }
{real}          { ECHO; return(REAL_LITERAL);}
.		            { ECHO; appendError(LEXICAL, yytext); }
%%

int main()
{
	firstLine();

	FILE *file = fopen("lexemes.txt", "wa");
	int token = yylex();
	while (token)
	{
		fprintf(file, "%d %s\n", token, yytext);
		token = yylex();
	}
	lastLine();
	fclose(file);
	return 0;
}
