package frc.team7170.lib.unit;

import java.util.EnumMap;

public class UnitTypeImpl<F extends Enum<F> & FundamentalUnitType> implements UnitType<F> {

    public static class Builder<F extends Enum<F> & FundamentalUnitType> {

        private final Class<F> futClass;
        private final EnumMap<F, Integer> powerMap;

        public Builder(Class<F> futClass) {
            this.futClass = futClass;
            powerMap = new EnumMap<>(futClass);
            for (F fut : futClass.getEnumConstants()) {
                powerMap.put(fut, 0);
            }
        }

        public Builder<F> power(F fut, int pow) {
            powerMap.put(fut, pow);
            return this;
        }

        public UnitType<F> build() {
            return new UnitTypeImpl<>(this);
        }
    }

    private final Class<F> futClass;
    private final EnumMap<F, Integer> powerMap;

    private UnitTypeImpl(Class<F> futClass, EnumMap<F, Integer> powerMap) {
        this.futClass = futClass;
        this.powerMap = powerMap;
    }

    private UnitTypeImpl(Builder<F> builder) {
        this(builder.futClass, builder.powerMap);
    }

    public UnitType<F> multiply(UnitType<F> multiplier) {
        EnumMap<F, Integer> newMap = new EnumMap<>(futClass);
        powerMap.forEach((fut, i) -> newMap.put(fut, i + multiplier.powerOf(fut)));
        return new UnitTypeImpl<>(futClass, newMap);
    }

    public UnitType<F> divide(UnitType<F> divisor) {
        // The following would be more elegant, but makes an unnecessary intermediary instance.
        // return multiply(divisor.reciprocal());
        EnumMap<F, Integer> newMap = new EnumMap<>(futClass);
        powerMap.forEach((fut, i) -> newMap.put(fut, i - divisor.powerOf(fut)));
        return new UnitTypeImpl<>(futClass, newMap);
    }

    public UnitType<F> reciprocal() {
        EnumMap<F, Integer> newMap = new EnumMap<>(futClass);
        powerMap.forEach((fut, i) -> newMap.put(fut, -i));
        return new UnitTypeImpl<>(futClass, newMap);
    }

    public int powerOf(F fut) {
        return powerMap.get(fut);
    }

    @Override
    public boolean isEquivalentUnitType(UnitType<F> other) {
        if (other == null) {
            return false;
        }
        if (other instanceof UnitTypeImpl<?>) {
            return ((UnitTypeImpl<F>) other).powerMap.equals(powerMap);
        }
        return powerMap.entrySet().stream().allMatch(entry -> other.powerOf(entry.getKey()) == entry.getValue());
    }
}
