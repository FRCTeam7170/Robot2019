package frc.team7170.lib.unit;

import frc.team7170.lib.unit.unittypes.*;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;

// TODO: allow adding non basis unit types to facilitate more complex derived units
public class UnitSet<R extends Enum<R> & IFundamentalUnitType<R>> {

    protected static class Entry<R extends Enum<R> & IFundamentalUnitType<R>> {

        public final ScaledUnit<R, ? extends UnitType<R>> unit;
        public final int numTimes;

        protected Entry(ScaledUnit<R, ? extends UnitType<R>> unit, int numTimes) {
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

        public Builder(UnitSet<R> unitSet) {
            this.futClass = unitSet.futClass;
            this.unitMap = unitSet.unitMap;
        }

        public Builder<R> map(R fut, ScaledUnit<R, ? extends UnitType<R>> unit, int times) {
            unitMap.get(fut).add(0, new Entry<>(unit, times));
            return this;
        }

        public Builder<R> map(R fut, ScaledUnit<R, ? extends UnitType<R>> unit) {
            ArrayList<Entry<R>> arrayList = unitMap.get(fut);
            arrayList.remove(unitMap.size() - 1);
            arrayList.add(arrayList.size(), new Entry<>(unit, -1));
            return this;
        }

        public Builder<R> clearMappings(R fut) {
            ArrayList<Entry<R>> arrayList = unitMap.get(fut);
            Entry<R> last = arrayList.get(arrayList.size() - 1);
            arrayList.clear();
            arrayList.add(last);
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

    protected <T extends UnitType<R>> double calcFactorFor(T type) {
        double factor = 1.0;
        for (R fut : futClass.getEnumConstants()) {
            int powersNeeded = type.powerOf(fut);
            boolean isInverted = powersNeeded < 0;
            powersNeeded = Math.abs(powersNeeded);
            Iterator<Entry<R>> iterator = unitMap.get(fut).iterator();
            while (powersNeeded > 0) {
                Entry<R> entry = iterator.next();
                int power;
                if (entry.numTimes == -1) {
                    power = powersNeeded;
                } else {
                    power = Math.min(powersNeeded, entry.numTimes);
                }
                factor *= Math.pow(entry.unit.getFactor(), isInverted ? -power : power);
                powersNeeded -= power;
            }
        }
        return factor;
    }
}
