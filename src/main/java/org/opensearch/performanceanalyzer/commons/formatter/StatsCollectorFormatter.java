/*
 * Copyright OpenSearch Contributors
 * SPDX-License-Identifier: Apache-2.0
 */

package org.opensearch.performanceanalyzer.commons.formatter;

import static org.opensearch.performanceanalyzer.commons.stats.metrics.StatsType.LATENCIES;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.opensearch.performanceanalyzer.commons.stats.eval.Statistics;
import org.opensearch.performanceanalyzer.commons.stats.format.Formatter;
import org.opensearch.performanceanalyzer.commons.stats.measurements.MeasurementSet;

public class StatsCollectorFormatter implements Formatter {
    private StringBuilder metricsBuilder;
    private Map<String, Double> latencyMap = new HashMap<>();

    private String sep = "";
    long startTime;
    long endTime;

    public StatsCollectorFormatter() {
        metricsBuilder = new StringBuilder();
        latencyMap.clear();
    }

    private void format(
            MeasurementSet measurementSet, Statistics aggregationType, String name, Number value) {
        if (Objects.equals(measurementSet.getStatsType(), LATENCIES)) {
            latencyMap.put(measurementSet.getName(), value.doubleValue());
        } else {
            formatStat(metricsBuilder, measurementSet, aggregationType, name, value);
        }
    }

    private void formatStat(
            StringBuilder metricsBuilder,
            MeasurementSet measurementSet,
            Statistics aggregationType,
            String name,
            Number value) {
        metricsBuilder.append(sep);
        metricsBuilder.append(measurementSet.getName()).append("=").append(value);
        if (!measurementSet.getUnit().isEmpty()) {
            metricsBuilder.append(" ").append(measurementSet.getUnit());
        }
        metricsBuilder.append(" ").append("aggr|").append(aggregationType);
        if (!name.isEmpty()) {
            metricsBuilder.append(" ").append("key|").append(name);
        }
        sep = ",";
    }

    @Override
    public void formatNamedAggregatedValue(
            MeasurementSet measurementSet, Statistics aggregationType, String name, Number value) {
        format(measurementSet, aggregationType, name, value);
    }

    @Override
    public void formatAggregatedValue(
            MeasurementSet measurementSet, Statistics aggregationType, Number value) {
        format(measurementSet, aggregationType, "", value);
    }

    @Override
    public void setStartAndEndTime(long start, long end) {
        this.startTime = start;
        this.endTime = end;
    }

    public List<StatsCollectorReturn> getAllMetrics() {
        List<StatsCollectorReturn> list = new ArrayList<>();
        StatsCollectorReturn statsCollectorReturn =
                new StatsCollectorReturn(this.startTime, this.endTime);
        statsCollectorReturn.statsdata.put("Metrics", metricsBuilder.toString());
        statsCollectorReturn.latencies = new HashMap<>(latencyMap);

        list.add(statsCollectorReturn);
        return list;
    }

    public static class StatsCollectorReturn {
        private Map<String, String> statsdata;
        private Map<String, Double> latencies;
        private long startTimeMillis;
        private long endTimeMillis;

        public StatsCollectorReturn(long startTimeMillis, long endTimeMillis) {
            statsdata = new HashMap<>();
            latencies = new HashMap<>();
            this.startTimeMillis = startTimeMillis;
            this.endTimeMillis = endTimeMillis;
        }

        public Map<String, String> getStatsdata() {
            return statsdata;
        }

        public Map<String, Double> getLatencies() {
            return latencies;
        }

        public long getStartTimeMillis() {
            return startTimeMillis;
        }

        public long getEndTimeMillis() {
            return endTimeMillis;
        }

        public boolean isEmpty() {
            return statsdata.isEmpty() && latencies.isEmpty();
        }
    }
}
