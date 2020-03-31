package edu.iastate.cs228.hw2;

/**
 * 
 * Fully implemented class to represent a line segment for drawing. Used by the class Plot and the 
 * draw() method in the class AbstractSorter.
 *
 */
public class Segment 
{
	private Point p; 
	private Point q; 
	
	public Segment(Point p0, Point q0)
	{
		p = new Point(p0); 
		q = new Point(q0); 
	}
	
	public Point getP()
	{
		return p; 
	}
	
	public Point getQ()
	{
		return q; 
	}
}
