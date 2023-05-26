/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats;


import java.util.Arrays;
import org.opensearch.performanceanalyzer.commons.stats.collectors.SampleAggregator;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatMetrics;

/**
 * Catalog Service Metrics class that is to be populated upon PerformanceAnalyzerApp class load and
 * to be used by both PA and RCA.
 */
public class ServiceMetrics {
    public static SampleAggregator READER_METRICS_AGGREGATOR,
            WRITER_METRICS_AGGREGATOR = new SampleAggregator(StatMetrics.values()),
            ERRORS_AND_EXCEPTIONS_AGGREGATOR,
            PERIODIC_SAMPLE_AGGREGATOR,
            RCA_GRAPH_METRICS_AGGREGATOR,
            RCA_RUNTIME_METRICS_AGGREGATOR,
            RCA_VERTICES_METRICS_AGGREGATOR;

    // TODO: Make Private after the collectors are ported over from RCA
    public static final SampleAggregator COMMONS_STAT_METRICS_AGGREGATOR =
            new SampleAggregator(StatMetrics.values());

    public static StatsReporter STATS_REPORTER;

    public static void initStatsReporter() {
        STATS_REPORTER =
                new StatsReporter(
                        Arrays.asList(
                                COMMONS_STAT_METRICS_AGGREGATOR,
                                READER_METRICS_AGGREGATOR,
                                RCA_GRAPH_METRICS_AGGREGATOR,
                                RCA_RUNTIME_METRICS_AGGREGATOR,
                                RCA_VERTICES_METRICS_AGGREGATOR,
                                ERRORS_AND_EXCEPTIONS_AGGREGATOR,
                                PERIODIC_SAMPLE_AGGREGATOR));
    }
}
