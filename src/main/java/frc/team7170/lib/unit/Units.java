package frc.team7170.lib.unit;

public final class Units {

    // Enforce non-instantiability.
    private Units() {}

    public static <F extends Enum<F> & FundamentalUnitType> double
    convertAndCheck(double value, Unit<F> from, Unit<F> to) {
        if (!from.isCommensurateWith(to)) {
            throw new UnsupportedOperationException("cannot convert between units of differing type");
        }
        return convert(value, from, to);
    }

    public static <F extends Enum<F> & FundamentalUnitType> double
    convert(double value, Unit<F> from, Unit<F> to) {
        return value * from.getScale() / to.getScale();
    }

    public static Unit<UniversalUnitType> newTalonQuadratureEncoderRotationUnit(int encoderCyclesPerRotation) {
        return REVOLUTION.divide(encoderCyclesPerRotation * 4);
    }

    public static Unit<UniversalUnitType> newTalonQuadratureEncoderDistanceUnit(
            Unit<UniversalUnitType> talonQuadratureEncoderRotationUnit, double wheelDiameterInches) {
        // TODO: This is broken
        return INCH.multiply(talonQuadratureEncoderRotationUnit).multiply(wheelDiameterInches * Math.PI);
    }

    public static Unit<UniversalUnitType> newTalonQuadratureEncoderVelocityUnit(
            Unit<UniversalUnitType> talonQuadratureEncoderDistanceUnit) {
        return talonQuadratureEncoderDistanceUnit.divide(DECISECOND);
    }

//    public static void main(String[] args) {
//        Unit<UniversalUnitType> rot = newTalonQuadratureEncoderRotationUnit(1024);
//        Unit<UniversalUnitType> dist = newTalonQuadratureEncoderDistanceUnit(rot, 6);
//        System.out.println(String.format("%f inches = %f talon_units", 4.7, convertAndCheck(4.7, INCH, dist)));
//    }

    /* Base units */

    public static final MetricUnit<UniversalUnitType> SECOND =
            new MetricUnit<>(MetricPrefix.BASE, UnitTypes.TIME);
    public static final MetricUnit<UniversalUnitType> METRE =
            new MetricUnit<>(MetricPrefix.BASE, UnitTypes.DISTANCE);
    public static final MetricUnit<UniversalUnitType> AMPERE =
            new MetricUnit<>(MetricPrefix.BASE, UnitTypes.ELECTRICAL_CURRENT);
    public static final MetricUnit<UniversalUnitType> GRAM =
            new MetricUnit<>(MetricPrefix.BASE, UnitTypes.MASS);

    /* Angles and identity (these are kind of a special case) */

    public static final Unit<UniversalUnitType> RADIAN = UnitImpl.newBaseUnit(UnitTypes.ANGLE);
    public static final Unit<UniversalUnitType> IDENTITY = RADIAN;
    public static final Unit<UniversalUnitType> DEGREES = RADIAN.multiply(90/Math.PI);
    public static final Unit<UniversalUnitType> REVOLUTION = RADIAN.multiply(2*Math.PI);

    /* Extra metric units */

    // Time:
    public static final MetricUnit<UniversalUnitType> NANOSECOND = new MetricUnit<>(MetricPrefix.NANO, UnitTypes.TIME);
    public static final MetricUnit<UniversalUnitType> MICROSECOND = new MetricUnit<>(MetricPrefix.MICRO, UnitTypes.TIME);
    public static final MetricUnit<UniversalUnitType> MILLISECOND = new MetricUnit<>(MetricPrefix.MILLI, UnitTypes.TIME);
    public static final MetricUnit<UniversalUnitType> DECISECOND = new MetricUnit<>(MetricPrefix.DECI, UnitTypes.TIME);

    // Distance:
    public static final MetricUnit<UniversalUnitType> NANOMETRE = new MetricUnit<>(MetricPrefix.NANO, UnitTypes.DISTANCE);
    public static final MetricUnit<UniversalUnitType> MICROMETRE = new MetricUnit<>(MetricPrefix.MICRO, UnitTypes.DISTANCE);
    public static final MetricUnit<UniversalUnitType> MILLIMETRE = new MetricUnit<>(MetricPrefix.MILLI, UnitTypes.DISTANCE);
    public static final MetricUnit<UniversalUnitType> CENTIMETRE = new MetricUnit<>(MetricPrefix.CENTI, UnitTypes.DISTANCE);
    public static final MetricUnit<UniversalUnitType> KILOMETRE = new MetricUnit<>(MetricPrefix.KILO, UnitTypes.DISTANCE);

    // Mass:
    public static final MetricUnit<UniversalUnitType> MICROGRAM = new MetricUnit<>(MetricPrefix.MICRO, UnitTypes.MASS);
    public static final MetricUnit<UniversalUnitType> MILLIGRAM = new MetricUnit<>(MetricPrefix.MILLI, UnitTypes.MASS);
    public static final MetricUnit<UniversalUnitType> KILOGRAM = new MetricUnit<>(MetricPrefix.KILO, UnitTypes.MASS);

