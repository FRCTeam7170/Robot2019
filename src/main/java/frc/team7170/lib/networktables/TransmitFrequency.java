package frc.team7170.lib.networktables;

public class TransmitFrequency {

    // TODO: Have "ON_DEMAND" option? (i.e. special case of static)

    public static final int STATIC = -1;
    public static final int SLOW = 500;
    public static final int MODERATE = 250;
    public static final int FAST = 100;
    public static final int VOLATILE = 0;

    private TransmitFrequency() {}
}
