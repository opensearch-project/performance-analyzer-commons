/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.metrics.stats.measurements;

import java.util.Arrays;
import java.util.List;
import org.opensearch.performanceanalyzer.commons.stats.eval.Statistics;
import org.opensearch.performanceanalyzer.commons.stats.measurements.MeasurementSet;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatsType;

public enum MeasurementSetTestHelper implements MeasurementSet {
    TEST_MEASUREMENT1(
            "TestMeasurement1",
            "millis",
            StatsType.LATENCIES,
            Arrays.asList(Statistics.MAX, Statistics.MEAN, Statistics.MIN)),
    TEST_MEASUREMENT2(
            "TestMeasurement2", "millis", StatsType.LATENCIES, Arrays.asList(Statistics.COUNT)),
    TEST_MEASUREMENT3(
            "TestMeasurement3", "millis", StatsType.LATENCIES, Arrays.asList(Statistics.COUNT)),
    TEST_MEASUREMENT4(
            "TestMeasurement4", "millis", StatsType.LATENCIES, Arrays.asList(Statistics.SAMPLE)),
    TEST_MEASUREMENT5(
            "TestMeasurement5", "millis", StatsType.LATENCIES, Arrays.asList(Statistics.SUM)),
    TEST_MEASUREMENT6(
            "TestMeasurement6",
            "millis",
            StatsType.LATENCIES,
            Arrays.asList(Statistics.NAMED_COUNTERS)),
    JVM_FREE_MEM_SAMPLER(
            "jvmFreeMemorySampler",
            "bytes",
            StatsType.STATS_DATA,
            Arrays.asList(Statistics.SAMPLE));

    private String name;
    private String unit;
    private StatsType statsType;
    private List<Statistics> statsList;

    MeasurementSetTestHelper(
            String name, String unit, StatsType statsType, List<Statistics> statisticList) {
        this.name = name;
        this.unit = unit;
        this.statsType = statsType;
        this.statsList = statisticList;
    }

    public String toString() {
        return new StringBuilder(name).append("-").append(unit).toString();
    }

    @Override
    public List<Statistics> getStatsList() {
        return statsList;
    }

    @Override
    public StatsType getStatsType() {
        return statsType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUnit() {
        return unit;
    }
}
