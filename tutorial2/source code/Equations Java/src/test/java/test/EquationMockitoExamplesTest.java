package test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;

import uk.ac.york.modules.testing.Equation;
import uk.ac.york.modules.testing.EquationPanel;
import uk.ac.york.modules.testing.FirstOrderEquation;


public class EquationMockitoExamplesTest {

    // shows basic mocking and stubbing by controlling a method’s return value and verifying the call.
    @Test
    public void testMockEquationReturnsStubbedValue() {
        Equation mockEq = mock(Equation.class);
        when(mockEq.of(2.0)).thenReturn(10.0);

        double result = mockEq.of(2.0);

        assertEquals(10.0, result, 0.0001);
        verify(mockEq).of(2.0); // verify interaction
    }

    // shows how spies call real methods unless stubbed, and how to verify interactions on a spy
    @Test
    public void testSpyEquationWithConditionalStub() {
        FirstOrderEquation real = new FirstOrderEquation(2, 3); // f(x)=2x+3
        FirstOrderEquation spyEq = spy(real);

        // Only override behaviour for x = 0
        when(spyEq.of(0.0)).thenReturn(999.0);

        assertEquals(999.0, spyEq.of(0.0), 1e-6);  // stubbed
        assertEquals(2 * 5 + 3, spyEq.of(5.0), 1e-6); // real behaviour

        verify(spyEq).of(0.0);
        verify(spyEq).of(5.0);
    }

    
    // shows argument matchers: stubbing and verifying multiple calls
    @Test
    public void testMockWithMultipleCalls() {
        Equation mockEq = mock(Equation.class);

        when(mockEq.of(anyDouble())).thenReturn(42.0);

        assertEquals(42.0, mockEq.of(0.0), 0.0001);
        assertEquals(42.0, mockEq.of(100.0), 0.0001);
        assertEquals(42.0, mockEq.of(-5.5), 0.0001);

        verify(mockEq, times(3)).of(anyDouble());
    }
    
  
    //  Test verifying the order of operations is correct  
    @Test
    public void testEquationPanelCallsEquationInOrder() {
        Equation mockEq = mock(Equation.class);
        when(mockEq.of(anyDouble())).thenReturn(1.0);

        // creating an equation panel triggers this.populate(10), which calls equation.of(x) 2000 times
        new EquationPanel(mockEq);

        // expected increasing x-values
        double step = 10.0 / 2000.0;

        InOrder inOrder = inOrder(mockEq);

        // verify first 3 calls happened in order
        inOrder.verify(mockEq).of(0.0);
        inOrder.verify(mockEq).of(step);
        inOrder.verify(mockEq).of(step * 2);
    }
    
    
    //  shows simulating exceptions using thenThrow to test error handling  
    @Test(expected = RuntimeException.class)
    public void testEquationPanelFailsWhenEquationFails() {
        Equation mockEq = mock(Equation.class);

        when(mockEq.of(anyDouble()))
            .thenReturn(1.0)
            .thenReturn(1.0)
            .thenThrow(new RuntimeException("boom"));

     // creating an equation panel triggers this.populate(10), which calls equation.of(x) 2000 times
        new EquationPanel(mockEq); // populate() should blow up
    }

      
    @Test
    public void testEquationPanelCallsEquationOf2000Times() {
        Equation mockEq = mock(Equation.class);
        when(mockEq.of(anyDouble())).thenReturn(1.0);

     // creating an equation panel triggers this.populate(10), which calls equation.of(x) 2000 times
        EquationPanel panel = new EquationPanel(mockEq);

        verify(mockEq, times(2000)).of(anyDouble());
    }
    
    
    //    EquationPanel calls Equation.of(x) with the correct sequence of x-values
    //    when it populates the internal data series.
    @Test
    public void testEquationPanelPopulatesWithIncreasingXValues() {
        Equation mockEq = mock(Equation.class);
        when(mockEq.of(anyDouble())).thenReturn(1.0);

        // creating an equation panel triggers this.populate(10), which calls equation.of(x) 2000 times
//      Ex   0.0, 10/2000, 2*(10/2000), 3*(10/2000), ...
        new EquationPanel(mockEq); 
        
        //    ArgumentCaptor records every argument passed to mockEq.of(...)
        ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class); 
        verify(mockEq, atLeastOnce()).of(captor.capture());

