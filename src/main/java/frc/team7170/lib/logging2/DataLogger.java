package frc.team7170.lib.logging2;

import edu.wpi.first.wpilibj.Timer;

import java.util.*;

public class DataLogger {

    private static final Map<Loggable, DataLogger> loggers = new HashMap<>();

    private final Loggable loggable;
    private final List<DataHandler> handlers = new ArrayList<>(1);

    private DataLogger(Loggable loggable) {
        this.loggable = loggable;
        addHandler(MessagePackDataHandler.getDefault());
    }

    public void logValue(String name, Value value) {
        logValues(Map.of(name, value));
    }

    public void logValues(Map<String, Value> values) {
        double time = Timer.getFPGATimestamp();
        for (DataHandler handler : handlers) {
            handler.handle(time, loggable.getName(), values);
        }
    }

    public void addHandler(DataHandler handler) {
        handlers.add(Objects.requireNonNull(handler));
    }

    public boolean removeHandler(DataHandler handler) {
        return handlers.remove(handler);
    }

    public List<DataHandler> getHandlers() {
        return List.copyOf(handlers);
    }

    private boolean containsAll(List<String> tags) {
        return loggable.getTags().containsAll(tags);
    }

    private boolean containsAny(List<String> tags) {
        List<String> loggableTags = loggable.getTags();
        for (String tag : tags) {
            if (loggableTags.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    public static DataLogger registerDataLogger(Loggable loggable) {
        DataLogger dataLogger = loggers.get(Objects.requireNonNull(loggable));
        if (dataLogger != null) {
            return dataLogger;
        }
        dataLogger = new DataLogger(loggable);
        loggers.put(loggable, dataLogger);
        return dataLogger;
    }

    public static void addHandlerByTags(DataHandler handler, boolean all, String... tags) {
        List<String> tagsList = List.of(tags);
        for (DataLogger logger : loggers.values()) {
            if (all ? logger.containsAll(tagsList) : logger.containsAny(tagsList)) {
                logger.addHandler(handler);
            }
        }
    }

    public static void addHandlerByTags(DataHandler handler, String... tags) {
        addHandlerByTags(handler, false, tags);
    }

    public static void removeHandlerByTags(DataHandler handler, boolean all, String... tags) {
        List<String> tagsList = List.of(tags);
        for (DataLogger logger : loggers.values()) {
            if (all ? logger.containsAll(tagsList) : logger.containsAny(tagsList)) {
                logger.removeHandler(handler);
            }
        }
    }

    public static void removeHandlerByTags(DataHandler handler, String... tags) {
        removeHandlerByTags(handler, false, tags);
    }
}
