/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os.metrics;


import org.opensearch.performanceanalyzer.commons.os.observer.impl.CPUObserver.StatKeys;

public class CPUMetrics {
    public double cpuUtilization;
    public double majorFault;
    public double minorFault;
    public double residentSetSize;

    public CPUMetrics(
            double cpuUtilization, double majorFault, double minorFault, double residentSetSize) {
        this.cpuUtilization = cpuUtilization;
        this.majorFault = majorFault;
        this.minorFault = minorFault;
        this.residentSetSize = residentSetSize;
    }

    public String toString() {
        return new StringBuilder()
                .append(StatKeys.CPU.getLabel() + ":")
                .append(cpuUtilization)
                .append(" " + StatKeys.MAJFLT.getLabel() + ":")
                .append(majorFault)
                .append(" " + StatKeys.MINFLT.getLabel() + ":")
                .append(minorFault)
                .append(" " + StatKeys.RSS + ":")
                .append(residentSetSize)
                .toString();
    }
}
