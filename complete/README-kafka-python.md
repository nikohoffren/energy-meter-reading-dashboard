# Energy Meter Readings Processing Application

This project demonstrates a modern data pipeline using Spring Boot, Kafka, and Python analytics.

## Architecture

```
[Spring Boot Meter Reading Generator] --(Kafka: meter-readings)--> [Python Analytics Service] --(Kafka: analysis-results)--> [Spring Boot Web UI]
```

- **Spring Boot**: Generates dummy meter readings, sends to Kafka, and displays analytics results.
- **Kafka**: Message broker for decoupling ingestion and analytics.
- **Python**: Consumes meter readings, analyzes them, and sends results back to Kafka.

## How to Run

### 1. Start Kafka (via Docker)

```
docker compose up -d
```

### 2. Start Spring Boot Application

```
cd complete
```

- Maven: `mvn spring-boot:run`
- Gradle: `./gradlew bootRun`

### 3. Start Python Analytics Service

- `cd complete/python-analytics`
- `python3 -m venv venv`
- `source venv/bin/activate`
- `pip install -r requirements.txt`
- `python analytics_service.py`

After you're done working on your project, you can deactivate the virtual environment by simply typing:
- `deactivate`

## Directory Structure

- `complete/` - Spring Boot application
- `complete/python-analytics/` - Python analytics service

---

See each subdirectory for more details.

