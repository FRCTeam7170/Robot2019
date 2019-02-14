package frc.team7170.lib.fsm;

public class Event {

    public final State src;
    public final State dest;
    public final Transition transition;
    public final FiniteStateMachine machine;
    private final Trigger trigger;
    public Object[] arguments;

    Event(State src, State dest, Transition transition, FiniteStateMachine machine, Trigger trigger) {
        this.src = src;
        this.dest = dest;
        this.transition = transition;
        this.machine = machine;
        this.trigger = trigger;
    }
}
