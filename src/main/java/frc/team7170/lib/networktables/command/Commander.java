package frc.team7170.lib.networktables.command;

import edu.wpi.first.networktables.EntryNotification;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import frc.team7170.lib.Name;
import frc.team7170.lib.networktables.stream.StringStream;

import java.util.Arrays;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Commander {

    // TODO: add help messages for commands (and arguments)?

    private static final Logger LOGGER = Logger.getLogger(Commander.class.getName());

    // I.e. Dashboard -> robot
    private static final String UPLOAD_SUFFIX = "_UPLOAD";
    // I.e. Robot -> dashboard
    private static final String DOWNLOAD_SUFFIX = "_DOWNLOAD";

    private final HashMap<String, Consumer<StringArguments>> commandMap = new HashMap<>();
    private final StringStream commandStream;

    public Commander(NetworkTable table, Name namePrefix) {
        NetworkTableEntry receivingEntry = table.getEntry(namePrefix + UPLOAD_SUFFIX);
        NetworkTableEntry transmittingEntry = table.getEntry(namePrefix + DOWNLOAD_SUFFIX);
        commandStream = new StringStream(new StringStream.Config()
                .receivingEntry(receivingEntry)
                .transmittingEntry(transmittingEntry));
        // Not peak mode (by default), so the input won't be blocked.
        commandStream.addListener(this::listenerCallback);
    }

    public boolean registerCommand(Name name, Consumer<StringArguments> callback) {
        return commandMap.put(name.getName().toUpperCase(), callback) == null;
    }

    public boolean unregisterCommand(Name name) {
        return commandMap.remove(name.getName().toUpperCase()) != null;
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
