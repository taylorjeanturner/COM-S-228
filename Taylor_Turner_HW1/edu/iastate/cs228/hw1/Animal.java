package edu.iastate.cs228.hw1;

/**
 *  
 * @author Taylor Turner
 *
 */

/*
 * This class is to be extended by the Badger, Fox, and Rabbit classes. 
 */
public abstract class Animal extends Living implements MyAge
{
	protected int age;   // age of the animal 

	@Override
	/**
	 * 
	 * @return age of the animal 
	 */
	public int myAge()
	{
		return age; 
	}
}
