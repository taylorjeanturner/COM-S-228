package edu.iastate.cs228.hw4;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.HashMap;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Junit Tests for CS 228 homework assignment 4 (Infix & Postfix expressions)
 * 
 * @author CL
 *
 */
public class hw4Tests {
	static int p;
	static int count;

	@BeforeClass
	public static void beforeClass() {
		p = count = 0;
	}

	@Before
	public void count() {
		count++;
	}

	@AfterClass
	public static void passed() {
		if (p == count) System.out.println("Passed all tests");
		else System.out.println("Passed " + p + "/" + count + " tests");
	}

	@Test // Miscellaneous tests on abstract class Expression
	public void testExpression() {
		// isInt method tests
		assertEquals("Expression isInt() method returning incorrect value\n", true, Expression.isInt("01234"));
		assertEquals("Expression isInt() method returning incorrect value\n", false, Expression.isInt(" "));
		assertEquals("Expression isInt() method returning incorrect value\n", false, Expression.isInt(" 4"));
		assertEquals("Expression isInt() method returning incorrect value\n", false, Expression.isInt("3 "));
		assertEquals("Expression isInt() method returning incorrect value\n", false, Expression.isInt("10 4"));

		// isOperator method tests
		assertEquals("Expression isOperator() method returning incorrect value\n", true, Expression.isOperator('~'));
		assertEquals("Expression isOperator() method returning incorrect value\n", true, Expression.isOperator('+'));
		assertEquals("Expression isOperator() method returning incorrect value\n", true, Expression.isOperator('-'));
		assertEquals("Expression isOperator() method returning incorrect value\n", true, Expression.isOperator('*'));
		assertEquals("Expression isOperator() method returning incorrect value\n", true, Expression.isOperator('/'));
		assertEquals("Expression isOperator() method returning incorrect value\n", true, Expression.isOperator('%'));
		assertEquals("Expression isOperator() method returning incorrect value\n", true, Expression.isOperator('^'));
		assertEquals("Expression isOperator() method returning incorrect value\n", true, Expression.isOperator('('));
		assertEquals("Expression isOperator() method returning incorrect value\n", true, Expression.isOperator(')'));
		assertEquals("Expression isOperator() method returning incorrect value\n", false, Expression.isOperator('1'));
		assertEquals("Expression isOperator() method returning incorrect value\n", false, Expression.isOperator(' '));
		assertEquals("Expression isOperator() method returning incorrect value\n", false, Expression.isOperator('a'));

		// isVariable method tests
		assertEquals("Expression isVariable() method returning incorrect value\n", true, Expression.isVariable('a'));
		assertEquals("Expression isVariable() method returning incorrect value\n", false, Expression.isVariable('A'));
		assertEquals("Expression isVariable() method returning incorrect value\n", false, Expression.isVariable('+'));
		assertEquals("Expression isVariable() method returning incorrect value\n", false, Expression.isVariable('.'));

		// removeExtraSpaces method tests
		assertEquals("Expression removeExtraSpaces() method returning incorrect value\n", "Hello, world", Expression.removeExtraSpaces("	Hello,	world	"));
		assertEquals("Expression removeExtraSpaces() method returning incorrect value\n", "5 - 32 + 1", Expression.removeExtraSpaces("	 5 - 	32 + 1"));
		assertEquals("Expression removeExtraSpaces() method returning incorrect value\n", "1+ 2", Expression.removeExtraSpaces("	1+ 	2		 	"));
		assertEquals("Expression removeExtraSpaces() method returning incorrect value\n", "1+ 2-4+ 5* ( 3 + 2 )", Expression.removeExtraSpaces("	1+ 	2-4+  5* 	(  3 + 2			)		"));
		assertEquals("Expression removeExtraSpaces() method returning incorrect value\n", "- 1 +2 - 4%4 +2 ^ 2", Expression.removeExtraSpaces("- 1 +2 - 4%4 +2 		^ 2"));
		p++;
	}

