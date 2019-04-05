package frc.team7170.robot2019;

public enum ClimbLevel {

    LEVEL_1(0.0, 0.0),
    LEVEL_2(Constants.Field.HAB_LEVEL_1_TO_2_METRES, Constants.Climb.L2_INITIAL_ANGLE_DEGREES),
    LEVEL_3(Constants.Field.HAB_LEVEL_1_TO_3_METRES, Constants.Climb.L3_INITIAL_ANGLE_DEGREES);

    private final double heightMetres;
    private final double initialAngleDegrees;

    ClimbLevel(double heightMetres, double initialAngleDegrees) {
        this.heightMetres = heightMetres;
        this.initialAngleDegrees = initialAngleDegrees;
    }

    public double getHeightMetres() {
        return heightMetres;
    }

    public double getInitialAngleDegrees() {
        return initialAngleDegrees;
    }
}
