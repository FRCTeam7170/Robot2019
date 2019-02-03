package frc.team7170.lib.unit.unittypes;

import java.util.EnumMap;

public class UnitType<R extends Enum<R> & IFundamentalUnitType<R>> {

    protected static class Config<R extends Enum<R> & IFundamentalUnitType<R>> {

        public final EnumMap<R, Integer> powerMap;

        public Config(Class<R> futClass) {
            powerMap = new EnumMap<>(futClass);
            for (R fut : futClass.getEnumConstants()) {
                powerMap.put(fut, 0);
            }
        }

        public Config<R> power(R fut, int pow) {
            powerMap.put(fut, pow);
            return this;
        }
    }

    private final Class<R> cls;
    private final EnumMap<R, Integer> powerMap;

    // TODO: this should accept Config, not EnumMap
    protected UnitType(Class<R> futClass, EnumMap<R, Integer> powerMap) {
        cls = futClass;
        this.powerMap = powerMap;
    }

    public UnitType<R> multiply(UnitType<R> multiplier) {
        EnumMap<R, Integer> newMap = new EnumMap<>(cls);
        powerMap.forEach((fut, i) -> newMap.put(fut, i + multiplier.powerMap.get(fut)));
        return new UnitType<>(cls, newMap);
    }

    public UnitType<R> divide(UnitType<R> divisor) {
        // The following would be more elegant, but makes an unnecessary intermediary instance.
        // return multiply(divisor.reciprocal());
        EnumMap<R, Integer> newMap = new EnumMap<>(cls);
        powerMap.forEach((fut, i) -> newMap.put(fut, i - divisor.powerMap.get(fut)));
        return new UnitType<>(cls, newMap);
    }

    public UnitType<R> reciprocal() {
        EnumMap<R, Integer> newMap = new EnumMap<>(cls);
        powerMap.forEach((fut, i) -> newMap.put(fut, -i));
        return new UnitType<>(cls, newMap);
    }

    public int powerOf(R fut) {
        return powerMap.get(fut);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof UnitType<?>)) {
            return false;
        }
        UnitType<?> other = (UnitType<?>) obj;
        return powerMap == other.powerMap;
    }

    @Override
    public int hashCode() {
        return powerMap.hashCode();
    }
}
