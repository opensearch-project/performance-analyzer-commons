/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os.observer.impl;


import java.util.Map;
import java.util.stream.Stream;
import org.opensearch.performanceanalyzer.commons.os.OSGlobals;
import org.opensearch.performanceanalyzer.commons.os.SchemaFileParser;
import org.opensearch.performanceanalyzer.commons.os.SchemaFileParser.FieldTypes;
import org.opensearch.performanceanalyzer.commons.os.observer.ResourceObserver;

public class SchedObserver implements ResourceObserver {

    public enum SchedKeys {
        RUNTICKS("runticks"),
        WAITTICKS("waitticks"),
        TOTCTXSWS("totctxsws");
        private final String label;

        public String getLabel() {
            return label;
        }

        SchedKeys(String label) {
            this.label = label;
        }

        public static String[] getStatKeys() {
            return Stream.of(SchedKeys.values()).map(SchedKeys::getLabel).toArray(String[]::new);
        }
    }

    private static FieldTypes[] schedTypes = {
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG
    };

    @Override
    public Map<String, Object> observe(String threadId) {
        return (new SchemaFileParser(
                        "/proc/" + OSGlobals.getPid() + "/task/" + threadId + "/schedstat",
                        SchedKeys.getStatKeys(),
                        schedTypes))
                .parse();
    }
}
