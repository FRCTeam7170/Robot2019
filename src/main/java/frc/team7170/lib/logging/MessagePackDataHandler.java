package frc.team7170.lib.logging;

import frc.team7170.lib.ResourceManager;
import frc.team7170.lib.data.MsgPackUtil;
import frc.team7170.lib.data.Value;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: thread safety
public class MessagePackDataHandler implements DataHandler {

    // TODO: this should be made a top-level class
    public static class FileCyclingScheme {

        public static final FileCyclingScheme DEFAULT = new FileCyclingScheme("log%s.msgpack");
        private static final String VERIFICATION_RE = ".*%s.*";

        public final String format;
        public final int maxFiles;
        public final Pattern re;

        public FileCyclingScheme(String format, int maxFiles) {
            this.format = requireValidFormatString(format);
            this.maxFiles = maxFiles;
            String[] formatPartitions = format.split("%s", 2);
            this.re = Pattern.compile(
                    Pattern.quote(formatPartitions[0]) + "(\\d+)" + Pattern.quote(formatPartitions[1])
            );
        }

        public FileCyclingScheme(String format) {
            this(format, 5);
        }

        private String requireValidFormatString(String format) {
            Objects.requireNonNull(format, "format string must be non-null");
            if (!format.matches(VERIFICATION_RE)) {
                throw new IllegalArgumentException("invalid format string; must accept a string parameter ('%s')");
            }
            try {
                // Make sure the format string works with a single string argument, as it should.
                // This is a bit of a hack, but it works. ¯\_(ツ)_/¯
                String.format(format, "0");
            } catch (IllegalFormatException e) {
                throw new IllegalArgumentException(e);
            }
            return format;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(MessagePackDataHandler.class.getName());
    private static final File DEFAULT_DIR = new File(System.getProperty("user.home"), "dataLogs");
    private static MessagePackDataHandler DEFAULT;

    private final MessagePacker packer;

    public MessagePackDataHandler(OutputStream stream) {
        packer = MessagePack.newDefaultPacker(stream);
    }

    @Override
    public void handle(double timestamp, Map<List<String>, Value> values) {
        try {
            packer.packDouble(timestamp);
            MsgPackUtil.packMap(
                    values,
                    packer,
                    strings -> MsgPackUtil.packArray((String[]) strings.toArray(), packer, packer::packString),
                    value -> {
                        switch (value.getType()) {
                            case BOOLEAN:
                                packer.packBoolean(value.getBoolean());
                                break;
                            case DOUBLE:
                                packer.packDouble(value.getDouble());
                                break;
                            case STRING:
                                packer.packString(value.getString());
                                break;
                            case BOOLEAN_ARRAY:
                                MsgPackUtil.packArray(value.getBooleanArray(), packer, packer::packBoolean);
                                break;
                            case DOUBLE_ARRAY:
                                MsgPackUtil.packArray(value.getDoubleArray(), packer, packer::packDouble);
                                break;
                            case STRING_ARRAY:
                                MsgPackUtil.packArray(value.getStringArray(), packer, packer::packString);
                                break;
                            case RAW:
                                MsgPackUtil.packArray(value.getRaw(), packer, packer::packByte);
                                break;
                        }
                    }
            );
            packer.flush();
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "failed to pack data", e);
        }
    }

    @Override
    public void close() throws Exception {
        packer.close();
    }

    public static MessagePackDataHandler fromAbsoluteFile(File file)
            throws FileNotFoundException {
        Objects.requireNonNull(file, "file must be non-null");
        return new MessagePackDataHandler(new FileOutputStream(file.getAbsoluteFile()));
    }

    public static MessagePackDataHandler fromRelativeFileName(String fileName)
            throws FileNotFoundException {
        // File does null pointer check on name for us.
        return fromAbsoluteFile(new File(DEFAULT_DIR, fileName));
    }

    public static MessagePackDataHandler fromDirectory(File directory, FileCyclingScheme fcs)
            throws FileNotFoundException {
        Objects.requireNonNull(directory, "directory must be non-null");
        Objects.requireNonNull(fcs, "file cycling scheme must be non-null");
        if (directory.exists()) {
            if (!directory.isDirectory()) {
                throw new IllegalArgumentException("path exists, but is not a directory");
            }
        } else {
            if (!directory.mkdir()) {
                throw new RuntimeException("directory does not exist and could not be created");
            }
        }
        shiftFiles(directory, fcs);
        return fromAbsoluteFile(new File(directory, String.format(fcs.format, 0)));
    }

    public static MessagePackDataHandler fromDirectory(File directory)
            throws FileNotFoundException {
        return fromDirectory(directory, FileCyclingScheme.DEFAULT);
    }

    public static MessagePackDataHandler fromDirectory(String directoryName, FileCyclingScheme fcs)
            throws FileNotFoundException {
        // File does null pointer check on name for us.
        return fromDirectory(new File(directoryName), fcs);
    }

    public static MessagePackDataHandler fromDirectory(String directoryName)
            throws FileNotFoundException{
        return fromDirectory(directoryName, FileCyclingScheme.DEFAULT);
    }

    public static MessagePackDataHandler getDefault() {
        if (DEFAULT == null) {
            try {
                DEFAULT = fromDirectory(DEFAULT_DIR);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(
                        String.format("default {} could not be initialized", MessagePackDataHandler.class.getName()),
                        e
                );
            }
            ResourceManager.getInstance().addResource(MessagePackDataHandler.class.getSimpleName(), DEFAULT);
        }
        return DEFAULT;
    }

    private static void shiftFiles(File directory, FileCyclingScheme fcs) {
        File[] files = new File[fcs.maxFiles];
        for (File file : directory.listFiles()) {
            Matcher matcher = fcs.re.matcher(file.getName());
            if (matcher.matches()) {
                int fileNum = Integer.parseInt(matcher.group(1));
                if (fileNum < fcs.maxFiles) {
                    files[fileNum] = file;
                }
            }
        }
        int firstMissing = 0;
        for (File file : files) {
            if (file == null) {
                break;
            }
            ++firstMissing;
        }
        if (firstMissing > 0 && firstMissing < fcs.maxFiles) {
            if (!files[firstMissing - 1].renameTo(new File(directory, String.format(fcs.format, firstMissing)))) {
                LOGGER.warning("failed to rename file during shift; file might be overwritten");
            }
        }
        for (int i = firstMissing - 2; i >= 0; --i) {
            if (!files[i].renameTo(files[i + 1])) {
                LOGGER.warning("failed to rename file during shift; file might be overwritten");
            }
        }
    }
}
