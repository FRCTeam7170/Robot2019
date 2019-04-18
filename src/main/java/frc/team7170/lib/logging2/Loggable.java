package frc.team7170.lib.logging2;

import frc.team7170.lib.Named;

import java.util.ArrayList;
import java.util.List;

public interface Loggable extends Named {

    default void registerDataFrames(DataFrameBuilder dataFrameBuilder) {
        // Default to adding no data frames.
    }

    default List<String> getTags() {
        return new ArrayList<>(0);
    }
}
