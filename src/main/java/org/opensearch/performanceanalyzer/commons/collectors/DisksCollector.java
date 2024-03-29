/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;

import java.util.HashMap;
import java.util.Map;
import org.opensearch.performanceanalyzer.commons.OSMetricsGeneratorFactory;
import org.opensearch.performanceanalyzer.commons.metrics.MetricsConfiguration;
import org.opensearch.performanceanalyzer.commons.metrics.MetricsProcessor;
import org.opensearch.performanceanalyzer.commons.metrics.PerformanceAnalyzerMetrics;
import org.opensearch.performanceanalyzer.commons.metrics_generator.DiskMetricsGenerator;
import org.opensearch.performanceanalyzer.commons.metrics_generator.OSMetricsGenerator;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatExceptionCode;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatMetrics;

public class DisksCollector extends PerformanceAnalyzerMetricsCollector
        implements MetricsProcessor {

    private static final int sTimeInterval =
            MetricsConfiguration.CONFIG_MAP.get(DisksCollector.class).samplingInterval;

    public DisksCollector() {
        super(
                sTimeInterval,
                "DisksCollector",
                StatMetrics.DISKS_COLLECTOR_EXECUTION_TIME,
                StatExceptionCode.DISK_METRICS_COLLECTOR_ERROR);
    }

    @Override
    public String getMetricsPath(long startTime, String... keysPath) {
        // throw exception if keys.length is not equal to 0
        if (keysPath.length != 0) {
            throw new RuntimeException("keys length should be 0");
        }

        return PerformanceAnalyzerMetrics.generatePath(
                startTime, PerformanceAnalyzerMetrics.sDisksPath);
    }

    @Override
    public void collectMetrics(long startTime) {
        OSMetricsGenerator generator = OSMetricsGeneratorFactory.getInstance();
        if (generator == null) {
            return;
        }
        DiskMetricsGenerator diskMetricsGenerator = generator.getDiskMetricsGenerator();
        diskMetricsGenerator.addSample();

        saveMetricValues(getMetrics(diskMetricsGenerator), startTime);
    }

    private Map<String, DiskMetrics> getMetricsMap(DiskMetricsGenerator diskMetricsGenerator) {

        Map<String, DiskMetrics> map = new HashMap<>();

        for (String disk : diskMetricsGenerator.getAllDisks()) {
            DiskMetrics diskMetrics = new DiskMetrics();
            diskMetrics.name = disk;
            diskMetrics.await = diskMetricsGenerator.getAwait(disk);
            diskMetrics.serviceRate = diskMetricsGenerator.getServiceRate(disk);
            diskMetrics.utilization = diskMetricsGenerator.getDiskUtilization(disk);

            map.put(disk, diskMetrics);
        }

        return map;
    }

    private String getMetrics(DiskMetricsGenerator diskMetricsGenerator) {

        Map<String, DiskMetrics> map = getMetricsMap(diskMetricsGenerator);
        value.setLength(0);
        value.append(PerformanceAnalyzerMetrics.getJsonCurrentMilliSeconds())
                .append(PerformanceAnalyzerMetrics.sMetricNewLineDelimitor);
        for (Map.Entry<String, DiskMetrics> entry : map.entrySet()) {
            value.append(entry.getValue().serialize())
                    .append(PerformanceAnalyzerMetrics.sMetricNewLineDelimitor);
        }
        return value.toString();
    }
}
