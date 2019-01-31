package frc.team7170.lib.logging;

import edu.wpi.first.networktables.NetworkTableInstance;
import frc.team7170.lib.networktables.command.ArgumentativeCommand;
import frc.team7170.lib.networktables.command.CommandAbortException;
import frc.team7170.lib.networktables.command.Commander;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageTypeException;
import org.msgpack.core.MessageUnpacker;

import java.io.*;
import java.util.logging.*;

public class LoggerManager {

    public static final Logger GLOBAL_LOGGER = Logger.getLogger("");
    private static final Logger LOGGER = LoggerManager.INSTANCE.getLogger(LoggerManager.class.getName());

    static final File LOG_DIR = new File("/home/lvuser/logs");
    private static final String FILE_NAME_FMT = "%d.log";  // "N.log"
    private static final File NUM_FILE = new File(LOG_DIR, "nextN.msgpack");

    static final int LOG_NUM = loadLogFileNum();
    static {
        LOGGER.info("logging with log num of " + LOG_NUM);
        saveLogFileNum(LOG_NUM + 1);
    }

    private final Commander logCommander = new Commander(NetworkTableInstance.getDefault(), "log");
    private ConsoleHandler consoleHandler;
    private FileHandler fileHandler;

    public static final LoggerManager INSTANCE = new LoggerManager();

    private LoggerManager() {
        // Setup console handler
        consoleHandler = new ConsoleHandler();
        GLOBAL_LOGGER.addHandler(consoleHandler);

        // (Try to) setup file handler
        // TODO: Create way to try to enable file handler dynamically via dashboard?
        String fileName = String.format(FILE_NAME_FMT, LOG_NUM);
        File file = new File(LOG_DIR, fileName);
        try {
            fileHandler = new FileHandler(file.getAbsolutePath());
            GLOBAL_LOGGER.addHandler(fileHandler);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "failed to enable file handler", e);
        }

        logCommander.registerCommand("setLevel", new ArgumentativeCommand(parsedArguments -> {
                    parsedArguments.rest.assertArgsLength(1);
                    String levelStr = parsedArguments.rest.asIterator().nextString().toLowerCase();
                    Level level = strToLevel(levelStr);
                    if (level == null) {
                        throw new CommandAbortException("invalid logger level");
                    }
                    if (parsedArguments.argMap.get("console") != null) {
                        consoleHandler.setLevel(level);
                    } else if (parsedArguments.argMap.get("file") != null) {
                        if (fileHandler != null) {
                            fileHandler.setLevel(level);
                        } else {
                            LOGGER.warning("cannot set level on file handler: file handler setup failed");
                        }
                    } else {
                        GLOBAL_LOGGER.setLevel(level);
                    }
                },
                        new ArgumentativeCommand.Argument("console", "c", 0),
                        new ArgumentativeCommand.Argument("file", "f", 0))
        );
    }

    public Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setParent(GLOBAL_LOGGER);
        logger.setLevel(null);  // Inherit level from parent.
        logger.setUseParentHandlers(true);  // Inherit handlers from parent.
        return logger;
    }

    public void setLevel(Level level) {
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

    private static int loadLogFileNum() {
        if (!NUM_FILE.exists()) {
            LOGGER.info("log num file does not exist; defaulting to zero");
            return 0;
        }
        MessageUnpacker unpacker;
        try {
            unpacker = MessagePack.newDefaultUnpacker(new FileInputStream(NUM_FILE));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "failed to read log num file; defaulting to zero", e);
            return 0;
        }
        try {
            int ret = unpacker.unpackInt();
            unpacker.close();
            return ret;
        } catch (IOException | MessageTypeException e) {
            LOGGER.log(Level.SEVERE, "error occurred while unpacking log num; defaulting to zero", e);
            return 0;
        }
    }

    private static void saveLogFileNum(int num) {
        MessagePacker packer;
        try {
            packer = MessagePack.newDefaultPacker(new FileOutputStream(NUM_FILE));
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "failed to save log num to file", e);
            return;
        }
        try {
            packer.packInt(num);
            packer.close();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "failed to pack log num to file", e);
        }
    }
}
