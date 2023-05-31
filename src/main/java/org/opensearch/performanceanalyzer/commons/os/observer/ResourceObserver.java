/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os.observer;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.opensearch.performanceanalyzer.commons.os.OSGlobals;

/**
 * Observers resources consumption by the threads
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
     * Retrieves the metric for the given thread, if it is present
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

    /**
     * Retrieves the metrics for all available threads
     *
     * @return map of threads and associated metrics
     */
    default Map<String, Map<String, T>> observe() {
        List<String> threadIds = OSGlobals.getTids();
        return threadIds.stream()
                .collect(Collectors.toMap(threadId -> threadId, threadId -> observe(threadId)));
    }
}
