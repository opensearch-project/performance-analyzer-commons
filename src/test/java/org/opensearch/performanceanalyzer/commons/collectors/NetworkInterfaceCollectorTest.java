/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;

import org.junit.Before;

public class NetworkInterfaceCollectorTest extends AbstractCollectorTest {
    @Before
    public void setup() {
        setUut(new NetworkInterfaceCollector());
    }

    @Override
    public void validateMetric(String metric) throws Exception {
        NetInterfaceSummary interfaceSummary = mapper.readValue(metric, NetInterfaceSummary.class);
        // TODO implement further validation of the MetricStatus
        NetInterfaceSummary.Direction direction = interfaceSummary.getDirection();
        double packetRate4 = interfaceSummary.getPacketRate4();
        double dropRate4 = interfaceSummary.getDropRate4();
        double packetRate6 = interfaceSummary.getPacketRate6();
        double dropRate6 = interfaceSummary.getPacketRate6();
        double bps = interfaceSummary.getBps();
    }
}
