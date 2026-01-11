/**
 * 
 */
package uk.ac.york.modules.testing;

/**
 * This class represents a first order equation (f(x)=ax+b)
 * 
 * @author Manuel Oriol (manuel@cs.york.ac.uk)
 * @date Feb 14, 2010
 *
 */
public class FirstOrderEquation extends Equation {

	double a;
	double b;
	
	/**
	 * Creates a first-order equation.
	 * 
	 * @param a a in ax+b
	 * @param b b in ax+b
	 */
	public FirstOrderEquation(double a, double b) {
		this.a=a;
		this.b=b;
	}
	
	/* (non-Javadoc)
	 * @see uk.ac.york.modules.testing.Equation#toString()
	 */
	@Override
	public String toString() {
		return a+"x+"+b;
	}

	/* (non-Javadoc)
	 * @see uk.ac.york.modules.testing.Equation#of(double)
	 */
	@Override
	public double of(double x) {
		return a*x+b;
	}
	
}
