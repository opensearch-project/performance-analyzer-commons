/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;

import static org.opensearch.performanceanalyzer.commons.os.metrics.CPUMetricsCalculator.calculateCPUUtilization;
import static org.opensearch.performanceanalyzer.commons.os.metrics.CPUMetricsCalculator.calculateMajorFault;
import static org.opensearch.performanceanalyzer.commons.os.metrics.CPUMetricsCalculator.calculateMinorFault;
import static org.opensearch.performanceanalyzer.commons.os.metrics.CPUMetricsCalculator.getResidentSetSize;

import java.util.HashMap;
import java.util.Map;
import org.opensearch.performanceanalyzer.commons.metrics_generator.linux.LinuxCPUPagingActivityGenerator;
import org.opensearch.performanceanalyzer.commons.observer.ResourceObserver;
import org.opensearch.performanceanalyzer.commons.os.observer.impl.CPUObserver;

public final class ThreadCPU {

    public static final ThreadCPU INSTANCE = new ThreadCPU();
    private static final ResourceObserver cpuObserver = new CPUObserver();
    private Map<String, Map<String, Object>> tidKVMap = new HashMap<>();
    private Map<String, Map<String, Object>> oldtidKVMap = new HashMap<>();
    private long kvTimestamp = 0;
    private long oldkvTimestamp = 0;
    private LinuxCPUPagingActivityGenerator cpuPagingActivityMap =
            new LinuxCPUPagingActivityGenerator();

    public synchronized void addSample() {
        oldtidKVMap.clear();
        oldtidKVMap.putAll(tidKVMap);

        tidKVMap.clear();
        oldkvTimestamp = kvTimestamp;
        kvTimestamp = System.currentTimeMillis();
        // Retrieve the cpu metrics for all threads
        tidKVMap.putAll(cpuObserver.observe());

        calculateCPUDetails();
        calculatePagingActivity();
    }

    private void calculateCPUDetails() {
        if (oldkvTimestamp == kvTimestamp) {
            return;
        }

        for (Map.Entry<String, Map<String, Object>> entry : tidKVMap.entrySet()) {
            Map<String, Object> v = entry.getValue();
            Map<String, Object> oldv = oldtidKVMap.get(entry.getKey());
            double util = calculateCPUUtilization(kvTimestamp, oldkvTimestamp, v, oldv);
            cpuPagingActivityMap.setCPUUtilization(entry.getKey(), util);
        }
    }

    /** Note: major faults include mmap()'ed accesses */
    private void calculatePagingActivity() {
        if (oldkvTimestamp == kvTimestamp) {
            return;
        }

        for (Map.Entry<String, Map<String, Object>> entry : tidKVMap.entrySet()) {
            Map<String, Object> v = entry.getValue();
            Map<String, Object> oldv = oldtidKVMap.get(entry.getKey());

            double majdiff = calculateMajorFault(kvTimestamp, oldkvTimestamp, v, oldv);
            double mindiff = calculateMinorFault(kvTimestamp, oldkvTimestamp, v, oldv);
            double rss = getResidentSetSize(v);
            Double[] fltarr = {majdiff, mindiff, rss};
            cpuPagingActivityMap.setPagingActivities(entry.getKey(), fltarr);
        }
    }

    public LinuxCPUPagingActivityGenerator getCPUPagingActivity() {

        return cpuPagingActivityMap;
    }
}
