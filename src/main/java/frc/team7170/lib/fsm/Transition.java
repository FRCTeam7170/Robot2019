package frc.team7170.lib.fsm;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Logger;

public class Transition {

    private static final Logger LOGGER = Logger.getLogger(Transition.class.getName());

    public static final State[] ALL_STATES = new State[0];

    private enum TransitionMode {
        NORMAL, REFLEXIVE, INTERNAL
    }

    public static class TransitionConfig {

        private final State[] srcs;
        private final State dest;
        private final Trigger trigger;
        @SuppressWarnings("unchecked")
        private Consumer<Event>[] prepare = new Consumer[0];
        @SuppressWarnings("unchecked")
        private Consumer<Event>[] onStart = new Consumer[0];
        @SuppressWarnings("unchecked")
        private Consumer<Event>[] onEnd = new Consumer[0];
        @SuppressWarnings("unchecked")
        private Predicate<Event>[] conditions = new Predicate[0];
        private boolean permitMistrigger;
        private TransitionMode mode = TransitionMode.NORMAL;

        public TransitionConfig(State[] srcs, State dest, Trigger trigger) {
            this.srcs = srcs;
            this.dest = dest;
            this.trigger = trigger;
        }

        public TransitionConfig(State src, State dest, Trigger trigger) {
            this(new State[] {src}, dest, trigger);
        }

        public TransitionConfig onStart(Consumer<Event>... onStart) {
            this.onStart = onStart;
            return this;
        }

        public TransitionConfig onEnd(Consumer<Event>... onEnd) {
            this.onEnd = onEnd;
            return this;
        }

        public TransitionConfig prepare(Consumer<Event>... prepare) {
            this.prepare = prepare;
            return this;
        }

        public TransitionConfig conditions(Predicate<Event>... conditions) {
            this.conditions = conditions;
            return this;
        }

        public TransitionConfig permitMistrigger() {
            permitMistrigger = true;
            return this;
        }

        public static TransitionConfig newReflexive(State[] states, Trigger trigger) {
            TransitionConfig ret = new TransitionConfig(states, null, trigger);
            ret.mode = TransitionMode.REFLEXIVE;
            return ret;
        }

        public static TransitionConfig newReflexive(State state, Trigger trigger) {
            return newReflexive(new State[] {state}, trigger);
        }

        public static TransitionConfig newInternal(State[] states, Trigger trigger) {
            TransitionConfig ret = new TransitionConfig(states, null, trigger);
            ret.mode = TransitionMode.INTERNAL;
            return ret;
        }

        public static TransitionConfig newInternal(State state, Trigger trigger) {
            return newInternal(new State[] {state}, trigger);
        }
    }

    private final List<State> srcs;
    private final State dest;
    private final Trigger trigger;
    private final Consumer<Event>[] prepare, onStart, onEnd;
    private final Predicate<Event>[] conditions;
    private final boolean permitMistrigger;
    private final TransitionMode mode;
    private final FiniteStateMachine machine;

    Transition(TransitionConfig config, FiniteStateMachine machine) {
        if (config.trigger.getMachine() != machine) {
            throw new IllegalArgumentException("trigger machine and given machine do not match");
        }
        this.srcs = Arrays.asList(config.srcs);
        this.dest = config.dest;
        this.trigger = config.trigger;
        this.onStart = config.onStart;
        this.onEnd = config.onEnd;
        this.prepare = config.prepare;
        this.conditions = config.conditions;
        this.permitMistrigger = config.permitMistrigger;
        this.mode = config.mode;
        this.machine = machine;
    }

    public List<State> getSrcs() {
        return new ArrayList<>(srcs);
    }

    public boolean isWildcard() {
        return srcs.isEmpty();
    }

    public State getDest() {
        return dest;
    }

    public Trigger getTrigger() {
        return trigger;
    }

    public Consumer<Event>[] getOnStart() {
        return onStart;
    }

    public Consumer<Event>[] getOnEnd() {
        return onEnd;
    }

    public Consumer<Event>[] getPrepare() {
        return prepare;
    }

    public Predicate<Event>[] getConditions() {
        return conditions;
    }

    public boolean getPermitMistrigger() {
        return permitMistrigger;
    }

    public FiniteStateMachine getMachine() {
        return machine;
    }

    public boolean isReflexive() {
        return mode == TransitionMode.REFLEXIVE;
    }

    public boolean isInternal() {
        return mode == TransitionMode.INTERNAL;
    }

    public State mapsTo(State state) {
        if (state == null) {
            throw new NullPointerException();
        }
        if (!isWildcard()) {
            while (state != null) {
                if (srcs.contains(state)) {
                    break;
                }
                state = state.getParent();
            }
        }
        if (state == null) {
            return null;
        }
        if (dest != null) {
            return dest;
        }
        return state;
    }

    public boolean hasMappingFor(State state) {
        return mapsTo(state) != null;
    }

    public boolean execute(boolean log, Object... arguments) {
        boolean success = machine.executeTransition(this, arguments);
        if (log) {
            if (success) {
                LOGGER.fine(String.format("Transition '%s' executed successfully.", toString()));
            } else {
                LOGGER.fine(String.format("Transition '%s' failed to execute.", toString()));
            }
        }
        return success;
    }

    public boolean execute(Object... arguments) {
        return execute(true, arguments);
    }

    void started(Event event) {
        for (Consumer<Event> os : onStart) {
            os.accept(event);
        }
    }

    void ended(Event event) {
        for (Consumer<Event> oe : onEnd) {
            oe.accept(event);
        }
    }

    void prepare(Event event) {
        for (Consumer<Event> p : prepare) {
            p.accept(event);
        }
    }

    boolean checkConditions(Event event) {
        for (Predicate<Event> condition : conditions) {
            if (!condition.test(event)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s -> %s", srcs.isEmpty() ? "ALL" : srcs, dest);
    }
}
