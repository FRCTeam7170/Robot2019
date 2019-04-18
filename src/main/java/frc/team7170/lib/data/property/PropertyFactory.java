package frc.team7170.lib.data.property;

import java.util.function.*;

public final class PropertyFactory {

    // Enforce non-instantiability.
    private PropertyFactory() {}

    @FunctionalInterface
    public interface BooleanConsumer {

        void accept(boolean value);
    }

    public static RProperty newBooleanRProperty(String name, int pollPeriodMs, BooleanSupplier getter) {
        return new BaseRProperty(name, PropertyType.BOOLEAN, pollPeriodMs) {
            @Override
            public boolean getBoolean() {
                return getter.getAsBoolean();
            }
        };
    }

    public static WProperty newBooleanWProperty(String name, BooleanConsumer setter) {
        return new BaseWProperty(name, PropertyType.BOOLEAN) {
            @Override
            public void setBoolean(boolean value) {
                setter.accept(value);
            }
        };
    }

    public static RWProperty newBooleanRWProperty(String name, int pollPeriodMs,
                                                  BooleanSupplier getter, BooleanConsumer setter) {
        return new BaseRWProperty(name, PropertyType.BOOLEAN, pollPeriodMs) {
            @Override
            public boolean getBoolean() {
                return getter.getAsBoolean();
            }

            @Override
            public void setBoolean(boolean value) {
                setter.accept(value);
            }
        };
    }

    public static RProperty newDoubleRProperty(String name, int pollPeriodMs, DoubleSupplier getter) {
        return new BaseRProperty(name, PropertyType.DOUBLE, pollPeriodMs) {
            @Override
            public double getDouble() {
                return getter.getAsDouble();
            }
        };
    }

    public static WProperty newDoubleWProperty(String name, DoubleConsumer setter) {
        return new BaseWProperty(name, PropertyType.DOUBLE) {
            @Override
            public void setDouble(double value) {
                setter.accept(value);
            }
        };
    }

    public static RWProperty newDoubleRWProperty(String name, int pollPeriodMs,
                                                 DoubleSupplier getter, DoubleConsumer setter) {
        return new BaseRWProperty(name, PropertyType.DOUBLE, pollPeriodMs) {
            @Override
            public double getDouble() {
                return getter.getAsDouble();
            }

            @Override
            public void setDouble(double value) {
                setter.accept(value);
            }
        };
    }

    public static RProperty newStringRProperty(String name, int pollPeriodMs, Supplier<String> getter) {
        return new BaseRProperty(name, PropertyType.STRING, pollPeriodMs) {
            @Override
            public String getString() {
                return getter.get();
            }
        };
    }

    public static WProperty newStringWProperty(String name, Consumer<String> setter) {
        return new BaseWProperty(name, PropertyType.STRING) {
            @Override
            public void setString(String value) {
                setter.accept(value);
            }
        };
    }

    public static RWProperty newStringRWProperty(String name, int pollPeriodMs,
                                                 Supplier<String> getter, Consumer<String> setter) {
        return new BaseRWProperty(name, PropertyType.STRING, pollPeriodMs) {
            @Override
            public String getString() {
                return getter.get();
            }

            @Override
            public void setString(String value) {
                setter.accept(value);
            }
        };
    }

    public static RProperty newBooleanArrayRProperty(String name, int pollPeriodMs, Supplier<boolean[]> getter) {
        return new BaseRProperty(name, PropertyType.BOOLEAN_ARRAY, pollPeriodMs) {
            @Override
            public boolean[] getBooleanArray() {
                return getter.get();
            }
        };
    }

    public static WProperty newBooleanArrayWProperty(String name, Consumer<boolean[]> setter) {
        return new BaseWProperty(name, PropertyType.BOOLEAN_ARRAY) {
            @Override
            public void setBooleanArray(boolean[] value) {
                setter.accept(value);
            }
        };
    }

    public static RWProperty newBooleanArrayRWProperty(String name, int pollPeriodMs,
                                                       Supplier<boolean[]> getter, Consumer<boolean[]> setter) {
        return new BaseRWProperty(name, PropertyType.BOOLEAN_ARRAY, pollPeriodMs) {
            @Override
            public boolean[] getBooleanArray() {
                return getter.get();
            }

            @Override
            public void setBooleanArray(boolean[] value) {
                setter.accept(value);
            }
        };
    }

    public static RProperty newDoubleArrayRProperty(String name, int pollPeriodMs, Supplier<double[]> getter) {
        return new BaseRProperty(name, PropertyType.DOUBLE_ARRAY, pollPeriodMs) {
            @Override
            public double[] getDoubleArray() {
                return getter.get();
            }
        };
    }

    public static WProperty newDoubleArrayWProperty(String name, Consumer<double[]> setter) {
        return new BaseWProperty(name, PropertyType.DOUBLE_ARRAY) {
            @Override
            public void setDoubleArray(double[] value) {
                setter.accept(value);
            }
        };
    }

    public static RWProperty newDoubleArrayRWProperty(String name, int pollPeriodMs,
                                                      Supplier<double[]> getter, Consumer<double[]> setter) {
        return new BaseRWProperty(name, PropertyType.DOUBLE_ARRAY, pollPeriodMs) {
            @Override
            public double[] getDoubleArray() {
                return getter.get();
            }

            @Override
            public void setDoubleArray(double[] value) {
                setter.accept(value);
            }
        };
    }

    public static RProperty newStringArrayRProperty(String name, int pollPeriodMs, Supplier<String[]> getter) {
        return new BaseRProperty(name, PropertyType.STRING_ARRAY, pollPeriodMs) {
            @Override
            public String[] getStringArray() {
                return getter.get();
            }
        };
    }

    public static WProperty newStringArrayWProperty(String name, Consumer<String[]> setter) {
        return new BaseWProperty(name, PropertyType.STRING_ARRAY) {
            @Override
            public void setStringArray(String[] value) {
                setter.accept(value);
            }
        };
    }

    public static RWProperty newStringArrayRWProperty(String name, int pollPeriodMs,
                                                      Supplier<String[]> getter, Consumer<String[]> setter) {
        return new BaseRWProperty(name, PropertyType.STRING_ARRAY, pollPeriodMs) {
            @Override
            public String[] getStringArray() {
                return getter.get();
            }

            @Override
            public void setStringArray(String[] value) {
                setter.accept(value);
            }
        };
    }

    public static RProperty newRawRProperty(String name, int pollPeriodMs, Supplier<byte[]> getter) {
        return new BaseRProperty(name, PropertyType.RAW, pollPeriodMs) {
            @Override
            public byte[] getRaw() {
                return getter.get();
            }
        };
    }

    public static WProperty newRawWProperty(String name, Consumer<byte[]> setter) {
        return new BaseWProperty(name, PropertyType.RAW) {
            @Override
            public void setRaw(byte[] value) {
                setter.accept(value);
            }
        };
    }

    public static RWProperty newRawRWProperty(String name, int pollPeriodMs,
                                              Supplier<byte[]> getter, Consumer<byte[]> setter) {
        return new BaseRWProperty(name, PropertyType.RAW, pollPeriodMs) {
            @Override
            public byte[] getRaw() {
                return getter.get();
            }

            @Override
            public void setRaw(byte[] value) {
                setter.accept(value);
            }
        };
    }
}
