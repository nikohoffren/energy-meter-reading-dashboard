# Energy Meter Analytics Dashboard

A real-time energy meter analytics dashboard built with Spring Boot, Kafka, and Python. This project demonstrates a modern data pipeline for processing and visualizing energy meter readings with anomaly detection.

## Features

- Real-time meter reading generation
- Anomaly detection using Python analytics
- WebSocket-based real-time updates
- Interactive dashboard with charts and alerts
- Kafka-based message processing pipeline

## Architecture

```
[Spring Boot Meter Reading Generator] --(Kafka: meter-readings)--> [Python Analytics Service] --(Kafka: analysis-results)--> [Spring Boot Web UI]
```

## Prerequisites

- Java 17 or later
- Python 3.8 or later
- Docker and Docker Compose
- Maven or Gradle

## Setup and Running

### 1. Start Kafka (via Docker)

```bash
docker compose up -d
```

### 2. Start Spring Boot Application

```bash
cd complete
./mvnw spring-boot:run
```

### 3. Start Python Analytics Service

```bash
cd complete/python-analytics
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
python analytics_service.py
```

### 4. Access the Dashboard

Open your browser and navigate to:

```
http://localhost:8080
```

## Project Structure

- `complete/` - Spring Boot application
  - `src/main/java/` - Java source code
  - `src/main/resources/` - Static resources and configuration
  - `python-analytics/` - Python analytics service

## Technologies Used

- Spring Boot
- Spring WebSocket
- Spring Kafka
- Python
- Kafka
- WebSocket
- Chart.js
- Bootstrap

## License

This project is licensed under the MIT License - see the LICENSE file for details.
