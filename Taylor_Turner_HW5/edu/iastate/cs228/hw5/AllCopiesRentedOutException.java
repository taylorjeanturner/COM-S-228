package edu.iastate.cs228.hw5;

/**
 * 
 * @author Yan-Bin Jia
 * 
 */


/**
 * Exception is thrown if a requested movie has no video copy left in the store.
 *
 */
public class AllCopiesRentedOutException extends Exception
{
	public AllCopiesRentedOutException()
	{
		super();
	}
	  
	public AllCopiesRentedOutException(String msg)
	{
	    super(msg);
	}

}
