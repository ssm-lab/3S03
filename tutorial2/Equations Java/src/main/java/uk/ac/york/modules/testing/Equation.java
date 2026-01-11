/**
 * 
 */
package uk.ac.york.modules.testing;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JOptionPane;

/**
 * This class represents an equation.
 * Note that there should be only one constructor for a given equation type with only double arguments.
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Feb 14, 2010
 *
 */
public abstract class Equation {

	
	/**
	 * Static method creating an instance of a given Equation type. 
	 * The method asks values of parameters through option panes.
	 * 
	 * @param equationType the class of the equation 
	 * @return the Equation
	 */
	@SuppressWarnings("unchecked")
	public static Equation createEquationFromType(Class equationType) {
		Constructor c =  equationType.getConstructors()[0];
		int n_arguments = c.getParameterTypes().length;
		Object[] arguments = new Double [n_arguments];
		for (int i=0; i<n_arguments;i++) {
			//ask for values
			String s = JOptionPane.showInputDialog(null, ((char)(((byte)'a')+i))+" =", 
					"Enter argument", JOptionPane.QUESTION_MESSAGE);
			arguments[i] = Double.parseDouble(s);
		}
		try {
			// we return the new instance
			return (Equation)c.newInstance(arguments);
		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "alert", "alert", JOptionPane.ERROR_MESSAGE);
		}
		// if there was no exception
		return null;
	}
	
	@Override
	public String toString() {
		return "A "+this.getClass().getName();
	}
	
	/**
	 * Calculates the value of this equation for x.
	 * 
	 * @param x the x to use with f(x)
	 * @return the result for this equation given x.
	 */
	public double of(double x) {
		
		return 0.0d;
	}
	
}
