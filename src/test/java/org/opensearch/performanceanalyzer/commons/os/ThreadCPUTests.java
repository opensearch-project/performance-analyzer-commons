/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.opensearch.performanceanalyzer.commons.metrics_generator.linux.LinuxCPUPagingActivityGenerator;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
// whenNew requires the class calling the constructor to be PreparedForTest
@PrepareForTest({SchemaFileParser.class, OSGlobals.class, ThreadCPU.class})
public class ThreadCPUTests extends OSTests {
    public ThreadCPUTests() throws Exception {
        super(
                new TreeMap<String, Map<String, Object>>(
                        Map.of(
                                "1",
                                        Map.of(
                                                "pid", 1, "minflt", 1L, "majflt", 1L, "utime", 1L,
                                                "stime", 1L, "rss", 1L),
                                "2",
                                        Map.of(
                                                "pid", 2, "minflt", 5L, "majflt", 10L, "utime", 3L,
                                                "stime", 13L, "rss", 1L))),
                new TreeMap<String, Map<String, Object>>(
                        Map.of(
                                "1",
                                        Map.of(
                                                "pid", 1, "minflt", 10L, "majflt", 100L, "utime",
                                                6L, "stime", 6L, "rss", 2L),
                                "2",
                                        Map.of(
                                                "pid", 2, "minflt", 10L, "majflt", 15L, "utime",
                                                13L, "stime", 18L, "rss", 4L))),
                "0",
                List.of("1", "2", "3"),
                100,
                10,
                1010);

        mockSchemaFileParser();
    }

    @Test
    public void testMetrics() throws Exception {
        LinuxCPUPagingActivityGenerator linuxCPUPagingActivityGenerator =
                Mockito.mock(LinuxCPUPagingActivityGenerator.class);
        PowerMockito.whenNew(LinuxCPUPagingActivityGenerator.class)
                .withNoArguments()
                .thenReturn(linuxCPUPagingActivityGenerator);

        ThreadCPU.INSTANCE.addSample();

        PowerMockito.verifyNew(SchemaFileParser.class)
                .withArguments(
                        eq("/proc/0/task/1/stat"),
                        isA(String[].class),
                        isA(SchemaFileParser.FieldTypes[].class),
                        eq(true));
        PowerMockito.verifyNew(SchemaFileParser.class)
                .withArguments(
                        eq("/proc/0/task/2/stat"),
                        isA(String[].class),
                        isA(SchemaFileParser.FieldTypes[].class),
                        eq(true));

        ThreadCPU.INSTANCE.addSample();

        verify(linuxCPUPagingActivityGenerator)
                .setPagingActivities("1", new Double[] {14.0, 9.0, 4.0});
        verify(linuxCPUPagingActivityGenerator).setCPUUtilization("1", 0.29);
        verify(linuxCPUPagingActivityGenerator)
                .setPagingActivities("2", new Double[] {5.0, 5.0, 4.0});
        verify(linuxCPUPagingActivityGenerator).setCPUUtilization("2", 0.15);
    }
}
