package com.example.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ResultsController {
    @Autowired
    private KafkaConsumerService kafkaConsumerService;

    @GetMapping("/api/results")
    public List<String> getResults() {
        return kafkaConsumerService.getLatestResults();
    }
}
