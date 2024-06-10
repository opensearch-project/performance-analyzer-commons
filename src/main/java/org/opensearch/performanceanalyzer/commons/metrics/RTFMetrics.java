/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.metrics;

import org.jooq.Field;
import org.jooq.impl.DSL;
import org.opensearch.performanceanalyzer.commons.stats.JooqFieldValue;

/**
 * We use open telemetry semantic conventions:
 * https://opentelemetry.io/docs/specs/semconv/system/system-metrics/
 */
public class RTFMetrics {

    // contents of metrics
    public enum GCType {
        TOT_YOUNG_GC(Constants.TOT_YOUNG_GC_VALUE),
        TOT_FULL_GC(Constants.TOT_FULL_GC_VALUE),
        SURVIVOR(Constants.SURVIVOR_VALUE),
        PERM_GEN(Constants.PERM_GEN_VALUE),
        OLD_GEN(Constants.OLD_GEN_VALUE),
        EDEN(Constants.EDEN_VALUE),
        NON_HEAP(Constants.NON_HEAP_VALUE),
        HEAP(Constants.HEAP_VALUE);

        private final String value;

        GCType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static class Constants {
            public static final String TOT_YOUNG_GC_VALUE = "total_young_gc";
            public static final String TOT_FULL_GC_VALUE = "total_full_gc";
            public static final String SURVIVOR_VALUE = "survivor";
            public static final String PERM_GEN_VALUE = "perm_gen";
            public static final String OLD_GEN_VALUE = "old_gen";
            public static final String EDEN_VALUE = "eden";
            public static final String NON_HEAP_VALUE = "non_heap";
            public static final String HEAP_VALUE = "heap";
        }
    }

    public enum HeapValue implements MetricValue {
        GC_COLLECTION_EVENT(Constants.COLLECTION_COUNT_VALUE),
        GC_COLLECTION_TIME(Constants.COLLECTION_TIME_VALUE),
        HEAP_COMMITTED(Constants.COMMITTED_VALUE),
        HEAP_INIT(Constants.INIT_VALUE),
        HEAP_MAX(Constants.MAX_VALUE),
        HEAP_USED(Constants.USED_VALUE);

        private final String value;

        HeapValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static class Constants {
            public static final String COLLECTION_COUNT_VALUE = "gc_collection_event";

            public static final String COLLECTION_TIME_VALUE = "gc_collection_time";

            public static final String COMMITTED_VALUE = "heap_committed";

            public static final String INIT_VALUE = "heap_init";

            public static final String MAX_VALUE = "heap_max";

            public static final String USED_VALUE = "heap_used";
        }
    }

    public enum DiskValue implements MetricValue {
        DISK_UTILIZATION(Constants.UTIL_VALUE),
        DISK_WAITTIME(Constants.WAIT_VALUE),
        DISK_SERVICE_RATE(Constants.SRATE_VALUE);

        private final String value;

        DiskValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static class Constants {
            public static final String UTIL_VALUE = "disk_utilization";

            public static final String WAIT_VALUE = "disk_wait_time";

            public static final String SRATE_VALUE = "disk_service_rate";
        }
    }

    public enum ThreadPoolValue implements MetricValue {
        THREADPOOL_QUEUE_SIZE(Constants.QUEUE_SIZE_VALUE),
        THREADPOOL_REJECTED_REQS(Constants.REJECTED_VALUE),
        THREADPOOL_TOTAL_THREADS(Constants.THREADS_COUNT_VALUE),
        THREADPOOL_ACTIVE_THREADS(Constants.THREADS_ACTIVE_VALUE),
        THREADPOOL_QUEUE_LATENCY(Constants.QUEUE_LATENCY_VALUE),
        THREADPOOL_QUEUE_CAPACITY(Constants.QUEUE_CAPACITY_VALUE);

        private final String value;

        ThreadPoolValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static class Constants {
            public static final String QUEUE_SIZE_VALUE = "threadpool_queue_size";
            public static final String REJECTED_VALUE = "threadpool_rejected_reqs";
            public static final String THREADS_COUNT_VALUE = "threadpool_total_threads";
            public static final String THREADS_ACTIVE_VALUE = "threadpool_active_threads";
            public static final String QUEUE_LATENCY_VALUE = "threadpool_queue_latency";
            public static final String QUEUE_CAPACITY_VALUE = "threadpool_queue_capacity";
        }
    }

    public enum ShardStatsValue implements MetricValue {
        INDEXING_THROTTLE_TIME(Constants.INDEXING_THROTTLE_TIME_VALUE),
        CACHE_QUERY_HIT(Constants.QUEY_CACHE_HIT_COUNT_VALUE),
        CACHE_QUERY_MISS(Constants.QUERY_CACHE_MISS_COUNT_VALUE),
        CACHE_QUERY_SIZE(Constants.QUERY_CACHE_IN_BYTES_VALUE),
        CACHE_FIELDDATA_EVICTION(Constants.FIELDDATA_EVICTION_VALUE),
        CACHE_FIELDDATA_SIZE(Constants.FIELD_DATA_IN_BYTES_VALUE),
        CACHE_REQUEST_HIT(Constants.REQUEST_CACHE_HIT_COUNT_VALUE),
        CACHE_REQUEST_MISS(Constants.REQUEST_CACHE_MISS_COUNT_VALUE),
        CACHE_REQUEST_EVICTION(Constants.REQUEST_CACHE_EVICTION_VALUE),
        CACHE_REQUEST_SIZE(Constants.REQUEST_CACHE_IN_BYTES_VALUE);
        private final String value;

        ShardStatsValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static class Constants {
            public static final String INDEXING_THROTTLE_TIME_VALUE = "indexing_throttle_time";

            public static final String QUEY_CACHE_HIT_COUNT_VALUE = "cache_query_hit";

            public static final String QUERY_CACHE_MISS_COUNT_VALUE = "cache_query_miss";

            public static final String QUERY_CACHE_IN_BYTES_VALUE = "cache_query_size";

            public static final String FIELDDATA_EVICTION_VALUE = "cache_field_data_eviction";

            public static final String FIELD_DATA_IN_BYTES_VALUE = "cache_field_data_size";

            public static final String REQUEST_CACHE_HIT_COUNT_VALUE = "cache_request_hit";

            public static final String REQUEST_CACHE_MISS_COUNT_VALUE = "cache_request_miss";

            public static final String REQUEST_CACHE_EVICTION_VALUE = "cache_request_eviction";

            public static final String REQUEST_CACHE_IN_BYTES_VALUE = "cache_request_size";
        }
    }

    public enum HeapDimension implements MetricDimension, JooqFieldValue {
        MEM_TYPE(Constants.TYPE_VALUE);

        private final String value;

        HeapDimension(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        @Override
        public Field<String> getField() {
            return DSL.field(DSL.name(this.value), String.class);
        }

        @Override
        public String getName() {
            return value;
        }

        public static class Constants {
            public static final String TYPE_VALUE = "mem_type";
        }
    }

    public enum DiskDimension implements MetricDimension {
        DISK_NAME(Constants.NAME_VALUE);

        private final String value;

        DiskDimension(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static class Constants {
            public static final String NAME_VALUE = "disk_name";
        }
    }

    public enum CommonDimension implements MetricDimension {
        INDEX_NAME(Constants.INDEX_NAME_VALUE),
        OPERATION(Constants.OPERATION_VALUE),
        SHARD_ROLE(Constants.SHARD_ROLE_VALUE),
        SHARD_ID(Constants.SHARD_ID_VALUE),
        EXCEPTION(Constants.EXCEPTION_VALUE),
        THREAD_NAME(Constants.THREAD_NAME),
        FAILED(Constants.FAILED_VALUE);

        private final String value;

        CommonDimension(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static class Constants {
            public static final String INDEX_NAME_VALUE = "index_name";
            public static final String SHARD_ID_VALUE = "shard_id";
            public static final String OPERATION_VALUE = "operation";
            public static final String SHARD_ROLE_VALUE = "shard_role";
            public static final String EXCEPTION_VALUE = "exception";
            public static final String FAILED_VALUE = "failed";
            public static final String THREAD_NAME = "thread_name";
        }
    }

    public enum ThreadPoolDimension implements MetricDimension, JooqFieldValue {
        THREAD_POOL_TYPE(Constants.TYPE_VALUE);

        private final String value;

        ThreadPoolDimension(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        @Override
        public Field<String> getField() {
            return DSL.field(DSL.name(this.value), String.class);
        }

        @Override
        public String getName() {
            return value;
        }

        public static class Constants {
            public static final String TYPE_VALUE = "threadpool_type";
        }
    }

    public enum MetricUnits {
        CORES(Constants.CORES_VALUE),
        COUNT_PER_SEC(Constants.COUNT_PER_SEC_VALUE),
        COUNT(Constants.COUNT_VALUE),
        PAGES(Constants.PAGES_VALUE),
        SEC_PER_CONTEXT_SWITCH(Constants.SEC_PER_CONTEXT_SWITCH_VALUE),
        BYTE_PER_SEC(Constants.BYTE_PER_SEC_VALUE),
        SEC_PER_EVENT(Constants.SEC_PER_EVENT_VALUE),
        MILLISECOND(Constants.MILLISECOND_VALUE),
        BYTE(Constants.BYTE_VALUE),
        PERCENT(Constants.PERCENT_VALUE),
        MEGABYTE_PER_SEC(Constants.MEGABYTE_PER_SEC_VALUE),
        SEGMENT_PER_FLOW(Constants.SEGMENT_PER_FLOW_VALUE),
        BYTE_PER_FLOW(Constants.BYTE_PER_FLOW_VALUE),
        PACKET_PER_SEC(Constants.PACKET_PER_SEC_VALUE);

        private final String value;

        MetricUnits(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return value;
        }

        public static class Constants {
            public static final String CORES_VALUE = "cores";
            public static final String COUNT_PER_SEC_VALUE = "count/s";
            public static final String COUNT_VALUE = "count";
            public static final String PAGES_VALUE = "pages";
            public static final String SEC_PER_CONTEXT_SWITCH_VALUE = "s/ctxswitch";
            public static final String BYTE_PER_SEC_VALUE = "B/s";
            public static final String SEC_PER_EVENT_VALUE = "s/event";
            public static final String MILLISECOND_VALUE = "ms";
            public static final String BYTE_VALUE = "B";
            public static final String PERCENT_VALUE = "%";
            public static final String MEGABYTE_PER_SEC_VALUE = "MB/s";
            public static final String SEGMENT_PER_FLOW_VALUE = "segments/flow";
            public static final String BYTE_PER_FLOW_VALUE = "B/flow";
            public static final String PACKET_PER_SEC_VALUE = "packets/s";
        }
    }
}
