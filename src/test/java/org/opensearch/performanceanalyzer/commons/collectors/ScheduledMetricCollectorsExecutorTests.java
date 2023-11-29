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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({StatsCollector.class, System.class})
public class ScheduledMetricCollectorsExecutorTests {
    @Test
    public void testSlowMuting() throws Exception {
        // mock System.currentTimeMillis()
        // used by ThreadSched to compute SchedMetric
        PowerMockito.mockStatic(System.class);
        when(System.currentTimeMillis())
                .thenReturn(
                        1L, 1L, // addScheduledMetricCollector calls
                        1L, // prevStartTimestamp
                        1L, // millisToSleep
                        11L, // prevStartTimestamp
                        11L, // currentTime
                        12L, // millisToSleep
                        22L, // prevStartTimestamp
                        22L, // currentTime
                        11L, 22L, 33L, 44L, 55L, 66L, 77L, 88L, 99L);

        ScheduledMetricCollectorsExecutor executor = new ScheduledMetricCollectorsExecutor();
        executor.setEnabled(true);

        ArrayList<RunBehaviour> bh = new ArrayList<RunBehaviour>();
        bh.add(new RunBehaviour(0, true));
        bh.add(new RunBehaviour(0, true));
        bh.add(new RunBehaviour(0, true));
        bh.add(new RunBehaviour(0, true));
        bh.add(new RunBehaviour(0, true));
        bh.add(new RunBehaviour(0, true));
        bh.add(new RunBehaviour(0, true));

        TestCollector tc = spy(new TestCollector(10, bh));
        executor.addScheduledMetricCollector(tc);

        // mock StatsCollector.instance
        PowerMockito.mockStatic(StatsCollector.class);
        // 200 ms allows
        StatsCollector sc =
                new StatsCollector("statsCollector", 200, new HashMap<String, String>());
        when(StatsCollector.instance()).thenReturn(sc);

        executor.addScheduledMetricCollector(StatsCollector.instance());

        executor.start();

        Thread.sleep(1000);

        verify(tc).setState(PerformanceAnalyzerMetricsCollector.State.SLOW);
        verify(tc).setState(PerformanceAnalyzerMetricsCollector.State.MUTED);
    }
}
