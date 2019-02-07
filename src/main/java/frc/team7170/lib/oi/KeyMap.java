package frc.team7170.lib.oi;

import frc.team7170.lib.Named;

public interface KeyMap extends Named {

    Axis actionToAxis(AxisAction action);

    Button actionToButton(ButtonAction action);

    boolean hasBindingFor(AxisAction action);

    boolean hasBindingFor(ButtonAction action);
}
