package frc.team7170.lib;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A helper class for managing the closing of {@linkplain AutoCloseable resources} that remain open until the end of the
 * application.
 * <p>
 * Other resource-owning classes in spooky-lib automatically add their resources here for automated
 * closing, so the user should be sure to call {@link ResourceManager#close() close} at the end of the application even
 * if they do not add their own resources (via {@link ResourceManager#addResource(String, AutoCloseable)} or
 * {@link ResourceManager#addResource(AutoCloseable)}). Alternatively, one can use
 * {@link TimedRobotEnhanced TimedRobotEnhanced} as the robot base class, which automatically calls
 * {@link ResourceManager#close() close} on application exit/error.
 *
 * @author Robert Russell
 * @see java.lang.AutoCloseable
 * @see TimedRobotEnhanced
 */
public final class ResourceManager implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(ResourceManager.class.getName());

    // Hash on the AutoCloseable so duplicates are not allowed and multiple resources can have the same name.
    private final Map<AutoCloseable, String> resources = new HashMap<>();

    private ResourceManager() {}

    private static final ResourceManager INSTANCE = new ResourceManager();

    /**
     * Get the singleton instance.
     * @return the singleton instance.
     */
    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    /**
     * Register a resource to be automatically closed.
     * @param name the name to assign the resource. This appears in error messages if the resource fails to close.
     * @param autoCloseable the resource to register.
     */
    public synchronized void addResource(String name, AutoCloseable autoCloseable) {
        resources.put(Objects.requireNonNull(autoCloseable), Objects.requireNonNull(name));
    }

    /**
     * Register a resource to be automatically closed. The resource will be given the name returned by
     * {@link Object#toString() toString}.
     * @param autoCloseable the resource to register.
     */
    public void addResource(AutoCloseable autoCloseable) {
        addResource(autoCloseable.toString(), autoCloseable);
    }

    /**
     * Attempt to close all the registered resources. If any resource fails to {@linkplain AutoCloseable#close close},
     * this method will log an error message to {@linkplain java.util.logging Java util logging}.
     */
    @Override
    public synchronized void close() {
        Iterator<Map.Entry<AutoCloseable, String>> iterator = resources.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<AutoCloseable, String> entry = iterator.next();
            try {
                entry.getKey().close();
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, String.format("failed to close resource '%s'", entry.getValue()), e);
            }
        }
    }
}
