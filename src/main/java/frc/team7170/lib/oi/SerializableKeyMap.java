package frc.team7170.lib.oi;

import frc.team7170.lib.Name;
import frc.team7170.lib.Pair;

import java.util.HashMap;
import java.util.Map;

public class SerializableKeyMap implements KeyMap {

    public static final String ENTRY_SEP = ";";
    public static final String TRIPLET_SEP = ":";

    private final Map<AxisAction, Pair<Controller, Axis>> axisMap;
    private final Map<ButtonAction, Pair<Controller, Button>> buttonMap;
    private final Name name;

    public static class Builder {

        private final Name name;
        private final Map<AxisAction, Pair<Controller, Axis>> axisMap;
        private final Map<ButtonAction, Pair<Controller, Button>> buttonMap;

        public Builder(Name name) {
            this.name = name;
            this.axisMap = new HashMap<>();
            this.buttonMap = new HashMap<>();
        }

        public Builder(SerializableKeyMap keyMap) {
            this.name = keyMap.name;
            this.axisMap = keyMap.axisMap;
            this.buttonMap = keyMap.buttonMap;
        }

        public Builder addPair(AxisAction action, Controller controller, Axis axis) {
            axisMap.put(action, new Pair<>(controller, axis));
            return this;
        }

        public Builder addPair(ButtonAction action, Controller controller, Button button) {
            buttonMap.put(action, new Pair<>(controller, button));
            return this;
        }

        public SerializableKeyMap build() {
            return new SerializableKeyMap(this);
        }
    }

    private SerializableKeyMap(Builder builder) {
        this.axisMap = builder.axisMap;
        this.buttonMap = builder.buttonMap;
        this.name = builder.name;
    }

    @Override
    public String getName() {
        return name.getName();
    }

    @Override
    public Name getNameObject() {
        return name;
    }

    @Override
    public Axis actionToAxis(AxisAction action) {
        Pair<Controller, Axis> pair = axisMap.get(action);
        if (pair == null) {
            return null;
        }
        return pair.getRight();
    }

    @Override
    public Button actionToButton(ButtonAction action) {
        Pair<Controller, Button> pair = buttonMap.get(action);
        if (pair == null) {
            return null;
        }
        return pair.getRight();
    }

    @Override
    public boolean hasBindingFor(AxisAction action) {
        return axisMap.containsKey(action);
    }

    @Override
    public boolean hasBindingFor(ButtonAction action) {
        return buttonMap.containsKey(action);
    }

    public String serialize() {
        StringBuilder builder = new StringBuilder();
        buttonMap.entrySet().iterator().forEachRemaining(entry ->
                // ...action:controller:axis;...
                builder.append(entry.getKey().name())
                        .append(TRIPLET_SEP)
                        .append(entry.getValue().getLeft().getName())
                        .append(TRIPLET_SEP)
                        .append(entry.getValue().getRight().getName())
                        .append(ENTRY_SEP)
        );
        axisMap.entrySet().iterator().forEachRemaining(entry ->
                // ...action:controller:button;...
                builder.append(entry.getKey().name())
                        .append(TRIPLET_SEP)
                        .append(entry.getValue().getLeft().getName())
                        .append(TRIPLET_SEP)
                        .append(entry.getValue().getRight().getName())
                        .append(ENTRY_SEP)
        );
        return builder.toString();
    }
}
