package frc.team7170.lib.logging2;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

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
