/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.rca.stats.impl;


import java.util.Collections;
import java.util.List;
import org.opensearch.performanceanalyzer.commons.rca.stats.Statistics;
import org.opensearch.performanceanalyzer.commons.rca.stats.impl.vals.Value;

public class Sample implements IStatistic<Value> {
    private Number value;
    private boolean empty;

    public Sample() {
        empty = true;
    }

    @Override
    public Statistics type() {
        return Statistics.SAMPLE;
    }

    @Override
    public void calculate(String key, Number value) {
        this.value = value;
        empty = false;
    }

    @Override
    public List<Value> get() {
        return Collections.singletonList(new Value(value));
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }
}
