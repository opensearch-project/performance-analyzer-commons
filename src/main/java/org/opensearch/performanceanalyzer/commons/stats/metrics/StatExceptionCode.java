/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats.metrics;

/**
 * StatExceptionCode defines counters for various points of failure in the framework.
 *
 * <p>Note: The framework also provides the specialized 'Named Counters' at {@link
 * ExceptionsAndErrors}. Named Counter take a key with it, and reports the count of occurrences for
 * each key.
 */
public enum StatExceptionCode {
    TOTAL_ERROR("TotalError"),

    /** java_pid file is missing. */
    JVM_ATTACH_ERROR_JAVA_PID_FILE_MISSING("JvmAttachErrorJavaPidFileMissing"),

    /** Tracks the number of VM attach/dataDump or detach failures. */
    JVM_ATTACH_ERROR("JvmAttachErrror"),

    /** Lock could not be acquired within the timeout. */
    JVM_ATTACH_LOCK_ACQUISITION_FAILED("JvmAttachLockAcquisitionFailed"),

    /** ThreadState could not be found for an OpenSearch thread in the critical OpenSearch path. */
    NO_THREAD_STATE_INFO("NoThreadStateInfo"),

    /** Thread ID is no loner exists */
    JVM_THREAD_ID_NO_LONGER_EXISTS("JVMThreadIdNoLongerExists"),

    /**
     * We start 6 threads within RCA Agent. Below metrics track count of thread started and ended.
     *
     * <p>Note: The 'PA' in metricName is confusing, it is meant to imply threads started within RCA
     * Agent.
     */
    NUM_PA_THREADS_STARTED("NumberOfPAThreadsStarted"),
    NUM_PA_THREADS_ENDED("NumberOfPAThreadsEnded"),

    /** For each thread, we add a respective 'threadExceptionCode' metric. */
    READER_THREAD_STOPPED("ReaderThreadStopped"),
    ERROR_HANDLER_THREAD_STOPPED("ErrorHandlerThreadStopped"),
    GRPC_SERVER_THREAD_STOPPED("GRPCServerThreadStopped"),
    WEB_SERVER_THREAD_STOPPED("WebServerThreadStopped"),
    RCA_CONTROLLER_THREAD_STOPPED("RcaControllerThreadStopped"),
    RCA_SCHEDULER_THREAD_STOPPED("RcaSchedulerThreadStopped"),

    MUTE_ERROR("MuteError"),
    REQUEST_REMOTE_ERROR("RequestRemoteError"),
    CONFIG_DIR_NOT_FOUND("ConfigDirectoryNotFound"),
    CONFIG_OVERRIDES_SER_FAILED("ConfigOverridesSerFailed"),
    WRITE_UPDATED_RCA_CONF_ERROR("WriteUpdatedRcaConfError"),

    /** Tracks stale metrics - metrics to be collected is behind current bucket */
    STALE_METRICS("StaleMetrics"),

    /** This metric indicates faiure in intercepting opensearch requests at transport channel */
    OPENSEARCH_REQUEST_INTERCEPTOR_ERROR("OpenSearchRequestInterceptorError"),

    /** Below tracks Metrics specific Errors. */
    THREAD_IO_ERROR("ThreadIOError"),
    SCHEMA_PARSER_ERROR("SchemaParserError"),
    JSON_PARSER_ERROR("JsonParserError"),
    REQUEST_ERROR("RequestError"),
    MISCONFIGURED_OLD_GEN_RCA_HEAP_MAX_MISSING("MisconfiguredOldGenRcaHeapMaxMissing"),
    MISCONFIGURED_OLD_GEN_RCA_HEAP_USED_MISSING("MisconfiguredOldGenRcaHeapUsedMissing"),
    MISCONFIGURED_OLD_GEN_RCA_GC_EVENTS_MISSING("MisconfiguredOldGenRcaGcEventsMissing"),
    TOTAL_MEM_READ_ERROR("TotalMemReadError"),

