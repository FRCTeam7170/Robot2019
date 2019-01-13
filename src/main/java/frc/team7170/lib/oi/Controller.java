package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;

import java.util.Map;


public abstract class Controller {

    private final GenericHID hid;
    private final String name;

    public Controller(GenericHID hid, String name) {
        KeyBindings.verifyName(name);
        this.hid = hid;
        this.name = name;
    }

    public final GenericHID getHid() {
        return hid;
    }

    public final int getPOV() {
        return hid.getPOV();
    }

    public final String getName() {
        return name;
    }

    abstract Map<String, Axis> getAxesNamesMap();

    abstract Map<String, Button> getButtonsNamesMap();
}
