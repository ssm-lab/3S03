/**
 * 
 */
package uk.ac.york.modules.testing;

/**
 * This class represents a*sin(b*x)^c+d
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Feb 15, 2010
 *
 */
public class SinusBasedEquation extends Equation {
	/**
	 * a in a*sin(b*x)^c+d.
	 */
	double a;
	
	/**
	 * b in a*sin(b*x)^c+d.
	 */
	double b;

	/**
	 * c in a*sin(b*x)^c+d.
	 */
	double c;

	/**
	 * d in a*sin(b*x)^c+d.
	 */
	double d;

	/**
	 * Constructor for sinus-based equations.
	 */
	public SinusBasedEquation(double a, double b, double c, double d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	
	}
	@Override
	public double of(double x) {
		return a*Math.pow(Math.sin(b*x),c)+d;
	}
	@Override
	public String toString() {
		return a+"(sin("+b+"x)^"+c+")+"+d;
	}
}
