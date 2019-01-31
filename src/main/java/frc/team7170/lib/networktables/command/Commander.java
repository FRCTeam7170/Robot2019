package frc.team7170.lib.networktables.command;

import edu.wpi.first.networktables.EntryNotification;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.team7170.lib.logging.LoggerManager;
import frc.team7170.lib.networktables.stream.StringStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Commander {

    // TODO: add help messages for commands (and arguments)?

    private static final Logger LOGGER = LoggerManager.getLogger(Commander.class.getName());

    // I.e. Dashboard -> robot
    private static final String UPLOAD_SUFFIX = "_UPLOAD";
    // I.e. Robot -> dashboard
    private static final String DOWNLOAD_SUFFIX = "_DOWNLOAD";

    private final HashMap<String, Consumer<StringArguments>> commandMap = new HashMap<>();
    private final StringStream commandStream;

    public Commander(NetworkTableInstance inst, String namePrefix) {
        // TODO: error check names in master Communication class.
        NetworkTableEntry receivingEntry = inst.getEntry(namePrefix + UPLOAD_SUFFIX);
        NetworkTableEntry transmittingEntry = inst.getEntry(namePrefix + DOWNLOAD_SUFFIX);
        commandStream = new StringStream(new StringStream.Config()
                .receivingEntry(receivingEntry)
                .transmittingEntry(transmittingEntry));
        // Not peak mode (by default), so the input won't be blocked.
        commandStream.addListener(this::listenerCallback);
    }

    public boolean registerCommand(String name, Consumer<StringArguments> callback) {
        name = name.toUpperCase();
        return commandMap.put(name, callback) == null;
    }

    public boolean unregisterCommand(String name) {
        name = name.toUpperCase();
        return commandMap.remove(name) != null;
    }

    private void listenerCallback(EntryNotification notification) {
        Arrays.stream(notification.value.getStringArray())
                .map(String::strip)
                .map(str -> str.split("\\s+"))
                .forEach(tokens -> {
                    String cmdName = tokens[0].toUpperCase();
                    try {
                        commandMap.get(cmdName).accept(
                                new StringArguments(Arrays.copyOfRange(tokens, 1, tokens.length)));
                    } catch (NullPointerException e) {
                        // throw new RuntimeException("unmapped command received");
                        LOGGER.warning(String.format("unmapped command ('%s') received", cmdName));
                    } catch (CommandAbortException e) {
                        LOGGER.log(Level.WARNING, String.format("command '%s' aborted", cmdName), e);
                    }
                });
    }
}
