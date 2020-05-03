package edu.iastate.cs228.hw3;

/**
 *  
 * @author Taylor Turner
 *
 */

import java.util.ListIterator;

public class PrimeFactorization implements Iterable<PrimeFactor> {
	private static final long OVERFLOW = -1;
	private long value; // the factored integer
						// it is set to OVERFLOW when the number is greater than 2^63-1, the
						// largest number representable by the type long.

	/**
	 * Reference to dummy node at the head.
	 */
	private Node head;

	/**
	 * Reference to dummy node at the tail.
	 */
	private Node tail;

	private int size; // number of distinct prime factors

	// ------------
	// Constructors
	// ------------

	/**
	 * Default constructor constructs an empty list to represent the number 1.
	 * 
	 * Combined with the add() method, it can be used to create a prime
	 * factorization.
	 */
	public PrimeFactorization() {
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		size = 0;
		value = 1;
	}

	/**
	 * Obtains the prime factorization of n and creates a doubly linked list to
	 * store the result. Follows the direct search factorization algorithm in
	 * Section 1.2 of the project description.
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization(long n) throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		value = n;
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		long m = n; // variable so n doesn't change
		int d = 2;

		// factor n and add to list
		while (d * d <= m) {
			while (m % d == 0) {
				if (isPrime(d)) {
					this.add((int) d, 1);
					m /= d;
				}
			}
			d++;
			while (d % 2 == 0) {
				d++;
			}
		}
		if (isPrime(m) && m != 1) {
			this.add((int) m, 1);
			m /= d;
		}
		if (m % d == 0) {
			if (isPrime(d)) {
				if (n % d == 0) {
					this.add((int) d, 1);
				}
			}
		}
	}

	/**
	 * Copy constructor. It is unnecessary to verify the primality of the numbers in
	 * the list.
	 * 
	 * @param pf
	 */
	public PrimeFactorization(PrimeFactorization pf) {
		this.value = pf.value;
		this.size = pf.size;
		this.head = pf.head;
		this.tail = pf.tail;
	}

	/**
	 * Constructs a factorization from an array of prime factors. Useful when the
	 * number is too large to be represented even as a long integer.
	 * 
	 * @param pflist
	 */
	public PrimeFactorization(PrimeFactor[] pfList) {
		head = new Node();
		tail = new Node();
		head.next = tail;
		tail.previous = head;
		for (int i = 0; i < pfList.length; i++) {
			add(pfList[i].prime, pfList[i].multiplicity);
		}
		updateValue();

	}

	// --------------
	// Primality Test
	// --------------

	/**
	 * Test if a number is a prime or not. Check iteratively from 2 to the largest
	 * integer not exceeding the square root of n to see if it divides n.
	 * 
	 * @param n
	 * @return true if n is a prime false otherwise
	 */
	public static boolean isPrime(long n) {
		if (n == 2 || n == 3) {
			return true;
		}
		if (n == 0 || n == 1) {
			return false;
		}
		int squareRoot = (int) Math.sqrt(n) + 1;
		for (int i = 2; i < squareRoot; i++) {
			if (n % i == 0) {
				return false;
			}
		}

		return true;

	}

	// ---------------------------
	// Multiplication and Division
	// ---------------------------

