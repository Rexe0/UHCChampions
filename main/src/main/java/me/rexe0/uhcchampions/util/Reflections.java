package me.rexe0.uhcchampions.util;

import java.lang.reflect.Field;

/**
 * A utility class that simplifies reflection in Bukkit plugins.
 *
 * @author Kristian
 *
 * Edited by Rexe0
 */
public final class Reflections {

    /**
     * Retrieve a field accessor for a specific field type and name.
     *
     * @param target    the target type
     * @param name      the name of the field, or NULL to ignore
     * @param fieldType a compatible field type
     * @return the field accessor
     */
    public static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType) {
        return getField(target, name, fieldType, 0);
    }

    // Common method
    private static <T> FieldAccessor<T> getField(Class<?> target, String name, Class<T> fieldType, int index) {
        for (final Field field : target.getDeclaredFields()) {
            if ((name == null || field.getName().equals(name)) && fieldType.isAssignableFrom(field.getType()) && index-- <= 0) {
                field.setAccessible(true);

                // A function for retrieving a specific field value
                return (target1, value) -> {
                    try {
                        field.set(target1, value);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Cannot access reflection.", e);
                    }
                };
            }
        }

        // Search in parent classes
        if (target.getSuperclass() != null)
            return getField(target.getSuperclass(), name, fieldType, index);
        throw new IllegalArgumentException("Cannot find field with type " + fieldType);
    }

    /**
     * An interface for retrieving the field content.
     *
     * @param <T> field type
     */
    public interface FieldAccessor<T> {
        /**
         * Set the content of a field.
         *
         * @param target the target object, or NULL for a static field
         * @param value  the new value of the field
         */
         void set(Object target, Object value);
    }
}