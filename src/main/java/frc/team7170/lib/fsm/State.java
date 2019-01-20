package frc.team7170.lib.fsm;

import java.util.function.Consumer;

public class State {

    public static class StateConfig {

        private final String name;
        private Consumer<Event>[] onEnter, onExit;
        private boolean permitNoChildSelection = false;
        // TODO: permitAllOutgoing & permitAllIncoming

        public StateConfig(String name) {
            this.name = name;
        }

        public void onEnter(Consumer<Event>... onEnter) {
            this.onEnter = onEnter;
        }

        public void onExit(Consumer<Event>... onExit) {
            this.onExit = onExit;
        }

        public void permitNoChildSelection() {
            permitNoChildSelection = true;
        }
    }

    private final String name;
    private final Consumer<Event>[] onEnter, onExit;
    private final boolean permitNoChildSelection;  // TODO
    private final FiniteStateMachine machine;
    private final State parent;
    private State preferredChild;

    private State(StateConfig config, FiniteStateMachine machine, State parent) {
        this.name = config.name;
        this.onEnter = config.onEnter;
        this.onExit = config.onExit;
        this.permitNoChildSelection = config.permitNoChildSelection;
        this.machine = machine;
        this.parent = parent;
    }

    State(StateConfig config, FiniteStateMachine machine) {
        this(config, machine, null);
    }

    public String getName() {
        return name;
    }

    public State getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Consumer<Event>[] getOnEnter() {
        return onEnter;
    }

    public Consumer<Event>[] getOnExit() {
        return onExit;
    }

    public boolean getPermitNoChildSelection() {
        return permitNoChildSelection;
    }

    public FiniteStateMachine getMachine() {
        return machine;
    }

    public void setPreferredChild(State preferredChild) {
        if (preferredChild.getParent() != this) {
            throw new RuntimeException("cannot set preferredChild to a state not parented by this State");
        }
        this.preferredChild = preferredChild;
    }

    public State getPreferredChild() {
        return preferredChild;
    }

    public State addSubState(StateConfig config) {
        State subState = new State(config, this.machine, this);
        if (preferredChild == null) {
            preferredChild = subState;
        }
        return subState;
    }

    public State addSubState(String name) {
        return addSubState(new StateConfig(name));
    }

    void entered(Event event) {
        for (Consumer<Event> oe : onEnter) {
            oe.accept(event);
        }
    }

    void exited(Event event) {
        for (Consumer<Event> oe : onExit) {
            oe.accept(event);
        }
    }
}
