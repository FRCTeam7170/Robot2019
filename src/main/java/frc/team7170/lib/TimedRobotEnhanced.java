package frc.team7170.lib;

import edu.wpi.first.wpilibj.TimedRobot;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A {@link TimedRobot TimedRobot} version that automatically closes all resources registered with the
 * {@link ResourceManager ResourceManager} and logs any error that might occur during robot operation with
 * {@linkplain java.util.logging Java util logging}; otherwise exactly the same as TimedRobot.
 *
 * @author Robert Russell
 * @see TimedRobot
 */
public class TimedRobotEnhanced extends TimedRobot {

    private static final Logger LOGGER = Logger.getLogger(TimedRobotEnhanced.class.getName());

    @Override
    public void startCompetition() {
        try {
            super.startCompetition();
        } catch (Throwable t) {
            LOGGER.log(Level.SEVERE, "Uh-oh! Spaghetti-O's!", t);
            throw t;
        } finally {
            ResourceManager.getInstance().close();
        }
    }
}
