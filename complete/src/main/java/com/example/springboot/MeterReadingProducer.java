package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MeterReadingProducer {
    private static final String TOPIC = "meter-readings";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Scheduled(fixedRate = 5000)
    public void sendDummyReading() {
        MeterReading reading = new MeterReading(
                UUID.randomUUID().toString(),
                System.currentTimeMillis(),
                Math.random() * 100
        );
        kafkaTemplate.send(TOPIC, reading);
        System.out.println("Produced: " + reading.getId() + ", value: " + reading.getValue());
    }
}
