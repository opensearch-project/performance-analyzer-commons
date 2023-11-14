### Performance Analyzer Commons
In `src/main/java/org/opensearch/performanceanalyzer/commons`:
- Classes in `hwnet`, `jvm` and `os` collect metrics by interfacing with external systems (the OS or Java).
- Classes in `metrics_generator/linux` wrap over collection logic and store results in `Map`s that are refreshed when `addSample` is called.
- Classes in `metrics_generator` contain interfaces to abstract over classes in `metrics_generator/linux`.
- Classes in `collectors`
    - suffixed by `Metrics` or `Summary` extend `MetricStatus`. These classes describe one measurement. For example, `DiskMetrics` contains the name, utilization, service rate, etc.
    - suffixed by `Collector` extend `PerformanceAnalyzerMetricsCollector` and implement `MetricsProcessor`.
        - `PerformanceAnalyzerMetricsCollector` implements `Runnable` and contains common variables like `value` where a collector stores serialized metrics.
        - A collector is triggered through  `PerformanceAnalyzerMetricsCollector.collectMetrics`.
            1. The collector will store serialized data in the `value` variable and then call `MetricsProcessor.saveMetricValues`.
            2. `saveMetricValues` calls `PerformanceAnalyzerMetrics.emitMetric` that creates an `Event` from the serialized data and adds it to a static queue (`PerformanceAnalyzerMetrics.metricQueue`) shared across all collectors. 

- `collectors/StatsCollector` is a special collector that does *not* implement `MetricsProcessor`. It writes to the `"stats_log"` log when `collectMetrics` is called.

- `metrics/PerformanceAnalyzerMetrics` contains logic for mapping events to files in the temporary filesystem used to store events, and the queue that contains events emitted by collectors.
- `metrics/MetricsProcessor` is an interface used to obtain information about the key of a particular measurement and pushing it to the queue with `saveMetricValues`.
    - Note: `MetricsProcessor.getMetricsPath` provides the full identifier for a metric measurement: the epoch + key (for some collectors, the key is the corresponding string in `PerformanceAnalyzerMetrics`)
    - Reading the path returned by `getMetricsPath` via `getMetric` will return the value of a metric.

- `collectors/ScheduledMetricCollectorsExecutor` runs collectors periodically and updates their states based on whether they are still running at the next iteration (`HEALTHY`, `SLOW`, etc. defined in `PerformanceAnalyzerMetricsCollector`).

- `event_process/EventLogFileHandler` contains logic for writing events to files in the tmpfs. Used by  `EventLogQueueProcessor`.

- `config/overrides/ConfigOverrides` and `config/overrides/ConfigOverridesWrapper` can be used to piece-wise enable and disable components. They are used by the plugin in the Performance Analyzer repository.

- `stats` contains code to aggregate and report metrics, along with service metrics such as latency of executions and occurences of events.
    - `ServiceMetrics` creates multiple `SampleAggregator`s that track useful measurements. For example, `STAT_METRICS_AGGREGATOR`, `ERRORS_AND_EXCEPTIONS_AGGREGATOR`, etc.
    - `stats/SampleAggregator` is a class that tracks multiple related measurements and can dump them to a formatter. SampleAggregator can calculate multiple statistics (Min, Max, etc.) for a single measurement.
        - `SampleAggregator`s are created in `ServiceMetrics` by passing in all the variants of an enum that implement `MeasurementSet`.
        - For example, 
            - `CollectorMetrics` is an enum that implements `MeasurementSet`.
            - It contains multiple measurements that should be tracked:
                - `COLLECTORS_SKIPPED("CollectorSkippedCount", "namedCount", StatsType.STATS_DATA, Collections.singletonList(Statistics.NAMED_COUNTERS)),`
            - Code that wants to update the measurement calls to the corresponding `SampleAggregator` and passes the key of the measurement to update: `ServiceMetrics.COMMONS_STAT_METRICS_AGGREGATOR.updateStat(
                                    StatMetrics.COLLECTORS_MUTED, collector.getCollectorName(), 1);`
    - `ExceptionsAndErrors` seems to only record certain errors in RCA; collectors and other code instead use `StatExceptionCode` which does not write to any `SampleAggregator` but instead directly writes to counters maintained in `StatsCollector`.
    - `StatsCollector` is responsible for processing metrics aggregated at `ServiceMetrics` through `collectMetrics`.
        - Every `collectMetrics` call emits 2 entries into `STATS_LOGGER.debug`:
            - The first records the state of the counters that are maintained directly in `StatsCollector`; all the enum variants of `StatExceptionCode` are converted to strings and serve as keys to the counters.
            - The second entry records formatted reports from all the `SampleAggregators` inside `ServiceMetrics`.
    - `StatsReporter` wraps over a list of `SampleAggregator`s and writes out their reports to a formatter passed in `getNextReport`. `ServiceMetrics` uses this class to wrap over the multiple `SampleAggregator`s it abstracts over.