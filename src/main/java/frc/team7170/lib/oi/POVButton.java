package frc.team7170.lib.oi;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Notifier;

import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * A {@link TriggerButton TriggerButton} that retrieves its value from single angle on a
 * {@link GenericHID#getPOV(int) POV} on a {@link GenericHID GenericHID}.
 * </p>
 * <p>
 * {@code POVButton}s constructed directly through the one of the constructors do not support the
 * {@link POVButton#getPressed() getPressed} and {@link POVButton#getReleased() getReleased} functionality; in order to
 * build {@code POVButton}s that support those operations, one must use one of the static factory methods
 * ({@link POVButton#newButtonsWithPoller(GenericHID, int)} or
 * {@link POVButton#newButtonsWithPoller(GenericHID, int, int)}), which construct a "complete set" (i.e. all eight
 * angles) of {@code POVButton}s.
 * </p>
 *
 * @apiNote The two different {@code POVButton} instantiation methods exists for performance reasons. A previous version
 * of {@code POVButton} had each individual button have its own {@link Notifier Notifier} running its own internal poll
 * loop to update the internal pressed and released flags necessary for the operation of the
 * {@link POVButton#getPressed() getPressed} and {@link POVButton#getReleased() getReleased} methods, but this caused
 * considerable performance issues depending on the poll period. Since only one angle on a POV can be active at a time
 * anyway, switching to a single {@code Notifier} loop to handle polling a complete set of {@code POVButton}s increases
 * performance by 8x. This is why the static factory methods construct all {@code POVButton}s at once.
 *
 * @author Robert Russell
 */
// TODO: impossible to give PolledPOVButtons a custom name
public class POVButton extends TriggerButton {

    /**
     * The eight possible angles that can be read from a {@linkplain GenericHID#getPOV(int) POV} on a
     * {@link GenericHID GenericHID}.
     *
     * @apiNote There are actually nine states a POV can be in; the one not mentioned here is "unpressed" because the
     * user need not reference it.
     *
     * @implNote The "unpressed" state is represented internally with {@code null}.
     *
     * @author Robert Russell
     */
    public enum POVAngle {
        // Names cannot start with numerals... prefix with "A" for "angle"?
        A0(0),
        A45(45),
        A90(90),
        A135(135),
        A180(180),
        A225(225),
        A270(270),
        A315(315);

        private final int angle;

        POVAngle(int angle) {
            this.angle = angle;
        }

        public int getIntegerAngle() {
            return angle;
        }
    }

    private final GenericHID hid;

    /**
     * Since some controllers can have multiple POVs, this "POV number" is used to differentiate between them.
     */
    private final int pov;

    private final POVAngle angle;

    /**
     * Construct a new {@code POVButton} with an explicit name.
     *
     * @param hid the {@link GenericHID GenericHID} to get the {@linkplain GenericHID#getPOV(int) POV angle} from.
     * @param pov the POV number.
     * @param angle the {@link POVAngle POVAngle}.
     * @param name the name of the {@code POVButton}.
     * @throws NullPointerException if the given name, {@code GenericHID}, or {@code POVAngle} is {@code null}.
     * @throws IllegalArgumentException if the given name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public POVButton(GenericHID hid, int pov, POVAngle angle, String name) {
        super(name);
        this.hid = Objects.requireNonNull(hid, "hid must be non-null");
        this.pov = pov;
        this.angle = Objects.requireNonNull(angle, "angle must be non-null");
    }

    /**
     * Construct a new {@code POVButton} with the name set to the angle.
     *
     * @param hid the {@link GenericHID GenericHID} to get the {@linkplain GenericHID#getPOV(int) POV angle} from.
     * @param pov the POV number.
     * @param angle the {@link POVAngle POVAngle}.
     * @throws NullPointerException if the given {@code GenericHID} or the given {@code POVAngle} is {@code null}.
     * @throws IllegalArgumentException if the derived name is not valid according to the global naming rules set out in
     * {@link frc.team7170.lib.Name Name}.
     */
    public POVButton(GenericHID hid, int pov, POVAngle angle) {
        // This null check is redundant, but I'd like to have the error message be consistent.
        this(hid, pov, angle, Objects.requireNonNull(angle, "angle must be non-null").name());
    }

    @Override
    public boolean get() {
        return hid.getPOV(pov) == angle.getIntegerAngle();
    }

    /**
     * Not supported for {@code POVButton}s constructed directly through the constructor. In order to enable this
     * functionality, use the static factory methods ({@link POVButton#newButtonsWithPoller(GenericHID, int)} or
     * {@link POVButton#newButtonsWithPoller(GenericHID, int, int)}) instead.
     *
     * @return N/A
     * @throws UnsupportedOperationException always.
     */
    @Override
    public boolean getPressed() {
        throw new UnsupportedOperationException("use POVButton.newButtonsWithPoller to enable this functionality");
    }

    /**
     * Not supported for {@code POVButton}s constructed directly through the constructor. In order to enable this
     * functionality, use the static factory methods ({@link POVButton#newButtonsWithPoller(GenericHID, int)} or
     * {@link POVButton#newButtonsWithPoller(GenericHID, int, int)}) instead.
     *
     * @return N/A
     * @throws UnsupportedOperationException always.
     */
    @Override
    public boolean getReleased() {
        throw new UnsupportedOperationException("use POVButton.newButtonsWithPoller to enable this functionality");
    }

    /**
     * Get the {@link GenericHID GenericHID} used by this {@code POVButton}.
     *
     * @return the {@link GenericHID GenericHID}.
     */
    public GenericHID getHid() {
        return hid;
    }

    /**
     * Get the POV number used by this {@code POVButton}
     *
     * @return the POV number.
     */
    public int getPOVNumber() {
        return pov;
    }

    /**
     * Get the {@link POVAngle POVAngle} used by this {@code POVButton}.
     *
     * @return the {@link POVAngle POVAngle}.
     */
    public POVAngle getPOVAngle() {
        return angle;
    }

    /**
     * <p>
     * An auxiliary class for repeatedly polling the {@linkplain GenericHID#getPOV(int) POV} on a
     * {@link GenericHID GenericHID} to update the {@link PolledPOVButton#pressed pressed} and
     * {@link PolledPOVButton#released released} flags on a complete set (i.e. all eight angles) of {@code POVButton}s.
     * This class enables the use of the {@link POVButton#getPressed() getPressed} and
     * {@link POVButton#getReleased() getReleased} functionality on {@code POVButton}s.
     * </p>
     * <p>
     * This class has a "set and forget" policy: after construction, instances cannot be interacted with. However, a
     * reference to each instance must be maintained in order to avoid garbage collection.
     * </p>
     *
     * @author Robert Russell
     */
    private static class POVButtonPoller {

        /**
         * The default poll period used when an explicit one is not provided
         * (in {@link POVButton#newButtonsWithPoller(GenericHID, int)}).
         */
        private static final int DEFAULT_POLL_PERIOD_MS = 10;

        /**
         * The {@link Notifier Notifier} used to repeatedly {@linkplain GenericHID#getPOV(int) poll the POV} and update
         * the {@link PolledPOVButton#pressed pressed} and {@link PolledPOVButton#released released} flags on the
         * appropriate {@link PolledPOVButton PolledPOVButton}.
         */
        private final Notifier notifier = new Notifier(this::poll);

        private final GenericHID hid;

        private final int pov;

        private final PolledPOVButton
                button0, button45, button90, button135,
                button180, button225, button270, button315;

        /**
         * Stores a reference to the {@link PolledPOVButton PolledPOVButton} that was pressed on the last iteration so
         * that it can be compared to the {@code PolledPOVButton} pressed on the current iteration and detect if a
         * change occurred.
         *
         * @implNote Only the notifier thread accesses this, so no synchronization needed.
         */
        private PolledPOVButton lastPressed = null;

        /**
         * Construct a new {@code POVButtonPoller}. This constructor performs no error checking except for on the poll
         * period and assumes that the {@link GenericHID GenericHID} for all given
         * {@link PolledPOVButton PolledPOVButton}s matches the given {@code GenericHID}.
         *
         * @param hid the {@link GenericHID GenericHID} to get the {@linkplain GenericHID#getPOV(int) POV angle} from.
         * @param pov the POV number.
         * @param button0 the {@code PolledPOVButton} at 0 degrees.
         * @param button45 the {@code PolledPOVButton} at 45 degrees.
         * @param button90 the {@code PolledPOVButton} at 90 degrees.
         * @param button135 the {@code PolledPOVButton} at 135 degrees.
         * @param button180 the {@code PolledPOVButton} at 180 degrees.
         * @param button225 the {@code PolledPOVButton} at 225 degrees.
         * @param button270 the {@code PolledPOVButton} at 270 degrees.
         * @param button315 the {@code PolledPOVButton} at 315 degrees.
         * @param pollPeriodMs the duration of the poll period for the POV in milliseconds.
         * @throws IllegalArgumentException if the given poll period is less than zero.
         */
        private POVButtonPoller(GenericHID hid,
                               int pov,
                               PolledPOVButton button0,
                               PolledPOVButton button45,
                               PolledPOVButton button90,
                               PolledPOVButton button135,
                               PolledPOVButton button180,
                               PolledPOVButton button225,
                               PolledPOVButton button270,
                               PolledPOVButton button315,
                               int pollPeriodMs) {
            if (pollPeriodMs < 0) {
                throw new IllegalArgumentException("negative poll period");
            }

            this.hid = hid;
            this.pov = pov;
            this.button0 = button0;
            this.button45 = button45;
            this.button90 = button90;
            this.button135 = button135;
            this.button180 = button180;
            this.button225 = button225;
            this.button270 = button270;
            this.button315 = button315;
            notifier.startPeriodic((double) pollPeriodMs / 1000.0);
        }

        /**
         * Run a single poll iteration. This is called by the {@link POVButtonPoller#notifier Notifier}.
         */
        private void poll() {
            // Read the POV and decode the integer angle into its corresponding button.
            PolledPOVButton pressedButton = angleToButton(hid.getPOV(pov));
            // Only act if a change has occurred.
            if (pressedButton != lastPressed) {
                // Tell the last-pressed button (if there is one) that it has been released.
                if (lastPressed != null) {
                    lastPressed.released();
                }
                // Tell the currently-pressed button (if there is one) that is has been pressed.
                if (pressedButton != null) {
                    pressedButton.pressed();
                }
                lastPressed = pressedButton;
            }
        }

        /**
         * TODO
         *
         * @param angle
         * @return
         */
        private PolledPOVButton angleToButton(int angle) {
            switch (angle) {
                case -1:
                    return null;
                case 0:
                    return button0;
                case 45:
                    return button45;
                case 90:
                    return button90;
                case 135:
                    return button135;
                case 180:
                    return button180;
                case 225:
                    return button225;
                case 270:
                    return button270;
                case 315:
                    return button315;
                default:
                    throw new AssertionError(String.format("illegal angle '%d' read from POV", angle));
            }
        }
    }

    /**
     * A {@code POVButton} that supports the use of {@link PolledPOVButton#getPressed() getPressed} and
     * {@link PolledPOVButton#getReleased() getReleased} using a {@link POVButtonPoller POVButtonPoller}. Instances of
     * this class are returned by the static factory methods on {@code POVButton}.
     *
     * @author Robert Russell
     */
    private static class PolledPOVButton extends POVButton {

        /**
         * A reference to the {@link POVButtonPoller POVButtonPoller} polling this {@code PolledPOVButton}. This is not
         * actually used anywhere, but it is important that every {@code PolledPOVButton} hold a strong reference to its
         * {@code POVButtonPoller} so that the {@code POVButtonPoller} is not garbage collected until all eight
         * {@code PolledPOVButton}s in a complete set are also garbage collected.
         */
        private POVButtonPoller poller;

        /**
         * Whether or not the POV angle got enabled since the last invocation of
         * {@link NTButton#getPressed() getPressed}.
         *
         * @implNote Access is synchronized on {@code this}.
         */
        private boolean pressed = false;

        /**
         * Whether or not the POV angle got disabled since the last invocation of
         * {@link NTButton#getReleased() getReleased}.
         *
         * @implNote Access is synchronized on {@code this}.
         */
        private boolean released = false;

        /**
         * @param hid the {@link GenericHID GenericHID} to get the {@linkplain GenericHID#getPOV(int) POV angle} from.
         * @param pov the POV number.
         * @param angle the {@link POVAngle POVAngle}.
         * @throws NullPointerException if the given {@code GenericHID} or the given {@code POVAngle} is {@code null}.
         * @throws IllegalArgumentException if the derived name is not valid according to the global naming rules set out in
         * {@link frc.team7170.lib.Name Name}.
         */
        private PolledPOVButton(GenericHID hid, int pov, POVAngle angle) {
            super(hid, pov, angle);
        }

        @Override
        public synchronized boolean getPressed() {
            if (pressed) {
                pressed = false;
                return true;
            }
            return false;
        }

        @Override
        public synchronized boolean getReleased() {
            if (released) {
                released = false;
                return true;
            }
            return false;
        }

        /**
         * Indicate to the {@code PolledPOVButton} that is has been pressed. Called by
         * {@link POVButtonPoller POVButtonPoller}s.
         */
        private synchronized void pressed() {
            pressed = true;
        }

        /**
         * Indicate to the {@code PolledPOVButton} that is has been released. Called by
         * {@link POVButtonPoller POVButtonPoller}s.
         */
        private synchronized void released() {
            released = true;
        }
    }

    /**
     * Get an immutable {@link Map Map} mapping all eight {@link POVAngle POVAngle}s to a respective instance of
     * {@code POVButton}. {@code POVButton}s returned here support the use of the
     * {@link PolledPOVButton#getPressed() getPressed} and {@link PolledPOVButton#getReleased() getReleased}
     * functionality.
     *
     * @implNote This actually returns instances of {@link PolledPOVButton PolledPOVButton}.
     *
     * @param hid the {@link GenericHID GenericHID} to get the {@linkplain GenericHID#getPOV(int) POV angle} from.
     * @param pov the POV number.
     * @param pollPeriodMs the duration of the poll period for the POV in milliseconds.
     * @return an immutable {@link Map Map} mapping all eight {@link POVAngle POVAngle}s to a respective instance of
     * {@code POVButton}.
     * @throws NullPointerException if the given {@code GenericHID} is {@code null}.
     * @throws IllegalArgumentException if the {@linkplain POVAngle#name() derived names} for each {@code POVButton} is
     * not valid according to the global naming rules set out in {@link frc.team7170.lib.Name Name}.
     * @throws IllegalArgumentException if the given poll period is less than zero.
     * @see PolledPOVButton
     * @see POVButtonPoller
     */
    public static Map<POVAngle, POVButton> newButtonsWithPoller(GenericHID hid, int pov, int pollPeriodMs) {
        PolledPOVButton b0 = new PolledPOVButton(hid, pov, POVAngle.A0);
        PolledPOVButton b45 = new PolledPOVButton(hid, pov, POVAngle.A45);
        PolledPOVButton b90 = new PolledPOVButton(hid, pov, POVAngle.A90);
        PolledPOVButton b135 = new PolledPOVButton(hid, pov, POVAngle.A135);
        PolledPOVButton b180 = new PolledPOVButton(hid, pov, POVAngle.A180);
        PolledPOVButton b225 = new PolledPOVButton(hid, pov, POVAngle.A225);
        PolledPOVButton b270 = new PolledPOVButton(hid, pov, POVAngle.A270);
        PolledPOVButton b315 = new PolledPOVButton(hid, pov, POVAngle.A315);
        POVButtonPoller poller = new POVButtonPoller(
                hid, pov, b0, b45, b90, b135, b180, b225, b270, b315, pollPeriodMs
        );
        b0.poller = poller;
        b45.poller = poller;
        b90.poller = poller;
        b135.poller = poller;
        b180.poller = poller;
        b225.poller = poller;
        b270.poller = poller;
        b315.poller = poller;
        return Map.of(
                POVAngle.A0,   b0,
                POVAngle.A45,  b45,
                POVAngle.A90,  b90,
                POVAngle.A135, b135,
                POVAngle.A180, b180,
                POVAngle.A225, b225,
                POVAngle.A270, b270,
                POVAngle.A315, b315
        );
    }

    /**
     * <p>
     * Get an immutable {@link Map Map} mapping all eight {@link POVAngle POVAngle}s to a respective instance of
     * {@code POVButton}. {@code POVButton}s returned here support the use of the
     * {@link PolledPOVButton#getPressed() getPressed} and {@link PolledPOVButton#getReleased() getReleased}
     * functionality.
     * </p>
     * <p>
     * This uses the default poll period ({@value POVButtonPoller#DEFAULT_POLL_PERIOD_MS}).
     * </p>
     *
     * @implNote This actually returns instances of {@link PolledPOVButton PolledPOVButton}.
     *
     * @param hid the {@link GenericHID GenericHID} to get the {@linkplain GenericHID#getPOV(int) POV angle} from.
     * @param pov the POV number.
     * @return an immutable {@link Map Map} mapping all eight {@link POVAngle POVAngle}s to a respective instance of
     * {@code POVButton}.
     * @throws NullPointerException if the given {@code GenericHID} is {@code null}.
     * @throws IllegalArgumentException if the {@linkplain POVAngle#name() derived names} for each {@code POVButton} is
     * not valid according to the global naming rules set out in {@link frc.team7170.lib.Name Name}.
     * @throws IllegalArgumentException if the given poll period is less than zero.
     * @see PolledPOVButton
     * @see POVButtonPoller
     */
    public static Map<POVAngle, POVButton> newButtonsWithPoller(GenericHID hid, int pov) {
        return newButtonsWithPoller(hid, pov, POVButtonPoller.DEFAULT_POLL_PERIOD_MS);
    }
}
