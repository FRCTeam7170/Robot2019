package frc.team7170.lib.logging;

import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import frc.team7170.lib.networktables.command.CommandAbortException;
import frc.team7170.lib.networktables.command.Commander;
import frc.team7170.lib.util.Pair;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataLoggerManager {

    private static final Logger LOGGER = LoggerManager.INSTANCE.getLogger(DataLoggerManager.class.getName());

    private static final String FILE_NAME_FMT = "%d.msgpack";  // "N.msgpack"
    private static final int MIN_PERIOD_MS = 10;
    private static final String DATA_LOGGER_PREFIX_NAME_SEP = "_";

    private final Commander dataLogCommander = new Commander(NetworkTableInstance.getDefault(), "dataLog");
    private final MessagePacker packer;
    private final DataCollectorThread thread = new DataCollectorThread();
    private volatile HashMap<DataLogger, Pair<String[], String>> dataLoggers = new HashMap<>();
    private volatile int periodMs = 20;

    public static final DataLoggerManager INSTANCE = new DataLoggerManager();

    private DataLoggerManager() {
        dataLogCommander.registerCommand("setPeriodMs", stringArguments -> {
            stringArguments.assertArgsLength(1);
            int newPeriodMs = stringArguments.asIterator().nextInt();
            if (newPeriodMs < MIN_PERIOD_MS) {
                throw new CommandAbortException(String.format("given periodMs (%d) below minimum (%d)",
                        newPeriodMs, MIN_PERIOD_MS));
            }
            periodMs = newPeriodMs;
        });

        String filename = String.format(FILE_NAME_FMT, LoggerManager.LOG_NUM);
        File file = new File(LoggerManager.LOG_DIR, filename);
        MessagePacker p = null;
        try {
            p = MessagePack.newDefaultPacker(new FileOutputStream(file));
            thread.start();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "failed to initialize data logger output stream; " +
                    "blocking data collector thread startup", e);
        }
        packer = p;
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
                            LOGGER.log(Level.SEVERE, "given more headers than data values; ignoring remaining headers", e);
                        }
                    }
                    packer.flush();
                    Thread.sleep(periodMs);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "failed to pack data log", e);
            } finally {
                try {
                    packer.close();
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "failed to gracefully close packer", e);
                }
            }
        }
    }
}
