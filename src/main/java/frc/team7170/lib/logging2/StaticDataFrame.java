package frc.team7170.lib.logging2;

import frc.team7170.lib.Name;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

// TODO: thread safety
public class StaticDataFrame implements DataFrame {

    private List<DataFrame> children;
    private final Map<String, Supplier<Value>> sources = new HashMap<>();
    private final DataLogger dataLogger;

    public StaticDataFrame(DataLogger dataLogger) {
        this.dataLogger = dataLogger;
    }

    private Map<String, Value> getFromSources() {
        return sources.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().get()));
    }

    @Override
    public void addBooleanSource(String name, Supplier<Boolean> source) {
        Name.requireValidName(name);
        sources.put(name, () -> new Value(source.get()));
    }

    @Override
    public void addDoubleSource(String name, Supplier<Double> source) {
        Name.requireValidName(name);
        sources.put(name, () -> new Value(source.get()));
    }

    @Override
    public void addStringSource(String name, Supplier<String> source) {
        Name.requireValidName(name);
        sources.put(name, () -> new Value(source.get()));
    }

    @Override
    public void addBooleanArraySource(String name, Supplier<Boolean[]> source) {
        Name.requireValidName(name);
        sources.put(name, () -> new Value(source.get()));
    }

    @Override
    public void addDoubleArraySource(String name, Supplier<Double[]> source) {
        Name.requireValidName(name);
        sources.put(name, () -> new Value(source.get()));
    }

    @Override
    public void addStringArraySource(String name, Supplier<String[]> source) {
        Name.requireValidName(name);
        sources.put(name, () -> new Value(source.get()));
    }

    @Override
    public boolean removeSource(String name) {
        return sources.remove(name) != null;
    }

    @Override
    public void consolidate(DataFrame child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    @Override
    public void update() {
        dataLogger.logValues(getFromSources());
        if (children != null) {
            for (DataFrame child : children) {
                child.update();
            }
        }
    }
}
