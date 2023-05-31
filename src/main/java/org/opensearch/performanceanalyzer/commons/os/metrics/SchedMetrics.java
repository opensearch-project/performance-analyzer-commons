/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os.metrics;

public class SchedMetrics {
    public final double avgRuntime;
    public final double avgWaittime;
    public final double contextSwitchRate; // both voluntary and involuntary

    public SchedMetrics(double avgRuntime, double avgWaittime, double contextSwitchRate) {
        this.avgRuntime = avgRuntime;
        this.avgWaittime = avgWaittime;
        this.contextSwitchRate = contextSwitchRate;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("avgruntime: ")
                .append(avgRuntime)
                .append(" avgwaittime: ")
                .append(avgWaittime)
                .append(" ctxrate: ")
                .append(contextSwitchRate)
                .toString();
    }
}
