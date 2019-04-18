package frc.team7170.lib.logging2;

import frc.team7170.lib.ResourceManager;
import org.msgpack.core.MessagePack;
import org.msgpack.core.MessagePacker;

import java.io.*;
import java.util.IllegalFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO: it might be work making a MsgPackUtils class or packing Maps, Lists, arrays, etc. easily (one call)
// TODO: thread safety
public class MessagePackDataHandler implements DataHandler {

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
    public void handle(double timestamp, String loggableName, Map<String, Value> values) {
        try {
            packer.packDouble(timestamp);
            packer.packString(loggableName);
            packer.packMapHeader(values.size());
            Iterator<Map.Entry<String, Value>> iterator = values.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Value> entry = iterator.next();
                packer.packString(entry.getKey());
                Value value = entry.getValue();
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
                        packArray(packer::packBoolean, value.getBooleanArray());
                        break;
                    case DOUBLE_ARRAY:
                        packArray(packer::packDouble, value.getDoubleArray());
                        break;
                    case STRING_ARRAY:
                        packArray(packer::packString, value.getStringArray());
                        break;
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "failed to pack data", e);
        }
    }

    @FunctionalInterface
    private interface CheckedConsumer<T> {

        void accept(T val) throws IOException;
    }

    private <T> void packArray(CheckedConsumer<T> packerFun, T[] vals) throws IOException {
        packer.packArrayHeader(vals.length);
        for (T val : vals) {
            packerFun.accept(val);
        }
    }

    @Override
    public void close() throws Exception {
        packer.close();
    }

    public static MessagePackDataHandler fromAbsoluteFile(File file) {
        try {
            return new MessagePackDataHandler(new FileOutputStream(file.getAbsoluteFile()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static MessagePackDataHandler fromFile(String fileName) {
        return fromAbsoluteFile(new File(DEFAULT_DIR, fileName));
    }

    public static MessagePackDataHandler fromDirectory(File directory, FileCyclingScheme fcs) {
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

    public static MessagePackDataHandler fromDirectory(File directory) {
        return fromDirectory(directory, FileCyclingScheme.DEFAULT);
    }

    public static MessagePackDataHandler fromDirectory(String directoryName, FileCyclingScheme fcs) {
        return fromDirectory(new File(directoryName), fcs);
    }

    public static MessagePackDataHandler fromDirectory(String directoryName) {
        return fromDirectory(directoryName, FileCyclingScheme.DEFAULT);
    }

    public static MessagePackDataHandler getDefault() {
        if (DEFAULT == null) {
            DEFAULT = fromDirectory(DEFAULT_DIR);
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
