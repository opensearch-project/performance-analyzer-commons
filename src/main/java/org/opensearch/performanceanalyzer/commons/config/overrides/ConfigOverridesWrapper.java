/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.config.overrides;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.annotations.VisibleForTesting;

/** Class responsible for holding the latest config overrides across the cluster. */
public class ConfigOverridesWrapper {

    private volatile ConfigOverrides currentClusterConfigOverrides;
    private volatile long lastUpdatedTimestamp;
    private final ObjectMapper mapper;

    public ConfigOverridesWrapper() {
        this(new ObjectMapper());
    }

    /**
     * Ctor used only for unit test purposes.
     *
     * @param mapper The object mapper instance.
     */
    @VisibleForTesting
    public ConfigOverridesWrapper(final ObjectMapper mapper) {
        this.currentClusterConfigOverrides = new ConfigOverrides();
        this.mapper = mapper;
    }

    public ConfigOverrides getCurrentClusterConfigOverrides() {
        return currentClusterConfigOverrides;
    }

    /**
     * Sets a new ConfigOverrides instance as the current cluster config overrides instance.
     *
     * @param configOverrides the ConfigOverrides instance.
     */
    public void setCurrentClusterConfigOverrides(final ConfigOverrides configOverrides) {
        this.currentClusterConfigOverrides = configOverrides;
    }

    public long getLastUpdatedTimestamp() {
        return lastUpdatedTimestamp;
    }

    public void setLastUpdatedTimestamp(long lastUpdatedTimestamp) {
        this.lastUpdatedTimestamp = lastUpdatedTimestamp;
    }
}
