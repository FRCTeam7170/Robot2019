package frc.team7170.lib.fsm;

import frc.team7170.lib.Name;
import frc.team7170.lib.Named;

public class Trigger implements Named {

    private final Name name;
    private final boolean permitMistrigger;
    private final FiniteStateMachine machine;

    Trigger(Name name, boolean permitMistrigger, FiniteStateMachine machine) {
        this.name = name;
        this.permitMistrigger = permitMistrigger;
        this.machine = machine;
    }

    public boolean execute(Object... arguments) {
        return machine.executeTrigger(this, arguments);
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
    public Name getCheckedName() {
        return name;
    }
}
