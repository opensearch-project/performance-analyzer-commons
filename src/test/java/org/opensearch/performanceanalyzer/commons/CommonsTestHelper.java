/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons;


import org.opensearch.performanceanalyzer.commons.collectors.StatsCollector;

public class CommonsTestHelper {
    public static boolean verifyStatException(String exceptionCode) throws InterruptedException {
        final int MAX_TIME_TO_WAIT_MILLIS = 10_000;
        int waited_for_millis = 0;
        while (waited_for_millis++ < MAX_TIME_TO_WAIT_MILLIS) {
            if (StatsCollector.instance().getCounters().containsKey(exceptionCode)) {
                return true;
            }
            Thread.sleep(1);
        }
        return false;
    }
}
