package frc.team7170.lib.data;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.Timer;
import frc.team7170.lib.data.property.RProperty;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * A helper for scheduling periodically polling the value of {@linkplain RProperty readable properties} according to
 * their {@linkplain RProperty#getPollPeriodMs() poll period} and the
 * {@linkplain Timer#getFPGATimestamp() roboRIO FPGA time}. Readable properties are scheduled to be periodically polled
 * via {@link PropertyPoller#addProperty(List, RProperty) addProperty} or
 * {@link PropertyPoller#addProperties(PropertyGroup) addProperties}, the former of which accepts a {@link List List} of
 * ancestors' names to facilitate hierarchical nomenclature. The value of each registered {@code RProperty} is
 * periodically polled at the appropriate times and the resulting {@link Value Value} object is passed to a given
 * {@link Consumer callback} along with a {@code List} of {@code String}s representing its lineage.
 * </p>
 * <p>
 * The scheduling of readable properties employs a <em>consolidation mechanism</em>, whereby readable properties
 * scheduled to be polled within some time threshold of each other are grouped together. To be exact, if the
 * {@code PropertyPoller} has a consolidation threshold {@code t}, and if a {@code RProperty} is being scheduled at some
 * time {@code A}, but there exists some readable property or group of readable properties scheduled at time {@code B}
 * with {@code A - t <= B <= A + t}, then the new {@code RProperty} will be consolidated with the older one; that is,
 * the new {@code RProperty} will be scheduled at time {@code B} rather then time {@code A}.
 * </p>
 * TODO characterize the performance of the consolidation mechanism and insert figures here in an apiNote.
 *
 * @author Robert Russell
 * @see RProperty
 */
// TODO: generalize method of getting current time (i.e. make it so you pass in Timer::getFPGATimestamp as a param)
// TODO: we ought to actually test the performance gain (or loss!) of the consolidation mechanism with various callbacks (slow or fast)
public final class PropertyPoller {

    /**
     * This {@link NavigableMap NavigableMap} maps the scheduled time for a group of properties to be polled to the
     * group of properties. Note the group of properties is in turn stored as a map itself with the {@link List List} of
     * each {@link RProperty RProperty}'s ancestors' names and its own name as the key. A {@link TreeMap TreeMap} (BST)
     * is used so that finding the next group of readable properties to poll has time complexity O(log(n)).
     */
    private final NavigableMap<Double, Map<List<String>, RProperty>> timePropertyMap = new TreeMap<>();

    /**
     *
     */
    private final Consumer<Map<List<String>, Value>> callback;

    /**
     *
     */
    private final double consolidationThresholdSec;

    /**
     *
     */
    private final Notifier notifier = new Notifier(this::run);

    /**
     *
     */
    private double nextKey;

    /**
     *
     */
    private boolean started = false;

    public PropertyPoller(Consumer<Map<List<String>, Value>> callback, int consolidationThreshholdMs) {
        this.callback = callback;
        this.consolidationThresholdSec = (double) consolidationThreshholdMs / 1000.0;
    }

    public PropertyPoller(Consumer<Map<List<String>, Value>> callback) {
        this(callback, 5);
    }

    public void addProperty(List<String> ancestry, RProperty property) {
        List<String> names = new ArrayList<>(ancestry);
        names.add(property.getName());
        double nowSeconds = Timer.getFPGATimestamp();
        addPropertyWithConsolidation(nowSeconds, names, property);
        if (!started) {
            started = true;
            nextKey = nowSeconds;
            run();
        }
    }

    public void addProperties(PropertyGroup<RProperty> propertyGroup) {
        propertyGroup.forEach(pair -> addProperty(pair.getLeft().getLineage(), pair.getRight()));
    }

    private synchronized void addPropertyWithConsolidation(double pollTime, List<String> lineage, RProperty property) {
        // Check if there is an entry scheduled before the new one and if there is and it is within the consolidation
        // threshold, consolidate them.
        // Note that this is not a optimal implementation in terms of maintaining a poll period as close as possible to
        // that specified in the RProperty in that, when checking for a previously-scheduled property to consolidate
        // with, it short-circuits if it finds one scheduled before the new RProperty and does not bother checking if
        // there's one scheduled closer to the new RProperty after the RProperty; also, each set of consolidated
        // properties take on the scheduled time of the eldest RProperty, not the average time of each RProperty in the
        // set.
        Map.Entry<Double, Map<List<String>, RProperty>> entry = timePropertyMap.floorEntry(pollTime);
        if (entry != null && (pollTime - entry.getKey()) <= consolidationThresholdSec) {
            entry.getValue().put(lineage, property);
        } else {
            // Check if there is an entry scheduled after the new one and if there is and it is within the consolidation
            // threshold, consolidate them.
            entry = timePropertyMap.ceilingEntry(pollTime);
            if (entry != null && (entry.getKey() - pollTime) <= consolidationThresholdSec) {
                entry.getValue().put(lineage, property);
            } else {
                // Otherwise, just schedule a new entry.
                // Note the initial capacity of 4 here is arbitrary, but the default of 16 seemed extreme if
                // consolidation is infrequent.
                Map<List<String>, RProperty> newMap = new HashMap<>(4);
                newMap.put(lineage, property);
                timePropertyMap.put(pollTime, newMap);
            }
        }
    }

    private void run() {
        Map<List<String>, RProperty> properties;
        synchronized (this) {
            properties = timePropertyMap.remove(nextKey);
            // Re-schedule the properties about to be polled according to their poll period and the current time.
            double nowSeconds = Timer.getFPGATimestamp();
            properties.forEach(
                    (ancestry, property) -> addPropertyWithConsolidation(
                            nowSeconds + (double) property.getPollPeriodMs() / 1000.0,
                            ancestry, property
                    )
            );
            // Get the absolute time of when the next batch of properties should be polled.
            nextKey = timePropertyMap.firstKey();
        }
        // Potentially expensive operation, hence the minimal synchronization above.
        callback.accept(properties.entrySet().stream().collect(
                Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValue()))
        );
        double delay = nextKey - Timer.getFPGATimestamp();
        if (delay <= 0) {
            // If for whatever reason the delay is less than or equal to zero (i.e. we should've already polled the next
            // batch of properties), just run the next batch directly instead of incurring overhead with the Notifier.
            run();
        } else {
            notifier.startSingle(delay);
        }
    }
}
