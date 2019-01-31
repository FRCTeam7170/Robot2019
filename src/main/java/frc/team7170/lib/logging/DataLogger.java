package frc.team7170.lib.logging;

import org.msgpack.value.Value;

import java.util.Arrays;

// TODO: in the future (post season), the data logging system should probably be merged with the communicator system--they have a lot of similarities.
// Most likely this could be achieved with an extra boolean flag on Transmitter annotations ("dataLog" or something).
public interface DataLogger {

    String[] reportHeaders();

    Value[] reportData();

    default String reportName() {
        return getClass().getSimpleName();
    }

    default Value[] mergeValueArrays(Value[] a, Value[] b) {
        Value[] ret = Arrays.copyOf(a, a.length + b.length);
        System.arraycopy(b, 0, ret, a.length, b.length);
        return ret;
    }
}