	/**
	 * Multiplies the integer v represented by this object with another number n.
	 * Note that v may be too large (in which case this.value == OVERFLOW). You can
	 * do this in one loop: Factor n and traverse the doubly linked list
	 * simultaneously. For details refer to Section 3.1 in the project description.
	 * Store the prime factorization of the product. Update value and size.
	 * 
	 * @param n
	 * @throws IllegalArgumentException if n < 1
	 */
	public void multiply(long n) throws IllegalArgumentException {
		if (n >= Math.pow(2, 63 - 1)) {
			this.value = OVERFLOW;
		}
		try {
			this.value = Math.multiplyExact(this.value, n);
		} catch (ArithmeticException e) {
			value = OVERFLOW;
		}
		long m = n;
		long d = 2;
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		PrimeFactorizationIterator iter = this.iterator();
		PrimeFactor pf = iter.cursor.pFactor;
		long lastd = 0; // keeps track of last d value
		while (d * d <= m) {
			while (m % d == 0) {
				while (iter.hasNext()) {
					if (pf.prime == d) {
						pf.multiplicity++;
						break;
					} else if (pf.prime > d) {
						// make sure NullPointerException will not be thrown
						while (iter.cursor.previous != head && d != iter.cursor.previous.pFactor.prime
								&& d < iter.cursor.previous.pFactor.prime) {
							iter.previous();
						}
						add((int) d, 1);
						// in order to see if prime has more multiplicities
						if (lastd == d) {
							pf = iter.previous();
						}
						break;
					} else if (pf.prime < d) {
						// make sure NullPointerException will not be thrown
						while (iter.cursor != tail && d != iter.cursor.pFactor.prime && d > iter.cursor.pFactor.prime) {
							iter.next();
						}
						add((int) d, 1);
						// in order to see if prime has more multiplicities
						if (lastd == d) {
							pf = iter.previous();
						}
						break;
					}
					pf = iter.next();
				}
				m /= d;
			}
			lastd = d;
			d++;
			if (iter.cursor == tail) {
				pf = iter.previous();
			}
		}
		// add final primes, if applicable
		if (m % d == 0) {
			if (isPrime(d)) {
				if (n % d == 0) {
					add((int) d, 1);
				}
			}
		} else if (isPrime(m) && m != 1) {
			if (m == d) {
				iter.cursor.pFactor.multiplicity++;
			} else if (m > d) {
				add((int) m, 1);
			}
		}
		updateValue();
	}

	/**
	 * Multiplies the represented integer v with another number in the factorization
	 * form. Traverse both linked lists and store the result in this list object.
	 * See Section 3.1 in the project description for details of algorithm.
	 * 
	 * @param pf
	 */
	public void multiply(PrimeFactorization pf) {
		PrimeFactorizationIterator iterFirst = this.iterator();
		PrimeFactorizationIterator iterPf = pf.iterator();
		while (iterPf.hasNext()) {
			PrimeFactor second = iterPf.next();
			PrimeFactor first = iterFirst.cursor.pFactor;
			if (containsPrimeFactor(second.prime)) {
				// moves cursors to correct positions if necessary
				while (first.prime != second.prime) {
					if (first.prime >= second.prime) {
						while (first.prime != second.prime) {
							first = iterFirst.previous();
						}
					}
					if (first.prime <= second.prime) {
						while (first.prime != second.prime) {
							first = iterFirst.next();
						}
					}
				}
				first.multiplicity += second.multiplicity;
			} else if (!containsPrimeFactor(second.prime) && first.prime != second.prime) {
				add(second.prime, second.multiplicity);
			}
			if (iterFirst.hasNext() && iterFirst.cursor.next != tail) {
				first = iterFirst.next();
			}
		}
		updateValue();
	}

	/**
	 * Multiplies the integers represented by two PrimeFactorization objects.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return object of PrimeFactorization to represent the product
	 */
	public static PrimeFactorization multiply(PrimeFactorization pf1, PrimeFactorization pf2) {
		long product = pf1.value * pf2.value;
		PrimeFactorization pf = new PrimeFactorization(product);
		return pf;
	}

