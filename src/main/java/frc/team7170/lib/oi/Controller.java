package frc.team7170.lib.oi;

import frc.team7170.lib.Named;

import java.util.Map;

public interface Controller extends Named {

    Map<String, Axis> getAxesNamesMap();

    Map<String, Button> getButtonsNamesMap();
}
