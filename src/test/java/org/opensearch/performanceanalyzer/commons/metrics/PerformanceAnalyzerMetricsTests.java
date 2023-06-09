/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.metrics;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.opensearch.performanceanalyzer.commons.config.PluginSettings;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@PowerMockIgnore({
    "javax.xml.*",
    "org.xml.sax.*",
    "org.w3c.dom.*",
    "org.springframework.context.*",
    "org.apache.log4j.*",
    "javax.management.*"
})
@RunWith(PowerMockRunner.class)
@PrepareForTest({PerformanceAnalyzerMetrics.class, PluginSettings.class})
@SuppressStaticInitializationFor({"PluginSettings"})
public class PerformanceAnalyzerMetricsTests {

    @Before
    public void setUp() throws Exception {
        PluginSettings config = Mockito.mock(PluginSettings.class);
        Mockito.when(config.getMetricsLocation()).thenReturn("/dev/shm/performanceanalyzer");
        Mockito.when(config.getWriterQueueSize()).thenReturn(1);
        PowerMockito.mockStatic(PluginSettings.class);
        PowerMockito.when(PluginSettings.instance()).thenReturn(config);
    }

    @Ignore
    public void testBasicMetric() {
        System.setProperty("performanceanalyzer.metrics.log.enabled", "False");
        PerformanceAnalyzerMetrics.emitMetric(
                System.currentTimeMillis(),
                PluginSettings.instance().getMetricsLocation() + "/dir1/test1",
                "value1");
        assertEquals(
                "value1",
                PerformanceAnalyzerMetrics.getMetric(
                        PluginSettings.instance().getMetricsLocation() + "/dir1/test1"));

        assertEquals(
                "",
                PerformanceAnalyzerMetrics.getMetric(
                        PluginSettings.instance().getMetricsLocation() + "/dir1/test2"));

        PerformanceAnalyzerMetrics.removeMetrics(
                PluginSettings.instance().getMetricsLocation() + "/dir1");
    }

    @Test
    public void testGeneratePath() {
        long startTimeInMillis = 1553725339;
        String generatedPath =
                PerformanceAnalyzerMetrics.generatePath(startTimeInMillis, "dir1", "id", "dir2");
        String expectedPath =
                PluginSettings.instance().getMetricsLocation()
                        + "/"
                        + String.valueOf(
                                PerformanceAnalyzerMetrics.getTimeInterval(startTimeInMillis))
                        + "/dir1/id/dir2";
        assertEquals(expectedPath, generatedPath);
    }
}
