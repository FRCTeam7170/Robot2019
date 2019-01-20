package frc.team7170.lib.networktables;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.networktables.RpcAnswer;
import frc.team7170.lib.util.ReflectUtil;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Method;


public class Communication {

    private static final NetworkTableInstance inst = NetworkTableInstance.getDefault();
    private static final String entryNamePrefixSep = "_";

    public static void registerRpc(Class<?> cls, String namePrefix) {
        registerRpc(null, cls, namePrefix);
    }

    public static void registerRpc(Object obj, String namePrefix) {
        registerRpc(obj, obj.getClass(), namePrefix);
    }

    private static void registerRpc(Object rpcer, Class<?> cls, String prefix) {
        ReflectUtil.getMethodAnnotationStream(cls, RPC.class).forEach(pair -> {
            Method method = pair.getLeft();
            RPC rpc = pair.getRight();

            if ((rpcer != null && !ReflectUtil.isStatic(method)) || (rpcer == null && ReflectUtil.isStatic(method))) {
                ReflectUtil.assertInvokable(method);
                NetworkTableEntry entry = inst.getEntry(resolveEntryName(prefix, rpc.value(), method));

                if (rpc.passAnswer()) {
                    ReflectUtil.assertParameterSpec(method, new Class[] {RpcAnswer.class});
                    ReflectUtil.assertReturnType(method, void.class);
                    entry.createRpc(answer -> {
                        try {
                            method.invoke(rpcer, answer);
                        } catch (Exception e) {
                            // TODO: Better error handling?
                            throw new RuntimeException("rpc invocation failure", e);
                        }
                    });
                } else {
                    ReflectUtil.assertParameterSpec(method, new Class[] {byte[].class});
                    ReflectUtil.assertReturnType(method, byte[].class);
                    entry.createRpc(answer -> {
                        try {
                            answer.postResponse((byte[]) method.invoke(rpcer, ArrayUtils.toObject(answer.params)));
                        } catch (Exception e) {
                            // TODO: Better error handling?
                            throw new RuntimeException("rpc invocation failure", e);
                        }
                    });
                }
            }
        });
    }

    private static String resolveEntryName(String prefix, String annoVal, Method method) {
        return prefix + entryNamePrefixSep + (annoVal.isEmpty() ? method.getName() : annoVal);
    }
}
