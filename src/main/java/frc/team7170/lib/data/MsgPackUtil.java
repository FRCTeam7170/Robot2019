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

    @FunctionalInterface
    public interface CheckedBooleanConsumer {

        void accept(boolean value) throws IOException;
    }

    @FunctionalInterface
    public interface CheckedByteConsumer {

        void accept(byte value) throws IOException;
    }

    @FunctionalInterface
    public interface CheckedCharConsumer {

        void accept(char value) throws IOException;
    }

    @FunctionalInterface
    public interface CheckedShortConsumer {

        void accept(short value) throws IOException;
    }

    @FunctionalInterface
    public interface CheckedIntegerConsumer {

        void accept(int value) throws IOException;
    }

    @FunctionalInterface
    public interface CheckedLongConsumer {

        void accept(long value) throws IOException;
    }

    @FunctionalInterface
    public interface CheckedFloatConsumer {

        void accept(float value) throws IOException;
    }

    @FunctionalInterface
    public interface CheckedDoubleConsumer {

        void accept(double value) throws IOException;
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

    public static void packArray(boolean[] array,
                                 MessagePacker packer,
                                 CheckedBooleanConsumer packerMethod)
            throws IOException {
        packer.packArrayHeader(array.length);
        for (boolean element : array) {
            packerMethod.accept(element);
        }
    }

    public static void packArray(byte[] array,
                                 MessagePacker packer,
                                 CheckedByteConsumer packerMethod)
            throws IOException {
        packer.packArrayHeader(array.length);
        for (byte element : array) {
            packerMethod.accept(element);
        }
    }

    public static void packArray(char[] array,
                                 MessagePacker packer,
                                 CheckedCharConsumer packerMethod)
            throws IOException {
        packer.packArrayHeader(array.length);
        for (char element : array) {
            packerMethod.accept(element);
        }
    }

    public static void packArray(short[] array,
                                 MessagePacker packer,
                                 CheckedShortConsumer packerMethod)
            throws IOException {
        packer.packArrayHeader(array.length);
        for (short element : array) {
            packerMethod.accept(element);
        }
    }

    public static void packArray(int[] array,
                                 MessagePacker packer,
                                 CheckedIntegerConsumer packerMethod)
            throws IOException {
        packer.packArrayHeader(array.length);
        for (int element : array) {
            packerMethod.accept(element);
        }
    }

    public static void packArray(long[] array,
                                 MessagePacker packer,
                                 CheckedLongConsumer packerMethod)
            throws IOException {
        packer.packArrayHeader(array.length);
        for (long element : array) {
            packerMethod.accept(element);
        }
    }

    public static void packArray(float[] array,
                                 MessagePacker packer,
                                 CheckedFloatConsumer packerMethod)
            throws IOException {
        packer.packArrayHeader(array.length);
        for (float element : array) {
            packerMethod.accept(element);
        }
    }

    public static void packArray(double[] array,
                                 MessagePacker packer,
                                 CheckedDoubleConsumer packerMethod)
            throws IOException {
        packer.packArrayHeader(array.length);
        for (double element : array) {
            packerMethod.accept(element);
        }
    }

    public static <T> void packArray(T[] array,
                                     MessagePacker packer,
                                     CheckedConsumer<T> packerMethod)
            throws IOException {
        packer.packArrayHeader(array.length);
        for (T element : array) {
            packerMethod.accept(element);
        }
    }
}
