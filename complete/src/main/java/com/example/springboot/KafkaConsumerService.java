package com.example.springboot;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;

@Service
public class KafkaConsumerService {
    private final List<String> latestResults = Collections.synchronizedList(new LinkedList<>());
    private static final int MAX_RESULTS = 20;

    @Autowired
    private WebSocketService webSocketService;

    @Autowired
    private ObjectMapper objectMapper;

    @KafkaListener(topics = "analysis-results", groupId = "spring-boot-group")
    public void listenAnalysisResults(ConsumerRecord<String, String> record) {
        try {
            String value = record.value();
            synchronized (latestResults) {
                if (latestResults.size() >= MAX_RESULTS) {
                    latestResults.remove(0);
                }
                latestResults.add(value);
            }

            // Parse the analysis result and send via WebSocket
            JsonNode jsonNode = objectMapper.readTree(value);
            MeterReading reading = new MeterReading();
            reading.setId(jsonNode.get("id").asText());
            reading.setTimestamp(jsonNode.get("timestamp").asLong());
            reading.setValue(jsonNode.get("value").asDouble());

            webSocketService.sendMeterReading(reading);

            // If anomaly score is high, send an alert
            double anomalyScore = jsonNode.get("anomalyScore").asDouble();
            if (anomalyScore > 0.8) {
                webSocketService.sendAnomalyAlert(reading, anomalyScore);
            }

            System.out.println("Received analysis result: " + value);
        } catch (Exception e) {
            System.err.println("Error processing analysis result: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<String> getLatestResults() {
        synchronized (latestResults) {
            return new LinkedList<>(latestResults);
        }
    }
}
