package frc.team7170.lib.networktables.command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;

public class ArgumentativeCommand implements Consumer<String[]> {

    private static final String LONG_ARG_PREFIX = "--";
    private static final String SHORT_ARG_PREFIX = "-";

    public static class Argument {

        public final String longName;
        public final String shortName;
        public final int consume;

        public Argument(String longName, String shortName, int consume) {
            if (longName == null) {
                throw new NullPointerException("argument long name must be non-null");
            }
            if (consume < 0) {
                throw new IllegalArgumentException("argument cannot consume less that zero arguments");
            }
            // TODO: error check on argument names (no spaces, etc.)
            this.longName = longName;
            this.shortName = shortName;
            this.consume = consume;
        }

        public Argument(String longName, String shortName) {
            this(longName, shortName, 1);
        }

        public Argument(String longName) {
            this(longName, null);
        }
    }

    private final HashMap<String, Argument> nameArgumentMap = new HashMap<>();
    private final Consumer<ParsedArguments> callback;

    public ArgumentativeCommand(Consumer<ParsedArguments> callback, Argument... arguments) {
        for (Argument argument : arguments) {
            if (nameArgumentMap.put(argument.longName, argument) != null) {
                throw new IllegalArgumentException("multiple arguments of the same name were given");
            }
            if (argument.shortName != null && nameArgumentMap.put(argument.shortName, argument) != null) {
                throw new IllegalArgumentException("multiple arguments of the same name were given");
            }
        }
        this.callback = callback;
    }

    @Override
    public void accept(String[] tokens) {
        callback.accept(parseArgs(tokens));
    }

    public static class ParsedArguments {

        public final HashMap<String, String[]> argMap;
        public final String[] rest;

        private ParsedArguments(HashMap<String, String[]> argMap, String[] rest) {
            this.argMap = argMap;
            this.rest = rest;
        }
    }

    private ParsedArguments parseArgs(String[] tokens) {
        HashMap<String, String[]> argMap = new HashMap<>();
        String[] rest = new String[0];  // Default to empty array in case there is truly no "rest".
        int currConsume = 0;
        String[] currArray = null;
        int skip = 0;
        int idx = 0;
        for (String token : tokens) {
            if (skip > 0) {
                currArray[currConsume - skip] = token;
                --skip;
                break;
            }
            if (token.startsWith(SHORT_ARG_PREFIX)) {
                String argName;
                if (token.startsWith(LONG_ARG_PREFIX)) {
                    argName = token.substring(LONG_ARG_PREFIX.length());
                } else {
                    argName = token.substring(SHORT_ARG_PREFIX.length());
                }
                try {
                    currConsume = nameArgumentMap.get(argName).consume;
                    currArray = new String[currConsume];
                    argMap.put(argName, currArray);
                    skip = currConsume;
                } catch (NullPointerException e) {
                    // This occurs when an unrecognized argument was given.
                    System.out.println("Uh-oh");  // TODO: do some useful logging here
                }
            } else {
                rest = Arrays.copyOfRange(tokens, idx, tokens.length);
                break;
            }
            ++idx;
        }
        return new ParsedArguments(argMap, rest);
    }
}
