---
transition: slide-up
---

## Логирование
<br>

- Все Spring-сервисы логируют в **stdout** в формате **JSON**, добавляя **traceId** из **MDC**
- **Promtail** собирает и преобразует логи со всех сервисов в кластере
- **Loki** хранит преобразованные логи
- **Grafana** позволяет использовать Loki в качестве источника данных для поиска по логам

---
transition: slide-up
---

## Логирование
<br>

<img src="/loki.png" style="height: 430px;">

---
transition: slide-up
---

## Трейсинг
<br>

- Во всех Spring-сервисах настроен трейсинг
- Для интеграции трейсинга в Spring-приложение используется бридж между Micrometer и OpenTelemetry
- Трейсы экспортируются в формате OpenTelemetry
- В качестве коллектора трейсов используется Jaeger

---
transition: slide-up
---

## Трейсинг

<br>
<img src="/jaeger-ui.png">


---
transition: slide-up
---

## Метрики
<br>

- Во всех Spring-сервисах настроена отдача метрик в формате **Prometheus** через эндпоинт актуатора
- В качестве коллектора метрик используется **Prometheus**
- Для анализа и визуализации метрик используется **Grafana**

---
transition: slide-down
---

## Метрики


<br>
<img src="/prometheus.png">
