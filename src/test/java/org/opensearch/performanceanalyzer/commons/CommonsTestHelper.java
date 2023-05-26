/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons;


import org.opensearch.performanceanalyzer.commons.stats.CommonStats;
import org.opensearch.performanceanalyzer.commons.stats.measurements.MeasurementSet;

public class CommonsTestHelper {
    public static boolean verify(MeasurementSet measurementSet) throws InterruptedException {
        final int MAX_TIME_TO_WAIT_MILLIS = 10_000;
        int waited_for_millis = 0;
        while (waited_for_millis++ < MAX_TIME_TO_WAIT_MILLIS) {
            if (CommonStats.RCA_STATS_REPORTER.isMeasurementCollected(measurementSet)) {
                return true;
            }
            Thread.sleep(1);
        }
        return false;
    }
}
