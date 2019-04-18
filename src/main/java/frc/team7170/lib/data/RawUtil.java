package frc.team7170.lib.data;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public final class RawUtil {

    // Enforce non-instantiability.
    private RawUtil() {}

    public static byte[] toBytes(int val) {
        return ByteBuffer.allocate(Integer.BYTES).putInt(val).array();
    }

    public static byte[] toBytes(short val) {
        return ByteBuffer.allocate(Short.BYTES).putShort(val).array();
    }

    public static byte[] toBytes(long val) {
        return ByteBuffer.allocate(Long.BYTES).putLong(val).array();
    }

    public static byte[] toBytes(float val) {
        return ByteBuffer.allocate(Float.BYTES).putFloat(val).array();
    }

    public static byte[] toBytes(double val) {
        return ByteBuffer.allocate(Integer.BYTES).putDouble(val).array();
    }

    public static byte[] toBytes(char val) {
        return ByteBuffer.allocate(Character.BYTES).putChar(val).array();
    }

    public static byte[] toBytes(int[] vals) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(vals.length * Integer.BYTES);
        byteBuffer.asIntBuffer().put(vals);
        return byteBuffer.array();
    }

    public static byte[] toBytes(short[] vals) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(vals.length * Short.BYTES);
        byteBuffer.asShortBuffer().put(vals);
        return byteBuffer.array();
    }

    public static byte[] toBytes(long[] vals) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(vals.length * Long.BYTES);
        byteBuffer.asLongBuffer().put(vals);
        return byteBuffer.array();
    }

    public static byte[] toBytes(float[] vals) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(vals.length * Float.BYTES);
        byteBuffer.asFloatBuffer().put(vals);
        return byteBuffer.array();
    }

    public static byte[] toBytes(double[] vals) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(vals.length * Double.BYTES);
        byteBuffer.asDoubleBuffer().put(vals);
        return byteBuffer.array();
    }

    public static byte[] toBytes(char[] vals) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(vals.length * Character.BYTES);
        byteBuffer.asCharBuffer().put(vals);
        return byteBuffer.array();
    }

    public static byte[] toBytes(Object val) {
        return String.valueOf(val).getBytes();
    }

    public static int toInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getInt();
    }

    public static short toShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getShort();
    }

    public static long toLong(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getLong();
    }

    public static float toFloat(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getFloat();
    }

    public static double toDouble(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getDouble();
    }

    public static char toChar(byte[] bytes) {
        return ByteBuffer.wrap(bytes).getChar();
    }

    public static int[] toIntArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).asIntBuffer().array();
    }

    public static short[] toShortArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).asShortBuffer().array();
    }

    public static long[] toLongArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).asLongBuffer().array();
    }

    public static float[] toFloatArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).asFloatBuffer().array();
    }

    public static double[] toDoubleArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).asDoubleBuffer().array();
    }

    public static char[] toCharArray(byte[] bytes) {
        return ByteBuffer.wrap(bytes).asCharBuffer().array();
    }

    public static String toString(byte[] bytes, Charset charset) {
        return new String(bytes, charset);
    }

    public static String toString(byte[] bytes) {
        return toString(bytes, StandardCharsets.UTF_8);
    }
}
