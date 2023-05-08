/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.event_process;

public interface EventProcessor {
    int BATCH_LIMIT = 500;

    void initializeProcessing(long startTime, long endTime);

    void finalizeProcessing();

    void processEvent(Event event);

    boolean shouldProcessEvent(Event event);

    void commitBatchIfRequired();
}
