package frc.team7170.lib.routine;

import frc.team7170.lib.looping.IterativeLooper;
import frc.team7170.lib.looping.Loop;
import frc.team7170.lib.looping.Looper;

import java.util.*;
import java.util.logging.Logger;

public final class RoutineScheduler {

    private static final Logger LOGGER = Logger.getLogger(RoutineScheduler.class.getName());

    private final Looper looper;
    private final Map<Subsystem, Integer> subsystemClaimsMap = new HashMap<>();
    private final Set<Routine> currentlyRunning = new HashSet<>();

    public RoutineScheduler(Looper looper) {
        this.looper = looper;
    }

    private static RoutineScheduler DEFAULT_SCHEDULER;

    public static RoutineScheduler getDefault() {
        if (DEFAULT_SCHEDULER == null) {
            Looper looper = new IterativeLooper(20);
            DEFAULT_SCHEDULER = new RoutineScheduler(looper);
            looper.startLoops();
        }
        return DEFAULT_SCHEDULER;
    }

    public Looper getLooper() {
        return looper;
    }

    public void registerSubsystem(Subsystem subsystem) {
        if (subsystemClaimsMap.containsKey(
                Objects.requireNonNull(subsystem, "cannot register null Subsystem")
        )) {
            throw new IllegalArgumentException(String.format("Subsystem '%s' already registered", subsystem.getName()));
        }
        subsystemClaimsMap.put(subsystem, 0);
        looper.registerLoop(
                new Loop() {
                    @Override
                    protected void onLoop() {
                        subsystem.onLoop();
                    }
                }
        );
        runDefaultRoutine(subsystem);
    }

    public boolean trySchedule(Routine routine) {
        return tryScheduleInContext(routine, Collections.emptySet());
    }

    boolean tryScheduleInContext(Routine routine, Set<Subsystem> context) {
        if (canRun(routine, context)) {
            claimRequirements(routine, context);
            startRoutine(routine);
            return true;
        }
        return false;
    }

    void unschedule(Routine routine) {
        freeRequirements(routine);
        stopRoutine(routine);
        for (Subsystem subsystem : routine.requirements) {
            runDefaultRoutine(subsystem);
        }
    }

    private boolean canRun(Routine routine, Set<Subsystem> context) {
        for (Subsystem requirement : routine.requirements) {
            if (!subsystemClaimsMap.containsKey(requirement)) {
                throw new IllegalArgumentException(
                        String.format("cannot require Subsystem '%s': not registered with this RoutineScheduler", requirement)
                );
            }
            // Less than zero in subsystemClaimsMap means not interruptable.
            if (!context.contains(requirement) && subsystemClaimsMap.get(requirement) < 0) {
                return false;
            }
        }
        return true;
    }

    private void claimRequirements(Routine routine, Set<Subsystem> context) {
        for (Subsystem requirement : routine.requirements) {
            if (!context.contains(requirement)) {
                forcefullyFree(requirement);
            }
            incClaimCounter(requirement, routine.isInterruptable());
        }
    }

    private void freeRequirements(Routine routine) {
        for (Subsystem requirement : routine.requirements) {
            decClaimCounter(requirement);
        }
    }

    private void startRoutine(Routine routine) {
        currentlyRunning.add(routine);
        looper.registerLoop(routine.loop);
    }

    private void stopRoutine(Routine routine) {
        currentlyRunning.remove(routine);
        looper.removeLoop(routine.loop);
    }

    private void incClaimCounter(Subsystem subsystem, boolean interruptable) {
        int prevVal = subsystemClaimsMap.get(subsystem);
        int newVal;
        if (prevVal == 0) {
            newVal = interruptable ? 1 : -1;
        } else if (prevVal < 0) {
            newVal = prevVal - 1;
        } else {  // if (prevVal > 0)
            newVal = prevVal + 1;
        }
        subsystemClaimsMap.replace(subsystem, newVal);
    }

    private void decClaimCounter(Subsystem subsystem) {
        int prevVal = subsystemClaimsMap.get(subsystem);
        int newVal;
        if (prevVal < 0) {
            newVal = prevVal + 1;
        } else {  // if (prevVal > 0)
            newVal = prevVal - 1;
        }
        subsystemClaimsMap.replace(subsystem, newVal);
    }

    private void forcefullyFree(Subsystem subsystem) {
        Routine routine;
        for (Iterator<Routine> iterator = currentlyRunning.iterator(); iterator.hasNext();) {
            routine = iterator.next();
            if (routine.requires(subsystem)) {
                freeRequirements(routine);
                stopRoutine(routine);
                iterator.remove();
            }
        }
    }

    private void runDefaultRoutine(Subsystem subsystem) {
        Routine defaultRoutine = subsystem.getDefaultRoutine();
        if (defaultRoutine == null) {
            return;
        }
        defaultRoutine.checkIfCanRun();
        if (!defaultRoutine.requires(subsystem)) {
            throw new RuntimeException(
                    String.format("the default Routine for Subsystem '%s' does not require that Subsystem", subsystem)
            );
        }
        if (defaultRoutine.requirements.size() > 1) {
            throw new RuntimeException(
                    String.format("the default Routine for Subsystem '%s' requires multiple Subsystems", subsystem)
            );
        }
        boolean interruptable = defaultRoutine.isInterruptable();
        if (!interruptable) {
            LOGGER.warning(
                    String.format("The default Routine for Subsystem '%s' is not interruptable.", subsystem)
            );
        }
        incClaimCounter(subsystem, interruptable);
        startRoutine(defaultRoutine);
    }
}
