/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.jvm;

import java.lang.management.ThreadInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opensearch.performanceanalyzer.commons.CommonsTestHelper;
import org.opensearch.performanceanalyzer.commons.OSMetricsGeneratorFactory;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatExceptionCode;

// This test only runs in linux systems as the some static members of the ThreadList
// class are specific to Linux.
public class ThreadListTest {
    @Before
    public void before() {
        System.setProperty("performanceanalyzer.metrics.log.enabled", "False");
        org.junit.Assume.assumeNotNull(OSMetricsGeneratorFactory.getInstance());
    }

    @Test
    public void testNullThreadInfo() throws InterruptedException {
        String propertyName = "clk.tck";
        String old_clk_tck = System.getProperty(propertyName);
        System.setProperty(propertyName, "100");
        ThreadInfo[] infos = ThreadList.getAllThreadInfos();
        // Artificially injecting a null to simulate that the thread id does not exist
        // any more and therefore the corresponding threadInfo is null.
        infos[0] = null;

        ThreadList.parseAllThreadInfos(infos);
        Assert.assertTrue(
                CommonsTestHelper.verifyStatException(
                        StatExceptionCode.JVM_THREAD_ID_NO_LONGER_EXISTS.toString()));
        if (old_clk_tck != null) {
            System.setProperty(propertyName, old_clk_tck);
        }
    }
}
