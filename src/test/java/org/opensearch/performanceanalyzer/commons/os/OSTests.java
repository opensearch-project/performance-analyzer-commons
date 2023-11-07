/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.stream.Collectors;

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
@PrepareForTest({SchemaFileParser.class, OSGlobals.class})
public abstract class OSTests {
    Map<String, Map<String, Object>> tidKVMap;
    Map<String, Map<String, Object>> nextTidKVMap;

    public OSTests(SortedMap<String, Map<String, Object>> tidKVMap, SortedMap<String, Map<String, Object>> nextTidKVMap, String pid, List<String> tids, long scClkTlk, long millisStart, long millisEnd) throws Exception {
        // mock OSGlobals
        PowerMockito.mockStatic(OSGlobals.class);
        PowerMockito.when(OSGlobals.getPid()).thenReturn(pid);
        PowerMockito.when(OSGlobals.getTids()).thenReturn(tids);
        PowerMockito.when(OSGlobals.getScClkTck()).thenReturn(scClkTlk);

        // mock System.currentTimeMillis()
        // used by ThreadSched to compute SchedMetric
        PowerMockito.mockStatic(System.class);
        // having the time difference = 1000ms
        // means that contextSwitchRate = difference in totctxsws
        PowerMockito.when(System.currentTimeMillis()).thenReturn(millisStart, millisEnd);

        // mock SchemaFileParser (used by ThreadSched to read procfiles)
        SchemaFileParser schemaFileParser = Mockito.mock(SchemaFileParser.class);

        // create an array that contains all the values of tidKVMap
        Object[] tidArr = tidKVMap.values().toArray();
        Object[] nextTidArr = nextTidKVMap.values().toArray();

        PowerMockito.when(schemaFileParser.parse())
                .thenReturn((Map<String, Object>) tidArr[0], (Map<String, Object>[]) Arrays.copyOfRange(tidArr, 1, nextTidArr.length))
                .thenReturn((Map<String, Object>) nextTidArr[0], (Map<String, Object>[]) Arrays.copyOfRange(nextTidArr, 1, nextTidArr.length));

        PowerMockito.whenNew(SchemaFileParser.class)
                .withAnyArguments()
                .thenReturn(schemaFileParser);
    }
}
