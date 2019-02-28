package frc.team7170.lib.fsm;

import frc.team7170.lib.Name;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Consumer;

public class FiniteStateMachine {

    public static class Builder {

        private boolean permitMistrigger;
        @SuppressWarnings("unchecked")
        private Consumer<Event>[] beforeStateChange = new Consumer[0];
        @SuppressWarnings("unchecked")
        private Consumer<Event>[] afterStateChange = new Consumer[0];

        public Builder permitMistrigger() {
            permitMistrigger = true;
            return this;
        }

        public Builder beforeStateChange(Consumer<Event>... beforeStateChange) {
            this.beforeStateChange = beforeStateChange;
            return this;
        }

        public Builder afterStateChange(Consumer<Event>... afterStateChange) {
            this.afterStateChange = afterStateChange;
            return this;
        }

        public FiniteStateMachine build() {
            return new FiniteStateMachine(this);
        }
    }

    private final List<State> states = new ArrayList<>();
    private final List<Transition> transitions = new ArrayList<>();
    private final Map<Trigger, List<Transition>> triggerToTransitions = new HashMap<>();
    private final boolean permitMistrigger;
    private final Consumer<Event>[] beforeStateChange, afterStateChange;
    private State currentState;

    protected FiniteStateMachine(boolean permitMistrigger,
                                 Consumer<Event>[] beforeStateChange,
                                 Consumer<Event>[] afterStateChange) {
        this.permitMistrigger = permitMistrigger;
        this.beforeStateChange = beforeStateChange;
        this.afterStateChange = afterStateChange;
    }

    private FiniteStateMachine(Builder builder) {
        this(builder.permitMistrigger, builder.beforeStateChange, builder.afterStateChange);
    }

    public State newState(State.StateConfig config) {
        if (!isStateNameUnique(config.name.getName())) {
            throw new IllegalArgumentException("a state with name '" + config.name + "' already exists on this machine");
        }
        State state = new State(config, this);
        states.add(state);
        return state;
    }

    private boolean isStateNameUnique(String name) {
        for (State s : states) {
            if (s.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public State newState(Name name) {
        return newState(new State.StateConfig(name));
    }

    public Transition newTransition(Transition.TransitionConfig config) {
        Transition transition = new Transition(config, this);
        transitions.add(transition);
        triggerToTransitions.computeIfAbsent(transition.getTrigger(), trigger -> new ArrayList<>());
        triggerToTransitions.get(transition.getTrigger()).add(transition);
        return transition;
    }

    public Trigger newTrigger(Name name, boolean permitMistrigger) {
        // TODO: add to some list?
        return new Trigger(name, permitMistrigger, this);
    }

    public Trigger newTrigger(Name name) {
        return newTrigger(name, false);
    }

    public List<State> getStates() {
        return new ArrayList<>(states);
    }

    public List<Transition> getTransitions() {
        return new ArrayList<>(transitions);
    }

    public Set<Trigger> getTriggers() {
        return triggerToTransitions.keySet();
    }

    public State getState() {
        return currentState;
    }

    public boolean getPermitMistrigger() {
        return permitMistrigger;
    }

    public boolean inState(State state) {
        assertInitialized();
        return currentState.getLineage().contains(state);
    }

    public void forceTo(State state, Object... arguments) {
        assertInitialized();
        state = state.assureValidNesting();
        Event event = prepareEvent(currentState, state, null, null, arguments);
        beforeStateChange(event);
        currentState.exited(event);
        currentState = state;
        state.entered(event);
        afterStateChange(event);
    }

    private void beforeStateChange(Event event) {
        for (Consumer<Event> bsc : beforeStateChange) {
            bsc.accept(event);
        }
    }

    private void afterStateChange(Event event) {
        for (Consumer<Event> asc : afterStateChange) {
            asc.accept(event);
        }
    }

    boolean executeTrigger(Trigger trigger, Object[] arguments) {
        assertInitialized();
        for (Transition transition : triggerToTransitions.get(trigger)) {
            State dest = transition.mapsTo(currentState);
            if (dest != null) {
                return executeTransition(transition, dest, trigger, arguments);
            }
        }
        if (permitMistrigger || trigger.getPermitMistrigger() || currentState.getPermitMistrigger()) {
            return false;
        }
        throw new IllegalStateException(String.format("cannot execute trigger '%s' in state '%s'",
                trigger, currentState));
    }

    boolean executeTransition(Transition transition, Object[] arguments) {
        assertInitialized();
        State dest = transition.mapsTo(currentState);
        if (dest == null) {
            if (permitMistrigger || transition.getPermitMistrigger() || currentState.getPermitMistrigger()) {
                return false;
            } else {
                throw new IllegalStateException(String.format("cannot execute transition '%s' in state '%s'",
                        transition, currentState));
            }
        }
        return executeTransition(transition, dest, null, arguments);
    }

    private boolean executeTransition(Transition transition, State dest, Trigger trigger, Object[] arguments) {
        dest = dest.assureValidNesting();
        Event event = prepareEvent(currentState, dest, transition, trigger, arguments);

        transition.prepare(event);
        if (!transition.checkConditions(event)) {
            return false;
        }
        beforeStateChange(event);
        transition.started(event);
        if (!transition.isInternal()) {
            currentState.exited(event);
            currentState = dest;
            dest.entered(event);
        }
        transition.ended(event);
        afterStateChange(event);
        return true;
    }

    private Event prepareEvent(State src, State dest, Transition transition, Trigger trigger, Object[] arguments) {
        Event event = new Event(src, dest, transition, this, trigger);
        event.arguments = arguments;
        return event;
    }

    public void initialize(State state) {
        if (currentState != null) {
            throw new IllegalStateException("state machine is already initialized");
        }
        currentState = state;
    }

    private void assertInitialized() {
        if (currentState == null) {
            throw new IllegalStateException("state machine not initialized");
        }
    }
}
