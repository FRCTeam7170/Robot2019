package frc.team7170.lib;

import edu.wpi.first.wpilibj.PWM;

public class ParallaxFeedback360Servo {

    private static final int SERVO_MAX_US = 1720;
    private static final int SERVO_DEADBAND_MAX_US = 1520;
    private static final int SERVO_CENTRE_US = 1500;
    private static final int SERVO_DEADBAND_MIN_US = 1480;
    private static final int SERVO_MIN_US = 1280;

    private final PWM pwm;

    public ParallaxFeedback360Servo(int pwmPin, int feedbackPin) {
        pwm = new PWM(pwmPin);
        pwm.setBounds((double) SERVO_MAX_US / 1000.0,
                (double) SERVO_DEADBAND_MAX_US / 1000.0,
                (double) SERVO_CENTRE_US / 1000.0,
                (double) SERVO_DEADBAND_MIN_US / 1000.0,
                (double) SERVO_MIN_US / 1000.0);
    }
}