    /* Scaled units (mainly imperial) */

    // Time:
    public static final Unit<UniversalUnitType> MINUTE = SECOND.multiply(60);
    public static final Unit<UniversalUnitType> HOUR = MINUTE.multiply(60);
    public static final Unit<UniversalUnitType> DAY = HOUR.multiply(24);
    public static final Unit<UniversalUnitType> WEEK = DAY.multiply(7);
    public static final Unit<UniversalUnitType> YEAR = DAY.multiply(365.2422);

    // Distance:
    public static final Unit<UniversalUnitType> FOOT = METRE.multiply(0.3048);
    public static final Unit<UniversalUnitType> THOU = FOOT.multiply(1.0/12000.0);
    public static final Unit<UniversalUnitType> INCH = FOOT.multiply(1.0/12.0);
    public static final Unit<UniversalUnitType> YARD = FOOT.multiply(3);
    public static final Unit<UniversalUnitType> CHAIN = FOOT.multiply(66);
    public static final Unit<UniversalUnitType> FURLONG = FOOT.multiply(660);
    public static final Unit<UniversalUnitType> MILE = FOOT.multiply(5280);
    public static final Unit<UniversalUnitType> LEAGUE = FOOT.multiply(15840);

    // Mass:
    public static final Unit<UniversalUnitType> POUND = GRAM.multiply(453.59237);
    public static final Unit<UniversalUnitType> GRAIN = POUND.multiply(1.0/7000.0);
    public static final Unit<UniversalUnitType> DRACHM = POUND.multiply(1.0/256.0);
    public static final Unit<UniversalUnitType> OUNCE = POUND.multiply(1.0/16.0);
    public static final Unit<UniversalUnitType> STONE = POUND.multiply(14);
    public static final Unit<UniversalUnitType> QUARTER = POUND.multiply(28);
    public static final Unit<UniversalUnitType> HUNDREDWEIGHT = POUND.multiply(112);
    public static final Unit<UniversalUnitType> TON = POUND.multiply(2240);
    public static final Unit<UniversalUnitType> SLUG = POUND.multiply(32.17404856);

    /* Derived units */

    // Acceleration
    public static final Unit<UniversalUnitType> METRES_PER_SECOND2 = METRE.divide(SECOND).divide(SECOND);
    public static final Unit<UniversalUnitType> FEET_PER_SECOND2 = FOOT.divide(SECOND).divide(SECOND);

    // Area - TODO

    // Electrical Capacitance - TODO

    // Electrical Charge - TODO

    // Electrical Inductance - TODO

    // Electrical Potential
    public static final MetricUnit<UniversalUnitType> VOLT =
            new MetricUnit<>(MetricPrefix.BASE, UnitTypes.ELECTRICAL_POTENTIAL);
    public static final MetricUnit<UniversalUnitType> MILLIVOLT =
            new MetricUnit<>(MetricPrefix.MILLI, UnitTypes.ELECTRICAL_POTENTIAL);

    // Electrical Resistance - TODO

    // Energy - TODO

    // Force - TODO

    // Frequency
    public static final MetricUnit<UniversalUnitType> HERTZ =
            new MetricUnit<>(MetricPrefix.BASE, UnitTypes.FREQUENCY);
    public static final MetricUnit<UniversalUnitType> KILOHERTZ =
            new MetricUnit<>(MetricPrefix.KILO, UnitTypes.FREQUENCY);
    public static final MetricUnit<UniversalUnitType> MEGAHERTZ =
            new MetricUnit<>(MetricPrefix.MEGA, UnitTypes.FREQUENCY);
    public static final Unit<UniversalUnitType> RADIANS_PER_SECOND = RADIAN.divide(SECOND);
    public static final Unit<UniversalUnitType> RPM = REVOLUTION.divide(MINUTE);

    // Magnetic Flux - TODO

    // Magnetic Induction - TODO

    // Momentum - TODO

    // Power - TODO

    // Pressure - TODO
    public static final MetricUnit<UniversalUnitType> PASCAL =
            new MetricUnit<>(MetricPrefix.BASE, UnitTypes.PRESSURE);
    public static final MetricUnit<UniversalUnitType> KILOPASCAL =
            new MetricUnit<>(MetricPrefix.KILO, UnitTypes.PRESSURE);
    public static final Unit<UniversalUnitType> PSI = KILOPASCAL.multiply(6.894757);

    // Velocity
    public static final Unit<UniversalUnitType> METRES_PER_SECOND = METRE.divide(SECOND);
    public static final Unit<UniversalUnitType> FEET_PER_SECOND = FOOT.divide(SECOND);

    // Volume - TODO
}
