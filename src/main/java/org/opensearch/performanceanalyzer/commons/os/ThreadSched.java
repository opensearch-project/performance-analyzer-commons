/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;


import java.util.HashMap;
import java.util.Map;
import org.opensearch.performanceanalyzer.commons.metrics_generator.SchedMetricsGenerator;
import org.opensearch.performanceanalyzer.commons.metrics_generator.linux.LinuxSchedMetricsGenerator;
import org.opensearch.performanceanalyzer.commons.os.metrics.SchedMetrics;
import org.opensearch.performanceanalyzer.commons.os.metrics.SchedMetricsCalculator;
import org.opensearch.performanceanalyzer.commons.observer.ResourceObserver;
import org.opensearch.performanceanalyzer.commons.os.observer.impl.SchedObserver;

public final class ThreadSched {

    public static final ThreadSched INSTANCE = new ThreadSched();
    private static final ResourceObserver schedObserver = new SchedObserver();
    private Map<String, Map<String, Object>> tidKVMap = new HashMap<>();
    private Map<String, Map<String, Object>> oldtidKVMap = new HashMap<>();
    private long kvTimestamp = 0;
    private long oldkvTimestamp = 0;

    private LinuxSchedMetricsGenerator schedLatencyMap = new LinuxSchedMetricsGenerator();

    public synchronized void addSample() {
        oldtidKVMap.clear();
        oldtidKVMap.putAll(tidKVMap);

        tidKVMap.clear();
        oldkvTimestamp = kvTimestamp;
        kvTimestamp = System.currentTimeMillis();
        // Retrieve the sched metrics for all threads
        tidKVMap.putAll(schedObserver.observe());

        calculateSchedLatency();
    }

    private void calculateSchedLatency() {
        if (oldkvTimestamp == kvTimestamp) {
            return;
        }

        for (Map.Entry<String, Map<String, Object>> entry : tidKVMap.entrySet()) {
            Map<String, Object> v = entry.getValue();
            Map<String, Object> oldv = oldtidKVMap.get(entry.getKey());
            SchedMetrics schedMetrics =
                    SchedMetricsCalculator.calculateThreadSchedLatency(
                            kvTimestamp, oldkvTimestamp, v, oldv);
            if (schedMetrics != null) {
                schedLatencyMap.setSchedMetric(entry.getKey(), schedMetrics);
            }
        }
    }

    public synchronized SchedMetricsGenerator getSchedLatency() {

        return schedLatencyMap;
    }
}
