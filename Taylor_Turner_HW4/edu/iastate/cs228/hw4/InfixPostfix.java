package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;

/**
 *  
 * @author Taylor Turner
 *
 */

/**
 * 
 * This class evaluates input infix and postfix expressions. 
 *
 */

import java.util.HashMap;
import java.util.Scanner;

public class InfixPostfix {

	/**
	 * Repeatedly evaluates input infix and postfix expressions. See the project
	 * description for the input description. It constructs a HashMap object for
	 * each expression and passes it to the created InfixExpression or
	 * PostfixExpression object.
	 * 
	 * @param args
	 * @throws UnassignedVariableException
	 * @throws ExpressionFormatException
	 * @throws FileNotFoundException
	 **/
	public static void main(String[] args)
			throws ExpressionFormatException, UnassignedVariableException, FileNotFoundException {
		int trial = 1; // keeps track of trial number
		int input = 0; // keeps track of user input
		Scanner sc = new Scanner(System.in); // scanner to scan user input
		HashMap<Character, Integer> varTable = new HashMap<Character, Integer>();
		System.out.println("Evaluation of Infix and Postfix Expressions");
		System.out.println("keys: 1 (standard input) 2 (file input) 3 (exit)");
		System.out.println("(Enter \"I\" before an infix expression, \"P\" before a postfix expression)");

		// keeps running program until 3 is entered
		while (input != 3) {
			System.out.print(" \nTrial " + trial + ": ");
			input = sc.nextInt();

			// standard input
			if (input == 1) {
				System.out.print("Expression: ");
				String type = sc.next();
				String expression = sc.next();
				expression += sc.nextLine();

				// infix
				if (type.charAt(0) == 'I') {
					InfixExpression infix = new InfixExpression(expression, varTable);
					System.out.println("Infix form: " + infix.toString());
					infix.postfix();
					String post = infix.postfixString();
					System.out.println("Postfix form: " + post);
					for (int i = 0; i < infix.postfixExpression.length(); i++) {
						if (Expression.isVariable(infix.postfixExpression.charAt(i))) {
							variableInput(infix.postfixExpression, varTable, sc);
							break;
						}
					}
					System.out.println("Expression value: " + infix.evaluate());
				}

				// postfix
				else if (type.charAt(0) == 'P') {
					PostfixExpression postfix = new PostfixExpression(expression, varTable);
					System.out.println("Postfix form: " + postfix.toString());
					for (int i = 0; i < postfix.postfixExpression.length(); i++) {
						if (Expression.isVariable(postfix.postfixExpression.charAt(i))) {
							variableInput(postfix.postfixExpression, varTable, sc);
							break;
						}
					}
					System.out.println("Expression value: " + postfix.evaluate());
				}
			}

			// file input
			else if (input == 2) {
				System.out.println("Input from a file");
				System.out.print("Enter file name: ");
				String fileName = sc.next();
				File file = new File(fileName);
				Scanner scan = new Scanner(file);
				System.out.print("\n");
				while (scan.hasNext()) {
					String type = scan.next();

					// infix
					if (type.equals("I")) {
						String expression = scan.nextLine();
						InfixExpression infix = new InfixExpression(expression, varTable);
						System.out.println("Infix form: " + infix.toString());
						infix.postfix();
						String post = infix.postfixString();
						System.out.println("Postfix form: " + post);
						for (int i = 0; i < infix.postfixExpression.length(); i++) {
							if (Expression.isVariable(infix.postfixExpression.charAt(i))) {
								variableScan(varTable, scan);
								break;
							}
						}
						System.out.println("Expression value: " + infix.evaluate() + "\n");
					}

					// postfix
					else if (type.equals("P")) {
						String expression = scan.nextLine();
						PostfixExpression postfix = new PostfixExpression(expression, varTable);
						System.out.println("Postfix form: " + postfix.toString());
						for (int i = 0; i < postfix.postfixExpression.length(); i++) {
							if (Expression.isVariable(postfix.postfixExpression.charAt(i))) {
								variableScan(varTable, scan);
								break;
							}
						}
						System.out.println("Expression value: " + postfix.evaluate() + "\n");
					}
				}
			}
			trial++;
		}
		sc.close();
	}

	// helper methods if needed

	/**
	 * Gets variable values from standard input
	 * 
	 * @param expression Postfix or Infix expression
	 * @param varTable   Hash map to store variables
	 * @param scan       Scanner to scan user input
	 */
	private static void variableInput(String expression, HashMap<Character, Integer> varTable, Scanner scan) {
		System.out.println("where");
		for (int i = 0; i < expression.length(); i++) {
			if (Expression.isVariable(expression.charAt(i))) {
				System.out.print(expression.charAt(i) + " = ");
				int value = scan.nextInt();
				varTable.put(expression.charAt(i), value);
			}
		}
	}

	/**
	 * Gets variable values from file
	 * 
	 * @param varTable Hash map to store variables
	 * @param scan     Scanner to scan user input
	 */
	private static void variableScan(HashMap<Character, Integer> varTable, Scanner scan) {
		System.out.println("where");
		while (scan.hasNext()) {
			if (scan.hasNext("I") || scan.hasNext("P")) {
				break;
			} else {
				String string = scan.next();
				char c = string.charAt(0);
				// pass the '='
				scan.next();
				int value = scan.nextInt();
				varTable.put(c, value);
				System.out.println(c + " = " + varTable.get(c));
			}
		}
	}
}
