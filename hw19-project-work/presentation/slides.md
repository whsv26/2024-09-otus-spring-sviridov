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
- **Авторам**
  - создавать, редактировать и удалять свои веб-новеллы
  - публиковать главы своих веб-новелл

- **Читателям** 
  - искать подходящие произведения по различным критериям
  - читать веб-новеллы
  - выставлять оценки веб-новеллам

---
transition: slide-up
---

# Архитектура приложения

Приложение реализовано в микросервисной архитектуре с применением современных стандартов разработки облачных приложений и за счет этого обладает хорошей _масштабируемостью_, _отказоустойчивостью_ и _наблюдаемостью_.

- Приложение предоставляет гарантии доставки сообщений с помощью паттерна Outbox, реализованного через механизм CDC посредством Change Stream'ов в MongoDB. 
- Приложение использует подход идемпотентной обработки POST-запросов и сообщений в Kafka-консьюмерах
- В приложении используются механизмы отказоустойчивости
  - Circuit Breaker (Для HTTP-статуса 503 Service Unavailable)
  - Rate Limiter по IP (Token Bucket)
  - Time Limiter (Timeout)
  - Retry (Для транзиентных HTTP-статусов)

---
src: ./pages/observability.md
---

---

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
