package frc.team7170.robot.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.team7170.robot.Constants;

public class EndEffector extends Subsystem {

    private final Solenoid ejectSolenoid = new Solenoid(Constants.PCM.PIN_SOLENOID);
    private final Solenoid pinSolenoid = new Solenoid(Constants.PCM.PIN_SOLENOID);
    // TODO: servo for lateral slide

    private EndEffector() {
        super("endEffector");

        ejectSolenoid.setPulseDuration(Constants.EndEffector.EJECT_PULSE_DURATION_SECONDS);
        retractPin();
    }

    public void eject() {
        ejectSolenoid.startPulse();
    }

    public void deployPin() {
        pinSolenoid.set(true);
    }

    public void retractPin() {
        pinSolenoid.set(false);
    }

    @Override
    protected void initDefaultCommand() {}
}
