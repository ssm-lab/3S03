package telementaryExercise;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class TelemetryGenerator {
    private static class Boundary {
        String type;
        double min;
        double max;
    }

    private final Map<String, Boundary> boundaries = new HashMap<>();

    public TelemetryGenerator() {
        loadBoundaries("boundaries.JSON");
    }

    // JSON parsing
    private void loadBoundaries(String resource) {
        InputStream stream =
            getClass().getClassLoader().getResourceAsStream(resource);

        try (Reader reader = new InputStreamReader(stream)) {
            JsonArray arr = JsonParser.parseReader(reader).getAsJsonArray();

            for (JsonElement e : arr) {
                JsonObject o = e.getAsJsonObject();

                Boundary b = new Boundary();
                b.type = o.get("type").getAsString();
                b.min  = o.get("min").getAsDouble();
                b.max  = o.get("max").getAsDouble();

                boundaries.put(o.get("field").getAsString(), b);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    // Validation methods
    public boolean validateVehicleSpeedKmh(int value) {
        return validate("vehicleSpeedKmh", value);
    }

    public boolean validateEngineRpm(int value) {
        return validate("engineRpm", value);
    }

    public boolean validateThrottlePercent(double value) {
        return validate("throttlePercent", value);
    }

    public boolean validateBrakePressureBar(double value) {
        return validate("brakePressureBar", value);
    }

    public boolean validateSteeringAngleDegrees(double value) {
        return validate("steeringAngleDegrees", value);
    }

    public boolean validateFuelLevelPercent(double value) {
        return validate("fuelLevelPercent", value);
    }

    public boolean validateBatteryVoltage(double value) {
        return validate("batteryVoltage", value);
    }

    public boolean validateEngineTemperatureC(double value) {
        return validate("engineTemperatureC", value);
    }

    public boolean validateTirePressurePsi(double value) {
        return validate("tirePressurePsi", value);
    }

    public boolean validateOdometerKm(long value) {
        return validate("odometerKm", value);
    }

    // validation logic
    private boolean validate(String field, double value) {
        Boundary b = boundaries.get(field);
        return value >= b.min && value <= b.max;
    }

}
