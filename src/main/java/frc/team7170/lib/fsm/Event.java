package frc.team7170.lib.fsm;

import java.util.Arrays;

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

    public void assertArgumentsCount(int count) {
        if (arguments.length != count) {
            throw new IllegalArgumentException(
                    String.format("invalid number of arguments; expected %d, got %d", count, arguments.length)
            );
        }
    }

    public void assertArgumentsTypes(Class<?>... types) {
        assertArgumentsCount(types.length);
        for (int i = 0; i < types.length; ++i) {
            if (!types[i].isInstance(arguments[i])) {
                throw new IllegalArgumentException(
                        String.format("invalid arguments type; expected %s, got %s",
                                Arrays.toString(Arrays.stream(types).map(Class::getName).toArray()),
                                Arrays.toString(Arrays.stream(arguments).map(Object::getClass).map(Class::getName).toArray())
                        )
                );
            }
        }
    }

    @Override
    public String toString() {
        return String.format("Src: '%s'; Dest: '%s'; Transition: '%s'; Trigger: '%s'",
                src.getName(), dest.getName(), transition, trigger.getName());
    }
}
