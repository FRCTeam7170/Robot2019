package frc.team7170.robot2019;

public enum ElevatorLevel {
    LEVEL1(Constants.Elevator.LEVEL1_METRES, Constants.State.LEVEL1_MULTIPLIER),
    LEVEL2(Constants.Elevator.LEVEL2_METRES, Constants.State.LEVEL2_MULTIPLIER),
    LEVEL3(Constants.Elevator.LEVEL3_METRES, Constants.State.LEVEL3_MULTIPLIER);

    private final double heightMetres;
    private final double driveMultiplier;

    ElevatorLevel(double heightMetres, double driveMultiplier) {
        this.heightMetres = heightMetres;
        this.driveMultiplier = driveMultiplier;
    }

    public double getHeightMetres() {
        return heightMetres;
    }

    public double getDriveMultiplier() {
        return driveMultiplier;
    }
}
