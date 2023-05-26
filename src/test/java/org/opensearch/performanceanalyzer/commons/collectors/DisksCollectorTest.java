/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;


import org.junit.Assert;
import org.junit.Before;
import org.opensearch.performanceanalyzer.commons.metrics.MetricsConfiguration;

public class DisksCollectorTest extends AbstractCollectorTest {
    @Before
    public void setup() {
        int interval = MetricsConfiguration.CONFIG_MAP.get(DisksCollector.class).samplingInterval;
        setUut(new DisksCollector("DiskCollector", interval));
    }

    @Override
    public void validateMetric(String metric) throws Exception {
        DiskMetrics diskMetrics = mapper.readValue(metric, DiskMetrics.class);
        // TODO implement further validation of the MetricStatus
        Assert.assertFalse(diskMetrics.getName().isEmpty());
        Assert.assertTrue(diskMetrics.getUtilization() >= 0 && diskMetrics.getUtilization() <= 1);
        Assert.assertTrue(diskMetrics.getAwait() >= 0);
        Assert.assertTrue(diskMetrics.getServiceRate() >= 0);
    }
}
