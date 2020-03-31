package edu.iastate.cs228.hw2;

import java.io.File;

/**
 * 
 * @author Taylor Turner
 *
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * 
 * This class sorts all the points in an array by polar angle with respect to a
 * reference point whose x and y coordinates are respectively the medians of the
 * x and y coordinates of the original points.
 * 
 * It records the employed sorting algorithm as well as the sorting time for
 * comparison.
 *
 */
public class RotationalPointScanner {
	private Point[] points;

	private Point medianCoordinatePoint; // point whose x and y coordinates are respectively the medians of
											// the x coordinates and y coordinates of those points in the array
											// points[].
	private Algorithm sortingAlgorithm;

	protected String outputFileName; // "select.txt", "insert.txt", "merge.txt", or "quick.txt"

	protected long scanTime; // execution time in nanoseconds.

	/**
	 * This constructor accepts an array of points and one of the four sorting
	 * algorithms as input. Copy the points into the array points[]. Set
	 * outputFileName.
	 * 
	 * @param pts input array of points
	 * @throws IllegalArgumentException if pts == null or pts.length == 0.
	 */
	public RotationalPointScanner(Point[] pts, Algorithm algo) throws IllegalArgumentException {
		if (pts == null || pts.length == 0) {
			throw new IllegalArgumentException();
		}
		points = new Point[pts.length];
		for (int i = 0; i < pts.length; i++) {
			Point p = new Point(pts[i]);
			points[i] = p;
		}
		sortingAlgorithm = algo;
		if (sortingAlgorithm == Algorithm.InsertionSort) {
			outputFileName = "insert.txt";
		} else if (sortingAlgorithm == Algorithm.MergeSort) {
			outputFileName = "merge.txt";
		} else if (sortingAlgorithm == Algorithm.QuickSort) {
			outputFileName = "quick.txt";
		} else if (sortingAlgorithm == Algorithm.SelectionSort) {
			outputFileName = "selection.txt";
		}
	}

	/**
	 * This constructor reads points from a file. Set outputFileName.
	 * 
	 * @param inputFileName
	 * @throws FileNotFoundException
	 * @throws InputMismatchException if the input file contains an odd number of
	 *                                integers
	 */
	protected RotationalPointScanner(String inputFileName, Algorithm algo)
			throws FileNotFoundException, InputMismatchException {
		sortingAlgorithm = algo;
		if (sortingAlgorithm == Algorithm.InsertionSort) {
			outputFileName = "insert.txt";
		} else if (sortingAlgorithm == Algorithm.MergeSort) {
			outputFileName = "merge.txt";
		} else if (sortingAlgorithm == Algorithm.QuickSort) {
			outputFileName = "quick.txt";
		} else if (sortingAlgorithm == Algorithm.SelectionSort) {
			outputFileName = "selection.txt";
		}
		File file = new File(inputFileName);
		Scanner scan = new Scanner(file); // Scanner for getting length
		Scanner sc = new Scanner(file); // Scanner for getting points
		int numCount = 0;
		int length = 0;
		while (scan.hasNextInt()) {
			scan.nextInt();
			numCount++;
		}
		if (numCount % 2 != 0) {
			scan.close();
			sc.close();
			throw new InputMismatchException();
		}
		length = numCount / 2;
		points = new Point[length];
		for (int i = 0; i < length; i++) {
			points[i] = new Point(sc.nextInt(), sc.nextInt());
		}
		scan.close();
		sc.close();
	}

