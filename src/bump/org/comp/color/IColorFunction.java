package bump.org.comp.color;

import java.awt.Color;
/**
 * Provides a common interface that provides colors in various ways.
 * @author Jaked122
 *
 */
public interface IColorFunction {
	/**
	 * retrieve the Color calculated by the color function at the given step.
	 * 
	 * @param i
	 *            the step of the color to retrieve
	 * @return the color computed by the function.
	 */
	public Color getColorAtStep(int i);

	/**
	 * The maximum step that can be service in the Color function
	 * 
	 * @return the maximum number of steps without running the risk of recieving
	 *         an error
	 */
	public int getMaxStep();
}
