/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.os;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.apache.logging.log4j.util.Supplier;
import org.opensearch.performanceanalyzer.commons.config.ConfigStatus;
import org.opensearch.performanceanalyzer.commons.metrics.MetricsConfiguration;

public class OSGlobals {
    private static long scClkTck;
    private static String pid;
    private static final String CLK_TCK_SYS_PROPERTY_NAME = "clk.tck";

    private static final Logger LOGGER = LogManager.getLogger(OSGlobals.class);
    private static final long REFRESH_INTERVAL_MS =
            MetricsConfiguration.CONFIG_MAP.get(OSGlobals.class).samplingInterval;
    private static List<String> tids = new ArrayList<>();
    private static long lastUpdated = -1;

    static {
        try {
            pid = new File("/proc/self").getCanonicalFile().getName();
            getScClkTckFromConfig();
            enumTids();
            lastUpdated = System.currentTimeMillis();
        } catch (Exception e) {
            LOGGER.error(
                    (Supplier<?>)
                            () ->
                                    new ParameterizedMessage(
                                            "Error in static initialization of OSGlobals with exception: {}",
                                            e.toString()),
                    e);
        }
    }

    public static String getPid() {
        return pid;
    }

    public static long getScClkTck() {
        return scClkTck;
    }

    private static void getScClkTckFromConfig() throws Exception {
        try {
            scClkTck = Long.parseUnsignedLong(System.getProperty(CLK_TCK_SYS_PROPERTY_NAME));
        } catch (Exception e) {
            LOGGER.error(
                    (Supplier<?>)
                            () ->
                                    new ParameterizedMessage(
                                            "Error in reading/parsing clk.tck value: {}",
                                            e.toString()),
                    e);
            ConfigStatus.INSTANCE.setConfigurationInvalid();
        }
    }

    private static void enumTids() {
        tids.clear();
        tids.add(pid);

        File self = new File("/proc/self/task");
        File[] filesList = self.listFiles();
        if (filesList != null) {
            for (File f : filesList) {
                if (f.isDirectory()) {
                    String tid = f.getName();
                    tids.add(tid);
                }
            }
        }
    }

    static synchronized List<String> getTids() {
        long curtime = System.currentTimeMillis();
        if (curtime - lastUpdated > REFRESH_INTERVAL_MS) {
            enumTids();
            lastUpdated = curtime;
        }
        return new ArrayList<>(tids);
    }
}
