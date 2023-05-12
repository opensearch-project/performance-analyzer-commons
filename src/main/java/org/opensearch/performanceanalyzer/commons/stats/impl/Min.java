/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats.impl;


import org.opensearch.performanceanalyzer.commons.stats.Statistics;

/** To get the minimum observed value. */
public class Min extends MinMaxCommon {
    public Min() {
        super(Long.MAX_VALUE);
    }

    @Override
    boolean shouldUpdate(Number v) {
        return v.doubleValue() < getOldVal().doubleValue();
    }

    @Override
    public Statistics type() {
        return Statistics.MIN;
    }
}
