package frc.team7170.lib.oi;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.team7170.lib.Name;

public class NTButton extends TriggerButton {

    private final NetworkTableEntry entry;
    private boolean pressed = false;
    private boolean released = false;

    public NTButton(NetworkTableEntry entry, Name name) {
        super(name);
        if (!isValidEntry(entry)) {
            throw new RuntimeException("entry must be of type 'unassigned' or 'double'");
        }
        this.entry = entry;
        this.entry.addListener(entryNotification -> {
            if (entryNotification.value.getBoolean()) {
                pressed = true;
            } else {
                released = true;
            }
        }, EntryListenerFlags.kUpdate);
    }

    public NTButton(NetworkTableEntry entry) {
        this(entry, new Name(entry.getName()));
    }

    // TODO: Test the logic here for clearing pressed and released flags... the doc in GenericHID is vague.

    @Override
    public boolean get() {
        return entry.getBoolean(false);
    }

    @Override
    public boolean getPressed() {
        if (pressed) {
            pressed = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean getReleased() {
        if (released) {
            released = false;
            return true;
        }
        return false;
    }

    private static boolean isValidEntry(NetworkTableEntry entry) {
        switch (entry.getType()) {
            case kBoolean:
                break;
            case kUnassigned:
                entry.setBoolean(false);
                break;
            default:
                return false;
        }
        return true;
    }
}
