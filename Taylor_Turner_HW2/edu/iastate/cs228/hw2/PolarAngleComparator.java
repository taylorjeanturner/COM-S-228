package edu.iastate.cs228.hw2;

/**
 *  
 * @author Taylor Turner
 *
 */

import java.util.Comparator;

/**
 * 
 * This class compares two points p1 and p2 by polar angle with respect to a
 * reference point.
 *
 */
public class PolarAngleComparator implements Comparator<Point> {
	private Point referencePoint;

	/**
	 * 
	 * @param p reference point
	 */
	public PolarAngleComparator(Point p) {
		referencePoint = p;
	}

	/**
	 * Call comparePolarAngle() and compareDistance().
	 * 
	 * @param p1
	 * @param p2
	 * @return 0 if p1 and p2 are the same point -1 otherwise, if one of the
	 *         following two conditions holds:
	 * 
	 *         a) the polar angle of p1 w.r.t. referencePoint is less than that of
	 *         p2. b) the two points have the same polar angle but p1 is closer to
	 *         referencePoint than p2.
	 * 
	 *         1 otherwise.
	 * 
	 */
	public int compare(Point p1, Point p2) {
		if (p1.equals(p2)) {
			return 0;
		} else if (comparePolarAngle(p1, p2) < 0 || (comparePolarAngle(p1, p2) == 0 && compareDistance(p1, p2) < 0)) {
			return -1;
		}
		else {
			return 1;
		}
	}

	/**
	 * Compare the polar angles of two points p1 and p2 with respect to
	 * referencePoint. Use dot and cross products. Do not use trigonometric
	 * functions.
	 * 
	 * All polar angles are within the range [0, 2 * pi).
	 * 
	 * Ought to be private but made public for testing purpose.
	 * 
	 * @param p1
	 * @param p2
	 * @return 0 if one of the following two situations happens:
	 * 
	 *         a) p1 and p2 are the same point (this case is checked already if the
	 *         method is called within compare()). b) none is equal to
	 *         referencePoint, but the vectors p1 - referencePoint and p2 -
	 *         referencePoint have a zero cross product and a positive dot product.
	 * 
	 *         -1 otherwise, if p1 equals referencePoint; otherwise, if p2 is not
	 *         equal to referencePoint and one of the following situations below
	 *         happens:
	 * 
	 *         1) p1.y < referencePoint.y and p2.y < referencePoint.y, and the cross
	 *         product of p1 - referencePoint and p2 - referencePoint is positive.
	 * 
	 *         2) p1.y == referencePoint.y and one of the following three situations
	 *         happens:
	 * 
	 *         a) p2.y < referencePoint.y b) p2.y == referencePoint.y and p1.x >
	 *         referencePoint.x and p2.x < referencePoint.x c) p2.y >
	 *         referencePoint.y and p1.x > referencePoint.x
	 * 
	 *         3) p1.y > referencePoint.y and one of the following three situations
	 *         happens:
	 * 
	 *         a) p2.y > referencePoint.y and the cross product of p1 -
	 *         referencePoint and p2 - referencePoint is positive. b) p2.y ==
	 *         referencePoint.y and p2.x < referencePoint.x. c) p2.y <
	 *         referencePoint.y
	 * 
	 *         1 otherwise.
	 */
	public int comparePolarAngle(Point p1, Point p2) {
		if (p1.equals(p2) || (!p1.equals(referencePoint) && !p2.equals(referencePoint) && crossProduct(p1, p2) == 0
				&& dotProduct(p1, p2) > 0)) {
			return 0;
		} else if (p1.equals(referencePoint)) {
			return -1;
		} else if (!p2.equals(referencePoint)
				&& ((p1.getY() < referencePoint.getY() && p2.getY() < referencePoint.getY() && crossProduct(p1, p2) > 0)
						|| (p1.getY() == referencePoint.getY() && ((p2.getY() < referencePoint.getY())
								|| (p2.getY() == referencePoint.getY() && p1.getX() > referencePoint.getX()
										&& p2.getX() < referencePoint.getX())
								|| (p2.getY() > referencePoint.getY() && p1.getX() > referencePoint.getX())))
						|| (p1.getY() > referencePoint.getY()
								&& ((p2.getY() > referencePoint.getY() && crossProduct(p1, p2) > 0)
										|| (p2.getY() == referencePoint.getY() && p2.getX() < referencePoint.getX())
										|| (p2.getY() < referencePoint.getY()))))) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * Compare the distances of two points p1 and p2 to referencePoint. Use dot
	 * products. Do not take square roots.
	 * 
	 * Ought to be private but made public for testing purpose.
	 * 
	 * @param p1
	 * @param p2
	 * @return 0 if p1 and p2 are equidistant to referencePoint -1 if p1 is closer
	 *         to referencePoint than p2 1 otherwise
	 */
	public int compareDistance(Point p1, Point p2) {
		if (dotProduct(p1, p1) == dotProduct(p2, p2)) {
			return 0;
		} else if (dotProduct(p2, p2) > dotProduct(p1, p1)) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return cross product of two vectors p1 - referencePoint and p2 -
	 *         referencePoint
	 */
	private int crossProduct(Point p1, Point p2) {
		int x1y2 = (p1.getX() - referencePoint.getX()) * (p2.getY() - referencePoint.getY());
		int x2y1 = (p2.getX() - referencePoint.getX()) * (p1.getY() - referencePoint.getY());
		return x1y2 - x2y1;
	}

	/**
	 * 
	 * @param p1
	 * @param p2
	 * @return dot product of two vectors p1 - referencePoint and p2 -
	 *         referencePoint
	 */
	private int dotProduct(Point p1, Point p2) {
		int x1x2 = (p1.getX() - referencePoint.getX()) * (p2.getX() - referencePoint.getX());
		int y1y2 = (p1.getY() - referencePoint.getY()) * (p2.getY() - referencePoint.getY());
		return x1x2 + y1y2;
	}
}
