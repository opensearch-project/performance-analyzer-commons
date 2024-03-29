/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.hwnet;

import java.io.File;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.performanceanalyzer.commons.collectors.DiskMetrics;
import org.opensearch.performanceanalyzer.commons.metrics_generator.DiskMetricsGenerator;
import org.opensearch.performanceanalyzer.commons.metrics_generator.linux.LinuxDiskMetricsGenerator;
import org.opensearch.performanceanalyzer.commons.os.SchemaFileParser;
import org.opensearch.performanceanalyzer.commons.util.Util;

public class Disks {
    private static Map<String, Map<String, Object>> diskKVMap = new HashMap<>();
    private static Map<String, Map<String, Object>> olddiskKVMap = new HashMap<>();
    private static long kvTimestamp = 0;
    private static long oldkvTimestamp = 0;
    private static Set<String> diskList = new HashSet<>();
    private static final Logger LOG = LogManager.getLogger(Disks.class);
    private static LinuxDiskMetricsGenerator linuxDiskMetricsHandler =
            new LinuxDiskMetricsGenerator();

    private static String statKeys[] = {
        "majno", // 1
        "minno",
        "name",
        "rdone",
        "rmerged",
        "rsectors",
        "rtime",
        "wdone",
        "wmerged",
        "wsectors", // 10
        "wtime",
        "inprogressIO",
        "IOtime",
        "weightedIOtime"
    };

    private static SchemaFileParser.FieldTypes statTypes[] = {
        SchemaFileParser.FieldTypes.INT, // 1
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.STRING,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG, // 10
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG
    };

    static {
        Util.invokePrivileged(() -> listDisks());
        oldkvTimestamp = System.currentTimeMillis();
        kvTimestamp = oldkvTimestamp;
    }

    private static StringBuilder value = new StringBuilder();

    private static void listDisks() {
        try {
            File file = new File("/sys/block");
            File[] files = file.listFiles();
            if (files != null) {
                for (File dfile : files) {
                    if (dfile != null && !dfile.getCanonicalPath().contains("/virtual/")) {
                        diskList.add(dfile.getName());
                    }
                }
            }
        } catch (Exception e) {
            LOG.debug("Exception in calling listDisks with details: {}", () -> e.toString());
        }
    }

    public static DiskMetricsGenerator getDiskMetricsHandler() {
        return linuxDiskMetricsHandler;
    }

    public static void addSample() {
        olddiskKVMap.clear();
        olddiskKVMap.putAll(diskKVMap);
        diskKVMap.clear();

        SchemaFileParser parser = new SchemaFileParser("/proc/diskstats", statKeys, statTypes);
        List<Map<String, Object>> sampleList = parser.parseMultiple();

        for (Map<String, Object> sample : sampleList) {
            String diskname = (String) (sample.get("name"));
            if (!diskList.contains(diskname)) {
                diskKVMap.put(diskname, sample);
            }
        }

        oldkvTimestamp = kvTimestamp;
        kvTimestamp = System.currentTimeMillis();

        calculateDiskMetrics();
    }

    private static void calculateDiskMetrics() {

        linuxDiskMetricsHandler.setDiskMetricsMap(getMetricsMap());
    }

    public static Map<String, DiskMetrics> getMetricsMap() {
        Map<String, DiskMetrics> map = new HashMap<>();
        if (kvTimestamp > oldkvTimestamp) {
            for (Map.Entry<String, Map<String, Object>> entry : diskKVMap.entrySet()) {
                String disk = entry.getKey();
                Map<String, Object> m = entry.getValue();
                Map<String, Object> mold = olddiskKVMap.get(disk);
                if (mold != null) {
                    DiskMetrics dm = new DiskMetrics();
                    dm.name = (String) m.get("name");
                    double rwdeltatime =
                            1.0
                                    * ((long) m.get("rtime")
                                            + (long) m.get("wtime")
                                            - (long) mold.get("rtime")
                                            - (long) mold.get("wtime"));
                    double rwdeltaiops =
                            1.0
                                    * ((long) m.get("rdone")
                                            + (long) m.get("wdone")
                                            - (long) mold.get("rdone")
                                            - (long) mold.get("wdone"));
                    double rwdeltasectors =
                            1.0
                                    * ((long) m.get("rsectors")
                                            + (long) m.get("wsectors")
                                            - (long) mold.get("rsectors")
                                            - (long) mold.get("wsectors"));

                    dm.utilization = rwdeltatime / (kvTimestamp - oldkvTimestamp);
                    dm.await = (rwdeltaiops > 0) ? rwdeltatime / rwdeltaiops : 0;
                    dm.serviceRate =
                            (rwdeltatime > 0) ? rwdeltasectors * 512 * 1.0e-3 / rwdeltatime : 0;

                    map.put(disk, dm);
                }
            }
        }
        return map;
    }

    public static void runOnce() {
        addSample();
        System.out.println("disks: " + getMetricsMap());
    }
}
