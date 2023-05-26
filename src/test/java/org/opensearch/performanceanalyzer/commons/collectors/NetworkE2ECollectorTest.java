/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Before;
import org.opensearch.performanceanalyzer.commons.metrics.MetricsConfiguration;

public class NetworkE2ECollectorTest extends AbstractCollectorTest {
    private static final Logger LOG = LogManager.getLogger(NetworkE2ECollectorTest.class);

    @Before
    public void setup() {
        int interval =
                MetricsConfiguration.CONFIG_MAP.get(NetworkE2ECollector.class).samplingInterval;
        setUut(new NetworkE2ECollector("NetworkE2ECollector", interval));
    }

    @Override
    public void validateMetric(String metric) throws Exception {
        TCPStatus tcpStatus = mapper.readValue(metric, TCPStatus.class);
        Assert.assertFalse(tcpStatus.getDest().isEmpty());
        // TODO implement further validation of the MetricStatus
        int numFlows = tcpStatus.getNumFlows();
        double txQ = tcpStatus.getTxQ();
        double rxQ = tcpStatus.getRxQ();
        double curLost = tcpStatus.getCurLost();
        double sndCWND = tcpStatus.getSndCWND();
        double ssThresh = tcpStatus.getSsThresh();
        LOG.info(
                "numFlows {}, txQ {}, rxQ {}, curLost {}, sendCWND {}, ssThresh {}",
                numFlows,
                txQ,
                rxQ,
                curLost,
                sndCWND,
                ssThresh);
    }
}
