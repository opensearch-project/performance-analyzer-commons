/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os.metrics;

public class IOMetrics {
    public double avgReadThroughputBps;
    public double avgWriteThroughputBps;
    public double avgTotalThroughputBps;

    public double avgReadSyscallRate;
    public double avgWriteSyscallRate;
    public double avgTotalSyscallRate;

    public double avgPageCacheReadThroughputBps;
    public double avgPageCacheWriteThroughputBps;
    public double avgPageCacheTotalThroughputBps;

    @SuppressWarnings("checkstyle:parameternumber")
    public IOMetrics(
            double avgReadThroughputBps,
            double avgReadSyscallRate,
            double avgWriteThroughputBps,
            double avgWriteSyscallRate,
            double avgTotalThroughputBps,
            double avgTotalSyscallRate,
            double avgPageCacheReadThroughputBps,
            double avgPageCacheWriteThroughputBps,
            double avgPageCacheTotalThroughputBps) {
        this.avgReadThroughputBps = avgReadThroughputBps;
        this.avgWriteThroughputBps = avgWriteThroughputBps;
        this.avgTotalThroughputBps = avgTotalThroughputBps;
        this.avgReadSyscallRate = avgReadSyscallRate;
        this.avgWriteSyscallRate = avgWriteSyscallRate;
        this.avgTotalSyscallRate = avgTotalSyscallRate;
        this.avgPageCacheReadThroughputBps = avgPageCacheReadThroughputBps;
        this.avgPageCacheWriteThroughputBps = avgPageCacheWriteThroughputBps;
        this.avgPageCacheTotalThroughputBps = avgPageCacheTotalThroughputBps;
    }

    public String toString() {
        return new StringBuilder()
                .append("rBps:")
                .append(avgReadThroughputBps)
                .append(" wBps:")
                .append(avgWriteThroughputBps)
                .append(" totBps:")
                .append(avgTotalThroughputBps)
                .append(" rSysc:")
                .append(avgReadSyscallRate)
                .append(" wSysc:")
                .append(avgWriteSyscallRate)
                .append(" totSysc:")
                .append(avgTotalSyscallRate)
                .append(" rPcBps:")
                .append(avgPageCacheReadThroughputBps)
                .append(" wPcBps:")
                .append(avgPageCacheWriteThroughputBps)
                .append(" totPcBps:")
                .append(avgPageCacheTotalThroughputBps)
                .toString();
    }
}
