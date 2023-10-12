/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.observer;


import java.util.Map;

/**
 * Observers resources consumption
 *
 * @param <T> Type of the metric
 */
public interface ResourceObserver<T> {

    /**
     * Retrieves the metrics for the given thread
     *
     * @param threadId
     * @return
     */
    T observe(String threadId);

    Map<String, T> observe();
}