    /** Below tracks Collector specific Errors. */
    DISK_METRICS_COLLECTOR_ERROR("DiskMetricsError"),
    RTF_DISK_METRICS_COLLECTOR_ERROR("RTFDiskMetricsError"),
    GC_INFO_COLLECTOR_ERROR("GCInfoCollectorError"),
    HEAP_METRICS_COLLECTOR_ERROR("HeapMetricsCollectorError"),
    RTF_HEAP_METRICS_COLLECTOR_ERROR("RTFHeapMetricsCollectorError"),
    RTF_SHARD_OPERATION_COLLECTOR_ERROR("RTFShardOperationCollectorError"),
    MOUNTED_PARTITION_METRICS_COLLECTOR_ERROR("MountedPartitionMetricsCollectorError"),
    NETWORK_COLLECTION_ERROR("NetworkCollectionError"),
    OS_METRICS_COLLECTOR_ERROR("OSMetricsCollectorError"),
    STATS_COLLECTOR_ERROR("StatsCollectorError"),
    THREADPOOL_METRICS_COLLECTOR_ERROR("ThreadPoolMetricsCollectorError"),
    RTF_THREADPOOL_METRICS_COLLECTOR_ERROR("RTFThreadPoolMetricsCollectorError"),
    SHARD_STATE_COLLECTOR_ERROR("ShardStateCollectorError"),
    CACHE_CONFIG_METRICS_COLLECTOR_ERROR("CacheConfigMetricsCollectorError"),
    RTF_CACHE_CONFIG_METRICS_COLLECTOR_ERROR("RTFCacheConfigMetricsCollectorError"),
    ADMISSION_CONTROL_COLLECTOR_ERROR("AdmissionControlCollectorError"),
    CIRCUIT_BREAKER_COLLECTOR_ERROR("CircuitBreakerCollectorError"),
    CLUSTER_MANAGER_SERVICE_EVENTS_METRICS_COLLECTOR_ERROR(
            "ClusterManagerServiceEventsMetricsCollectorError"),
    CLUSTER_MANAGER_SERVICE_METRICS_COLLECTOR_ERROR("ClusterManagerServiceMetricsCollectorError"),
    CLUSTER_MANAGER_THROTTLING_COLLECTOR_ERROR("ClusterManagerThrottlingMetricsCollectorError"),
    FAULT_DETECTION_COLLECTOR_ERROR("FaultDetectionMetricsCollectorError"),
    CLUSTER_APPLIER_SERVICE_STATS_COLLECTOR_ERROR("ClusterApplierServiceStatsCollectorError"),
    ELECTION_TERM_COLLECTOR_ERROR("ElectionTermCollectorError"),
    SHARD_INDEXING_PRESSURE_COLLECTOR_ERROR("ShardIndexingPressureMetricsCollectorError"),
    NODESTATS_COLLECTION_ERROR("NodeStatsCollectionError"),
    RTF_NODESTATS_COLLECTION_ERROR("RTFNodeStatsCollectionError"),
    CLUSTER_MANAGER_NODE_NOT_UP("ClusterManagerNodeNotUp"),

    /** Below tracks Reader specific Errors. */
    READER_ERROR_PA_DISABLE_SUCCESS("ReaderErrorPADisableSuccess"),
    READER_ERROR_PA_DISABLE_FAILED("ReaderErrorPADisableFailed"),
    READER_ERROR_RCA_AGENT_STOPPED("ReaderErrorRCAAgentStopped"),
    READER_METRICSDB_ACCESS_ERRORS("ReaderMetricsdbAccessError"),
    READER_PARSER_ERROR("ReaderParserError"),
    READER_RESTART_PROCESSING("ReaderRestartProcessing"),
    READER_METRICS_PROCESSOR_ERROR("ReaderMetricsProcessorError"),

    /** This metric indicates metric entry insertion to event log queue failed */
    METRICS_WRITE_ERROR("MetricsWriteError"),

    /** This metric indicates faiure in cleaning up the event log files */
    METRICS_REMOVE_ERROR("MetricsRemoveError"),

    /** This metric indicates faiure in cleaning up the event log files */
    METRICS_REMOVE_FAILURE("MetricsRemoveFailure"),

    /** This metric indicates that the writer file creation was skipped. */
    WRITER_FILE_CREATION_SKIPPED("WriterFileCreationSkipped"),

    /** This metric indicates that error occurred while closing grpc channels. */
    GRPC_CHANNEL_CLOSURE_ERROR("GrpcChannelClosureError"),

    /** This metric indicates that error occurred while closing grpc server. */
    GRPC_SERVER_CLOSURE_ERROR("GrpcServerClosureError"),

    /** This metric indicates that error occurred while closing metrics db. */
    METRICS_DB_CLOSURE_ERROR("MetricsDbClosureError"),

    /** This metric indicates that error occurred while closing database connection. */
    IN_MEMORY_DATABASE_CONN_CLOSURE_ERROR("InMemoryDatabaseConnClosureError"),

    /** Below tracks RCA framework specific Errors. */
    RCA_NETWORK_ERROR("RcaNetworkError"),
    RCA_VERTEX_RX_BUFFER_FULL_ERROR("RcaVertexRxBufferFullError"),
    RCA_NETWORK_THREADPOOL_QUEUE_FULL_ERROR("RcaNetworkThreadpoolQueueFullError"),
    RCA_SCHEDULER_STOPPED_ERROR("RcaSchedulerStoppedError"),
    RCA_FRAMEWORK_CRASH("RcaFrameworkCrash"),
    INVALID_CONFIG_RCA_AGENT_STOPPED("InvalidConfigRCAAgentStopped"),

    /** Batch Metric relevant errors */
    BATCH_METRICS_CONFIG_ERROR("BatchMetricsConfigError"),
    BATCH_METRICS_EXCEEDED_MAX_DATAPOINTS("ExceededBatchMetricsMaxDatapoints"),
    BATCH_METRICS_HTTP_CLIENT_ERROR("BatchMetricsHttpClientError"),
    BATCH_METRICS_HTTP_HOST_ERROR("BatchMetricsHttpHostError");

    private final String value;

    StatExceptionCode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
