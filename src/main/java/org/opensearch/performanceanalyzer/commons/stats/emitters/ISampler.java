/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats.emitters;

import org.opensearch.performanceanalyzer.commons.stats.collectors.SampleAggregator;

public interface ISampler {
    void sample(SampleAggregator sampleCollector);
}
