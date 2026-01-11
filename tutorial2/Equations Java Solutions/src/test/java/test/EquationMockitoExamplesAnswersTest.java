package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import uk.ac.york.modules.testing.Equation;
import uk.ac.york.modules.testing.FirstOrderEquation;


public class EquationMockitoExamplesAnswersTest {
	
	
	@Test
	public void testMissingMockStubbingFails() {

	    Equation mockEq = mock(Equation.class);

	    // 1. Stub eq.of(3) so the behaviour is defined
	    when(mockEq.of(3)).thenReturn(9.0);

	    double result = mockEq.of(3);

	    // 2. Correct expected value
	    assertEquals(9.0, result, 0.0001);
	}
	
	
	@Test
	public void testEquationValidatorFails() {

	    class EquationValidator {
	        boolean isPositiveAt(Equation e, double x) {
	            double value = e.of(x);
	            return value > 0;
	        }
	    }

	    Equation mockEq = mock(Equation.class);
	    EquationValidator validator = new EquationValidator();

	    // 1. Stub eq.of(5.0) to control the validation outcome
	    when(mockEq.of(5.0)).thenReturn(-3.0);  // choose a negative number

	    // 2. Validate behaviour based on stubbed output
	    boolean isPos = validator.isPositiveAt(mockEq, 5.0);

	    // 3. Expected result based on our stub (negative -> false)
	    assertEquals(false, isPos);

	    // 4. Verify interaction with the dependency
	    verify(mockEq).of(5.0);
	}
	
	
	@Test
	public void testLoggingEquationLogsResult() {

	    // Local helper types kept inside the test (same as student version)
	    interface Logger {
	        void log(String message);
	    }

	    class LoggingEquation {
	        private final FirstOrderEquation eq;
	        private final Logger logger;

	        LoggingEquation(FirstOrderEquation eq, Logger logger) {
	            this.eq = eq;
	            this.logger = logger;
	        }

	        public double compute(double x) {
	            double result = eq.of(x);
	            logger.log("Result: " + result);
	            return result;
	        }
	    }

	    // 1. Mock the equation dependency
	    FirstOrderEquation mockEq = mock(FirstOrderEquation.class);

	    // 2. Stub behaviour: eq.of(2.0) should return 7.0
	    when(mockEq.of(2.0)).thenReturn(7.0);

	    // 3. Mock the logger dependency
	    Logger mockLogger = mock(Logger.class);

	    LoggingEquation le = new LoggingEquation(mockEq, mockLogger);

	    // 4. Act: compute the result
	    double output = le.compute(2.0);

	    // 5. Assert the numerical result
	    assertEquals(7.0, output, 0.0001);

	    // 6. Verify that eq.of(2.0) was called exactly once
	    verify(mockEq).of(2.0);

	    // 7. Verify that the logger was called with the correct message
	    verify(mockLogger).log("Result: 7.0");
	}
	


	
	
	

	
	
	
	

}
