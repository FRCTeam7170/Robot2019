package frc.team7170.lib.logging;

import edu.wpi.first.networktables.NetworkTableInstance;
import frc.team7170.lib.Name;
import frc.team7170.lib.networktables.command.ArgumentativeCommand;
import frc.team7170.lib.networktables.command.CommandAbortException;
import frc.team7170.lib.networktables.command.Commander;

import java.util.logging.*;

public class LoggerManager {

    public static final Logger GLOBAL_LOGGER = Logger.getLogger("");
    private static final Logger LOGGER = Logger.getLogger(LoggerManager.class.getName());

    private static final Commander logCommander = new Commander(NetworkTableInstance.getDefault(), new Name("log"));
    private static FileHandler fileHandler;
    private static ConsoleHandler consoleHandler;

    static {
        LOGGER.info("Initializing LoggerManager...");

        for (Handler handler : GLOBAL_LOGGER.getHandlers()) {
            if (handler instanceof FileHandler) {
                fileHandler = (FileHandler) handler;
            } else if (handler instanceof ConsoleHandler) {
                consoleHandler = (ConsoleHandler) handler;
            }
        }
        if (fileHandler == null) {
            LOGGER.warning("FileHandler not detected.");
        }
        if (consoleHandler == null) {
            LOGGER.warning("ConsoleHandler not detected.");
        }

        logCommander.registerCommand(new Name("setLevel"), new ArgumentativeCommand(parsedArguments -> {
                    parsedArguments.rest.assertArgsLength(1);
                    String levelStr = parsedArguments.rest.asIterator().nextString().toLowerCase();
                    Level level = strToLevel(levelStr);
                    if (level == null) {
                        throw new CommandAbortException("invalid logger level");
                    }
                    if (parsedArguments.argMap.get("console") != null) {
                        if (consoleHandler != null) {
                            consoleHandler.setLevel(level);
                        } else {
                            LOGGER.warning("Cannot set level on console handler: console handler not detected.");
                        }
                        consoleHandler.setLevel(level);
                    } else if (parsedArguments.argMap.get("file") != null) {
                        if (fileHandler != null) {
                            fileHandler.setLevel(level);
                        } else {
                            LOGGER.warning("Cannot set level on file handler: file handler not detected.");
                        }
                    } else {
                        setLevel(level);
                    }
                },
                        new ArgumentativeCommand.Argument(new Name("console"), new Name("c"), 0),
                        new ArgumentativeCommand.Argument(new Name("file"), new Name("f"), 0))
        );

        LOGGER.info("Finished initializing LoggerManager.");
    }

    // Enforce non-instantiability.
    private LoggerManager() {}

    public static void setLevel(Level level) {
        GLOBAL_LOGGER.setLevel(level);
    }

    private static Level strToLevel(String levelStr) {
        switch (levelStr) {
            case "all":
                return Level.ALL;
            case "finest":
                return Level.FINEST;
            case "finer":
                return Level.FINER;
            case "fine":
                return Level.FINE;
            case "config":
                return Level.CONFIG;
            case "info":
                return Level.INFO;
            case "warning":
                return Level.WARNING;
            case "severe":
                return Level.SEVERE;
            case "off":
                return Level.OFF;
            default:
                return null;
        }
    }
}
