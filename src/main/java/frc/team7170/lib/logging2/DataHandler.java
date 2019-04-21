package frc.team7170.lib.logging2;

import frc.team7170.lib.data.Value;

import java.util.Map;

public interface DataHandler extends AutoCloseable {

    void handle(double timestamp, String loggableName, Map<String, Value> values);
}
