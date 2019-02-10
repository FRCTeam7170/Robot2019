package frc.team7170.lib.wrappers;

import com.ctre.phoenix.motorcontrol.can.BaseMotorController;
import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.Units;
import frc.team7170.lib.unit.UniversalUnitType;

public final class WrapperFactory {

    // Enforce non-instantiablilty.
    private WrapperFactory() {}

    public static PIDMotor ctreMotorControllerToPIDMotor(BaseMotorController ctreMotorController,
                                                         Unit<UniversalUnitType> positionUnit,
                                                         Unit<UniversalUnitType> velocityUnit) {
        return new WrappedCTREMotorController(ctreMotorController, positionUnit, velocityUnit);
    }

    public static PIDMotor ctreMotorControllerToPIDMotor(BaseMotorController ctreMotorController,
                                                         int encoderCyclesPerRotation,
                                                         double wheelDiameterInches) {
        Unit<UniversalUnitType> positionUnit = Units.newTalonQuadratureEncoderDistanceUnit(
                Units.newTalonQuadratureEncoderRotationUnit(encoderCyclesPerRotation), wheelDiameterInches);
        return new WrappedCTREMotorController(ctreMotorController, positionUnit,
                Units.newTalonQuadratureEncoderVelocityUnit(positionUnit));
    }
}
