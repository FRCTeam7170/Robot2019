package frc.team7170.lib.data;

import frc.team7170.lib.Named;
import frc.team7170.lib.Pair;
import frc.team7170.lib.data.property.Property;

import java.util.List;

public interface DataFrame<P extends Property> extends Named, Iterable<Pair<DataFrame, P>> {

    DataFrame<P> newSubFrame(String name);

    void addSubFrame(DataFrame<P> frame);

    List<DataFrame<P>> getSubFrames();

    DataFrame<P> getParentFrame();

    void merge(DataFrame<P> frame);

    void addProperty(P property);

    List<P> getProperties();
}
