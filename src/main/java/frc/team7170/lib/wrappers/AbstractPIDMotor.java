package frc.team7170.lib.wrappers;

import frc.team7170.lib.unit.Unit;
import frc.team7170.lib.unit.UnitTypes;
import frc.team7170.lib.unit.UniversalUnitType;

abstract class AbstractPIDMotor implements PIDMotor {

    private static final double DEFAULT_TOLERANCE = 0.001;

    private final Unit<UniversalUnitType> elecPotentialUnit;
    private final Unit<UniversalUnitType> currentUnit;
    private final Unit<UniversalUnitType> positionUnit;
    private final Unit<UniversalUnitType> velocityUnit;

    private double tolerance;

    AbstractPIDMotor(Unit<UniversalUnitType> elecPotentialUnit,
                     Unit<UniversalUnitType> currentUnit,
                     Unit<UniversalUnitType> positionUnit,
                     Unit<UniversalUnitType> velocityUnit) {
        elecPotentialUnit.getUnitType().assertMatching(UnitTypes.ELECTRICAL_POTENTIAL);
        currentUnit.getUnitType().assertMatching(UnitTypes.ELECTRICAL_CURRENT);
        positionUnit.getUnitType().assertMatching(UnitTypes.DISTANCE);
        velocityUnit.getUnitType().assertMatching(UnitTypes.VELOCITY);

        this.elecPotentialUnit = elecPotentialUnit;
        this.currentUnit = currentUnit;
        this.positionUnit = positionUnit;
        this.velocityUnit = velocityUnit;

        setTolerance(DEFAULT_TOLERANCE);
    }

    @Override
    public void setSetpoint(MotorMode motorMode, double setpoint) {
        setSetpointRaw(motorMode, fromStandardUnits(motorMode, setpoint));
    }

    @Override
    public double getSetpoint() {
        return toStandardUnits(getSetpointMode(), getSetpointRaw());
    }

    @Override
    public double getError() {
        return toStandardUnits(getSetpointMode(), getErrorRaw());
    }

    @Override
    public void setTolerance(double tolerance) {
        this.tolerance = tolerance;
        setToleranceRaw(fromStandardUnits(getSetpointMode(), tolerance));
    }

    @Override
    public double getTolerance() {
        return tolerance;
    }

    private double toStandardUnits(MotorMode motorMode, double value) {
        return motorMode.toStandardUnits(value, elecPotentialUnit, currentUnit, positionUnit, velocityUnit);
    }

    private double fromStandardUnits(MotorMode motorMode, double value) {
        return motorMode.fromStandardUnits(value, elecPotentialUnit, currentUnit, positionUnit, velocityUnit);
    }

    abstract void setSetpointRaw(MotorMode motorMode, double setpoint);

    abstract double getSetpointRaw();  // TODO: pass in MotorMode?

    abstract double getErrorRaw();  // TODO: pass in MotorMode?

    abstract void setToleranceRaw(double tolerance);  // TODO: pass in MotorMode?
}
