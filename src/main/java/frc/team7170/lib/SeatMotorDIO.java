package frc.team7170.lib;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Pretty much just a renamed counter.
 */
public class SeatMotorDIO extends Counter {

    private boolean inverted;

    public SeatMotorDIO(int dio, boolean inverted) {
        super(new DigitalInput(dio));
        this.inverted = inverted;
    }

    public SeatMotorDIO(int dio) {
        this(dio, false);
    }

    public void forward() {
        setReverseDirection(inverted);
    }

    public void backward() {
        setReverseDirection(!inverted);
    }

    public void motorSetpoint(double setpoint) {
        if (setpoint >= 0.0) {
            forward();
        } else {
            backward();
        }
    }

    public boolean getInverted() {
        return inverted;
    }

    public void setInverted(boolean inverted) {
        this.inverted = inverted;
    }
}
