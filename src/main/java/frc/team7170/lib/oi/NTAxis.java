package frc.team7170.lib.oi;

import edu.wpi.first.networktables.NetworkTableEntry;
import frc.team7170.lib.Name;

public class NTAxis extends ScaledAxis {

    private final NetworkTableEntry entry;

    public NTAxis(NetworkTableEntry entry, Name name) {
        super(name);
        if (!isValidEntry(entry)) {
            throw new RuntimeException("entry must be of type 'unassigned' or 'double'");
        }
        this.entry = entry;
    }

    public NTAxis(NetworkTableEntry entry) {
        this(entry, new Name(entry.getName()));
    }

    @Override
    public double getRaw() {
        return entry.getDouble(0.0);
    }

    private static boolean isValidEntry(NetworkTableEntry entry) {
        switch (entry.getType()) {
            case kDouble:
                break;
            case kUnassigned:
                entry.setDouble(0.0);
                break;
            default:
                return false;
        }
        return true;
    }
}
