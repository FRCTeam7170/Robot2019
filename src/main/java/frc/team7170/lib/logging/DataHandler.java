package frc.team7170.lib.logging;

import frc.team7170.lib.data.Value;

import java.util.List;
import java.util.Map;

public interface DataHandler extends AutoCloseable {

    void handle(double timestamp, Map<List<String>, Value> values);
}
