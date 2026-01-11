package test;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.york.modules.testing.FirstOrderEquation;

public class FirstOrderEquationTest {

	@Test
	public final void testOfMethod() {
	    FirstOrderEquation equation = new FirstOrderEquation(2.0, 3.0);

	    assertEquals("f(0) should return 3.0", 3.0, equation.of(0), 0.0001);
	    assertEquals("f(1) should return 5.0", 5.0, equation.of(1), 0.0001);
	    assertEquals("f(2) should return 7.0", 7.0, equation.of(2), 0.0001);
	    assertEquals("f(-2) should return -1.0", -1.0, equation.of(-2), 0.0001);
	}


	@Test
	public final void testToString() {
	    // Test with positive coefficients
	    FirstOrderEquation equation1 = new FirstOrderEquation(2.0, 3.0);
	    assertEquals("String representation should be '2.0x+3.0'", "2.0x+3.0", equation1.toString());

	    // Test with negative 'b'
	    FirstOrderEquation equation2 = new FirstOrderEquation(2.0, -3.0);
	    assertEquals("String representation should be '2.0x+-3.0'", "2.0x+-3.0", equation2.toString());
	    
	    // Test with negative 'a'
	    FirstOrderEquation equation3 = new FirstOrderEquation(-2.0, 3.0);
	    assertEquals("String representation should be '-2.0x+3.0'", "-2.0x+3.0", equation3.toString());

	    // Test with both coefficients negative
	    FirstOrderEquation equation4 = new FirstOrderEquation(-2.0, -3.0);
	    assertEquals("String representation should be '-2.0x+-3.0'", "-2.0x+-3.0", equation4.toString());  
	    
	}
	
	
	

}





