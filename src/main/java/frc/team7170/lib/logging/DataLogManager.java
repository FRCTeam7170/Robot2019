package frc.team7170.lib.logging;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import frc.team7170.lib.Name;
import frc.team7170.lib.networktables.command.CommandAbortException;
import frc.team7170.lib.networktables.command.Commander;
import frc.team7170.lib.util.Pair;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.core.MessageTypeException;
import org.msgpack.core.MessageUnpacker;
import org.msgpack.value.Value;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class DataLogManager {

    private static final Logger LOGGER = Logger.getLogger(DataLogManager.class.getName());

    private static final File LOG_DIR = new File(System.getProperty("user.home"), "dlogs");
    private static final File NUM_FILE = new File(LOG_DIR, "nextN.msgpack");
    private static final String FILE_NAME_FMT = "%d.msgpack";  // "N.msgpack"
    private static final int MIN_PERIOD_MS = 10;
    private static final String DATA_LOGGER_PREFIX_NAME_SEP = "_";
    private static final int LOG_NUM = loadLogFileNum();
    static {
        saveLogFileNum(LOG_NUM + 1);
    }

    private final Commander dataLogCommander = new Commander(NetworkTableInstance.getDefault(), new Name("dataLog"));
    private final MessagePacker packer;
    private final DataCollectorThread thread = new DataCollectorThread();
    private volatile HashMap<DataLogger, Pair<String[], String>> dataLoggers = new HashMap<>();
    private volatile int periodMs = 20;

    private DataLogManager() {
        LOGGER.info("Data logging with log num of " + LOG_NUM);

        dataLogCommander.registerCommand(new Name("setPeriodMs"), stringArguments -> {
            stringArguments.assertArgsLength(1);
            int newPeriodMs = stringArguments.asIterator().nextInt();
            if (newPeriodMs < MIN_PERIOD_MS) {
                throw new CommandAbortException(String.format("given periodMs (%d) below minimum (%d)",
                        newPeriodMs, MIN_PERIOD_MS));
            }
            periodMs = newPeriodMs;
        });

        String filename = String.format(FILE_NAME_FMT, LOG_NUM);
        File file = new File(LOG_DIR, filename);
        MessagePacker p = null;
        try {
            p = MessagePack.newDefaultPacker(new FileOutputStream(file));
            thread.start();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize data logger output stream; " +
                    "blocking data collector thread startup.", e);
        }
        packer = p;
    }

    private static final DataLogManager INSTANCE = new DataLogManager();

    public static DataLogManager getInstance() {
        return INSTANCE;
    }

    public boolean isCollectingData() {
        return thread.isAlive();
    }

    public boolean registerDataLogger(DataLogger dataLogger, String namePrefix) {
        return dataLoggers.put(dataLogger, new Pair<>(dataLogger.reportHeaders(),
                resolveDataLoggerName(namePrefix, dataLogger.reportName()))) == null;
    }

    public boolean unregisterDataLogger(DataLogger dataLogger) {
        return dataLoggers.remove(dataLogger) != null;
    }

    private String resolveDataLoggerName(String prefix, String dataLoggerName) {
        assertValidName(dataLoggerName);
        if (prefix == null) {
            return dataLoggerName;
        }
        assertValidName(prefix);
        return prefix + DATA_LOGGER_PREFIX_NAME_SEP + dataLoggerName;
    }

    private void assertValidName(String name) {
        if (name.matches("[^a-zA-Z0-9_]")) {
            throw new RuntimeException("invalid data logger name");
        }
    }

    private class DataCollectorThread extends Thread {

        private DataCollectorThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            try {
                while (true) {
                    packer.packDouble(Timer.getFPGATimestamp());
                    packer.packMapHeader(dataLoggers.size());
                    Iterator<Map.Entry<DataLogger, Pair<String[], String>>> iterator = dataLoggers.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<DataLogger, Pair<String[], String>> entry = iterator.next();
                        DataLogger dataLogger = entry.getKey();
                        Pair<String[], String> pair = entry.getValue();
                        packer.packString(pair.getRight());  // pack data logger name
                        String[] keys = pair.getLeft();
                        Value[] values = dataLogger.reportData();
                        int numDataPoints = keys.length;
                        packer.packMapHeader(numDataPoints);
                        try {
                            for (int i = 0; i < numDataPoints; ++i) {
                                packer.packString(keys[i]);
                                packer.packValue(values[i]);
                            }
                        } catch (IndexOutOfBoundsException e) {
                            LOGGER.log(Level.WARNING, "Given more headers than data values; ignoring remaining headers.", e);
                        }
                    }
                    packer.flush();
                    Thread.sleep(periodMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Failed to pack data log.", e);
            } finally {
                try {
                    packer.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Failed to gracefully close packer.", e);
                }
            }
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
