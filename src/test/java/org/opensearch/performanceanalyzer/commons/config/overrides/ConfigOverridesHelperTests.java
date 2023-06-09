/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.config.overrides;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import org.junit.Before;
import org.junit.Test;
import org.opensearch.performanceanalyzer.commons.util.JsonConverter;

public class ConfigOverridesHelperTests {
    private ConfigOverridesWrapper testConfigOverridesWrapper;
    private final ConfigOverrides validTestOverrides = buildValidConfigOverrides();
    private final String validTestOverrideJson =
            JsonConverter.writeValueAsString(validTestOverrides);

    @Before
    public void setUp() {
        testConfigOverridesWrapper = new ConfigOverridesWrapper();
        testConfigOverridesWrapper.setCurrentClusterConfigOverrides(validTestOverrides);
    }

    @Test
    public void testSerializeSuccess() throws IOException {
        String serializedOverrides = ConfigOverridesHelper.serialize(validTestOverrides);

        assertEquals(validTestOverrideJson, serializedOverrides);
    }

    @Test
    public void testDeserializeSuccess() throws IOException {
        ConfigOverrides deserializedOverrides =
                ConfigOverridesHelper.deserialize(validTestOverrideJson);

        assertEquals(
                validTestOverrides.getEnable().getRcas(),
                deserializedOverrides.getEnable().getRcas());
        assertEquals(
                validTestOverrides.getEnable().getDeciders(),
                deserializedOverrides.getEnable().getDeciders());
        assertEquals(
                validTestOverrides.getEnable().getActions(),
                deserializedOverrides.getEnable().getActions());
        assertEquals(
                validTestOverrides.getEnable().getCollectors(),
                deserializedOverrides.getEnable().getCollectors());

        assertEquals(
                validTestOverrides.getDisable().getRcas(),
                deserializedOverrides.getDisable().getRcas());
        assertEquals(
                validTestOverrides.getDisable().getDeciders(),
                deserializedOverrides.getDisable().getDeciders());
        assertEquals(
                validTestOverrides.getDisable().getActions(),
                deserializedOverrides.getDisable().getActions());
        assertEquals(
                validTestOverrides.getDisable().getCollectors(),
                deserializedOverrides.getDisable().getCollectors());
    }

    @Test(expected = IOException.class)
    public void testDeserializeIOException() throws IOException {
        String nonJsonString = "Not a JSON string.";
        ConfigOverridesHelper.deserialize(nonJsonString);
    }

    private ConfigOverrides buildValidConfigOverrides() {
        ConfigOverrides overrides = new ConfigOverrides();
        overrides.getDisable().setRcas(Arrays.asList("rca1", "rca2"));
        overrides.getDisable().setActions(Arrays.asList("action1", "action2"));
        overrides.getEnable().setRcas(Arrays.asList("rca3", "rca4"));
        overrides.getEnable().setDeciders(Collections.singletonList("decider1"));
        overrides.getDisable().setCollectors(Collections.singletonList("collector1"));

        return overrides;
    }
}
