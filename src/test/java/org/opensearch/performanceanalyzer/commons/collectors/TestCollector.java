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
        int repetitions;
        int current_reps;
        int latency;
        boolean shouldThrow;

        RunBehaviour(int repetitions, int latency, boolean throwException) {
            this.repetitions = repetitions;
            this.current_reps = repetitions;
            this.latency = latency;
            this.shouldThrow = throwException;
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
        try {
            RunBehaviour b = bh.get(idx);
            if (b.latency > 0) {
                Thread.sleep(b.latency);
            }

            if (b.current_reps > 0) {
                b.current_reps--;
            } else {
                // reset repetitions to use for next iteration
                b.current_reps = b.repetitions;
                idx++;
            }

            if (b.shouldThrow) {
                throw new RuntimeException("TestCollector exception");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IndexOutOfBoundsException e) {
            // start from beginning
            idx = 0;
        }
    }
}
