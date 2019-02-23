package frc.team7170.lib.wrappers;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj.PIDBase;

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
}