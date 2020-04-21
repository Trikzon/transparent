// Copied from Blur. Blur is under the MIT license
// https://github.com/tterrag1098/Blur/blob/fabric/src/main/java/com/tterrag/blur/util/ReflectionHelper.java
package io.github.trikzon.transparent;

import java.lang.reflect.Field;
import java.util.Arrays;

public class ReflectionHelper
{
    @SuppressWarnings("unchecked")
    public static <T> T getValue(Class<?> cls, Object instance, String...names) {
        try {
            return (T) getField(cls, names).get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Class<?> cls, String... names) {
        for (String name : names) {
            Field f = getFieldInternal(cls, name);
            if (f != null) {
                return f;
            }
        }
        throw new IllegalArgumentException("Could not find any of fields " + Arrays.toString(names) + " on class " + cls);
    }

    private static Field getFieldInternal(Class<?> cls, String name) {
        try {
            Field f = cls.getDeclaredField(name);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException | SecurityException e) {
            return null;
        }
    }
}
