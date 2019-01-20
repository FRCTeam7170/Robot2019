package frc.team7170.lib.fsm;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Transition {

    private enum TransitionModes {
        NORMAL, REFLEXIVE, INTERNAL
    }

    public static class TransitionConfig {

        private final State[] srcs;
        private final State dest;
        private Consumer<Event>[] prepare, onStart, onEnd;
        private Predicate<Event>[] conditions;
        private TransitionModes mode = TransitionModes.NORMAL;

        public TransitionConfig(State[] srcs, State dest) {
            this.srcs = srcs;
            this.dest = dest;
        }

        public TransitionConfig(State src, State dest) {
            this(new State[] {src}, dest);
        }

        public void onStart(Consumer<Event>... onStart) {
            this.onStart = onStart;
        }

        public void onEnd(Consumer<Event>... onEnd) {
            this.onEnd = onEnd;
        }

        public void prepare(Consumer<Event>... prepare) {
            this.prepare = prepare;
        }

        public void conditions(Predicate<Event>... conditions) {
            this.conditions = conditions;
        }

        public static TransitionConfig newReflexive(State... states) {
            TransitionConfig ret = new TransitionConfig(states, null);
            ret.mode = TransitionModes.REFLEXIVE;
            return ret;
        }

        public static TransitionConfig newInternal(State... states) {
            TransitionConfig ret = new TransitionConfig(states, null);
            ret.mode = TransitionModes.INTERNAL;
            return ret;
        }
    }

    private final List<State> srcs;
    private final State dest;
    private final Consumer<Event>[] prepare, onStart, onEnd;
    private final Predicate<Event>[] conditions;
    private final TransitionModes mode;
    private final FiniteStateMachine machine;

    Transition(TransitionConfig config, FiniteStateMachine machine) {
        this.srcs = Arrays.asList(config.srcs);
        this.dest = config.dest;
        this.onStart = config.onStart;
        this.onEnd = config.onEnd;
        this.prepare = config.prepare;
        this.conditions = config.conditions;
        this.mode = config.mode;
        this.machine = machine;
    }

    public List<State> getSrcs() {
        return srcs;
    }

    public State getDest() {
        return dest;
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

    public FiniteStateMachine getMachine() {
        return machine;
    }

    public boolean isReflexive() {
        return mode == TransitionModes.REFLEXIVE;
    }

    public boolean isInternal() {
        return mode == TransitionModes.INTERNAL;
    }

    public State mapsTo(State state) {
        if (!srcs.contains(state)) {
            return null;
        }
        if (dest != null) {
            return dest;
        }
        return state;
    }

    public boolean trigger(Object... arguments) {
        return machine.executeTransition(this, arguments);
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
}
