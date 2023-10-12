/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os.metrics;


import java.util.Map;
import org.opensearch.performanceanalyzer.commons.os.observer.impl.SchedObserver.SchedKeys;

/** Calculates sched metric resources consumption by threads */
public final class SchedMetricsCalculator {
    /**
     * Calculates sched metrics based on the values from beginning and end of measurement
     *
     * @param endTimeResourceMetrics
     * @param startTimeResourceMetrics
     * @param endMeasurementTime
     * @param startMeasurementTime
     * @return
     */
    public static SchedMetrics calculateThreadSchedLatency(
            long endMeasurementTime,
            long startMeasurementTime,
            Map<String, Object> endTimeResourceMetrics,
            Map<String, Object> startTimeResourceMetrics) {

        if (startMeasurementTime == endMeasurementTime) {
            return null;
        }

        if (endTimeResourceMetrics == null || startTimeResourceMetrics == null) {
            return null;
        }

        if (!endTimeResourceMetrics.containsKey(SchedKeys.TOTCTXSWS.getLabel())
                || !startTimeResourceMetrics.containsKey(SchedKeys.TOTCTXSWS.getLabel())) {
            return null;
        }

        long ctxdiff =
                (long) endTimeResourceMetrics.getOrDefault(SchedKeys.TOTCTXSWS.getLabel(), 0L)
                        - (long)
                                startTimeResourceMetrics.getOrDefault(
                                        SchedKeys.TOTCTXSWS.getLabel(), 0L);
        double avgRuntime =
                1.0e-9
                        * ((long)
                                        endTimeResourceMetrics.getOrDefault(
                                                SchedKeys.RUNTICKS.getLabel(), 0L)
                                - (long)
                                        startTimeResourceMetrics.getOrDefault(
                                                SchedKeys.RUNTICKS.getLabel(), 0L));
        double avgWaittime =
                1.0e-9
                        * ((long)
                                        endTimeResourceMetrics.getOrDefault(
                                                SchedKeys.WAITTICKS.getLabel(), 0L)
                                - (long)
                                        startTimeResourceMetrics.getOrDefault(
                                                SchedKeys.WAITTICKS.getLabel(), 0L));
        if (ctxdiff == 0) {
            avgRuntime = 0;
            avgWaittime = 0;
        } else {
            avgRuntime /= 1.0 * ctxdiff;
            avgWaittime /= 1.0 * ctxdiff;
        }
        double contextSwitchRate = ctxdiff;
        contextSwitchRate /= 1.0e-3 * (endMeasurementTime - startMeasurementTime);

        return new SchedMetrics(avgRuntime, avgWaittime, contextSwitchRate);
    }
}
