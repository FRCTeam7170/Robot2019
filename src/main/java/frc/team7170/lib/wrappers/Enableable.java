package frc.team7170.lib.wrappers;

public interface Enableable {

    void enable();

    void disable();

    boolean isEnabled();

    default void setEnabled(boolean enable) {
        if (enable) {
            enable();
        } else {
            disable();
        }
    }
}
