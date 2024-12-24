/*
 * Copyright (c) 2024 Ampflower
 *
 * This software is subject to the terms of either the MIT or CC0-1.0 Licenses.
 * If a copy was not distributed with this file, you can obtain one at
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE-MIT
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE-CC0
 *
 * Sources:
 *  - https://github.com/Modflower/QuickBench
 *
 * SPDX-License-Identifier: MIT OR CC0-1.0
 *
 * Additional details are outlined in LICENSE.md, which you can obtain at
 * https://github.com/Modflower/QuickBench/blob/trunk/LICENSE.md
 */

package tfar.fastbench.quickbench.internal;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * @author Ampflower
 * @since 4.3.2
 **/
public final class Reflector {
    private static final String namespace = "intermediary";

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();
    private static final MappingResolver resolver = FabricLoader.getInstance().getMappingResolver();
    private static final boolean production = namespace.equals(resolver.getCurrentRuntimeNamespace());

    private static String unmap(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            return clazz.descriptorString();
        }
        return 'L' + unmapClassName(clazz).replace('.', '/') + ';';
    }

    private static String unmapClassName(Class<?> clazz) {
        return resolver.unmapClassName(namespace, clazz.getName());
    }

    private static boolean virtual(Method method, String reference, MethodType signature) {
        if (!signature(method, signature)) {
            return false;
        }
        if (production) {
            return reference.equals(method.getName());
        }

        final var parameters = method.getParameterTypes();
        final var unmap = new String[method.getParameterCount()];
        for (int i = 0; i < unmap.length; i++) {
            unmap[i] = unmap(parameters[i]);
        }

        return resolver.mapMethodName(namespace,
                        unmapClassName(method.getDeclaringClass()),
                        reference,
                        '(' + String.join("", unmap) + ')' + unmap(method.getReturnType()))
                .equals(method.getName());
    }

    private static boolean signature(Method method, MethodType signature) {
        if (signature.parameterCount() != method.getParameterCount()) {
            return false;
        }
        if (!signature.returnType().equals(method.getReturnType())) {
            return false;
        }
        final var parameters = method.getParameterTypes();
        for (int i = 0; i < signature.parameterCount(); i++) {
            if (!parameters[i].isAssignableFrom(signature.parameterType(i))) {
                return false;
            }
        }
        return true;
    }

    private static MethodHandle virtual(MethodType signature, Class<?> clazz, String reference) throws IllegalAccessException {
        for (final var method : clazz.getMethods()) {
            if (Modifier.isStatic(method.getModifiers())) {
                continue;
            }
            if (!virtual(method, reference, signature)) {
                continue;
            }
            return lookup.unreflect(method);
        }
        return null;
    }

    public static MethodHandle virtual(Class<?> clazz, String reference, MethodType signature) {
        try {
            final var method = virtual(signature, clazz, reference);
            if (method != null) {
                return method;
            }
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
        throw new AssertionError(clazz + " has no such method: " + reference + signature);
    }

    public static MethodHandle virtual(Class<?> clazz, MethodType signature, String... reference) {
        try {
            for (final var i : reference) {
                final var method = virtual(signature, clazz, i);
                if (method != null) {
                    return method;
                }
            }
        } catch (IllegalAccessException e) {
            throw new AssertionError(e);
        }
        throw new AssertionError(clazz + " has no such method: " + Arrays.toString(reference) + signature);
    }
}
