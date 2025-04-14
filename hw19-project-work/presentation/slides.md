---
theme: seriph
transition: fade-out
class: text-center
author: Alexander Sviridov
presenter: false
monaco: false
record: false
colorSchema: light
---

# 📒 Platform for Publishing and Discovering Web Novels

An analogue of www.royalroad.com

---

# Introduction

The application allows:
- **Authors**
  - to create, edit, and delete their web novels
  - to publish chapters of their web novels

- **Readers** 
  - to search for suitable works using various criteria
  - to read web novels
  - to rate web novels

---
transition: slide-up
---

# Application Architecture

The application is implemented using a microservices architecture, applying modern cloud-native development standards, and thus offers strong _scalability_, _fault tolerance_, and _observability_.

- The application guarantees message delivery using the Outbox pattern implemented via CDC with MongoDB Change Streams.
- The application uses an idempotent approach for handling POST requests and Kafka consumer messages.
- The application implements fault tolerance mechanisms:
  - Circuit Breaker (for HTTP status 503 Service Unavailable)
  - IP-based Rate Limiter (Token Bucket)
  - Time Limiter (Timeout)
  - Retry (for transient HTTP statuses)

---
src: ./pages/observability.md
---

---
src: ./pages/с4.md
---
