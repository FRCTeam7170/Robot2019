package frc.team7170.robot;

public enum ClimbLevel {
    LEVEL_1(0.0,
            0.0,
            0.0  // TODO: contactAngleDegrees for no climb
    ),
    LEVEL_2(Constants.Field.HAB_LEVEL_1_TO_2_INCHES,
            Constants.Climb.L2_BUMPER_DISTANCE_INCHES,
            Constants.Climb.L2_CONTACT_ANGLE_DEGREES
    ),
    LEVEL_3(Constants.Field.HAB_LEVEL_1_TO_3_INCHES,
            Constants.Climb.L3_BUMPER_DISTANCE_INCHES,
            Constants.Climb.L3_CONTACT_ANGLE_DEGREES
    );

    private final double heightInches;
    private final double bumperDistanceInches;
    private final double contactAngleDegrees;

    ClimbLevel(double heightInches, double bumperDistanceFeet, double contactAngleDegrees) {
        this.heightInches = heightInches;
        this.bumperDistanceInches = bumperDistanceFeet;
        this.contactAngleDegrees = contactAngleDegrees;
    }

    public double getHeightInches() {
        return heightInches;
    }

    public double getBumperDistanceInches() {
        return bumperDistanceInches;
    }

    public double getContactAngleDegrees() {
        return contactAngleDegrees;
    }
}
