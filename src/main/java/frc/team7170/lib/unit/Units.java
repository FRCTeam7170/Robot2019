package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.*;

import java.util.HashMap;

public final class Units {

    // Enforce non-instantiability.
    private Units() {}

    public static <R extends Enum<R> & IFundamentalUnitType<R>, T extends UnitType<R>>
    double convert(double value, Unit<R, T> from, Unit<R, T> to) {
        return value * to.getFactor() / from.getFactor();
    }

    public static BaseUnit<FundamentalUnitType, ? extends UnitType<FundamentalUnitType>>
    futToBaseUnit(FundamentalUnitType fut) {
        return futBaseUnitMap.get(fut);
    }

    /* Base units */

    public static final BaseMetricUnit<FundamentalUnitType, Time> SECOND =
            new BaseMetricUnit<>(UnitTypes.TIME);
    public static final BaseMetricUnit<FundamentalUnitType, Distance> METRE =
            new BaseMetricUnit<>(UnitTypes.DISTANCE);
    public static final BaseMetricUnit<FundamentalUnitType, ElectricalCurrent> AMPERE =
            new BaseMetricUnit<>(UnitTypes.ELECTRICAL_CURRENT);
    public static final BaseMetricUnit<FundamentalUnitType, Mass> GRAM =
            new BaseMetricUnit<>(UnitTypes.MASS);

    private static final HashMap<FundamentalUnitType, BaseUnit<FundamentalUnitType, ? extends UnitType<FundamentalUnitType>>>
            futBaseUnitMap = new HashMap<>(4);

    static {
        futBaseUnitMap.put(FundamentalUnitType.TIME, SECOND);
        futBaseUnitMap.put(FundamentalUnitType.DISTANCE, METRE);
        futBaseUnitMap.put(FundamentalUnitType.CURRENT, AMPERE);
        futBaseUnitMap.put(FundamentalUnitType.MASS, GRAM);
    }

    /* Angles (these are kind of a special case) */

    public static final BaseUnit<FundamentalUnitType, Angle> RADIAN =
            new BaseUnit<>(UnitTypes.ANGLE);
    public static final ScaledUnit<FundamentalUnitType, Angle> DEGREES =
            new ScaledUnit<>(90/Math.PI, RADIAN);
    public static final ScaledUnit<FundamentalUnitType, Angle> REVOLUTION =
            new ScaledUnit<>(2*Math.PI, RADIAN);

    /* Extra metric units */

    // Time:
    public static final MetricUnit<FundamentalUnitType, Time> NANOSECOND =
            new MetricUnit<>(MetricPrefix.NANO, SECOND);
    public static final MetricUnit<FundamentalUnitType, Time> MICROSECOND =
            new MetricUnit<>(MetricPrefix.MICRO, SECOND);
    public static final MetricUnit<FundamentalUnitType, Time> MILLISECOND =
            new MetricUnit<>(MetricPrefix.MILLI, SECOND);
    public static final MetricUnit<FundamentalUnitType, Time> DECISECOND =
            new MetricUnit<>(MetricPrefix.DECI, SECOND);

    // Distance:
    public static final MetricUnit<FundamentalUnitType, Distance> NANOMETRE =
            new MetricUnit<>(MetricPrefix.NANO, METRE);
    public static final MetricUnit<FundamentalUnitType, Distance> MICROMETRE =
            new MetricUnit<>(MetricPrefix.MICRO, METRE);
    public static final MetricUnit<FundamentalUnitType, Distance> MILLIMETRE =
            new MetricUnit<>(MetricPrefix.MILLI, METRE);
    public static final MetricUnit<FundamentalUnitType, Distance> CENTIMETRE =
            new MetricUnit<>(MetricPrefix.CENTI, METRE);
    public static final MetricUnit<FundamentalUnitType, Distance> KILOMETRE =
            new MetricUnit<>(MetricPrefix.KILO, METRE);

    // Mass:
    public static final MetricUnit<FundamentalUnitType, Mass> MICROGRAM =
            new MetricUnit<>(MetricPrefix.MICRO, GRAM);
    public static final MetricUnit<FundamentalUnitType, Mass> MILLIGRAM =
            new MetricUnit<>(MetricPrefix.MILLI, GRAM);
    public static final MetricUnit<FundamentalUnitType, Mass> KILOGRAM =
            new MetricUnit<>(MetricPrefix.KILO, GRAM);

    /* Imperial scaled units */

    // Time:
    public static final ScaledUnit<FundamentalUnitType, Time> MINUTES =
            new ScaledUnit<>(60, SECOND);
    public static final ScaledUnit<FundamentalUnitType, Time> HOUR =
            new ScaledUnit<>(60, MINUTES);
    public static final ScaledUnit<FundamentalUnitType, Time> DAY =
            new ScaledUnit<>(24, HOUR);
    public static final ScaledUnit<FundamentalUnitType, Time> WEEK =
            new ScaledUnit<>(7, DAY);
    public static final ScaledUnit<FundamentalUnitType, Time> YEAR =
            new ScaledUnit<>(365.2422, DAY);

