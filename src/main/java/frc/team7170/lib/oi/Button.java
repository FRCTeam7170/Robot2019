package frc.team7170.lib.oi;

import frc.team7170.lib.Named;

public interface Button extends Named {

    boolean get();

    boolean getPressed();

    boolean getReleased();
}
