/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;


import java.util.ArrayList;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatExceptionCode;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatMetrics;

public class TestCollector extends PerformanceAnalyzerMetricsCollector {
    public static class RunBehaviour {
        int latency;
        boolean throwException;

        RunBehaviour(int latency, boolean throwException) {
            this.latency = latency;
            this.throwException = throwException;
        }

        public int getLatency() {
            return latency;
        }

        public boolean getThrowException() {
            return throwException;
        }
    }

    ArrayList<RunBehaviour> bh;
    int idx = 0;

    TestCollector(int timeInterval, ArrayList<RunBehaviour> bh) {
        // timeInterval is measured in milliseconds
        // default timeInterval is 5000 milliseconds (5 seconds) for all collectors
        // except StatsCollector, which is 60 seconds
        // this collector pretents to be DisksCollector for test purposes
        super(
                timeInterval,
                "testCollector",
                StatMetrics.DISKS_COLLECTOR_EXECUTION_TIME,
                StatExceptionCode.DISK_METRICS_COLLECTOR_ERROR);
        this.bh = bh;
    }

    @Override
    public void collectMetrics(long startTime) {
        idx++;
        try {
            RunBehaviour b = bh.get(idx);
            if (b.getLatency() > 0) {
                Thread.sleep(b.getLatency());
            }

            if (b.getThrowException()) {
                throw new RuntimeException("TestCollector exception");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            // reset idx to 0
            idx = 0;
        }
    }
}
