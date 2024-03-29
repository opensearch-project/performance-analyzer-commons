/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.stats.eval.impl;

import java.util.Collections;
import java.util.List;
import org.opensearch.performanceanalyzer.commons.stats.eval.impl.vals.NamedAggregateValue;

/**
 * This is a utility class that is shares methods that are used for statistics where values are
 * compared with all the previous samples but no mathematical calculation is done. Things such as
 * max and min.
 */
abstract class MinMaxCommon implements IStatistic<NamedAggregateValue> {
    private final Number initialVal;

    private Number oldVal;
    private String oldKey;

    private boolean empty;

    public MinMaxCommon(Number initialVal) {
        this.initialVal = initialVal;
        this.oldVal = initialVal;
        this.oldKey = "";
        this.empty = true;
    }

    /**
     * Based on the new observation, should the metric be updated.
     *
     * @param v The new new observation.
     * @return true if the member value needs to be updated, false otherwise.
     */
    abstract boolean shouldUpdate(Number v);

    /**
     * This is just a comparison followed by an update if required.
     *
     * @param key How to identify each sample in the measurement. Say, if we are measure the time
     *     spent in the call of the operate() method of the RCA graph and want to find the RCA class
     *     that has the most expensive call. So the key will be the name of the RCA class.
     * @param value The measurement on which statistics are calculated.
     */
    @Override
    public void calculate(String key, Number value) {
        synchronized (this) {
            if (shouldUpdate(value)) {
                oldVal = value;
                oldKey = key;
            }
        }
        empty = false;
    }

    @Override
    public synchronized List<NamedAggregateValue> get() {
        return Collections.singletonList(new NamedAggregateValue(oldVal, type(), oldKey));
    }

    public Number getOldVal() {
        return oldVal;
    }

    @Override
    public boolean isEmpty() {
        return empty;
    }
}
