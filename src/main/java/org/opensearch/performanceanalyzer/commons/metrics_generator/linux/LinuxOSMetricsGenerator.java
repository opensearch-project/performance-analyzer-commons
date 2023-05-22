/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.metrics_generator.linux;


import java.util.Set;
import org.opensearch.performanceanalyzer.commons.metrics_generator.CPUPagingActivityGenerator;
import org.opensearch.performanceanalyzer.commons.metrics_generator.DiskIOMetricsGenerator;
import org.opensearch.performanceanalyzer.commons.metrics_generator.OSMetricsGenerator;
import org.opensearch.performanceanalyzer.commons.metrics_generator.SchedMetricsGenerator;
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
}
