/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.collectors;

import com.google.common.annotations.VisibleForTesting;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opensearch.performanceanalyzer.commons.config.PluginSettings;
import org.opensearch.performanceanalyzer.commons.formatter.StatsCollectorFormatter;
import org.opensearch.performanceanalyzer.commons.metrics.MetricsConfiguration;
import org.opensearch.performanceanalyzer.commons.rca.Version;
import org.opensearch.performanceanalyzer.commons.stats.ServiceMetrics;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatExceptionCode;
import org.opensearch.performanceanalyzer.commons.stats.metrics.StatMetrics;

public class StatsCollector extends PerformanceAnalyzerMetricsCollector {
    private static final Logger STATS_LOGGER = LogManager.getLogger("stats_log");
    private static final Logger GENERAL_LOG = LogManager.getLogger(StatsCollector.class);
    public static final String COLLECTOR_NAME = "StatsCollector";
    public static String STATS_TYPE = "plugin-stats-metadata";

    private static final String LOG_ENTRY_INIT =
            "------------------------------------------------------------------------";
    private static final String LOG_ENTRY_END = "EOE";
    private static final String LOG_LINE_BREAK = "\n";
    private static final double MILLISECONDS_TO_SECONDS_DIVISOR = 1000D;

    private static StatsCollector statsCollector = null;
    private final Map<String, String> metadata;
    private Map<String, AtomicInteger> counters = new ConcurrentHashMap<>();
    private final List<StatExceptionCode> defaultExceptionCodes = new Vector<>();
    private Date objectCreationTime = new Date();

    public StatsCollector(String name, int samplingIntervalMillis, Map<String, String> metadata) {
        super(
                samplingIntervalMillis,
                name,
                StatMetrics.STAT_COLLECTOR_EXECUTION_TIME,
                StatExceptionCode.STATS_COLLECTOR_ERROR);
        this.metadata = metadata;
        addRcaVersionMetadata(this.metadata);
        defaultExceptionCodes.add(StatExceptionCode.TOTAL_ERROR);
    }

    private StatsCollector(Map<String, String> metadata) {
        this(
                COLLECTOR_NAME,
                MetricsConfiguration.CONFIG_MAP.get(StatsCollector.class).samplingInterval,
                metadata);
    }

    public static StatsCollector instance() {
        if (statsCollector == null) {
            synchronized (StatsCollector.class) {
                if (statsCollector == null) {
                    statsCollector =
                            new StatsCollector(
                                    loadMetadata(
                                            PluginSettings.instance()
                                                    .getSettingValue(STATS_TYPE, STATS_TYPE)));
                }
            }
        }

        return statsCollector;
    }

    @Override
    public void collectMetrics(long startTime) {
        Map<String, AtomicInteger> currentCounters = counters;
        counters = new ConcurrentHashMap<>();

        for (StatExceptionCode statExceptionCode : defaultExceptionCodes) {
            currentCounters.putIfAbsent(statExceptionCode.toString(), new AtomicInteger(0));
        }

        /**
         * Each run StatsCollector collectMetric(scheduled every 60s) emits 2 entries. The first
         * entry via {@link writeStats} writes counters, and the second {@link
         * collectAndWriteRcaStats} writes timers and metrics.
         */
        writeStats(
                metadata,
                currentCounters,
                null,
                null,
                objectCreationTime.getTime(),
                new Date().getTime());
        collectAndWriteRcaStats();
        objectCreationTime = new Date();
    }

    private void collectAndWriteRcaStats() {
        boolean hasNext;
        do {
            StatsCollectorFormatter formatter = new StatsCollectorFormatter();
            hasNext = ServiceMetrics.STATS_REPORTER.getNextReport(formatter);
            for (StatsCollectorFormatter.StatsCollectorReturn statsReturn :
                    formatter.getAllMetrics()) {
                if (!statsReturn.isEmpty()) {
                    logStatsRecord(
                            null,
                            statsReturn.getStatsdata(),
                            statsReturn.getLatencies(),
                            statsReturn.getStartTimeMillis(),
                            statsReturn.getEndTimeMillis());
                }
            }
        } while (hasNext);
    }

    @VisibleForTesting
    public Map<String, AtomicInteger> getCounters() {
        return counters;
    }

    public void logException(StatExceptionCode statExceptionCode) {
        incCounter(statExceptionCode.toString());
        incErrorCounter();
    }

    public void logStatsRecord(
            Map<String, AtomicInteger> counterData,
            Map<String, String> statsData,
            Map<String, Double> latencyData,
            long startTimeMillis,
            long endTimeMillis) {
        writeStats(metadata, counterData, statsData, latencyData, startTimeMillis, endTimeMillis);
    }

