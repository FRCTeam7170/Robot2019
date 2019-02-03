package frc.team7170.lib.unit.unittypes;

public class BaseUnitType<R extends Enum<R> & IFundamentalUnitType<R>> extends UnitType<R> {

    protected BaseUnitType(Class<R> futClass, R fut) {
        super(getConfig(futClass, fut));
    }

    private static <R extends Enum<R> & IFundamentalUnitType<R>> Config<R> getConfig(Class<R> futClass, R fut) {
        Config<R> config = new Config<>(futClass);
        if (fut != null) {
            config.power(fut, 1);
        }
        return config;
    }
}
