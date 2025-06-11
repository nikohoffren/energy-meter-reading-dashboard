package com.example.springboot;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AnalysisResult {
    private MeterReading reading;
    private double anomalyScore;
}
