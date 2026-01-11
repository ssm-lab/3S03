package test;

import static org.junit.Assert.*;

import org.junit.Test;

import uk.ac.york.modules.testing.EquationPanel;
import uk.ac.york.modules.testing.FirstOrderEquation;
import uk.ac.york.modules.testing.Equation;

import java.util.ArrayList;

public class EquationsPanelTest {

	@Test
	public void testConstructor() {
	    Equation mockEquation = new FirstOrderEquation(2.0, 3.0);
	    EquationPanel panel = new EquationPanel(mockEquation);

	    // Clear pre-populated data
	    panel.series[0].clear();
	    panel.series[1].clear();

	    assertNotNull("Equation should not be null", panel.equation);
	    assertEquals("Equation should be the one passed in constructor", mockEquation, panel.equation);
	    assertEquals("Series[0] should be empty after clearing", 0, panel.series[0].size());
	    assertEquals("Series[1] should be empty after clearing", 0, panel.series[1].size());
	}


    @Test
    public void testAddValue() {
        Equation mockEquation = new FirstOrderEquation(1.0, 0.0);
        EquationPanel panel = new EquationPanel(mockEquation);
        
        panel.series[0].clear();
        panel.series[1].clear();

        panel.addValue(1.0, 2.0);
        panel.addValue(2.0, 4.0);
        panel.addValue(3.0, 6.0);

        assertEquals("Series[0] should have 3 values", 3, panel.series[0].size());
        assertEquals("Series[1] should have 3 values", 3, panel.series[1].size());
        assertEquals("First x-value should be 1.0", 1.0, panel.series[0].get(0), 0.0001);
        assertEquals("First y-value should be 2.0", 2.0, panel.series[1].get(0), 0.0001);
    }

    @Test
    public void testPopulate() {
        Equation mockEquation = new FirstOrderEquation(2.0, 3.0);
        EquationPanel panel = new EquationPanel(mockEquation);

        panel.series[0] = new ArrayList<>(); // Reset list
        panel.series[1] = new ArrayList<>();

        panel.populate(10);

        assertEquals("Should populate 2000 points", 2000, panel.series[0].size());
        assertEquals("Should populate 2000 y-values", 2000, panel.series[1].size());
        assertEquals("First x value should be 0", 0.0, panel.series[0].get(0), 0.0001);
    }

}
