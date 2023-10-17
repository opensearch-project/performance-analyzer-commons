/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;


import org.junit.Test;

public class ThreadDiskIOTests {

    public static void runOnce() {
        ThreadDiskIO.addSample();
        System.out.println(ThreadDiskIO.getIOUtilization().toString());
    }

    @Test
    public void testMetrics() {
        // this test checks that
        // 1. ThreadDiskIO calls the SchemaFileParser constructor with the correct path
        // 2. ThreadDiskIO calculates the correct metrics from procfile data
    }
}
