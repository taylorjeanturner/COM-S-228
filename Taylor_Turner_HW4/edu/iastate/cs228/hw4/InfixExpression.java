package edu.iastate.cs228.hw4;

/**
 *  
 * @author Taylor Turner
 *
 */

import java.util.HashMap;

/**
 * 
 * This class represents an infix expression. It implements infix to postfix
 * conversion using one stack, and evaluates the converted postfix expression.
 *
 */

public class InfixExpression extends Expression {
	private String infixExpression; // the infix expression to convert
	private boolean postfixReady = false; // postfix already generated if true
	private int rankTotal = 0; // Keeps track of the cumulative rank of the infix expression.

	private PureStack<Operator> operatorStack; // stack of operators

	/**
	 * Constructor stores the input infix string, and initializes the operand stack
	 * and the hash map.
	 * 
	 * @param st     input infix string.
	 * @param varTbl hash map storing all variables in the infix expression and
	 *               their values.
	 */
	public InfixExpression(String st, HashMap<Character, Integer> varTbl) {
		super("", varTbl);
		operatorStack = new ArrayBasedStack<Operator>();
		infixExpression = st;
	}

	/**
	 * Constructor supplies a default hash map.
	 * 
	 * @param s
	 */
	public InfixExpression(String s) {
		super("");
		infixExpression = s;
		operatorStack = new ArrayBasedStack<Operator>();
	}

