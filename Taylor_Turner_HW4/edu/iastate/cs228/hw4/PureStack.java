package edu.iastate.cs228.hw4;

/**
 *  
 * @author Steve Kautz 
 *
 */


public interface PureStack<E>
{
	/**
	 * Adds an element to the top of the
	 */
	void push(E item);
	
	/**
	* Removes and returns the top element of
	* the stack. Throws
	* NoSuchElementException if the
	* stack is empty
	*/
	E pop();
	
	/**
	* Returns the top element of the stack
	* without removing it. Throws
	* NoSuchElementException if the stack
	* is empty
	*/
	E peek();
	
	/**
	* Return true if the stack is empty,
	* false otherwise
	*/
	boolean isEmpty();
	
	/**
	* Returns the number of elements in the
	* stack.
	*/
	int size();
}