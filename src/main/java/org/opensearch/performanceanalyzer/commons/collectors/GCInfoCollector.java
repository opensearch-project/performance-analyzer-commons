/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;


import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import java.util.function.Supplier;
import org.opensearch.performanceanalyzer.commons.jvm.GarbageCollectorInfo;
import org.opensearch.performanceanalyzer.commons.metrics.AllMetrics.GCInfoDimension;
import org.opensearch.performanceanalyzer.commons.metrics.MetricsProcessor;
import org.opensearch.performanceanalyzer.commons.metrics.PerformanceAnalyzerMetrics;
import org.opensearch.performanceanalyzer.commons.stats.CommonStats;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatExceptionCode;
import org.opensearch.performanceanalyzer.commons.stats.metrics.WriterMetrics;

/**
 * A collector that collects info about the current garbage collectors for various regions in the
 * heap.
 */
public class GCInfoCollector extends PerformanceAnalyzerMetricsCollector
        implements MetricsProcessor {

    private static final int EXPECTED_KEYS_PATH_LENGTH = 0;

    public GCInfoCollector(String name, int samplingIntervalMillis) {
        super(
                samplingIntervalMillis,
                name,
                WriterMetrics.GC_INFO_COLLECTOR_EXECUTION_TIME,
                StatExceptionCode.GC_INFO_COLLECTOR_ERROR);
    }

    @Override
    public void collectMetrics(long startTime) {
        long mCurrT = System.currentTimeMillis();
        // Zero the string builder
        value.setLength(0);

        // first line is the timestamp
        value.append(PerformanceAnalyzerMetrics.getJsonCurrentMilliSeconds())
                .append(PerformanceAnalyzerMetrics.sMetricNewLineDelimitor);

        for (Map.Entry<String, Supplier<String>> entry :
                GarbageCollectorInfo.getGcSuppliers().entrySet()) {
            value.append(new GCInfo(entry.getKey(), entry.getValue().get()).serialize())
                    .append(PerformanceAnalyzerMetrics.sMetricNewLineDelimitor);
        }

        saveMetricValues(value.toString(), startTime);
        CommonStats.WRITER_METRICS_AGGREGATOR.updateStat(
                WriterMetrics.GC_INFO_COLLECTOR_EXECUTION_TIME,
                "",
                System.currentTimeMillis() - mCurrT);
    }

    @Override
    public String getMetricsPath(long startTime, String... keysPath) {
        if (keysPath != null && keysPath.length != EXPECTED_KEYS_PATH_LENGTH) {
            throw new RuntimeException("keys length should be " + EXPECTED_KEYS_PATH_LENGTH);
        }

        return PerformanceAnalyzerMetrics.generatePath(
                startTime, PerformanceAnalyzerMetrics.sGcInfoPath);
    }

    public static class GCInfo extends MetricStatus {
        private String memoryPool;
        private String collectorName;

        public GCInfo() {}

        public GCInfo(final String memoryPool, final String collectorName) {
            this.memoryPool = memoryPool;
            this.collectorName = collectorName;
        }

        @JsonProperty(GCInfoDimension.Constants.MEMORY_POOL_VALUE)
        public String getMemoryPool() {
            return memoryPool;
        }

        @JsonProperty(GCInfoDimension.Constants.COLLECTOR_NAME_VALUE)
        public String getCollectorName() {
            return collectorName;
        }
    }
}