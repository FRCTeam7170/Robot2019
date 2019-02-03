package frc.team7170.lib.unit.unittypes;

public final class UnitTypes {

    // Enforce non-instantiability.
    private UnitTypes() {}

    public static final Acceleration ACCELERATION = new Acceleration();
    public static final Angle ANGLE = new Angle();
    public static final Area AREA = new Area();
    public static final Distance DISTANCE = new Distance();
    public static final Distance LENGTH = DISTANCE;
    public static final ElectricalCapacitance ELECTRICAL_CAPACITANCE = new ElectricalCapacitance();
    public static final ElectricalCharge ELECTRICAL_CHARGE = new ElectricalCharge();
    public static final ElectricalCurrent ELECTRICAL_CURRENT = new ElectricalCurrent();
    public static final ElectricalInductance ELECTRICAL_INDUCTANCE = new ElectricalInductance();
    public static final ElectricalPotential ELECTRICAL_POTENTIAL = new ElectricalPotential();
    public static final ElectricalResistance ELECTRICAL_RESISTANCE = new ElectricalResistance();
    public static final Energy ENERGY = new Energy();
    public static final Energy WORK = ENERGY;
    public static final Energy TORQUE = ENERGY;
    public static final Force FORCE = new Force();
    public static final Force WEIGHT = FORCE;
    public static final Frequency FREQUENCY = new Frequency();
    public static final MagneticFlux MAGNETIC_FLUX = new MagneticFlux();
    public static final MagneticInduction MAGNETIC_INDUCTION = new MagneticInduction();
    public static final Mass MASS = new Mass();
    public static final Momentum MOMENTUM = new Momentum();
    public static final Power POWER = new Power();
    public static final Pressure PRESSURE = new Pressure();
    public static final Time TIME = new Time();
    public static final Velocity VELOCITY = new Velocity();
    public static final Volume VOLUME = new Volume();
}
