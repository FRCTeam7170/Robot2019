package frc.team7170.lib;

public interface Named {

    String getName();

    default Name getCheckedName() {
        return new Name(getName());
    }
}
