package frc.team7170.lib.logging2;

import frc.team7170.lib.Named;
import frc.team7170.lib.data.DataFrame;
import frc.team7170.lib.data.property.RProperty;

import java.util.List;

public interface Loggable extends Named {

    void registerDataFrames(DataFrame<RProperty> frame);

    default List<String> getTags() {
        return List.of();
    }
}
