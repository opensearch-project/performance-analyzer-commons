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
import java.util.TreeMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.opensearch.performanceanalyzer.commons.metrics_generator.linux.LinuxSchedMetricsGenerator;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
// whenNew requires the class calling the constructor to be PreparedForTest
@PrepareForTest({SchemaFileParser.class, OSGlobals.class, ThreadSched.class})
public class ThreadSchedTests extends OSTests {
    public ThreadSchedTests() throws Exception {
        super(
                new TreeMap<String, Map<String, Object>>(
                        Map.of(
                                "1",
                                Map.of(
                                        "runticks",
                                        100000000L,
                                        "waitticks",
                                        100000000L,
                                        "totctxsws",
                                        100L),
                                "2",
                                Map.of(
                                        "runticks",
                                        100000000L,
                                        "waitticks",
                                        100000000L,
                                        "totctxsws",
                                        10L),
                                "3",
                                Map.of(
                                        "runticks",
                                        500000000L,
                                        "waitticks",
                                        500000000L,
                                        "totctxsws",
                                        120L))),
                new TreeMap<String, Map<String, Object>>(
                        Map.of(
                                "1",
                                Map.of(
                                        "runticks",
                                        200000000L,
                                        "waitticks",
                                        200000000L,
                                        "totctxsws",
                                        200L),
                                "2",
                                Map.of(
                                        "runticks",
                                        500000000L,
                                        "waitticks",
                                        500000000L,
                                        "totctxsws",
                                        20L),
                                "3",
                                Map.of(
                                        "runticks",
                                        700000000L,
                                        "waitticks",
                                        700000000L,
                                        "totctxsws",
                                        220L))),
                "0",
                List.of("1", "2", "3"),
                100,
                10,
                1010);

        mockSchemaFileParser();
    }

    @Test
    public void testMetrics() throws Exception {
        // this test checks that
        // 1. ThreadSched calls the SchemaFileParser constructor with the correct path
        // 2. ThreadSched calculates the correct metrics from procfile data

        // mock the metrics generator used by ThreadSched
        LinuxSchedMetricsGenerator linuxSchedMetricsGenerator =
                Mockito.mock(LinuxSchedMetricsGenerator.class);
        PowerMockito.whenNew(LinuxSchedMetricsGenerator.class)
                .withNoArguments()
                .thenReturn(linuxSchedMetricsGenerator);

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
