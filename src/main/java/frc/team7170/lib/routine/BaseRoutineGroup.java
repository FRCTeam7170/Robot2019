package frc.team7170.lib.routine;

import java.util.Collection;
import java.util.Iterator;

public abstract class BaseRoutineGroup extends Routine {

    protected final Collection<Routine> routines;

    protected BaseRoutineGroup(boolean interruptable,
                               Collection<Subsystem> requirements,
                               Collection<Routine> routines) {
        super(interruptable, requirements);
        this.routines = requireLengthGT2(routines);
        this.routines.forEach(r -> r.claimForComposition(this));
    }

    @Override
    public String toString() {
        // "<ClassName>(Routine1, Routine2, Routine3, ...)"
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append("(");
        Iterator<Routine> routineIterator = routines.iterator();
        sb.append(routineIterator.next().toString());
        routineIterator.forEachRemaining(r -> {
            sb.append(", ");
            sb.append(r.toString());
        });
        sb.append(")");
        return sb.toString();
    }

    private static Collection<Routine> requireLengthGT2(Collection<Routine> routines) {
        if (routines.size() < 2) {
            throw new IllegalArgumentException("cannot group fewer than two Routines");
        }
        return routines;
    }
}
