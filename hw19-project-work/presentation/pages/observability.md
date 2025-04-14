---
transition: slide-up
---

## Logging
<br>

- All Spring services log to **stdout** in **JSON** format, adding a **traceId** from the **MDC**
- **Promtail** collects and transforms logs from all services in the cluster
- **Loki** stores the transformed logs
- **Grafana** enables log search by using Loki as a data source

---
transition: slide-up
---

## Logging
<br>

<img src="/loki.png" style="height: 430px;">

---
transition: slide-up
---

## Tracing
<br>

- Tracing is configured in all Spring services
- A bridge between Micrometer and OpenTelemetry is used to integrate tracing into Spring applications
- Traces are exported in the OpenTelemetry format
- **Jaeger** is used as the trace collector

---
transition: slide-up
---

## Tracing

<br>
<img src="/jaeger-ui.png">


---
transition: slide-up
---

## Metrics
<br>

- Metrics export in **Prometheus** format is configured in all Spring services via the actuator endpoint
- **Prometheus** is used as the metrics collector
- **Grafana** is used for metrics analysis and visualization

---

## Metrics

<br>
<img src="/prometheus.png">
