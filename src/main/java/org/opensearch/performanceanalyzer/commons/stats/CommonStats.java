/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats;

/* Catalogue class that is to be populated upon PerformanceAnalyzerApp class load
and to be used by both PA and PA-RCA as well as commons repo.
 */
public class CommonStats {
    public static SampleAggregator RCA_GRAPH_METRICS_AGGREGATOR,
            RCA_RUNTIME_METRICS_AGGREGATOR,
            RCA_VERTICES_METRICS_AGGREGATOR,
            READER_METRICS_AGGREGATOR,
            WRITER_METRICS_AGGREGATOR,
            MISBEHAVING_NODES_LISTENER,
            ERRORS_AND_EXCEPTIONS_AGGREGATOR,
            PERIODIC_SAMPLE_AGGREGATOR;

    public static StatsReporter RCA_STATS_REPORTER;
}