	@Test // Tests for InfixExpression class without any variables
	public void testInfix() {
		// Expression without any extra spaces or urnary operators
		InfixExpression i = new InfixExpression("8 / (1 + 3) - 6 ^ 2");
		assertEquals("InfixExpression toString() method returning incorrect value\n", "8 / (1 + 3) - 6 ^ 2", i.toString());
		assertEquals("InfixExpression postfixString() method returning incorrect value\n", "8 1 3 + / 6 2 ^ -", i.postfixString());
		try {
			assertEquals("InfixExpression evaluate returning incorrect value\n", -34, i.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("InfixExpression evaluate method threw exception when correct expression entered");
		}

		// Expression with a few extra spaces and without urnary operators
		InfixExpression h = new InfixExpression("	(5 -  1) % 	3 + 90 / 9 -  2  ^ 3 ");
		assertEquals("InfixExpression toString() method returning incorrect value\n", "(5 - 1) % 3 + 90 / 9 - 2 ^ 3", h.toString());
		assertEquals("InfixExpression postfixString() method incorrect\n", "5 1 - 3 % 90 9 / + 2 3 ^ -", h.postfixString());
		try {
			assertEquals("InfixExpression evaluate returning incorrect value\n", 3, h.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("InfixExpression evaluate method threw exception when correct expression entered");
		}

		// Expression with urnary operators
		InfixExpression g = new InfixExpression("(5 - - 1) % 3 + 90 / 9 - - 2 ^ 3");
		assertEquals("InfixExpression toString() method returning incorrect value\n", "(5 - - 1) % 3 + 90 / 9 - - 2 ^ 3", g.toString());
		assertEquals("InfixExpression postfixString() method returning incorrect value\n", "5 1 ~ - 3 % 90 9 / + 2 ~ 3 ^ -", g.postfixString());
		try {
			assertEquals("InfixExpression evaluate returning incorrect value\n", 18, g.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("InfixExpression evaluate method threw exception when correct expression entered");
		}

		// Incorrect expression
		InfixExpression f = new InfixExpression("5 - 3 + ");
		assertEquals("InfixExpression toString() method returning incorrect value\n", "5 - 3 +", f.toString());
		assertEquals("InfixExpression postfixString() should return null when postfix throws an exception\n", null, f.postfixString());

		// resetInfix method test
		f.resetInfix("2 + 1/ 3");
		assertEquals("InfixExpression toString() returning incorrect value after a call to resetInfix\n", "2 + 1 / 3", f.toString());
		assertEquals("InfixExpression postfixString() method returning incorrect value after a call to resetInfix()\n", "2 1 3 / +", f.postfixString());
		p++;
	}

	@Test // Tests for InfixExpression class with variables
	public void testInfixVar() {
		// Expression with variables, but unassigned values
		InfixExpression i = new InfixExpression("( x + y ) * z");
		assertEquals("InfixExpression toString() method returning incorrect value\n", "(x + y) * z", i.toString());
		assertEquals("InfixExpression postfixString() method returning incorrect value\n", "x y + z *", i.postfixString());
		try {
			i.evaluate();
			fail("InfixExpression should throw an exception when evaluating and there are unassigned variables");
		}
		catch (ExpressionFormatException e1) {
			e1.printStackTrace();
			fail("InfixExpression evaluate method threw an ExpressionFormatException when it should have thrown an UnassignedVariableException. (This may be because of another issue)");
		}
		catch (UnassignedVariableException e2) {
			// Correct exception thrown :)
		}

		// Expression with variables (with assigned values)
		HashMap<Character, Integer> varMap = new HashMap<Character, Integer>();
		varMap.put('x', 21);
		varMap.put('y', 13);
		varMap.put('z', 5);
		InfixExpression h = new InfixExpression("( x + y ) * z", varMap);
		assertEquals("InfixExpression toString() method returning incorrect value\n", "(x + y) * z", h.toString());
		assertEquals("InfixExpression postfixString() method incorrect\n", "x y + z *", h.postfixString());
		try {
			assertEquals("InfixExpression evaluate returning incorrect value\n", 170, h.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("InfixExpression evaluate method threw exception when correct expression entered");
		}

		// Expression with variables (with assigned values)
		varMap = new HashMap<Character, Integer>();
		varMap.put('x', 4);
		varMap.put('y', 2);
		varMap.put('z', 1);
		InfixExpression g = new InfixExpression("- 6 % x + 4 ^ 2 / y - z", varMap);
		assertEquals("InfixExpression toString() method returning incorrect value\n", "- 6 % x + 4 ^ 2 / y - z", g.toString());
		assertEquals("InfixExpression postfixString() method incorrect\n", "6 ~ x % 4 2 ^ y / + z -", g.postfixString());
		try {
			assertEquals("InfixExpression evaluate returning incorrect value\n", 5, g.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("InfixExpression evaluate method threw exception when correct expression entered");
		}

		// Expression with repeating variables (assigned values)
		varMap = new HashMap<Character, Integer>();
		varMap.put('a', 4);
		varMap.put('b', 2);
		InfixExpression f = new InfixExpression("a + b ^ 2 / a * b", varMap);
		assertEquals("InfixExpression toString() method returning incorrect value\n", "a + b ^ 2 / a * b", f.toString());
		assertEquals("InfixExpression postfixString() method incorrect\n", "a b 2 ^ a / b * +", f.postfixString());
		try {
			assertEquals("InfixExpression evaluate returning incorrect value\n", 6, f.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("InfixExpression evaluate method threw exception when correct expression entered");
		}
		p++;
	}

	@Test // Tests for PostfixExpression class without variables
	public void testPostfix() {
		// Expression without any extra spaces or unary operators
		PostfixExpression i = new PostfixExpression("5 2 - 1 +");
		assertEquals("PostfixExpression toString() method returning incorrect value\n", "5 2 - 1 +", i.toString());
		try {
			assertEquals("PostfixExpression evaluate returning incorrect value\n", 4, i.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("PostfixExpression evaluate method threw exception when correct expression entered");
		}

		// Expression with a few extra spaces and no unary operator
		PostfixExpression h = new PostfixExpression("6  4  - 2	  / 	 ");
		assertEquals("PostfixExpression toString() method returning incorrect value\n", "6 4 - 2 /", h.toString());
		try {
			assertEquals("PostfixExpression evaluate returning incorrect value\n", 1, h.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("PostfixExpression evaluate method threw exception when correct expression entered");
		}

		// Expression with a unary operator
		PostfixExpression g = new PostfixExpression("6 ~ 2	  / 	3  +");
		assertEquals("PostfixExpression toString() method returning incorrect value\n", "6 ~ 2 / 3 +", g.toString());
		try {
			assertEquals("PostfixExpression evaluate returning incorrect value\n", 0, g.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("PostfixExpression evaluate method threw exception when correct expression entered");
		}

		// Incorrect Expression
		PostfixExpression f = new PostfixExpression("1  2 - +");
		assertEquals("PostfixExpression toString() method returning incorrect value\n", "1 2 - +", f.toString());
		try {
			f.evaluate();
			fail("Evaluate should have thrown an ExpressionFormatException because an incorrect expression was entered.");
		}
		catch (UnassignedVariableException e1) {
			e1.printStackTrace();
			fail("Incorrect Exception thrown. Expected ExpressionFormatException.\n");
		}
		catch (ExpressionFormatException e2) {
			// Correct exception thrown :)
		}
		p++;
	}

	@Test // Tests for PostfixException class with variables
	public void testPostfixVar() {
		// Expression with variables, but unassigned values
		PostfixExpression i = new PostfixExpression("x y + z *");
		assertEquals("PostfixExpression toString() method returning incorrect value\n", "x y + z *", i.toString());
		try {
			i.evaluate();
			fail("PostfixExpression should throw an exception when evaluating and there are unassigned variables");
		}
		catch (ExpressionFormatException e1) {
			e1.printStackTrace();
			fail("PostfixExpression evaluate method threw an ExpressionFormatException when it should have thrown an UnassignedVariableException. (This may be because of another issue)");
		}
		catch (UnassignedVariableException e2) {
			// Correct exception thrown :)
		}

		// Expression with variables (with assigned values)
		HashMap<Character, Integer> varMap = new HashMap<Character, Integer>();
		varMap = new HashMap<Character, Integer>();
		varMap.put('x', 21);
		varMap.put('y', 13);
		varMap.put('z', 5);
		PostfixExpression h = new PostfixExpression("x y + z *", varMap);
		assertEquals("PostfixExpression toString() method returning incorrect value\n", "x y + z *", h.toString());
		try {
			assertEquals("PostfixExpression evaluate returning incorrect value\n", 170, h.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("PostfixExpression evaluate method threw exception when correct expression (and variables with values) entered");
		}

		// Expression with variables (with assigned values)
		varMap = new HashMap<Character, Integer>();
		varMap.put('x', 4);
		varMap.put('y', 2);
		varMap.put('z', 1);
		PostfixExpression g = new PostfixExpression("6 ~ x % 4 2 ^ y / + z -", varMap);
		assertEquals("PostfixExpression toString() method returning incorrect value\n", "6 ~ x % 4 2 ^ y / + z -", g.toString());
		try {
			assertEquals("PostfixExpression evaluate returning incorrect value\n", 5, g.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("PostfixExpression evaluate method threw exception when correct expression (and variables with values) entered");
		}

		// Expression with repeating variables (assigned values)
		varMap = new HashMap<Character, Integer>();
		varMap.put('a', 4);
		varMap.put('b', 2);
		PostfixExpression f = new PostfixExpression("a b 2 ^ a / b * +", varMap);
		assertEquals("PostfixExpression toString() method returning incorrect value\n", "a b 2 ^ a / b * +", f.toString());
		try {
			assertEquals("InfixExpression evaluate returning incorrect value\n", 6, f.evaluate());
		}
		catch (ExpressionFormatException | UnassignedVariableException e) {
			e.printStackTrace();
			fail("InfixExpression evaluate method threw exception when correct expression entered");
		}
		p++;
	}

	@Test // Tests for Operator class
	public void testOperator() {
		// Tests just ensure that compareTo returns specifically -1, 0, or 1
		Operator o = new Operator('^');
		Operator q = new Operator('+');
		assertEquals("Operator compareTo() returning incorrect value (might be input or stack precedence value)\n", 1, o.compareTo(q));
		assertEquals("Operator compareTo() returning incorrect value (might be input or stack precedence value)\n", -1, q.compareTo(o));
		o = new Operator('*');
		q = new Operator('%');
		assertEquals("Operator compareTo() returning incorrect value (might be input or stack precedence value)\n", 0, q.compareTo(o));
		p++;
	}
}
