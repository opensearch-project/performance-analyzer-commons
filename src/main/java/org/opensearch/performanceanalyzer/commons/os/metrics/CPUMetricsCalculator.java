/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os.metrics;


import java.util.Map;
import org.opensearch.performanceanalyzer.commons.os.OSGlobals;
import org.opensearch.performanceanalyzer.commons.os.observer.impl.CPUObserver.StatKeys;

/**
 * Calculates the cpu and paging activity for the thread considering the beginning and end
 * measurements
 */
public final class CPUMetricsCalculator {
    /**
     * Calculates CPU related metrics - cpu utilization + paging activity based on the provided
     * metrics map
     *
     * @param endMeasurementTime
     * @param startMeasurementTime
     * @param endTimeResourceMetrics
     * @param startTimeResourceMetrics
     * @return
     */
    public static CPUMetrics calculateThreadCpuPagingActivity(
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

        double majorFault =
                calculateMajorFault(
                        endMeasurementTime,
                        startMeasurementTime,
                        endTimeResourceMetrics,
                        startTimeResourceMetrics);
        double minorFault =
                calculateMinorFault(
                        endMeasurementTime,
                        startMeasurementTime,
                        endTimeResourceMetrics,
                        startTimeResourceMetrics);
        double rss = getResidentSetSize(endTimeResourceMetrics);
        double cpuUtilization =
                calculateCPUUtilization(
                        endMeasurementTime,
                        startMeasurementTime,
                        endTimeResourceMetrics,
                        startTimeResourceMetrics);

        return new CPUMetrics(cpuUtilization, majorFault, minorFault, rss);
    }

    public static double calculateCPUUtilization(
            long endMeasurementTime,
            long startMeasurementTime,
            Map<String, Object> endTimeResourceMetrics,
            Map<String, Object> startTimeResourceMetrics) {
        if (endMeasurementTime == startMeasurementTime) {
            return 0D;
        }
        if (endTimeResourceMetrics == null || startTimeResourceMetrics == null) {
            return 0D;
        }
        return calculateCPUUtilization(
                endMeasurementTime,
                startMeasurementTime,
                (long) endTimeResourceMetrics.getOrDefault(StatKeys.UTIME.getLabel(), 0L),
                (long) startTimeResourceMetrics.getOrDefault(StatKeys.UTIME.getLabel(), 0L),
                (long) endTimeResourceMetrics.getOrDefault(StatKeys.STIME.getLabel(), 0L),
                (long) startTimeResourceMetrics.getOrDefault(StatKeys.STIME.getLabel(), 0L));
    }

    public static double calculateMajorFault(
            long endMeasurementTime,
            long startMeasurementTime,
            Map<String, Object> endTimeResourceMetrics,
            Map<String, Object> startTimeResourceMetrics) {
        if (endTimeResourceMetrics == null || startTimeResourceMetrics == null) {
            return 0d;
        }

        return calculateFault(
                endMeasurementTime,
                startMeasurementTime,
                (long) (endTimeResourceMetrics.getOrDefault(StatKeys.MAJFLT.getLabel(), 0L)),
                (long) (startTimeResourceMetrics.getOrDefault(StatKeys.MAJFLT.getLabel(), 0L)));
    }

    public static double calculateMinorFault(
            long endMeasurementTime,
            long startMeasurementTime,
            Map<String, Object> endTimeResourceMetrics,
            Map<String, Object> startTimeResourceMetrics) {
        if (endTimeResourceMetrics == null || startTimeResourceMetrics == null) {
            return 0d;
        }

        return calculateFault(
                endMeasurementTime,
                startMeasurementTime,
                (long) (endTimeResourceMetrics.getOrDefault(StatKeys.MINFLT.getLabel(), 0L)),
                (long) (startTimeResourceMetrics.getOrDefault(StatKeys.MINFLT.getLabel(), 0L)));
    }

    public static double getResidentSetSize(Map<String, Object> v) {
        return (double) ((long) v.getOrDefault(StatKeys.RSS.getLabel(), 0L));
    }

    /**
     * Calculates the CPU utilization based on the given parameters
     *
     * @param endMeasurementTime End time of the measurement
     * @param startMeasurementTime Start time of the measurement
     * @param endUTime utime metric value at the end of the measurement
     * @param startUtime utime metric value at the beginning of the measurement
     * @param endSTime stime metric value at the end of the measurement
     * @param startSTime stime metric value at the beginning the measurement
     * @return cpu utilization
     */
    public static double calculateCPUUtilization(
            long endMeasurementTime,
            long startMeasurementTime,
            long endUTime,
            long startUtime,
            long endSTime,
            long startSTime) {
        long scClckTck = OSGlobals.getScClkTck();
        long diff = endUTime - startUtime + endSTime - startSTime;
        return (1.0e3 * diff / scClckTck) / (endMeasurementTime - startMeasurementTime);
    }

    private static double calculateFault(
            long endMeasurementTime,
            long startMeasurementTime,
            long endMajorFault,
            long startMajorFault) {
        double majdiff = endMajorFault - startMajorFault;
        majdiff /= 1.0e-3 * (endMeasurementTime - startMeasurementTime);
        return majdiff;
    }
}
