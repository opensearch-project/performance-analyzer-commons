/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats.metrics;

import java.util.Collections;
import java.util.List;
import org.opensearch.performanceanalyzer.commons.stats.eval.Statistics;
import org.opensearch.performanceanalyzer.commons.stats.measurements.MeasurementSet;

public enum CollectorMetrics implements MeasurementSet {
    /** Measures the time spent in deleting the event log files */
    EVENT_LOG_FILES_DELETION_TIME(
            "EventLogFilesDeletionTime", "millis", StatsType.LATENCIES, Statistics.SUM),

    /** Measures the count of event log files deleted */
    EVENT_LOG_FILES_DELETED("EventLogFilesDeleted"),

    /**
     * Successfully completed a thread-dump. An omission of indicate thread taking the dump got
     * stuck.
     */
    JVM_THREAD_DUMP_SUCCESSFUL("JvmThreadDumpSuccessful"),

    /** Tracks the number of muted collectors */
    COLLECTORS_MUTED(
            "CollectorsMutedCount",
            "namedCount",
            StatsType.STATS_DATA,
            Collections.singletonList(Statistics.NAMED_COUNTERS)),
    COLLECTORS_SKIPPED(
            "CollectorSkippedCount",
            "namedCount",
            StatsType.STATS_DATA,
            Collections.singletonList(Statistics.NAMED_COUNTERS)),
    COLLECTORS_SLOW(
            "CollectorSlowCount",
            "namedCount",
            StatsType.STATS_DATA,
            Collections.singletonList(Statistics.NAMED_COUNTERS)),

    /** Tracks time taken by respective collectors to collect event metrics. */
    OS_METRICS_COLLECTOR_EXECUTION_TIME(
            "OSMetricsCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    HEAP_METRICS_COLLECTOR_EXECUTION_TIME(
            "HeapMetricsCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    MOUNTED_PARTITION_METRICS_COLLECTOR_EXECUTION_TIME(
            "MountedPartitionMetricsCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    DISKS_COLLECTOR_EXECUTION_TIME(
            "DisksCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    NETWORK_E2E_COLLECTOR_EXECUTION_TIME(
            "NetworkE2ECollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    NETWORK_INTERFACE_COLLECTOR_EXECUTION_TIME(
            "NetworkInterfaceCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    GC_INFO_COLLECTOR_EXECUTION_TIME(
            "GCInfoCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    STAT_COLLECTOR_EXECUTION_TIME(
            "StatCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM);

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

    CollectorMetrics(String name) {
        this(name, "count", StatsType.STATS_DATA, Collections.singletonList(Statistics.COUNT));
    }

    CollectorMetrics(String name, String unit, StatsType statsType, Statistics stats) {
        this(name, unit, statsType, Collections.singletonList(stats));
    }

    CollectorMetrics(String name, String unit, StatsType statsType, List<Statistics> stats) {
        this.name = name;
        this.unit = unit;
        this.statsType = statsType;
        this.statsList = stats;
    }

    public String toString() {
        return new StringBuilder(name).append("-").append(unit).toString();
    }

    @Override
    public StatsType getStatsType() {
        return statsType;
    }

    @Override
    public List<Statistics> getStatsList() {
        return statsList;
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
