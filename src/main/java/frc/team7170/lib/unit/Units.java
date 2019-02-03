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

//    public static final MetricUnit<Time> SECOND = new MetricUnit<>(MetricPrefix.BASE, UnitTypes.TIME);
//    public static final MetricUnit<Distance> METRE= new MetricUnit<>(MetricPrefix.BASE, UnitTypes.DISTANCE);
//    public static final MetricUnit<ElectricalCurrent> AMPERE = new MetricUnit<>(MetricPrefix.BASE, UnitTypes.ELECTRICAL_CURRENT);
//    public static final MetricUnit<Mass> KILOGRAM = new MetricUnit<>(MetricPrefix.KILO, UnitTypes.MASS);

    private static final HashMap<FundamentalUnitType, BaseUnit<FundamentalUnitType, ? extends UnitType<FundamentalUnitType>>>
            futBaseUnitMap = new HashMap<>(4);

    static {
//        futBaseUnitMap.put(UnitTypes.TIME, SECOND);
//        futBaseUnitMap.put(UnitTypes.DISTANCE, METRE);
//        futBaseUnitMap.put(UnitTypes.ELECTRICAL_CURRENT, AMPERE);
//        futBaseUnitMap.put(UnitTypes.MASS, KILOGRAM);
    }

    /* Extra units (mainly imperial) */

//    // Angle:
//    public static final Unit<Angle> RADIAN = new Unit<>(1, UnitTypes.ANGLE);
//    public static final Unit<Angle> DEGREES = new Unit<>(90/Math.PI, UnitTypes.ANGLE);
//    public static final Unit<Angle> REVOLUTION = new Unit<>(2*Math.PI, UnitTypes.ANGLE);
//
//    // Time:
//    public static final Unit<Time> MINUTES = new Unit<>(60, SECOND);
//    public static final Unit<Time> HOUR = new Unit<>(60, MINUTES);
//    public static final Unit<Time> DAY = new Unit<>(24, HOUR);
//    public static final Unit<Time> WEEK = new Unit<>(7, DAY);
//    public static final Unit<Time> YEAR = new Unit<>(365.2422, DAY);
//
//    // Distance:
//    public static final Unit<Distance> THOU = new Unit<>(0.0000254, METRE);
//    public static final Unit<Distance> INCH = new Unit<>(1000, THOU);
//    public static final Unit<Distance> FOOT = new Unit<>(12, INCH);
//    public static final Unit<Distance> YARD = new Unit<>(3, FOOT);
//    public static final Unit<Distance> CHAIN = new Unit<>(22, YARD);
//    public static final Unit<Distance> FURLONG = new Unit<>(10, CHAIN);
//    public static final Unit<Distance> MILE = new Unit<>(8, FURLONG);
//    public static final Unit<Distance> LEAGUE = new Unit<>(3, MILE);
//
//    // Mass:
//    public static final Unit<Mass> POUND = new Unit<>(0.45359237, KILOGRAM);
//    public static final Unit<Mass> GRAIN = new Unit<>(1.0/7000.0, POUND);
//    public static final Unit<Mass> DRACHM = new Unit<>(1.0/256.0, POUND);
//    public static final Unit<Mass> OUNCE = new Unit<>(1.0/16.0, POUND);
//    public static final Unit<Mass> STONE = new Unit<>(14, POUND);
//    public static final Unit<Mass> QUARTER = new Unit<>(28, POUND);
//    public static final Unit<Mass> HUNDREDWEIGHT = new Unit<>(112, POUND);
//    public static final Unit<Mass> TON = new Unit<>(2240, POUND);
//    public static final Unit<Mass> SLUG = new Unit<>(32.17404856, POUND);
}
