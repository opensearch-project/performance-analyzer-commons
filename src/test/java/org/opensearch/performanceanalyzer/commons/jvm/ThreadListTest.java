/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.jvm;


import org.junit.Before;
import org.opensearch.performanceanalyzer.commons.OSMetricsGeneratorFactory;

// This test only runs in linux systems as some of the static members of the ThreadList
// class are specific to Linux.
public class ThreadListTest {
    @Before
    public void before() {
        org.junit.Assume.assumeNotNull(OSMetricsGeneratorFactory.getInstance());
    }

    //    @Test
    //    public void testNullThreadInfo() throws InterruptedException {
    //        CommonStats.ERRORS_AND_EXCEPTIONS_AGGREGATOR =
    //                new SampleAggregator(StatExceptionCode.values());
    //        String propertyName = "clk.tck";
    //        String old_clk_tck = System.getProperty(propertyName);
    //        System.setProperty(propertyName, "100");
    //        ThreadInfo[] infos = ThreadList.getAllThreadInfos();
    //        // Artificially injecting a null to simulate that the thread id does not exist
    //        // any more and therefore the corresponding threadInfo is null.
    //        infos[0] = null;
    //
    //        ThreadList.parseAllThreadInfos(infos);
    //        Assert.assertTrue(
    //                CommonsTestHelper.verify(StatExceptionCode.JVM_THREAD_ID_NO_LONGER_EXISTS));
    //        if (old_clk_tck != null) {
    //            System.setProperty(propertyName, old_clk_tck);
    //        }
    //    }
}
