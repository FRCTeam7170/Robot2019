package frc.team7170.lib.oi;

import frc.team7170.util.Pair;

import java.util.HashMap;
import java.util.Map;


public class KeyMap {

    final Map<AxisAction, Pair<Controller, Axis>> axisMap;
    final Map<ButtonAction, Pair<Controller, Button>> buttonMap;
    private final String name;

    public static class Builder {

        private final Map<AxisAction, Pair<Controller, Axis>> axisMap;
        private final Map<ButtonAction, Pair<Controller, Button>> buttonMap;
        private final String name;

        public Builder(String name) {
            this.name = name;
            axisMap = new HashMap<>();
            buttonMap = new HashMap<>();
        }

        public Builder(KeyMap keyMap) {
            name = keyMap.name;
            axisMap = keyMap.axisMap;
            buttonMap = keyMap.buttonMap;
        }

        public Builder addPair(AxisAction action, Controller controller, Axis axis) {
            axisMap.put(action, new Pair<>(controller, axis));
            return this;
        }

        public Builder addPair(ButtonAction action, Controller controller, Button button) {
            buttonMap.put(action, new Pair<>(controller, button));
            return this;
        }

        public KeyMap build() {
            return new KeyMap(this);
        }
    }

    private KeyMap(Builder builder) {
        this.axisMap = builder.axisMap;
        this.buttonMap = builder.buttonMap;
        this.name = builder.name;
    }

    public String getName() {
        return name;
    }

    public Axis actionToAxis(AxisAction action) {
        return axisMap.get(action).getRight();
    }

    public Button actionToButton(ButtonAction action) {
        return buttonMap.get(action).getRight();
    }
}
