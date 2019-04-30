package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.buttons.Trigger;
import frc.team7170.lib.Name;

// TODO: make this use routines/Oblarg's new command system instead
public abstract class TriggerButton extends Trigger implements Button {

    public TriggerButton(String name) {
        setName(Name.requireValidName(name));
    }
}
