package frc.team7170.lib.oi;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableEntry;

import java.util.Objects;

/**
 * A {@link TriggerButton TriggerButton} that retrieves its value from a {@link NetworkTableEntry NetworkTableEntry}.
 *
 * @author Robert Russell
 */
public class NTButton extends TriggerButton {

    private final NetworkTableEntry entry;

    /**
     * Whether or not the entry got enabled since the last invocation of {@link NTButton#getPressed() getPressed}.
     *
     * @implNote Access is synchronized on {@code this}.
     */
    private boolean pressed = false;

    /**
     * Whether or not the entry got disabled since the last invocation of {@link NTButton#getReleased() getReleased}.
     *
     * @implNote Access is synchronized on {@code this}.
     */
    private boolean released = false;

    /**
     * Construct a new {@code NTButton with an explicit name.
     *
     * @param entry the {@link NetworkTableEntry NetworkTableEntry} whose
     * {@linkplain NetworkTableEntry#getBoolean(boolean) boolean value} is to be used as the value of this
     * {@code NTButton}.
     * @param name the name of the {@code NTButton}.
     * @throws NullPointerException if the given name or the given {@code NetworkTableEntry} is {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}, or if the given {@code NetworkTableEntry} has a type other than
     * {@link edu.wpi.first.networktables.NetworkTableType#kBoolean kBoolean} or
     * {@link edu.wpi.first.networktables.NetworkTableType#kUnassigned kUnassigned}.
     */
    public NTButton(NetworkTableEntry entry, String name) {
        super(name);
        this.entry = requireValidEntry(entry);
        this.entry.addListener(entryNotification -> {
            synchronized (this) {
                if (entryNotification.value.getBoolean()) {
                    pressed = true;
                } else {
                    released = true;
                }
            }
        }, EntryListenerFlags.kUpdate);
    }

    /**
     * Construct a new {@code NTButton} with the name set to the
     * {@linkplain NetworkTableEntry#getName() name of the entry}.
     *
     * @param entry the {@link NetworkTableEntry NetworkTableEntry} whose
     * {@linkplain NetworkTableEntry#getBoolean(boolean) boolean value} is to be used as the value of this
     * {@code NTButton}.
     * @throws NullPointerException if the given {@code NetworkTableEntry} is {@code null}.
     * @throws IllegalArgumentException if the entry's name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}, or if the given {@code NetworkTableEntry} has a type other than
     * {@link edu.wpi.first.networktables.NetworkTableType#kBoolean kBoolean} or
     * {@link edu.wpi.first.networktables.NetworkTableType#kUnassigned kUnassigned}.
     */
    public NTButton(NetworkTableEntry entry) {
        // This null check is redundant, but I'd like to have the error message be consistent.
        this(entry, Objects.requireNonNull(entry, "entry must be non-null").getName());
    }

    @Override
    public boolean get() {
        return entry.getBoolean(false);
    }

    @Override
    public synchronized boolean getPressed() {
        if (pressed) {
            pressed = false;
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean getReleased() {
        if (released) {
            released = false;
            return true;
        }
        return false;
    }

    /**
     * Require that the given {@link NetworkTableEntry NetworkTableEntry} be valid. A {@code NetworkTableEntry} is valid
     * if and only if it is non-{@code null} and its {@linkplain NetworkTableEntry#getType() type} is either
     * {@link edu.wpi.first.networktables.NetworkTableType#kBoolean kBoolean} or
     * {@link edu.wpi.first.networktables.NetworkTableType#kUnassigned kUnassigned}. If the {@code NetworkTableEntry}'s
     * type is {@code kUnassigned}, then the {@code NetworkTableEntry} will be assigned a type of {@code kBoolean}.
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
            case kBoolean:
                return entry;
            case kUnassigned:
                // Force the entry's type to be boolean if it has no type.
                entry.setBoolean(false);
                return entry;
            default:
                throw new IllegalArgumentException("entry must be of type 'unassigned' or 'boolean'");
        }
    }
}