        List<Double> xs = captor.getAllValues();

        // The first few expected values based on step = max/2000
        assertEquals(0.0, xs.get(0), 1e-6);
        assertEquals(10.0/2000, xs.get(1), 1e-6);
    }
    
    
    //  Mocks can have more dynamic behaviour, computing returns along with static ones   
    @Test
    public void testEquationPanelWithCustomAnswer() {
        Equation mockEq = mock(Equation.class);

        // mock returns x*x (square)
        when(mockEq.of(anyDouble())).thenAnswer(inv -> {
            double x = inv.getArgument(0);
            return x * x;
        });

        new EquationPanel(mockEq);

        ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
        verify(mockEq, atLeastOnce()).of(captor.capture());

        List<Double> xs = captor.getAllValues();

        // ensure return matches the function f(x)=x^2
        double exampleX = xs.get(500);
        assertEquals(exampleX * exampleX, mockEq.of(exampleX), 1e-6);
    }
    
    
 // shows mocking a collaborator and verifying the SUT interacts with it.
    interface EquationPrinter {
        String print(Equation e);
    }

    @Test
    public void testCollaboratorIsCalled() {
        EquationPrinter printer = mock(EquationPrinter.class);
        Equation eq = new FirstOrderEquation(1, 2); // 1x + 2

        when(printer.print(eq)).thenReturn("Equation: 1x+2");

        String s = printer.print(eq);

        assertEquals("Equation: 1x+2", s);
        verify(printer).print(eq);
    }

    
    
    // -------------------------EXERCISES-------------------------
    // (FIX ME) missing stubbing causes incorrect result
    @Test
    public void testMissingMockStubbingFails() {

        Equation mockEq = mock(Equation.class);

        // TODO: stub the mock so eq.of(3) returns something meaningful
        // FIX ME

        double result = mockEq.of(3);

        // TODO: fix expected value
        assertEquals("FIX ME", result, 0.0001);
    }



    // (FIX ME) stub + verify behaviour
    @Test
    public void testEquationValidatorFails() {

        // Local helper class that depends on Equation
        class EquationValidator {
            boolean isPositiveAt(Equation e, double x) {
                double value = e.of(x);
                return value > 0;
            }
        }

        Equation mockEq = mock(Equation.class);
        EquationValidator validator = new EquationValidator();

        // TODO: stub mockEq.of(5.0) to return a value (e.g., positive or negative)
        // FIX ME

        boolean isPos = validator.isPositiveAt(mockEq, 5.0);

        // TODO: update expected boolean to what your stubbed equation should produce
        // FIX ME
        assertEquals("FIX ME", isPos);

        // TODO: verify mockEq.of(...) was called
        // FIX ME
    }
    
    
 // (FIX ME) test LoggingEquation using Mockito
    @Test
    public void testLoggingEquationLogsResult() {

        // A tiny logger interface
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

        // TODO: mock FirstOrderEquation
        // Hint: FirstOrderEquation mockEq = mock(FirstOrderEquation.class);
        FirstOrderEquation mockEq = /* FIX ME */ null;

        // TODO: stub the mock eq to return 7.0
        // FIX ME

        // TODO: mock Logger
        Logger mockLogger = /* FIX ME */ null;

        LoggingEquation le = new LoggingEquation(mockEq, mockLogger);

        double output = le.compute(2.0);

        // TODO: assert the result is 7.0
        // FIX ME

        // TODO: verify that eq.of(2.0) was called
        // FIX ME

        // TODO: verify that logger.log("Result: 7.0") was called
        // FIX ME
    }


}
