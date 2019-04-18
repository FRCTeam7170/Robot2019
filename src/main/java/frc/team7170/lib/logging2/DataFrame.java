package frc.team7170.lib.logging2;

import java.util.function.Supplier;

public interface DataFrame {

    void addBooleanSource(String name, Supplier<Boolean> source);

    void addDoubleSource(String name, Supplier<Double> source);

    void addStringSource(String name, Supplier<String> source);

    void addBooleanArraySource(String name, Supplier<Boolean[]> source);

    void addDoubleArraySource(String name, Supplier<Double[]> source);

    void addStringArraySource(String name, Supplier<String[]> source);

    boolean removeSource(String name);

    void consolidate(DataFrame child);

    void update();
}
