package frc.team7170.lib.networktables.command;

import frc.team7170.lib.Name;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class ArgumentativeCommand implements Consumer<StringArguments> {

    private static final Logger LOGGER = Logger.getLogger(ArgumentativeCommand.class.getName());

    private static final String LONG_ARG_PREFIX = "--";
    private static final String SHORT_ARG_PREFIX = "-";

    public static class Argument {

        public final String longName;
        public final String shortName;
        public final int consume;

        public Argument(Name longName, Name shortName, int consume) {
            if (consume < 0) {
                throw new IllegalArgumentException("argument cannot consume less that zero arguments");
            }
            this.longName = longName.getName();
            this.shortName = shortName != null ? shortName.getName() : null;
            this.consume = consume;
        }

        public Argument(Name longName, Name shortName) {
            this(longName, shortName, 1);
        }

        public Argument(Name longName) {
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
    public void accept(StringArguments args) {
        callback.accept(parseArgs(args));
    }

    public static class ParsedArguments {

        public final HashMap<String, StringArguments> argMap;
        public final StringArguments rest;

        private ParsedArguments(HashMap<String, StringArguments> argMap, StringArguments rest) {
            this.argMap = argMap;
            this.rest = rest;
        }
    }

    private ParsedArguments parseArgs(StringArguments args) {
        HashMap<String, StringArguments> argMap = new HashMap<>();
        int currConsume = 0;
        String[] currArray = null;
        int skip = 0;
        int idx = 0;
        for (String token : args.args) {
            if (skip > 0) {
                currArray[currConsume - skip] = token;
                --skip;
            }
            if (token.startsWith(SHORT_ARG_PREFIX)) {
                String argName;
                if (token.startsWith(LONG_ARG_PREFIX)) {
                    argName = token.substring(LONG_ARG_PREFIX.length());
                } else {
                    argName = token.substring(SHORT_ARG_PREFIX.length());
                }
                try {
                    Argument argument = nameArgumentMap.get(argName);
                    currConsume = argument.consume;
                    currArray = new String[currConsume];
                    argMap.put(argument.longName, new StringArguments(currArray));
                    skip = currConsume;
                } catch (NullPointerException e) {
                    // This occurs when an unrecognized argument was given.
                    LOGGER.warning(String.format("an unrecognized argument '%s' was given; " +
                            "stopping parsing and remaining arguments will be put in 'rest'", token));
                    break;
                }
            } else {
                break;
            }
            ++idx;
        }
        String[] rest = Arrays.copyOfRange(args.args, idx, args.numArgs());
        return new ParsedArguments(argMap, new StringArguments(rest));
    }
}
