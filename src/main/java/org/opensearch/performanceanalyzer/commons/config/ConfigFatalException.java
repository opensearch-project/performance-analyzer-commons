/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.config;

class ConfigFatalException extends Exception {
    ConfigFatalException(String message) {
        super(message);
    }
}
