/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
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

    public OSTests(
            SortedMap<String, Map<String, Object>> tidKVMap,
            SortedMap<String, Map<String, Object>> nextTidKVMap,
            String pid,
            List<String> tids,
            long scClkTlk,
            long millisStart,
            long millisEnd)
            throws Exception {
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

        this.tidKVMap = tidKVMap;
        this.nextTidKVMap = nextTidKVMap;
    }

    void mockSchemaFileParser() throws Exception {
        // mock SchemaFileParser (used by ThreadSched and ThreadCPU to read procfiles)
        SchemaFileParser schemaFileParser = Mockito.mock(SchemaFileParser.class);

        // create a list that contains all the values of tidKVMap
        List<Map<String, Object>> tidArr = new ArrayList<Map<String, Object>>(tidKVMap.values());
        List<Map<String, Object>> nextTidArr =
                new ArrayList<Map<String, Object>>(nextTidKVMap.values());

        var thenReturn = PowerMockito.when(schemaFileParser.parse()).thenReturn(tidArr.get(0));
        for (int i = 1; i < tidArr.size(); i++) {
            thenReturn = thenReturn.thenReturn(tidArr.get(i));
        }
        for (int i = 0; i < nextTidArr.size(); i++) {
            thenReturn = thenReturn.thenReturn(nextTidArr.get(i));
        }

        PowerMockito.whenNew(SchemaFileParser.class)
                .withAnyArguments()
                .thenReturn(schemaFileParser);
    }
}
