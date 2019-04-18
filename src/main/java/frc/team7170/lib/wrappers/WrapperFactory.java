package frc.team7170.lib.wrappers;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.PIDBase;

// TODO: major flaw: state isn't respected between different wrappers (i.e. PIDFController doesn't know about Encoder state)
// TODO: for planned changes, we need a more general name than "wrappers" for this whole system
public final class WrapperFactory {

    // Enforce non-instantiablilty.
    private WrapperFactory() {}

    public static PIDFMotor wrapCTREMotorController(BaseMotorController ctreMotorController) {
        return new WrappedCTREMotorController(ctreMotorController);
    }

    public static PIDFMotor wrapSparkMax(CANSparkMax sparkMax) {
        return new WrappedSparkMax(sparkMax);
    }

    public static PIDFController wrapWPIPIDBase(PIDBase pidBase) {
        return new WrappedWPIPIDBase(pidBase);
    }


    public static Encoder wrapCTREEncoder(BaseMotorController ctreMotorController, int cyclesPerRotation) {
        return new WrappedCTREEncoder(ctreMotorController, cyclesPerRotation);
    }

    public static Encoder wrapSparkMaxEncoder(CANSparkMax sparkMax, int cyclesPerRotation) {
        return new WrappedSparkMaxEncoder(sparkMax, cyclesPerRotation);
    }

    public static Encoder wrapWPIEncoder(edu.wpi.first.wpilibj.Encoder encoder, int cyclesPerRotation) {
        return new WrappedWPIEncoder(encoder, cyclesPerRotation);
    }

    public static DigitalInput wrapWPIDigitalInput(edu.wpi.first.wpilibj.DigitalInput wpiDigitalInput) {
        return new WrappedWPIDigitalInput(wpiDigitalInput);
    }

    public static DigitalOutput wrapWPIDigitalOutput(edu.wpi.first.wpilibj.DigitalOutput wpiDigitalOutput) {
        return new WrappedWPIDigitalOutput(wpiDigitalOutput);
    }

    public static AnalogInput wrapWPIAnalogInput(edu.wpi.first.wpilibj.AnalogInput wpiAnalogInput) {
        return new WrappedWPIAnalogInput(wpiAnalogInput);
    }
}
