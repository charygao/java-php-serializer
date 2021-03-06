package com.marcospassos.phpserializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Stores and resolves adapters for types.
 *
 * @author Marcos Passos
 * @since 1.0
 */
public class AdapterRegistry
{
    /**
     * The list of adapters.
     */
    private List<TypeAdapter> adapters;

    /**
     * The list of classes.
     */
    private List<Class> classes;

    /**
     * The list of primitive types.
     */
    private static Map<Class, Class> primitives = new HashMap<>();

    static {
        primitives.put(int.class, Integer.class);
        primitives.put(long.class, Long.class);
        primitives.put(double.class, Double.class);
        primitives.put(float.class, Float.class);
        primitives.put(boolean.class, Boolean.class);
        primitives.put(char.class, Character.class);
        primitives.put(byte.class, Byte.class);
        primitives.put(short.class, Short.class);
    }

    /**
     * Creates a registry containing the specified adapters.
     *
     * @param adapters The map of classes and adapters.
     */
    public AdapterRegistry(Map<Class, TypeAdapter> adapters)
    {
        this();

        for (Map.Entry<Class, TypeAdapter> entry : adapters.entrySet()) {
            registerAdapter(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Creates an empty registry.
     */
    public AdapterRegistry()
    {
        this.adapters = new ArrayList<>();
        this.classes = new ArrayList<>();
    }

    /**
     * Returns the map of types and adapters.
     *
     * @return The map of adapters indexed by the supported type.
     */
    public Map<Class, TypeAdapter> getAdapters()
    {
        LinkedHashMap<Class, TypeAdapter> map = new LinkedHashMap<>();

        for (int index = 0, size = classes.size(); index < size; index++) {
            map.put(classes.get(index), adapters.get(index));
        }

        return map;
    }

    /**
     * Registers an adapter for the specified type.
     *
     * This operation overrides any adapter already registered for the
     * specified type.
     *
     * @param type The type supported by the adapter.
     * @param adapter The adapter.
     */
    public void registerAdapter(Class type, TypeAdapter adapter)
    {
        for (int index = 0, size = classes.size(); index < size; index++) {
            Class<?> currentClass = classes.get(index);

            if (currentClass.isAssignableFrom(type)) {
                classes.add(index, type);
                adapters.add(index, adapter);

                return;
            }
        }

        classes.add(type);
        adapters.add(adapter);
    }

    /**
     * Returns the most specified adapter for the specified type.
     *
     * @param type The type which the adapter should support.
     *
     * @return The most specified adapter for the specified type.
     *
     * @throws IllegalArgumentException if no adapter for the specified type is
     * registered.
     */
    public TypeAdapter getAdapter(Class type)
    {
        for (int index = 0, size = classes.size(); index < size; index++) {
            Class<?> currentClass = classes.get(index);

            if (isAssignableFrom(currentClass, type)) {
                return adapters.get(index);
            }
        }

        throw new IllegalArgumentException(String.format(
            "No adapter registered for %s.",
            type
        ));
    }

    /**
     * Determines if the class or interface represented by {@code left} Class
     * object is either the same as, or is a superclass or superinterface of,
     * the class or interface represented by the {@code right} Class parameter.
     *
     * If the {@code left} Class object represents a primitive type, this method
     * returns true if the exactly {@code left} Class object or if the unboxed
     * type of this type is assignable from the {@code right} class.
     *
     * @param left The class to check if is the same as, or a subtype of the
     * {@code right} class.
     * @param right The class to check if is the same as or is a supertype of
     * the {@code left} class.
     *
     * @return the {@code boolean} value indicating whether objects of the
     * type {@code left} can be assigned to objects of type {@code right}.
     */
    private static boolean isAssignableFrom(Class<?> left, Class<?> right)
    {
        if (left.isArray() && right.isArray()) {
            left = left.getComponentType();
            right = right.getComponentType();
        }

        if (primitives.containsKey(left)) {
            left = primitives.get(left);
        }

        if (primitives.containsKey(right)) {
            right = primitives.get(right);
        }

        return left.isAssignableFrom(right);
    }
}
