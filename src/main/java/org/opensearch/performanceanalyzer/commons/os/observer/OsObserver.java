/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os.observer;


import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.opensearch.performanceanalyzer.commons.observer.ResourceObserver;
import org.opensearch.performanceanalyzer.commons.os.OSGlobals;

public abstract class OsObserver<T> implements ResourceObserver {

    @Override
    public Map<String, T> observe(String threadId) {
        throw new UnsupportedOperationException(
                "Observer abstraction can't be used to observer the thread");
    }

    /**
     * Retrieves the metrics for all available threads
     *
     * @return map of threads and associated metrics
     */
    @Override
    public Map<String, Map<String, T>> observe() {
        List<String> threadIds = OSGlobals.getTids();
        return threadIds.stream()
                .collect(Collectors.toMap(threadId -> threadId, threadId -> observe(threadId)));
    }
}
