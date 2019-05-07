package frc.team7170.lib.oi;

import edu.wpi.first.networktables.NetworkTableEntry;

import java.util.Objects;

/**
 * A {@link ScaledAxis ScaledAxis} that retrieves its value from a {@link NetworkTableEntry NetworkTableEntry}.
 *
 * @author Robert Russell
 */
// TODO: fix docs with controller arg
public class NTAxis extends ScaledAxis {

    private final NetworkTableEntry entry;

    /**
     * Construct a new {@code NTAxis} with an explicit name.
     *
     * @param entry the {@link NetworkTableEntry NetworkTableEntry} whose
     * {@linkplain NetworkTableEntry#getDouble(double) double value} is to be used as the value of this {@code NTAxis}.
     * @param name the name of the {@code NTAxis}.
     * @throws NullPointerException if the given name or the given {@code NetworkTableEntry} is {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}, or if the given {@code NetworkTableEntry} has a type other than
     * {@link edu.wpi.first.networktables.NetworkTableType#kDouble kDouble} or
     * {@link edu.wpi.first.networktables.NetworkTableType#kUnassigned kUnassigned}.
     */
    public NTAxis(NetworkTableEntry entry, Controller controller, String name) {
        super(controller, name);
        this.entry = requireValidEntry(entry);
    }

    /**
     * Construct a new {@code NTAxis} with the name set to the
     * {@linkplain NetworkTableEntry#getName() name of the entry}.
     *
     * @param entry the {@link NetworkTableEntry NetworkTableEntry} whose
     * {@linkplain NetworkTableEntry#getDouble(double) double value} is to be used as the value of this {@code NTAxis}.
     * @throws NullPointerException if the given {@code NetworkTableEntry} is {@code null}.
     * @throws IllegalArgumentException if the entry's name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}, or if the given {@code NetworkTableEntry} has a type other than
     * {@link edu.wpi.first.networktables.NetworkTableType#kDouble kDouble} or
     * {@link edu.wpi.first.networktables.NetworkTableType#kUnassigned kUnassigned}.
     */
    public NTAxis(NetworkTableEntry entry, Controller controller) {
        super(controller, requireValidEntry(entry).getName());
        this.entry = entry;
    }

    @Override
    public double getRaw() {
        return entry.getDouble(0.0);
    }

    /**
     * Require that the given {@link NetworkTableEntry NetworkTableEntry} be valid. A {@code NetworkTableEntry} is valid
     * if and only if it is non-{@code null} and its {@linkplain NetworkTableEntry#getType() type} is either
     * {@link edu.wpi.first.networktables.NetworkTableType#kDouble kDouble} or
     * {@link edu.wpi.first.networktables.NetworkTableType#kUnassigned kUnassigned}. If the {@code NetworkTableEntry}'s
     * type is {@code kUnassigned}, then the {@code NetworkTableEntry} will be assigned a type of {@code kDouble}.
     *
     * @param entry the {@code NetworkTableEntry} to require validity on.
     * @return the given {@code NetworkTableEntry} for sake of composing calls to this function in variable assignments
     * or other function/method invocations.
     * @throws NullPointerException if the given {@code NetworkTableEntry} is {@code null}.
     * @throws IllegalArgumentException if the given {@code NetworkTableEntry} is of an invalid type.
     */
    private static NetworkTableEntry requireValidEntry(NetworkTableEntry entry) {
        Objects.requireNonNull(entry, "entry must be non-null");
        switch (entry.getType()) {
            case kDouble:
                return entry;
            case kUnassigned:
                // Force the entry's type to be double if it has no type.
                entry.setDouble(0.0);
                return entry;
            default:
                throw new IllegalArgumentException("entry must be of type 'unassigned' or 'double'");
        }
    }
}
