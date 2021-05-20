/* Creator: John Kucera (originated from Dr. Duane J. Jarc)
* Filename: values.cc (for Project 3)
* Date: April 22, 2021
* Code Description: values.cc is the file that contains functions that will
* evaluate operations in the input programs such as arithmetic operators,
* relational operators, reduction, and conditional expressions.
*/

#include <string>
#include <vector>
#include <cmath>

using namespace std;

#include "values.h"
#include "listing.h"

bool caseMet = false;
double caseExpression;
double caseResult = 0;

double evaluateReduction(Operators operator_, double head, double tail)
{
	if (operator_ == ADD)
		return head + tail;

	else if (operator_ == MULTIPLY)
		return head * tail;

	return head * tail;
}


double evaluateRelational(double left, Operators operator_, double right)
{
	double result;
	switch (operator_)
	{
		case LESS:
			result = left < right;
			break;
		case GREATER:
			result = left > right;
			break;
		case LESS_EQUAL:
			result = left <= right;
			break;
		case GREATER_EQUAL:
			result = left >= right;
			break;
		case EQUAL:
			result = left == right;
			break;
		case NOT_EQUAL:
			result = left != right;
			break;
	}
	return result;
}

double evaluateArithmetic(double left, Operators operator_, double right)
{
	double result;
	switch (operator_)
	{
		case ADD:
			result = left + right;
			break;
		case SUBTRACT:
			result = left - right;
			break;
		case MULTIPLY:
			result = left * right;
			break;
		case DIVIDE:
			result = left / right;
			break;
		case REM:
			result = fmod(left, right);
			break;
		case EXP:
			result = pow(left * 1.0, right * 1.0);
			break;
		case ARROW:
			left = right;
			result = left;
			break;
	}
	return result;
}

double evaluateIfElse(double expression, double ifStatement, double elseStatement)
{
	double result;
	if (expression == 1) {
		result = ifStatement;
	}
	else if (expression == 0) {
		result = elseStatement;
	}
	return result;
}

void storeCaseExpression(double expression)
{
	caseExpression = expression;
}

double evaluateCase(double whenStatement, double arrowStatement)
{
	if (whenStatement == caseExpression) {
		caseResult = arrowStatement;
		caseMet = true;
	}
	return caseResult;
}

double evaluateOthers(double arrowStatement)
{
	if (caseMet == false) {
		caseResult = arrowStatement;
	}
	caseMet = false;
	return caseResult;
}
