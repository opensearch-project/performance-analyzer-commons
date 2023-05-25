/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats.metrics;

/** The framework supports 3 stat metric types: Counters, StatsData and Latencies. */
public enum StatsType {
    COUNTERS("Counters"),
    STATS_DATA("StatsData"),
    LATENCIES("Latencies");

    private final String fieldValue;

    StatsType(String fieldValue) {
        this.fieldValue = fieldValue;
    }

    @Override
    public String toString() {
        return fieldValue;
    }
}
