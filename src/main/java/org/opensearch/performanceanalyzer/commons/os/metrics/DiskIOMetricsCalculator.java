/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os.metrics;


import java.util.Map;
import org.opensearch.performanceanalyzer.commons.os.observer.impl.IOObserver.StatKeys;

/**
 * Calculates the disk io metrics for the threads considering the beginning and end measurements
 */
public final class DiskIOMetricsCalculator {
    public static IOMetrics calculateIOMetrics(
            long endMeasurementTime,
            long startMeasurementTime,
            Map<String, Long> endTimeResourceMetrics,
            Map<String, Long> startTimeResourceMetrics) {
        if (startMeasurementTime == endMeasurementTime) {
            return null;
        }

        if (endTimeResourceMetrics != null && startTimeResourceMetrics != null) {
            double duration = 1.0e-3 * (endMeasurementTime - startMeasurementTime);
            double readBytes =
                    endTimeResourceMetrics.get(StatKeys.READ_BYTES.getLabel())
                            - startTimeResourceMetrics.get(StatKeys.READ_BYTES.getLabel());
            double writeBytes =
                    endTimeResourceMetrics.get(StatKeys.WRITE_BYTES.getLabel())
                            - startTimeResourceMetrics.get(StatKeys.WRITE_BYTES.getLabel());
            double readSyscalls =
                    endTimeResourceMetrics.get(StatKeys.SYSCR.getLabel())
                            - startTimeResourceMetrics.get(StatKeys.SYSCR.getLabel());
            double writeSyscalls =
                    endTimeResourceMetrics.get(StatKeys.SYSCW.getLabel())
                            - startTimeResourceMetrics.get(StatKeys.SYSCW.getLabel());
            double readPcBytes =
                    endTimeResourceMetrics.get(StatKeys.RCHAR.getLabel())
                            - startTimeResourceMetrics.get(StatKeys.RCHAR.getLabel())
                            - readBytes;
            double writePcBytes =
                    endTimeResourceMetrics.get(StatKeys.WCHAR.getLabel())
                            - startTimeResourceMetrics.get(StatKeys.WCHAR.getLabel())
                            - writeBytes;
            readBytes /= duration;
            readSyscalls /= duration;
            writeBytes /= duration;
            writeSyscalls /= duration;
            readPcBytes /= duration;
            writePcBytes /= duration;

            return new IOMetrics(
                    readBytes,
                    readSyscalls,
                    writeBytes,
                    writeSyscalls,
                    readBytes + writeBytes,
                    readSyscalls + writeSyscalls,
                    readPcBytes,
                    writePcBytes,
                    readPcBytes + writePcBytes);
        }
        return null;
    }
}
