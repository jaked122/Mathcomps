package bump.org.comp;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;

import bump.org.comp.color.IColorFunction;
import bump.org.util.ChartingUtil;

/**
 * <p>
 * Shows multiple lines on a single chart.
 * </p>
 * <p>
 * Works in a similar way to the single chart component (ChartingThing), but
 * shows multiple sets of data.
 * </p>
 * <p>
 * Data has a color determined by a IColorFunction(typically a ColorList) which
 * provides its own methods of changing the color scheme.
 * </p>
 * 
 * @author Jaked122
 * 
 */
public class MultiLineChart extends SingleLineChart {
	public void AddSeries(ArrayList<Integer> g) {
		this.data.add(g);
		repaint();
	}

	public void setSeriesAtIndex(ArrayList<Integer> g, int c) {
		this.data.set(c, g);
	}

	public void paint(Graphics g) {
		Graphics2D put = (Graphics2D) g.create();

		double vstep;
		double hstep = (getWidth() / data.size());
		vstep = (getHeight() / (ChartingUtil.getdiff(data, (Object) null)));

		// Essentially configuring whether or not the antialiasing is on
		RenderingHints h;
		if (isAntialias()) {
			h = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);

		} else {
			h = new RenderingHints(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		// If set for bicubic rendering, add it.
		if (isBicubic()) {
			h.add(new RenderingHints(RenderingHints.KEY_INTERPOLATION,
					RenderingHints.VALUE_INTERPOLATION_BICUBIC));
		}
		put.addRenderingHints(h);
		put.setColor(getBackgroundcolor());
		// fill in the background.
		put.fill(new Rectangle2D.Float(0, 0, getWidth(), getHeight()));
		put.setColor(ChartingUtil.createInverse(getBackgroundcolor()));
		for (int i = 0; i < data.size(); i++) {
			put.draw(new Line2D.Double(hstep * (i - 1), 0, hstep * (i), 0));
			if (i % xtick == 0)
				put.draw(new Line2D.Double(hstep * (i - 1), -vstep,
						hstep * (i), vstep));
		}
		// draw the vertical ticks.
		put.draw(new Line2D.Double(getWidth() / 2, 0, getWidth() / 2,
				getHeight()));
		for (int i = 0; i * vstep < getHeight() / (2 * ChartingUtil.Max(data)); i++) {
			put.draw(new Line2D.Double(0, vstep * i, hstep, vstep * i));
		}
		// allow the data line to overwrite the current colors.
		put.setPaintMode();
		put.setColor(linecolor);

		for (int c = 0; c < data.size(); c++) {
			// if the color function can't provide for the number of lines, wrap
			// around so that it does not throw an exception.
			put.setColor(color.getColorAtStep(c % color.getMaxStep()));
			// Initialize the path.
			Path2D.Double e = new Path2D.Double();
			// Move the start point of the path to the zero index of the list in
			// question right now.
			e.moveTo(0, vstep * data.get(c).get(0));
			for (int i = 2; i < data.size(); i++) {
				if (!isCurvelines())
					e.lineTo(hstep * i, data.get(c).get(i) * vstep);
				else {
					// quadratic interpolation
					// Just interpolate with the former point.
					e.quadTo(hstep * (i - 1) + (i % 3 == 0 ? 4 : 1), vstep
							* data.get(c).get(i - 1) + 2, i * hstep, data
							.get(c).get(i) * vstep);
				}
			}
			put.draw(e);

		}

	}

	@Override
	public final Color getBackground() {
		return this.getBackgroundcolor();
	}

	@Override
	public final void setBackground(Color g) {
		this.setBackgroundcolor(g);
		repaint();
	}

	/**
	 * <p>
	 * The series that is manipulated by the setTargetSeries and returned by
	 * getTargetSeries functions.
	 * </p>
	 * <p>
	 * Controls which dataseries is manipulated by the addData and setData
	 * functions
	 * </p>
	 */
	private int targetseries = 0;

	/**
	 * Set the new dataseries to manipulate.
	 * 
	 * @param i
	 *            the new series to target
	 * @see bump.org.comp.MultiLineChart.targetseries
	 */
	public final void setTargetSeries(int i) {
		targetseries = i;
	}

	/**
	 * Get the dataseries that is being modified by the addData and the setData
	 * commands.
	 * 
	 * @return the dataseries being manipulated
	 * @see bump.org.comp.MultiLineChart.targetseries
	 */
	public final int getTargetSeries() {
		return targetseries;
	}

	/**
	 * Add data to the current target series.
	 * 
	 * @param i
	 *            <p>
	 *            The array of data to add to the chart.
	 *            </p>
	 */
	@Override
	public final void addData(int[] i) {
		ArrayList<Integer> g = data.get(targetseries);
		for (int e : i)
			g.add(e);
		data.set(targetseries, g);
		repaint();
	}

	/**
	 * @return the color
	 */
	public final IColorFunction getColorFunction() {
		return color;
	}

	/**
	 * @param color
	 *            the color function to use in charting.
	 */
	public final void setColorFunction(IColorFunction color) {
		this.color = color;
	}

	protected IColorFunction color;
	ArrayList<ArrayList<Integer>> data;
}
