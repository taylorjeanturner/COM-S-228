package edu.iastate.cs228.hw3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Junit Tests for CS 228 homework assignment 3 (PrimeFactorization).
 * 
 * @author CL
 *
 */
public class hw3Tests {

	@Test // PrimeFactor tests
	public void testPF() {
		PrimeFactor a = new PrimeFactor(2, 3);
		assertEquals("Prime or Multiplicity value\n", 2, a.prime);
		assertEquals("Prime or Multiplicity value\n", 3, a.multiplicity);
		assertEquals("PrimeFactor toString method\n", "2^3", a.toString());
		a = new PrimeFactor(2, 1);
		assertEquals("PrimeFactor toString method\n", "2", a.toString());
		PrimeFactor b = a.clone();
		assertEquals("Prime or Multiplicity value\n", 2, b.prime);
		assertEquals("Prime or Multiplicity value\n", 1, b.multiplicity);
		assertEquals("PrimeFactor toString method\n", a.toString(), b.toString());
		try {
			PrimeFactor c = new PrimeFactor(3, 0); // Should throw an IllegalArgumentException because multiplicity < 1
			fail("Exception was not thrown in PrimeFactor constructor"); // Fails when an exception is not thrown;
		}
		catch (Exception e) {
			if (!(e instanceof IllegalArgumentException)) { // Check exception type
				fail("Inocrrect Exception (PrimeFactor constructor)\nExpected: IllegalArgumentException.\nActual: " + e);
			}
		}

	}

	@Test // Basic Constructor tests
	public void testConstructors() {
		// Default constructor
		PrimeFactorization a = new PrimeFactorization();
		assertEquals("Default constructor value\n", 1, a.value());
		assertEquals("toString method\n", "1", a.toString());
		assertEquals("Default size\n", 0, a.size());

		// Direct search factorization on prime number
		a = new PrimeFactorization(89);
		assertEquals("Constructor value\n", 89, a.value());
		assertEquals("toString method\n", "89", a.toString());
		assertEquals("Size\n", 1, a.size());

		// Direct search factorization constructor
		a = new PrimeFactorization(25480);
		assertEquals("Constructor value\n", 25480, a.value());
		assertEquals("toString method\n", "2^3 * 5 * 7^2 * 13", a.toString());
		assertEquals("Size\n", 4, a.size());

		// Copy constructor
		PrimeFactorization b = new PrimeFactorization(a);
		assertEquals("Constructor value\n", 25480, b.value());
		assertEquals("toString method\n", "2^3 * 5 * 7^2 * 13", b.toString());
		assertEquals("Size\n", 4, b.size());

		// Array constructor
		PrimeFactor[] pf = new PrimeFactor[4];
		pf[0] = new PrimeFactor(2, 3);
		pf[1] = new PrimeFactor(5, 1);
		pf[2] = new PrimeFactor(7, 2);
		pf[3] = new PrimeFactor(13, 1);
		PrimeFactorization c = new PrimeFactorization(pf);
		assertEquals("Constructor value\n", 25480, c.value());
		assertEquals("toString method\n", "2^3 * 5 * 7^2 * 13", c.toString());
		assertEquals("Size\n", 4, c.size());

		// Array constructor used with a primeFactorials that combine to be an overflow
		// value
		PrimeFactor[] of = new PrimeFactor[2];
		of[0] = new PrimeFactor(2, 64);
		of[1] = new PrimeFactor(3, 64);
		PrimeFactorization d = new PrimeFactorization(of);
		assertEquals("Constructor value\n", -1, d.value());
		assertEquals("toString method\n", "2^64 * 3^64", d.toString());
		assertEquals("Size\n", 2, d.size());
	}

