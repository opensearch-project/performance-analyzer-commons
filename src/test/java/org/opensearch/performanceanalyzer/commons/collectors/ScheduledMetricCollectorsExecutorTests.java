/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opensearch.performanceanalyzer.commons.collectors.TestCollector.RunBehaviour;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@PrepareForTest({StatsCollector.class})
public class ScheduledMetricCollectorsExecutorTests {
    @Test
    public void testSlowMuting() throws Exception {
        ScheduledMetricCollectorsExecutor executor = new ScheduledMetricCollectorsExecutor();
        executor.setEnabled(true);

        ArrayList<RunBehaviour> bh = new ArrayList<RunBehaviour>();
        bh.add(new RunBehaviour(10, 0, true));

        TestCollector tc = spy(new TestCollector(10, bh));
        executor.addScheduledMetricCollector(tc);

        // mock StatsCollector.instance
        PowerMockito.mockStatic(StatsCollector.class);
        StatsCollector sc =
                new StatsCollector("statsCollector", 300, new HashMap<String, String>());
        when(StatsCollector.instance()).thenReturn(sc);

        executor.addScheduledMetricCollector(StatsCollector.instance());

        executor.start();

        Thread.sleep(1000);

        verify(tc).setState(PerformanceAnalyzerMetricsCollector.State.SLOW);
        verify(tc).setState(PerformanceAnalyzerMetricsCollector.State.MUTED);
    }
}
