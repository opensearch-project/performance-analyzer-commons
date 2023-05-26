/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;


import org.junit.Assert;
import org.junit.Before;
import org.opensearch.performanceanalyzer.commons.metrics.MetricsConfiguration;

public class MountedPartitionMetricsCollectorTest extends AbstractCollectorTest {
    @Before
    public void setup() {
        int interval =
                MetricsConfiguration.CONFIG_MAP.get(MountedPartitionMetricsCollector.class)
                        .samplingInterval;
        setUut(new MountedPartitionMetricsCollector("MountedPartitionMetricsCollector", interval));
    }

    @Override
    public void validateMetric(String metric) throws Exception {
        MountedPartitionMetrics partitionMetrics =
                mapper.readValue(metric, MountedPartitionMetrics.class);
        // TODO implement further validation of the MetricStatus
        Assert.assertFalse(partitionMetrics.getMountPoint().isEmpty());
        Assert.assertFalse(partitionMetrics.getDevicePartition().isEmpty());
        long totalSpace = partitionMetrics.getTotalSpace();
        Assert.assertTrue(totalSpace >= 0 || totalSpace == -1);
        long freeSpace = partitionMetrics.getFreeSpace();
        Assert.assertTrue(freeSpace >= 0 || freeSpace == -1);
        long usableFreeSpace = partitionMetrics.getUsableFreeSpace();
        Assert.assertTrue(usableFreeSpace >= 0 || usableFreeSpace == -1);
    }
}
