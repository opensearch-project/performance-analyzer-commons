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