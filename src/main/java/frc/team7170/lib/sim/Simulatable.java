package frc.team7170.lib.sim;

public interface Simulatable<T extends Simulatable<T>> {

    // TODO: accept some object(s) describing how the simulator should behave?
    <R extends Simulator & Simulatable<T>> R getSimulator();
}
