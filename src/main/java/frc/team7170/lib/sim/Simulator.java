package frc.team7170.lib.sim;

import frc.team7170.lib.Named;
import frc.team7170.lib.data.PropertyGroup;
import frc.team7170.lib.data.property.RProperty;

// TODO: name this extend Loggable instead?
public interface Simulator extends Named {

    void registerSimulatedProperties(PropertyGroup<RProperty> propertyGroup);
}
