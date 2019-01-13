package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.Preferences;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;


// TODO: Make non-static?
public final class KeyBindings {

    private static final String PREF_KEY_PREFIX = "KM_";
    private static final String ENTRY_SEP = ";";
    private static final String TRIPLET_SEP = ":";

    private static final Map<String, Controller> controllerMap = new HashMap<>();
    private static final Map<String, AxisAction> axisActionMap = new HashMap<>();
    private static final Map<String, ButtonAction> buttonActionMap = new HashMap<>();
    private static final Map<String, KeyMap> keyMapMap = new HashMap<>();
    private static KeyMap currKeyMap = new KeyMap.Builder("dummy").build();

    private KeyBindings() {}

    static void verifyName(String name) {
        if (name.contains(":") || name.contains(".")) {
            throw new RuntimeException("axis/button/controller name may not contain ':' or '.'");
        }
    }

    public static void setCurrKeyMap(KeyMap keyMap) {
        if (keyMap == null) {
            throw new NullPointerException();
        }
        currKeyMap = keyMap;
    }

    public static void setCurrKeyMap(String keyMapName) {
        KeyMap keyMap = keyMapMap.get(keyMapName);
        if (keyMap == null) {
            throw new IllegalArgumentException("no keymap with the name '" + keyMapName + "' exists");
        }
        setCurrKeyMap(keyMap);
    }

    public static void loadFromPrefs() {
        getKMKeyStream().forEach(key -> {
            String value = Preferences.getInstance().getString(key, null);
            String name = key.substring(PREF_KEY_PREFIX.length());
            KeyMap keyMap = parseToKeymap(value, name);
            keyMapMap.put(keyMap.getName(), keyMap);
        });
    }

    public static void clearPrefs() {
        getKMKeyStream().forEach(key -> Preferences.getInstance().remove(key));
    }

    public static void registerController(Controller controller) {
        controllerMap.put(controller.getName(), controller);
    }

    public static void registerAxisActions(AxisAction[] actions) {
        for (AxisAction action : actions) {
            axisActionMap.put(action.name(), action);
        }
    }

    public static void registerButtonActions(ButtonAction[] actions) {
        for (ButtonAction action : actions) {
            buttonActionMap.put(action.name(), action);
        }
    }

    public static void registerKeyMap(KeyMap keyMap, boolean saveToPrefs) {
        if (keyMap == null) {
            throw new NullPointerException();
        }
        if (saveToPrefs) {
            Preferences.getInstance().putString(PREF_KEY_PREFIX + keyMap.getName(), serializeKeyMap(keyMap));
        }
        keyMapMap.put(keyMap.getName(), keyMap);
    }

    public static void registerKeyMap(KeyMap keyMap) {
        registerKeyMap(keyMap, true);
    }

    public static Axis actionToAxis(AxisAction action) {
        return currKeyMap.actionToAxis(action);
    }

    public static Button actionToButton(ButtonAction action) {
        return currKeyMap.actionToButton(action);
    }

    public static KeyMap getCurrKeyMap() {
        return currKeyMap;
    }

    public static Collection<KeyMap> getKeyMaps() {
        return keyMapMap.values();
    }

    private static KeyMap parseToKeymap(String value, String name) {
        KeyMap.Builder kmBuilder = new KeyMap.Builder(name);
        Arrays.stream(value.split(ENTRY_SEP))
                .map(triplet -> triplet.split(TRIPLET_SEP))
                .forEach(triplet -> {
                    if (triplet.length != 3) {
                        throw new RuntimeException("preference keymap format invalid; try clearing keymaps");
                    }
                    Controller controller = resolveControllerName(triplet[1]);
                    if (buttonActionMap.containsKey(triplet[0])) {
                        ButtonAction action = buttonActionMap.get(triplet[0]);
                        Button button = resolveButtonName(controller, triplet[2]);
                        kmBuilder.addPair(action, controller, button);
                    } else if (axisActionMap.containsKey(triplet[0])) {
                        AxisAction action = axisActionMap.get(triplet[0]);
                        Axis axis = resolveAxisName(controller, triplet[2]);
                        kmBuilder.addPair(action, controller, axis);
                    } else {
                        throw new RuntimeException("unregistered action name ('" + triplet[0] + "') found in preference keymaps");
                    }
                });
        return kmBuilder.build();
    }

    private static Controller resolveControllerName(String controllerName) {
        Controller controller = controllerMap.get(controllerName);
        if (controller == null) {
            throw new RuntimeException("unregistered controller name ('" + controllerName + "') found in preference keymaps");
        }
        return controller;
    }

    private static Axis resolveAxisName(Controller controller, String axisName) {
        Axis axis = controller.getAxesNamesMap().get(axisName);
        if (axis == null) {
            throw new RuntimeException("could not find axis '" + axisName + "' on controller '" + controller.getName() + "'");
        }
        return axis;
    }

    private static Button resolveButtonName(Controller controller, String buttonName) {
        Button button = controller.getButtonsNamesMap().get(buttonName);
        if (button == null) {
            throw new RuntimeException("could not find button '" + buttonName + "' on controller '" + controller.getName() + "'");
        }
        return button;
    }

    private static Stream<String> getKMKeyStream() {
        return Preferences.getInstance().getKeys().stream().filter(key -> key.startsWith(PREF_KEY_PREFIX));
    }

    private static String serializeKeyMap(KeyMap keyMap) {
        StringBuilder builder = new StringBuilder();
        keyMap.buttonMap.entrySet().iterator().forEachRemaining(entry ->
            builder.append(entry.getKey().name())
                    .append(TRIPLET_SEP)
                    .append(entry.getValue().getLeft().getName())
                    .append(TRIPLET_SEP)
                    .append(entry.getValue().getRight().getButtonName())
                    .append(ENTRY_SEP)
        );
        keyMap.axisMap.entrySet().iterator().forEachRemaining(entry ->
            builder.append(entry.getKey().name())
                    .append(TRIPLET_SEP)
                    .append(entry.getValue().getLeft().getName())
                    .append(TRIPLET_SEP)
                    .append(entry.getValue().getRight().getAxisName())
                    .append(ENTRY_SEP)
        );
        return builder.toString();
    }
}
