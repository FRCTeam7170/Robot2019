package frc.team7170.lib.oi;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ButtonPollHelper implements Button {

    private static final List<ButtonPollHelper> instances = new ArrayList<>();
    private static final String errorFormat = "Unbound ButtonAction '%s' requested.";

    private final ButtonAction action;
    private final Consumer<String> errorHandler;
    private final boolean defaultValue;
    private Button button;

    public ButtonPollHelper(ButtonAction action, Consumer<String> errorHandler, boolean defaultValue) {
        this.action = action;
        this.errorHandler = errorHandler;
        this.defaultValue = defaultValue;
        refresh();
        instances.add(this);
    }

    public ButtonPollHelper(ButtonAction action, Consumer<String> errorHandler) {
        this(action, errorHandler, false);
    }

    public ButtonPollHelper(ButtonAction action, boolean defaultValue) {
        this(action, System.out::println, defaultValue);
    }

    public ButtonPollHelper(ButtonAction action) {
        this(action, System.out::println, false);
    }

    @Override
    public boolean get() {
        if (button == null) {
            return defaultValue;
        }
        return button.get();
    }

    @Override
    public boolean getPressed() {
        if (button == null) {
            return false;
        }
        return button.getPressed();
    }

    @Override
    public boolean getReleased() {
        if (button == null) {
            return false;
        }
        return button.getReleased();
    }

    @Override
    public String getName() {
        return button.getName();
    }

    public void refresh() {
        button = KeyBindings.getInstance().actionToButton(action);
        if (button == null) {
            handleError();
        }
    }

    private void handleError() {
        errorHandler.accept(String.format(errorFormat, action.name()));
    }

    static void refreshInstances() {
        for (ButtonPollHelper instance : instances) {
            instance.refresh();
        }
    }
}
