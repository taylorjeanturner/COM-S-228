package edu.iastate.cs228.hw2;

/**
 * This class is fully implemented.  It is used only by the draw() method in the RotationalPointScanner 
 * class. Please do NOT modify it for better display unless you know what you are doing.   
 */

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Plot {

	private static class MyFrame extends JFrame {
		private static final long serialVersionUID = 1L;

		public MyFrame(JPanel panel, String name) {
			super(name);
			setContentPane(panel);
			setSize(WINDOW_WIDTH, WINDOW_WIDTH);
			setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			setVisible(true);
		}
	}

	private static class MyPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		// coordinates of the center pixel of the square representing the origin
		private int ox = (WINDOW_WIDTH - 1) / 2;
		private int oy = (WINDOW_WIDTH - 1) / 2;

		private Point[] points; // array of points to draw
		private Segment[] segments; // array of segments to draw

		public MyPanel(Point[] pts, Segment[] segs) {
			int n = pts.length;
			points = new Point[n];
			for (int i = 0; i < n; i++)
				points[i] = pts[i];

			n = segs.length;
			segments = new Segment[n];
			for (int i = 0; i < n; i++)
				segments[i] = segs[i];

			setSize(WINDOW_WIDTH, WINDOW_WIDTH);
			setBackground(Color.WHITE);

			setVisible(true);
		}

		/**
		 * This method draws the x- and y-axes with the origin at the pixel position (ox, oy). 
		 * 
		 * @param g2
		 */
		private void drawAxes(Graphics2D g2) {
			g2.setPaint(Color.BLACK);
			g2.drawLine(MARGIN, oy, WINDOW_WIDTH - MARGIN, oy);
			g2.drawLine(ox, MARGIN, ox, WINDOW_WIDTH - 1);

			int px, py;

			// draw tick marks on the x-axis
			for (int i = MIN_XY; i <= MAX_XY; i++) {
				px = pixelCenter(i);
				if (i == 0)
					;
				else if (i % 10 == 0) {
					g2.drawLine(px, oy, px, oy - 8);
					if (i < 0)
						g2.drawString(String.valueOf(i), px - 9, oy + 15);
					else
						g2.drawString(String.valueOf(i), px - 6, oy + 15);
				} else if (i % 5 == 0)
					g2.drawLine(px, oy, px, oy - 4);
			}

			// draw tick marks on the y axis
			for (int i = MIN_XY; i <= MAX_XY; i++) {
				py = pixelCenter(i);
				if (i == 0)
					;
				else if (i % 10 == 0) {
					g2.drawLine(ox, py, ox + 8, py);
					if (i < 0)
						g2.drawString(String.valueOf(-i), ox - 20, py + 5);
					else
						g2.drawString(String.valueOf(-i), ox - 25, py + 5);
				} else if (i % 5 == 0)
					g2.drawLine(ox, py, ox + 4, py);
			}
		}

		/**
		 * Draw a point with Cartesian coordinates (x, y).
		 * 
		 * @param g2
		 * @param x
		 * @param y
		 */
		private void drawPoint(Graphics2D g2, Point p) {
			// pixel coordinates of the upper left corner of the square representing the point
			int px = pixelMin(+p.getX());
			int py = pixelMin(-p.getY());

			g2.setPaint(Color.RED);
			g2.drawRect(px, py, POINT_WIDTH, POINT_WIDTH);
			g2.fillRect(px, py, POINT_WIDTH, POINT_WIDTH); // fill the rectangle
		}

		/**
		 * Draw a line segment connecting (x1, y1) and (x2, y2). Both points are represented by the 
		 * center pixels of their squares.
		 * 
		 * Do not modify this method unless you prefer your own display.
		 * 
		 * @param g2
		 * @param x1
		 * @param y1
		 * @param x2
		 * @param y2
		 */
		private void drawSegment(Graphics2D g2, Segment s) {
			g2.setPaint(Color.BLUE);
			Point p = s.getP();
			Point q = s.getQ();
			g2.drawLine(pixelCenter(p.getX()), pixelCenter(-p.getY()), pixelCenter(q.getX()), pixelCenter(-q.getY()));
		}

		/**
		 * This method is called automatically when a window is created. Think of Graphics object as a pen.
		 */
		@Override
		public void paint(Graphics g) {
			super.paint(g);

			Graphics2D g2 = (Graphics2D) g; // pen for 2D drawing
			drawAxes(g2); // draw coordinate axes

			for (int i = 0; i < points.length; i++)
				drawPoint(g2, points[i]);

			for (int i = 0; i < segments.length; i++)
				drawSegment(g2, segments[i]);
		}

		/**
		 * x- or y-coordinate of the center pixel in the square representing a point with Cartesian 
		 * x-coordinate or y-coordinate i.
		 * 
		 * @param i
		 * @return
		 */
		private int pixelCenter(int i) {
			if (i < -MAX_XY || i > MAX_XY)
				return WINDOW_WIDTH + 1;

			return ox + i * POINT_WIDTH;
		}

		/**
		 * minimum x- or y-coordinate of any pixel in the square representing a point with Cartesian 
		 * x- or y-coordinate i.
		 * 
		 * @param i
		 * @return
		 */
		private int pixelMin(int i) {
			if (i < -MAX_XY || i > MAX_XY)
				return WINDOW_WIDTH + 1;

			return ox + i * POINT_WIDTH - POINT_WIDTH / 2;
		}
	}

	private static final HashMap<String, MyFrame> map = new HashMap<>();

	private static final int MAX_XY = 50; // maximum x- and y-coordinates
	private static final int MIN_XY = -50; // minimum Cartesian x- and y-coordinates

	// a point is plotted as a square with dimension POINT_WIDTH by POINT_WIDTH
	private static final int POINT_WIDTH = 5;

	// window is a square with dimension WINDOW_WIDTH by WINDOW_WIDTH.
	private static final int WINDOW_WIDTH = 601;
	
	// margin for plotting the x- and y-axes 
	private static final int MARGIN = 35; 

	/**
	 * Create a window and draw an array of points and an array of segments.
	 * 
	 * @param pts
	 * @param segs
	 * @return
	 */
	public static void myFrame(final Point[] pts, final Segment[] segs, final String name) {
		if (map.containsKey(name))
			map.get(name).dispose();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				map.put(name, new MyFrame(new MyPanel(pts, segs), name));
			}
		});
	}

}
