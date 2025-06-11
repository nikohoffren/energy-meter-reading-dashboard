package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class WebSocketService {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketService.class);

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendMeterReading(MeterReading reading) {
        try {
            if (reading == null) {
                logger.warn("Attempted to send null meter reading");
                return;
            }
            logger.info("Sending meter reading: {}", reading);
            messagingTemplate.convertAndSend("/topic/meter-readings", reading);
        } catch (Exception e) {
            logger.error("Error sending meter reading: {}", e.getMessage(), e);
        }
    }

    public void sendAnomalyAlert(MeterReading reading, double anomalyScore) {
        try {
            if (reading == null) {
                logger.warn("Attempted to send anomaly alert with null reading");
                return;
            }
            logger.info("Sending anomaly alert for reading: {} with score: {}", reading, anomalyScore);
            AnomalyAlert alert = new AnomalyAlert(reading, anomalyScore);
            messagingTemplate.convertAndSend("/topic/anomalies", alert);
        } catch (Exception e) {
            logger.error("Error sending anomaly alert: {}", e.getMessage(), e);
        }
    }
}
