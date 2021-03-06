package frc.team7170.robot2019.subsystems;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import frc.team7170.lib.WrappingDouble;
import frc.team7170.lib.multiplex.AnalogMultiplexer;
import frc.team7170.lib.wrappers.WrapperFactory;
import frc.team7170.robot2019.Constants;
import frc.team7170.robot2019.commands.CmdEndEffectorTeleop;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class EndEffector extends Subsystem {

    // TODO: make this generic (e.g. implement lib.wrappers.Servo and generalize for all (continuous) servos) and move into spooky-lib
    public static class LateralSlide {

        private class PollerThread extends Thread {

            private PollerThread() {
                super("LateralSlide.PollerThread");
                setDaemon(true);
            }

            @Override
            public void run() {
                while (true) {
                    pollFeedback();
                    try {
                        Thread.sleep(Constants.EndEffector.SERVO_FEEDBACK_POLL_PERIOD_MS);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }

        private final PWM pwm = new PWM(Constants.PWM.LATERAL_SLIDE_SERVO);
        private final AnalogInput feedback = new AnalogInput(Constants.AIN.LATERAL_SLIDE_SERVO_FEEDBACK);
        private final WrappingDouble wrappingDouble;
        // public final WrappingDouble.WrappingDoubleCharacterizer wdc;
        // private final Notifier feedbackNotifier = new Notifier(this::pollFeedback);
        private final DigitalInput limitSwitch = new DigitalInput(Constants.DIO.LATERAL_SLIDE_LIMIT_SWITCH);
        private final Thread pollerThread = new PollerThread();

        public LateralSlide() {
            pwm.setBounds((double) Constants.EndEffector.SERVO_MAX_US / 1000.0,
                    (double) Constants.EndEffector.SERVO_DEADBAND_MAX_US / 1000.0,
                    (double) Constants.EndEffector.SERVO_CENTRE_US / 1000.0,
                    (double) Constants.EndEffector.SERVO_DEADBAND_MIN_US / 1000.0,
                    (double) Constants.EndEffector.SERVO_MIN_US / 1000.0);
            pwm.setSpeed(0.0);

            feedback.setOversampleBits(0);
            feedback.setAverageBits(Constants.EndEffector.SERVO_FEEDBACK_AVERAGE_BITS);

            wrappingDouble = new WrappingDouble(
                    Constants.EndEffector.SERVO_FEEDBACK_POTENTIAL_RANGE_mV,
                    Constants.EndEffector.SERVO_FEEDBACK_NEAR_EXTREME_THRESHOLD,
                    Constants.EndEffector.SERVO_FEEDBACK_MAX_NOISE_DEVIATION,
                    Constants.EndEffector.SERVO_FEEDBACK_STD_DEVIATION,
                    Constants.EndEffector.SERVO_FEEDBACK_MEAN,
                    feedback.getAverageVoltage()
            );
            // wdc = new WrappingDouble.WrappingDoubleCharacterizer(feedback.getAverageVoltage());

            // feedbackNotifier.startPeriodic((double) Constants.EndEffector.SERVO_FEEDBACK_POLL_PERIOD_MS / 1000.0);
            pollerThread.start();
        }

        private static final LateralSlide INSTANCE = new LateralSlide();

        public static LateralSlide getInstance() {
            return INSTANCE;
        }

        public void set(double value) {
            pwm.setSpeed(Constants.EndEffector.INVERT_SERVO ? -value : value);
        }

        public void kill() {
            set(0.0);
        }

        public double getSpeed() {
            return pwm.getSpeed();
        }

        public synchronized double getFeedback() {
            return Constants.EndEffector.SERVO_FEEDBACK_MULTIPLIER * getFeedbackRaw();
        }

        public synchronized double getFeedbackRaw() {
            return wrappingDouble.get();
        }

        public synchronized double getFeedbackVoltage() {
            return feedback.getAverageVoltage() * 1000.0;
        }

        public synchronized void resetFeedback() {
            wrappingDouble.reset();
        }

        public boolean isLimitSwitchTriggered() {
            return limitSwitch.get();
        }

        private synchronized void pollFeedback() {
            double value = getFeedbackVoltage() - Constants.EndEffector.SERVO_FEEDBACK_MIN_POTENTIAL_mV;
            if (Constants.EndEffector.INVERT_SERVO_FEEDBACK) {
                value = Constants.EndEffector.SERVO_FEEDBACK_POTENTIAL_RANGE_mV - value;
            }
            wrappingDouble.feed(value);
            // wdc.feed(value);
        }

        /*
        @Override
        public void pidWrite(double output) {
            set(output);
        }

        @Override
        public void setPIDSourceType(PIDSourceType pidSource) {
            if (pidSource == PIDSourceType.kRate) {
                throw new IllegalArgumentException("PIDSourceType.kRate not supported");
            }
        }

        @Override
        public PIDSourceType getPIDSourceType() {
            return PIDSourceType.kDisplacement;
        }

        @Override
        public double pidGet() {
            return getFeedback();
        }
        */
    }

    public static class ReflectanceSensorArray {

        public static class LineDeviation {

            private final double deviation;  // m
            private final boolean isError;

            private LineDeviation(double deviation, boolean isError) {
                this.deviation = deviation;
                this.isError = isError;
            }

            public double getValue() {
                if (isError) {
                    throw new IllegalStateException("error occurred while calculating deviation; deviation value not available");
                }
                return deviation;
            }

            public boolean isError() {
                return isError;
            }

            @Override
            public String toString() {
                return isError ? "ERROR" : String.format("%.03f", deviation);
            }
        }

        private static final Logger LOGGER = Logger.getLogger(ReflectanceSensorArray.class.getName());

        private final AnalogMultiplexer mux0 = new AnalogMultiplexer(
                WrapperFactory.wrapWPIAnalogInput(new AnalogInput(Constants.AIN.REFLECTANCE_SENSOR_ARRAY_0)),
                WrapperFactory.wrapWPIDigitalOutput(new DigitalOutput(Constants.DIO.REFLECTANCE_SENSOR_ARRAY_SELECT_1)),
                WrapperFactory.wrapWPIDigitalOutput(new DigitalOutput(Constants.DIO.REFLECTANCE_SENSOR_ARRAY_SELECT_0))
        );

        private final AnalogMultiplexer mux1 = new AnalogMultiplexer(
                WrapperFactory.wrapWPIAnalogInput(new AnalogInput(Constants.AIN.REFLECTANCE_SENSOR_ARRAY_1)), mux0
        );

        private final AnalogMultiplexer mux2 = new AnalogMultiplexer(
                WrapperFactory.wrapWPIAnalogInput(new AnalogInput(Constants.AIN.REFLECTANCE_SENSOR_ARRAY_2)), mux0
        );

        // sensor11 disabled because the traces for it were broken on the PCB.
        private final frc.team7170.lib.wrappers.AnalogInput sensor0 = mux0.getAnalogInputFor(0);
        private final frc.team7170.lib.wrappers.AnalogInput sensor1 = mux0.getAnalogInputFor(2);
        private final frc.team7170.lib.wrappers.AnalogInput sensor2 = mux0.getAnalogInputFor(1);
        private final frc.team7170.lib.wrappers.AnalogInput sensor3 = mux0.getAnalogInputFor(3);
        private final frc.team7170.lib.wrappers.AnalogInput sensor4 = mux1.getAnalogInputFor(0);
        private final frc.team7170.lib.wrappers.AnalogInput sensor5 = mux1.getAnalogInputFor(2);
        private final frc.team7170.lib.wrappers.AnalogInput sensor6 = mux1.getAnalogInputFor(1);
        private final frc.team7170.lib.wrappers.AnalogInput sensor7 = mux1.getAnalogInputFor(3);
        private final frc.team7170.lib.wrappers.AnalogInput sensor8 = mux2.getAnalogInputFor(0);
        private final frc.team7170.lib.wrappers.AnalogInput sensor9 = mux2.getAnalogInputFor(2);
        private final frc.team7170.lib.wrappers.AnalogInput sensor10 = mux2.getAnalogInputFor(1);
        // private final frc.team7170.lib.wrappers.AnalogInput sensor11 = mux2.getAnalogInputFor(3);
        private final frc.team7170.lib.wrappers.AnalogInput[] sensors = {
                sensor0, sensor1, sensor2, sensor3,
                sensor4, sensor5, sensor6, sensor7,
                sensor8, sensor9, sensor10, // sensor11
        };

        // Enforce non-instantiability.
        private ReflectanceSensorArray() {}

        private static final ReflectanceSensorArray INSTANCE = new ReflectanceSensorArray();

        public static ReflectanceSensorArray getInstance() {
            return INSTANCE;
        }

        public LineDeviation getDeviationFromLine() {
            // double avg = averageVoltage();
            boolean onStreak = false;
            List<Integer> triggeredSensorIndices = new ArrayList<>(Constants.ReflectanceSensorArray.MAX_TRIGGERED_SENSORS);
            for (int i = 0; i < sensors.length; ++i) {
                // if (!CalcUtil.inThreshold(sensors[i].getVoltage(), avg,
                //         Constants.ReflectanceSensorArray.MIN_DEVIATION_FROM_AVG_VOLTS)) {
                if (sensors[i].getVoltage() < Constants.ReflectanceSensorArray.SENSOR_TRIGGER_THRESHOLD) {
                    if (!onStreak) {
                        if (triggeredSensorIndices.size() != 0) {
                            LOGGER.fine("Sensed more than one reflective object.");
                            return new LineDeviation(0.0, true);
                        }
                        onStreak = true;
                    }
                    if (triggeredSensorIndices.size() == Constants.ReflectanceSensorArray.MAX_TRIGGERED_SENSORS) {
                        LOGGER.fine(String.format(
                                "Sensed something reflective, but more than MAX_TRIGGERED_SENSORS (%d) were triggered.",
                                Constants.ReflectanceSensorArray.MAX_TRIGGERED_SENSORS
                        ));
                        return new LineDeviation(0.0, true);
                    }
                    triggeredSensorIndices.add(i);
                } else if (onStreak) {
                    // The zero-check on the first element is for in the case of when half the tape might to out-of-view
                    // to the left.
                    if (triggeredSensorIndices.size() < Constants.ReflectanceSensorArray.MIN_TRIGGERED_SENSORS &&
                            triggeredSensorIndices.get(0) != 0) {
                        LOGGER.fine(String.format(
                                "Sensed something reflective, but fewer than MIN_TRIGGERED_SENSORS (%d) were triggered.",
                                Constants.ReflectanceSensorArray.MIN_TRIGGERED_SENSORS
                        ));
                        return new LineDeviation(0.0, true);
                    }
                    onStreak = false;
                }
            }
            if (triggeredSensorIndices.isEmpty()) {
                LOGGER.fine("No reflectance sensors were triggered.");
                return new LineDeviation(0.0, true);
            }
            /*
             * Let x = deviation from centre of array (desired measure),
             *     w = width of a single sensor (in m),
             *     r = sensor centre to centre spacing (in m),
             *     n = number of detected sensors,
             *     i = index of first sensor in streak (first sensor in array is 0),
             *     l = length of array (in m)
             *
             * With some basic reasoning, we can determine that
             *     x = {distance from the left to beginning of the streak} +
             *         {half the width of the streak} -
             *         {half the sensor array length}
             *       = (w/2 + i*r) +
             *         ((n-1) * r / 2) -
             *         (l/2)
             *       = w/2 + r * (i + (n-1)/2) - l/2
             */
            // Renaming for sake of above formula:
            double w = Constants.ReflectanceSensorArray.SENSOR_WIDTH_M;
            double r = Constants.ReflectanceSensorArray.SENSOR_SPACING_M;
            double n = triggeredSensorIndices.size();
            double i = triggeredSensorIndices.get(0);
            double l = Constants.ReflectanceSensorArray.ARRAY_LENGTH_M;
            return new LineDeviation(w/2 + r * (i + (n-1)/2) - l/2, false);
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (frc.team7170.lib.wrappers.AnalogInput sensor : sensors) {
                sb.append(sensor.getVoltage() < Constants.ReflectanceSensorArray.SENSOR_TRIGGER_THRESHOLD ? "1" : "0");
            }
            sb.append("->");
            sb.append(EndEffector.ReflectanceSensorArray.getInstance().getDeviationFromLine());
            return sb.toString();
        }

        /*
        private double averageVoltage() {
            double average = 0.0;
            for (frc.team7170.lib.wrappers.AnalogInput sensor : sensors) {
                average += sensor.getVoltage();
            }
            return average / sensors.length;
        }
        */
    }

    private final Solenoid ejectDeploySolenoid = new Solenoid(Constants.CAN.PCM, Constants.PCM.EJECT_DEPLOY_SOLENOID);
    private final Solenoid ejectRetractSolenoid = new Solenoid(Constants.CAN.PCM, Constants.PCM.EJECT_RETRACT_SOLENOID);
    private final Solenoid pinDeploySolenoid = new Solenoid(Constants.CAN.PCM, Constants.PCM.PIN_DEPLOY_SOLENOID);
    private final Solenoid pinRetractSolenoid = new Solenoid(Constants.CAN.PCM, Constants.PCM.PIN_RETRACT_SOLENOID);
    private final Notifier ejectNotifier = new Notifier(this::ejectFinished);
    private final Notifier pinNotifer = new Notifier(this::pinFinished);

    private boolean pinDeployed = false;
    private boolean ejecting = false;
    private boolean togglingPin = false;

    private final NetworkTableEntry pinStateEntry;

    private EndEffector() {
        super("endEffector");

        pinStateEntry = Shuffleboard.getTab(Constants.Shuffleboard.MAIN_TAB).add("pinState", false).getEntry();

        ejectDeploySolenoid.setPulseDuration(Constants.EndEffector.EJECT_SOLENOID_PULSE_DURATION_SECONDS);
        ejectRetractSolenoid.setPulseDuration(Constants.EndEffector.EJECT_SOLENOID_PULSE_DURATION_SECONDS);
        pinDeploySolenoid.setPulseDuration(Constants.EndEffector.PIN_SOLENOID_PULSE_DURATION_SECONDS);
        pinRetractSolenoid.setPulseDuration(Constants.EndEffector.PIN_SOLENOID_PULSE_DURATION_SECONDS);

        ejectDeploySolenoid.set(false);
        ejectRetractSolenoid.set(false);
        pinDeploySolenoid.set(false);
        retractPin();
    }

    private static final EndEffector INSTANCE = new EndEffector();

    public static EndEffector getInstance() {
        return INSTANCE;
    }

    public boolean eject() {
//        if (!ejecting) {
//            ejecting = true;
//            ejectDeploySolenoid.startPulse();
//            ejectNotifier.startSingle(Constants.EndEffector.EJECT_PULSE_DURATION_SECONDS);
//            return true;
//        }
//        return false;
        // TODO: temp, Joel's thingy
        if (ejecting) {
            ejectRetractSolenoid.startPulse();
            ejecting = false;
        } else {
            ejectDeploySolenoid.startPulse();
            ejecting = true;
        }
        return true;
    }

    private void ejectFinished() {
        ejecting = false;
        ejectRetractSolenoid.startPulse();
    }

    public boolean deployPin() {
        if (!togglingPin) {
            togglingPin = true;
            pinDeployed = true;
            pinDeploySolenoid.startPulse();
            pinNotifer.startSingle(Constants.EndEffector.PIN_SOLENOID_PULSE_DURATION_SECONDS);
            pinStateEntry.setBoolean(true);
            return true;
        }
        return false;
    }

    public boolean retractPin() {
        if (!togglingPin) {
            togglingPin = true;
            pinDeployed = false;
            pinRetractSolenoid.startPulse();
            pinNotifer.startSingle(Constants.EndEffector.PIN_SOLENOID_PULSE_DURATION_SECONDS);
            pinStateEntry.setBoolean(false);
            return true;
        }
        return false;
    }

    public boolean togglePin() {
        if (pinDeployed) {
            return retractPin();
        }
        return deployPin();
    }

    private void pinFinished() {
        togglingPin = false;
    }

    public boolean isPinDeployed() {
        return pinDeployed;
    }

    @Override
    protected void initDefaultCommand() {
        setDefaultCommand(new CmdEndEffectorTeleop());
    }
}
