/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.opensearch.performanceanalyzer.commons.metrics_generator.linux.LinuxSchedMetricsGenerator;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.management.*")
@SuppressStaticInitializationFor({"org.opensearch.performanceanalyzer.commons.os.OSGlobals"})
// whenNew requires the class calling the constructor to be PreparedForTest
@PrepareForTest({SchemaFileParser.class, OSGlobals.class, ThreadSched.class})
public class ThreadSchedTests {

    private Map<String, Map<String, Object>> tidKVMap =
            Map.of(
                    "1", Map.of("runticks", 100000000L, "waitticks", 100000000L, "totctxsws", 100L),
                    "2", Map.of("runticks", 100000000L, "waitticks", 100000000L, "totctxsws", 10L),
                    "3",
                            Map.of(
                                    "runticks",
                                    500000000L,
                                    "waitticks",
                                    500000000L,
                                    "totctxsws",
                                    120L));

    private Map<String, Map<String, Object>> nextTidKVMap =
            Map.of(
                    "1", Map.of("runticks", 200000000L, "waitticks", 200000000L, "totctxsws", 200L),
                    "2", Map.of("runticks", 500000000L, "waitticks", 500000000L, "totctxsws", 20L),
                    "3",
                            Map.of(
                                    "runticks",
                                    700000000L,
                                    "waitticks",
                                    700000000L,
                                    "totctxsws",
                                    220L));

    @Test
    public void testMetrics() throws Exception {
        // this test checks that
        // 1. ThreadSched calls the SchemaFileParser constructor with the correct path
        // 2. ThreadSched calculates the correct metrics from procfile data

        // mock OSGlobals
        PowerMockito.mockStatic(OSGlobals.class);
        PowerMockito.when(OSGlobals.getPid()).thenReturn("0");
        PowerMockito.when(OSGlobals.getTids()).thenReturn(List.of("1", "2", "3"));

        // mock System.currentTimeMillis()
        // used by ThreadSched to compute SchedMetric
        PowerMockito.mockStatic(System.class);
        // having the time difference = 1000ms
        // means that contextSwitchRate = difference in totctxsws
        PowerMockito.when(System.currentTimeMillis()).thenReturn(10L, 1010L);

        // mock the metrics generator used by ThreadSched
        LinuxSchedMetricsGenerator linuxSchedMetricsGenerator =
                Mockito.mock(LinuxSchedMetricsGenerator.class);
        PowerMockito.whenNew(LinuxSchedMetricsGenerator.class)
                .withNoArguments()
                .thenReturn(linuxSchedMetricsGenerator);

        // mock SchemaFileParser (used by ThreadSched to read procfiles)
        SchemaFileParser schemaFileParser = Mockito.mock(SchemaFileParser.class);

        PowerMockito.when(schemaFileParser.parse())
                .thenReturn(tidKVMap.get("1"), tidKVMap.get("2"), tidKVMap.get("3"))
                .thenReturn(nextTidKVMap.get("1"), nextTidKVMap.get("2"), nextTidKVMap.get("3"));

        PowerMockito.whenNew(SchemaFileParser.class)
                .withAnyArguments()
                .thenReturn(schemaFileParser);

        ThreadSched.INSTANCE.addSample();

        // assert that ThreadSched calls the SchemaFileParser constructor with the
        // correct path
        PowerMockito.verifyNew(SchemaFileParser.class)
                .withArguments(
                        eq("/proc/0/task/1/schedstat"),
                        isA(String[].class),
                        isA(SchemaFileParser.FieldTypes[].class));
        PowerMockito.verifyNew(SchemaFileParser.class)
                .withArguments(
                        eq("/proc/0/task/2/schedstat"),
                        isA(String[].class),
                        isA(SchemaFileParser.FieldTypes[].class));
        PowerMockito.verifyNew(SchemaFileParser.class)
                .withArguments(
                        eq("/proc/0/task/3/schedstat"),
                        isA(String[].class),
                        isA(SchemaFileParser.FieldTypes[].class));

        assertEquals(tidKVMap, ThreadSched.INSTANCE.getTidKVMap());

        ThreadSched.INSTANCE.addSample();

        // verify that the metrics generator is given correct metrics
        verify(linuxSchedMetricsGenerator)
                .setSchedMetric("1", new ThreadSched.SchedMetrics(0.001, 0.001, 100.0));
        verify(linuxSchedMetricsGenerator)
                .setSchedMetric("2", new ThreadSched.SchedMetrics(0.04, 0.04, 10.0));
        verify(linuxSchedMetricsGenerator)
                .setSchedMetric("3", new ThreadSched.SchedMetrics(0.002, 0.002, 100.0));
    }
}
