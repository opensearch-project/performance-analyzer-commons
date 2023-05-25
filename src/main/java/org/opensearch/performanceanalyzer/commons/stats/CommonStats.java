/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats;


import java.util.Arrays;
import org.opensearch.performanceanalyzer.commons.stats.collectors.SampleAggregator;
import org.opensearch.performanceanalyzer.commons.stats.metrics.WriterMetrics;

/**
 * Catalog Service Metrics class that is to be populated upon PerformanceAnalyzerApp class load and
 * to be used by both PA and RCA.
 */
public class CommonStats {
    public static SampleAggregator RCA_GRAPH_METRICS_AGGREGATOR,
            RCA_RUNTIME_METRICS_AGGREGATOR,
            RCA_VERTICES_METRICS_AGGREGATOR,
            READER_METRICS_AGGREGATOR,
            WRITER_METRICS_AGGREGATOR = new SampleAggregator(WriterMetrics.values()),
            ERRORS_AND_EXCEPTIONS_AGGREGATOR,
            PERIODIC_SAMPLE_AGGREGATOR;

    public static StatsReporter RCA_STATS_REPORTER;

    public static void initStatsReporter() {
        RCA_STATS_REPORTER =
                new StatsReporter(
                        Arrays.asList(
                                RCA_GRAPH_METRICS_AGGREGATOR,
                                RCA_RUNTIME_METRICS_AGGREGATOR,
                                RCA_VERTICES_METRICS_AGGREGATOR,
                                READER_METRICS_AGGREGATOR,
                                WRITER_METRICS_AGGREGATOR,
                                ERRORS_AND_EXCEPTIONS_AGGREGATOR,
                                PERIODIC_SAMPLE_AGGREGATOR));
    }
}
