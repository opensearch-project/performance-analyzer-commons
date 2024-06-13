/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.opensearch.performanceanalyzer.commons.stats.collectors.SampleAggregator;
import org.opensearch.performanceanalyzer.commons.stats.metrics.CollectorMetrics;
import org.opensearch.performanceanalyzer.commons.stats.metrics.ExceptionsAndErrors;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatMetrics;

/**
 * Catalog Service Metrics class that is to be populated upon PerformanceAnalyzerApp class load and
 * to be used by both PA and RCA.
 */
public class ServiceMetrics {
    public static SampleAggregator READER_METRICS_AGGREGATOR,
            COLLECTORS_METRICS_AGGREGATOR = new SampleAggregator(CollectorMetrics.values()),
            ERRORS_AND_EXCEPTIONS_AGGREGATOR = new SampleAggregator(ExceptionsAndErrors.values()),
            PERIODIC_SAMPLE_AGGREGATOR,
            RCA_GRAPH_METRICS_AGGREGATOR,
            RCA_RUNTIME_METRICS_AGGREGATOR,
            RCA_VERTICES_METRICS_AGGREGATOR;

    // TODO: Make Private after the collectors are ported over from RCA
    public static final SampleAggregator COMMONS_STAT_METRICS_AGGREGATOR =
            new SampleAggregator(StatMetrics.values());

    public static StatsReporter STATS_REPORTER;

    public static void initStatsReporter() {
        List<SampleAggregator> aggregators =
                Stream.of(
                                COMMONS_STAT_METRICS_AGGREGATOR,
                                READER_METRICS_AGGREGATOR,
                                COLLECTORS_METRICS_AGGREGATOR,
                                ERRORS_AND_EXCEPTIONS_AGGREGATOR,
                                PERIODIC_SAMPLE_AGGREGATOR,
                                RCA_GRAPH_METRICS_AGGREGATOR,
                                RCA_RUNTIME_METRICS_AGGREGATOR,
                                RCA_VERTICES_METRICS_AGGREGATOR)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

        STATS_REPORTER = new StatsReporter(aggregators);
    }
}