	/**
	 * Carry out three rounds of sorting using the algorithm designated by
	 * sortingAlgorithm as follows:
	 * 
	 * a) Sort points[] by the x-coordinate to get the median x-coordinate. b) Sort
	 * points[] again by the y-coordinate to get the median y-coordinate. c)
	 * Construct medianCoordinatePoint using the obtained median x- and
	 * y-coordinates. d) Sort points[] again by the polar angle with respect to
	 * medianCoordinatePoint.
	 * 
	 * Based on the value of sortingAlgorithm, create an object of SelectionSorter,
	 * InsertionSorter, MergeSorter, or QuickSorter to carry out sorting. Copy the
	 * sorting result back onto the array points[] by calling the method getPoints()
	 * in AbstractSorter.
	 * 
	 * @param algo
	 * @return
	 */
	public void scan() {
		AbstractSorter aSorter = null;
		if (sortingAlgorithm == Algorithm.InsertionSort) {
			aSorter = new InsertionSorter(points);
		} else if (sortingAlgorithm == Algorithm.MergeSort) {
			aSorter = new MergeSorter(points);
		} else if (sortingAlgorithm == Algorithm.QuickSort) {
			aSorter = new QuickSorter(points);
		} else if (sortingAlgorithm == Algorithm.SelectionSort) {
			aSorter = new SelectionSorter(points);
		}
		Point x1, y1;
		int x, y;
		// Sort by x
		long xStart = System.nanoTime();
		aSorter.setComparator(0);
		aSorter.sort();
		x1 = aSorter.getMedian();
		x = x1.getX();
		long xTime = System.nanoTime() - xStart;
		// Sort by y
		long yStart = System.nanoTime();
		aSorter.setComparator(1);
		aSorter.sort();
		y1 = aSorter.getMedian();
		y = y1.getY();
		long yTime = System.nanoTime() - yStart;
		// Set median coordinate point
		medianCoordinatePoint = new Point(x, y);
		aSorter.setReferencePoint(medianCoordinatePoint);
		// Sort by polar angle
		long pStart = System.nanoTime();
		aSorter.setComparator(2);
		aSorter.sort();
		aSorter.getPoints(points);
		long pTime = System.nanoTime() - pStart;
		scanTime = xTime + yTime + pTime;
	}

	/**
	 * Outputs performance statistics in the format:
	 * 
	 * <sorting algorithm> <size> <time>
	 * 
	 * For instance,
	 * 
	 * selection sort 1000 9200867
	 * 
	 * Use the spacing in the sample run in Section 2 of the project description.
	 */
	public String stats() {
		String result = sortingAlgorithm + " " + points.length + " " + scanTime;
		return result;

	}

	/**
	 * Write points[] after a call to scan(). When printed, the points will appear
	 * in order of polar angle with respect to medianCoordinatePoint with every
	 * point occupying a separate line. The x and y coordinates of the point are
	 * displayed on the same line with exactly one blank space in between.
	 */
	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < points.length; i++) {
			result += points[i].getX() + " " + points[i].getY() + "\n";
		}
		return result;
	}

	/**
	 * 
	 * This method, called after scanning, writes point data into a file by
	 * outputFileName. The format of data in the file is the same as printed out
	 * from toString(). The file can help you verify the full correctness of a
	 * sorting result and debug the underlying algorithm.
	 * 
	 * @throws FileNotFoundException
	 */
	public void writePointsToFile() throws FileNotFoundException {
		File file = new File(outputFileName);
		PrintWriter pw = new PrintWriter(file);
		pw.write(this.toString());
		pw.close();
	}

	/**
	 * This method is called after each scan for visually check whether the result
	 * is correct. You just need to generate a list of points and a list of
	 * segments, depending on the value of sortByAngle, as detailed in Section 4.1.
	 * Then create a Plot object to call the method myFrame().
	 */
	public void draw() {
		int numSegs = 0; // number of segments to draw
		// Remove duplicate points
		Point[] temp = new Point[points.length];
		int j = 0;
		for (int i = 0; i < points.length - 1; i++) {
			if (!points[i].equals(points[i + 1])) {
				temp[j++] = points[i];
			}
		}
		temp[j++] = points[points.length - 1];
		for (int i = 0; i < j; i++) {
			points[i] = temp[i];
		}
		numSegs = points.length * 2;
		Segment[] segments = new Segment[numSegs];
		// Creates segments connecting the outer points
		for (int i = 0; i < points.length + 1; i++) {
			if (i + 1 < points.length) {
				segments[i] = new Segment(points[i], points[i + 1]);
			}
		}
		// Creates segments connecting outer points to MCR
		for (int l = points.length - 1, k = 0; k < points.length; l++, k++) {
			segments[l] = new Segment(points[k], medianCoordinatePoint);
		}
		// Creates one segment that connects the first points to the last
		segments[segments.length - 1] = new Segment(points[0], points[points.length - 1]);
		String sort = null;
		switch (sortingAlgorithm) {
		case SelectionSort:
			sort = "Selection Sort";
			break;
		case InsertionSort:
			sort = "Insertion Sort";
			break;
		case MergeSort:
			sort = "Mergesort";
			break;
		case QuickSort:
			sort = "Quicksort";
			break;
		default:
			break;
		}
		// The following statement creates a window to display the sorting result.
		Plot.myFrame(points, segments, sort);
	}
}
