package frc.team7170.lib.logging;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

// TODO
public class ShuffleboardHandler extends Handler {

    @Override
    public void publish(LogRecord record) {
        if (!isLoggable(record)) {
            return;
        }
        // TODO
    }

    @Override
    public void flush() {

    }

    @Override
    public void close() throws SecurityException {

    }
}
