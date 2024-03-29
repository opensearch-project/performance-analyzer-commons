/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.config.overrides;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;

/** Class that helps with operations concerning {@link ConfigOverrides}s */
public class ConfigOverridesHelper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Serializes a {@link ConfigOverrides} instance to its JSON representation.
     *
     * @param overrides The {@link ConfigOverrides} instance.
     * @return String in JSON format representing the serialized equivalent.
     * @throws IOException if conversion runs into an IOException.
     */
    public static synchronized String serialize(final ConfigOverrides overrides)
            throws IOException {
        // We can't use a local variable to set the exception generated inside the lambda as the
        // local variable is not effectively final(because we'll end up mutating the reference).
        // In order to fish the exception out, we need to create a wrapper and set the exception
        // there instead for the caller to get the value.
        final IOException[] exception = new IOException[1];
        final String serializedOverrides =
                AccessController.doPrivileged(
                        (PrivilegedAction<String>)
                                () -> {
                                    try {
                                        return MAPPER.writeValueAsString(overrides);
                                    } catch (IOException e) {
                                        exception[0] = e;
                                    }
                                    return "";
                                });

        if (serializedOverrides.isEmpty() && exception[0] != null) {
            throw exception[0];
        }

        return serializedOverrides;
    }

    /**
     * Deserializes a JSON representation of the config overrides into a {@link ConfigOverrides}
     * instance.
     *
     * @param overrides The JSON string representing config overrides.
     * @return A {@link ConfigOverrides} instance if the JSON is valid.
     * @throws IOException if conversion runs into an IOException.
     */
    public static synchronized ConfigOverrides deserialize(final String overrides)
            throws IOException {
        // We can't use a local variable to set the exception generated inside the lambda as the
        // local variable is not effectively final(because we'll end up mutating the reference).
        // In order to fish the exception out, we need to create a wrapper and set the exception
        // there instead for the caller to get the value.
        final IOException[] exception = new IOException[1];
        final ConfigOverrides configOverrides =
                AccessController.doPrivileged(
                        (PrivilegedAction<ConfigOverrides>)
                                () -> {
                                    try {
                                        return MAPPER.readValue(overrides, ConfigOverrides.class);
                                    } catch (IOException ioe) {
                                        exception[0] = ioe;
                                    }
                                    return null;
                                });

        if (configOverrides == null && exception[0] != null) {
            // re throw the exception that was consumed while deserializing.
            throw exception[0];
        }

        return configOverrides;
    }
}
