package frc.team7170.lib.fsm;

import java.util.ArrayList;
import java.util.function.Consumer;

public class FiniteStateMachine {

    public static class Builder {

        private boolean permitMistrigger;
        private Consumer<Event>[] beforeStateChange, afterStateChange;

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

    private final ArrayList<State> states = new ArrayList<>();
    private final ArrayList<Transition> transitions = new ArrayList<>();
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
        State state = new State(config, this);
        states.add(state);
        // Make first state the initial state.
        if (currentState == null) {
            currentState = state;
        }
        return state;
    }

    public State newState(String name) {
        return newState(new State.StateConfig(name));
    }

    public Transition newTransition(Transition.TransitionConfig config) {
        return new Transition(config, this);
    }

    public ArrayList<State> getStates() {
        return states;
    }

    public ArrayList<Transition> getTransitions() {
        return transitions;
    }

    public State getState() {
        return currentState;
    }

    public boolean getPermitMistrigger() {
        return permitMistrigger;
    }

    public ArrayList<State> getStateChain() {
        ArrayList<State> stateChain = new ArrayList<>();
        State state = currentState;
        while (state != null) {
            stateChain.add(state);
            state = state.getParent();
        }
        return stateChain;
    }

    public boolean inState(State state) {
        return getStateChain().contains(state);
    }

    public void forceTo(State state, Object... arguments) {
        Event event = prepareEvent(currentState, state, null, arguments);
        beforeStateChange(event);
        currentState.exited(event);
        currentState = state;
        state.entered(event);
        afterStateChange(event);
    }

    protected void beforeStateChange(Event event) {
        for (Consumer<Event> bsc : beforeStateChange) {
            bsc.accept(event);
        }
    }

    protected void afterStateChange(Event event) {
        for (Consumer<Event> asc : afterStateChange) {
            asc.accept(event);
        }
    }

    protected boolean executeTransition(Transition transition, Object[] arguments) {
        if (!canExecuteTransition(transition)) {
            if (permitMistrigger) {
                return false;
            } else {
                throw new RuntimeException("cannot execute given transition in current state");  // TODO: custom error?
            }
        }
        State dest = transition.mapsTo(currentState);
        Event event = prepareEvent(currentState, dest, transition, arguments);

        transition.prepare(event);
        if (!transition.checkConditions(event)) {
            return false;
        }
        beforeStateChange(event);
        transition.started(event);
        currentState.exited(event);
        currentState = dest;
        dest.entered(event);
        transition.ended(event);
        afterStateChange(event);
        return true;
    }

    protected Event prepareEvent(State src, State dest, Transition transition, Object[] arguments) {
        Event event = new Event(src, dest, transition, this);
        event.arguments = arguments;
        return event;
    }

    protected boolean canExecuteTransition(Transition transition) {
        return transition.getSrcs().contains(currentState);
    }

    private State assureValidNesting(State state) {
        while (state.hasParent() && !state.getPermitNoChildSelection()) {
            state = state.getPreferredChild();
            if (state == null) {
                throw new RuntimeException("invalid machine state: state requiring child selection must have children");
            }
        }
        return state;
    }
}