	/**
	 * Outputs the infix expression according to the format in the project
	 * description.
	 */
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < infixExpression.length(); i++) {
			char c = infixExpression.charAt(i);
			if (Expression.isInt(Character.toString(c))) {
				int j = i;
				String num = "";
				while (Expression.isInt(Character.toString(infixExpression.charAt(j)))) {
					num += infixExpression.charAt(j);
					j++;
					if (j >= infixExpression.length()) {
						break;
					}
				}
				i += num.length() - 1;
				result += num + " ";
			} else if (Expression.isVariable(c)) {
				result += c + " ";
			} else if (Expression.isOperator(c)) {
				if (c == ')') {
					result = result.substring(0, result.length() - 1);
					result += c + " ";
				} else if (c == '(') {
					result += c;
				} else {
					result += c + " ";
				}
			}
		}
		return result.trim();
	}

	/**
	 * @return equivalent postfix expression, or
	 * 
	 *         a null string if a call to postfix() inside the body (when
	 *         postfixReady == false) throws an exception.
	 */
	public String postfixString() {
		try {
			postfix();
		} catch (ExpressionFormatException e) {
			return null;
		}
		return postfixExpression.toString().trim();
	}

	/**
	 * Resets the infix expression.
	 * 
	 * @param st
	 */
	public void resetInfix(String st) {
		infixExpression = st;
		postfixExpression = "";
		postfixReady = false;
		operatorStack = new ArrayBasedStack<Operator>();
		varTable.clear();
	}

	/**
	 * Converts infix expression to an equivalent postfix string stored at
	 * postfixExpression. If postfixReady == false, the method scans the
	 * infixExpression, and does the following (for algorithm details refer to the
	 * relevant PowerPoint slides):
	 * 
	 * 1. Skips a whitespace character. 2. Writes a scanned operand to
	 * postfixExpression. 3. When an operator is scanned, generates an operator
	 * object. In case the operator is determined to be a unary minus, store the
	 * char '~' in the generated operator object. 4. If the scanned operator has a
	 * higher input precedence than the stack precedence of the top operator on the
	 * operatorStack, push it onto the stack. 5. Otherwise, first calls
	 * outputHigherOrEqual() before pushing the scanned operator onto the stack. No
	 * push if the scanned operator is ). 6. Keeps track of the cumulative rank of
	 * the infix expression.
	 * 
	 * During the conversion, catches errors in the infixExpression by throwing
	 * ExpressionFormatException with one of the following messages:
	 * 
	 * -- "Operator expected" if the cumulative rank goes above 1; -- "Operand
	 * expected" if the rank goes below 0; -- "Missing '('" if scanning a �)�
	 * results in popping the stack empty with no '('; -- "Missing ')'" if a '(' is
	 * left unmatched on the stack at the end of the scan; -- "Invalid character" if
	 * a scanned char is neither a digit nor an operator;
	 * 
	 * If an error is not one of the above types, throw the exception with a message
	 * you define.
	 * 
	 * Sets postfixReady to true.
	 */
	public void postfix() throws ExpressionFormatException {
		int rightParen = 0; // keep track of right parentheses
		int leftParen = 0; // keep track of left parentheses
		infixExpression = Expression.removeExtraSpaces(infixExpression);
		if (!postfixReady) {
			for (int i = 0; i < infixExpression.length(); i++) {
				char c = infixExpression.charAt(i);
				if (c == ' ') {
					continue;
				} else if (c == '*' || c == '/' || c == '%' || c == '^' || c == '+' || c == '-') {
					rankTotal += -1;
				} else if (c == '(') {
					leftParen++;
				} else if (c == ')') {
					rightParen++;
				}
				if (Expression.isInt(Character.toString(c))) {
					int j = i;
					String num = "";
					// sees if number in expression is double-digit
					while (Expression.isInt(Character.toString(infixExpression.charAt(j)))) {
						num += infixExpression.charAt(j);
						j++;
						if (j >= infixExpression.length()) {
							break;
						}
					}
					// sets i to correct position
					i += num.length() - 1;
					postfixExpression += num + " ";
					rankTotal += 1;
				} else if (Expression.isVariable(c)) {
					postfixExpression += c + " ";
					rankTotal += 1;
				} else if (Expression.isOperator(c)) {
					// check if '-' is unary
					if ((c == '-' && i == 0) || (c == '-' && infixExpression.charAt(i - 2) != ')'
							&& Expression.isOperator(infixExpression.charAt(i - 2))
							|| (c == '-' && infixExpression.charAt(i - 1) == '('))) {
						c = '~';
						rankTotal += 1;
					}
					Operator o = new Operator(c);
					if (c == ')' && operatorStack.peek().operator != '(' && operatorStack.size() == 1) {
						throw new ExpressionFormatException("Missing '('");
					}
					if (operatorStack.isEmpty()) {
						operatorStack.push(o);
					} else if (operatorStack.peek().compareTo(o) < 0) {
						operatorStack.push(o);
					} else {
						outputHigherOrEqual(o);
						if (c != ')') {
							if(!operatorStack.isEmpty() && operatorStack.peek().compareTo(o) == 0) {
								postfixExpression += operatorStack.pop().operator + " ";
							}
							operatorStack.push(o);
						}
					}
				} else {
					throw new ExpressionFormatException("Invalid character");
				}
				if (rankTotal > 1) {
					throw new ExpressionFormatException("Operator expected");
				} else if (rankTotal < 0) {
					throw new ExpressionFormatException("Operand expected");
				}
			}
			if (leftParen != rightParen) {
				throw new ExpressionFormatException("Missing ')'");
			}
			if (rankTotal != 1) {
				throw new ExpressionFormatException("Incorrect formatting");
			}
			while (!operatorStack.isEmpty()) {
				postfixExpression += operatorStack.pop().operator + " ";
			}
			postfixReady = true;
		}
	}

	/**
	 * This function first calls postfix() to convert infixExpression into
	 * postfixExpression. Then it creates a PostfixExpression object and calls its
	 * evaluate() method (which may throw an exception). It also passes any
	 * exception thrown by the evaluate() method of the PostfixExpression object
	 * upward the chain.
	 * 
	 * @return value of the infix expression
	 * @throws ExpressionFormatException, UnassignedVariableException
	 */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException {
		try {
			postfix();
		} catch (ExpressionFormatException e) {
			e.printStackTrace();
		}
		PostfixExpression postfix = new PostfixExpression(postfixExpression, varTable);
		return postfix.evaluate();
	}

	/**
	 * Pops the operator stack and output as long as the operator on the top of the
	 * stack has a stack precedence greater than or equal to the input precedence of
	 * the current operator op. Writes the popped operators to the string
	 * postfixExpression.
	 * 
	 * If op is a ')', and the top of the stack is a '(', also pops '(' from the
	 * stack but does not write it to postfixExpression.
	 * 
	 * @param op current operator
	 */
	private void outputHigherOrEqual(Operator op) {
		if (op.operator == ')') {
			while (operatorStack.peek().operator != '(') {
				postfixExpression += operatorStack.pop().operator + " ";
			}
			operatorStack.pop();
		} else if (operatorStack.peek().compareTo(op) >= 0) {
			postfixExpression += operatorStack.pop().operator + " ";
		}
	}

	// other helper methods if needed
}
