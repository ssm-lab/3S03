/**
 * 
 */
package uk.ac.york.modules.testing;

/**
 * This class represents an equation of the type f(x) = a/(x+b).
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Feb 14, 2010
 *
 */
public class FractionEquation extends Equation {

	/**
	 * a in a/(x+b).
	 */
	double a;
	
	/**
	 * b in a/(x+b).
	 */
	double b;
	/**
	 * 
	 */
	public FractionEquation(double a, double b) {
		this.a = a;
		this.b = b;
	
	}
	@Override
	public double of(double x) {
		return a/(x+b);
	}
	@Override
	public String toString() {
		return a+"/(x+"+b+")";
	}

}
