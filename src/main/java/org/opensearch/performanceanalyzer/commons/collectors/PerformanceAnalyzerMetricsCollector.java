/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;


import com.google.common.annotations.VisibleForTesting;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.performanceanalyzer.commons.stats.ServiceMetrics;
import org.opensearch.performanceanalyzer.commons.stats.measurements.MeasurementSet;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatExceptionCode;
import org.opensearch.performanceanalyzer.commons.util.Util;

public abstract class PerformanceAnalyzerMetricsCollector implements Runnable {
    public enum State {
        HEALTHY,

        // This collector could not complete between two runs of
        // ScheduledMetricCollectorsExecutor. First occurrence of
        // this is considered a warning.
        SLOW,

        // A collector is muted if it failed to complete between two runs of
        // ScheduledMetricCollectorsExecutor. A muted collector is skipped.
        MUTED
    }

    private static final Logger LOG =
            LogManager.getLogger(PerformanceAnalyzerMetricsCollector.class);
    private int timeInterval;
    private long startTime;

    private String collectorName;
    private MeasurementSet statLatencyMetric;
    private StatExceptionCode errorMetric;
    protected StringBuilder value;

    protected State state;
    private boolean threadContentionMonitoringEnabled;

    protected PerformanceAnalyzerMetricsCollector(
            int timeInterval,
            String collectorName,
            MeasurementSet statLatencyMetric,
            StatExceptionCode errorMetric) {
        this.timeInterval = timeInterval;
        this.collectorName = collectorName;
        this.statLatencyMetric = statLatencyMetric;
        this.errorMetric = errorMetric;
        this.value = new StringBuilder();
        this.state = State.HEALTHY;
    }

    private AtomicBoolean bInProgress = new AtomicBoolean(false);

    public int getTimeInterval() {
        return timeInterval;
    }

    public boolean inProgress() {
        return bInProgress.get();
    }

    public String getCollectorName() {
        return collectorName;
    }

    public abstract void collectMetrics(long startTime);

    public void setStartTime(long startTime) {
        this.startTime = startTime;
        bInProgress.set(true);
    }

    public void run() {
        long mCurrT = System.currentTimeMillis();
        try {
            Util.invokePrivileged(() -> collectMetrics(startTime));
            LOG.debug(
                    "[ {} ]Successfully collected ClusterManager Event Metrics.",
                    getCollectorName());
            ServiceMetrics.COMMONS_STAT_METRICS_AGGREGATOR.updateStat(
                    statLatencyMetric, System.currentTimeMillis() - mCurrT);
        } catch (Exception ex) {
            LOG.error(
                    "[ {} ] Error in metric collection for startTime {}: {}",
                    () -> mCurrT,
                    () -> getCollectorName(),
                    () -> ex.toString());
            StatsCollector.instance().logException(errorMetric);
        } finally {
            bInProgress.set(false);
        }
    }

    @VisibleForTesting
    public StringBuilder getValue() {
        return value;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setThreadContentionMonitoringEnabled(boolean enabled) {
        this.threadContentionMonitoringEnabled = enabled;
    }

    public boolean getThreadContentionMonitoringEnabled() {
        return threadContentionMonitoringEnabled;
    }
}
