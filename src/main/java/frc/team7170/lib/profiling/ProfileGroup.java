package frc.team7170.lib.profiling;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.RpcAnswer;
import frc.team7170.lib.networktables.RPCResponses;
import frc.team7170.lib.util.RawUtil;
import frc.team7170.lib.util.ReflectUtil;

import java.lang.reflect.Method;
import java.nio.BufferUnderflowException;
import java.util.HashMap;


/**
 * TODO make networktables usage consistent by using the RPC annotation?
 */
public class ProfileGroup implements Runnable {

    public static final NetworkTable DEFAULT_TABLE =
            NetworkTableInstance.getDefault().getTable("profiling/profiles");
    public static final int NO_SELECTION = -1;

    private int nextID = 0;
    private HashMap<Integer, Runnable> routines;
    private int selected = NO_SELECTION;
    private NetworkTableEntry entry;

    public ProfileGroup(NetworkTableEntry entry) {
        this.entry = entry;
        NetworkTableInstance.getDefault().createRpc(entry, this::rpcCallback);
    }

    public ProfileGroup(String entryName) {
        this(DEFAULT_TABLE.getEntry(entryName));
    }

    public int addRoutine(Runnable routine) {
        if (routine == null) {
            throw new NullPointerException();
        }
        if (routines == null) {
            routines = new HashMap<>();
            selected = nextID;
        }
        routines.put(nextID, routine);
        int ret = nextID;
        updateID();
        return ret;
    }

    public boolean addRoutine(Runnable routine, int id) {
        if (routines != null && routines.containsKey(id)) {
            return false;
        }
        nextID = id;
        addRoutine(routine);
        return true;
    }

    public boolean removeRoutine(int id) {
        if (selected == id) {
            selected = NO_SELECTION;
        }
        return routines.remove(id) != null;
    }

    public void selectRoutine(int id) {
        if (routines.containsKey(id)) {
            selected = id;
        } else {
            throw new IllegalArgumentException("profile does not contain routine '" + id + "'");
        }
    }

    public int getSelectedRoutine() {
        return selected;
    }

    public NetworkTableEntry getEntry() {
        return entry;
    }

    public void run() {
        if (selected >= 0) {
            routines.get(selected).run();
        } else {
            throw new RuntimeException("no routine selected");
        }
    }

    private void rpcCallback(RpcAnswer answer) {
        if (answer.params.length == Integer.BYTES) {
            try {
                selectRoutine(RawUtil.toInt(answer.params));
                answer.postResponse(RPCResponses.SUCCESS);
            } catch (BufferUnderflowException e) {
                answer.postResponse(RPCResponses.ERROR);
            } catch (IllegalArgumentException e) {
                answer.postResponse(RPCResponses.FAIL);
            }
        } else {
            answer.postResponse(RPCResponses.ERROR);
        }
    }

    private void updateID() {
        while (routines.containsKey(nextID)) {
            ++nextID;
        }
    }

    public static ProfileGroup buildFromProfilable(Object obj, String entryName) {
        return buildFromProfilable(obj, obj.getClass(), DEFAULT_TABLE.getEntry(entryName));
    }

    public static ProfileGroup buildFromProfilable(Class<?> cls, String entryName) {
        return buildFromProfilable(null, cls, DEFAULT_TABLE.getEntry(entryName));
    }

    public static ProfileGroup buildFromProfilable(Object obj, NetworkTableEntry entry) {
        return buildFromProfilable(obj, obj.getClass(), entry);
    }

    public static ProfileGroup buildFromProfilable(Class<?> cls, NetworkTableEntry entry) {
        return buildFromProfilable(null, cls, entry);
    }

    private static ProfileGroup buildFromProfilable(Object profilable, Class<?> cls, NetworkTableEntry entry) {
        ProfileGroup profileGroup = new ProfileGroup(entry);
        ReflectUtil.getMethodAnnotationStream(cls, Profile.class).forEach(pair -> {
            Method method = pair.getLeft();
            Profile profile = pair.getRight();
            ReflectUtil.assertParameterCount(method, 0);
            ReflectUtil.assertReturnType(method, void.class);
            ReflectUtil.assertInvokable(method);

            Runnable routine = () -> {
                try {
                    method.invoke(profilable);
                } catch (Exception e) {
                    // TODO: Better error handling?
                    throw new RuntimeException("profile routine failure", e);
                }
            };
            if (!profileGroup.addRoutine(routine,
                    profile.value() == -1 ? profileGroup.nextID : profile.value())) {
                throw new RuntimeException("cannot use duplicate profile ID");
            }
        });
        return profileGroup;
    }
}