	/**
	 * Divides the represented integer v by n. Make updates to the list, value, size
	 * if divisible. No update otherwise. Refer to Section 3.2 in the project
	 * description for details.
	 * 
	 * @param n
	 * @return true if divisible false if not divisible
	 * @throws IllegalArgumentException if n <= 0
	 */
	public boolean dividedBy(long n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException();
		}
		if (this.value != -1 && this.value < n) {
			return false;
		}
		PrimeFactorization pf = new PrimeFactorization(n);
		dividedBy(pf);
		updateValue();
		return true;
	}

	/**
	 * Division where the divisor is represented in the factorization form. Update
	 * the linked list of this object accordingly by removing those nodes housing
	 * prime factors that disappear after the division. No update if this number is
	 * not divisible by pf. Algorithm details are given in Section 3.2.
	 * 
	 * @param pf
	 * @return true if divisible by pf false otherwise
	 */
	public boolean dividedBy(PrimeFactorization pf) {
		if ((this.value != -1 && pf.value != -1 && this.value < pf.value) || (this.value != -1 && pf.value == -1)) {
			return false;
		}
		if (this.value == pf.value) {
			clearList();
			value = 1;
			return true;
		} else {
			PrimeFactorization copy = new PrimeFactorization(this);
			PrimeFactorizationIterator iterCopy = copy.iterator();
			PrimeFactorizationIterator iterPf = pf.iterator();
			while (iterCopy.hasNext()) {
				if (iterPf.cursor == pf.tail) {
					copy.updateValue();
					this.head = copy.head;
					this.tail = copy.tail;
					this.size = copy.size;
					this.value = copy.value;
					return true;
				}
				if ((!iterCopy.hasNext() && iterPf.hasNext())) {
					return false;
				} else if (iterCopy.cursor.pFactor.prime >= iterPf.cursor.pFactor.prime) {
					if (iterCopy.cursor.pFactor.prime > iterPf.cursor.pFactor.prime) {
						return false;
					} else if (iterCopy.cursor.pFactor.prime == iterPf.cursor.pFactor.prime
							&& iterCopy.cursor.pFactor.multiplicity < iterPf.cursor.pFactor.multiplicity) {
						return false;
					} else if (iterCopy.cursor.pFactor.prime == iterPf.cursor.pFactor.prime
							&& iterCopy.cursor.pFactor.multiplicity >= iterPf.cursor.pFactor.multiplicity) {
						iterCopy.cursor.pFactor.multiplicity -= iterPf.cursor.pFactor.multiplicity;
						if (iterCopy.cursor.pFactor.multiplicity == 0) {
							while(iterCopy.pending == null) {
								iterCopy.next();
							}
							while(iterCopy.pending.pFactor.prime != iterPf.cursor.pFactor.prime) {
								iterCopy.next();
							}
							iterCopy.remove();
							iterCopy.previous();
						}
						iterCopy.next();
						iterPf.next();
						continue;
					}
				}
				if (iterCopy.hasNext()) {
					iterCopy.next();
				}
				if (iterPf.cursor == pf.tail) {
					copy.updateValue();
					this.head = copy.head;
					this.tail = copy.tail;
					this.size = copy.size;
					this.value = copy.value;
					return true;
				}
			}
			if (iterPf.cursor == pf.tail) {
				copy.updateValue();
				this.head = copy.head;
				this.tail = copy.tail;
				this.size = copy.size;
				this.value = copy.value;
				return true;
			}
		}
		return false;
	}

	/**
	 * Divide the integer represented by the object pf1 by that represented by the
	 * object pf2. Return a new object representing the quotient if divisible. Do
	 * not make changes to pf1 and pf2. No update if the first number is not
	 * divisible by the second one.
	 * 
	 * @param pf1
	 * @param pf2
	 * @return quotient as a new PrimeFactorization object if divisible null
	 *         otherwise
	 */
	public static PrimeFactorization dividedBy(PrimeFactorization pf1, PrimeFactorization pf2) {
		if (pf1.value % pf2.value != 0) {
			return null;
		}
		long quotient = pf1.value / pf2.value;
		PrimeFactorization pf = new PrimeFactorization(quotient);
		return pf;
	}

	// -----------------------
	// Greatest Common Divisor
	// -----------------------

	/**
	 * Computes the greatest common divisor (gcd) of the represented integer v and
	 * an input integer n. Returns the result as a PrimeFactor object. Calls the
	 * method Euclidean() if this.value != OVERFLOW.
	 * 
	 * It is more efficient to factorize the gcd than n, which can be much greater.
	 * 
	 * @param n
	 * @return prime factorization of gcd
	 * @throws IllegalArgumentException if n < 1
	 */
	public PrimeFactorization gcd(long n) throws IllegalArgumentException {
		PrimeFactorization pf = null;
		if (this.value != OVERFLOW) {
			pf = new PrimeFactorization(Euclidean(this.value, n));
		} else {
			pf = new PrimeFactorization(n);
			pf = gcd(pf);
		}
		return pf;
	}

	/**
	 * Implements the Euclidean algorithm to compute the gcd of two natural numbers
	 * m and n. The algorithm is described in Section 4.1 of the project
	 * description.
	 * 
	 * @param m
	 * @param n
	 * @return gcd of m and n.
	 * @throws IllegalArgumentException if m < 1 or n < 1
	 */
	public static long Euclidean(long m, long n) throws IllegalArgumentException {
		if (m < 1 || n < 1) {
			throw new IllegalArgumentException();
		}
		// if n is bigger than m, switch values
		if (m < n) {
			long tmp = m;
			m = n;
			n = tmp;
		}
		if (m % n == 0) {
			return n;
		} else {
			return Euclidean(n, m % n);
		}
	}

	/**
	 * Computes the gcd of the values represented by this object and pf by
	 * traversing the two lists. No direct computation involving value and pf.value.
	 * Refer to Section 4.2 in the project description on how to proceed.
	 * 
	 * @param pf
	 * @return prime factorization of the gcd
	 */
	public PrimeFactorization gcd(PrimeFactorization pf) {
		if (this.value == -1 || pf.value == -1) {
			updateValue();
		}
		long pfVal = pf.value;
		return gcd(pfVal);
	}

	/**
	 * 
	 * @param pf1
	 * @param pf2
	 * @return prime factorization of the gcd of two numbers represented by pf1 and
	 *         pf2
	 */
	public static PrimeFactorization gcd(PrimeFactorization pf1, PrimeFactorization pf2) {
		return pf1.gcd(pf2);
	}

	// ------------
	// List Methods
	// ------------

	/**
	 * Traverses the list to determine if p is a prime factor.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p
	 * @return true if p is a prime factor of the number v represented by this
	 *         linked list false otherwise
	 * @throws IllegalArgumentException if p is not a prime
	 */
	public boolean containsPrimeFactor(int p) throws IllegalArgumentException {
		if (!isPrime(p)) {
			throw new IllegalArgumentException();
		}
		PrimeFactorizationIterator iter = this.iterator();
		while (iter.hasNext()) {
			PrimeFactor pf = iter.next();
			if (pf != null && pf.prime == p) {
				return true;
			}
		}
		return false;
	}

	// The next two methods ought to be private but are made public for testing
	// purpose. Keep
	// them public

	/**
	 * Adds a prime factor p of multiplicity m. Search for p in the linked list. If
	 * p is found at a node N, add m to N.multiplicity. Otherwise, create a new node
	 * to store p and m.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p prime
	 * @param m multiplicity
	 * @return true if m >= 1 false if m < 1
	 */
	public boolean add(int p, int m) {
		if (m < 1) {
			return false;
		}
		boolean added = false;
		if (m >= 1) {
			if (containsPrimeFactor(p)) {
				Node currentNode = head.next;
				while (currentNode != null) {
					if (currentNode.pFactor.prime == p) {
						currentNode.pFactor.multiplicity += m;
						break;
					} else {
						currentNode = currentNode.next;
					}
				}
				added = true;
			} else {
				// add first prime
				if (size == 0) {
					Node temp = new Node(p, m);
					link(tail.previous, temp);
					size++;
					added = true;
				} else {
					Node current = head.next;
					while (current != tail) {
						// adds node in correct position (increasing order)
						if (current.pFactor.prime > p && current != null) {
							Node temp = new Node(p, m);
							link(current.previous, temp);
							size++;
							break;
						} else {
							current = current.next;
							if (current == tail) {
								Node temp = new Node(p, m);
								link(tail.previous, temp);
								size++;
							}
						}
					}
					added = true;
				}
			}
		}
		updateValue();
		return added;
	}

	/**
	 * Removes m from the multiplicity of a prime p on the linked list. It starts by
	 * searching for p. Returns false if p is not found, and true if p is found. In
	 * the latter case, let N be the node that stores p. If N.multiplicity > m,
	 * subtracts m from N.multiplicity. If N.multiplicity <= m, removes the node N.
	 * 
	 * Precondition: p is a prime.
	 * 
	 * @param p
	 * @param m
	 * @return true when p is found. false when p is not found.
	 * @throws IllegalArgumentException if m < 1
	 */
	public boolean remove(int p, int m) throws IllegalArgumentException {
		if (m < 1) {
			throw new IllegalArgumentException();
		}
		boolean removed = false;
		if (!containsPrimeFactor(p)) {
			return false;
		} else if (containsPrimeFactor(p)) {
			PrimeFactorizationIterator iter = this.iterator();
			while (iter.hasNext()) {
				PrimeFactor pf = iter.next();
				if (pf != null && pf.prime == p) {
					if (pf.multiplicity > m) {
						pf.multiplicity -= m;
					} else if (pf.multiplicity <= m) {
						Node n = iter.cursor.next;
						unlink(iter.cursor);
						iter.cursor = n;
						size--;
					}
				}
			}
			removed = true;
		}
		updateValue();
		return removed;
	}

	/**
	 * 
	 * @return size of the list
	 */
	public int size() {
		return size;
	}

	/**
	 * Writes out the list as a factorization in the form of a product. Represents
	 * exponentiation by a caret. For example, if the number is 5814, the returned
	 * string would be printed out as "2 * 3^2 * 17 * 19".
	 */
	@Override
	public String toString() {
		if(size == 0) {
			return "1";
		}
		String result = "";
		PrimeFactorizationIterator iter = this.iterator();
		while (iter.hasNext()) {
			PrimeFactor pf = iter.cursor.pFactor;
			result += pf.toString();
			pf = iter.next();
			if (iter.index != size) {
				result += " * ";
			}
		}
		return result;
	}

	// The next three methods are for testing, but you may use them as you like.

	/**
	 * @return true if this PrimeFactorization is representing a value that is too
	 *         large to be within long's range. e.g. 999^999. false otherwise.
	 */
	public boolean valueOverflow() {
		return value == OVERFLOW;
	}

	/**
	 * @return value represented by this PrimeFactorization, or -1 if
	 *         valueOverflow()
	 */
	public long value() {
		return value;
	}

	public PrimeFactor[] toArray() {
		PrimeFactor[] arr = new PrimeFactor[size];
		int i = 0;
		for (PrimeFactor pf : this)
			arr[i++] = pf;
		return arr;
	}

	@Override
	public PrimeFactorizationIterator iterator() {
		return new PrimeFactorizationIterator();
	}

	/**
	 * Doubly-linked node type for this class.
	 */
	private class Node {
		public PrimeFactor pFactor; // prime factor
		public Node next;
		public Node previous;

		/**
		 * Default constructor for creating a dummy node.
		 */
		public Node() {
			pFactor = null;
		}

		/**
		 * Precondition: p is a prime
		 * 
		 * @param p prime number
		 * @param m multiplicity
		 * @throws IllegalArgumentException if m < 1
		 */
		public Node(int p, int m) throws IllegalArgumentException {
			if (m < 1) {
				throw new IllegalArgumentException();
			}
			pFactor = new PrimeFactor(p, m);

		}

		/**
		 * Constructs a node over a provided PrimeFactor object.
		 * 
		 * @param pf
		 * @throws IllegalArgumentException
		 */
		public Node(PrimeFactor pf) {
			pFactor = pf;
		}

		/**
		 * Printed out in the form: prime + "^" + multiplicity. For instance "2^3".
		 * Also, deal with the case pFactor == null in which a string "dummy" is
		 * returned instead.
		 */
		@Override
		public String toString() {
			if (pFactor == null) {
				return "dummy";
			}
			return pFactor.prime + "^" + pFactor.multiplicity;
		}
	}

	private class PrimeFactorizationIterator implements ListIterator<PrimeFactor> {
		// Class invariants:
		// 1) logical cursor position is always between cursor.previous and cursor
		// 2) after a call to next(), cursor.previous refers to the node just returned
		// 3) after a call to previous() cursor refers to the node just returned
		// 4) index is always the logical index of node pointed to by cursor

		private Node cursor = head.next;
		private Node pending = null; // node pending for removal
		private int index = 0;

		// other instance variables ...

		/**
		 * Default constructor positions the cursor before the smallest prime factor.
		 */
		public PrimeFactorizationIterator() {
			cursor = head.next;
			pending = null;
			index = 0;
		}

		@Override
		public boolean hasNext() {
			return index < size;
		}

		@Override
		public boolean hasPrevious() {
			return index > 0;
		}

		@Override
		public PrimeFactor next() {
			PrimeFactor next = cursor.pFactor;
			cursor = cursor.next;
			pending = cursor.previous;
			index++;
			return next;
		}

		@Override
		public PrimeFactor previous() {
			cursor = cursor.previous;
			index--;
			pending = cursor.next;
			return cursor.pFactor;
		}

		/**
		 * Removes the prime factor returned by next() or previous()
		 * 
		 * @throws IllegalStateException if pending == null
		 */
		@Override
		public void remove() throws IllegalStateException {
			if (pending == null) {
				throw new IllegalStateException();
			} else {
				if (pending == cursor.next) {
					Node n = cursor.next;
					unlink(cursor);
					cursor = n;
				} else {
					unlink(cursor.previous);
					index--;	
				}
			}
			size--;
			pending = null;
			updateValue();
		}

		/**
		 * Adds a prime factor at the cursor position. The cursor is at a wrong position
		 * in either of the two situations below:
		 * 
		 * a) pf.prime < cursor.previous.pFactor.prime if cursor.previous != head. b)
		 * pf.prime > cursor.pFactor.prime if cursor != tail.
		 * 
		 * Take into account the possibility that pf.prime == cursor.pFactor.prime.
		 * 
		 * Precondition: pf.prime is a prime.
		 * 
		 * @param pf
		 * @throws IllegalArgumentException if the cursor is at a wrong position.
		 */
		@Override
		public void add(PrimeFactor pf) throws IllegalArgumentException {
			if (cursor.pFactor == null) {
				Node temp = new Node(pf);
				link(cursor.previous, temp);
				index++;
				size++;
				pending = null;
			} else if (pf.prime == cursor.pFactor.prime) {
				pf.multiplicity++;
				updateValue();
			} else if (cursor.previous != head && pf.prime < cursor.previous.pFactor.prime) {
				throw new IllegalArgumentException();
			} else if (cursor != tail && pf.prime > cursor.pFactor.prime) {
				throw new IllegalArgumentException();

			}

			else {
				Node temp = new Node(pf);
				link(cursor.previous, temp);
				index++;
				size++;
				pending = null;
			}
			updateValue();
		}

		@Override
		public int nextIndex() {
			return index;
		}

		@Override
		public int previousIndex() {
			return index - 1;
		}

		@Deprecated
		@Override
		public void set(PrimeFactor pf) {
			throw new UnsupportedOperationException(getClass().getSimpleName() + " does not support set method");
		}

		// Other methods you may want to add or override that could possibly facilitate
		// other operations, for instance, addition, access to the previous element,
		// etc.
		//
		// ...
		//
	}

	// --------------
	// Helper methods
	// --------------

	/**
	 * Inserts toAdd into the list after current without updating size.
	 * 
	 * Precondition: current != null, toAdd != null
	 */
	private void link(Node current, Node toAdd) {
		toAdd.previous = current;
		toAdd.next = current.next;
		current.next.previous = toAdd;
		current.next = toAdd;
	}

	/**
	 * Removes toRemove from the list without updating size.
	 */
	private void unlink(Node toRemove) {
		if (toRemove != null) {
			toRemove.previous.next = toRemove.next;
			toRemove.next.previous = toRemove.previous;
		}
	}

	/**
	 * Remove all the nodes in the linked list except the two dummy nodes.
	 * 
	 * Made public for testing purpose. Ought to be private otherwise.
	 */
	public void clearList() {
		head.next = tail;
		tail.previous = head;
	}

	/**
	 * Multiply the prime factors (with multiplicities) out to obtain the
	 * represented integer. Use Math.multiply(). If an exception is throw, assign
	 * OVERFLOW to the instance variable value. Otherwise, assign the multiplication
	 * result to the variable.
	 * 
	 */
	private void updateValue() {
		try {
			long newValue = 1;
			PrimeFactorizationIterator iter = this.iterator();
			while (iter.hasNext()) {
				PrimeFactor pf = iter.next();
				newValue = Math.multiplyExact(newValue, (long) Math.pow(pf.prime, pf.multiplicity));
			}
			value = newValue;
		}

		catch (ArithmeticException e) {
			value = OVERFLOW;
		}
	}
}