    // Distance:
    public static final ScaledUnit<FundamentalUnitType, Distance> FOOT =
            new ScaledUnit<>(0.3048, METRE);
    public static final ScaledUnit<FundamentalUnitType, Distance> THOU =
            new ScaledUnit<>(1.0/12000.0, FOOT);
    public static final ScaledUnit<FundamentalUnitType, Distance> INCH =
            new ScaledUnit<>(1.0/12.0, FOOT);
    public static final ScaledUnit<FundamentalUnitType, Distance> YARD =
            new ScaledUnit<>(3, FOOT);
//    public static final ScaledUnit<FundamentalUnitType, Distance> CHAIN =
//            new ScaledUnit<>(66, FOOT);
//    public static final ScaledUnit<FundamentalUnitType, Distance> FURLONG =
//            new ScaledUnit<>(660, FOOT);
    public static final ScaledUnit<FundamentalUnitType, Distance> MILE =
            new ScaledUnit<>(5280, FOOT);
//    public static final ScaledUnit<FundamentalUnitType, Distance> LEAGUE =
//            new ScaledUnit<>(15840, FOOT);

    // Mass:
    public static final ScaledUnit<FundamentalUnitType, Mass> POUND =
            new ScaledUnit<>(453.59237, GRAM);
//    public static final ScaledUnit<FundamentalUnitType, Mass> GRAIN =
//            new ScaledUnit<>(1.0/7000.0, POUND);
//    public static final ScaledUnit<FundamentalUnitType, Mass> DRACHM =
//            new ScaledUnit<>(1.0/256.0, POUND);
    public static final ScaledUnit<FundamentalUnitType, Mass> OUNCE =
            new ScaledUnit<>(1.0/16.0, POUND);
//    public static final ScaledUnit<FundamentalUnitType, Mass> STONE =
//            new ScaledUnit<>(14, POUND);
//    public static final ScaledUnit<FundamentalUnitType, Mass> QUARTER =
//            new ScaledUnit<>(28, POUND);
//    public static final ScaledUnit<FundamentalUnitType, Mass> HUNDREDWEIGHT =
//            new ScaledUnit<>(112, POUND);
    public static final ScaledUnit<FundamentalUnitType, Mass> TON =
            new ScaledUnit<>(2240, POUND);
    public static final ScaledUnit<FundamentalUnitType, Mass> SLUG =
            new ScaledUnit<>(32.17404856, POUND);

    /* Unit sets */

    // Seconds, metres, amperes, kilograms
    public static final UnitSet<FundamentalUnitType> METRIC =
            new UnitSet.Builder<>(FundamentalUnitType.class)
                    .map(FundamentalUnitType.MASS, KILOGRAM)
                    .build();

    // Seconds, feet, amperes, pounds
    public static final UnitSet<FundamentalUnitType> IMPERIAL =
            new UnitSet.Builder<>(FundamentalUnitType.class)
                    .map(FundamentalUnitType.DISTANCE, FOOT)
                    .map(FundamentalUnitType.MASS, POUND)
                    .build();

    /* Derived units */

    // Acceleration
    public static final DerivedUnit<FundamentalUnitType, Acceleration> METRES_PER_SECOND2 =
            new DerivedUnit<>(UnitTypes.ACCELERATION, METRIC);
    public static final DerivedUnit<FundamentalUnitType, Acceleration> FEET_PER_SECOND2 =
            new DerivedUnit<>(UnitTypes.ACCELERATION, IMPERIAL);

    // Area - TODO

    // Electrical Capacitance - TODO

    // Electrical Charge - TODO

    // Electrical Inductance - TODO

    // Electrical Potential
    public static final MetricDerivedUnit<FundamentalUnitType, ElectricalPotential> VOLT =
            MetricDerivedUnit.newBaseMetricDerivedUnit(UnitTypes.ELECTRICAL_POTENTIAL, METRIC);
    public static final MetricDerivedUnit<FundamentalUnitType, ElectricalPotential> MILLIVOLT =
            new MetricDerivedUnit<>(MetricPrefix.MILLI, VOLT);

    // Electrical Resistance - TODO

    // Energy - TODO

    // Force - TODO

    // Frequency
    public static final MetricDerivedUnit<FundamentalUnitType, Frequency> HERTZ =
            MetricDerivedUnit.newBaseMetricDerivedUnit(UnitTypes.FREQUENCY, METRIC);
    public static final MetricDerivedUnit<FundamentalUnitType, Frequency> KILOHERTZ =
            new MetricDerivedUnit<>(MetricPrefix.KILO, HERTZ);
    public static final MetricDerivedUnit<FundamentalUnitType, Frequency> MEGAHERTZ =
            new MetricDerivedUnit<>(MetricPrefix.MEGA, HERTZ);
    public static final DerivedUnit<FundamentalUnitType, Frequency> RADIANS_PER_SECOND = HERTZ;
    public static final ScaledUnit<FundamentalUnitType, Frequency> RPM =
            new ScaledUnit<>(2*Math.PI/60, RADIANS_PER_SECOND);

    // Magnetic Flux - TODO

    // Magnetic Induction - TODO

    // Momentum - TODO

    // Power - TODO

    // Pressure - TODO
    public static final MetricDerivedUnit<FundamentalUnitType, Pressure> PASCAL =
            MetricDerivedUnit.newBaseMetricDerivedUnit(UnitTypes.PRESSURE, METRIC);
    public static final MetricDerivedUnit<FundamentalUnitType, Pressure> KILOPASCAL =
            new MetricDerivedUnit<>(MetricPrefix.KILO, PASCAL);
    public static final ScaledUnit<FundamentalUnitType, Pressure> PSI =
            new ScaledUnit<>(6.894757, KILOPASCAL);

    // Velocity
    public static final DerivedUnit<FundamentalUnitType, Velocity> METRES_PER_SECOND =
            new DerivedUnit<>(UnitTypes.VELOCITY, METRIC);
    public static final DerivedUnit<FundamentalUnitType, Velocity> FEET_PER_SECOND =
            new DerivedUnit<>(UnitTypes.VELOCITY, IMPERIAL);

    // Volume - TODO
}
