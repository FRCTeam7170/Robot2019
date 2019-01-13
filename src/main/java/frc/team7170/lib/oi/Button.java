package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.buttons.Trigger;


public abstract class Button extends Trigger {

    private final String name;

    public Button(String name) {
        KeyBindings.verifyName(name);
        this.name = name;
    }

    public abstract boolean get();

    public abstract boolean getPressed();

    public abstract boolean getReleased();

    public final String getButtonName() {
        return name;
    }
}
