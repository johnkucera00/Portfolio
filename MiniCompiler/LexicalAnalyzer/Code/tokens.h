/* Creator: John Kucera (originated from Dr. Duane J. Jarc)
* Filename: tokens.h (for Project 1)
* Date: March 23, 2021
* Code Description: tokens.h is a header file that defines enumerated type
* for tokens returned in scanner.l translation rules.
*/

enum Tokens {RELOP = 256, ADDOP, MULOP, ANDOP, BEGIN_, BOOLEAN, END, ENDREDUCE,
	FUNCTION, INTEGER, IS, REDUCE, RETURNS, IDENTIFIER, INT_LITERAL, ARROW, CASE,
	ELSE, ENDCASE, ENDIF, IF, OTHERS, REAL, THEN, WHEN, OROP, NOTOP, REMOP, EXPOP,
	REAL_LITERAL, BOOL_LITERAL};