    private void addRcaVersionMetadata(Map<String, String> metadata) {
        metadata.put(Version.RCA_VERSION_STR, Version.getRcaVersion());
    }

    private static Map<String, String> loadMetadata(String fileLocation) {
        Map<String, String> retVal = new ConcurrentHashMap<>();

        if (fileLocation != null) {
            Properties props = new Properties();

            try (InputStream input =
                    new FileInputStream(
                            PluginSettings.instance().getConfigFolderPath() + fileLocation); ) {
                props.load(input);
            } catch (Exception ex) {
                GENERAL_LOG.error(
                        "Error in loading metadata for folderLocation: {}, fileLocation: {}",
                        PluginSettings.instance().getConfigFolderPath(),
                        fileLocation,
                        ex);
            }

            props.forEach((key, value) -> retVal.put((String) key, (String) value));
        }

        return retVal;
    }

    private void incCounter(String counterName) {
        AtomicInteger val = counters.putIfAbsent(counterName, new AtomicInteger(1));
        if (val != null) {
            val.getAndIncrement();
        }
    }

    private void incErrorCounter() {
        AtomicInteger all_val =
                counters.putIfAbsent(
                        StatExceptionCode.TOTAL_ERROR.toString(), new AtomicInteger(1));
        if (all_val != null) {
            all_val.getAndIncrement();
        }
    }

    private static void writeStats(
            Map<String, String> metadata,
            Map<String, AtomicInteger> counters,
            Map<String, String> statsdata,
            Map<String, Double> latencies,
            long startTimeMillis,
            long endTimeMillis) {

        StringBuilder builder = new StringBuilder();
        builder.append(LOG_ENTRY_INIT + LOG_LINE_BREAK);
        logValues(metadata, builder);
        // Stats Data
        logValues(statsdata, builder);
        logTimeMetrics(startTimeMillis, endTimeMillis, builder);

        // Timers and Counters
        Optional.ofNullable(latencies)
                .ifPresent(
                        e -> latencies.put("total-time", (double) endTimeMillis - startTimeMillis));
        addEntry("Timing", getLatencyMetrics(latencies), builder);
        addEntry("Counters", getCountersString(counters), builder);

        builder.append(LOG_ENTRY_END);
        STATS_LOGGER.debug(builder.toString());
    }

    private static String getCountersString(Map<String, AtomicInteger> counters) {
        StringBuilder builder = new StringBuilder();
        if (counters == null || counters.isEmpty()) {
            return "";
        }
        for (Map.Entry<String, AtomicInteger> counter : counters.entrySet()) {
            builder.append(counter.getKey())
                    .append("=")
                    .append(counter.getValue().get())
                    .append(",");
        }
        builder.delete(builder.length() - 1, builder.length());
        return builder.toString();
    }

    private static void logTimeMetrics(
            long startTimeMillis, long endTimeMillis, StringBuilder builder) {
        // Date Example: Wed, 20 Mar 2013 15:07:51 GMT
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ROOT);
        addEntry(
                "StartTime",
                String.format(
                        Locale.ROOT, "%.3f", startTimeMillis / MILLISECONDS_TO_SECONDS_DIVISOR),
                builder);
        addEntry("EndTime", dateFormat.format(new Date(endTimeMillis)), builder);
        addEntry("Time", (endTimeMillis - startTimeMillis) + " msecs", builder);
    }

    private static void logValues(Map<String, String> values, StringBuilder sb) {
        if (values == null) {
            return;
        }
        for (Map.Entry<String, String> entry : values.entrySet()) {
            addEntry(entry.getKey(), entry.getValue(), sb);
        }
    }

    private static void addEntry(String key, Object value, StringBuilder sb) {
        sb.append(key).append('=').append(value).append(LOG_LINE_BREAK);
    }

    private static String getLatencyMetrics(Map<String, Double> values) {
        StringBuilder builder = new StringBuilder();
        if (values == null || values.isEmpty()) {
            return "";
        }
        for (Map.Entry<String, Double> value : values.entrySet()) {
            getTimingInfo(value.getKey(), value.getValue(), builder);
        }
        builder.delete(builder.length() - 1, builder.length());
        return builder.toString();
    }

    private static void getTimingInfo(String timerName, double latency, StringBuilder builder) {
        getTimingInfo(timerName, latency, builder, 1);
    }

    private static void getTimingInfo(
            String timerName, double latency, StringBuilder builder, int attempts) {
        builder.append(timerName)
                .append(":")
                .append(latency)
                .append("/")
                .append(attempts)
                .append(",");
    }
}
