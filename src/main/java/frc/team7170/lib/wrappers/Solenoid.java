package frc.team7170.lib.wrappers;

public interface Solenoid {

    void setSolenoid(boolean on);

    boolean getSolenoid();

    default void extendSolenoid() {
        setSolenoid(true);
    }

    default void retractSolenoid() {
        setSolenoid(false);
    }
}
