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
import org.opensearch.performanceanalyzer.commons.observer.ResourceObserver;

public class CPUObserver implements ResourceObserver<Object> {
    public enum StatKeys {
        PID("pid"),
        COMM("comm"),
        STATE("state"),
        PPID("ppid"),
        PGRP("pgrp"),
        SESSION("session"),
        TTYNR("ttynr"),
        TPGID("tpgid"),
        FLAGS("flags"),
        MINFLT("minflt"),
        CMNFLT("cminflt"),
        MAJFLT("majflt"),
        CMAJFLT("cmajflt"),
        UTIME("utime"),
        STIME("stime"),
        CUTIME("cutime"),
        CSTIME("cstime"),
        PRIO("prio"),
        NICE("nice"),
        NTHREADS("nthreads"),
        ITERALVALUE("itrealvalue"),
        STARTTIME("starttime"),
        VSIZE("vsize"),
        RSS("rss"),
        RSSLIM("rsslim"),
        STARTCODE("startcode"),
        ENDCODE("endcode"),
        STARTSTACK("startstack"),
        KSTKESP("kstkesp"),
        KSTKEIP("kstkeip"),
        SIGNAL("signal"),
        BLOCKED("blocked"),
        SIGIGNORE("sigignore"),
        SIGCATCH("sigcatch"),
        WCHAN("wchan"),
        NSWAP("nswap"),
        CNSWAP("cnswap"),
        EXISTSIG("exitsig"),
        CPU("cpu"),
        RTPRIO("rtprio"),
        SCHEDPOLICY("schedpolicy"),
        BIO_TICKS("bio_ticks"),
        VMTIME("vmtime"),
        CVMTIME("cvmtime");
        private final String label;

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

    private static FieldTypes[] statTypes = {
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.STRING,
        SchemaFileParser.FieldTypes.CHAR,
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.ULONG, // 10
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG, // 20
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG, // 30
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.ULONG,
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.INT, // 40
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.INT,
        SchemaFileParser.FieldTypes.INT
    };

    @Override
    public Map<String, Object> observe(String threadId) {
        return (new SchemaFileParser(
                        "/proc/" + OSGlobals.getPid() + "/task/" + threadId + "/stat",
                        StatKeys.getStatKeys(),
                        statTypes,
                        true))
                .parse();
    }
}
