package frc.team7170.lib.wrappers;

public interface ProfileAccessor extends PIDFAccessor {

    void setIZone(double IZone);

    double getIZone();

    void setMinimumOutput(double minimumOutput);

    double getMinimumOutput();

    void setMaximumOutput(double maximumOutput);

    double getMaximumOutput();

    void setTolerance(double tolerance);

    double getTolerance();
}
