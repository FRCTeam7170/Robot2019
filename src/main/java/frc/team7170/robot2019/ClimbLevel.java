package frc.team7170.robot2019;

public enum ClimbLevel {
    LEVEL_1(0.0,
            0.0,
            0.0
    ),
    LEVEL_2(Constants.Field.HAB_LEVEL_1_TO_2_METRES,
            Constants.Climb.L2_BUMPER_DISTANCE_METRES,
            Constants.Climb.L2_CONTACT_ANGLE_DEGREES
    ),
    LEVEL_3(Constants.Field.HAB_LEVEL_1_TO_3_METRES,
            Constants.Climb.L3_BUMPER_DISTANCE_METRES,
            Constants.Climb.L3_CONTACT_ANGLE_DEGREES
    );

    private final double heightMetres;
    private final double bumperDistanceMetres;
    private final double contactAngleDegrees;

    ClimbLevel(double heightMetres, double bumperDistanceMetres, double contactAngleDegrees) {
        this.heightMetres = heightMetres;
        this.bumperDistanceMetres = bumperDistanceMetres;
        this.contactAngleDegrees = contactAngleDegrees;
    }

    public double getHeightMetres() {
        return heightMetres;
    }

    public double getBumperDistanceMetres() {
        return bumperDistanceMetres;
    }

    public double getContactAngleDegrees() {
        return contactAngleDegrees;
    }
}
