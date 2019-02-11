package frc.team7170.lib;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Stream;

public final class ReflectUtil {

    private ReflectUtil() {}

    public static <T extends Annotation> Stream<Pair<Method, T>> getMethodAnnotationStream
            (Class<?> cls, Class<T> annotationCls) {
        return Arrays.stream(cls.getDeclaredMethods())
                .map(method -> new Pair<>(method, method.getAnnotation(annotationCls)))
                .filter(pair -> Objects.nonNull(pair.getRight()));
    }

    public static <T extends Annotation> Stream<Pair<Field, T>> getFieldAnnotationStream
            (Class<?> cls, Class<T> annotationCls) {
        return Arrays.stream(cls.getDeclaredFields())
                .map(field -> new Pair<>(field, field.getAnnotation(annotationCls)))
                .filter(pair -> Objects.nonNull(pair.getRight()));
    }

    public static void assertInvokable(Method method) {
        if (Modifier.isAbstract(method.getModifiers())) {
            throw new RuntimeException("method must not be abstract");
        }
    }

    public static void assertReturnType(Method method, Class type) {
        if (method.getReturnType() != type) {
            throw new RuntimeException(formatErrStr("invalid method return type",
                    type.getSimpleName(), method.getReturnType().getSimpleName()));
        }
    }

    public static void assertParameterCount(Method method, int count) {
        if (method.getParameterCount() != count) {
            throw new RuntimeException(formatErrStr("invalid method parameter count",
                    String.valueOf(count), String.valueOf(method.getParameterCount())));
        }
    }

    public static void assertParameterSpec(Method method, Class... types) {
        if (!Arrays.equals(method.getParameterTypes(), types)) {
            throw new RuntimeException(formatErrStr("invalid method parameter specification",
                    Arrays.toString(types), Arrays.toString(method.getParameterTypes())));
        }
    }

    public static void assertFinal(Member member) {
        if (!Modifier.isFinal(member.getModifiers())) {
            throw new RuntimeException("member is not final");
        }
    }

    public static void assertNonFinal(Member member) {
        if (Modifier.isFinal(member.getModifiers())) {
            throw new RuntimeException("member is final");
        }
    }

    private static String formatErrStr(String base, String expected, String got) {
        return base + "; expected '" + expected + "', got '" + got + "'";
    }
}
