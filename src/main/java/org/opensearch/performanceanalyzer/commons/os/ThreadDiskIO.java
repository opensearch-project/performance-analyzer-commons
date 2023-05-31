/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;


import java.util.HashMap;
import java.util.Map;
import org.opensearch.performanceanalyzer.commons.metrics_generator.linux.LinuxDiskIOMetricsGenerator;
import org.opensearch.performanceanalyzer.commons.os.metrics.DiskIOMetricsCalculator;
import org.opensearch.performanceanalyzer.commons.os.metrics.IOMetrics;
import org.opensearch.performanceanalyzer.commons.os.observer.ResourceObserver;
import org.opensearch.performanceanalyzer.commons.os.observer.impl.IOObserver;

public class ThreadDiskIO {
    private static final ResourceObserver ioObserver = new IOObserver();

    private static Map<String, Map<String, Long>> tidKVMap = new HashMap<>();
    private static Map<String, Map<String, Long>> oldtidKVMap = new HashMap<>();
    private static long kvTimestamp = 0;
    private static long oldkvTimestamp = 0;

    public static synchronized void addSample() {
        oldtidKVMap.clear();
        oldtidKVMap.putAll(tidKVMap);

        tidKVMap.clear();
        oldkvTimestamp = kvTimestamp;
        kvTimestamp = System.currentTimeMillis();
        // Retrieve the disk io metrics for all threads
        tidKVMap.putAll(ioObserver.observe());
    }

    public static synchronized LinuxDiskIOMetricsGenerator getIOUtilization() {

        LinuxDiskIOMetricsGenerator linuxDiskIOMetricsHandler = new LinuxDiskIOMetricsGenerator();
        if (oldkvTimestamp == kvTimestamp) {
            return linuxDiskIOMetricsHandler;
        }

        for (Map.Entry<String, Map<String, Long>> entry : tidKVMap.entrySet()) {
            Map<String, Long> v = entry.getValue();
            Map<String, Long> oldv = oldtidKVMap.get(entry.getKey());
            IOMetrics ioMetrics =
                    DiskIOMetricsCalculator.calculateIOMetrics(
                            kvTimestamp, oldkvTimestamp, v, oldv);

            if (ioMetrics != null) {
                linuxDiskIOMetricsHandler.setDiskIOMetrics(entry.getKey(), ioMetrics);
            }
        }
        return linuxDiskIOMetricsHandler;
    }
}
