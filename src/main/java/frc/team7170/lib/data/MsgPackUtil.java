package frc.team7170.lib.data;

import org.msgpack.core.MessagePacker;

import java.io.IOException;
import java.util.Map;

public final class MsgPackUtil {

    // Enforce non-instantiability.
    private MsgPackUtil() {}

    @FunctionalInterface
    public interface CheckedConsumer<T> {

        void accept(T value) throws IOException;
    }

    public static <K, V> void packMap(Map<K, V> map,
                                      MessagePacker packer,
                                      CheckedConsumer<K> keyPackerMethod,
                                      CheckedConsumer<V> valuePackerMethod)
            throws IOException {
        packer.packMapHeader(map.size());
        for (Map.Entry<K, V> entry : map.entrySet()) {
            keyPackerMethod.accept(entry.getKey());
            valuePackerMethod.accept(entry.getValue());
        }
    }

    public static <T> void packArray(T[] array,
                                     MessagePacker packer,
                                     CheckedConsumer<T> packerMethod)
            throws IOException {
        packer.packArrayHeader(array.length);
        for (T value : array) {
            packerMethod.accept(value);
        }
    }
}
