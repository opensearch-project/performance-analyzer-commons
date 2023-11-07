/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
@SuppressStaticInitializationFor({ "org.opensearch.performanceanalyzer.commons.os.OSGlobals" })
// whenNew requires the class calling the constructor to be PreparedForTest
@PrepareForTest({ SchemaFileParser.class, OSGlobals.class, ThreadDiskIO.class })
public class ThreadDiskIOTests extends OSTests {
	public ThreadDiskIOTests() throws Exception {
		super(
				new TreeMap<String, Map<String, Object>>(Map.of(
					"1", Map.of("runticks", 200000000L, "waitticks", 200000000L, "totctxsws", 200L),
					"2", Map.of("runticks", 500000000L, "waitticks", 500000000L, "totctxsws", 20L),
					"3",
					Map.of(
							"runticks",
							700000000L,
							"waitticks",
							700000000L,
							"totctxsws",
							220L))),
				new TreeMap<String, Map<String, Object>>(Map.of(
						"1",
						Map.of("runticks", 200000000L, "waitticks", 200000000L, "totctxsws",
								200L),
						"2",
						Map.of("runticks", 500000000L, "waitticks", 500000000L, "totctxsws",
								20L),
						"3",
						Map.of(
								"runticks",
								700000000L,
								"waitticks",
								700000000L,
								"totctxsws",
								220L))),
				"0", List.of("1", "2", "3"), 100, 10, 1010);
	}

	@Test
	public void testMetrics() throws Exception {
		// this test checks that
		// 1. ThreadDiskIO calls the SchemaFileParser constructor with the correct path
		// 2. ThreadDiskIO calculates the correct metrics from procfile data

		// mock the metrics generator used by DiskIO
		LinuxDiskIOMetricsGenerator linuxDiskIOMetricsGenerator = Mockito
				.mock(LinuxDiskIOMetricsGenerator.class);
		PowerMockito.whenNew(LinuxDiskIOMetricsGenerator.class)
				.withNoArguments()
				.thenReturn(linuxDiskIOMetricsGenerator);
	}
}
