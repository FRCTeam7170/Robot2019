package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.team7170.lib.Name;

public abstract class TriggerButton extends Trigger implements Button {

    public TriggerButton(Name name) {
        setName(name.getName());
    }
}
