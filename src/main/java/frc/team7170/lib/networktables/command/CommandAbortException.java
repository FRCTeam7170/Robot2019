package frc.team7170.lib.networktables.command;

public class CommandAbortException extends RuntimeException {

    public CommandAbortException(String msg) {
        super(msg);
    }

    public CommandAbortException() {}
}
