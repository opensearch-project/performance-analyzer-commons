/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.metrics_generator.linux;

import java.util.Set;
import org.opensearch.performanceanalyzer.commons.hwnet.Disks;
import org.opensearch.performanceanalyzer.commons.hwnet.MountedPartitions;
import org.opensearch.performanceanalyzer.commons.hwnet.NetworkE2E;
import org.opensearch.performanceanalyzer.commons.hwnet.NetworkInterface;
import org.opensearch.performanceanalyzer.commons.metrics_generator.*;
import org.opensearch.performanceanalyzer.commons.os.OSGlobals;
import org.opensearch.performanceanalyzer.commons.os.ThreadCPU;
import org.opensearch.performanceanalyzer.commons.os.ThreadDiskIO;
import org.opensearch.performanceanalyzer.commons.os.ThreadSched;

public class LinuxOSMetricsGenerator implements OSMetricsGenerator {

    private static OSMetricsGenerator osMetricsGenerator;

    static {
        osMetricsGenerator = new LinuxOSMetricsGenerator();
    }

    public static OSMetricsGenerator getInstance() {

        return osMetricsGenerator;
    }

    @Override
    public String getPid() {

        return OSGlobals.getPid();
    }

    @Override
    public CPUPagingActivityGenerator getPagingActivityGenerator() {

        return ThreadCPU.INSTANCE.getCPUPagingActivity();
    }

    @Override
    public Set<String> getAllThreadIds() {
        return ThreadCPU.INSTANCE.getCPUPagingActivity().getAllThreadIds();
    }

    @Override
    public DiskIOMetricsGenerator getDiskIOMetricsGenerator() {

        return ThreadDiskIO.getIOUtilization();
    }

    @Override
    public SchedMetricsGenerator getSchedMetricsGenerator() {

        return ThreadSched.INSTANCE.getSchedLatency();
    }

    @Override
    public TCPMetricsGenerator getTCPMetricsGenerator() {

        return NetworkE2E.getTCPMetricsHandler();
    }

    @Override
    public IPMetricsGenerator getIPMetricsGenerator() {

        return NetworkInterface.getLinuxIPMetricsGenerator();
    }

    @Override
    public DiskMetricsGenerator getDiskMetricsGenerator() {

        return Disks.getDiskMetricsHandler();
    }

    @Override
    public MountedPartitionMetricsGenerator getMountedPartitionMetricsGenerator() {
        return MountedPartitions.getLinuxMountedPartitionMetricsGenerator();
    }
}