	@Test // Test primality test (isPrime() method)
	public void testPrime() {
		assertEquals("Primality test on 0\n", false, PrimeFactorization.isPrime(0));
		assertEquals("Primality test on 1\n", false, PrimeFactorization.isPrime(1));
		assertEquals("Primality test on 2\n", true, PrimeFactorization.isPrime(2));
		assertEquals("Primality test on 3\n", true, PrimeFactorization.isPrime(3));
		assertEquals("Primality test on 4\n", false, PrimeFactorization.isPrime(4));
		assertEquals("Primality test on 81\n", false, PrimeFactorization.isPrime(81));
		assertEquals("Primality test on 97\n", true, PrimeFactorization.isPrime(97));
		assertEquals("Primality test on 25480\n", false, PrimeFactorization.isPrime(25480));
		assertEquals("Primality test on 10000\n", false, PrimeFactorization.isPrime(10000));
	}

	@Test // Multiplication tests
	public void testMultiplication() {
		// First multiply method
		PrimeFactorization a = new PrimeFactorization(25480);
		a.multiply(405);
		assertEquals("Value incorrect after multiplication\n", 10319400, a.value());
		assertEquals("toString (possibly the list itself) incorrect after multiplication\n",
				"2^3 * 3^4 * 5^2 * 7^2 * 13", a.toString());

		// Second multiply method
		a = new PrimeFactorization(25480);
		PrimeFactorization b = new PrimeFactorization(405);
		a.multiply(b);
		assertEquals("Value incorrect after multiplication\n", 10319400, a.value());
		assertEquals("toString (possibly the list itself) incorrect after multiplication\n",
				"2^3 * 3^4 * 5^2 * 7^2 * 13", a.toString());

		// Third multiply method
		a = new PrimeFactorization(25480);
		b = new PrimeFactorization(405);
		PrimeFactorization c = PrimeFactorization.multiply(a, b);
		assertEquals("Value incorrect after multiplication\n", 10319400, c.value());
		assertEquals("toString (possibly the list itself) incorrect after multiplication\n",
				"2^3 * 3^4 * 5^2 * 7^2 * 13", c.toString());

		// First multiply method to test case with an overflow value
		PrimeFactor[] of = new PrimeFactor[1];
		of[0] = new PrimeFactor(2, 64);
		a = new PrimeFactorization(of);
		a.multiply(10000);
		assertEquals("Value incorrect (should be Overflow) after multiplication\n", -1, a.value());
		assertEquals("toString (possibly the list itself) incorrect after multiplication\n", "2^68 * 5^4",
				a.toString());
	}

	@Test // Division tests
	public void testDivision() {
		// First dividedBy method
		PrimeFactorization a = new PrimeFactorization(25480);
		assertEquals("DividedBy value incorrect\n", true, a.dividedBy(98));
		assertEquals("Value incorrect after dividing\n", 260, a.value());
		assertEquals("toString (possibly the list itself) incorrect after division\n", "2^2 * 5 * 13", a.toString());

		// Second dividedBy method
		a = new PrimeFactorization(25480);
		PrimeFactorization b = new PrimeFactorization(98);
		assertEquals("DividedBy value incorrect\n", true, a.dividedBy(b));
		assertEquals("Value incorrect after dividing\n", 260, a.value());
		assertEquals("toString (possibly the list itself) incorrect after division\n", "2^2 * 5 * 13", a.toString());

		// Third dividedBy method
		a = new PrimeFactorization(25480);
		b = new PrimeFactorization(98);
		PrimeFactorization c = PrimeFactorization.dividedBy(a, b);
		assertEquals("Value incorrect after dividing\n", 260, c.value());
		assertEquals("toString (possibly the list itself) incorrect after division\n", "2^2 * 5 * 13", c.toString());

		// Not divisible tests
		assertEquals("DividedBy value incorrect\n", false, b.dividedBy(25480));
		assertEquals("Value incorrect after a call to dividedBy returned false\n", 98, b.value());
		assertEquals("DividedBy value incorrect\n", false, b.dividedBy(a));
		assertEquals("DividedBy value incorrect\n", null, PrimeFactorization.dividedBy(b, a));
	}

