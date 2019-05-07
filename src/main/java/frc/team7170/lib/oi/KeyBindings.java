package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.Preferences;
import frc.team7170.lib.Name;
import frc.team7170.lib.Named;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

// TODO: this should only facilitate a global structure; using the OI system with dependency injection should be fully supported too
// TODO: warn if multiple actions have same binding
// TODO: get rid of need to bind with controller AND containing controller?
public final class KeyBindings implements Named {

    private static final String PREF_KEY_PREFIX = "KM_";

    private final Map<String, Controller> controllerMap = new HashMap<>();
    private final Map<String, AxisAction> axisActionMap = new HashMap<>();
    private final Map<String, ButtonAction> buttonActionMap = new HashMap<>();
    private final Map<String, KeyMap> keyMapMap = new HashMap<>();
    private final List<BiConsumer<KeyMap, KeyMap>> keyMapChangeCallbacks = new ArrayList<>(2);
    private KeyMap currKeyMap = new SerializableKeyMap.Builder(new Name("dummy")).build();

    private KeyBindings() {
        onKeyMapChange((__, ___) -> ButtonPollHelper.refreshInstances());
        onKeyMapChange((__, ___) -> AxisPollHelper.refreshInstances());
    }

    private static final KeyBindings INSTANCE = new KeyBindings();

    public static KeyBindings getInstance() {
        return INSTANCE;
    }

    public void setCurrKeyMap(KeyMap keyMap) {
        if (keyMap == null) {
            throw new NullPointerException();
        }
        keyMapChangeCallbacks.forEach(callback -> callback.accept(currKeyMap, keyMap));
        currKeyMap = keyMap;
    }

    public void setCurrKeyMap(String keyMapName) {
        KeyMap keyMap = keyMapMap.get(keyMapName);
        if (keyMap == null) {
            throw new IllegalArgumentException("no keymap with the name '" + keyMapName + "' exists");
        }
        setCurrKeyMap(keyMap);
    }

    public void onKeyMapChange(BiConsumer<KeyMap, KeyMap> callback) {
        keyMapChangeCallbacks.add(callback);
    }

    // TODO: add way to remove KM change callback (using the BiConsumer might be awkward--use integer ID instead?)

    public void loadFromPrefs() {
        getKMKeyStream().forEach(key -> {
            String value = Preferences.getInstance().getString(key, null);
            Name name = new Name(key.substring(PREF_KEY_PREFIX.length()));
            SerializableKeyMap keyMap = parseToKeymap(value, name);
            keyMapMap.put(keyMap.getName(), keyMap);
        });
    }

    public void clearPrefs() {
        getKMKeyStream().forEach(key -> Preferences.getInstance().remove(key));
    }

    public void registerController(Controller controller) {
        controllerMap.put(controller.getName(), controller);
    }

    public void registerAxisActions(AxisAction... actions) {
        for (AxisAction action : actions) {
            axisActionMap.put(action.name(), action);
        }
    }

    public void registerButtonActions(ButtonAction... actions) {
        for (ButtonAction action : actions) {
            buttonActionMap.put(action.name(), action);
        }
    }

    public void registerSerializableKeyMap(SerializableKeyMap keyMap) {
        Preferences.getInstance().putString(PREF_KEY_PREFIX + keyMap.getName(), keyMap.serialize());
        registerKeyMap(keyMap);
    }

    public void registerKeyMap(KeyMap keyMap) {
        keyMapMap.put(keyMap.getName(), keyMap);
    }

    public Axis actionToAxis(AxisAction action) {
        return currKeyMap.actionToAxis(action);
    }

    public Button actionToButton(ButtonAction action) {
        return currKeyMap.actionToButton(action);
    }

    public KeyMap getCurrKeyMap() {
        return currKeyMap;
    }

    public Collection<KeyMap> getKeyMaps() {
        return keyMapMap.values();
    }

    // TODO: move decode functionality to SerializableKeyMap

    private SerializableKeyMap parseToKeymap(String value, Name name) {
        SerializableKeyMap.Builder kmBuilder = new SerializableKeyMap.Builder(name);
        Arrays.stream(value.split(SerializableKeyMap.ENTRY_SEP))
                .map(triplet -> triplet.split(SerializableKeyMap.TRIPLET_SEP))
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

    private Controller resolveControllerName(String controllerName) {
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

    @Override
    public String getName() {
        return "keyBindings";
    }
}
