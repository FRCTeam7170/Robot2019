package frc.team7170.lib.fsm;

import frc.team7170.lib.Name;
import frc.team7170.lib.Named;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class State implements Named {

    private static final String PARENT_CHILD_STR_SEP = ".";

    public static class StateConfig {

        final Name name;
        @SuppressWarnings("unchecked")
        private Consumer<Event>[] onEnter = new Consumer[0];
        @SuppressWarnings("unchecked")
        private Consumer<Event>[] onExit = new Consumer[0];
        private boolean permitNoChildSelection = true;
        private boolean permitMistrigger = false;

        public StateConfig(Name name) {
            this.name = name;
        }

        public StateConfig onEnter(Consumer<Event>... onEnter) {
            this.onEnter = onEnter;
            return this;
        }

        public StateConfig onExit(Consumer<Event>... onExit) {
            this.onExit = onExit;
            return this;
        }

        public StateConfig requireChildSelection() {
            permitNoChildSelection = false;
            return this;
        }

        public StateConfig permitMistrigger() {
            permitMistrigger = true;
            return this;
        }
    }

    private final Name name;
    private final Consumer<Event>[] onEnter, onExit;
    private final boolean permitNoChildSelection;
    private final boolean permitMistrigger;
    private final FiniteStateMachine machine;
    private final State parent;
    private State preferredChild;
    private Set<String> subStateNames;

    private State(StateConfig config, FiniteStateMachine machine, State parent) {
        this.name = config.name;
        this.onEnter = config.onEnter;
        this.onExit = config.onExit;
        this.permitNoChildSelection = config.permitNoChildSelection;
        this.permitMistrigger = config.permitMistrigger;
        this.machine = machine;
        this.parent = parent;
    }

    State(StateConfig config, FiniteStateMachine machine) {
        this(config, machine, null);
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
    public String toString() {
        return lineageToString(new StringBuilder());
    }

    private String lineageToString(StringBuilder builder) {
        builder.insert(0, getName());
        if (hasParent()) {
            builder.insert(0, PARENT_CHILD_STR_SEP);
            parent.lineageToString(builder);
        }
        return builder.toString();
    }

    public State getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public List<State> getLineage() {
        // TODO: cache this?
        List<State> lineage = new ArrayList<>();
        State state = this;
        while (state != null) {
            lineage.add(state);
            state = state.parent;
        }
        return lineage;
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

    public boolean getPermitMistrigger() {
        return permitMistrigger;
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
        if (subStateNames == null) {
            subStateNames = new HashSet<>();
        }
        String name = config.name.getName();
        if (!isSubStateNameUnique(name)) {
            throw new IllegalArgumentException("a sub-state with name '" + name + "' already exists on this state");
        }
        subStateNames.add(name);
        State subState = new State(config, this.machine, this);
        if (preferredChild == null) {
            preferredChild = subState;
        }
        return subState;
    }

    private boolean isSubStateNameUnique(String name) {
        return !subStateNames.contains(name);
    }

    public State addSubState(Name name) {
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

    State assureValidNesting() {
        State state = this;
        while (!state.permitNoChildSelection) {
            state = state.getPreferredChild();
            if (state == null) {
                throw new IllegalStateException("state requiring child selection must have children");
            }
        }
        return state;
    }
}
