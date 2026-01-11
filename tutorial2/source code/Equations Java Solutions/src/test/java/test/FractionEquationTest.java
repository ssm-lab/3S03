package test;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.york.modules.testing.FractionEquation;

public class FractionEquationTest {

	@Test
    public void testOfMethod() {
        FractionEquation eq = new FractionEquation(6.0, 2.0);

        // Test with x = 1
        assertEquals("f(1) should return 6/(1+2) = 2.0", 2.0, eq.of(1), 0.0001);

        // Test with x = 0
        assertEquals("f(0) should return 6/(0+2) = 3.0", 3.0, eq.of(0), 0.0001);

        // Test with x = -2 (should not divide by zero)
        assertEquals("f(-2) should return 6/(-2+2) = division by zero (Infinity)", Double.POSITIVE_INFINITY, eq.of(-2), 0.0001);
    }

    @Test
    public void testToString() {
        FractionEquation eq1 = new FractionEquation(4.0, 2.0);
        assertEquals("String representation should be '4.0/(x+2.0)'", "4.0/(x+2.0)", eq1.toString());

        // negative b
        FractionEquation eq2 = new FractionEquation(5.0, -3.0);
        assertEquals("String representation should be '5.0/(x+-3.0)'", "5.0/(x+-3.0)", eq2.toString());

        // zero coefficients
        FractionEquation eq3 = new FractionEquation(0.0, 0.0);
        assertEquals("String representation should be '0.0/(x+0.0)'", "0.0/(x+0.0)", eq3.toString());
    }

}
