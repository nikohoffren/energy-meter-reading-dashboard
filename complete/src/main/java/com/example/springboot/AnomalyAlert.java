package com.example.springboot;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnomalyAlert {
    private MeterReading reading;
    private double anomalyScore;
    private String timestamp;

    public AnomalyAlert(MeterReading reading, double anomalyScore) {
        this.reading = reading;
        this.anomalyScore = anomalyScore;
        this.timestamp = String.valueOf(System.currentTimeMillis());
    }
}
