/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;

import org.opensearch.performanceanalyzer.commons.util.JsonConverter;

public class MetricStatus {

    /**
     * converts any object to a JSON string and return that string
     *
     * @return A string containing a JSON representation of the object
     */
    public String serialize() {
        return JsonConverter.writeValueAsString(this);
    }
}
