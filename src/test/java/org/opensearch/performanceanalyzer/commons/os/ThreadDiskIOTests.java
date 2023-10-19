/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.opensearch.performanceanalyzer.commons.metrics_generator.linux.LinuxDiskIOMetricsGenerator;
import org.opensearch.performanceanalyzer.commons.metrics_generator.linux.LinuxSchedMetricsGenerator;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;
import org.junit.Test;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@SuppressStaticInitializationFor({"org.opensearch.performanceanalyzer.commons.os.OSGlobals"})
// whenNew requires the class calling the constructor to be PreparedForTest
@PrepareForTest({SchemaFileParser.class, OSGlobals.class, ThreadDiskIO.class})
public class ThreadDiskIOTests {

    public static void runOnce() {
        ThreadDiskIO.addSample();
        System.out.println(ThreadDiskIO.getIOUtilization().toString());
    }

    @Test
    public void testMetrics() throws Exception {
        // this test checks that
        // 1. ThreadDiskIO calls the SchemaFileParser constructor with the correct path
        // 2. ThreadDiskIO calculates the correct metrics from procfile data

        // mock OSGlobals
        PowerMockito.mockStatic(OSGlobals.class);
        PowerMockito.when(OSGlobals.getPid()).thenReturn("0");
        PowerMockito.when(OSGlobals.getTids()).thenReturn(List.of("1", "2", "3"));

        // mock System.currentTimeMillis()
        // used by DiskIO to compute SchedMetric
        PowerMockito.mockStatic(System.class);
        // having the time difference = 1000ms
        // means that contextSwitchRate = difference in totctxsws
        PowerMockito.when(System.currentTimeMillis()).thenReturn(10L, 1010L);

        // mock the metrics generator used by DiskIO
        LinuxDiskIOMetricsGenerator linuxDiskIOMetricsGenerator =
                Mockito.mock(LinuxDiskIOMetricsGenerator.class);
        PowerMockito.whenNew(LinuxDiskIOMetricsGenerator.class)
                .withNoArguments()
                .thenReturn(linuxDiskIOMetricsGenerator);

        // mock SchemaFileParser (used by ThreadSched to read procfiles)
        SchemaFileParser schemaFileParser = Mockito.mock(SchemaFileParser.class);
    }
}
