package telementaryExercise;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.*;


import org.junit.Before;
import org.junit.Test;


public class TelemetryGeneratorTest {

    private TelemetryGenerator generator;

    @Before
    public void setUp() {
        generator = new TelemetryGenerator();
    }

    @Test
    public void vehicleSpeedWithinRangeIsValid() {
        assertTrue(generator.validateVehicleSpeedKmh(120));
    }
    
    @Test
    public void engineRpmWithinRangeIsValid() {
        assertTrue(generator.validateEngineRpm(4000));
    }
    
    @Test
    public void brakePressureWithinRangeIsValid() {
        assertTrue(generator.validateBrakePressureBar(100.0));
    }
}


