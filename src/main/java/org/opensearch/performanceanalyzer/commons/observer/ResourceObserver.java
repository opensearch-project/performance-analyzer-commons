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
    Map<String, T> observe(String threadId);

    /**
     * Retrieves the specified metric for the given thread, if it is present
     *
     * @param threadId id of the thread
     * @param metric metric name
     * @return metric value
     */
    default T observeMetricForThread(String threadId, String metric) {
        Map<String, T> threadSample = observe(threadId);
        if (threadSample == null || threadSample.isEmpty()) {
            return null;
        }

        return observe(threadId).get(metric);
    }

    Map<String, Map<String, T>> observe();
}
