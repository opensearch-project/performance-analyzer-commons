/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;

import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
                    "1", Map.of("runticks", "1", "waitticks", "2", "totctxsws", "3"),
                    "2", Map.of("runticks", "4", "waitticks", "5", "totctxsws", "6"),
                    "3", Map.of("runticks", "7", "waitticks", "8", "totctxsws", "9"));

    @Before
    public void setUp() throws Exception {
        // mock OSGlobals
        PowerMockito.mockStatic(OSGlobals.class);
        PowerMockito.when(OSGlobals.getPid()).thenReturn("0");
        PowerMockito.when(OSGlobals.getTids()).thenReturn(List.of("1", "2", "3"));
    }

    @Test
    public void testMetrics() throws Exception {
        // mock SchemaFileParser (used by ThreadSched to read procfiles)
        SchemaFileParser schemaFileParser = Mockito.mock(SchemaFileParser.class);

        PowerMockito.when(schemaFileParser.parse())
                .thenReturn(tidKVMap.get("1"), tidKVMap.get("2"), tidKVMap.get("3"));

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
    }
}
