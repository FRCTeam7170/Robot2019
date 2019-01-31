package frc.team7170.lib.networktables.command;

import java.util.Iterator;

public class StringArgumentsIterator implements Iterator<String> {

    private final StringArguments args;
    private int idx = 0;

    StringArgumentsIterator(StringArguments args) {
        this.args = args;
    }

    @Override
    public boolean hasNext() {
        return idx < args.numArgs();
    }

    @Override
    public String next() {
        String ret = args.get(idx);
        ++idx;
        return ret;
    }

    public String nextString() {
        return next();
    }

    public byte nextByte() {
        String next = next();
        try {
            return Byte.parseByte(next);
        } catch (NumberFormatException e) {
            throw new CommandAbortException(String.format("could not parse '%s' to byte", next));
        }
    }

    public short nextShort() {
        String next = next();
        try {
            return Short.parseShort(next);
        } catch (NumberFormatException e) {
            throw new CommandAbortException(String.format("could not parse '%s' to short", next));
        }
    }

    public int nextInt() {
        String next = next();
        try {
            return Integer.parseInt(next);
        } catch (NumberFormatException e) {
            throw new CommandAbortException(String.format("could not parse '%s' to int", next));
        }
    }

    public long nextLong() {
        String next = next();
        try {
            return Long.parseLong(next);
        } catch (NumberFormatException e) {
            throw new CommandAbortException(String.format("could not parse '%s' to long", next));
        }
    }

    public float nextFloat() {
        String next = next();
        try {
            return Float.parseFloat(next);
        } catch (NumberFormatException e) {
            throw new CommandAbortException(String.format("could not parse '%s' to float", next));
        }
    }

    public double nextDouble() {
        String next = next();
        try {
            return Double.parseDouble(next);
        } catch (NumberFormatException e) {
            throw new CommandAbortException(String.format("could not parse '%s' to double", next));
        }
    }
}