	@Test // Greatest Common Divisor tests
	public void testGCD() {
		PrimeFactorization a = new PrimeFactorization(25480);
		PrimeFactorization b = new PrimeFactorization(98);

		// First method tests
		PrimeFactorization c = a.gcd(98);
		assertEquals("Greatest Common Divisor return value incorrect\n", 98, c.value());

		// Second method tests
		c = a.gcd(b);
		assertEquals("Greatest Common Divisor return value incorrect\n", 98, c.value());

		// Third method tests
		assertEquals("Greatest Common Divisor return value incorrect\n", 98, PrimeFactorization.gcd(a, b).value());
	}

	@Test // Exception throwing tests
	public void testExceptions() {
		PrimeFactorization a = new PrimeFactorization(25480);
		// PrimeFactorization constructor
		try {
			a = new PrimeFactorization(0); // Should throw an IllegalArgumentException because n < 1
			fail("Exception was not thrown in PrimeFactorization constructor"); // Fails when an exception is not thrown;
		}
		catch (Exception e) {
			if (!(e instanceof IllegalArgumentException)) { // Check exception type
				fail("Inocrrect Exception (PrimeFactorization constructor)\nExpected: IllegalArgumentException.\nActual: " + e);
			}
		}
		
		// Multiply method
		try {
			a.multiply(0); // Should throw an IllegalArgumentException because n < 1
			fail("Exception was not thrown in PrimeFactorization multiply method"); // Fails when an exception is not thrown;
		}
		catch (Exception e) {
			if (!(e instanceof IllegalArgumentException)) { // Check exception type
				fail("Inocrrect Exception (PrimeFactorization multiply)\nExpected: IllegalArgumentException.\nActual: " + e);
			}
		}
		
		// dividedBy method
		try {
			a.dividedBy(0); // Should throw an IllegalArgumentException because n <= 0
			fail("Exception was not thrown in PrimeFactorization dividedBy method"); // Fails when an exception is not thrown;
		}
		catch (Exception e) {
			if (!(e instanceof IllegalArgumentException)) { // Check exception type
				fail("Inocrrect Exception (PrimeFactorization dividedBy)\nExpected: IllegalArgumentException.\nActual: " + e);
			}
		}
		
		// gcd method
		try {
			a.gcd(0); // Should throw an IllegalArgumentException because n < 1
			fail("Exception was not thrown in PrimeFactorization gcd method"); // Fails when an exception is not thrown;
		}
		catch (Exception e) {
			if (!(e instanceof IllegalArgumentException)) { // Check exception type
				fail("Inocrrect Exception (PrimeFactorization gcd)\nExpected: IllegalArgumentException.\nActual: " + e);
			}
		}
		
		// Euclidean method
		try {
			PrimeFactorization.Euclidean(0, 1); // Should throw an IllegalArgumentException because n or m < 1
			PrimeFactorization.Euclidean(1, 0);
			fail("Exception was not thrown in PrimeFactorization Euclidean method"); // Fails when an exception is not thrown;
		}
		catch (Exception e) {
			if (!(e instanceof IllegalArgumentException)) { // Check exception type
				fail("Inocrrect Exception (PrimeFactorization Euclidean)\nExpected: IllegalArgumentException.\nActual: " + e);
			}
		}
		
		// containsPrimeFactor method
		try {
			a.containsPrimeFactor(4); // Should throw an IllegalArgumentException because n (4) is not prime
			fail("Exception was not thrown in PrimeFactorization containsPrimeFactor method"); // Fails when an exception is not thrown;
		}
		catch (Exception e) {
			if (!(e instanceof IllegalArgumentException)) { // Check exception type
				fail("Inocrrect Exception (PrimeFactorization containsPrimeFactor)\nExpected: IllegalArgumentException.\nActual: " + e);
			}
		}
		
		// remove method
		try {
			a.remove(2, 0); // Should throw an IllegalArgumentException because m < 1
			fail("Exception was not thrown in PrimeFactorization remove method"); // Fails when an exception is not thrown;
		}
		catch (Exception e) {
			if (!(e instanceof IllegalArgumentException)) { // Check exception type
				fail("Inocrrect Exception (PrimeFactorization remove)\nExpected: IllegalArgumentException.\nActual: " + e);
			}
		}
		// There are more exceptions that could be checked within the Node and Iterator classes in PrimeFactorization
	}
}
