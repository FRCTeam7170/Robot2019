package frc.team7170.lib.oi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AxisPollHelper implements Axis {

    private static final List<AxisPollHelper> instances = new ArrayList<>();
    private static final String errorFormat = "Unbound AxisAction '%s' requested.";

    private final AxisAction action;
    private final Consumer<String> errorHandler;
    private final double defaultValue;
    private Axis axis;

    public AxisPollHelper(AxisAction action, Consumer<String> errorHandler, double defaultValue) {
        this.action = action;
        this.errorHandler = errorHandler;
        this.defaultValue = defaultValue;
        refresh();
        instances.add(this);
    }

    public AxisPollHelper(AxisAction action, Consumer<String> errorHandler) {
        this(action, errorHandler, 0.0);
    }

    public AxisPollHelper(AxisAction action, double defaultValue) {
        this(action, System.out::println, defaultValue);
    }

    public AxisPollHelper(AxisAction action) {
        this(action, System.out::println, 0.0);
    }

    @Override
    public double get() {
        if (axis == null) {
            return defaultValue;
        }
        return axis.get();
    }

    @Override
    public String getName() {
        return axis.getName();
    }

    public void refresh() {
        axis = KeyBindings.getInstance().actionToAxis(action);
        if (axis == null) {
            handleError();
        }
    }

    private void handleError() {
        errorHandler.accept(String.format(errorFormat, action.name()));
    }

    static void refreshInstances() {
        for (AxisPollHelper instance : instances) {
            instance.refresh();
        }
    }
}
