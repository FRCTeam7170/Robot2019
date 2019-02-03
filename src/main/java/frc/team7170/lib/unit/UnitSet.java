package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.*;

import java.util.ArrayList;
import java.util.EnumMap;

public class UnitSet<R extends Enum<R> & IFundamentalUnitType<R>> {

    public static class Entry<R extends Enum<R> & IFundamentalUnitType<R>> {

        public final Unit<R, ? extends UnitType<R>> unit;
        public final int numTimes;

        protected Entry(Unit<R, ? extends UnitType<R>> unit, int numTimes) {
            this.unit = unit;
            this.numTimes = numTimes;
        }
    }

    public static class Builder<R extends Enum<R> & IFundamentalUnitType<R>> {

        private final Class<R> futClass;
        private final EnumMap<R, ArrayList<Entry<R>>> unitMap;

        public Builder(Class<R> futClass) {
            this.futClass = futClass;
            unitMap = new EnumMap<>(futClass);
            for (R fut : futClass.getEnumConstants()) {
                ArrayList<Entry<R>> arrayList = new ArrayList<>(5);
                arrayList.add(new Entry<>(fut.getBaseUnit(), -1));
                unitMap.put(fut, arrayList);
            }
        }

        public Builder map(R fut, Unit<R, ? extends UnitType<R>> unit, int times) {
            unitMap.get(fut).add(0, new Entry<>(unit, times));
            return this;
        }

        public UnitSet<R> build() {
            return new UnitSet<>(this);
        }
    }

    private final Class<R> futClass;
    private final EnumMap<R, ArrayList<Entry<R>>> unitMap;

    private UnitSet(Builder<R> builder) {
        this.futClass = builder.futClass;
        this.unitMap = builder.unitMap;
    }

    public <T extends UnitType<R>> double factorBetween(UnitSet<R> other, T type) {
        for (R fut : futClass.getEnumConstants()) {
            // TODO: add a BaseUnitType class
        }
        return 0.0;
    }
}
