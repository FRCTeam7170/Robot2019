package frc.team7170.lib.data;

import frc.team7170.lib.Name;
import frc.team7170.lib.data.property.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DefaultDataFrame implements DataFrame {

    private final String name;
    private final List<DataFrame> subframes = new ArrayList<>();

    public DefaultDataFrame(String name) {
        this.name = Name.requireValidName(name);
    }

    @Override
    public DataFrame newSubFrame(String name) {
        DataFrame frame = new DefaultDataFrame(name);
        subframes.add(frame);
        return frame;
    }

    @Override
    public void addSubFrame(DataFrame frame) {
        subframes.add(Objects.requireNonNull(frame));
    }

    @Override
    public void merge(DataFrame frame) {

    }

    @Override
    public void addProperty(Property property) {

    }

    @Override
    public boolean removeProperty(String name) {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }
}
