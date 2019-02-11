package frc.team7170.lib;


public final class DebugUtil {

    private DebugUtil() {}
    
    public static void assert_(boolean test, String msg) {
        if (!test) {
            throw new AssertionError(msg);
        }
    }
}
