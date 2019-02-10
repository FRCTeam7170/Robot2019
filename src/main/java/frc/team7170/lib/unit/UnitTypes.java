package frc.team7170.lib.unit;

public final class UnitTypes {

    // Enforce non-instantiability.
    private UnitTypes() {}

    public static final UnitType<UniversalUnitType> ACCELERATION =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -2)
                    .power(UniversalUnitType.DISTANCE, 1)
                    .build();

    public static final UnitType<UniversalUnitType> ANGLE =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .build();

    public static final UnitType<UniversalUnitType> AREA =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.DISTANCE, 2)
                    .build();

    public static final UnitType<UniversalUnitType> DISTANCE =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.DISTANCE, 1)
                    .build();
    public static final UnitType<UniversalUnitType> LENGTH = DISTANCE;

    public static final UnitType<UniversalUnitType> ELECTRICAL_CAPACITANCE =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, 4)
                    .power(UniversalUnitType.DISTANCE, -2)
                    .power(UniversalUnitType.CURRENT, 2)
                    .power(UniversalUnitType.MASS, -1)
                    .build();

    public static final UnitType<UniversalUnitType> ELECTRICAL_CHARGE =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, 1)
                    .power(UniversalUnitType.CURRENT, 1)
                    .build();

    public static final UnitType<UniversalUnitType> ELECTRICAL_CURRENT =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.CURRENT, 1)
                    .build();

    public static final UnitType<UniversalUnitType> ELECTRICAL_INDUCTANCE =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -2)
                    .power(UniversalUnitType.DISTANCE, 2)
                    .power(UniversalUnitType.CURRENT, -2)
                    .power(UniversalUnitType.MASS, 1)
                    .build();

    public static final UnitType<UniversalUnitType> ELECTRICAL_POTENTIAL =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -3)
                    .power(UniversalUnitType.DISTANCE, 2)
                    .power(UniversalUnitType.CURRENT, -1)
                    .power(UniversalUnitType.MASS, 1)
                    .build();

    public static final UnitType<UniversalUnitType> ELECTRICAL_RESISTANCE =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -3)
                    .power(UniversalUnitType.DISTANCE, 2)
                    .power(UniversalUnitType.CURRENT, -2)
                    .power(UniversalUnitType.MASS, 1)
                    .build();

    public static final UnitType<UniversalUnitType> ENERGY =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -2)
                    .power(UniversalUnitType.DISTANCE, 2)
                    .power(UniversalUnitType.MASS, 1)
                    .build();
    public static final UnitType<UniversalUnitType> WORK = ENERGY;
    public static final UnitType<UniversalUnitType> TORQUE = ENERGY;

    public static final UnitType<UniversalUnitType> FORCE =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -2)
                    .power(UniversalUnitType.DISTANCE, 1)
                    .power(UniversalUnitType.MASS, 1)
                    .build();
    public static final UnitType<UniversalUnitType> WEIGHT = FORCE;

    public static final UnitType<UniversalUnitType> FREQUENCY =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -1)
                    .build();

    public static final UnitType<UniversalUnitType> MAGNETIC_FLUX =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -2)
                    .power(UniversalUnitType.DISTANCE, 2)
                    .power(UniversalUnitType.CURRENT, -1)
                    .power(UniversalUnitType.MASS, 1)
                    .build();

    public static final UnitType<UniversalUnitType> MAGNETIC_INDUCTION =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -2)
                    .power(UniversalUnitType.DISTANCE, -1)
                    .power(UniversalUnitType.MASS, 1)
                    .build();

    public static final UnitType<UniversalUnitType> MASS =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.MASS, 1)
                    .build();

    public static final UnitType<UniversalUnitType> MOMENTUM =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -1)
                    .power(UniversalUnitType.DISTANCE, 1)
                    .power(UniversalUnitType.MASS, 1)
                    .build();

    public static final UnitType<UniversalUnitType> POWER =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -3)
                    .power(UniversalUnitType.DISTANCE, 2)
                    .power(UniversalUnitType.MASS, 1)
                    .build();

    public static final UnitType<UniversalUnitType> PRESSURE =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -2)
                    .power(UniversalUnitType.DISTANCE, -1)
                    .power(UniversalUnitType.MASS, 1)
                    .build();

    public static final UnitType<UniversalUnitType> TIME =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, 1)
                    .build();

    public static final UnitType<UniversalUnitType> VELOCITY =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.TIME, -1)
                    .power(UniversalUnitType.DISTANCE, 1)
                    .build();

    public static final UnitType<UniversalUnitType> VOLUME =
            new UnitTypeImpl.Builder<>(UniversalUnitType.class)
                    .power(UniversalUnitType.DISTANCE, 3)
                    .build();
}
