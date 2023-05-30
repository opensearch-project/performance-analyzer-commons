/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats.metrics;


import java.util.Collections;
import java.util.List;
import org.opensearch.performanceanalyzer.commons.stats.eval.Statistics;
import org.opensearch.performanceanalyzer.commons.stats.measurements.MeasurementSet;

public enum StatMetrics implements MeasurementSet {
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
    DISKS_COLLECTOR_EXECUTION_TIME(
            "DisksCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    GC_INFO_COLLECTOR_EXECUTION_TIME(
            "GCInfoCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    HEAP_METRICS_COLLECTOR_EXECUTION_TIME(
            "HeapMetricsCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    MOUNTED_PARTITION_METRICS_COLLECTOR_EXECUTION_TIME(
            "MountedPartitionMetricsCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    NETWORK_E2E_COLLECTOR_EXECUTION_TIME(
            "NetworkE2ECollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    NETWORK_INTERFACE_COLLECTOR_EXECUTION_TIME(
            "NetworkInterfaceCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    OS_METRICS_COLLECTOR_EXECUTION_TIME(
            "OSMetricsCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    STAT_COLLECTOR_EXECUTION_TIME(
            "StatCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),

    // TODO: These are part of former PACollectorMetrics and should be moved in future
    ADMISSION_CONTROL_COLLECTOR_EXECUTION_TIME(
            "AdmissionControlCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    CACHE_CONFIG_METRICS_COLLECTOR_EXECUTION_TIME(
            "CacheConfigMetricsCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    CIRCUIT_BREAKER_COLLECTOR_EXECUTION_TIME(
            "CircuitBreakerCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    CLUSTER_APPLIER_SERVICE_STATS_COLLECTOR_EXECUTION_TIME(
            "ClusterApplierServiceStatsCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    CLUSTER_MANAGER_SERVICE_EVENTS_METRICS_COLLECTOR_EXECUTION_TIME(
            "ClusterManagerServiceEventsMetricsCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    CLUSTER_MANAGER_SERVICE_METRICS_COLLECTOR_EXECUTION_TIME(
            "ClusterManagerServiceMetricsCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    CLUSTER_MANAGER_THROTTLING_COLLECTOR_EXECUTION_TIME(
            "ClusterManagerThrottlingCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    ELECTION_TERM_COLLECTOR_EXECUTION_TIME(
            "ElectionTermCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    FAULT_DETECTION_COLLECTOR_EXECUTION_TIME(
            "FaultDetectionCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    NODE_DETAILS_COLLECTOR_EXECUTION_TIME(
            "NodeDetailsCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    NODE_STATS_ALL_SHARDS_METRICS_COLLECTOR_EXECUTION_TIME(
            "NodeStatsAllShardsMetricsCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    NODE_STATS_FIXED_SHARDS_METRICS_COLLECTOR_EXECUTION_TIME(
            "NodeStatsFixedShardsMetricsCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    SHARD_INDEXING_PRESSURE_COLLECTOR_EXECUTION_TIME(
            "ShardIndexingPressureCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),
    SHARD_STATE_COLLECTOR_EXECUTION_TIME(
            "ShardStateCollectorExecutionTime", "millis", StatsType.LATENCIES, Statistics.SUM),
    THREADPOOL_METRICS_COLLECTOR_EXECUTION_TIME(
            "ThreadPoolMetricsCollectorExecutionTime",
            "millis",
            StatsType.LATENCIES,
            Statistics.SUM),

    /** Tracks collector specific metrics - available/enabled/disabled and other params */
    ADMISSION_CONTROL_COLLECTOR_NOT_AVAILABLE("AdmissionControlCollectorNotAvailable"),

    CLUSTER_MANAGER_THROTTLING_COLLECTOR_NOT_AVAILABLE(
            "ClusterManagerThrottlingCollectorNotAvailable");
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

    StatMetrics(String name) {
        this(name, "count", StatsType.STATS_DATA, Collections.singletonList(Statistics.COUNT));
    }

    StatMetrics(String name, String unit, StatsType statsType, Statistics stats) {
        this(name, unit, statsType, Collections.singletonList(stats));
    }

    StatMetrics(String name, String unit, StatsType statsType, List<Statistics> stats) {
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
