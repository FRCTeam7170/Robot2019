package frc.team7170.lib.logging2;

import java.util.Map;

public interface DataHandler extends AutoCloseable {

    void handle(double timestamp, String loggableName, Map<String, Value> values);
}
