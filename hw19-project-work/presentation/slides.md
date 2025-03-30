---
theme: seriph
transition: fade-out
class: text-center
author: Свиридов Александр
remoteAssets: true
---

# 📒 Платформа для публикации и поиска веб-новелл

Аналог www.royalroad.com

---

# Введение

Приложение позволяет:
- **Авторам** публиковать веб-новеллы и главы к ним
- **Читателям** искать подходящие произведения по различным критериям
- **Читателям** выставлять оценки новеллам

---
transition: slide-up
---

# Архитектура

Приложение реализовано в микросервисной архитектуре 
и за счет этого обладает хорошей _масштабируемостью_ и _отказоустойчивостью_

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
transition: slide-down
---

## Метрики
<br>

- Во всех Spring-сервисах настроена отдача метрик в формате **Prometheus** через эндпоинт актуатора
- В качестве коллектора метрик используется **Prometheus**
- Для анализа и визуализации метрик используется **Grafana**

---

## dasdsa

**Были применены такие подходы, как**:
- Идемпотентная обработка сообщений в Kafka-консьюмере
- Идемпотентная обработка POST-запросов с помощью заголовка X-Idempotency-Key
- Outbox с механизмом Change Data Capture на основе механизма Change Stream в MongoDB
- Транзакции в Redis для идемпотентной и атомарной модификации нескольких ключей 
- Clean Architecture

---
layout: image
image: /structurizr-1-gateway.svg
title: Gateway system container level
backgroundSize: contain
---

---
layout: image
image: /structurizr-1-user.svg
title: User system container level
backgroundSize: contain
---

---
layout: image
image: /structurizr-1-novel.svg
title: Novel system container level
backgroundSize: contain
---

---
layout: image
image: /structurizr-1-rating.svg
title: Rating system container level
backgroundSize: contain
---

---
layout: image
image: /structurizr-1-search.svg
title: Search system container level
backgroundSize: contain
---
