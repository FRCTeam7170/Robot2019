package frc.team7170.lib.networktables.command;

public class StringArguments {

    public final String[] args;

    StringArguments(String[] args) {
        this.args = args;
    }

    public String get(int i) {
        return args[i];
    }

    public int numArgs() {
        return args.length;
    }

    public void assertArgsLength(int nargs) {
        if (numArgs() != nargs) {
            throw new CommandAbortException(String.format("invalid number of args; expected %d, got %d",
                    nargs, numArgs()));
        }
    }

    public StringArgumentsIterator asIterator() {
        return new StringArgumentsIterator(this);
    }
}
