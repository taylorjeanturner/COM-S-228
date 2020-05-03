package edu.iastate.cs228.hw4;

/**
 *  
 * @author Taylor Turner
 *
 */

/**
 * 
 * This class evaluates a postfix expression using one stack.    
 *
 */

import java.util.HashMap;
import java.util.NoSuchElementException;

public class PostfixExpression extends Expression {
	private int leftOperand; // left operand for the current evaluation step
	private int rightOperand; // right operand (or the only operand in the case of
								// a unary minus) for the current evaluation step

	private PureStack<Integer> operandStack; // stack of operands

	/**
	 * Constructor stores the input postfix string and initializes the operand
	 * stack.
	 * 
	 * @param st     input postfix string.
	 * @param varTbl hash map that stores variables from the postfix string and
	 *               their values.
	 */
	public PostfixExpression(String st, HashMap<Character, Integer> varTbl) {
		super(st, varTbl);
		operandStack = new ArrayBasedStack<>();
	}

	/**
	 * Constructor supplies a default hash map.
	 * 
	 * @param s
	 */
	public PostfixExpression(String s) {
		super(s);
		operandStack = new ArrayBasedStack<>();
	}

	/**
	 * Outputs the postfix expression according to the format in the project
	 * description.
	 */
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < postfixExpression.length(); i++) {
			char c = postfixExpression.charAt(i);
			if (Expression.isInt(Character.toString(c))) {
				int j = i;
				String num = "";
				while (Expression.isInt(Character.toString(postfixExpression.charAt(j)))) {
					num += postfixExpression.charAt(j);
					j++;
					if (j >= postfixExpression.length()) {
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
	 * Resets the postfix expression.
	 * 
	 * @param st
	 */
	public void resetPostfix(String st) {
		postfixExpression = st;
	}

	/**
	 * Scan the postfixExpression and carry out the following:
	 * 
	 * 1. Whenever an integer is encountered, push it onto operandStack. 2. Whenever
	 * a binary (unary) operator is encountered, invoke it on the two (one) elements
	 * popped from operandStack, and push the result back onto the stack. 3. On
	 * encountering a character that is not a digit, an operator, or a blank space,
	 * stop the evaluation.
	 * 
	 * @return value of the postfix expression
	 * @throws ExpressionFormatException with one of the messages below:
	 * 
	 *                                   -- "Invalid character" if encountering a
	 *                                   character that is not a digit, an operator
	 *                                   or a whitespace (blank, tab); -- "Too many
	 *                                   operands" if operandStack is non-empty at
	 *                                   the end of evaluation; -- "Too many
	 *                                   operators" if getOperands() throws
	 *                                   NoSuchElementException; -- "Divide by zero"
	 *                                   if division or modulo is the current
	 *                                   operation and rightOperand == 0; -- "0^0"
	 *                                   if the current operation is "^" and
	 *                                   leftOperand == 0 and rightOperand == 0; --
	 *                                   self-defined message if the error is not
	 *                                   one of the above.
	 * 
	 *                                   UnassignedVariableException if the operand
	 *                                   as a variable does not have a value stored
	 *                                   in the hash map. In this case, the
	 *                                   exception is thrown with the message
	 * 
	 *                                   -- "Variable <name> was not assigned a
	 *                                   value", where <name> is the name of the
	 *                                   variable.
	 * 
	 */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException {
		postfixExpression = Expression.removeExtraSpaces(postfixExpression);
		for (int i = 0; i < postfixExpression.length(); i++) {
			char c = postfixExpression.charAt(i);
			if (c == ' ') {
				continue;
			} else if (Expression.isInt(Character.toString(c))) {
				int j = i;
				String num = "";
				// sees if number in expression is double-digit
				while (Expression.isInt(Character.toString(postfixExpression.charAt(j)))) {
					num += postfixExpression.charAt(j);
					j++;
					if (j >= postfixExpression.length()) {
						break;
					}
				}
				// moves i to correct position
				i += num.length() - 1;
				operandStack.push(Integer.parseInt(num));
			} else if (Expression.isVariable(c)) {
				if (varTable.containsKey(c)) {
					int x = (int) varTable.get(c);
					operandStack.push(x);
				} else {
					throw new UnassignedVariableException("Variable " + c + " was not assigned a value");
				}
			} else if (Expression.isOperator(c)) {
				try {
					getOperands(c);
				} catch (NoSuchElementException e) {
					throw new ExpressionFormatException("Too many operators");
				}
				if (rightOperand == 0 && (c == '/' || c == '%')) {
					throw new ExpressionFormatException("Divide by zero");
				} else if (rightOperand == 0 && leftOperand == 0 && c == '^') {
					throw new ExpressionFormatException("0^0");
				}
				operandStack.push(compute(c));
			} else if (!Expression.isInt(Character.toString(c)) || !Expression.isOperator(c)) {
				throw new ExpressionFormatException("Invalid Character");
			}
		}
		int answer = operandStack.pop();
		if (!operandStack.isEmpty()) {
			throw new ExpressionFormatException("Too many operands");
		}
		return answer;
	}

	/**
	 * For unary operator, pops the right operand from operandStack, and assign it
	 * to rightOperand. The stack must have at least one entry. Otherwise, throws
	 * NoSuchElementException. For binary operator, pops the right and left operands
	 * from operandStack, and assign them to rightOperand and leftOperand,
	 * respectively. The stack must have at least two entries. Otherwise, throws
	 * NoSuchElementException.
	 * 
	 * @param op char operator for checking if it is binary or unary operator.
	 */
	private void getOperands(char op) throws NoSuchElementException {
		if (op == '~') {
			if (operandStack.size() < 1) {
				throw new NoSuchElementException();
			}
			rightOperand = operandStack.pop();
		} else {
			if (operandStack.size() < 2) {
				throw new NoSuchElementException();
			}
			rightOperand = operandStack.pop();
			leftOperand = operandStack.pop();
		}
	}

	/**
	 * Computes "leftOperand op rightOprand" or "op rightOprand" if a unary
	 * operator.
	 * 
	 * @param op operator that acts on leftOperand and rightOperand.
	 * @return
	 */
	private int compute(char op) {
		if (op == '~') {
			return rightOperand * -1;
		} else if (op == '+') {
			return leftOperand + rightOperand;
		} else if (op == '-') {
			return leftOperand - rightOperand;
		} else if (op == '/') {
			return leftOperand / rightOperand;
		} else if (op == '*') {
			return leftOperand * rightOperand;
		} else if (op == '%') {
			return leftOperand % rightOperand;
		} else if(op == '^'){
			return (int) Math.pow(leftOperand, rightOperand);
		}
		return 0;
	}
}
