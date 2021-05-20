/* Creator: John Kucera (originated from Dr. Duane J. Jarc)
* Filename: values.h (for Project 3)
* Date: April 22, 2021
* Code Description: values.h is the file that contains definitions for the
* evaluation functions contained in values.cc.
*/

typedef char* CharPtr;
enum Operators {LESS, GREATER, LESS_EQUAL, GREATER_EQUAL, EQUAL, NOT_EQUAL,
  ADD, SUBTRACT, MULTIPLY, DIVIDE, REM, EXP, ARROW};

double evaluateReduction(Operators operator_, double head, double tail);
double evaluateRelational(double left, Operators operator_, double right);
double evaluateArithmetic(double left, Operators operator_, double right);
double evaluateIfElse(double expression, double ifStatement, double elseStatement);
void storeCaseExpression(double expression);
double evaluateCase(double whenStatement, double arrowStatement);
double evaluateOthers(double arrowStatement);
