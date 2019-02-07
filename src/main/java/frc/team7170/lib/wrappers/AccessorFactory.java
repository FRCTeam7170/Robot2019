package frc.team7170.lib.wrappers;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public final class AccessorFactory {

    // Enforce non-instantiablilty.
    private AccessorFactory() {}

    // TODO: generalize WrappedTalon and use it for victors
    public static PIDMotor talonToPIDMotor(TalonSRX talon, double initP, double initI,
                                            double initD, double initF, int initTolerance) {
        return new WrappedTalon(talon, initP, initI, initD, initF, initTolerance);
    }

    public static PIDMotor sparkMaxToPIDMotor() {
        return null;
    }
}
