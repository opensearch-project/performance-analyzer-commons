/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats.metrics;

import java.util.Collections;
import java.util.List;
import org.opensearch.performanceanalyzer.commons.stats.eval.Statistics;
import org.opensearch.performanceanalyzer.commons.stats.measurements.MeasurementSet;

/**
 * Aggregate metrics across RCA Graph nodes tracking failure in various staged of RCA Graph
 * Execution. Only add errors and exceptions which require {@link Statistics#NAMED_COUNTERS},
 * everything else goes to Counters maintained at {@link StatExceptionCode}.
 */
public enum ExceptionsAndErrors implements MeasurementSet {

    /** Exception thrown in operate() method, implemented by each RCA Graph node */
    EXCEPTION_IN_OPERATE("ExceptionInOperate", "namedCount", Statistics.NAMED_COUNTERS),

    /** Exception thrown in compute() method in publisher. */
    EXCEPTION_IN_COMPUTE("ExceptionInCompute", "namedCount", Statistics.NAMED_COUNTERS),

    /** When calling the MetricsDB API throws an exception. */
    EXCEPTION_IN_GATHER("ExceptionInGather", "namedCount", Statistics.NAMED_COUNTERS),

    /** Exception thrown when persisting action or flowunit when unable to write to DB */
    EXCEPTION_IN_PERSIST("ExceptionInPersist", "namedCount", Statistics.NAMED_COUNTERS);

    /** What we want to appear as the metric name. */
    private String name;

    /**
     * The unit the measurement is in. This is not used for the statistics calculations but as an
     * information that will be dumped with the metrics.
     */
    private String unit;

    /** The type of the measurement, refer {@link StatsType} */
    private StatsType statsType;

    /**
     * Multiple statistics can be collected for each measurement like MAX, MIN and MEAN. This is a
     * collection of one or more such statistics.
     */
    private List<Statistics> statsList;

    ExceptionsAndErrors(String name, String unit, Statistics stats) {
        this.name = name;
        this.unit = unit;
        this.statsType = StatsType.STATS_DATA;
        this.statsList = Collections.singletonList(stats);
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
