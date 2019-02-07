package frc.team7170.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

public class FrontArms extends Subsystem {

    private FrontArms() {}

    private static final FrontArms INSTANCE = new FrontArms();

    public static FrontArms getInstance() {
        return INSTANCE;
    }

    @Override
    protected void initDefaultCommand() {}
}
