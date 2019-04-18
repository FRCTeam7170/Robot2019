package frc.team7170.lib;

/**
 * @author Robert Russell
 * @see Name
 */
public interface Named {

    String getName();

    default Name getCheckedName() {
        return new Name(getName());
    }
}
