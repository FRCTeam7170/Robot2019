package frc.team7170.lib.logging;

import frc.team7170.lib.Named;
import frc.team7170.lib.data.PropertyGroup;
import frc.team7170.lib.data.property.RProperty;

import java.util.List;

public interface Loggable extends Named {

    String ALL = "all";

    void registerProperties(PropertyGroup<RProperty> propertyGroup);

    default List<String> getTags() {
        return List.of(ALL);
    }
}
