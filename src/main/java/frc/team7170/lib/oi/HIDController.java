package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import frc.team7170.lib.Name;

// TODO: accept String not name
public abstract class HIDController implements Controller {

    private final GenericHID hid;
    private final Name name;

    public HIDController(GenericHID hid, Name name) {
        this.hid = hid;
        this.name = name;
    }

    public GenericHID getHid() {
        return hid;
    }

    public int getPOV() {
        return hid.getPOV();
    }

    @Override
    public String getName() {
        return name.getName();
    }

    @Override
    public Name getNameObject() {
        return name;
    }
}
