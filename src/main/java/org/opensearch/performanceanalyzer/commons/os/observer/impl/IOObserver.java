/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os.observer.impl;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.performanceanalyzer.commons.os.OSGlobals;
import org.opensearch.performanceanalyzer.commons.os.observer.ResourceObserver;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatExceptionCode;

public class IOObserver implements ResourceObserver<Long> {
    private static final Logger LOGGER = LogManager.getLogger(IOObserver.class);

    public enum StatKeys {
        READ_BYTES("read_bytes"),
        WRITE_BYTES("write_bytes"),
        SYSCR("syscr"),
        SYSCW("syscw"),
        RCHAR("rchar"),
        WCHAR("wchar");

        public final String label;

        public String getLabel() {
            return label;
        }

        StatKeys(String label) {
            this.label = label;
        }

        public static String[] getStatKeys() {
            return Stream.of(StatKeys.values()).map(StatKeys::getLabel).toArray(String[]::new);
        }
    }

    @Override
    public Map<String, Long> observe(String threadId) {
        try (FileReader fileReader =
                        new FileReader(
                                new File(
                                        "/proc/"
                                                + OSGlobals.getPid()
                                                + "/task/"
                                                + threadId
                                                + "/io"));
                BufferedReader bufferedReader = new BufferedReader(fileReader); ) {
            String line;
            Map<String, Long> kvmap = new HashMap<>();
            while ((line = bufferedReader.readLine()) != null) {
                String[] toks = line.split("[: ]+");
                String key = toks[0];
                long val = Long.parseLong(toks[1]);
                kvmap.put(key, val);
            }
            return kvmap;
        } catch (FileNotFoundException e) {
            LOGGER.debug("FileNotFound in parse with exception: {}", () -> e.toString());
        } catch (Exception e) {
            LOGGER.debug(
                    "Error In addSample Tid for: {}  with error: {} with ExceptionCode: {}",
                    () -> threadId,
                    () -> e.toString(),
                    () -> StatExceptionCode.THREAD_IO_ERROR.toString());
        }
        return Collections.emptyMap();
    }
}
