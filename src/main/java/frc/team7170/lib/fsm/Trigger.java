package frc.team7170.lib.fsm;

import frc.team7170.lib.Name;
import frc.team7170.lib.Named;

import java.util.logging.Logger;

public class Trigger implements Named {

    private static final Logger LOGGER = Logger.getLogger(Trigger.class.getName());

    private final Name name;
    private final boolean permitMistrigger;
    private final FiniteStateMachine machine;

    Trigger(Name name, boolean permitMistrigger, FiniteStateMachine machine) {
        this.name = name;
        this.permitMistrigger = permitMistrigger;
        this.machine = machine;
    }

    public boolean execute(boolean log, Object... arguments) {
        boolean success = machine.executeTrigger(this, arguments);
        if (log) {
            if (success) {
                LOGGER.fine(String.format("Trigger '%s' executed successfully.", toString()));
            } else {
                LOGGER.fine(String.format("Trigger '%s' failed to execute.", toString()));
            }
        }
        return success;
    }

    public boolean execute(Object... arguments) {
        return execute(true, arguments);
    }

    public boolean getPermitMistrigger() {
        return permitMistrigger;
    }

    public FiniteStateMachine getMachine() {
        return machine;
    }

    @Override
    public String getName() {
        return name.getName();
    }

    @Override
    public Name getNameObject() {
        return name;
    }
}
